/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producto;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import conexion.ConexionBD;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author oscar
 */
public class Opciones {

    static ConexionBD cc = new ConexionBD();
    static Connection cn = cc.conexion();
    static PreparedStatement ps;
    static ResultSet rs;
    

    public static int registrar(Sentencias uc) {
        int rsu = 0;
        String sql = Sentencias.REGISTRAR;
        try {
            ps = cn.prepareStatement(sql);
            ps.setString(1, uc.getNombre());
            ps.setString(2, uc.getDescripcion());
            ps.setString(3, uc.getTipo());
            ps.setDouble(4, uc.getPrecio());
            ps.setString(5, uc.getStock());
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        System.out.println(sql);
        return rsu;
    }

    public static int actualizar(Sentencias uc) {
        int rsu = 0;
        String sql = Sentencias.ACTUALIZAR;
        try {
            ps = cn.prepareStatement(sql);
            ps.setString(1, uc.getNombre());
            ps.setString(2, uc.getDescripcion());
            ps.setString(3, uc.getTipo());
            ps.setDouble(4, uc.getPrecio());
            ps.setString(5, uc.getStock());
            ps.setInt(6, uc.getId());
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
        }
        System.out.println(sql);
        return rsu;
    }
    
    public static int actualizarStock(Sentencias uc) {
        int rsu = 0;
        String sql = Sentencias.ACTUALIZAR_STOCK;
        try {
            ps = cn.prepareStatement(sql);
            ps.setString(1, uc.getStock());
            ps.setInt(2, uc.getId());
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
        }
        System.out.println(sql);
        return rsu;
    }
    
    public static int eliminar(int id) {
        int rsu = 0;
        String sql = Sentencias.ELIMINAR;

        try {
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println(sql);
        return rsu;
    }
    public static int imprimirE(int id){
        int id2 = id;
        Image img;
        ResultSet rs;
        try {
            ps = cn.prepareStatement("SELECT * FROM producto WHERE idproducto =" + id2 +"");            
            rs=ps.executeQuery();
            Document doc= new Document();
            PdfWriter pdf=PdfWriter.getInstance(doc, new FileOutputStream("codigos.pdf"));
            doc.open();
            PdfPTable table = new PdfPTable(3);
            Barcode128 code =new Barcode128();
            float [] columnWidths = {3f, 3f, 3f};
            table.setWidths (columnWidths);
            while(rs.next()){               
            code.setCode(rs.getString("idproducto"));
            img = code.createImageWithBarcode(pdf.getDirectContent(), BaseColor.BLACK, BaseColor.BLACK);
            
            img.scalePercent(150);
            int x=10;
            int y=780;
            int z=50;
           
            for(int i=0; i<8; i++){
              img.setAbsolutePosition(x,y);
              doc.add(img);              
              x=x+70;
            }
            x=5;
             for(int a=0; a<15;a++){
                for(int i=0; i<8; i++){
                  img.setAbsolutePosition(x,y-z);
                  doc.add(img);              
                  x=x+70;
                }
                x=10;
                z=z+50;
             }
            File objetofile = new File ("codigos.pdf");
            Desktop.getDesktop().open(objetofile);
            
            
            }
            doc.close();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Opciones.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(Opciones.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Opciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println();
        return id2;
    }
    
    public static int eliminarTodo() {
        int rsu = 0;
        String sql = Sentencias.ELIMINAR_TODO;

        try {
            ps = cn.prepareStatement(sql);
            rsu = ps.executeUpdate();
            rsu++;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println(sql);
        return rsu;
    }
    

    public static void listar(String busca) {
        DefaultTableModel modelo = (DefaultTableModel) producto.Productos.tabla.getModel();

        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
        String sql = "";
        if (busca.equals("")) {
            sql = Sentencias.LISTAR;
        } else {
            sql = "SELECT * FROM producto WHERE (idproducto LIKE'" + busca + "%' OR "
                    + "nombre LIKE'" + busca + "%' OR descripcion LIKE'" + busca + "%' OR "
                    + "inscant LIKE'" + busca + "%' OR precio LIKE'" + busca + "%' OR "
                    + "idproducto LIKE'" + busca + "%') "
                    + "ORDER BY idproducto";
        }
        String datos[] = new String[6];
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                datos[0] = rs.getString("idproducto");
                datos[1] = rs.getString("nombre");
                datos[2] = rs.getString("descripcion");
                datos[3] = rs.getString("inscant");
                datos[4] = rs.getString("precio");
                datos[5] = rs.getString("stock");
                modelo.addRow(datos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Opciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static int extraerID() {
        int c = 0;
        String SQL = "SELECT MAX(idproducto) FROM producto";

        try {
             Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                c = rs.getInt(1);
            }
                      
        } catch (SQLException ex) {
            Logger.getLogger(Opciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }
    
    public static void iniciarTransaccion(){
        try {
            cn.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(Opciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void finalizarTransaccion(){
        try {
            cn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(Opciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void cancelarTransaccion(){
        try {
            cn.rollback();
        } catch (SQLException ex) {
            Logger.getLogger(Opciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
