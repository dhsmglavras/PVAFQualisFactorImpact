/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvaf.qualis.journal.dao;

import com.pvaf.qualis.journal.service.DBLocator;
import com.pvaf.qualis.journal.entidades.AreaAvaliacao;
import com.pvaf.qualis.journal.entidades.AreaClassification;
import com.pvaf.qualis.journal.entidades.Journal;
import com.pvaf.qualis.journal.entidades.Issn;
import com.pvaf.qualis.journal.exceptions.InternalErrorException;
import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author marte
 */
public class JournalDAO {
    
    private final static Logger log = Logger.getLogger(JournalDAO.class);    
    
    private static AreaAvaliacao checkAreaExists(String area) throws InternalErrorException{
        List<AreaAvaliacao> listA = AreaAvaliacaoDAO.getAllNamesAreaAvaliacao();
        
        for(AreaAvaliacao a: listA){
            if(area.equals(a.getNomeArea())){
                return a; 
            }
        }
        return null;
    }
    
    private static boolean checkIssnExists(String issn) throws InternalErrorException{
        List<Issn> listI = IssnDAO.getAllIssn();
        
        for(Issn i: listI){
            if(issn.equals(i.getIssn())){
                return true; 
            }
        }
        return false;
    }
    
    private static boolean checkIssnExists1(String issn) throws InternalErrorException{
        boolean exist = IssnDAO.getIssn(issn);
        
        if(exist){
            return true; 
            
        }else{
            return false;
        }
    }
    
    private static int getIndice(String estrato){
        String[] estratos = {"C","B5","B4","B3","B2","B1","A2","A1"};
        int indice;
        indice=0;
        for(String s: estratos){
            if (s.equals(estrato)){
                return indice;
            }
            indice++;
        }
        return indice;
    }
    
