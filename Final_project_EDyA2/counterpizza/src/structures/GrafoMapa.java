package structures;
import java.util.*;

public class GrafoMapa {
    // Representación de una conexión/pasillo entre dos zonas
    public static class Arista {
        private String destino;
        private int tiempoSegundos;
        private boolean bloqueado;

        public Arista(String destino, int tiempoSegundos) {
            this.destino = destino;
            this.tiempoSegundos = tiempoSegundos;
            this.bloqueado = false;
        }

        public String getDestino() {
            return destino;
        }

        public int getTiempoSegundos() {
            return tiempoSegundos;
        }

        public boolean isBloqueado() {
            return bloqueado;
        }

        public void setBloqueado(boolean bloqueado) {
            this.bloqueado = bloqueado;
        }
    }

    // Resultado con la ruta óptima calculada
    public static class ResultadoRuta {
        private List<String> camino;
        private int tiempoTotal;
        private boolean existeRuta;

        public ResultadoRuta(List<String> camino, int tiempoTotal, boolean existeRuta) {
            this.camino = camino;
            this.tiempoTotal = tiempoTotal;
            this.existeRuta = existeRuta;
        }

        //Getters
        public List<String> getCamino() {
            return camino;
        }

        public int getTiempoTotal() {
            return tiempoTotal;
        }

        public boolean isExisteRuta() {
            return existeRuta;
        }

        @Override
        public String toString() {
            if (!existeRuta) {
                return "No existe ruta disponible (todos los caminos alternativos están bloqueados).";
            }
            return "Ruta óptima: " + String.join(" ➔ ", camino) + " | Tiempo estimado: " + tiempoTotal + " segundos.";
        }
    }

    // Estructura de Lista de Adyacencia
    private Map<String, List<Arista>> mapa;

    public GrafoMapa() {
        this.mapa = new HashMap<>();
        inicializarMapaMirage();
    }

    public void agregarNodo(String nombreNodo) {
        mapa.putIfAbsent(nombreNodo, new ArrayList<>());
    }

    // Conexión bidireccional entre dos zonas
    public void agregarCamino(String origen, String destino, int tiempoSegundos) {
        agregarNodo(origen);
        agregarNodo(destino);
        mapa.get(origen).add(new Arista(destino, tiempoSegundos));
        mapa.get(destino).add(new Arista(origen, tiempoSegundos));
    }

    // Cambiar estado de un camino (bloquear/desbloquear por enemigo)
    public void cambiarEstadoCamino(String nodoA, String nodoB, boolean bloqueado) {
        if (mapa.containsKey(nodoA)) {
            for (Arista arista : mapa.get(nodoA)) {
                if (arista.getDestino().equalsIgnoreCase(nodoB)) {
                    arista.setBloqueado(bloqueado);
                }
            }
        }
        if (mapa.containsKey(nodoB)) {
            for (Arista arista : mapa.get(nodoB)) {
                if (arista.getDestino().equalsIgnoreCase(nodoA)) {
                    arista.setBloqueado(bloqueado);
                }
            }
        }
    }

    // Evento aleatorio: Genera un enemigo y bloquea un camino aleatorio del mapa
    public String generarEnemigoAleatorio() {
        List<String[]> caminosDisponibles = new ArrayList<>();
        for (String origen : mapa.keySet()) {
            for (Arista arista : mapa.get(origen)) {
                if (!arista.isBloqueado()) {
                    caminosDisponibles.add(new String[]{origen, arista.getDestino()});
                }
            }
        }

        if (caminosDisponibles.isEmpty()) {
            return "¡Mapa en alerta máxima! Todos los caminos están bloqueados.";
        }

        Random rand = new Random();
        String[] caminoElegido = caminosDisponibles.get(rand.nextInt(caminosDisponibles.size()));
        cambiarEstadoCamino(caminoElegido[0], caminoElegido[1], true);

        return "¡ENEMIGO DETECTADO! Camino bloqueado entre: [" + caminoElegido[0] + " X " + caminoElegido[1] + "]";
    }

