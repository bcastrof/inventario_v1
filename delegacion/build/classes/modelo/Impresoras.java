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

public class Impresoras {

    private String ej;
    private String marca;
    private String modelo;
    private String tipo;
    private String ubicacion;
    private String descripcion;
    private String cd_repuesto;

    public Impresoras() {
    }

    public Impresoras(String ej, String marca, String modelo, String tipo, String ubicacion, String descripcion) {
        this.ej = ej;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
    }

    public Impresoras(String ej, String marca, String modelo, String tipo, String ubicacion, String descripcion, String cd_repuesto) {
        this.ej = ej;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.cd_repuesto = cd_repuesto;
    }

    public Impresoras(String cd_repuesto, String ej) {
        this.cd_repuesto = cd_repuesto;
        this.ej = ej;
    }

    public Impresoras(String ej) {
        this.ej = ej;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCd_repuesto() {
        return cd_repuesto;
    }

    public void setCd_repuesto(String cd_repuesto) {
        this.cd_repuesto = cd_repuesto;
    }

    public static List<Impresoras> listarImpresoras() {
        Conexion.conectar();
        String sql = "call listarImpresoras (?)";

        List<Impresoras> impresoras = new ArrayList<>();

        try {
            CallableStatement cs = Conexion.getConexion().prepareCall(sql);
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet rs = (ResultSet) cs.getObject(1);

            while (rs.next()) {

                Impresoras i = new Impresoras(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6));

                impresoras.add(i);
            }
            rs.close();
            cs.close();
            Conexion.desconectar();

            return impresoras;

        } catch (SQLException ex) {
            Logger.getLogger(Impresoras.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static Impresoras recuperarImpresora(String eji) {
        Conexion.conectar();
        String sql = "call recuperarImpresora (?,?,?,?,?,?,?)";
        Impresoras impresora;
        try {
            CallableStatement cs = Conexion.getConexion().prepareCall(sql);
            cs.setString(1, eji);
            cs.registerOutParameter(2, OracleTypes.VARCHAR);
            cs.registerOutParameter(3, OracleTypes.VARCHAR);
            cs.registerOutParameter(4, OracleTypes.VARCHAR);
            cs.registerOutParameter(5, OracleTypes.VARCHAR);
            cs.registerOutParameter(6, OracleTypes.VARCHAR);
            cs.registerOutParameter(7, OracleTypes.VARCHAR);
            cs.execute();

            impresora = new Impresoras(cs.getString(2),
                    cs.getString(3),
                    cs.getString(4),
                    cs.getString(5),
                    cs.getString(6),
                    cs.getString(7));

            cs.close();
            Conexion.desconectar();

            return impresora;

        } catch (SQLException ex) {
            Logger.getLogger(Impresoras.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean altaImpresora() {
        Conexion.conectar();
        String sql = "insert into impresoras values (?,?,?,?,?,?)";
      
        PreparedStatement ps;
        try {
            ps = Conexion.getConexion().prepareStatement(sql);

            ps.setString(1, ej);
            ps.setString(2, marca);
            ps.setString(3, modelo);
            ps.setString(4, tipo);
            ps.setString(5, ubicacion);
            ps.setString(6, descripcion);
            ps.executeUpdate();
            ps.close();
            
            Conexion.desconectar();
            
            return true;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se ha podido dar de alta la impresora\n" + ex.getMessage());
            return false;
        }
    }
    
    public String altaTienen() {
          Conexion.conectar();
          String sql2 = "insert into tienen values (?,?)";
          String mensaje="OK";
          PreparedStatement ps;
          
        try {
            ps = Conexion.getConexion().prepareStatement(sql2);
             ps.setString(1, cd_repuesto);
            ps.setString(2, ej);
            ps.execute();
            ps.close();
            
            Conexion.desconectar();
            return mensaje;
        } catch (SQLException ex) {
           Logger.getLogger(Impresoras.class.getName()).log(Level.SEVERE, null, ex); 
          int error = ex.getErrorCode();
          
          if(error==2291){
              mensaje="Referencia de repuesto incorrecta, por favor intente de nuevo";
              return mensaje;
          }else{
              return ex.getMessage();
          }        
        }       
    }

    public boolean borrarImpresora(String eji) {
        Conexion.conectar();
        String sql = "delete from impresoras where EJ_IMPRESORA=?";

        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, eji);
            ps.execute();

            ps.close();
            Conexion.desconectar();

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Impresoras.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean editarImpresora(String eji) {
        Conexion.conectar();

        String sql = "update impresoras set EJ_IMPRESORA=?, MARCA=?, MODELO=?, TIPO=?, UBICACION=?, DESCRIPCION=?"
                + "WHERE EJ_IMPRESORA=?";

        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, ej);
            ps.setString(2, marca);
            ps.setString(3, modelo);
            ps.setString(4, tipo);
            ps.setString(5, ubicacion);
            ps.setString(6, descripcion);
            ps.setString(7, eji);

            ps.executeUpdate();
            ps.close();
            Conexion.desconectar();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Impresoras.class.getName()).log(Level.SEVERE, null, ex);
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
            cs = (CallableStatement) Conexion.getConexion().prepareCall("call listarImpresoras (?)");

            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();
            // rs=st.executeQuery("select ej from pcs");
            rs = (ResultSet) cs.getObject(1);
            while (rs.next()) {

                equipoAutoComplet.addItem(rs.getString("EJ_IMPRESORA"));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se ha encontrado la impresora: " + ex.getMessage());
        }
    }

}
