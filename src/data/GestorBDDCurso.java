package data;

import modelos.usuario.Docente;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import modelos.cursos.*;

public class GestorBDDCurso {
    private Connection conexion;

    public GestorBDDCurso() {
        try {
            String url = "jdbc:mysql://localhost:3306/plataforma_cursos";
            String user = "root";
            String password = "mysql"; // AMPPS default
            conexion = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Conectado a la base de datos (cursos).");
        } catch (SQLException e) {
            System.out.println("❌ Error de conexión: " + e.getMessage());
        }
    }

    public void agregarCurso(Curso curso, Docente docente) {
        String sql = """
            INSERT INTO cursos 
            (nombre, descripcion, cupo, estado, fecha_inicio, fecha_fin, modalidad, 
             link_plataforma, plataforma, aula, direccion, id_docente)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, curso.getNombre());
            ps.setString(2, curso.getDescripcion());
            ps.setInt(3, curso.getCupo());
            ps.setString(4, curso.getEstado());
            ps.setDate(5, curso.getFechaInicio() != null ? new java.sql.Date(curso.getFechaInicio().getTime()) : null);
            ps.setDate(6, curso.getFechaFin() != null ? new java.sql.Date(curso.getFechaFin().getTime()) : null);

            if (curso instanceof CursoOnline online) {
                ps.setString(7, "Online");
                ps.setString(8, online.getLinkPlataforma());
                ps.setString(9, online.getPlataforma());
                ps.setNull(10, Types.VARCHAR);
                ps.setNull(11, Types.VARCHAR);
            } else if (curso instanceof CursoPresencial presencial) {
                ps.setString(7, "Presencial");
                ps.setNull(8, Types.VARCHAR);
                ps.setNull(9, Types.VARCHAR);
                ps.setString(10, presencial.getAula());
                ps.setString(11, presencial.getDireccion());
            } else {
                ps.setNull(7, Types.VARCHAR);
                ps.setNull(8, Types.VARCHAR);
                ps.setNull(9, Types.VARCHAR);
                ps.setNull(10, Types.VARCHAR);
                ps.setNull(11, Types.VARCHAR);
            }

            ps.setInt(12, docente.getId());
            ps.executeUpdate();
            System.out.println("🟢 Curso agregado correctamente a la base de datos.");

        } catch (SQLException e) {
            System.out.println("⚠️ Error al agregar curso: " + e.getMessage());
        }
    }

    public List<Curso> listarCursos() {
        List<Curso> lista = new ArrayList<>();
        String sql = "SELECT * FROM cursos";

        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String modalidad = rs.getString("modalidad");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                int cupo = rs.getInt("cupo");

                if ("Online".equalsIgnoreCase(modalidad)) {
                    String link = rs.getString("link_plataforma");
                    String plataforma = rs.getString("plataforma");
                    CursoOnline c = new CursoOnline(nombre, descripcion, cupo, link, plataforma);
                    lista.add(c);
                } else if ("Presencial".equalsIgnoreCase(modalidad)) {
                    String aula = rs.getString("aula");
                    String direccion = rs.getString("direccion");
                    CursoPresencial c = new CursoPresencial(nombre, descripcion, cupo, aula, direccion);
                    lista.add(c);
                }
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error al listar cursos: " + e.getMessage());
        }

        return lista;
    }

    public ArrayList<Curso> buscarTodos() {
        // todo
        System.out.println("⚠ Simulación: no se cargaron cursos desde la BDD.");
        return new ArrayList<>();
    }

    public void guardar(Curso nuevoCurso) {
        // todo
    }
}
