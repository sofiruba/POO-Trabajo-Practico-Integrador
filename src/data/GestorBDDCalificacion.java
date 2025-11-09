
package data;

import modelos.cursos.*;
import java.sql.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GestorBDDCalificacion {
    
    private static final String URL = "jdbc:mysql://localhost:3306/plataforma_cursos";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql"; 
    
    public Calificacion guardarCalificacion(Calificacion calificacion) {
        String sql = "INSERT INTO calificacion (idUsuario, idCurso, idEvaluacion, nota, comentario, fecha_calificacion) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             // Pedimos las claves generadas
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Seteamos los parámetros usando los IDs sincronizados de los objetos
            ps.setInt(1, calificacion.getAlumno().getId());
            ps.setInt(2, calificacion.getCurso().getIdCurso());
            ps.setInt(3, calificacion.getEvaluacion().getIdEval());
            ps.setFloat(4, calificacion.getNota());
            ps.setString(5, calificacion.getComentario());
            ps.setDate(6, new java.sql.Date(calificacion.getFechaRegistro().getTime()));
            
            ps.executeUpdate();
            
            // Sincronizar el ID de la BDD
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    calificacion.setIdCalificacion(rs.getInt(1));
                }
            }
            
            System.out.println("💾 Calificación guardada en la base de datos.");
            return calificacion;

        } catch (SQLException e) {
            System.err.println("❌ Error al guardar calificación: " + e.getMessage());
            return null;
        }
    }

public boolean existeCalificacion(int idAlumno, int idEvaluacion) {
    // La consulta verifica si ya hay una calificación registrada para este par
    String sql = "SELECT COUNT(*) FROM calificacion WHERE idUsuario = ? AND idEvaluacion = ?";
    
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); 
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, idAlumno);
        ps.setInt(2, idEvaluacion);
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Existe una calificación
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al verificar la existencia de calificación: " + e.getMessage());
    }
    return false;
}

public List<Map.Entry<Integer, Float>> obtenerCalificacionesBase(int idAlumno) {
    // Usamos Map.Entry para devolver el idEvaluacion y la nota
    List<Map.Entry<Integer, Float>> calificacionesBase = new ArrayList<>();
    
    String sql = "SELECT idEvaluacion, idCurso, nota, comentario FROM calificacion WHERE idUsuario = ?"; 
    
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); 
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, idAlumno);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int idEvaluacion = rs.getInt("idEvaluacion");
                float nota = rs.getFloat("nota");
                calificacionesBase.add(new AbstractMap.SimpleEntry<>(idEvaluacion, nota));
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al obtener calificaciones base: " + e.getMessage());
    }
    return calificacionesBase;
}
}