package com.SuperBank.api.controller;

import com.SuperBank.api.model.Insumo;
import com.SuperBank.api.model.dtos.CompanyDTO;
import com.SuperBank.api.repository.InsumoRepository;
import com.SuperBank.api.service.CompanyService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @PostMapping("/upload-python")
    public ResponseEntity importarTabelaProcessarPython(@RequestParam("file") MultipartFile file) throws IOException {

        RestTemplate restTemplate = new RestTemplate();

        // URL do endpoint da API Python
        String url = "http://127.0.0.1:8000/servicos/upload/";

        // Criar headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Converter MultipartFile para Resource
        Resource fileAsResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        // Criar corpo multipart
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileAsResource);

        // Criar entidade HTTP
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Fazer requisição POST
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

    }
    @PostMapping("/import")
    public ResponseEntity<String> importFile(@RequestParam("file") MultipartFile file) {
        try {
            // Salva o arquivo em "C:\\Users\\luizc\\Documents" e obtém o caminho do arquivo
            String filePath = saveFile(file);

            // Cria parâmetros do job, incluindo o caminho do arquivo
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("file", filePath)
                    .addDate("startAt", new Date())
                    .toJobParameters();

            // Inicia o job
            jobLauncher.run(job, jobParameters);

            return ResponseEntity.ok("Job iniciado com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getCause());
            return ResponseEntity.status(500).body("Erro ao iniciar o job: " + e);
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        // Obtém o caminho do diretório home do usuário atual
        String userHomeDirectory = System.getProperty("user.home");
        if (userHomeDirectory == null || userHomeDirectory.isEmpty()) {
            throw new IOException("User home directory is not available.");
        }
        File mainDir = new File(userHomeDirectory, "Documents");

        // Verifica se o diretório principal existe, se não, cria
        if (!mainDir.exists()) {
            mainDir.mkdirs();
        }

        // Cria um subdiretório temporário
        String tempDirectory = mainDir.getAbsolutePath() + System.getProperty("file.separator") + "temp";
        File tempDir = new File(tempDirectory);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        // Define o caminho completo do arquivo dentro do diretório temporário
        String uniqueFilename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = tempDirectory + System.getProperty("file.separator") + uniqueFilename;
        File dest = new File(filePath);

        // Verifica se há espaço suficiente no disco
        long usableSpace = tempDir.getUsableSpace();
        long fileSize = file.getSize();
        if (fileSize > usableSpace) {
            throw new IOException("Not enough space on disk to save the file.");
        }

        try {
            // Salva o arquivo no sistema de arquivos
            file.transferTo(dest);
        } catch (IOException e) {
            // Adiciona logging para ajudar na depuração
            System.err.println("Error saving file: " + e.getMessage());
            throw e;
        }

        return filePath; // Retorna o caminho do arquivo salvo
    }



    @PostMapping("/upload")
    public ResponseEntity<String> uploadArquivo(@RequestParam("file") MultipartFile arquivo) {
        List<Insumo> insumos = new ArrayList<>();

        if (arquivo.isEmpty()) {
            return new ResponseEntity<>("Por favor, selecione um arquivo para fazer o upload.", HttpStatus.BAD_REQUEST);
        }

        try (InputStream entrada = arquivo.getInputStream(); Workbook planilha = new XSSFWorkbook(entrada)) {
            Sheet primeiraAba = planilha.getSheetAt(0);
            int contadorLinhas = 0;

            // Pular a primeira linha se for o cabeçalho
            boolean primeiraLinha = true;

            for (Row linha : primeiraAba) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                try {
                    String codigo = linha.getCell(0) != null ? getStringCellValue(linha.getCell(0)) : "";
                    String descricao = linha.getCell(1) != null ? linha.getCell(1).getStringCellValue() : "";
                    String unidadeMedida = linha.getCell(2) != null ? linha.getCell(2).getStringCellValue() : "";
                    String origemPreco = linha.getCell(3) != null ? linha.getCell(3).getStringCellValue() : "";
                    String preco = linha.getCell(4) != null ? getStringCellValue(linha.getCell(4)) : "";

                    Insumo insumo = new Insumo();
                    insumo.setCodigo(codigo);
                    insumo.setDescricao(descricao);
                    insumo.setOrigemPreco(origemPreco);
                    insumo.setUnidadeMedida(unidadeMedida);
                    insumo.setPreco(preco);
                    insumos.add(insumo);

                    contadorLinhas++;
                } catch (Exception e) {
                    // Log de erro e continuação do processamento
                    System.err.println("Erro ao processar a linha " + (linha.getRowNum() + 1) + ": " + e.getMessage());
                    continue;
                }

                if (insumos.size() == 200) {
                    try {
                        insumoRepository.saveAll(insumos);
                        insumos.clear();
                    } catch (Exception e) {
                        return new ResponseEntity<>("Erro ao salvar dados no banco: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }

            // Salva quaisquer registros restantes que não foram salvos em um lote
            if (!insumos.isEmpty()) {
                try {
                    insumoRepository.saveAll(insumos);
                } catch (Exception e) {
                    return new ResponseEntity<>("Erro ao salvar dados no banco: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            return new ResponseEntity<>("Arquivo enviado com sucesso. Número de linhas: " + contadorLinhas, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Falha ao enviar arquivo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getStringCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    @GetMapping("/buscar")
    public ResponseEntity buscarTodos(){
        return ResponseEntity.status(200).body(service.buscarTodos());
    }

    }







