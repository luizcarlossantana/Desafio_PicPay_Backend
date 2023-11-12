package com.SuperBank.api.controller;

import com.SuperBank.api.model.Transaction;
import com.SuperBank.api.model.dtos.TransactionDTO;
import com.SuperBank.api.repository.TransactionRepository;
import com.SuperBank.api.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionDTO> creatTransaction(@RequestBody TransactionDTO transactionDTO){

        TransactionDTO createdTransaction = service.creatTransaction(transactionDTO);

        return  ResponseEntity.status(200).body(createdTransaction);
    }
}
