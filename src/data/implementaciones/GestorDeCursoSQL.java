package data.implementaciones;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelos.*;
import data.interfaces.*;

public class GestorDeCursoSQL implements ContratoDeCurso {

    private Curso mapearCurso(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        String descripcion = rs.getString("descripcion");
        int cupo = rs.getInt("cupo");
        String estado = rs.getString("estado");
        String modalidad = rs.getString("modalidad");

        if ("ONLINE".equalsIgnoreCase(modalidad)) {
            String link = rs.getString("linkPlataforma");
            String plataforma = rs.getString("plataforma");
            return new CursoOnline( nombre, descripcion, cupo, link, plataforma);
        } else if ("PRESENCIAL".equalsIgnoreCase(modalidad)) {
            String aula = rs.getString("aula");
            String direccion = rs.getString("direccion");
            return new CursoPresencial( nombre, descripcion, cupo, aula, direccion);
        }
        // Si no se reconoce la modalidad, devuelve nulo
        return null;
    }

    // =========================================================
    // Implementación del ContratoDeCurso
    // =========================================================

    @Override
    public void guardar(Curso curso) {
        String sql = "INSERT INTO Cursos (nombre, descripcion, cupo, estado, modalidad, linkPlataforma, plataforma, aula, direccion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, curso.getNombre());
            stmt.setString(2, curso.getDescripcion());
            stmt.setInt(3, curso.getCupo());
            stmt.setString(4, curso.getEstado());

            // Lógica para guardar la información específica de la subclase
            if (curso instanceof CursoOnline) {
                CursoOnline co = (CursoOnline) curso;
                stmt.setString(5, "ONLINE");
                stmt.setString(6, co.getLinkPlataforma());
                stmt.setString(7, co.getPlataforma());
                stmt.setNull(8, Types.VARCHAR); // aula
                stmt.setNull(9, Types.VARCHAR); // direccion
            } else if (curso instanceof CursoPresencial) {
                CursoPresencial cp = (CursoPresencial) curso;
                stmt.setString(5, "PRESENCIAL");
                stmt.setNull(6, Types.VARCHAR); // linkPlataforma
                stmt.setNull(7, Types.VARCHAR); // plataforma
                stmt.setString(8, cp.getAula());
                stmt.setString(9, cp.getDireccion());
            }

            stmt.executeUpdate();
            // Opcional: obtener el ID autogenerado para actualizar el objeto en memoria
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    // Actualizar el ID en el objeto Curso si es necesario
                    // curso.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar curso en DB: " + e.getMessage());
        }
    }

    @Override
    public List<Curso> buscarTodos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT * FROM Cursos";

        try (Connection conn = ConexionMySQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Curso curso = mapearCurso(rs);
                if (curso != null) {
                    cursos.add(curso);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar todos los cursos en DB: " + e.getMessage());
        }
        return cursos;
    }

    @Override
    public Curso buscarPorId(int id) {
        String sql = "SELECT nombre, blabla FROM Cursos" +
                "WHERE id = @id";
        try (Connection conn = ConexionMySQL.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {


            Curso curso = mapearCurso(rs);


        } catch (SQLException e) {
            System.err.println("Error al buscar todos los cursos en DB: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void actualizar(Curso curso) {

        String sql = "UPDATE nombre, blabla FROM Cursos" +
                "WHERE id = @curso.id";
        try (Connection conn = ConexionMySQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // todo


        } catch (SQLException e) {
            System.err.println("Error al buscar todos los cursos en DB: " + e.getMessage());
        }

    }

    @Override
    public void eliminar(int id) {
        // Implementación de DELETE WHERE id = ?
    }

    @Override
    public List<Curso> buscarPorModalidad(String modalidad) {
        // Implementación de SELECT * FROM Cursos WHERE modalidad = ?
        return new ArrayList<>();
    }
}