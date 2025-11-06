package data.implementaciones;


import data.interfaces.ContratoDeUsuario;
import user.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Implementa el contrato sencillo
public class GestorDeUsuarioSQL implements ContratoDeUsuario {

    // ... Código de conexión (ConexionMySQL.getConnection()) sigue igual ...

    @Override
    public void guardar(Usuario usuario) {
        // En MySQL, usamos la sentencia INSERT.
        String sql = "INSERT INTO Usuarios (nombre, email, clave) VALUES (?, ?, ?)";

        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getContrasenia());

            stmt.executeUpdate();
            System.out.println("DB: Usuario " + usuario.getNombre() + " guardado.");

        } catch (SQLException e) {
            System.err.println("Error al guardar usuario en DB: " + e.getMessage());
        }
    }

    @Override
    public Usuario buscarPorId(int id) {
        String sql = "SELECT id, nombre, email, clave FROM Usuarios WHERE id = ?";

        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID en DB: " + e.getMessage());
        }
        return null;
    }

    private void mapearUsuario(ResultSet rs) {
        // todo
    }

    @Override
    public List<Usuario> buscarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, nombre, email, clave FROM Usuarios";

        try (Connection conn = ConexionMySQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Hay que manejar la herencia (Alumno/Docente) aquí,
                // pero por ahora solo mapearemos la base.
                Usuario usuario = mapearUsuario(rs);
                if (usuario != null) {
                    usuarios.add(usuario);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar todos los usuarios en DB: " + e.getMessage());
        }
        return usuarios;
    }


    public void actualizar(Usuario usuario) {
        String sql = "UPDATE Usuarios SET nombre = ?, email = ?, clave = ? WHERE id = ?";

        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getContrasenia());
            stmt.setInt(4, usuario.getId());

            stmt.executeUpdate();
            System.out.println("DB: Usuario ID " + usuario.getId() + " actualizado.");

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario en DB: " + e.getMessage());
        }
    }


    public void eliminar(int id) {
        String sql = "DELETE FROM Usuarios WHERE id = ?";

        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("DB: Usuario ID " + id + " eliminado.");

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario en DB: " + e.getMessage());
        }
    }

    // Método específico para el login (Requerido por el contrato)
    @Override
    public boolean buscarPorNombreYClave(String nombre, String clave) {
        String query = "SELECT COUNT(*) FROM Usuarios WHERE nombre = ? AND clave = ?";

        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nombre);
            stmt.setString(2, clave);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por credenciales en DB: " + e.getMessage());
        }
        return false;
    }
    // ... Implementación de guardar, buscarPorId, etc. usando JDBC ...
}
