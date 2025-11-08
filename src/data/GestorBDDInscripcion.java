package data;

import modelos.inscripcion.Inscripcion;
import java.sql.*;

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

    public void guardar(Inscripcion inscripcion) {
        String sql = "INSERT INTO inscripcion (idAlumno, idCurso, fecha, estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, inscripcion.getAlumno().getId());
            ps.setInt(2, inscripcion.getCurso().getIdCurso());
            ps.setDate(3, new java.sql.Date(inscripcion.getFecha().getTime()));
            ps.setString(4, inscripcion.getEstado());
            ps.executeUpdate();
            System.out.println("📝 Alumno " + inscripcion.getAlumno().getNombre() + " preinscripto en " + inscripcion.getCurso().getNombre());
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar inscripción: " + e.getMessage());
        }
    }

    public void actualizarEstado(int idAlumno, int idCurso, String nuevoEstado) {
        String sql = "UPDATE inscripcion SET estado = ? WHERE idAlumno = ? AND idCurso = ?";
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
}
