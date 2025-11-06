package vista;

import controlador.ControladorUsuario;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class formAcceso extends JFrame {
    private JPanel pnlPrincipal;
    private JPanel pnlTitulo;
    private JPanel pnlDatos;
    private JPanel pnlMensaje;
    private JTextField txtUsuario;
    private JTextField txtClave;
    private JButton btnIngresar;
    private JLabel lblMensaje;
    private JButton btnRegistrar;
    //
    ControladorUsuario controlador = new ControladorUsuario();

    public formAcceso() {
        iniciarFormulario();
        btnIngresar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                validarCredenciales();
            }
        });
        btnRegistrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                formRegistrar ventana = new formRegistrar();
                ventana.setVisible(true);
                ventana.iniciarFormulario();
                setVisible(false);
                ventana.setControlador(controlador);
            }
        });
    }

    private void iniciarFormulario() {
        setContentPane(pnlPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
    }

    public void validarCredenciales() {
        String elUsuario = txtUsuario.getText();
        String laClave = txtClave.getText();
        String respuesta = controlador.autenticar(elUsuario, laClave);
        if (respuesta.equals("ACCESO_CONCEDIDO")) {
            lblMensaje.setText(respuesta);
        } else
            lblMensaje.setText("Error! Vuelva a ingresar");

    }
}
