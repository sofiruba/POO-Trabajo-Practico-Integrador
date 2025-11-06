package controller;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class gestionUsuario {
    private final Path dir;
    private final Path file;

    public gestionUsuario() {
        // Mantén la misma ruta que usabas
        this.dir = Paths.get("src", "datos");
        this.file = dir.resolve("usuario.txt");

        // Crea carpeta y archivo si no existen
        try {
            Files.createDirectories(dir);
            if (!Files.exists(file)) {
                Files.createFile(file);
            }
        } catch (IOException e) {
            // Log opcional
        }
    }

    // Conveniencia: buscar por Strings
    public boolean buscarUsuario(String nombre, String clave) {
        if (nombre == null || clave == null) return false;
        String target = nombre + "," + clave;

        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (target.equals(linea)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // Log opcional
        }
        return false;
    }

    // Mantén tu firma original
    public boolean buscarUsuario(Usuario usuario) {
        if (usuario == null) return false;
        return buscarUsuario(usuario.getNombre(), usuario.getClave());
    }

    public boolean registrarUsuario(Usuario usuario) {
        if (usuario == null ||
                usuario.getNombre() == null ||
                usuario.getClave() == null) {
            return false;
        }

        if (buscarUsuario(usuario)) {
            // Ya existe
            return false;
        }

        String registro = usuario.getNombre() + "," + usuario.getClave();

        try (BufferedWriter bw = Files.newBufferedWriter(
                file, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {

            // Si el archivo ya tiene contenido, agrega salto de línea antes
            if (Files.size(file) > 0) {
                bw.newLine();
            }
            bw.write(registro);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
