package vistas;

import controladores.ControladorAdmin;
import modelos.Producto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VistaAdmin extends JFrame {
    private ControladorAdmin controlador;
    private JPanel panelProductos;
    private JTextField campoNombre;
    private JTextField campoPrecio;
    private JTextField campoDescripcion;
    private JTextField campoCantidad;
    private JTextField campoEliminarId;

    public VistaAdmin(ControladorAdmin controlador) {
        this.controlador = controlador;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Panel de Administración - TUFARMA");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel superior para agregar productos
        JPanel panelAgregar = new JPanel(new GridLayout(5, 2, 10, 10));
        panelAgregar.setBorder(BorderFactory.createTitledBorder("Agregar Nuevo Producto"));
        panelAgregar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelAgregar.add(new JLabel("Nombre:"));
        campoNombre = new JTextField();
        panelAgregar.add(campoNombre);

        panelAgregar.add(new JLabel("Precio:"));
        campoPrecio = new JTextField();
        panelAgregar.add(campoPrecio);

        panelAgregar.add(new JLabel("Descripción:"));
        campoDescripcion = new JTextField();
        panelAgregar.add(campoDescripcion);

        panelAgregar.add(new JLabel("Cantidad:"));
        campoCantidad = new JTextField();
        panelAgregar.add(campoCantidad);

        JButton botonAgregar = new JButton("Agregar Producto");
        panelAgregar.add(botonAgregar);
        panelAgregar.add(new JLabel()); // Espacio vacío

        add(panelAgregar, BorderLayout.NORTH);

        // Panel central para mostrar productos
        panelProductos = new JPanel();
        panelProductos.setLayout(new GridLayout(0, 3, 20, 20));
        panelProductos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JScrollPane scrollProductos = new JScrollPane(panelProductos);
        add(scrollProductos, BorderLayout.CENTER);

        // Panel inferior para eliminar productos
        JPanel panelEliminar = new JPanel(new FlowLayout());
        panelEliminar.setBorder(BorderFactory.createTitledBorder("Eliminar Producto"));
        panelEliminar.add(new JLabel("ID del Producto:"));
        campoEliminarId = new JTextField(10);
        panelEliminar.add(campoEliminarId);
        JButton botonEliminar = new JButton("Eliminar");
        panelEliminar.add(botonEliminar);
        add(panelEliminar, BorderLayout.SOUTH);

        // Acción del botón Agregar
        botonAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = campoNombre.getText();
                String precioStr = campoPrecio.getText();
                String descripcion = campoDescripcion.getText();
                String cantidadStr = campoCantidad.getText();

                if (nombre.trim().isEmpty() || precioStr.trim().isEmpty() ||
                        descripcion.trim().isEmpty() || cantidadStr.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
                    return;
                }

                double precio;
                int cantidad;
                try {
                    precio = Double.parseDouble(precioStr);
                    cantidad = Integer.parseInt(cantidadStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Precio y Cantidad deben ser números.");
                    return;
                }

                controlador.agregarProducto(nombre, precio, descripcion, cantidad);
                limpiarCamposAgregar();
            }
        });

        // Acción del botón Eliminar
        botonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = campoEliminarId.getText();
                if (id.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese el ID del producto a eliminar.");
                    return;
                }
                controlador.eliminarProducto(id);
                campoEliminarId.setText("");
            }
        });

        setVisible(true);
    }

    // Mostrar productos en el panel
    public void mostrarProductos(List<Producto> productos) {
        panelProductos.removeAll();
        for (Producto p : productos) {
            JPanel panelProducto = new JPanel(new BorderLayout());
            panelProducto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            panelProducto.setBackground(Color.WHITE);

            // Nombre del producto
            JLabel nombre = new JLabel("<html><b>" + p.getNombre() + "</b></html>", SwingConstants.CENTER);
            panelProducto.add(nombre, BorderLayout.NORTH);

            // Descripción
            JTextArea descripcion = new JTextArea(p.getDescripcion());
            descripcion.setLineWrap(true);
            descripcion.setWrapStyleWord(true);
            descripcion.setEditable(false);
            descripcion.setBackground(Color.WHITE);
            panelProducto.add(descripcion, BorderLayout.CENTER);

            // Precio y cantidad
            JPanel panelInferior = new JPanel(new GridLayout(2, 1));
            panelInferior.setBackground(Color.WHITE);
            JLabel precio = new JLabel("Precio: $" + p.getPrecio());
            JLabel cantidad = new JLabel("Cantidad: " + p.getCantidad());
            panelInferior.add(precio);
            panelInferior.add(cantidad);

            panelProducto.add(panelInferior, BorderLayout.SOUTH);

            panelProductos.add(panelProducto);
        }
        panelProductos.revalidate();
        panelProductos.repaint();
    }

    // Limpiar campos de agregar producto
    private void limpiarCamposAgregar() {
        campoNombre.setText("");
        campoPrecio.setText("");
        campoDescripcion.setText("");
        campoCantidad.setText("");
    }
}