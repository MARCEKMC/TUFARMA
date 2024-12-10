package vistas;

import controladores.ControladorPrincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaRegistro extends JFrame {
    private ControladorPrincipal controlador;

    public VistaRegistro(ControladorPrincipal controlador) {
        this.controlador = controlador;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Registro - TUFARMA");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel central
        JPanel panelCentral = new JPanel(new GridLayout(9, 2, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Campos de registro
        panelCentral.add(new JLabel("Nombre:"));
        JTextField campoNombre = new JTextField();
        panelCentral.add(campoNombre);

        panelCentral.add(new JLabel("Apellido:"));
        JTextField campoApellido = new JTextField();
        panelCentral.add(campoApellido);

        panelCentral.add(new JLabel("Correo:"));
        JTextField campoCorreo = new JTextField();
        panelCentral.add(campoCorreo);

        panelCentral.add(new JLabel("Dirección:"));
        JTextField campoDireccion = new JTextField();
        panelCentral.add(campoDireccion);

        panelCentral.add(new JLabel("Teléfono:"));
        JTextField campoTelefono = new JTextField();
        panelCentral.add(campoTelefono);

        panelCentral.add(new JLabel("DNI:"));
        JTextField campoDni = new JTextField();
        panelCentral.add(campoDni);

        panelCentral.add(new JLabel("Usuario:"));
        JTextField campoUsuario = new JTextField();
        panelCentral.add(campoUsuario);

        panelCentral.add(new JLabel("Contraseña:"));
        JPasswordField campoContrasena = new JPasswordField();
        panelCentral.add(campoContrasena);

        // Botones
        JButton botonRegistrar = new JButton("Registrar");
        JButton botonCancelar = new JButton("Cancelar");
        panelCentral.add(botonRegistrar);
        panelCentral.add(botonCancelar);

        add(panelCentral, BorderLayout.CENTER);

        // Acción del botón Registrar
        botonRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = campoNombre.getText();
                String apellido = campoApellido.getText();
                String correo = campoCorreo.getText();
                String direccion = campoDireccion.getText();
                String telefono = campoTelefono.getText();
                String dni = campoDni.getText();
                String usuario = campoUsuario.getText();
                String contrasena = new String(campoContrasena.getPassword());

                if (nombre.trim().isEmpty() || apellido.trim().isEmpty() || correo.trim().isEmpty() ||
                        direccion.trim().isEmpty() || telefono.trim().isEmpty() || dni.trim().isEmpty() ||
                        usuario.trim().isEmpty() || contrasena.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
                    return;
                }

                controlador.registrarUsuario(nombre, apellido, correo, direccion, telefono, dni, usuario, contrasena);
                dispose();
            }
        });

        // Acción del botón Cancelar
        botonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }
}