/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvaf.qualis.journal.main;

import com.pvaf.qualis.journal.dao.IssnDAO;
import com.pvaf.qualis.journal.dao.JournalDAO;
import com.pvaf.qualis.journal.entidades.QualisJournal;
import com.pvaf.qualis.journal.entidades.QualisJournalInvalidIssn;
import com.pvaf.qualis.journal.entidades.AreaClassification;
import com.pvaf.qualis.journal.entidades.Journal;
import com.pvaf.qualis.journal.exceptions.ErrorException;
import com.pvaf.qualis.journal.exceptions.IssnException;
import com.pvaf.qualis.journal.io.ReadQualisJournal;
import java.io.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.PropertyConfigurator;
/**
 *
 * @author douglas
 */
public class AppQualisJournal {
    
    static {
        PropertyConfigurator.configure("properties/log4j.properties");
    }
            
    protected static List<QualisJournal> addComputerScienceDB = new ArrayList<>();
    protected static List<QualisJournal> addJournalsAreaDB = new ArrayList<>();
        
    protected static TreeSet<String> issnsIne = new TreeSet<>(); // isnns não existentes no bd    
    protected static TreeSet<String> issnsExi = new TreeSet<>(); // isnns existentes no bd
    
    public static Set<String> lerQualis(String path, int op) throws ErrorException{
        ReadQualisJournal rq = new ReadQualisJournal(path);
        if(op==1){
            rq.redQualis();
        }else{
            rq.redQualisInvalidIssn();
        }
        return rq.getJournalsQualis();
    } 
    
    public static boolean validateIssn(String issn){
        if(issn.matches("([0-9]\\d{3}-[0-9]\\d{2}X)") || issn.matches("([0-9]\\d{3}-[0-9]\\d{3})") ||
                issn.matches("([0-9]\\d{3}-[0-9]\\d{2}x)")){       
            return true;
        }            
        return false;
    }
    
    public static HashSet<String> addQualisComputerScience(HashSet<String> issns){ //retornar
        HashSet<String> issn = new HashSet<>();
        issn.addAll(issns);
        return issn;        
    }
    
    public static List<QualisJournal> processQualisJournalsArea(Set<String> qualisJournal, long year, int op) throws ErrorException {
        List<QualisJournal> qualisJournals = new ArrayList<>();
        
        if(op==1){
            int test = 0;        
            for(String q: qualisJournal){
                String[] token = q.split(";");
                        
                String issnAux;
                issnAux = token[0];
                    
                if(validateIssn(issnAux)){
                    String issn = issnAux.toUpperCase();                
                    String title = token[1];
                    title = title.replace("", "");
                    String area = token[2];
                    String classification = token[3];
                
                    AreaClassification ac = new AreaClassification(area,classification); 
                    QualisJournal ja = new QualisJournal(issn,title,ac,year);
                    qualisJournals.add(ja);
                }else{
                    try {
                        throw new IssnException(q);
                    } catch (IssnException e) {
                        e.log();
                    }finally{
                        test++;
                    }
                }
                
                if(test==0){
                    AppQualisJournal.deleteFile("registroErro.xls");
                }
            }
            
        }else{
            int test =0;
            for(String q: qualisJournal){
                
                String[] token = q.split(";");
                
                String issnAux;
                issnAux = token[0];
                if(validateIssn(issnAux)){
                    String issn = issnAux.toUpperCase();
                    String issnInvalid = token[1];
                    String title = token[2];
                    title = title.replace("", "");
                    String area = token[3];
                    String classification = token[4];
                
                    AreaClassification ac = new AreaClassification(area,classification); 
                    QualisJournal ja = new QualisJournalInvalidIssn(issn,issnInvalid,title,ac,year);
                    qualisJournals.add(ja);
                }else{
                    try {
                        throw new IssnException(q);
                    }catch (IssnException e) {
                        e.log2();
                    }finally{
                        test++;
                    }
                }
                
                if(test==0){
                    AppQualisJournal.deleteFile("registroErro.xls");
                
                }
            }
        }
        return qualisJournals;
    }
    
