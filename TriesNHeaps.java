import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class TriesNHeaps {

    public static void main(String[] args) {
        System.out.println("MOTOR DE BUSQUEDA INTELIGENTE (TRIE + HEAP)");

        MotorBusquedaInteligente motor = new MotorBusquedaInteligente();

        // Insertar productos con sus puntajes de popularidad
        System.out.println("Insertando productos en el catalogo...");
        motor.insertarProducto("air max", 90);
        motor.insertarProducto("air force", 95);
        motor.insertarProducto("air jordan", 85);
        motor.insertarProducto("air zoom", 88);
        motor.insertarProducto("adidas boost", 80);
        motor.insertarProducto("adidas ultraboost", 92);
        motor.insertarProducto("puma suede", 75);

        // Pruebas de Búsqueda de los K más populares por prefijo
        System.out.println("\nBuscando Top 2 para prefijo 'air'");
        List<Producto> topAir2 = motor.buscarTopK("air", 2);
        for (Producto p : topAir2) {
            System.out.println("  * " + p.toString());
        }

        System.out.println("\nBuscando Top 3 para prefijo 'air'");
        List<Producto> topAir3 = motor.buscarTopK("air", 3);
        for (Producto p : topAir3) {
            System.out.println("  * " + p.toString());
        }

        System.out.println("\nBuscando Top 2 para prefijo 'adi'");
        List<Producto> topAdi = motor.buscarTopK("adi", 2);
        for (Producto p : topAdi) {
            System.out.println("  * " + p.toString());
        }

        System.out.println("\nBuscando Top 5 para prefijo 'x' (sin coincidencias)");
        List<Producto> topX = motor.buscarTopK("x", 5);
        if (topX.isEmpty()) {
            System.out.println("No se encontraron productos con el prefijo 'x'.");
        }
    }
}

// 1. CLASE PRODUCTO
class Producto {
    private String nombre;
    private int popularidad;

    public Producto(String nombre, int popularidad) {
        this.nombre = nombre;
        this.popularidad = popularidad;
    }

    public String getNombre() { return nombre; }
    public int getPopularidad() { return popularidad; }

    @Override
    public String toString() {
        return nombre + " (Popularidad: " + popularidad + ")";
    }
}

// 2. NODO DEL ARBOL TRIE
class NodoTrie {
    private Map<Character, NodoTrie> hijos;
    private boolean esFinDePalabra;
    private List<Producto> productosCoincidentes;

    public NodoTrie() {
        this.hijos = new HashMap<>();
        this.esFinDePalabra = false;
        this.productosCoincidentes = new ArrayList<>();
    }

    public Map<Character, NodoTrie> getHijos() { return hijos; }
    public boolean isEsFinDePalabra() { return esFinDePalabra; }
    public void setEsFinDePalabra(boolean esFin) { this.esFinDePalabra = esFin; }
    public List<Producto> getProductosCoincidentes() { return productosCoincidentes; }
}

// 3. MOTOR DE BUSQUEDA INTELIGENTE
class MotorBusquedaInteligente {
    private NodoTrie raiz;

    public MotorBusquedaInteligente() {
        this.raiz = new NodoTrie();
    }

    // Insertar un producto en el Trie
    public void insertarProducto(String nombre, int popularidad) {
        Producto producto = new Producto(nombre, popularidad);
        NodoTrie actual = this.raiz;

        String texto = nombre.toLowerCase().trim();
        for (char c : texto.toCharArray()) {
            actual.getHijos().putIfAbsent(c, new NodoTrie());
            actual = actual.getHijos().get(c);
        }
        actual.setEsFinDePalabra(true);
        actual.getProductosCoincidentes().add(producto);
    }

    // Buscar todos los productos que coinciden con un prefijo
    private List<Producto> obtenerTodosPorPrefijo(String prefijo) {
        List<Producto> resultados = new ArrayList<>();
        NodoTrie actual = this.raiz;

        String prefix = prefijo.toLowerCase().trim();
        for (char c : prefix.toCharArray()) {
            if (!actual.getHijos().containsKey(c)) {
                return resultados; // Prefijo no encontrado
            }
            actual = actual.getHijos().get(c);
        }

        // Recolectar todos los productos en el subárbol a partir del prefijo
        recolectarProductos(actual, resultados);
        return resultados;
    }

    private void recolectarProductos(NodoTrie nodo, List<Producto> resultados) {
        if (nodo == null) return;

        if (nodo.isEsFinDePalabra()) {
            resultados.addAll(nodo.getProductosCoincidentes());
        }

        for (NodoTrie hijo : nodo.getHijos().values()) {
            recolectarProductos(hijo, resultados);
        }
    }

    // Buscar los Top K productos más populares usando un MinHeap
    public List<Producto> buscarTopK(String prefijo, int k) {
        List<Producto> candidatos = obtenerTodosPorPrefijo(prefijo);
        if (candidatos.isEmpty()) {
            return new ArrayList<>();
        }

        // Usar un MinHeap de tamaño K ordenado por popularidad
        PriorityQueue<Producto> minHeap = new PriorityQueue<>(Comparator.comparingInt(Producto::getPopularidad));

        for (Producto p : candidatos) {
            minHeap.add(p);
            if (minHeap.size() > k) {
                minHeap.poll(); // Elimina el de menor popularidad para conservar solo los K mas altos
            }
        }

        // Extraer los elementos del MinHeap y ordenarlos de mayor a menor popularidad
        List<Producto> topK = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            topK.add(0, minHeap.poll()); // Insertar al inicio para invertir el orden del MinHeap
        }

        return topK;
    }
}
