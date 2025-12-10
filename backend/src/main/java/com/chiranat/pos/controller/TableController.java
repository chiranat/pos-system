package com.chiranat.pos.controller;

import com.chiranat.pos.dto.DiningSessionDto;
import com.chiranat.pos.dto.OpenTableRequest;
import com.chiranat.pos.dto.TableDetailDto;
import com.chiranat.pos.dto.TableDto;
import com.chiranat.pos.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @GetMapping
    @PreAuthorize("hasRole('WAITER') or hasRole('ADMIN')")
    public ResponseEntity<List<TableDto>> getAllTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }

    @GetMapping("/{id}/details")
    @PreAuthorize("hasRole('WAITER') or hasRole('ADMIN')")
    public ResponseEntity<TableDetailDto> getTableDetails(@PathVariable UUID id) {
        return ResponseEntity.ok(tableService.getTableDetails(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TableDto> createTable(@RequestBody TableDto tableDto) {
        return ResponseEntity.ok(tableService.createTable(tableDto));
    }

    @PostMapping("/{id}/open")
    @PreAuthorize("hasRole('WAITER') or hasRole('ADMIN')")
    public ResponseEntity<DiningSessionDto> openTable(@PathVariable UUID id, @RequestBody OpenTableRequest request) {
        return ResponseEntity.ok(tableService.openTable(id, request));
    }

    @PostMapping("/{id}/close")
    @PreAuthorize("hasRole('WAITER') or hasRole('ADMIN')")
    public ResponseEntity<Void> closeTable(@PathVariable UUID id) {
        tableService.closeTable(id);
        return ResponseEntity.ok().build();
    }
}