    public static Set<String> createSetIssn(List<QualisJournal> list){
        Set<String> issnJa = new HashSet<>();
        
        for(QualisJournal ja: list){
            issnJa.add(ja.getIssn());
        }
        
        return issnJa;
    }
    
    public static void splitJournals(Set<String> comp, List<QualisJournal> journals) throws ErrorException{            
        for(QualisJournal s: journals){            
            boolean exist = false;
            
            exist = IssnDAO.getIssn(s.getIssn());
            
            if(!exist){
                addJournalsAreaDB.add(s);
                issnsIne.add(s.getIssn());
            }else{
                addComputerScienceDB.add(s);
                issnsExi.add(s.getIssn());
            }
        }
        
        CompararJournalsTitulos comT = new CompararJournalsTitulos();
        Collections.sort(addJournalsAreaDB, comT);
        
        Collections.sort(addComputerScienceDB, comT);
    }
    
    public static List<Journal> createListQualis(Set<String> issns){ //retornar
        List<Journal> listQualis = new ArrayList<>();
        
        for(String issn: issns){
            Journal q = new Journal(issn);
            listQualis.add(q);
        }
        
        return listQualis;
    }
    
    public static String verifyPubType(String title){
        
        if (title.contains("magazine")) {
            return "M"; 
        }else if(title.contains("journal")){
            return "J";
        }
        return null;
    }
    
    public static void addAttributesQualis(List<Journal> listQualis, List<QualisJournal> listJa){
        for(int i=0; i<listQualis.size(); i++){
            Journal qualis = listQualis.get(i);
            HashSet<String> titles;
            HashSet<String> invalidIssns;
            HashSet<AreaClassification> areasClassifications;
            
            for (QualisJournal ja: listJa) {
                                
                if(listQualis.get(i).getIssn().equals(ja.getIssn())){
                    
                    titles=qualis.getTitles();
                    titles.add(ja.getTitle());
                    qualis.setTitles(titles);
                    
                    String pubType = verifyPubType(ja.getTitle().toLowerCase());
            
                    pubType = (!(pubType==null)) ? pubType : "J";
                    
                    invalidIssns = qualis.getInvalidIssns();
                    invalidIssns.add(ja.getIssnInvalid());
                    
                    if(!(invalidIssns==null)){
                        qualis.setInvalidIssns(invalidIssns);
                    }
                
                    areasClassifications = qualis.getAreasClassification();
                    areasClassifications.add(ja.getAreClassification());
                    qualis.setAreasClassification(areasClassifications);
                    
                    qualis.setPubType(pubType);
                    
                    qualis.setYear(ja.getYear());
                    
                    listQualis.set(i, qualis);
                }
            }
        }
    }
    
    public static void deleteFile(String path){
        File file = new File(path);
        file.delete();
    }
    
    public static boolean fileExists(String path){
        File file = new File(path);
        return file.exists();
    }
    
