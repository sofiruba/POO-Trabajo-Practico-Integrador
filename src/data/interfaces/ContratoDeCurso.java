package data.interfaces;

import modelos.Curso;
import java.util.List;


public interface ContratoDeCurso {

    // Métodos CRUD básicos
    void guardar(Curso curso);
    Curso buscarPorId(int id);
    List<Curso> buscarTodos();
    void actualizar(Curso curso);
    void eliminar(int id);

    List<Curso> buscarPorModalidad(String modalidad);
}