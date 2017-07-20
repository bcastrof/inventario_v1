/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import com.mxrck.autocompleter.TextAutoCompleter;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author bcastrof
 */
public class Repuestos {
    private String cd_repuesto;
    private String marca;
    private String color;
    private int cantidad;

    public Repuestos() {
    }

    public Repuestos(int cantidad) {
        this.cantidad = cantidad;
    }

    public Repuestos(String cd_repuesto, String marca, String color, int cantidad) {
        this.cd_repuesto = cd_repuesto;
        this.marca = marca;
        this.color = color;
        this.cantidad = cantidad;
    }

    public String getCd_repuesto() {
        return cd_repuesto;
    }

    public void setCd_repuesto(String cd_repuesto) {
        this.cd_repuesto = cd_repuesto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public static List<Repuestos> listarRepuestosImpresora(String iRef){
        List <Repuestos> repuestos = new ArrayList<>();
        Conexion.conectar();
        
        String sql = "call listarRepuestosImpresora (?,?)";
        
        try {
            CallableStatement cs = Conexion.getConexion().prepareCall(sql);
            cs.setString(1, iRef);
            cs.registerOutParameter(2, OracleTypes.CURSOR);
            cs.execute();
            
            ResultSet rs = (ResultSet) cs.getObject(2);
            
            while(rs.next()){
                Repuestos r = new Repuestos(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4));
                repuestos.add(r);
            }
            cs.close();
            Conexion.desconectar();
            return repuestos; 
            
        } catch (SQLException ex) {
            Logger.getLogger(Repuestos.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }   
    }
    
    public static List<Repuestos>todosLosRepuestos(){
        List <Repuestos> repuestos = new ArrayList<>();
        Conexion.conectar();
        String sql = "call todosLosRepuestos(?)";
        
        try {
            CallableStatement cs = Conexion.getConexion().prepareCall(sql);
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();
            
            ResultSet rs = (ResultSet) cs.getObject(1);
            
            while(rs.next()){
                Repuestos r = new Repuestos(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4));
                repuestos.add(r);
            }
            cs.close();
            Conexion.desconectar();
            
            return repuestos;
        } catch (SQLException ex) {
            Logger.getLogger(Repuestos.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static Repuestos recuperarRepuesto(String cd_repuesto){
        Repuestos repuesto;
        Conexion.conectar();
        String sql = "call recuperarRepuesto (?,?,?,?,?)";
        try {
            CallableStatement cs = Conexion.getConexion().prepareCall(sql);
            cs.setString(1, cd_repuesto);
            cs.registerOutParameter(2, OracleTypes.VARCHAR);
            cs.registerOutParameter(3, OracleTypes.VARCHAR);
            cs.registerOutParameter(4, OracleTypes.VARCHAR);
            cs.registerOutParameter(5, OracleTypes.INTEGER);
            cs.execute();
            
            repuesto = new Repuestos(cs.getString(2), cs.getString(3), cs.getString(4), cs.getInt(5));
            return repuesto;
        } catch (SQLException ex) {
            Logger.getLogger(Repuestos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public boolean modificarRepuesto(String ref){
        Conexion.conectar();
        String sql = "update repuestos set CANTIDAD = ? WHERE CD_REPUESTO= ?";
        
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, cantidad);
            ps.setString(2, ref);
            ps.executeUpdate();
            ps.close();
            Conexion.desconectar();
            return  true;
        } catch (SQLException ex) {
            Logger.getLogger(Repuestos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
    public boolean altaRepuesto(){
        Conexion.conectar();
        String sql = "insert into repuestos values (?,?,?,?)";
        
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, cd_repuesto);
            ps.setString(2, marca);
            ps.setString(3, color);
            ps.setInt(4, cantidad);
            ps.execute();
            ps.close();
            Conexion.desconectar();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Repuestos.class.getName()).log(Level.SEVERE, null, ex);
             return false;
        }    
    }
    
    public static boolean borrarRepuesto(String ref){
        Conexion.conectar();
        
        String sql = "delete  from REPUESTOS where CD_REPUESTO=?";
        
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, ref);
            ps.execute();
            ps.close();
            Conexion.desconectar();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Repuestos.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }  
    }
    
    
    public static void autoCompletarImpresora(javax.swing.JTextField impresoraJTextField) {

        TextAutoCompleter equipoAutoComplet = new TextAutoCompleter(impresoraJTextField);

        Conexion.conectar();

        // Statement st;
        CallableStatement cs;
        ResultSet rs;

        try {
            // st = (Statement) Conexion.getConexion().createStatement();
            cs = (CallableStatement) Conexion.getConexion().prepareCall("call todosLosRepuestos (?)");

            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();
            // rs=st.executeQuery("select ej from pcs");
            rs = (ResultSet) cs.getObject(1);
            while (rs.next()) {

                equipoAutoComplet.addItem(rs.getString("CD_REPUESTO"));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se ha encontrado el repuesto: " + ex.getMessage());
        }
    }
}
