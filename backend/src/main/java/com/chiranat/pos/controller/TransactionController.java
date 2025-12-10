package com.chiranat.pos.controller;

import com.chiranat.pos.dto.PaymentRequest;
import com.chiranat.pos.model.Transaction;
import com.chiranat.pos.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/payment/{tableId}")
    @PreAuthorize("hasRole('WAITER') or hasRole('ADMIN')")
    public ResponseEntity<Transaction> processPayment(@PathVariable UUID tableId, @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(transactionService.processPayment(tableId, request));
    }
}
