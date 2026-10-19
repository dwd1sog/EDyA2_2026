package structures;

import java.util.*;

public class ArbolTrie {

    private static class NodoTrie {
        private Map<Character, NodoTrie> hijos;
        private boolean esFinDePalabra;

        public NodoTrie() {
            this.hijos = new HashMap<>();
            this.esFinDePalabra = false;
        }
    }

    private final NodoTrie raiz;

    public ArbolTrie() {
        this.raiz = new NodoTrie();
    }

    public void insertar(String palabra) {
        if (palabra == null || palabra.trim().isEmpty()) {
            return;
        }
        NodoTrie actual = raiz;
        String terminoLimpio = palabra.trim().toLowerCase();

        for (char c : terminoLimpio.toCharArray()) {
            actual.hijos.putIfAbsent(c, new NodoTrie());
            actual = actual.hijos.get(c);
        }
        actual.esFinDePalabra = true;
    }

    public boolean buscar(String palabra) {
        if (palabra == null || palabra.trim().isEmpty()) {
            return false;
        }
        NodoTrie actual = raiz;
        String terminoLimpio = palabra.trim().toLowerCase();

        for (char c : terminoLimpio.toCharArray()) {
            NodoTrie nodo = actual.hijos.get(c);
            if (nodo == null) {
                return false;
            }
            actual = nodo;
        }
        return actual.esFinDePalabra;
    }

    public List<String> obtenerSugerenciasPorPrefijo(String prefijo) {
        List<String> sugerencias = new ArrayList<>();
        if (prefijo == null) {
            return sugerencias;
        }

        NodoTrie actual = raiz;
        String prefijoLimpio = prefijo.trim().toLowerCase();

        for (char c : prefijoLimpio.toCharArray()) {
            NodoTrie nodo = actual.hijos.get(c);
            if (nodo == null) {
                return sugerencias;
            }
            actual = nodo;
        }

        recolectarPalabras(actual, new StringBuilder(prefijoLimpio), sugerencias);
        return sugerencias;
    }

    private void recolectarPalabras(NodoTrie nodo, StringBuilder prefijoActual, List<String> resultados) {
        if (nodo.esFinDePalabra) {
            String palabraFormateada = capitalizarPalabra(prefijoActual.toString());
            resultados.add(palabraFormateada);
        }

        for (Map.Entry<Character, NodoTrie> entrada : nodo.hijos.entrySet()) {
            prefijoActual.append(entrada.getKey());
            recolectarPalabras(entrada.getValue(), prefijoActual, resultados);
            prefijoActual.deleteCharAt(prefijoActual.length() - 1);
        }
    }

    private String capitalizarPalabra(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
}
