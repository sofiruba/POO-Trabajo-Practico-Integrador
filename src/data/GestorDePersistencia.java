package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GestorDePersistencia {
    private static final String URL = "jdbc:mysql://localhost:3306/plataforma_cursos";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql"; // cambiar si usás otra

    private static Connection conexion = null;

    // Singleton: una sola conexión abierta
    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión exitosa a MySQL");
            } catch (ClassNotFoundException e) {
                System.out.println("Error: no se encontró el driver JDBC de MySQL");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Error al conectar con la base de datos");
                e.printStackTrace();
            }
        }
        return conexion;
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("🔒 Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
