package vistas;

import controladores.ControladorPrincipal;
import controladores.ControladorAdmin;
import controladores.ControladorUsuario; // Importar ControladorUsuario
import modelos.Producto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VistaPrincipal extends JFrame {
    private ControladorPrincipal controlador;
    private JPanel panelProductos;
    private JTextField campoBusqueda;
    private JButton botonLogin;
    private JButton botonCarrito;
    private JButton botonAdmin;
    private JButton botonPerfil;

    public VistaPrincipal(ControladorPrincipal controlador) {
        this.controlador = controlador;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("TUFARMA - Farmacia Online");
        setSize(1200, 800); // Ajustar tamaño para mantener proporciones
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Barra Superior
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(new Color(70, 130, 180));
        barraSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Nombre de la farmacia
        JLabel nombreFarmacia = new JLabel("TUFARMA");
        nombreFarmacia.setFont(new Font("Arial", Font.BOLD, 24));
        nombreFarmacia.setForeground(Color.WHITE);
        barraSuperior.add(nombreFarmacia, BorderLayout.WEST);

        // Panel derecho de la barra superior
        JPanel derechaBarra = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        derechaBarra.setOpaque(false);

        // Barra de búsqueda
        campoBusqueda = new JTextField(20);
        JButton botonBuscar = new JButton("Buscar");
        botonBuscar.setBackground(Color.WHITE);
        botonBuscar.setForeground(Color.BLACK);
        botonBuscar.setFocusPainted(false);
        derechaBarra.add(campoBusqueda);
        derechaBarra.add(botonBuscar);

        // Botón Perfil
        botonPerfil = new JButton("Perfil");
        botonPerfil.setBackground(Color.WHITE);
        botonPerfil.setForeground(Color.BLACK);
        botonPerfil.setFocusPainted(false);
        derechaBarra.add(botonPerfil);

        // Botón Login
        botonLogin = new JButton("Login");
        botonLogin.setBackground(Color.WHITE);
        botonLogin.setForeground(Color.BLACK);
        botonLogin.setFocusPainted(false);
        derechaBarra.add(botonLogin);

        // Botón Carrito
        botonCarrito = new JButton("Carrito");
        botonCarrito.setBackground(Color.WHITE);
        botonCarrito.setForeground(Color.BLACK);
        botonCarrito.setFocusPainted(false);
        derechaBarra.add(botonCarrito);

        // Botón Admin (visible solo para el administrador)
        botonAdmin = new JButton("Admin");
        botonAdmin.setBackground(Color.WHITE);
        botonAdmin.setForeground(Color.BLACK);
        botonAdmin.setFocusPainted(false);
        botonAdmin.setVisible(false); // Inicialmente oculto
        derechaBarra.add(botonAdmin);

        barraSuperior.add(derechaBarra, BorderLayout.EAST);
        add(barraSuperior, BorderLayout.NORTH);

        // Panel de Productos con BoxLayout Y_AXIS
        panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));
        panelProductos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Scroll Pane para panelProductos
        JScrollPane scrollProductos = new JScrollPane(panelProductos);
        scrollProductos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollProductos.getVerticalScrollBar().setUnitIncrement(16); // Suavizar el scroll
        add(scrollProductos, BorderLayout.CENTER);

        // Acciones de botones
        botonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String termino = campoBusqueda.getText();
                if (termino.trim().isEmpty()) {
                    mostrarProductos(controlador.getProductos());
                } else {
                    List<Producto> resultados = controlador.buscarProductos(termino);
                    mostrarProductos(resultados);
                }
            }
        });

        botonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlador.abrirLogin();
            }
        });

        botonCarrito.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlador.abrirCarrito();
            }
        });

        botonAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Este botón solo debe ser visible para el administrador, así que no es necesario validar aquí
                // Si lo deseas, puedes agregar lógica adicional aquí
            }
        });

        botonPerfil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador.getUsuarioLogueado() != null) {
                    new ControladorUsuario(controlador); // Abrir ControladorUsuario
                } else {
                    JOptionPane.showMessageDialog(null, "Debe iniciar sesión para ver su perfil.");
                    controlador.abrirLogin();
                }
            }
        });

        setVisible(true);
    }

    // Mostrar productos en el panel
    public void mostrarProductos(List<Producto> productos) {
        panelProductos.removeAll();

        if (productos.isEmpty()) {
            JLabel mensaje = new JLabel("No se encontraron productos.");
            mensaje.setFont(new Font("Arial", Font.ITALIC, 16));
            mensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelProductos.add(mensaje);
        } else {
            int productosPorFila = 3;
            for (int i = 0; i < productos.size(); i += productosPorFila) {
                int end = Math.min(i + productosPorFila, productos.size());
                List<Producto> filaProductos = productos.subList(i, end);
                JPanel fila = crearFilaProductos(filaProductos);
                panelProductos.add(fila);
                panelProductos.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio entre filas
            }
        }

        panelProductos.revalidate();
        panelProductos.repaint();

        // Verificar si el usuario está logueado para mostrar el botón de admin
        if (controlador.getUsuarioLogueado() != null &&
                controlador.getUsuarioLogueado().getUsuario().equals("admin")) {
            botonAdmin.setVisible(true);
        } else {
            botonAdmin.setVisible(false);
        }
    }

    // Crear una fila de productos
    private JPanel crearFilaProductos(List<Producto> filaProductos) {
        JPanel fila = new JPanel();
        fila.setLayout(new BoxLayout(fila, BoxLayout.X_AXIS));
        fila.setAlignmentX(Component.CENTER_ALIGNMENT);

        for (Producto p : filaProductos) {
            JPanel panelProducto = crearPanelProducto(p);
            fila.add(panelProducto);
            fila.add(Box.createRigidArea(new Dimension(20, 0))); // Espacio entre productos
        }

        // Rellenar espacio si hay menos de 3 productos en la fila
        int espaciosRestantes = 3 - filaProductos.size();
        for (int i = 0; i < espaciosRestantes; i++) {
            // Utilizar Component en lugar de JPanel
            Component espacio = Box.createRigidArea(new Dimension(300, 100));
            fila.add(espacio);
            fila.add(Box.createRigidArea(new Dimension(20, 0)));
        }

        return fila;
    }

    // Método para crear el panel de un solo producto
    private JPanel crearPanelProducto(Producto p) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setBackground(Color.WHITE);

        // Establecer el tamaño del panel para que el ancho sea 3 veces su alto (por ejemplo, 300x100 píxeles)
        Dimension tamanoPanel = new Dimension(300, 100);
        panel.setPreferredSize(tamanoPanel);
        panel.setMinimumSize(tamanoPanel);
        panel.setMaximumSize(tamanoPanel);

        // Nombre del producto centrado
        JLabel lblNombre = new JLabel(p.getNombre(), SwingConstants.CENTER);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblNombre, BorderLayout.NORTH);

        // Descripción limitada a 2 líneas usando HTML
        String descripcion = p.getDescripcion();
        // Truncar la descripción si es muy larga
        if (descripcion.length() > 100) { // Ajusta el límite según sea necesario
            descripcion = descripcion.substring(0, 100) + "...";
        }
        JLabel lblDescripcion = new JLabel("<html><body style='width:280px'>" + descripcion + "</body></html>");
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDescripcion.setVerticalAlignment(SwingConstants.TOP);
        panel.add(lblDescripcion, BorderLayout.CENTER);

        // Panel para precio y botón "Agregar Producto"
        JPanel panelPrecioBoton = new JPanel(new BorderLayout());
        panelPrecioBoton.setBackground(Color.WHITE);
        panelPrecioBoton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Precio alineado a la izquierda
        JLabel lblPrecio = new JLabel("Precio: S/ " + String.format("%.2f", p.getPrecio()));
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 12));
        panelPrecioBoton.add(lblPrecio, BorderLayout.WEST);

        // Botón "Agregar Producto" alineado a la derecha
        JButton botonAgregar = new JButton("Agregar Producto");
        botonAgregar.setBackground(new Color(60, 179, 113));
        botonAgregar.setForeground(Color.WHITE);
        botonAgregar.setFocusPainted(false);
        botonAgregar.setFont(new Font("Arial", Font.BOLD, 12));

        // Acción del botón Agregar Producto
        botonAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlador.agregarAlCarrito(p);
            }
        });

        panelPrecioBoton.add(botonAgregar, BorderLayout.EAST);
        panel.add(panelPrecioBoton, BorderLayout.SOUTH);

        return panel;
    }
}