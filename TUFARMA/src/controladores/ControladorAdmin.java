package controladores;

import modelos.Producto;
import vistas.VistaAdmin;

import javax.swing.*;
import java.io.*;
import java.util.List;

public class ControladorAdmin {
    private VistaAdmin vista;
    private ControladorPrincipal controladorPrincipal;

    public ControladorAdmin(ControladorPrincipal controladorPrincipal) {
        this.controladorPrincipal = controladorPrincipal;
        this.vista = new VistaAdmin(this);
        this.vista.setVisible(true);
        this.vista.mostrarProductos(controladorPrincipal.getProductos());
    }

    // Método para agregar productos
    public void agregarProducto(String nombre, double precio, String descripcion, int cantidad) {
        String id = generarIdProducto();
        Producto nuevoProducto = new Producto(id, nombre, precio, descripcion, cantidad);
        controladorPrincipal.getProductos().add(nuevoProducto);
        controladorPrincipal.guardarProductos();
        vista.mostrarProductos(controladorPrincipal.getProductos());
        JOptionPane.showMessageDialog(null, "Producto agregado exitosamente.");
    }

    // Método para eliminar productos por ID
    public void eliminarProducto(String id) {
        Producto productoEliminar = null;
        for (Producto p : controladorPrincipal.getProductos()) {
            if (p.getId().equals(id)) {
                productoEliminar = p;
                break;
            }
        }
        if (productoEliminar != null) {
            controladorPrincipal.getProductos().remove(productoEliminar);
            controladorPrincipal.guardarProductos();
            vista.mostrarProductos(controladorPrincipal.getProductos());
            JOptionPane.showMessageDialog(null, "Producto eliminado exitosamente.");
        } else {
            JOptionPane.showMessageDialog(null, "Producto no encontrado.");
        }
    }

    // Generar ID único para el producto
    private String generarIdProducto() {
        return "P" + (controladorPrincipal.getProductos().size() + 1);
    }
}