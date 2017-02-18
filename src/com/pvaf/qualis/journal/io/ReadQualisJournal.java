/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvaf.qualis.journal.io;

import com.pvaf.qualis.journal.exceptions.ErrorException;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.TreeSet;
import java.io.File;
import java.io.IOException;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import org.apache.log4j.Logger;

/**
 *
 * @author marte
 */
public class ReadQualisJournal {
    
    private final static Logger log = Logger.getLogger(ReadQualisJournal.class);
    
    private final String path;
    private final Set<String> journalsQualis = new TreeSet<>();
    
    public ReadQualisJournal(String path){
        this.path = path;
    }
    
    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the journalsQualis
     */
    public Set<String> getJournalsQualis() {
        return this.journalsQualis;
    }
       
    public void redQualis() throws ErrorException {
        
        try{
            File file = new File(path);
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");

            Workbook workbook = Workbook.getWorkbook(file, ws);
            String[] sheetNames = workbook.getSheetNames();
            Sheet sheet = null;
            
            for (int sheetNumber = 0; sheetNumber < sheetNames.length; sheetNumber++) {
                sheet = workbook.getSheet(sheetNames[sheetNumber]);

                int linhas = sheet.getRows();

                Cell cell = sheet.findCell("ISSN");
                
                if(cell==null){
                    throw new ErrorException("Verifique se o arquivo está configurado corretamente");
                }

                cell.getRow();
                
                for (int i = cell.getRow() + 1; i < linhas; i++) {
                    
                    int column = cell.getColumn();
                
                    Cell a1 = sheet.getCell(column++, i);
                    Cell a2 = sheet.getCell(column++, i);
                    Cell a3 = sheet.getCell(column++, i);
                    Cell a4 = sheet.getCell(column++, i);
                
                    byte array[] = a1.getContents().getBytes("UTF-8");
                    String issn = new String(array,"UTF-8");
                    issn = issn.replaceAll(";","");
                    issn = issn.trim();
                                
                    array = a2.getContents().getBytes("UTF-8");
                    String title = new String(array,"UTF-8");
                    title = title.replaceAll(";","");
                    title = title.trim();
                                
                    array = a3.getContents().getBytes("UTF-8");
                    String area = new String(array,"UTF-8");
                    area = area.replaceAll(";","");
                    area = area.trim();
                                
                    array = a4.getContents().getBytes("UTF-8");
                    String classification = new String(array,"UTF-8");
                    classification = classification.replaceAll(";","");
                    classification = classification.trim();
                    
                    String line = issn+";"+title+";"+area+";"+classification;
                    journalsQualis.add(line);
                }
            }
            workbook.close();            
        }catch(FileNotFoundException f){
            log.error("Arq. nao existe.",f.fillInStackTrace());
            throw new ErrorException("Ocorreu um Erro Interno");
	}catch (IOException e){
            log.error("Erro de E/S.",e.fillInStackTrace());
            throw new ErrorException("Ocorreu um Erro Interno");
	} catch (BiffException ex) {
            log.error("Erro de Leitura do arquivo biff.", ex.fillInStackTrace());
            throw new ErrorException("Salve no Formato Microsoft Excel (.xls)");
        }
    }

    public void redQualisInvalidIssn() throws ErrorException{
        
        try{
            File file = new File(path);
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");

            Workbook workbook = Workbook.getWorkbook(file, ws);
            String[] sheetNames = workbook.getSheetNames();
            Sheet sheet = null;
            
            for (int sheetNumber = 0; sheetNumber < sheetNames.length; sheetNumber++) {
                sheet = workbook.getSheet(sheetNames[sheetNumber]);

                int linhas = sheet.getRows();

                Cell cell = sheet.findCell("ISSN");
                
                if(cell==null){
                    throw new ErrorException("Verifique se o arquivo está configurado corretamente");
                }

                cell.getRow();

                for (int i = cell.getRow() + 1; i < linhas; i++) {
                    
                    int column = cell.getColumn();
                
                    Cell a1 = sheet.getCell(column++, i);
                    Cell a2 = sheet.getCell(column++, i);
                    Cell a3 = sheet.getCell(column++, i);
                    Cell a4 = sheet.getCell(column++, i);
                    Cell a5 = sheet.getCell(column++, i);
                
                    byte array[] = a1.getContents().getBytes("UTF-8");
                    String issn = new String(array,"UTF-8");
                    issn = issn.replaceAll(";","");
                    issn = issn.trim();
                
                    array = a2.getContents().getBytes("UTF-8");
                    String issnInvalid = new String(array,"UTF-8");
                    issnInvalid = issnInvalid.replaceAll(";","");
                    issnInvalid = issnInvalid.trim();
                
                    array = a3.getContents().getBytes("UTF-8");
                    String title = new String(array,"UTF-8");
                    title = title.toUpperCase();
                    title = title.replaceAll(";","");
                    title = title.trim();
                
                    array = a4.getContents().getBytes("UTF-8");
                    String area = new String(array,"UTF-8");
                    area = area.replaceAll(";","");
                    area = area.trim();
                
                    array = a5.getContents().getBytes("UTF-8");
                    String classification = new String(array,"UTF-8");
                    classification = classification.replaceAll(";","");
                    classification = classification.trim();
                    
                    String line = issn+";"+issnInvalid+";"+title+";"+area+";"+classification;
                    journalsQualis.add(line);
                }
            }
            workbook.close();
            
        }catch(FileNotFoundException f){
            log.error("Arq. nao existe.", f.fillInStackTrace());
            throw new ErrorException("Ocorreu um Erro Interno");
	}catch (IOException e){
            log.error("Erro de E/S.", e.fillInStackTrace());
            throw new ErrorException("Ocorreu um Erro Interno");
	} catch (BiffException ex) {
            log.error("Erro de Leitura do arquivo biff.", ex.fillInStackTrace());
            throw new ErrorException("Salve no Formato Microsoft Excel (.xls)");
        }
    }
}