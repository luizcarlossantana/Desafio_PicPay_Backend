package com.SuperBank.api.controller;

import com.SuperBank.api.model.Fornecedor;
import com.SuperBank.api.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("fornecedor")
public class FornecedorController {

    @Autowired
    FornecedorRepository fornecedorRepository;

    @GetMapping("/buscar")
    public ResponseEntity<List<Fornecedor>> buscar(
            @RequestParam(required = false) Boolean ativo
    ){

        if (ativo == null) {
            return ResponseEntity.status(HttpStatus.OK).body(fornecedorRepository.findAll());
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(fornecedorRepository.findByAtivo(ativo));
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Fornecedor> atualizar(@RequestBody Fornecedor fornecedor, @PathVariable UUID id){
       Optional<Fornecedor> fornecedorBuscado = fornecedorRepository.findById(id);
       fornecedorBuscado.get().setRazaoSocial(fornecedor.getRazaoSocial());
       fornecedorBuscado.get().setAtivo(fornecedor.isAtivo());
       fornecedorBuscado.get().setLixeira(fornecedor.isLixeira());

        return ResponseEntity.status(HttpStatus.OK).body(fornecedorRepository.save(fornecedorBuscado.get()));
    }
    @PostMapping
    public ResponseEntity<Fornecedor> criar(@RequestBody Fornecedor fornecedor){
        return ResponseEntity.status(HttpStatus.OK).body(fornecedorRepository.save(fornecedor));
    }
}
