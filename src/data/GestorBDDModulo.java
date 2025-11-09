package data;

import modelos.cursos.Modulo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorBDDModulo {
    private Connection conn;
    
    // Asumo que tienes una forma de obtener la conexión, similar a otros gestores
    // Aquí utilizo la configuración de GestorBDDCurso como ejemplo:
    public GestorBDDModulo() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/plataforma_cursos", 
                    "root", 
                    "mysql" 
            );
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con la BDD (Módulo): " + e.getMessage());
        }
    }

    public Modulo guardar(Modulo modulo, int idCurso) {
        String sql = "INSERT INTO modulo (idCurso, titulo, contenido) VALUES (?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idCurso);
            ps.setString(2, modulo.getTitulo());
            ps.setString(3, modulo.getContenido());
            
            ps.executeUpdate();
            
            // Sincronizar el ID de la BDD
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    modulo.setIdModulo(idGenerado); // Necesitas este setter en Modulo.java
                }
            }
            System.out.println("💾 Módulo '" + modulo.getTitulo() + "' guardado correctamente.");

        } catch (SQLException e) {
            System.err.println("❌ Error al guardar módulo: " + e.getMessage());
        }
        return modulo;
    }

    public List<Modulo> obtenerModulosPorCurso(int idCurso) {
    List<Modulo> modulos = new ArrayList<>();
    // La consulta busca todos los módulos que coincidan con el idCurso
    String sql = "SELECT idModulo, titulo, contenido FROM modulo WHERE idCurso = ?";
    
    if (conn == null) {
        System.err.println("❌ No hay conexión a la base de datos para obtener módulos.");
        return modulos;
    }
    
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idCurso);
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Reconstruir el objeto Modulo
                Modulo modulo = new Modulo(
                    rs.getString("titulo"),
                    rs.getString("contenido")
                );
                // ⚠️ CLAVE: Sincronizar el ID de la BDD
                modulo.setIdModulo(rs.getInt("idModulo")); 
                modulos.add(modulo);
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al obtener módulos por curso: " + e.getMessage());
    }
    return modulos;
}

// Archivo: GestorBDDModulo.java

public Modulo buscarModuloPorTituloYCurso(String titulo, int idCurso) {
    // La consulta busca un módulo que coincida tanto en título como en idCurso
    String sql = "SELECT idModulo, titulo, contenido FROM modulo WHERE titulo = ? AND idCurso = ?";
    
    // Asumo que la conexión (conn) no es nula.
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, titulo);
        ps.setInt(2, idCurso);
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                // Reconstruir el objeto Modulo si se encuentra
                Modulo modulo = new Modulo(
                    rs.getString("titulo"),
                    rs.getString("contenido")
                );
                // Sincronizar el ID de la BDD
                modulo.setIdModulo(rs.getInt("idModulo")); 
                return modulo;
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al buscar módulo por título y curso: " + e.getMessage());
    }
    return null;
}
}