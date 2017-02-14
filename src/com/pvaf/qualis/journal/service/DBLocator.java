/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvaf.qualis.journal.service;

import com.pvaf.qualis.journal.exceptions.InternalErrorException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author marte
 */
public class DBLocator {
    
    private final static Logger log = Logger.getLogger(DBLocator.class);
    
    static{
        try{
            DriverManager.registerDriver(new com.mysql.jdbc.NonRegisteringDriver());			
	}catch(Exception e){
            log.error("Erro ao registrar Driver JDBC MySql.", e.fillInStackTrace());
	}		
    }
	
    public static java.sql.Connection getConnection() throws SQLException, InternalErrorException{
        Login login = new Login();
	Connection conn; 
	conn = DriverManager.getConnection(login.getUrl(), login.getUser(), login.getPassword());
	conn.setAutoCommit(false);
	return conn;		
    }
}
