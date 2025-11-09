package data;

import modelos.inscripcion.Inscripcion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorBDDInscripcion {
    private Connection conn;

    public GestorBDDInscripcion() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/plataforma_cursos", // nombre de tu base
                    "root", // usuario
                    "mysql" // contraseña
            );
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con la BDD: " + e.getMessage());
        }
    }

    // Archivo: GestorBDDInscripcion.java

public Inscripcion guardar(Inscripcion inscripcion) {
    String sql = "INSERT INTO inscripcion (idUsuario, idCurso, fecha, estado) VALUES (?, ?, ?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // 💡 CLAVE
        ps.setInt(1, inscripcion.getAlumno().getId());
        ps.setInt(2, inscripcion.getCurso().getIdCurso());
        ps.setDate(3, new java.sql.Date(inscripcion.getFecha().getTime()));
        ps.setString(4, inscripcion.getEstado());
        ps.executeUpdate();

        // 2. Recuperar el ID generado por la BDD (AUTO_INCREMENT)
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                int idGenerado = rs.getInt(1);
                inscripcion.setIdInscripcion(idGenerado); // 🌟 SINCRONIZAR
            }
        }
        
        System.out.println("📝 Alumno " + inscripcion.getAlumno().getNombre() + " preinscripto en " + inscripcion.getCurso().getNombre() + " (ID Inscripción BDD: " + inscripcion.getIdInscripcion() + ")");
    } catch (SQLException e) {
        System.err.println("❌ Error al guardar inscripción: " + e.getMessage());
    }
    return inscripcion;
}
// Archivo: GestorBDDInscripcion.java

public boolean existeInscripcion(int idAlumno, int idCurso) {
    String sql = "SELECT COUNT(*) FROM inscripcion WHERE idUsuario = ? AND idCurso = ?";
    
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idAlumno);
        ps.setInt(2, idCurso);
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Existe al menos una inscripción
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al verificar la existencia de la inscripción: " + e.getMessage());
    }
    return false;
}

    public void actualizarEstado(int idAlumno, int idCurso, String nuevoEstado) {
        String sql = "UPDATE inscripcion SET estado = ? WHERE idUsuario = ? AND idCurso = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idAlumno);
            ps.setInt(3, idCurso);
            ps.executeUpdate();
            System.out.println("💳 Inscripción actualizada correctamente en la base de datos.");
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar inscripción: " + e.getMessage());
        }
    }
    public List<Integer> obtenerCursosInscritosIds(int idAlumno) {
    List<Integer> idsCursos = new ArrayList<>();
    // Asumo que conn, URL, USER, PASSWORD están disponibles o se usan try-with-resources
    String sql = "SELECT idCurso FROM inscripcion WHERE idUsuario = ? AND estado = 'aceptada'"; 

    try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // Usar tus credenciales
        ps.setInt(1, idAlumno);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                idsCursos.add(rs.getInt("idCurso"));
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al obtener IDs de cursos inscritos: " + e.getMessage());
    }
    return idsCursos;
}

public List<Integer> obtenerAlumnosInscritosIds(int idCurso) {
    List<Integer> idsAlumnos = new ArrayList<>();
    String sql = "SELECT idUsuario FROM inscripcion WHERE idCurso = ? AND estado = 'aceptada'";
    
    try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        
        ps.setInt(1, idCurso);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                idsAlumnos.add(rs.getInt("idUsuario"));
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al obtener IDs de alumnos inscritos: " + e.getMessage());
    }
    return idsAlumnos;
}
}
