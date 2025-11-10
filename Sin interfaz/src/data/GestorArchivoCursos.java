package data;

import modelos.cursos.Curso;
import modelos.cursos.CursoOnline;
import modelos.cursos.CursoPresencial;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivoCursos {

    private static final String ARCHIVO_CURSOS = "cursos.csv";
    private static final String SEPARADOR = ",";

    public void guardarCursos(List<Curso> cursos) {
       
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_CURSOS))) {

            for (Curso curso : cursos) {
                
                StringBuilder linea = new StringBuilder();
                linea.append(curso.getIdCurso()).append(SEPARADOR);
                linea.append(curso.getNombre()).append(SEPARADOR);
                linea.append(curso.getDescripcion()).append(SEPARADOR);
                linea.append(curso.getPrecio()).append(SEPARADOR);

                if (curso instanceof CursoOnline online) {
                    linea.append("Online").append(SEPARADOR);
                    linea.append(online.getLinkPlataforma()).append(SEPARADOR);
                    linea.append(online.getPlataforma());
                } else if (curso instanceof CursoPresencial presencial) {
                    linea.append("Presencial").append(SEPARADOR);
                    linea.append(presencial.getAula()).append(SEPARADOR);
                    linea.append(presencial.getDireccion());
                }

                bw.write(linea.toString());
                bw.newLine();
            }
            System.out.println("💾 Persistencia en archivo: " + cursos.size() + " cursos guardados en " + ARCHIVO_CURSOS);

        } catch (IOException e) {
            System.err.println("❌ Error al escribir en el archivo de cursos: " + e.getMessage());
        }
    }

    
    public List<Curso> cargarCursos() {
        List<Curso> cursos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_CURSOS))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(SEPARADOR);
                if (partes.length < 5) continue; // Mínimo de campos

                String nombre = partes[1];
                String descripcion = partes[2];
                float precio = Float.parseFloat(partes[3]);
                String modalidad = partes[4];

                Curso curso = null;
                if (modalidad.equalsIgnoreCase("Online") && partes.length >= 7) {
                   
                    curso = new CursoOnline(nombre, descripcion, 0, precio, partes[5], partes[6]);
                } else if (modalidad.equalsIgnoreCase("Presencial") && partes.length >= 7) {
                    
                    curso = new CursoPresencial(nombre, descripcion, 0, precio, partes[5], partes[6]);
                }

                if (curso != null) {
                    cursos.add(curso);
                }
            }
            System.out.println("✅ Persistencia en archivo: " + cursos.size() + " cursos cargados desde " + ARCHIVO_CURSOS);

        } catch (FileNotFoundException e) {
            System.out.println("⚠️ Archivo de cursos no encontrado. Iniciando sin datos de archivo.");
        } catch (IOException | NumberFormatException e) {
            System.err.println("❌ Error de lectura/formato al cargar cursos: " + e.getMessage());
        }
        return cursos;
    }
}