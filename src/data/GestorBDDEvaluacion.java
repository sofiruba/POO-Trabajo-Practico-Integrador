package data;

import modelos.cursos.Evaluacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorBDDEvaluacion {
    // Usar la misma configuración de conexión que tus otros gestores
    private static final String URL = "jdbc:mysql://localhost:3306/plataforma_cursos";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql"; 
    
    public Evaluacion guardar(Evaluacion evaluacion, int idModulo) {
        String sql = "INSERT INTO evaluacion (idModulo, nombre, descripcion, nota_maxima) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idModulo);
            ps.setString(2, evaluacion.getNombre());
            ps.setString(3, evaluacion.getDescripcion());
            ps.setFloat(4, evaluacion.getNotaMaxima());
            
            ps.executeUpdate();
            
            // Sincronizar el ID de la BDD
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    evaluacion.setIdEval(idGenerado);
                }
            }
            System.out.println("💾 Evaluación  guardada.");

        } catch (SQLException e) {
            System.err.println("❌ Error al guardar evaluación: " + e.getMessage());
        }
        return evaluacion;
    }

    public Evaluacion buscarEvaluacionPorNombreYModulo(String nombre, int idModulo) {
    // La consulta busca una evaluación que coincida en nombre y en idModulo
    String sql = "SELECT idEvaluacion, nombre, descripcion, nota_maxima FROM evaluacion WHERE nombre = ? AND idModulo = ?";
    
    // Asumo que la conexión (conn) no es nula.
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, nombre);
        ps.setInt(2, idModulo);
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                // Reconstruir el objeto Evaluacion si se encuentra
                Evaluacion evaluacion = new Evaluacion(
                    rs.getString("nombre"),
                
                    rs.getString("descripcion"),
                    rs.getFloat("nota_maxima")
                );
                // Sincronizar el ID de la BDD
                evaluacion.setIdEval(rs.getInt("idEvaluacion")); 
                return evaluacion;
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al buscar evaluación por nombre y módulo: " + e.getMessage());
    }
    return null;
}

public Evaluacion buscarEvaluacionPorId(int idEvaluacion) {
    String sql = "SELECT idEvaluacion, nombre, descripcion, nota_maxima, idModulo FROM evaluacion WHERE idEvaluacion = ?";
    
    // Nota: Asumo que la conexión (URL, USER, PASSWORD) está definida.
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idEvaluacion);
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                // 1. Reconstruir el objeto Evaluacion usando los datos de la BDD
                Evaluacion evaluacion = new Evaluacion(
                    rs.getString("nombre"),
                    
                    rs.getString("descripcion"),
                    rs.getFloat("nota_maxima")
                );
                
                // 2. 💡 CLAVE: Sincronizar el ID de la BDD
                evaluacion.setIdEval(rs.getInt("idEvaluacion")); 
                
                return evaluacion;
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al buscar evaluación por ID: " + e.getMessage());
    }
    return null;
}

public List<Evaluacion> obtenerEvaluacionesPorModulo(int idModulo) {
    List<Evaluacion> evaluaciones = new ArrayList<>();
    String sql = "SELECT idEvaluacion, nombre, descripcion, nota_maxima FROM evaluacion WHERE idModulo = ?";

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idModulo);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Evaluacion eval = new Evaluacion(
                    rs.getString("nombre"),
                    
                    rs.getString("descripcion"),
                    rs.getFloat("nota_maxima")
                );
                eval.setIdEval(rs.getInt("idEvaluacion"));
                evaluaciones.add(eval);
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al obtener evaluaciones por módulo: " + e.getMessage());
    }
    return evaluaciones;
}
}