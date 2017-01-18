/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvaf.qualis.journal.entidades;

/**
 *
 * @author marte
 */
public class QualisJournal {
    
    private String issn;
    private String issnInvalid;
    private String title;
    private AreaClassification areClassification;
    //private String pubType;
    private long year;
                
    public QualisJournal(String issn, String title, AreaClassification areaClassification, long year){
        this.issn = issn;
        this.title = title;
        this.areClassification = areaClassification;
        this.year = year;
    }

    /**
     * @return the issn
     */
    public String getIssn() {
        return issn;
    }

    /**
     * @param issn
     */
    public void setIssn(String issn) {
        this.issn = issn;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * @return the issnInvalid
     */
    public String getIssnInvalid() {
        return issnInvalid;
    }

    /**
     * @param issnInvalid the issnInvalid to set
     */
    public void setIssnInvalid(String issnInvalid) {
        this.issnInvalid = issnInvalid;
    }
    
    /**
     * @return the areClassification
     */
    public AreaClassification getAreClassification() {
        return areClassification;
    }

    /**
     * @param areClassification the areClassification to set
     */
    public void setAreClassification(AreaClassification areClassification) {
        this.areClassification = areClassification;
    }
    
    /*public String getPubType(){
        return this.pubType;
    }*/
    
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
        String str = this.issn + ";" + this.title + ";"+this.areClassification.toString()+ ";" /*+ this.pubType */+";"+ this.year;
        return str;
    }
}
