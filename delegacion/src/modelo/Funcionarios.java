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
public class Funcionarios {

    private String apellidos;
    private String nombre;
    private String user;
    private String xlnet;
    private String correo;
    private int telefono;
    private int planta;
    private String equipo;
    private String impresora;

    public Funcionarios() {
    }

    public Funcionarios(String apellidos, String nombre, String user, String xlnet, String correo, int telefono, int planta, String equipo, String impresora) {
        this.apellidos = apellidos;
        this.nombre = nombre;
        this.user = user;
        this.xlnet = xlnet;
        this.correo = correo;
        this.telefono = telefono;
        this.planta = planta;
        this.equipo = equipo;
        this.impresora = impresora;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getXlnet() {
        return xlnet;
    }

    public void setXlnet(String xlnet) {
        this.xlnet = xlnet;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public int getPlanta() {
        return planta;
    }

    public void setPlanta(int planta) {
        this.planta = planta;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getImpresora() {
        return impresora;
    }

    public void setImpresora(String impresora) {
        this.impresora = impresora;
    }

    public boolean altaUsuarios() {
        Conexion.conectar();

        String sql = "insert into funcionarios values (?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);

            ps.setString(1, apellidos);
            ps.setString(2, nombre);
            ps.setString(3, user);
            ps.setString(4, xlnet);
            ps.setString(5, correo);
            ps.setInt(6, telefono);
            ps.setInt(7, planta);
            ps.setString(8, equipo);
            ps.setString(9, impresora);

            ps.executeUpdate();
            ps.close();
            Conexion.desconectar();

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Funcionarios.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean modificarUsuario(String user){
        Conexion.conectar();
        String sql = "update funcionarios set "
                + "APELLIDOS=?, "
                + "NOMBRE=?, "
                + "USUARIO=?, "
                + "XLNET=?, "
                + "CORREO=?, "
                + "PLANTA=?,"
                + "TELEFONO=?,"
                + "EJ_EQUIPO=?,"
                + "EJ_IMPRESORA=?"
                + "WHERE USUARIO=?";
        
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, apellidos);
            ps.setString(2, nombre);
            ps.setString(3, this.user);
            ps.setString(4, xlnet);
            ps.setString(5, correo);
            ps.setInt(6, telefono);
            ps.setInt(7, planta);
            ps.setString(8, equipo);
            ps.setString(9, impresora);
            ps.setString(10, user);
            
            ps.executeUpdate();
            ps.close();
            Conexion.desconectar();
            
            return true;
            
        } catch (SQLException ex) {
            Logger.getLogger(Funcionarios.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public static boolean borrarUsuario(String usuario){
        Conexion.conectar();
        String sql = "delete from funcionarios where usuario=?";
        
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            
            ps.setString(1, usuario);
            ps.execute();
            ps.close();
            Conexion.desconectar();
            
            return true;
            
        } catch (SQLException ex) {
            Logger.getLogger(Funcionarios.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }

    public static List<Funcionarios> recuperarUsuarios() {
        List<Funcionarios> funcionarios = new ArrayList<>();
        Funcionarios f;
        Conexion.conectar();
        String sql = "Call recuperarUsuarios (?)";
        try {
            CallableStatement cs = Conexion.getConexion().prepareCall(sql);
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();

            ResultSet rs = (ResultSet) cs.getObject(1);

            while (rs.next()) {
                f = new Funcionarios(rs.getString("APELLIDOS"),
                        rs.getString("NOMBRE"),
                        rs.getString("USUARIO"),
                        rs.getString("XLNET"),
                        rs.getString("CORREO"),
                        rs.getInt("TELEFONO"),
                        rs.getInt("PLANTA"),
                        rs.getString("EJ_EQUIPO"),
                        rs.getString("EJ_IMPRESORA"));
                funcionarios.add(f);
            }
            cs.close();
            rs.close();
            Conexion.desconectar();
            return funcionarios;

        } catch (SQLException ex) {
            Logger.getLogger(Funcionarios.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static Funcionarios recuperarUsuario(String ej) {
        Conexion.conectar();
        Funcionarios usuario;
        String sql = "call recuperarUsuario(?,?,?,?,?,?,?,?,?,?)";

        try {
            CallableStatement cs = Conexion.getConexion().prepareCall(sql);
            cs.setString(1, ej);
            cs.registerOutParameter(2, OracleTypes.VARCHAR);
            cs.registerOutParameter(3, OracleTypes.VARCHAR);
            cs.registerOutParameter(4, OracleTypes.VARCHAR);
            cs.registerOutParameter(5, OracleTypes.VARCHAR);
            cs.registerOutParameter(6, OracleTypes.VARCHAR);
            cs.registerOutParameter(7, OracleTypes.INTEGER);
            cs.registerOutParameter(8, OracleTypes.INTEGER);
            cs.registerOutParameter(9, OracleTypes.VARCHAR);
            cs.registerOutParameter(10, OracleTypes.VARCHAR);
            cs.execute();

            usuario = new Funcionarios(cs.getString(2),
                    cs.getString(3),
                    cs.getString(4),
                    cs.getString(5),
                    cs.getString(6),
                    cs.getInt(7),
                    cs.getInt(8),
                    cs.getString(9),
                    cs.getString(10));

            cs.close();
            Conexion.desconectar();

            return usuario;

        } catch (SQLException ex) {
            Logger.getLogger(Funcionarios.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void autoCompletarEquipo(javax.swing.JTextField equipoJTextField) {

        TextAutoCompleter equipoAutoComplet = new TextAutoCompleter(equipoJTextField);

        Conexion.conectar();

        // Statement st;
        CallableStatement cs;
        ResultSet rs;

        try {
            // st = (Statement) Conexion.getConexion().createStatement();
            cs = (CallableStatement) Conexion.getConexion().prepareCall("call listarPcs (?)");
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();
            // rs=st.executeQuery("select ej from pcs");
            rs = (ResultSet) cs.getObject(1);
            while (rs.next()) {

                equipoAutoComplet.addItem(rs.getString("EJ"));

            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se ha encontrado el equipo: " + ex.getMessage());
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
            JOptionPane.showMessageDialog(null, "No se ha encontrado el equipo: " + ex.getMessage());
        }
    }

    public static void autoCompletarUsuario(javax.swing.JTextField usuarioJTextField) {

        TextAutoCompleter equipoAutoComplet = new TextAutoCompleter(usuarioJTextField);

        Conexion.conectar();

        // Statement st;
        CallableStatement cs;
        ResultSet rs;

        try {
            // st = (Statement) Conexion.getConexion().createStatement();
            cs = (CallableStatement) Conexion.getConexion().prepareCall("call recuperarUsuarios (?)");

            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();
            // rs=st.executeQuery("select ej from pcs");
            rs = (ResultSet) cs.getObject(1);
            while (rs.next()) {

                equipoAutoComplet.addItem(rs.getString("USUARIO"));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se ha encontrado el equipo: " + ex.getMessage());
        }
    }

}
