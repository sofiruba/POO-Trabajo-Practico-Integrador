package data.interfaces;

import user.Usuario;
import java.util.List;

/**
 * Define el CONTRATO de métodos para guardar y leer Usuarios.
 */
public interface ContratoDeUsuario {

    // Métodos CRUD básicos
    void guardar(Usuario usuario);
    Usuario buscarPorId(int id);
    List<Usuario> buscarTodos();
    // ... otros métodos CRUD

    // Método específico de negocio (Ej: para el Login)
    boolean buscarPorNombreYClave(String nombre, String clave);
}