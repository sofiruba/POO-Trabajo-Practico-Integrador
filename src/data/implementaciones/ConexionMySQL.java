package data.implementaciones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {

    // Cambia estos valores según tu configuración de MAMPP/XAMPP
    private static final String URL = "jdbc:mysql://localhost:3306/nombre_tu_base_datos";
    private static final String USUARIO = "root";
    private static final String CLAVE = ""; // Clave de MAMPP/XAMPP

    /**
     * Establece y retorna la conexión a la base de datos MySQL.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Cargar el driver (necesitas el JAR del conector MySQL en tu Classpath)
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (ClassNotFoundException e) {
            // Error si el conector JDBC no se encuentra
            throw new SQLException("Error al cargar el driver JDBC: " + e.getMessage());
        }
    }
}