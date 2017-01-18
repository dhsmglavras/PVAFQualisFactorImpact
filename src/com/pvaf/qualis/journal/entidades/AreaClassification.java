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
public class AreaClassification {
    
    private String area;
    private String classification;
    
    public AreaClassification(String area, String classification){
        this.area = area;
        this.classification = classification;
    }

    /**
     * @return the area
     */
    public String getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * @return the classification
     */
    public String getClassification() {
        return classification;
    }

    /**
     * @param classification the classification to set
     */
    public void setClassification(String classification) {
        this.classification = classification;
    }
    
    public String toString(){
        return this.area+";"+this.classification;
    }
    
}
