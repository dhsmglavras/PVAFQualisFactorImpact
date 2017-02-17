/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvaf.qualis.journal.exceptions;

import java.io.File;
import java.io.IOException;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.log4j.Logger;
/**
 *
 * @author marte
 */
public class IssnException extends Exception{
    
    private static int  autoIncremento = 1;
    private final int numero;
    private final String line;
    
    private final static Logger log = Logger.getLogger(IssnException.class);
    
    public IssnException(){
        this.line = null;
        this.numero =0;
    }
    
    public IssnException(String line){
        this.line = line;
        this.numero = autoIncremento++;
    }
            
    public void log() throws ErrorException{
        
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
                
                String atributos[] = this.line.split(";");
                int cont =0;
                
                int linha = numero;
                
                label = new Label(0, linha, null);
                aba.addCell(label);

                label = new Label(1, linha, atributos[0]);
                aba.addCell(label);

                label = new Label(2, linha, atributos[1]);
                aba.addCell(label);

                label = new Label(3, linha, atributos[2]);
                aba.addCell(label);

                label = new Label(4, linha, atributos[3]);
                aba.addCell(label);
                
                planilha.write();
		planilha.close();
                
            } else if(this.numero>1){
                Workbook arqExist = Workbook.getWorkbook(new File("registroErro.xls"));
                WritableWorkbook copyPlanilha = Workbook.createWorkbook(new File("registroErro.xls"), arqExist);

                WritableSheet aba = copyPlanilha.getSheet("Sheet1");
                WritableCell cell;

                int linha = numero;
                String atributos[] = this.line.split(";");

                label = new Label(0, linha, null);
                cell = (WritableCell) label;
                aba.addCell(cell);

                label = new Label(1, linha, atributos[0]);
                cell = (WritableCell) label;
                aba.addCell(cell);

                label = new Label(2, linha, atributos[1]);
                cell = (WritableCell) label;
                aba.addCell(cell);

                label = new Label(3, linha, atributos[2]);
                cell = (WritableCell) label;
                aba.addCell(cell);

                label = new Label(4, linha, atributos[3]);
                cell = (WritableCell) label;
                aba.addCell(cell);

                arqExist.close();
                copyPlanilha.write();
                copyPlanilha.close();
            }
            
        } catch (IOException e) {
            log.error("Erro de E/S.", e.fillInStackTrace());
            throw new ErrorException("Ocorreu um Erro Interno");
        } catch (WriteException e){
            log.error("Erro em escrever no arquivo.", e.fillInStackTrace());
            throw new ErrorException("Ocorreu um Erro Interno");
        } catch (BiffException ex) {
            log.error("Erro de Leitura do arquivo biff.", ex.fillInStackTrace());
            throw new ErrorException("Ocorreu um Erro Interno");
        }
    }
    
    public void log2() throws ErrorException{
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
                
                String atributos[] = this.line.split(";");
                
                int linha = numero;
                
                label = new Label(0, linha, atributos[0]);
                aba.addCell(label);

                label = new Label(1, linha, atributos[1]);
                aba.addCell(label);

                label = new Label(2, linha, atributos[2]);
                aba.addCell(label);

                label = new Label(3, linha, atributos[3]);
                aba.addCell(label);

                label = new Label(4, linha, atributos[4]);
                aba.addCell(label);
                
                planilha.write();
		planilha.close();
                
            } else if(this.numero>1){
                Workbook arqExist = Workbook.getWorkbook(new File("registroErro.xls"));
                WritableWorkbook copyPlanilha = Workbook.createWorkbook(new File("registroErro.xls"), arqExist);

                WritableSheet aba = copyPlanilha.getSheet("Sheet1");
                WritableCell cell;

                int linha = numero;
                String atributos[] = this.line.split(";");

                label = new Label(0, linha, atributos[0]);
                cell = (WritableCell) label;
                aba.addCell(cell);

                label = new Label(1, linha, atributos[1]);
                cell = (WritableCell) label;
                aba.addCell(cell);

                label = new Label(2, linha, atributos[2]);
                cell = (WritableCell) label;
                aba.addCell(cell);

                label = new Label(3, linha, atributos[3]);
                cell = (WritableCell) label;
                aba.addCell(cell);

                label = new Label(4, linha, atributos[4]);
                cell = (WritableCell) label;
                aba.addCell(cell);

                arqExist.close();
                copyPlanilha.write();
                copyPlanilha.close();
            }
            
        } catch (IOException e) {
            log.error("Erro de E/S.", e.fillInStackTrace());
            throw new ErrorException("Ocorreu um Erro Interno");
        } catch (WriteException e) {
            log.error("Erro em escrever no arquivo.", e.fillInStackTrace());
            throw new ErrorException("Ocorreu um Erro Interno");
        } catch (BiffException ex) {
            log.error("Erro de Leitura do arquivo biff.", ex.fillInStackTrace());
            throw new ErrorException("Ocorreu um Erro Interno");
        }
    }
        
    public boolean fileExist(){
        File file = new File("registroErro.xls");
        if(this.numero==1){
            return false;
        }
        return true;        
    }
        
    @Override
    public String getMessage(){
	return "ISSN inválido, verifique no arquivo: registroErro.xls";
    }
}
