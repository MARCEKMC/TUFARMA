package vistas;

import controladores.ControladorCarrito;
import modelos.ItemCarrito;
import modelos.Carrito;
import modelos.Producto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VistaCarrito extends JFrame {
    private ControladorCarrito controladorCarrito;
    private Carrito carrito;
    private JPanel panelCarrito;
    private JLabel labelTotal;
    private JButton botonComprar;

    public VistaCarrito(ControladorCarrito controladorCarrito, Carrito carrito) {
        this.controladorCarrito = controladorCarrito;
        this.carrito = carrito;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Carrito de Compras - TUFARMA");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel central para mostrar productos del carrito
        panelCarrito = new JPanel();
        panelCarrito.setLayout(new GridBagLayout());
        panelCarrito.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JScrollPane scrollCarrito = new JScrollPane(panelCarrito);
        add(scrollCarrito, BorderLayout.CENTER);

        // Panel inferior para total y botón comprar
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        labelTotal = new JLabel("Total: S/ " + String.format("%.2f", carrito.calcularTotal()));
        labelTotal.setFont(new Font("Arial", Font.BOLD, 16));
        panelInferior.add(labelTotal, BorderLayout.WEST);

        botonComprar = new JButton("Comprar");
        botonComprar.setBackground(new Color(60, 179, 113));
        botonComprar.setForeground(Color.WHITE);
        botonComprar.setFocusPainted(false);
        panelInferior.add(botonComprar, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);

        // Acción del botón Comprar
        botonComprar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (carrito.getItems().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El carrito está vacío.");
                    return;
                }

                // Seleccionar método de pago
                String[] opciones = {"Tarjeta", "Yape", "Plin"};
                String metodoPago = (String) JOptionPane.showInputDialog(null, "Selecciona un método de pago:",
                        "Pago", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

                if (metodoPago != null) {
                    controladorCarrito.procesarCompra(metodoPago);
                    dispose();
                }
            }
        });

        mostrarCarrito();
    }

    // Mostrar productos en el carrito
    public void mostrarCarrito() {
        panelCarrito.removeAll();
        List<ItemCarrito> items = carrito.getItems();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        if (items.isEmpty()) {
            JLabel mensaje = new JLabel("El carrito está vacío.");
            mensaje.setFont(new Font("Arial", Font.ITALIC, 14));
            mensaje.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 4;
            panelCarrito.add(mensaje, gbc);
        } else {
            // Encabezados
            JLabel lblProducto = new JLabel("PRODUCTO", SwingConstants.CENTER);
            lblProducto.setFont(new Font("Arial", Font.BOLD, 12));
            JLabel lblCantidad = new JLabel("CANTIDAD", SwingConstants.CENTER);
            lblCantidad.setFont(new Font("Arial", Font.BOLD, 12));
            JLabel lblPrecio = new JLabel("PRECIO (S/)", SwingConstants.CENTER);
            lblPrecio.setFont(new Font("Arial", Font.BOLD, 12));
            JLabel lblAcciones = new JLabel("ACCIONES", SwingConstants.CENTER);
            lblAcciones.setFont(new Font("Arial", Font.BOLD, 12));

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            panelCarrito.add(lblProducto, gbc);

            gbc.gridx = 1;
            panelCarrito.add(lblCantidad, gbc);

            gbc.gridx = 2;
            panelCarrito.add(lblPrecio, gbc);

            gbc.gridx = 3;
            panelCarrito.add(lblAcciones, gbc);

            // Línea horizontal debajo de los encabezados
            JSeparator separator = new JSeparator();
            separator.setPreferredSize(new Dimension(650, 1));
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 4;
            panelCarrito.add(separator, gbc);

            // Listar los ítems del carrito
            int row = 2;
            for (ItemCarrito item : items) {
                Producto p = item.getProducto();
                int cantidad = item.getCantidad();
                double precio = p.getPrecio();

                // Producto
                JLabel lblNombre = new JLabel(p.getNombre(), SwingConstants.CENTER);
                lblNombre.setFont(new Font("Arial", Font.PLAIN, 12));
                gbc.gridx = 0;
                gbc.gridy = row;
                gbc.gridwidth = 1;
                panelCarrito.add(lblNombre, gbc);

                // Cantidad
                JLabel lblCantidadItem = new JLabel(String.valueOf(cantidad), SwingConstants.CENTER);
                lblCantidadItem.setFont(new Font("Arial", Font.PLAIN, 12));
                gbc.gridx = 1;
                panelCarrito.add(lblCantidadItem, gbc);

                // Precio
                JLabel lblPrecioItem = new JLabel("S/ " + String.format("%.2f", precio), SwingConstants.CENTER);
                lblPrecioItem.setFont(new Font("Arial", Font.PLAIN, 12));
                gbc.gridx = 2;
                panelCarrito.add(lblPrecioItem, gbc);

                // Botón Eliminar
                JButton btnEliminar = new JButton("X");
                btnEliminar.setBackground(new Color(220, 20, 60));
                btnEliminar.setForeground(Color.WHITE);
                btnEliminar.setFocusPainted(false);
                btnEliminar.setToolTipText("Eliminar producto del carrito");
                gbc.gridx = 3;
                panelCarrito.add(btnEliminar, gbc);

                // Acción del botón Eliminar
                btnEliminar.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int confirm = JOptionPane.showConfirmDialog(null,
                                "¿Deseas eliminar este producto del carrito?",
                                "Confirmar Eliminación",
                                JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            controladorCarrito.eliminarDelCarrito(item.getProducto());
                            mostrarCarrito();
                        }
                    }
                });

                row++;
            }

            // Línea horizontal debajo de los productos
            JSeparator separatorFinal = new JSeparator();
            separatorFinal.setPreferredSize(new Dimension(650, 1));
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.gridwidth = 4;
            panelCarrito.add(separatorFinal, gbc);
        }

    }

    // Método separado fuera de mostrarCarrito()
    public void actualizarCarrito() {
        mostrarCarrito();
    }
}