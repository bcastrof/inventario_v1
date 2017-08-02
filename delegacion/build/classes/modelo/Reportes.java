/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import sun.java2d.Disposer;
import ventanas.Repuesto;

/**
 *
 * @author bcastrof
 */
public class Reportes {

    public void reporte() {

        Conexion.conectar();

        JasperReport report;
        try {
            //report = JasperCompileManager.compileReport("D:\\pogramacion\\proyecto\\RETO\\inventario_v1\\inventario_v1\\delegacion\\src\\modelo\\report1.jrxml");
            report = (JasperReport) JRLoader.loadObjectFromFile("D:\\pogramacion\\proyecto\\RETO\\inventario_v1\\inventario_v1\\delegacion\\src\\modelo\\report2.jasper");
            JasperPrint print = JasperFillManager.fillReport(report, new HashMap(), Conexion.getConexion());
            JasperViewer ver = new JasperViewer(print,false);
            ver.setTitle("INVENTARIO ACTUAL");
            ver.setVisible(true);
          
        } catch (JRException ex) {
            Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void hojaInventario() {
        Conexion.conectar();

        try {
            JasperReport report = (JasperReport) JRLoader.loadObjectFromFile("D:\\pogramacion\\proyecto\\RETO\\inventario_v1\\inventario_v1\\delegacion\\src\\modelo\\report1.jasper");
            JasperPrint print = JasperFillManager.fillReport(report, new HashMap(), Conexion.getConexion());
            JasperViewer ver = new JasperViewer(print,false); // cierro el visor sin cerrar la apli
            ver.setTitle("HOJA DE INVENTARIO");
            ver.setVisible(true);
           
        } catch (JRException ex) {
            Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void pedidoToner() {
        Conexion.conectar();

        try {
            JasperReport report = (JasperReport) JRLoader.loadObjectFromFile("D:\\pogramacion\\proyecto\\RETO\\inventario_v1\\inventario_v1\\delegacion\\src\\modelo\\report3.jasper");
            JasperPrint print = JasperFillManager.fillReport(report, new HashMap(), Conexion.getConexion());
            JasperViewer ver = new JasperViewer(print,false);
            ver.setTitle("HOJA PEDIDO");
            ver.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
