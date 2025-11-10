package data;

import modelos.usuario.Docente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelos.cursos.*;

public class GestorBDDCurso {
    private Connection conexion;
    private static final String URL = "jdbc:mysql://localhost:3306/plataforma_cursos";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql";

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
public Curso guardar(Curso nuevoCurso, Docente docente) {
    
    String sql = """
    INSERT INTO curso 
    (nombre, descripcion, cupo, precio, estado, fecha_inicio, fecha_fin, modalidad, 
     link_plataforma, plataforma, aula, direccion, idDocente) 
    VALUES (?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?)
    """; 

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); 
        
         PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { 

        ps.setString(1, nuevoCurso.getNombre());        // Param 1 (nombre)
        ps.setString(2, nuevoCurso.getDescripcion());   // Param 2 (descripcion)
        ps.setInt(3, nuevoCurso.getCupo());     
        ps.setFloat(4, nuevoCurso.getPrecio());        // Param 3 (precio)

        ps.setString(5, "inactivo");
       
        ps.setDate(6, nuevoCurso.getFechaInicio() != null ? new java.sql.Date(nuevoCurso.getFechaInicio().getTime()) : null);
        ps.setDate(7, nuevoCurso.getFechaFin() != null ? new java.sql.Date(nuevoCurso.getFechaFin().getTime()) : null);

        if (nuevoCurso instanceof CursoOnline online) {
            ps.setString(8, "Online");
            ps.setString(9, online.getLinkPlataforma());
            ps.setString(10, online.getPlataforma()); 
            ps.setNull(11, Types.VARCHAR); // aula
            ps.setNull(12, Types.VARCHAR); // direccion
        } else if (nuevoCurso instanceof CursoPresencial presencial) {
            ps.setString(8, "Presencial");
            ps.setNull(9, Types.VARCHAR); // link_plataforma
            ps.setNull(10, Types.VARCHAR); // plataforma
            ps.setString(11, presencial.getAula());
            ps.setString(12, presencial.getDireccion());
        } else {
            // Caso por defecto
            ps.setNull(6, Types.VARCHAR); 
            ps.setNull(7, Types.VARCHAR);
            ps.setNull(8, Types.VARCHAR);
            ps.setNull(9, Types.VARCHAR);
            ps.setNull(10, Types.VARCHAR);
            ps.setNull(11, Types.VARCHAR);
            ps.setNull(12, Types.VARCHAR);
        }
        
        ps.setInt(13, docente.getId()); 

        ps.executeUpdate();

        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                int idGenerado = rs.getInt(1);
                nuevoCurso.setIdCurso(idGenerado); 
            }
        }
        
        System.out.println("🟢 Curso agregado correctamente a la base de datos (ID BDD: " + nuevoCurso.getIdCurso() + ").");

    } catch (SQLException e) {
        System.out.println("⚠️ Error al agregar curso: " + e.getMessage());
        return null;
    }
    return nuevoCurso;
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
                float precio = rs.getFloat("precio");

                if ("Online".equalsIgnoreCase(modalidad)) {
                    String link = rs.getString("link_plataforma");
                    String plataforma = rs.getString("plataforma");
                    CursoOnline c = new CursoOnline(nombre, descripcion, cupo, precio, link, plataforma);
                    lista.add(c);
                } else if ("Presencial".equalsIgnoreCase(modalidad)) {
                    String aula = rs.getString("aula");
                    String direccion = rs.getString("direccion");
                    CursoPresencial c = new CursoPresencial(nombre, descripcion, cupo, precio, aula, direccion);
                    lista.add(c);
                }
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error al listar cursos: " + e.getMessage());
        }

        return lista;
    }

    public ArrayList<Curso> buscarTodos() {
    ArrayList<Curso> lista = new ArrayList<>();
    String sql = "SELECT * FROM curso";

    try (Statement st = conexion.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        while (rs.next()) {
            
            // 1. Capturar todos los campos necesarios
            String modalidad = rs.getString("modalidad");
            String nombre = rs.getString("nombre");
            String descripcion = rs.getString("descripcion");
            int cupo = rs.getInt("cupo");
            float precio = rs.getFloat("precio");
            int idCursoBdd = rs.getInt("idCurso"); 

            if ("ONLINE".equalsIgnoreCase(modalidad)) {
                String link = rs.getString("link_plataforma");
                String plataforma = rs.getString("plataforma"); 

                CursoOnline c = new CursoOnline(nombre, descripcion, cupo, precio, link, plataforma);
                
                c.setIdCurso(idCursoBdd); // 💡 SINCRONIZACIÓN
                lista.add(c);
                
            } else if ("PRESENCIAL".equalsIgnoreCase(modalidad)) {
                String aula = rs.getString("aula");
                String direccion = rs.getString("direccion");
                
                CursoPresencial c = new CursoPresencial(nombre, descripcion, cupo, precio,  aula, direccion);
                
                c.setIdCurso(idCursoBdd); // 💡 SINCRONIZACIÓN
                lista.add(c);
            }
        }

    } catch (SQLException e) {
        System.out.println("⚠️ Error al buscar todos los cursos: " + e.getMessage());
    }

    return lista;
}


    
public Curso buscarPorNombre(String nombreCurso) {
    String sql = "SELECT * FROM curso WHERE nombre = ?";
    
    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setString(1, nombreCurso);
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                // Si encontramos el curso, lo reconstruimos y lo devolvemos
                String modalidad = rs.getString("modalidad");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                int cupo = rs.getInt("cupo");
                    float precio = rs.getFloat("precio");
                int idCurso = rs.getInt("idCurso");
                
                Curso cursoEncontrado = null;
                
                if ("ONLINE".equalsIgnoreCase(modalidad)) {
                    String link = rs.getString("link_plataforma");
                    // Aquí se asume que sabes el nombre de la plataforma (ej: ZOOM)
                    cursoEncontrado = new CursoOnline(nombre, descripcion, cupo, precio, link, "ZOOM");
                } else if ("PRESENCIAL".equalsIgnoreCase(modalidad)) {
                    String aula = rs.getString("aula");
                    String direccion = rs.getString("direccion");
                    cursoEncontrado = new CursoPresencial(nombre, descripcion, cupo, precio, aula, direccion);
                }
                
                if (cursoEncontrado != null) {
                    cursoEncontrado.setIdCurso(idCurso); // Sincronizamos el ID de la BDD
                    return cursoEncontrado;
                }
            }
        }
    } catch (SQLException e) {
        System.out.println("⚠️ Error al buscar curso por nombre: " + e.getMessage());
    }
    return null;
}

