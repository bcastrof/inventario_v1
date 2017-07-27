/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author bcastrof
 */
public class Conexion {
    
    private static Connection conexion;
 
    public static Connection getConexion() {
        return conexion;
    }
    
    
    public static void conectar() {
        try { 
         
          
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String login="impresoras";
            String pass="deh74f5c";
            String url="jdbc:oracle:thin:@localhost:1521:xe";
            conexion= DriverManager.getConnection(url, login, pass);
            conexion.setAutoCommit(true);
                  
        } catch (SQLException  | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un problema \n"+ex.getMessage()); 
           
        }
    }
    
    public static void desconectar() throws SQLException{
        conexion.close();
         
}
    
}
