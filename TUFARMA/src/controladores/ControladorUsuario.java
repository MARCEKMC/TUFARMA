package controladores;

import modelos.Usuario;
import vistas.VistaPerfil;

import javax.swing.*;

public class ControladorUsuario {
    private ControladorPrincipal controladorPrincipal;
    private VistaPerfil vistaPerfil;

    public ControladorUsuario(ControladorPrincipal controladorPrincipal) {
        this.controladorPrincipal = controladorPrincipal;
        this.vistaPerfil = new VistaPerfil(this);
        this.vistaPerfil.setVisible(true);
        cargarDatosUsuario();
    }

    // Actualizar información del usuario
    public void actualizarUsuario(String nombre, String apellido, String correo, String direccion,
                                  String telefono, String dni, String usuario, String contrasena) {
        Usuario usuarioLogueado = controladorPrincipal.getUsuarioLogueado();
        if (usuarioLogueado != null) {
            usuarioLogueado.setNombre(nombre);
            usuarioLogueado.setApellido(apellido);
            usuarioLogueado.setCorreo(correo);
            usuarioLogueado.setDireccion(direccion);
            usuarioLogueado.setTelefono(telefono);
            usuarioLogueado.setDni(dni);
            usuarioLogueado.setUsuario(usuario);
            usuarioLogueado.setContrasena(contrasena);
            controladorPrincipal.guardarUsuarios();
            JOptionPane.showMessageDialog(null, "Información actualizada exitosamente.");
            vistaPerfil.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "No hay un usuario logueado.");
        }
    }

    // Cargar datos del usuario actual en la vista de perfil
    private void cargarDatosUsuario() {
        Usuario usuario = controladorPrincipal.getUsuarioLogueado();
        if (usuario != null) {
            vistaPerfil.setDatosUsuario(usuario);
        }
    }
}