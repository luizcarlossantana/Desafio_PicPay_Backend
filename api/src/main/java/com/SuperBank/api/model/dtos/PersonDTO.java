package com.SuperBank.api.model.dtos;

import lombok.Data;

import java.util.UUID;
@Data
public class PersonDTO {

    private UUID id;
    private String cpf;
    private String name;
    private String email;
}