    public static void insert(Journal journal) throws InternalErrorException{
        Connection conn = null;
	try{
            int i=1;
            conn = DBLocator.getConnection();
            
            // Inserir publicationvenue
            PreparedStatement ps;
            ps = conn.prepareStatement("INSERT INTO publicationvenue (pub_type,publisher) VALUES (?,?)");
            ps.setString(i++,String.valueOf(journal.getPubType()));
            ps.setNull(i++, java.sql.Types.INTEGER);
            ps.executeUpdate();
            ps.close();
            
            ps = conn.prepareStatement("SELECT id_pub_venue FROM publicationvenue ORDER BY id_pub_venue");
            int idPubVenue=0;
            try (ResultSet publicationVenue = ps.executeQuery()) {
                if(publicationVenue.last()){
                    idPubVenue = publicationVenue.getInt("id_pub_venue");
                }
            }            
            ps.close();
            
            // Inserir issn válido
            i=1;
            ps = conn.prepareStatement("INSERT INTO issn (id_pub_venue,issn,`print/online`) VALUES (?,?,?)");
            ps.setInt(i++,idPubVenue);
            ps.setString(i++, journal.getIssn());
            ps.setNull(i++, java.sql.Types.INTEGER);
            ps.executeUpdate();
            ps.close();
            
            // Inserir issn inválido
            for(String q: journal.getInvalidIssns()){
                if((q!=null)){
                    boolean exist = checkIssnExists1(q);
                    if(exist){
                        i=1;
                        ps = conn.prepareStatement("INSERT INTO issn (id_pub_venue,issn,`print/online`) VALUES (?,?,?)");
                        ps.setInt(i++,idPubVenue);
                        ps.setString(i++, q);
                        ps.setNull(i++, java.sql.Types.INTEGER);
                        ps.executeUpdate();
                        ps.close();
                    }
                }
            }
            
            // Inserir Areas Classification
            for(AreaClassification ac: journal.getAreasClassification()){
                String[] token = ac.toString().split(";");
                
                int idArea=0;
                AreaAvaliacao area = checkAreaExists(token[0]);
                
                if(!(area==null)){
                     idArea = area.getIdArea();
                }else{
                    ps = conn.prepareStatement("INSERT INTO area_avaliacao (nome_area) VALUES (?)");
                    ps.setString(1,token[0]);
                    ps.executeUpdate();
                    ps.close();
                    
                    ps = conn.prepareStatement("SELECT id_area FROM area_avaliacao ORDER BY id_area");
                    try (ResultSet areaAvaliacao = ps.executeQuery()) {
                        if(areaAvaliacao.last()){
                            idArea = areaAvaliacao.getInt("id_area");                            
                        }
                    }            
                    ps.close();                    
                }
                
                // Inserir Qualis
                i = 1;
                ps = conn.prepareStatement("SELECT * FROM qualis WHERE id_pub_venue = ? AND id_area = ? AND year = ?");
                ps.setInt(i++, idPubVenue);
                ps.setInt(i++, idArea);
                ps.setBigDecimal(i++, BigDecimal.valueOf(journal.getYear()));

                try (ResultSet tableQualis = ps.executeQuery()) {
                    if (tableQualis.first()) {
                        i = 1;
                        ps = conn.prepareStatement("UPDATE qualis SET qualis = ? WHERE id_pub_venue = ? AND id_area = ? AND year = ?");
                        ps.setString(i++, token[1]);
                        ps.setInt(i++, idPubVenue);
                        ps.setInt(i++, idArea);
                        ps.setBigDecimal(i++, BigDecimal.valueOf(journal.getYear()));
                        ps.executeUpdate();
                        ps.close();
                    } else {
                        i = 1;
                        ps = conn.prepareStatement("INSERT INTO qualis (id_pub_venue,id_area,year,qualis) VALUES (?,?,?,?)");
                        ps.setInt(i++, idPubVenue);
                        ps.setInt(i++, idArea);
                        ps.setBigDecimal(i++, BigDecimal.valueOf(journal.getYear()));
                        ps.setString(i++, token[1]);
                        ps.executeUpdate();
                        ps.close();
                    }
                }
                ps.close();
            }
                        
            // Inserir titulos            
            for(String journalTitle: journal.getTitles()){
                int idPubVenueAux = 0;
                
                idPubVenueAux = TitleDAO.checkIdPubVenue(idPubVenue, journalTitle);
                
                if (idPubVenue != idPubVenueAux) {
                    i=1;
                    ps = conn.prepareStatement("INSERT INTO title (id_pub_venue, title) VALUES (?,?)");
                    ps.setInt(i++,idPubVenue);                    
                    ps.setString(i++, journalTitle);
                    ps.executeUpdate();
                    ps.close();
                    conn.commit();
                }
            }
            conn.commit();           
	} catch (SQLException e) {
            log.error("Ocorreu uma exceção de SQL.", e.fillInStackTrace());
            
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    log.error("Exceção ao realizar rollback.", e1.fillInStackTrace());
                    throw new InternalErrorException();
                }
            }
            throw new InternalErrorException();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("Exceção ao fechar a conexão.", e.fillInStackTrace());
                    throw new InternalErrorException();
                }
            }
        }
    }
    
    public static void update(Journal journal) throws InternalErrorException{
        Connection conn = null;
        
        try{
            conn = DBLocator.getConnection();
            int i=1;
            //cria um statement para a executar as querys
            PreparedStatement ps = conn.prepareStatement("SELECT id_pub_venue FROM issn WHERE issn = ?");
            ps.setString(i++,journal.getIssn());
            
            int idPubVenue = 0;
            
            try(ResultSet issn = ps.executeQuery()){
                if(issn.first()){
                    idPubVenue = issn.getInt("id_pub_venue");
                }
            }           
            ps.close();
            
            // Inserir issn inválido
            for(String q: journal.getInvalidIssns()){
                if((q!=null)){
                    boolean exist = checkIssnExists1(q);
                    if(exist){
                        i=1;
                        ps = conn.prepareStatement("INSERT INTO issn (id_pub_venue,issn,`print/online`) VALUES (?,?,?)");
                        ps.setInt(i++,idPubVenue);
                        ps.setString(i++, q);
                        ps.setNull(i++, java.sql.Types.INTEGER);
                        ps.executeUpdate();
                        ps.close();
                    }
                }
            }
            
            // Inserir Areas Classification
            for (AreaClassification ac : journal.getAreasClassification()) {
                String[] token = ac.toString().split(";");

                int idArea = 0;
                AreaAvaliacao area = checkAreaExists(token[0]);

                if (!(area == null)) {
                    idArea = area.getIdArea();
                } else {

                    ps = conn.prepareStatement("INSERT INTO area_avaliacao (nome_area) VALUES (?)");
                    ps.setString(1, token[0]);
                    ps.executeUpdate();
                    ps.close();
                    
                    ps = conn.prepareStatement("SELECT id_area FROM area_avaliacao ORDER BY id_area");
                    try (ResultSet areaAvaliacao = ps.executeQuery()) {

                        if (areaAvaliacao.last()) {
                            idArea = areaAvaliacao.getInt("id_area");
                        }
                    }

                    ps.close();
                }

                i = 1;
                ps = conn.prepareStatement("SELECT * FROM qualis WHERE id_pub_venue = ? AND id_area = ? AND year = ?");
                ps.setInt(i++, idPubVenue);
                ps.setInt(i++, idArea);
                ps.setBigDecimal(i++, BigDecimal.valueOf(journal.getYear()));
                                
                try (ResultSet tableQualis = ps.executeQuery()) {
                    if (tableQualis.first()) {
                        String qualis = tableQualis.getString("qualis");
                        int indicePVAF = getIndice(qualis);
                        int indiceNew = getIndice(token[1]);
                        if(indicePVAF < indiceNew){
                            i = 1;
                            ps = conn.prepareStatement("UPDATE qualis SET qualis = ? WHERE id_pub_venue = ? AND id_area = ? AND year = ?");
                            ps.setString(i++, token[1]);
                            ps.setInt(i++, idPubVenue);
                            ps.setInt(i++, idArea);
                            ps.setBigDecimal(i++, BigDecimal.valueOf(journal.getYear()));
                            ps.executeUpdate();
                            ps.close();
                        }
                    } else {
                        i = 1;
                        ps = conn.prepareStatement("INSERT INTO qualis (id_pub_venue,id_area,year,qualis) VALUES (?,?,?,?)");
                        ps.setInt(i++, idPubVenue);
                        ps.setInt(i++, idArea);
                        ps.setBigDecimal(i++, BigDecimal.valueOf(journal.getYear()));
                        ps.setString(i++, token[1]);
                        ps.executeUpdate();
                        ps.close();
                    }
                }
                ps.close();
            }
            
            for(String journalTitle: journal.getTitles()){
                int idPubVenueAux;
                
                try{
                    idPubVenueAux = TitleDAO.checkIdPubVenue(idPubVenue, journalTitle);
                }catch(InternalErrorException e){
                    throw new InternalErrorException();
                }
                 
                
                if (idPubVenue != idPubVenueAux) {
                    i=1;
                    ps = conn.prepareStatement("INSERT INTO title (id_pub_venue, title) VALUES (?,?)");
                    ps.setInt(i++,idPubVenue);                    
                    ps.setString(i++, journalTitle);
                    ps.executeUpdate();
                    ps.close();
                    conn.commit();
                }
            }
            conn.commit();
        }catch(SQLException e){
            log.error("Ocorreu uma exceção de SQL.", e.fillInStackTrace());            
            if(conn !=null){
		try{
                    conn.rollback();
		}catch(SQLException e1){
                    log.error("Exceção ao realizar rollback.", e1.fillInStackTrace());
                    throw new InternalErrorException();
		}
            }
            throw new InternalErrorException();
        }finally{
            if(conn !=null){
		try{
                    conn.close();
		}catch(SQLException e){
                    log.error("Exceção ao fechar a conexão.", e.fillInStackTrace());
                    throw new InternalErrorException();
		}
            }
	}
    }    
}
