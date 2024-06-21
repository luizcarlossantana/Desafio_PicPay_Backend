package com.SuperBank.api;

import com.SuperBank.api.model.Fornecedor;
import com.SuperBank.api.repository.FornecedorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FornecedorTest {

    @Autowired
    FornecedorRepository fornecedorRepository;
    @Test
    public void criarFornecedor(){

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setAtivo(true);
        fornecedor.setCnpj("098765992");
        fornecedor.setRazaoSocial(" Assai Varejo");

        fornecedorRepository.save(fornecedor);
    }
}
