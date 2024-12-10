package vistas;

import controladores.ControladorPrincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaLogin extends JFrame {
    private ControladorPrincipal controlador;

    public VistaLogin(ControladorPrincipal controlador) {
        this.controlador = controlador;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Login - TUFARMA");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel central
        JPanel panelCentral = new JPanel(new GridLayout(3, 2, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Usuario
        JLabel labelUsuario = new JLabel("Usuario:");
        JTextField campoUsuario = new JTextField();
        panelCentral.add(labelUsuario);
        panelCentral.add(campoUsuario);

        // Contraseña
        JLabel labelContrasena = new JLabel("Contraseña:");
        JPasswordField campoContrasena = new JPasswordField();
        panelCentral.add(labelContrasena);
        panelCentral.add(campoContrasena);

        // Botones
        JButton botonIngresar = new JButton("Ingresar");
        JButton botonCancelar = new JButton("Cancelar");
        panelCentral.add(botonIngresar);
        panelCentral.add(botonCancelar);

        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con opción de registro
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botonRegistrar = new JButton("Registrarse");
        panelInferior.add(botonRegistrar);
        add(panelInferior, BorderLayout.SOUTH);

        // Acción del botón Ingresar
        botonIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = campoUsuario.getText();
                String contrasena = new String(campoContrasena.getPassword());

                if (usuario.trim().isEmpty() || contrasena.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
                    return;
                }

                boolean valido = controlador.validarUsuario(usuario, contrasena);
                if (valido) {
                    JOptionPane.showMessageDialog(null, "Login exitoso.");
                    dispose();
                    controlador.abrirVistaPrincipal(); // Abre la vista correspondiente
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.");
                }
            }
        });

        // Acción del botón Cancelar
        botonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Acción del botón Registrarse
        botonRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana de login
                controlador.abrirRegistro(); // Abre la ventana de registro
            }
        });

        setVisible(true);
    }
}