package com.SuperBank.api.service;

import com.SuperBank.api.model.Transaction;
import com.SuperBank.api.model.dtos.TransactionDTO;
import com.SuperBank.api.repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.MarshalledObject;
import java.time.LocalDateTime;
@Service
public class TransactionService {

    @Autowired
    TransactionRepository repository;

    @Autowired
    ModelMapper modelMapper;

    public TransactionDTO creatTransaction(TransactionDTO transactionDTO){

        transactionDTO.setCreationDate(LocalDateTime.now());

        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
        Transaction createdTransaction = repository.save(transaction);
        transactionDTO = modelMapper.map(createdTransaction, TransactionDTO.class);

        return transactionDTO;


    }
}
