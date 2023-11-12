package com.SuperBank.api.model.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
public class TransactionDTO {

    private UUID id;
    private Double transactionValue;
    private LocalDateTime creationDate;
    private UUID sender;
    private UUID receiver;
}
