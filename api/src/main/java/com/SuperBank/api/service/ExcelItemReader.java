package com.SuperBank.api.service;
import com.SuperBank.api.model.Insumo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.batch.item.ItemReader;

import java.io.InputStream;
import java.util.Iterator;

public class ExcelItemReader implements ItemReader<Insumo> {

    private final Iterator<Row> rowIterator;

    public ExcelItemReader(InputStream inputStream) throws Exception {
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        this.rowIterator = sheet.iterator();
        // Skip the header row
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }
    }

    @Override
    public Insumo read() {
        if (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Insumo pessoa = new Insumo();
            pessoa.setCodigo( row.getCell(0).getStringCellValue());
            pessoa.setDescricao(row.getCell(1).getStringCellValue());
            pessoa.setUnidadeMedida(row.getCell(2).getStringCellValue());
            pessoa.setOrigemPreco(row.getCell(3).getStringCellValue());
            pessoa.setPreco(row.getCell(4).getStringCellValue());
            return pessoa;
        } else {
            return null;
        }
    }
}