public Curso buscarCursoPorId(int idCurso) {
    String sql = "SELECT * FROM curso WHERE idCurso = ?";
    // Usar tus credenciales y URLs
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); 
         PreparedStatement ps = conn.prepareStatement(sql)) { 

        ps.setInt(1, idCurso);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            // Mapeo de campos básicos
            String modalidad = rs.getString("modalidad");
            String nombre = rs.getString("nombre");
            String descripcion = rs.getString("descripcion");
            int cupo = rs.getInt("cupo");
            float precio = rs.getFloat("precio"); 
            if ("Online".equalsIgnoreCase(modalidad)) {
                String link = rs.getString("link_plataforma");
                String plataforma = rs.getString("plataforma");
                CursoOnline c = new CursoOnline(nombre, descripcion, cupo, precio, link, plataforma);
                c.setIdCurso(rs.getInt("idCurso"));
                return c;
            } else if ("Presencial".equalsIgnoreCase(modalidad)) {
                String aula = rs.getString("aula");
                String direccion = rs.getString("direccion");
                CursoPresencial c = new CursoPresencial(nombre, descripcion, cupo, precio, aula, direccion);
                c.setIdCurso(rs.getInt("idCurso"));
                return c;
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al buscar curso por ID: " + e.getMessage());
    }
    return null;
}
}
