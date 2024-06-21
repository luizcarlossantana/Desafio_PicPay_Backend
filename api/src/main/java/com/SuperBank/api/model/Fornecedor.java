package com.SuperBank.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String razaoSocial;
    private String fantasia;
    private String cnpj;
    private String estado;
    private String cidade;
    private String telefone;
    private String email;
    private String observacao;
    private boolean ativo;
    private boolean lixeira;
}
