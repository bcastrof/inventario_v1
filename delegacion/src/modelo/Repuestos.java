/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.oracore.OracleType;

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
    
    public static List<Repuestos> listarRepuestos(String iRef){
        List <Repuestos> repuestos = new ArrayList<>();
        Conexion.conectar();
        
        String sql = "call listarRepuestos (?,?)";
        
        try {
            CallableStatement cs = Conexion.getConexion().prepareCall(sql);
            cs.setString(1, iRef);
            cs.registerOutParameter(2, OracleTypes.CURSOR);
            cs.execute();
            
            ResultSet rs = (ResultSet) cs.getObject(2);
            
            while(rs.next()){
                Repuestos r = new Repuestos(rs.getString(1),rs.getString(2), rs.getString(3), rs.getInt(4));
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
}
