package modelos.usuario;
import java.awt.image.BufferedImage;

public abstract class Usuario {
    protected int id;
    protected String nombre;
    protected String email;
    protected BufferedImage avatar;
    protected String contrasenia;


    public Usuario(String nombre, String email, String contrasenia) {

        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
    }

    public void login() {
        System.out.println(nombre + " inicio sesion.");
    }

    public void logout() {
        System.out.println(nombre + " cerro sesion.");
    }

    // Getters y setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public BufferedImage getAvatar() { return avatar; }
    public void setAvatar(BufferedImage avatar) { this.avatar = avatar; }
    public String getContrasenia() { return contrasenia; }
}

