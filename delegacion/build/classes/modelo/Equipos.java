/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import com.mxrck.autocompleter.TextAutoCompleter;
import oracle.jdbc.OracleTypes;

import javax.swing.*;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bcastrof
 */
public class Equipos {
    
    private String ej;
    private String marca;
    private String modelo;
    private String descripcion;

    public Equipos() {
    }

    public Equipos(String ej, String marca, String modelo, String descripcion) {
        this.ej = ej;
        this.marca = marca;
        this.modelo = modelo;
        this.descripcion = descripcion;
    }

    public String getEj() {
        return ej;
    }

    public void setEj(String ej) {
        this.ej = ej;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public boolean altaEquipo(){
        Conexion.conectar();      
        String sql = "insert into PCS (EJ, MARCA, MODELO, DESCRIPCION) VALUES (?,?,?,?)";
        
        try {
            PreparedStatement smt = Conexion.getConexion().prepareStatement(sql);
            smt.setString(1, ej);
            smt.setString(2, marca);
            smt.setString(3, modelo);
            smt.setString(4, descripcion);
            
            smt.executeUpdate();
            smt.close();
            Conexion.desconectar();
            return true;
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se ha podido dar de alta el equipo" + ex.getMessage());
            return false;
        }     
    }
    
    public static List<Equipos>listarPcs(){
        List<Equipos> equipo = new ArrayList();
        Conexion.conectar();
        
        String sql = "call listarPcs (?)";
        
        try {
            CallableStatement cs = Conexion.getConexion().prepareCall(sql);
            
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();
            
            ResultSet rs = (ResultSet) cs.getObject(1);
            
            while (rs.next()) {
               Equipos e = new Equipos(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
               equipo.add(e);
              
            }
            rs.close();
            cs.close();
            Conexion.desconectar();
            return equipo;
            
        } catch (SQLException ex) {
            Logger.getLogger(Equipos.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static Equipos buscarPC(String ej){
        
        Equipos equipo;
        
        Conexion.conectar();
        String sql = "call recuperarPC (?,?,?,?,?) ";
        
        try {
            CallableStatement cs = Conexion.getConexion().prepareCall(sql);
            cs.setString(1, ej);
            cs.registerOutParameter(2, OracleTypes.VARCHAR);
            cs.registerOutParameter(3, OracleTypes.VARCHAR);
            cs.registerOutParameter(4, OracleTypes.VARCHAR);
            cs.registerOutParameter(5, OracleTypes.VARCHAR);
            cs.execute();
            
            equipo = new Equipos(cs.getString(2), cs.getString(3), cs.getString(4), cs.getString(5));
            
            cs.close();
            Conexion.desconectar();
            
            return equipo;
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se ha encontrado el equipo: " + ex.getMessage());
            return null;
        }
        
    }
    
    public static void autoCompletar (javax.swing.JTextField filtro){
        
    TextAutoCompleter equipoAutoComplet = new TextAutoCompleter (filtro);
    
      Conexion.conectar();
        
        // Statement st;
       CallableStatement cs;
        ResultSet rs;
        
        try {
           // st = (Statement) Conexion.getConexion().createStatement();
            cs = (CallableStatement) Conexion.getConexion().prepareCall("call ejEquipo (?)");
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();
           // rs=st.executeQuery("select ej from pcs");
            rs = (ResultSet) cs.getObject(1);
            while (rs.next()){
                
                equipoAutoComplet.addItem(rs.getString("EJ"));
                
            }
            
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, "No se ha encontrado el equipo: " + ex.getMessage());
        }
        
    }
    
    public boolean modicarEquipo(String e){
        
        Conexion.conectar();
        
        String sql = "update pcs set ej=?, marca=?, modelo=?, descripcion=? where ej=?";
        
        try {
            PreparedStatement smt = Conexion.getConexion().prepareStatement(sql);
            
            smt.setString(1, ej);
            smt.setString(2, marca);
            smt.setString(3,modelo);
            smt.setString(4, descripcion);
            smt.setString(5, e);
            
            smt.executeUpdate();
            smt.close();
            Conexion.desconectar();
            return true;
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se ha podido modificar el equipo \n" + ex.getMessage());
            return false;
        }      
    }
    
    public static boolean  borrarEquipo (String e){
        Conexion.conectar();
        
        String sql = "delete from pcs where ej=?";
        
        try {
            PreparedStatement smt = Conexion.getConexion().prepareStatement(sql);
            smt.setString(1, e);
            smt.execute();
            smt.close();
            Conexion.desconectar();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se ha podido borrar el equipo \n" + ex.getMessage());
            return false;
        }
    }
    
}
