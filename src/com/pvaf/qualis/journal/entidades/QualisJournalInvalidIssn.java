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
public final class QualisJournalInvalidIssn extends QualisJournal{
    
    private String issn;
    private String issnInvalid;
    private String title;
    private AreaClassification areClassification;
    private String pubType;
    private long year;
    
    
    public QualisJournalInvalidIssn(String issn, String issnInvalid, String title, AreaClassification areaClassification, long year){
        super(issn, title,areaClassification, year);
        this.issnInvalid = issnInvalid;
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
    
    @Override
    public String toString(){
        String str = this.issn + ";" + this.issnInvalid + ";" + this.title + ";"+this.areClassification.toString()+ ";" + this.pubType +";"+ this.year;
        return str;
    }
}
