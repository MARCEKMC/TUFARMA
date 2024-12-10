package modelos;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private List<ItemCarrito> items;

    public Carrito() {
        this.items = new ArrayList<>();
    }

    // Agregar producto al carrito
    public void agregarProducto(Producto producto) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getId().equals(producto.getId())) {
                item.setCantidad(item.getCantidad() + 1);
                return;
            }
        }
        items.add(new ItemCarrito(producto, 1));
    }

    // Eliminar producto del carrito
    public void eliminarProducto(Producto producto) {
        ItemCarrito itemEliminar = null;
        for (ItemCarrito item : items) {
            if (item.getProducto().getId().equals(producto.getId())) {
                itemEliminar = item;
                break;
            }
        }
        if (itemEliminar != null) {
            items.remove(itemEliminar);
        }
    }

    // Obtener ítems del carrito
    public List<ItemCarrito> getItems() {
        return items;
    }

    // Calcular total del carrito
    public double calcularTotal() {
        double total = 0.0;
        for (ItemCarrito item : items) {
            total += item.getProducto().getPrecio() * item.getCantidad();
        }
        return total;
    }

    // Vaciar el carrito
    public void vaciarCarrito() {
        items.clear();
    }
}