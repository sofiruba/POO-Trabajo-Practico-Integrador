package data;

import modelos.usuario.Alumno;
import modelos.usuario.Docente;
import modelos.usuario.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorBDDUsuario {

    private static final String URL = "jdbc:mysql://localhost:3306/plataforma_cursos";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql"; 

    
public Alumno guardarAlumno(Alumno alumno) {

    String sql = "INSERT INTO usuario (nombre, email, contrasenia, fecha_registro, especialidad, tipo) VALUES (?, ?, ?, ?, NULL,'ALUMNO')";
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, alumno.getNombre());
        ps.setString(2, alumno.getEmail());
        ps.setString(3, alumno.getContrasenia());
        ps.setDate(4, new java.sql.Date(alumno.getFechaInscripcion().getTime()));
        ps.executeUpdate();

        
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                int idGenerado = rs.getInt(1);
                alumno.setId(idGenerado); 
                System.out.println("Alumno guardado (ID: " + idGenerado + ") correctamente en la base de datos.");
            }
        }

    } catch (SQLException e) {
        System.err.println("Error al guardar alumno: " + e.getMessage());
    }
    return alumno; 
}

public Docente guardarDocente(Docente docente) {
    String sql = "INSERT INTO usuario (nombre, email, contrasenia, especialidad, tipo) VALUES (?, ?, ?, ?, 'DOCENTE')";
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

         PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { 

        ps.setString(1, docente.getNombre());
        ps.setString(2, docente.getEmail());
        ps.setString(3, docente.getContrasenia());
        ps.setString(4, docente.getEspecialidad());
        ps.executeUpdate();

        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                int idGenerado = rs.getInt(1);
                docente.setId(idGenerado); 
                System.out.println("Docente guardado (ID: " + idGenerado + ") correctamente en la base de datos.");
            }
        }

    } catch (SQLException e) {
        System.err.println("Error al guardar docente: " + e.getMessage()); 
    }
    return docente; 
}


    public Usuario buscarPorEmail(String email) {
    Usuario usuario = null;
    String sql = "SELECT * FROM usuario WHERE email = ?";

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String tipo = rs.getString("tipo"); 
            int idUsuario = rs.getInt("idUsuario"); 

            if ("ALUMNO".equalsIgnoreCase(tipo)) {
                Alumno alumno = new Alumno(
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("contrasenia"),
                        rs.getDate("fecha_registro")
                );
                alumno.setId(idUsuario); 
                usuario = alumno;
                
            } else if ("DOCENTE".equalsIgnoreCase(tipo)) {
                Docente docente = new Docente(
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("contrasenia"),
                        rs.getString("especialidad")
                );
                docente.setId(idUsuario); 
                usuario = docente;
            }
        }

    } catch (SQLException e) {
        System.err.println("Error al buscar usuario: " + e.getMessage());
    }

    return usuario;
}
    public Alumno buscarAlumnoPorEmail(String email) {
    String sql = "SELECT * FROM usuario WHERE email = ? AND tipo = 'ALUMNO'";
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            
            Alumno alumno = new Alumno(

                rs.getString("nombre"),
                rs.getString("email"),
                rs.getString("contrasenia"),
                rs.getDate("fecha_registro")
            );
            alumno.setId(rs.getInt("idUsuario"));
            return alumno;
        }

    } catch (SQLException e) {
        System.out.println("⚠️ Error al buscar alumno por email: " + e.getMessage());
    }
    return null;
}

    public List<Alumno> buscarTodosAlumnos() {
        List<Alumno> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE tipo = 'ALUMNO'";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Alumno(
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("contrasenia"),
                        rs.getDate("fecha_registro")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener alumnos: " + e.getMessage());
        }

        return lista;
    }

    public List<Docente> buscarTodosDocentes() {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE tipo = 'DOCENTE'";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Docente(
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("contrasenia"),
                        rs.getString("especialidad")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener docentes: " + e.getMessage());
        }

        return lista;
    }
public Docente buscarDocentePorEmail(String email) {
    String sql = "SELECT * FROM usuario WHERE email = ? AND tipo = 'DOCENTE'";

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Docente docente = new Docente(
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("contrasenia"),
                    rs.getString("especialidad")
            );
          
            docente.setId(rs.getInt("idUsuario")); 
            return docente;
        }

    } catch (SQLException e) {
        System.err.println("Error al buscar docente: " + e.getMessage());
    }
    return null;
}
public Alumno buscarAlumnoPorId(int id) {
    String sql = "SELECT idUsuario, nombre, email, contrasenia, fecha_registro FROM usuario WHERE idUsuario = ? AND tipo = 'ALUMNO'";

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); 
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, id);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                
                Alumno alumno = new Alumno(
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("contrasenia"),
                        rs.getDate("fecha_registro") 
                );
                
                
                alumno.setId(rs.getInt("idUsuario")); 
                return alumno;
            }
        }

    } catch (SQLException e) {
        System.err.println("❌ Error al buscar alumno por ID: " + e.getMessage());
    }
    return null;
}
}
