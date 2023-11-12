package com.SuperBank.api.model.dtos;

import lombok.Data;

import java.util.UUID;
@Data
public class CompanyDTO {

    private UUID id;
    private String cnpj;
    private String name;
    private String email;
}
