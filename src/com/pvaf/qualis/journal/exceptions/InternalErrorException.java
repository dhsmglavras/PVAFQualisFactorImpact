/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvaf.qualis.journal.exceptions;

/**
 *
 * @author douglas
 */
public class InternalErrorException extends Exception{
    
    public InternalErrorException(){
    
    }
    
    @Override
    public String getMessage(){
        return "Ocorreu um Erro Interno";
    }
    
    
}
