package persistence;

import models.Pedido;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GestorPersistencia {

    private static final String ARCHIVO_HISTORIAL = "historial_pedidos.json";

    public static void registrarTransaccion(Pedido pedido, int tiempoEntregaSegundos) {
        try {
            File file = new File(ARCHIVO_HISTORIAL);
            boolean esNuevo = !file.exists() || file.length() == 0;

            FileWriter writer = new FileWriter(file, true);

            if (esNuevo) {
                writer.write("[\n");
            } else {
                writer.write(",\n");
            }

            String jsonPedido = "  {\n" +
                    "    \"idPedido\": " + pedido.getIdPedido() + ",\n" +
                    "    \"cliente\": \"" + pedido.getUsuario().getNombre() + "\",\n" +
                    "    \"telefono\": \"" + pedido.getUsuario().getNumeroContacto() + "\",\n" +
                    "    \"destino\": \"" + pedido.getUsuario().getNodoUbicacion() + "\",\n" +
                    "    \"pizza\": \"" + pedido.getPizza().getSabor() + " (" + pedido.getPizza().getSize() + ")\",\n" +
                    "    \"montoTotal\": " + pedido.getPizza().getPrecio() + ",\n" +
                    "    \"tiempoEstimadoSegundos\": " + tiempoEntregaSegundos + ",\n" +
                    "    \"estado\": \"" + pedido.getEstado() + "\"\n" +
                    "  }";

            writer.write(jsonPedido);
            writer.close();
            System.out.println("Transaccion registrada con exito en " + ARCHIVO_HISTORIAL);
        } catch (IOException e) {
            System.err.println("Error al guardar transaccion: " + e.getMessage());
        }
    }
}
