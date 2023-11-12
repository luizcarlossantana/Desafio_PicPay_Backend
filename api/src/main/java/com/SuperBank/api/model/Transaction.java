package com.SuperBank.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;
    private Double transactionValue;
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "idSender")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "idReceiver")
    private User receiver;

}