    /** 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        long init =0; long end=0; long diff=0;
        
        try {
            System.out.println("Opções");
            System.out.println("1 journal Qualis");
            System.out.println("2 registro de erros");
            
            Scanner ler = new Scanner( System.in );
            int op = ler.nextInt();
            
            switch(op){
                
                case 1:
                    
                    init = System.currentTimeMillis();
                    
                    AppQualisJournal.deleteFile("registroErro.xls");
                    
                    Set<String> setJournalsQualis = AppQualisJournal.lerQualis("qualis-journal-todas-as-areas-2015.xls",op);
                    
                    List<QualisJournal> listQualisJournals = AppQualisJournal.processQualisJournalsArea(setJournalsQualis,2015,op);
                    
                    Set<String> setIssnJa = AppQualisJournal.createSetIssn(listQualisJournals);
                    
                    Set<String> setIssnCs = AppQualisJournal.addQualisComputerScience(IssnDAO.issnDB());
                    
                    AppQualisJournal.splitJournals(setIssnCs, listQualisJournals );
                    
                    listQualisJournals.clear();
                    setIssnCs.clear();
                    setIssnJa.clear();
                    
                    List<QualisJournal> listCs = AppQualisJournal.addComputerScienceDB;
                    List<Journal> qualisC = AppQualisJournal.createListQualis(AppQualisJournal.issnsExi);
                    
                    List<QualisJournal> listJa = AppQualisJournal.addJournalsAreaDB;
                    List<Journal> qualisJ = AppQualisJournal.createListQualis(AppQualisJournal.issnsIne);
                    
                    AppQualisJournal.addAttributesQualis(qualisJ,listJa);
                    listJa.clear();
                    AppQualisJournal.addJournalsAreaDB.clear();
                    AppQualisJournal.issnsIne.clear();
                    
                    AppQualisJournal.addAttributesQualis(qualisC, listCs);
                    listCs.clear();
                    AppQualisJournal.addComputerScienceDB.clear();
                    AppQualisJournal.issnsExi.clear();
                    
                    for(Journal q: qualisJ){
                        JournalDAO.insert(q);
                    }
                    
                    for(Journal q: qualisC){
                        JournalDAO.update(q);
                    }
                    
                    System.out.println(qualisJ.size());
                    System.out.println(qualisC.size());
                    
                    break;
                    
                case 2:
                    
                    init = System.currentTimeMillis();
                    
                    setJournalsQualis = AppQualisJournal.lerQualis("registroErro.xls",op);
                    
                    listQualisJournals = AppQualisJournal.processQualisJournalsArea(setJournalsQualis,2015,op);
                    
                    setIssnJa = AppQualisJournal.createSetIssn(listQualisJournals);
                    
                    setIssnCs = AppQualisJournal.addQualisComputerScience(IssnDAO.issnDB());
                    
                    AppQualisJournal.splitJournals(setIssnCs, listQualisJournals );
                    
                    listQualisJournals.clear();
                    setIssnCs.clear();
                    setIssnJa.clear();
                    
                    listCs = AppQualisJournal.addComputerScienceDB;
                    qualisC = AppQualisJournal.createListQualis(AppQualisJournal.issnsExi);
                    
                    listJa = AppQualisJournal.addJournalsAreaDB;
                    qualisJ = AppQualisJournal.createListQualis(AppQualisJournal.issnsIne);
                    
                    AppQualisJournal.addAttributesQualis(qualisJ,listJa);
                    listJa.clear();
                    AppQualisJournal.addJournalsAreaDB.clear();
                    AppQualisJournal.issnsIne.clear();
                    
                    AppQualisJournal.addAttributesQualis(qualisC, listCs);
                    listCs.clear();
                    AppQualisJournal.addComputerScienceDB.clear();
                    AppQualisJournal.issnsExi.clear();
                    
                    for(Journal q: qualisJ){
                        JournalDAO.insert(q);
                    }
                    
                    for(Journal q: qualisC){
                        JournalDAO.update(q);
                    }
                    
                    System.out.println(qualisJ.size());
                    System.out.println(qualisC.size());
                    
                    break;   
                    
                default:;
            }
            
            if (AppQualisJournal.fileExists("registroErro.xls")) {
                try {
                    throw new IssnException();
                } catch (IssnException e) {
                    System.err.println(e.getMessage());
                    
                }
            }
            
            end = System.currentTimeMillis();
            
        } catch (ErrorException ex) {
            System.err.println(ex.getMessage());
        }
        
        diff = end - init; 
        System.out.println("Demorou " + (double)(diff / 1000) + " segundos");                 
    }
    
    private static class CompararJournalsTitulos implements Comparator<QualisJournal>{      
        @Override
        public int compare(QualisJournal o1, QualisJournal o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    }
}

/*class CompararJournalsTitulos implements Comparator<QualisJournal>{      
    @Override
    public int compare(QualisJournal o1, QualisJournal o2) {
        return o1.getTitle().compareTo(o2.getTitle());
    }
}*/

/*class CompararJournalsIssn implements Comparator<JournalsArea>{
       
    @Override
    public int compare(QualisJournal o1, QualisJournal o2) {
        return o1.getIssn().compareTo(o2.getIssn());
    }
}*/