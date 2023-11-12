package com.SuperBank.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Company {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;
    private String cnpj;
    private String name;
    private String email;


}
