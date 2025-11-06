package vista;

import controlador.ControladorUsuario;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class formRegistrar extends JFrame {
    private JPanel pnlRegistrar;
    private JPanel pnlTitulo;
    private JTextField txtMail;
    private JPasswordField pwdPassword;
    private JButton btnRegistrar;
    private JLabel lblMensaje;
    private ControladorUsuario controlador;

    public void iniciarFormulario() {
        setContentPane(pnlRegistrar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
    }

    public formRegistrar() {
        iniciarFormulario();
        btnRegistrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                registrar();
            }
        });
    }
    public void registrar() {
        String elUsuario = txtMail.getText();
        String laClave = pwdPassword.getText();
        String respuesta = controlador.registro(elUsuario, laClave);
        if (respuesta.equals("REGISTRO_EXITOSO")) {
            lblMensaje.setText(respuesta);
        } else
            lblMensaje.setText("Error! Vuelva a ingresar");

    }

    public void setControlador(ControladorUsuario controlador) {
        this.controlador = controlador;
    }

}
