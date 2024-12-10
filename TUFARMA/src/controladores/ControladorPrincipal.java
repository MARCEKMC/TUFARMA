package controladores;

import modelos.Carrito;
import modelos.ItemCarrito;
import modelos.Producto;
import modelos.Usuario;
import modelos.Pedido;
import util.GeneradorPDF;
import vistas.VistaPrincipal;
import vistas.VistaLogin;
import vistas.VistaRegistro;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorPrincipal {
    private VistaPrincipal vista;
    private List<Producto> productos;
    private List<Usuario> usuarios;
    private Carrito carrito;
    private Usuario usuarioLogueado;

    public ControladorPrincipal() {
        this.productos = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.carrito = new Carrito();
        cargarProductos();
        cargarUsuarios();
        abrirVistaPrincipal(); // Inicia con la ventana principal
    }

    // Cargar productos desde el archivo
    private void cargarProductos() {
        File archivo = new File("datos/productos.txt");
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al crear el archivo de productos.");
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader("datos/productos.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    Producto producto = new Producto(datos[0], datos[1], Double.parseDouble(datos[2]),
                            datos[3], Integer.parseInt(datos[4]));
                    productos.add(producto);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar productos.");
        }
    }

    // Cargar usuarios desde el archivo
    private void cargarUsuarios() {
        File archivo = new File("datos/usuarios.txt");
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                // Crear el administrador por defecto
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
                    bw.write("Admin,Usuario,admin@tufarma.com,Administracion,0000000000,00000000A,admin,admin123");
                    bw.newLine();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al crear el archivo de usuarios.");
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader("datos/usuarios.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 8) {
                    Usuario usuario = new Usuario(datos[0], datos[1], datos[2], datos[3],
                            datos[4], datos[5], datos[6], datos[7]);
                    usuarios.add(usuario);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar usuarios.");
        }
    }

    // Abrir la ventana principal o de administración según el usuario
    public void abrirVistaPrincipal() {
        if (usuarioLogueado != null && usuarioLogueado.getUsuario().equals("admin")) {
            new ControladorAdmin(this); // Redirige al panel de administrador
        } else {
            vista = new VistaPrincipal(this); // Redirige al panel de usuario normal
            vista.mostrarProductos(productos);
        }
    }

    // Validar usuario para login
    public boolean validarUsuario(String usuario, String contrasena) {
        for (Usuario u : usuarios) {
            if (u.getUsuario().equals(usuario) && u.getContrasena().equals(contrasena)) {
                this.usuarioLogueado = u;
                return true;
            }
        }
        return false;
    }

    // Guardar productos en el archivo
    public void guardarProductos() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("datos/productos.txt"))) {
            for (Producto p : productos) {
                bw.write(p.getId() + "," + p.getNombre() + "," + p.getPrecio() + "," +
                        p.getDescripcion() + "," + p.getCantidad());
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar productos.");
        }
    }

    // Guardar usuarios en el archivo
    public void guardarUsuarios() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("datos/usuarios.txt"))) {
            for (Usuario u : usuarios) {
                bw.write(u.getNombre() + "," + u.getApellido() + "," + u.getCorreo() + "," +
                        u.getDireccion() + "," + u.getTelefono() + "," + u.getDni() + "," +
                        u.getUsuario() + "," + u.getContrasena());
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar usuarios.");
        }
    }

    // Registrar nuevo usuario
    public void registrarUsuario(String nombre, String apellido, String correo, String direccion,
                                 String telefono, String dni, String usuario, String contrasena) {
        // Verificar si el usuario ya existe
        for (Usuario u : usuarios) {
            if (u.getUsuario().equals(usuario)) {
                JOptionPane.showMessageDialog(null, "El usuario ya existe.");
                return;
            }
        }
        Usuario nuevoUsuario = new Usuario(nombre, apellido, correo, direccion, telefono, dni, usuario, contrasena);
        usuarios.add(nuevoUsuario);
        guardarUsuarios();
        JOptionPane.showMessageDialog(null, "Registro exitoso.");
    }

    // Agregar producto al carrito
    public void agregarAlCarrito(Producto producto) {
        if (producto.getCantidad() > 0) {
            carrito.agregarProducto(producto);
            JOptionPane.showMessageDialog(null, "Producto agregado al carrito.");
        } else {
            JOptionPane.showMessageDialog(null, "Producto agotado.");
        }
    }

    // Abrir ventana de registro
    public void abrirRegistro() {
        new VistaRegistro(this);
    }

    // Abrir ventana del carrito
    public void abrirCarrito() {
        new ControladorCarrito(this, carrito);
    }

    // Procesar compra
    public void procesarCompra(String metodoPago) {
        if (usuarioLogueado == null) {
            JOptionPane.showMessageDialog(null, "Debe iniciar sesión para realizar una compra.");
            abrirLogin();
            return;
        }

        if (carrito.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El carrito está vacío.");
            return;
        }

        // Actualizar stock
        for (ItemCarrito item : carrito.getItems()) {
            Producto p = item.getProducto();
            for (Producto prod : productos) {
                if (prod.getId().equals(p.getId())) {
                    prod.setCantidad(prod.getCantidad() - item.getCantidad());
                }
            }
        }
        guardarProductos();

        // Crear pedido
        String idPedido = "PED" + (System.currentTimeMillis());
        Pedido pedido = new Pedido(idPedido, usuarioLogueado, new ArrayList<>(carrito.getItems()), carrito.calcularTotal());

        // Generar factura PDF
        GeneradorPDF.generarFactura(pedido);

        // Vaciar carrito
        carrito.vaciarCarrito();

        JOptionPane.showMessageDialog(null, "Compra realizada exitosamente. Se ha generado su factura en PDF.");
    }

    // Buscar productos
    public List<Producto> buscarProductos(String termino) {
        List<Producto> resultados = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getNombre().toLowerCase().contains(termino.toLowerCase())) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    // Obtener lista completa de productos
    public List<Producto> getProductos() {
        return productos;
    }

    // Obtener carrito
    public Carrito getCarrito() {
        return carrito;
    }

    // Obtener usuario logueado
    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    // Abrir la ventana de login
    public void abrirLogin() {
        new VistaLogin(this);
    }
}