    // Reiniciar bloqueos del mapa
    public void reiniciarCaminos() {
        for (List<Arista> lista : mapa.values()) {
            for (Arista arista : lista) {
                arista.setBloqueado(false);
            }
        }
    }

    // Algoritmo de Dijkstra para la ruta más corta (utiliza MinHeap)
    public ResultadoRuta calcularRutaMasCorta(String origen, String destino) {
        if (!mapa.containsKey(origen) || !mapa.containsKey(destino)) {
            return new ResultadoRuta(Collections.emptyList(), 0, false);
        }

        Map<String, Integer> distancias = new HashMap<>();
        Map<String, String> predecesores = new HashMap<>();
        PriorityQueue<String[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> Integer.parseInt(a[1])));

        for (String nodo : mapa.keySet()) {
            distancias.put(nodo, Integer.MAX_VALUE);
        }
        distancias.put(origen, 0);
        minHeap.add(new String[]{origen, "0"});

        while (!minHeap.isEmpty()) {
            String[] actual = minHeap.poll();
            String u = actual[0];
            int distU = Integer.parseInt(actual[1]);

            if (distU > distancias.get(u)) continue;
            if (u.equalsIgnoreCase(destino)) break;

            for (Arista arista : mapa.get(u)) {
                if (arista.isBloqueado()) continue; // Salta caminos bloqueados por enemigos

                String v = arista.getDestino();
                int peso = arista.getTiempoSegundos();

                if (distancias.get(u) + peso < distancias.get(v)) {
                    distancias.put(v, distancias.get(u) + peso);
                    predecesores.put(v, u);
                    minHeap.add(new String[]{v, String.valueOf(distancias.get(v))});
                }
            }
        }
        if (distancias.get(destino) == Integer.MAX_VALUE) {
            return new ResultadoRuta(Collections.emptyList(), 0, false);
        }
        // Reconstrucción del camino optimo
        LinkedList<String> camino = new LinkedList<>();
        String paso = destino;
        while (paso != null) {
            camino.addFirst(paso);
            paso = predecesores.get(paso);
        }
        return new ResultadoRuta(camino, distancias.get(destino), true);
    }

    // Construcción del mapa de Mirage
    private void inicializarMapaMirage() {
        // Conexiones desde T Spawn
        agregarCamino("T Spawn", "Palace", 4);
        agregarCamino("T Spawn", "A Ramp", 4);
        agregarCamino("T Spawn", "Apps", 3);
        agregarCamino("T Spawn", "Mid", 3);

        // Zona A
        agregarCamino("Palace", "A Site", 3);
        agregarCamino("A Ramp", "A Site", 2);
        agregarCamino("Connector", "A Site", 3);
        agregarCamino("Jungle", "A Site", 2);
        agregarCamino("CT Spawn", "A Site", 3);

        // Zona Mid y Underpass
        agregarCamino("Mid", "Underpass", 2);
        agregarCamino("Mid", "Connector", 2);
        agregarCamino("Mid", "Window", 2);
        agregarCamino("Underpass", "Apps", 2);
        agregarCamino("Underpass", "Connector", 3);
        agregarCamino("Underpass", "Short B", 3);
        agregarCamino("Underpass", "Window", 3);
        agregarCamino("Underpass", "Ladder Room", 5); // Tramo más lento

        // Zona B
        agregarCamino("Apps", "B Site", 4);
        agregarCamino("Short B", "B Site", 3);
        agregarCamino("CT Spawn", "B Site", 3);

        // Conectores
        agregarCamino("Ladder Room", "Short B", 3);
        agregarCamino("Ladder Room", "Jungle", 4);
        agregarCamino("Connector", "Jungle", 2);
        agregarCamino("Jungle", "CT Spawn", 2);
    }
}
