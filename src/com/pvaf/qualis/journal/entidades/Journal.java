/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvaf.qualis.journal.entidades;

import java.util.HashSet;

/**
 *
 * @author douglas
 */
public class Journal {
    
    private String issn;
    
    private HashSet<String> invalidIssns = new HashSet<>();
    
    private HashSet<String> titles = new HashSet<>();
    
    private HashSet<AreaClassification> areasClassification = new HashSet<>();
    
    private String pubType;
    
    private long year;
    
    public Journal(){
        
    }
    
    public Journal(String issn){
        this.issn = issn;        
    }

    /**
     * @return the issn
     */
    public String getIssn() {
        return issn;
    }

    /**
     * @return the invalidIssns
     */
    public HashSet<String> getInvalidIssns() {
        return invalidIssns;
    }

    /**
     * @param invalidIssns the invalidIssns to set
     */
    public void setInvalidIssns(HashSet<String> invalidIssns) {
        this.invalidIssns = invalidIssns;
    }

    /**
     * @return the titles
     */
    public HashSet<String> getTitles() {
        return titles;
    }

    /**
     * @param titles the titles to set
     */
    public void setTitles(HashSet<String> titles) {
        this.titles = titles;
    } 
    
    /**
     * @return the pubType
     */
    public String getPubType() {
        return pubType;
    }

    /**
     * @param pubType the pubType to set
     */
    public void setPubType(String pubType) {
        this.pubType = pubType;
    }
    
    /**
     * @return the areasClassification
     */
    public HashSet<AreaClassification> getAreasClassification() {
        return areasClassification;
    }

    /**
     * @param areasClassification the areasClassification to set
     */
    public void setAreasClassification(HashSet<AreaClassification> areasClassification) {
        this.areasClassification = areasClassification;
    }
    
    /**
     * @return the year
     */
    public long getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(long year) {
        this.year = year;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        
        str.append(this.issn);
        str.append(" ");
        str.append(this.pubType);
        str.append(" ");
        str.append(this.year);
        str.append( "\n");
        
        str.append("====================================\n");        
        if(!(this.invalidIssns == null)){
            for(String s: this.invalidIssns){
                str.append(s);
                str.append("\n");
            }
        }
        
        str.append("====================================\n");
        if(!(this.titles == null)){
            for(String s: this.titles){
                str.append(s);
                str.append("\n");
            }
        }
        
        str.append("====================================\n");
        if(!(this.areasClassification == null)){
            for(AreaClassification s: this.areasClassification){
                str.append(s.toString());
                str.append("\n");
            }
        }        
        return str.toString();
    }
}
