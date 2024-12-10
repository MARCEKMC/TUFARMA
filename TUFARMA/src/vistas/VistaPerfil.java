package vistas;

import controladores.ControladorUsuario;
import modelos.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaPerfil extends JFrame {
    private ControladorUsuario controlador;
    private JTextField campoNombre;
    private JTextField campoApellido;
    private JTextField campoCorreo;
    private JTextField campoDireccion;
    private JTextField campoTelefono;
    private JTextField campoDni;
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;

    public VistaPerfil(ControladorUsuario controlador) {
        this.controlador = controlador;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Perfil de Usuario - TUFARMA");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel central
        JPanel panelCentral = new JPanel(new GridLayout(9, 2, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Campos de perfil
        panelCentral.add(new JLabel("Nombre:"));
        campoNombre = new JTextField();
        panelCentral.add(campoNombre);

        panelCentral.add(new JLabel("Apellido:"));
        campoApellido = new JTextField();
        panelCentral.add(campoApellido);

        panelCentral.add(new JLabel("Correo:"));
        campoCorreo = new JTextField();
        panelCentral.add(campoCorreo);

        panelCentral.add(new JLabel("Dirección:"));
        campoDireccion = new JTextField();
        panelCentral.add(campoDireccion);

        panelCentral.add(new JLabel("Teléfono:"));
        campoTelefono = new JTextField();
        panelCentral.add(campoTelefono);

        panelCentral.add(new JLabel("DNI:"));
        campoDni = new JTextField();
        panelCentral.add(campoDni);

        panelCentral.add(new JLabel("Usuario:"));
        campoUsuario = new JTextField();
        panelCentral.add(campoUsuario);

        panelCentral.add(new JLabel("Contraseña:"));
        campoContrasena = new JPasswordField();
        panelCentral.add(campoContrasena);

        // Botones
        JButton botonActualizar = new JButton("Actualizar");
        JButton botonCancelar = new JButton("Cancelar");
        panelCentral.add(botonActualizar);
        panelCentral.add(botonCancelar);

        add(panelCentral, BorderLayout.CENTER);

        // Acción del botón Actualizar
        botonActualizar.addActionListener(new ActionListener() {
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

                controlador.actualizarUsuario(nombre, apellido, correo, direccion, telefono, dni, usuario, contrasena);
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

    // Cargar datos del usuario actual
    public void setDatosUsuario(Usuario usuario) {
        campoNombre.setText(usuario.getNombre());
        campoApellido.setText(usuario.getApellido());
        campoCorreo.setText(usuario.getCorreo());
        campoDireccion.setText(usuario.getDireccion());
        campoTelefono.setText(usuario.getTelefono());
        campoDni.setText(usuario.getDni());
        campoUsuario.setText(usuario.getUsuario());
        campoContrasena.setText(usuario.getContrasena());
    }
}