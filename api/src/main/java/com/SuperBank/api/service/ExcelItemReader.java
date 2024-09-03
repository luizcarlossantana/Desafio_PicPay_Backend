package com.SuperBank.api.service;

import com.SuperBank.api.model.Insumo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ItemReader;

import java.io.InputStream;
import java.util.Iterator;

public class ExcelItemReader implements ItemReader<Insumo> {

    private final Iterator<Row> rowIterator;
    private final SXSSFWorkbook streamingWorkbook;

    public ExcelItemReader(InputStream inputStream) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        this.streamingWorkbook = new SXSSFWorkbook(workbook); // Enable streaming
        Sheet sheet = streamingWorkbook.getSheetAt(0);
        this.rowIterator = sheet.iterator();
        // Skip the header row
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }
        inputStream.close();
    }

    @Override
    public Insumo read() {
        if (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Insumo insumo = new Insumo();

            insumo.setCodigo(getCellValue(row, 6));
            insumo.setDescricao(getCellValue(row, 7));
            insumo.setUnidadeMedida(getCellValue(row, 8));
            insumo.setOrigemPreco(getCellValue(row, 9));
            insumo.setPreco(getCellValue(row, 10));

            return insumo;
        } else {
            // Close the workbook to free resources
            try {
                streamingWorkbook.close();
            } catch (Exception e) {
                // Handle exception if needed
            }
            return null;
        }
    }

    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Verifica se a célula é um número inteiro ou decimal e converte para string
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case STRING:
                        return cell.getRichStringCellValue().getString();
                    case NUMERIC:
                        return String.valueOf(cell.getNumericCellValue());
                    default:
                        return "";
                }
            case BLANK:
                return "";
            default:
                return "Tipo de célula não suportado";
        }
    }
}
