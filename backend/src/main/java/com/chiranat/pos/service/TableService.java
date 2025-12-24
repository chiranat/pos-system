package com.chiranat.pos.service;

import com.chiranat.pos.dto.DiningSessionDto;
import com.chiranat.pos.dto.OpenTableRequest;
import com.chiranat.pos.dto.TableDetailDto;
import com.chiranat.pos.dto.TableDto;
import com.chiranat.pos.exception.ResourceNotFoundException;
import com.chiranat.pos.model.DiningSession;
import com.chiranat.pos.model.Order;
import com.chiranat.pos.model.TableEntity;
import com.chiranat.pos.repository.DiningSessionRepository;
import com.chiranat.pos.repository.OrderRepository;
import com.chiranat.pos.repository.TableRepository;
import com.chiranat.pos.util.QrCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableRepository tableRepository;
    private final DiningSessionRepository diningSessionRepository;
    private final OrderRepository orderRepository;
    private final QrCodeGenerator qrCodeGenerator;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    public List<TableDto> getAllTables() {
        return tableRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TableDto createTable(TableDto tableDto) {
        TableEntity table = TableEntity.builder()
                .tableNumber(tableDto.getTableNumber())
                .capacity(tableDto.getCapacity())
                .status(TableEntity.TableStatus.AVAILABLE)
                .build();
        return mapToDto(tableRepository.save(table));
    }

    public TableDto updateTable(UUID id, TableDto tableDto) {
        TableEntity table = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));
        
        table.setTableNumber(tableDto.getTableNumber());
        table.setCapacity(tableDto.getCapacity());
        
        return mapToDto(tableRepository.save(table));
    }

    public void deleteTable(UUID id) {
        TableEntity table = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));

        if (table.getStatus() == TableEntity.TableStatus.OCCUPIED) {
            throw new IllegalStateException("Cannot delete an occupied table. Please close the session first.");
        }

        // Rename table number to allow reuse of the name
        table.setTableNumber(table.getTableNumber() + "_DELETED_" + System.currentTimeMillis());
        tableRepository.save(table);
        
        tableRepository.deleteById(id);
    }

    @Transactional
    public DiningSessionDto openTable(UUID tableId, OpenTableRequest request) {
        TableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        if (table.getStatus() == TableEntity.TableStatus.OCCUPIED) {
            throw new RuntimeException("Table is already occupied");
        }

        // Generate Access Token
        String accessToken = UUID.randomUUID().toString();

        DiningSession session = DiningSession.builder()
                .table(table)
                .accessToken(accessToken)
                .customerCount(request.getCustomerCount())
                .status(DiningSession.SessionStatus.ACTIVE)
                .build();

        diningSessionRepository.save(session);

        // Update Table Status
        table.setStatus(TableEntity.TableStatus.OCCUPIED);
        tableRepository.save(table);

        return mapToSessionDto(session);
    }

    @Transactional
    public void closeTable(UUID tableId) {
        TableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        DiningSession session = diningSessionRepository.findByTableIdAndStatus(tableId, DiningSession.SessionStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active session for this table"));

        session.setStatus(DiningSession.SessionStatus.COMPLETED);
        session.setEndTime(java.time.LocalDateTime.now());
        diningSessionRepository.save(session);

        table.setStatus(TableEntity.TableStatus.AVAILABLE);
        tableRepository.save(table);
    }

    private TableDto mapToDto(TableEntity table) {
        TableDto dto = new TableDto();
        dto.setId(table.getId());
        dto.setTableNumber(table.getTableNumber());
        dto.setCapacity(table.getCapacity());
        dto.setStatus(table.getStatus().name());

        if (table.getStatus() == TableEntity.TableStatus.OCCUPIED) {
             diningSessionRepository.findByTableIdAndStatus(table.getId(), DiningSession.SessionStatus.ACTIVE)
                     .ifPresent(session -> dto.setCurrentSession(mapToSessionDto(session)));
        }
        return dto;
    }

    private DiningSessionDto mapToSessionDto(DiningSession session) {
        DiningSessionDto dto = new DiningSessionDto();
        dto.setId(session.getId());
        dto.setAccessToken(session.getAccessToken());
        dto.setStartTime(session.getStartTime());
        dto.setCustomerCount(session.getCustomerCount());
        dto.setStatus(session.getStatus().name());

        // Generate QR Code URL
        String menuUrl = frontendUrl + "/menu?token=" + session.getAccessToken();
        try {
            String qrCodeBase64 = qrCodeGenerator.generateQRCodeImage(menuUrl, 200, 200);
            dto.setQrCodeUrl("data:image/png;base64," + qrCodeBase64);
        } catch (Exception e) {
            dto.setQrCodeUrl(null);
        }

        return dto;
    }

    public TableDetailDto getTableDetails(UUID tableId) {
        TableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));

        DiningSession activeSession = diningSessionRepository.findByTableIdAndStatus(tableId, DiningSession.SessionStatus.ACTIVE)
                .orElse(null);

        List<Order> orders = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        DiningSessionDto sessionDto = null;

        if (activeSession != null) {
            sessionDto = mapToSessionDto(activeSession);
            orders = orderRepository.findBySessionId(activeSession.getId());
            
            // Calculate total
            for (Order order : orders) {
                if (order.getStatus() != Order.OrderStatus.CANCELLED) {
                    for (com.chiranat.pos.model.OrderItem item : order.getOrderItems()) {
                        totalAmount = totalAmount.add(item.getSubtotal());
                    }
                }
            }
        }

        return TableDetailDto.builder()
                .table(mapToDto(table))
                .currentSession(sessionDto)
                .orders(orders)
                .totalAmount(totalAmount)
                .build();
    }
}
