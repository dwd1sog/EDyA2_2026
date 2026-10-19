package structures;

import java.util.*;

public class GrafoMapa {

    public static class Arista {
        private String destino;
        private int tiempoSegundos;
        private boolean bloqueado;

        public Arista(String destino, int tiempoSegundos) {
            this.destino = destino;
            this.tiempoSegundos = tiempoSegundos;
            this.bloqueado = false;
        }

        public String getDestino() { return destino; }
        public int getTiempoSegundos() { return tiempoSegundos; }
        public boolean isBloqueado() { return bloqueado; }
        public void setBloqueado(boolean bloqueado) { this.bloqueado = bloqueado; }
    }

    public static class ResultadoRuta {
        private List<String> camino;
        private int tiempoTotal;
        private boolean existeRuta;

        public ResultadoRuta(List<String> camino, int tiempoTotal, boolean existeRuta) {
            this.camino = camino;
            this.tiempoTotal = tiempoTotal;
            this.existeRuta = existeRuta;
        }

        public List<String> getCamino() { return camino; }
        public int getTiempoTotal() { return tiempoTotal; }
        public boolean isExisteRuta() { return existeRuta; }

        @Override
        public String toString() {
            if (!existeRuta) {
                return "No existe ruta disponible (todos los caminos alternativos estan bloqueados por enemigos).";
            }
            return "Ruta optima: " + String.join(" -> ", camino) + " | Tiempo estimado: " + tiempoTotal + " segundos.";
        }
    }

    private Map<String, List<Arista>> mapa;

    public GrafoMapa() {
        this.mapa = new HashMap<>();
        inicializarMapaMirage();
    }

    public Map<String, List<Arista>> getMapa() {
        return mapa;
    }

    public boolean esPuntoDePaso(String nombreNodo) {
        return nombreNodo != null && nombreNodo.startsWith("WP_");
    }

    public void agregarNodo(String nombreNodo) {
        mapa.putIfAbsent(nombreNodo, new ArrayList<>());
    }

    public void agregarCamino(String origen, String destino, int tiempoSegundos) {
        agregarNodo(origen);
        agregarNodo(destino);
        mapa.get(origen).add(new Arista(destino, tiempoSegundos));
        mapa.get(destino).add(new Arista(origen, tiempoSegundos));
    }

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

    public List<String[]> getCaminosBloqueados() {
        List<String[]> bloqueados = new ArrayList<>();
        Set<String> procesados = new HashSet<>();

        for (String origen : mapa.keySet()) {
            for (Arista arista : mapa.get(origen)) {
                if (arista.isBloqueado()) {
                    String clave1 = origen + "-" + arista.getDestino();
                    String clave2 = arista.getDestino() + "-" + origen;
                    if (!procesados.contains(clave1) && !procesados.contains(clave2)) {
                        bloqueados.add(new String[]{origen, arista.getDestino()});
                        procesados.add(clave1);
                        procesados.add(clave2);
                    }
                }
            }
        }
        return bloqueados;
    }

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
            return "Mapa en alerta maxima! Todos los caminos estan bloqueados.";
        }

        Random rand = new Random();
        String[] caminoElegido = caminosDisponibles.get(rand.nextInt(caminosDisponibles.size()));
        cambiarEstadoCamino(caminoElegido[0], caminoElegido[1], true);

        return "ENEMIGO DETECTADO! Camino bloqueado entre: [" + caminoElegido[0] + " - " + caminoElegido[1] + "]";
    }

    public void reiniciarCaminos() {
        for (List<Arista> lista : mapa.values()) {
            for (Arista arista : lista) {
                arista.setBloqueado(false);
            }
        }
    }

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
                if (arista.isBloqueado()) continue;

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

        LinkedList<String> caminoCompleto = new LinkedList<>();
        String paso = destino;
        while (paso != null) {
            caminoCompleto.addFirst(paso);
            paso = predecesores.get(paso);
        }

        return new ResultadoRuta(caminoCompleto, distancias.get(destino), true);
    }

    private void inicializarMapaMirage() {
        // T Spawn conex (TV es el acceso principal a TV, Apps y Mid)
        agregarCamino("T Spawn", "WP_T_Palace", 2);
        agregarCamino("WP_T_Palace", "Palace", 2);

        agregarCamino("T Spawn", "WP_T_Ramp", 2);
        agregarCamino("WP_T_Ramp", "A Ramp", 2);

        agregarCamino("T Spawn", "TV", 2);
        agregarCamino("TV", "Apps", 2);
        agregarCamino("TV", "Mid", 2);

        // A Site conex
        agregarCamino("Palace", "A Site", 3);
        agregarCamino("A Ramp", "A Site", 2);
        agregarCamino("A Site", "WP_Conn_A", 2);
        agregarCamino("WP_Conn_A", "Connector", 1);
        agregarCamino("A Site", "Jungle", 2);
        agregarCamino("A Site", "CT Spawn", 3);

        // Mid conex
        agregarCamino("Mid", "Window", 3);
        agregarCamino("Mid", "Connector", 2);
        agregarCamino("Mid", "WP_Mid_Short", 1);
        agregarCamino("WP_Mid_Short", "Short B", 3);

        // Ladder Room conex
        agregarCamino("Ladder Room", "Window", 2);
        agregarCamino("Ladder Room", "WP_Mid_Short", 2);
        agregarCamino("Window", "Jungle", 2);
        agregarCamino("Jungle", "WP_CT_B", 2);

        // B Site conex
        agregarCamino("Short B", "B Site", 3);
        agregarCamino("Apps", "B Site", 3);

        // CT conex
        agregarCamino("CT Spawn", "WP_CT_B", 2);
        agregarCamino("WP_CT_B", "B Site", 2);
        agregarCamino("Connector", "Jungle", 2);
    }
}
