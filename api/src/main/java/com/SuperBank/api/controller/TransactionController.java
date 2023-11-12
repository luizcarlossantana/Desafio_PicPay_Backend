package com.SuperBank.api.controller;

import com.SuperBank.api.model.Transaction;
import com.SuperBank.api.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionRepository repository;

    public ResponseEntity<Transaction> creatTransaction(@RequestBody Transaction transaction){

        Transaction createdTransaction = repository.save(transaction);

        return  ResponseEntity.status(200).body(createdTransaction);
    }
}
