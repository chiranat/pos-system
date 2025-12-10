package com.chiranat.pos.dto;

import com.chiranat.pos.model.Transaction;
import lombok.Data;

@Data
public class PaymentRequest {
    private Transaction.PaymentMethod paymentMethod;
}
