package util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import modelos.Pedido;
import modelos.Producto;
import modelos.ItemCarrito;
import modelos.Usuario;

import java.io.FileOutputStream;
import java.util.List;

public class GeneradorPDF {
    public static void generarFactura(Pedido pedido) {
        Document documento = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            String nombreArchivo = "factura_" + pedido.getId() + ".pdf";
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            documento.open();

            // Fuentes
            Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Font fuenteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font fuenteContenido = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font fuentePie = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12);

            // Título de la factura
            Paragraph titulo = new Paragraph("TUFARMA", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            // Línea horizontal
            LineSeparator linea = new LineSeparator();
            linea.setLineColor(BaseColor.BLACK);
            documento.add(new Chunk(linea));

            // Información de la factura
            PdfPTable tablaInfo = new PdfPTable(2);
            tablaInfo.setWidthPercentage(100);
            tablaInfo.setSpacingBefore(10f);
            tablaInfo.setSpacingAfter(10f);
            tablaInfo.setWidths(new float[]{1, 2});

            // Fecha
            tablaInfo.addCell(getCell("FECHA:", PdfPCell.ALIGN_LEFT, fuenteSubtitulo));
            tablaInfo.addCell(getCell(pedido.getFecha(), PdfPCell.ALIGN_LEFT, fuenteContenido));

            // Cliente
            tablaInfo.addCell(getCell("CLIENTE:", PdfPCell.ALIGN_LEFT, fuenteSubtitulo));
            Usuario cliente = pedido.getUsuario();
            String infoCliente = cliente.getNombre() + " " + cliente.getApellido() + "\n" +
                    "Correo: " + cliente.getCorreo() + "\n" +
                    "Dirección: " + cliente.getDireccion() + "\n" +
                    "Teléfono: " + cliente.getTelefono();
            tablaInfo.addCell(getCell(infoCliente, PdfPCell.ALIGN_LEFT, fuenteContenido));

            // Pedido
            tablaInfo.addCell(getCell("PEDIDO:", PdfPCell.ALIGN_LEFT, fuenteSubtitulo));
            tablaInfo.addCell(getCell(pedido.getId(), PdfPCell.ALIGN_LEFT, fuenteContenido));

            documento.add(tablaInfo);

            // Línea horizontal
            documento.add(new Chunk(linea));

            // Tabla de Productos
            PdfPTable tablaProductos = new PdfPTable(3);
            tablaProductos.setWidthPercentage(100);
            tablaProductos.setSpacingBefore(10f);
            tablaProductos.setSpacingAfter(10f);
            tablaProductos.setWidths(new float[]{4, 2, 2});

            // Encabezados de la tabla
            tablaProductos.addCell(getCell("PRODUCTO", PdfPCell.ALIGN_CENTER, fuenteSubtitulo));
            tablaProductos.addCell(getCell("CANTIDAD", PdfPCell.ALIGN_CENTER, fuenteSubtitulo));
            tablaProductos.addCell(getCell("PRECIO (S/)", PdfPCell.ALIGN_CENTER, fuenteSubtitulo));

            // Línea horizontal debajo de los encabezados
            LineSeparator lineaEncabezado = new LineSeparator();
            lineaEncabezado.setLineColor(BaseColor.BLACK);
            documento.add(new Chunk(lineaEncabezado));

            // Productos
            List<ItemCarrito> items = pedido.getProductosCarrito();
            for (ItemCarrito item : items) {
                Producto p = item.getProducto();
                int cantidad = item.getCantidad();
                tablaProductos.addCell(getCell(p.getNombre(), PdfPCell.ALIGN_LEFT, fuenteContenido));
                tablaProductos.addCell(getCell(String.valueOf(cantidad), PdfPCell.ALIGN_CENTER, fuenteContenido));
                tablaProductos.addCell(getCell(String.format("S/ %.2f", p.getPrecio()), PdfPCell.ALIGN_RIGHT, fuenteContenido));
            }

            // Línea horizontal debajo de los productos
            LineSeparator lineaProductos = new LineSeparator();
            lineaProductos.setLineColor(BaseColor.BLACK);
            documento.add(new Chunk(lineaProductos));

            documento.add(tablaProductos);

            // Cálculos de Subtotal, IGV y Total
            double subtotal = carritoCalcularSubtotal(items);
            double igv = subtotal * 0.18;
            double total = subtotal + igv;

            PdfPTable tablaTotales = new PdfPTable(2);
            tablaTotales.setWidthPercentage(40);
            tablaTotales.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tablaTotales.setSpacingBefore(10f);
            tablaTotales.setSpacingAfter(10f);
            tablaTotales.setWidths(new float[]{1, 1});

            tablaTotales.addCell(getCell("Subtotal:", PdfPCell.ALIGN_LEFT, fuenteSubtitulo));
            tablaTotales.addCell(getCell(String.format("S/ %.2f", subtotal), PdfPCell.ALIGN_RIGHT, fuenteContenido));

            tablaTotales.addCell(getCell("IGV (18%):", PdfPCell.ALIGN_LEFT, fuenteSubtitulo));
            tablaTotales.addCell(getCell(String.format("S/ %.2f", igv), PdfPCell.ALIGN_RIGHT, fuenteContenido));

            tablaTotales.addCell(getCell("Total:", PdfPCell.ALIGN_LEFT, fuenteSubtitulo));
            tablaTotales.addCell(getCell(String.format("S/ %.2f", total), PdfPCell.ALIGN_RIGHT, fuenteContenido));

            documento.add(tablaTotales);

            // Mensaje de agradecimiento
            Paragraph agradecimiento = new Paragraph("Gracias por su preferencia.", fuentePie);
            agradecimiento.setAlignment(Element.ALIGN_CENTER);
            documento.add(agradecimiento);

            // Línea horizontal final
            documento.add(new Chunk(linea));

            documento.close();
            System.out.println("Factura generada exitosamente: " + nombreArchivo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para crear celdas de tabla con estilo
    private static PdfPCell getCell(String texto, int alignment, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setPadding(5);
        celda.setHorizontalAlignment(alignment);
        celda.setBorder(Rectangle.NO_BORDER);
        return celda;
    }

    // Método para calcular el subtotal a partir de los ítems del carrito
    private static double carritoCalcularSubtotal(List<ItemCarrito> items) {
        double subtotal = 0.0;
        for (ItemCarrito item : items) {
            subtotal += item.getProducto().getPrecio() * item.getCantidad();
        }
        return subtotal;
    }
}