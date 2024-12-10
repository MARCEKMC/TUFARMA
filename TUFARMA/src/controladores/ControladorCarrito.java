package controladores;

import modelos.Carrito;
import modelos.Producto;
import vistas.VistaCarrito;

public class ControladorCarrito {
    private ControladorPrincipal controladorPrincipal;
    private Carrito carrito;
    private VistaCarrito vistaCarrito;

    public ControladorCarrito(ControladorPrincipal controladorPrincipal, Carrito carrito) {
        this.controladorPrincipal = controladorPrincipal;
        this.carrito = carrito;
        this.vistaCarrito = new VistaCarrito(this, carrito);
        this.vistaCarrito.setVisible(true);
    }

    // Obtener el ControladorPrincipal si es necesario
    public ControladorPrincipal getControladorPrincipal() {
        return controladorPrincipal;
    }

    // Método para eliminar un producto del carrito
    public void eliminarDelCarrito(Producto producto) {
        carrito.eliminarProducto(producto);
        vistaCarrito.actualizarCarrito();
    }

    // Método para procesar la compra
    public void procesarCompra(String metodoPago) {
        controladorPrincipal.procesarCompra(metodoPago);
    }
}