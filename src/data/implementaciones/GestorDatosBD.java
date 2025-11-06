package data.implementaciones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class GestorDatosBD {

    private static final String URL = "jdbc:mysql://localhost:3306/plataforma_cursos";
    private static final String USUARIO = "root";
    private static final String CLAVE = ""; // Contraseña de MAMPP/XAMPP
    protected Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (ClassNotFoundException e) {

            throw new SQLException("Error: El driver JDBC no se encontró. Asegúrate de incluir el JAR.", e);
        }
    }

}