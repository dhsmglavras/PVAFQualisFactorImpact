/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvaf.qualis.journal.dao;

import com.pvaf.qualis.journal.service.DBLocator;
import com.pvaf.qualis.journal.entidades.AreaAvaliacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author marte
 */
public class AreaAvaliacaoDAO {
    
    private final static Logger log = Logger.getLogger(AreaAvaliacaoDAO.class);
    
    public static List<AreaAvaliacao> getAllNamesAreaAvaliacao() throws Exception {
        List<AreaAvaliacao> listA = new ArrayList<>();
        
        try(Connection conn = DBLocator.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM area_avaliacao");
            ResultSet rs = ps.executeQuery()){
            
            AreaAvaliacao area;
            while(rs.next()){
                area = new AreaAvaliacao(rs.getInt("id_area"),rs.getString("nome_area"));
                listA.add(area);
            }
	}catch(SQLException e){
            log.error("Ocorreu uma exceção de SQL.", e.fillInStackTrace());
            throw new Exception();
	}
	return listA;
    }
}
