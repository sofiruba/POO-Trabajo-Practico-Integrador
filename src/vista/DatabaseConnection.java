package vista;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class DatabaseConnection {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String BBDD = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "negocio";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    public Connection conexionBBDD() {
        Connection connection = null;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(BBDD + DB_NAME, USUARIO, PASSWORD);
            System.out.println("Conexion OK");
        } catch (ClassNotFoundException e) {
            System.err.println("Error en DRIVER\n" + e);
        } catch (SQLException e) {
            System.err.println("Error al conectar con la BBDD\n" + e);
        }
        return connection;
    }
    public void cerrarConexion(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Se ha producido un error al cerrar la conexión con la base de datos." + e);
        }
    }
    /// /////////////////////
    public void createDatabaseAndTable() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(BBDD, USUARIO, PASSWORD);
            statement = connection.createStatement();

            // Crear la base de datos si no existe
            String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            statement.executeUpdate(sql);

            // Usar la base de datos
            statement.executeUpdate("USE " + DB_NAME);

            // Crear la tabla si no existe
            sql = "CREATE TABLE IF NOT EXISTS cliente (" +
                    "idCliente INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(50)  NOT NULL, " +
                    "telefono  VARCHAR NOT NULL, " ;
            statement.executeUpdate(sql);
            System.out.println("Base de datos y tabla creadas correctamente.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error en Driver.\n" + e);
        } catch (SQLException e) {
            System.err.println("Error en la conexión con Base de datos.\n" + e);
        }
    }
    /// //////////////////////////////
    public void insertData() {
        Connection connection = conexionBBDD();
        if (connection != null) {
            try {
                Random random = new Random();
                int num1 = random.nextInt(100)+1; //1 y 100
                int num2 = random.nextInt(100)+1;
                int num3 = random.nextInt(100)+1;
                int num4 = random.nextInt(100)+1;
                int num5 = random.nextInt(100)+1;
                int complementario = random.nextInt(10)+1;

                String consultaInsercion = String.format(
                        "INSERT INTO cliente (idCliente, nombre, telefono) " +
                                "VALUES (''null', '%s', '%s);",
                        "Maria", "115689777"
                );

                System.out.println(consultaInsercion);
                Statement consulta = connection.createStatement();
                consulta.executeUpdate(consultaInsercion);
                System.out.println("Datos insertados correctamente");
                consulta.close();
            } catch (SQLException e) {
                System.err.println("Se ha producido un error al insertar en la base de datos.\n" + e);
            } finally {
                cerrarConexion(connection);
            }
        }
    } }