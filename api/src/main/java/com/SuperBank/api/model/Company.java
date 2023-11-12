package com.SuperBank.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Company extends User{

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;
    private String cnpj;


}
