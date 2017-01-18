/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvaf.qualis.journal.exceptions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author marte
 */
public class IssnException extends Exception{
    
    private static int  autoIncremento = 1;
    private final int numero;
    private String line;
    private File file;
    private FileWriter out;
    private PrintWriter writer;
    
    public IssnException(String line){
        this.line = line;
        this.numero = autoIncremento++;
    }
            
    public void log(){
        try {
            
            Label label;
            if (this.numero == 1) {
                
                WritableWorkbook planilha = Workbook.createWorkbook(new File("registroErro.xls"));
                WritableSheet aba = planilha.createSheet("Sheet1", 0);

                String cabecalho[] = new String[5];
                cabecalho[0] = "ISSN";
                cabecalho[1] = "ISSN Invalido";
                cabecalho[2] = "Título";
                cabecalho[3] = "Área de Avaliação";
                cabecalho[4] = "Classificação";
                
                for (int i = 0; i < cabecalho.length; i++) {
                    label = new Label(i, 0, cabecalho[i]);
                    aba.addCell(label);
                }
                
                String line[] = this.line.split(";");
                int cont =0;
                
                int linha = numero;
                
                label = new Label(0, linha, null);
                aba.addCell(label);

                label = new Label(1, linha, line[0]);
                aba.addCell(label);

                label = new Label(2, linha, line[1]);
                aba.addCell(label);

                label = new Label(3, linha, line[2]);
                aba.addCell(label);

                label = new Label(4, linha, line[3]);
                aba.addCell(label);
                
                planilha.write();
		planilha.close();
                
            } else if(this.numero>1){
                
                try {
                    
                    Workbook arqExist = Workbook.getWorkbook(new File("registroErro.xls"));
                    WritableWorkbook copyPlanilha = Workbook.createWorkbook(new File("registroErro.xls"),arqExist);
                    
                    WritableSheet aba = copyPlanilha.getSheet("Sheet1");
                    WritableCell cell;
                    
                    int linha = numero;
                    String line[] = this.line.split(";");
                    
                    label = new Label(0, linha, null);
                    cell = (WritableCell) label;
                    aba.addCell(cell);

                    label = new Label(1, linha, line[0]);
                    cell = (WritableCell) label;
                    aba.addCell(cell);

                    label = new Label(2, linha, line[1]);
                    cell = (WritableCell) label;
                    aba.addCell(cell);

                    label = new Label(3, linha, line[2]);
                    cell = (WritableCell) label;
                    aba.addCell(cell);

                    label = new Label(4, linha, line[3]);
                    cell = (WritableCell) label;
                    aba.addCell(cell);
                    
                    arqExist.close();
                    copyPlanilha.write();
                    copyPlanilha.close();
                    
                } catch (BiffException ex) {
                    Logger.getLogger(IssnException.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        } catch (IOException e) {
            System.err.println("Erro de E/S. Causa: " + e.getMessage());
        } catch (WriteException ex){
            System.err.println("Erro em escrever no arquivo. Causa: " + ex.getMessage());
        }
    }
    
    public void log2(){
        try {
            
            Label label;
            if (this.numero == 1) {
                
                WritableWorkbook planilha = Workbook.createWorkbook(new File("registroErro.xls"));
                WritableSheet aba = planilha.createSheet("Sheet1", 0);

                String cabecalho[] = new String[5];
                cabecalho[0] = "ISSN";
                cabecalho[1] = "ISSN Invalido";
                cabecalho[2] = "Título";
                cabecalho[3] = "Área de Avaliação";
                cabecalho[4] = "Classificação";
                
                for (int i = 0; i < cabecalho.length; i++) {
                    label = new Label(i, 0, cabecalho[i]);
                    aba.addCell(label);
                }
                
                String line[] = this.line.split(";");
                
                int linha = numero;
                
                label = new Label(0, linha, line[0]);
                aba.addCell(label);

                label = new Label(1, linha, line[1]);
                aba.addCell(label);

                label = new Label(2, linha, line[2]);
                aba.addCell(label);

                label = new Label(3, linha, line[3]);
                aba.addCell(label);

                label = new Label(4, linha, line[4]);
                aba.addCell(label);
                
                planilha.write();
		planilha.close();
                
            } else if(this.numero>1){
                
                try {
                    
                    Workbook arqExist = Workbook.getWorkbook(new File("registroErro.xls"));
                    WritableWorkbook copyPlanilha = Workbook.createWorkbook(new File("registroErro.xls"),arqExist);
                    
                    WritableSheet aba = copyPlanilha.getSheet("Sheet1");
                    WritableCell cell;
                    
                    int linha = numero;
                    String line[] = this.line.split(";");
                    
                    label = new Label(0, linha, line[0]);
                    cell = (WritableCell) label;
                    aba.addCell(cell);

                    
                    label = new Label(1, linha, line[1]);
                    cell = (WritableCell) label;
                    aba.addCell(cell);

                    label = new Label(2, linha, line[2]);
                    cell = (WritableCell) label;
                    aba.addCell(cell);

                    label = new Label(3, linha, line[3]);
                    cell = (WritableCell) label;
                    aba.addCell(cell);

                    label = new Label(4, linha, line[4]);
                    cell = (WritableCell) label;
                    aba.addCell(cell);
                    
                    arqExist.close();
                    copyPlanilha.write();
                    copyPlanilha.close();
                    
                } catch (BiffException ex) {
                    Logger.getLogger(IssnException.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        } catch (IOException e) {
            System.err.println("Erro de E/S. Causa: " + e.getMessage());
        } catch (WriteException ex){
            System.err.println("Erro em escrever no arquivo. Causa: " + ex.getMessage());
        }
    }
    
    public boolean fileExist(){
        File file = new File("registroErro.xls");
        return file.exists();
    }
        
    @Override
    public String getMessage(){
	return "ISSN inválido, verifique no arquivo: registroErro.xls";
    }
}
