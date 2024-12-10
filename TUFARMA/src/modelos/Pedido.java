package modelos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Pedido {
    private String id;
    private Usuario usuario;
    private List<ItemCarrito> productosCarrito;
    private double total;
    private String fecha;

    public Pedido(String id, Usuario usuario, List<ItemCarrito> productosCarrito, double total) {
        this.id = id;
        this.usuario = usuario;
        this.productosCarrito = productosCarrito;
        this.total = total;
        this.fecha = obtenerFechaActual();
    }

    private String obtenerFechaActual() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(new Date());
    }

    // Getters

    public String getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<ItemCarrito> getProductosCarrito() {
        return productosCarrito;
    }

    public double getTotal() {
        return total;
    }

    public String getFecha() {
        return fecha;
    }
}