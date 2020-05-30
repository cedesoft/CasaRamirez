package conexion;

import java.sql.*;
import javax.swing.*;

/**
 *
 * @author oscar
 */

public class ConexionBD {

    Connection conect = null;

    public Connection conexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conect = DriverManager.getConnection("jdbc:mysql://localhost/casar", "root", ""); 
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en la conexión" + e);
        }
        return conect;
    }   
}
