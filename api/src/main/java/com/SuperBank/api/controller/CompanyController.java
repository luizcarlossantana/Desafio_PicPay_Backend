package com.SuperBank.api.controller;

import com.SuperBank.api.model.dtos.CompanyDTO;
import com.SuperBank.api.repository.InsumoRepository;
import com.SuperBank.api.service.CompanyService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    CompanyService service;
    @Autowired
    InsumoRepository insumoRepository;




    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CompanyDTO> creatCompany(@RequestBody CompanyDTO companyDTO){

        CompanyDTO createdCompany = service.creatCompany(companyDTO);

        return ResponseEntity.status(200).body(createdCompany);

    }
    @PostMapping("/upload")
    public ResponseEntity<String> importFile(@RequestParam("file") MultipartFile file) {
        try {
            // Salva o arquivo em "C:\\Users\\luizc\\Documents" e obtém o caminho do arquivo
            String filePath = saveFile(file);

            // Cria parâmetros do job, incluindo o caminho do arquivo
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("filePath", filePath)
                    .addDate("startAt", new Date())
                    .toJobParameters();

            // Inicia o job
            jobLauncher.run(job, jobParameters);

            return ResponseEntity.ok("Job iniciado com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao iniciar o job: " + e.getMessage());
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        // Define o caminho do arquivo
        String directory = "C:\\Users\\luizc\\Documents";
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = directory + System.getProperty("file.separator") + file.getOriginalFilename();
        File dest = new File(filePath);
        file.transferTo(dest); // Salva o arquivo no sistema de arquivos
        return filePath; // Retorna o caminho do arquivo salvo
    }

//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadArquivo(@RequestParam("file") MultipartFile arquivo) {
//        List<Insumo> insumos = new ArrayList<>();
//
//        if (arquivo.isEmpty()) {
//            return new ResponseEntity<>("Por favor, selecione um arquivo para fazer o upload.", HttpStatus.BAD_REQUEST);
//        }
//
//        try (InputStream entrada = arquivo.getInputStream(); Workbook planilha = new XSSFWorkbook(entrada)) {
//            Sheet primeiraAba = planilha.getSheetAt(0);
//            int contadorLinhas = 0;
//
//            // Pular a primeira linha se for o cabeçalho
//            boolean primeiraLinha = true;
//
//            for (Row linha : primeiraAba) {
//                if (primeiraLinha) {
//                    primeiraLinha = false;
//                    continue;
//                }
//
//                String codigo = linha.getCell(0).getStringCellValue();
//                String descricao = linha.getCell(1).getStringCellValue();
//                String unidadeMedida = linha.getCell(2).getStringCellValue();
//                String origemPreco = linha.getCell(3).getStringCellValue();
//                String preco = linha.getCell(4).getStringCellValue();
//
//                Insumo insumo = new Insumo();
//                insumo.setCodigo(codigo);
//                insumo.setDescricao(descricao);
//                insumo.setOrigemPreco(origemPreco);
//                insumo.setUnidadeMedida(unidadeMedida);
//                insumo.setPreco(preco);
//insumos.add(insumo);
//
//                contadorLinhas++;
//
//                if (insumos.size() == 200) {
//                    insumoRepository.saveAll(insumos);
//                    insumos.clear();
//                }
//            }
//
//            // Salva quaisquer registros restantes que não foram salvos em um lote
//            if (!insumos.isEmpty()) {
//                insumoRepository.saveAll(insumos);
//            }
//
//
//            return new ResponseEntity<>("Arquivo enviado com sucesso. Número de linhas: " + contadorLinhas, HttpStatus.OK);
//        } catch (IOException e) {
//            return new ResponseEntity<>("Falha ao enviar arquivo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @GetMapping("/buscar")
    public ResponseEntity buscarTodos(){
        return ResponseEntity.status(200).body(service.buscarTodos());
    }

    }
