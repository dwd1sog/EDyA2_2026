import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class NAryTrees {

    public static void main(String[] args) {
        System.out.println("ARBOL N-ARIO (SISTEMA DE MENUS)");

        // Crear Nodo Raiz del Sitio Web
        NodoMenu raizPortal = new NodoMenu("Inicio", "/", "HomeComponent");

        // Nivel 1: Secciones principales
        NodoMenu menuProductos = new NodoMenu("Productos", "/productos", "ProductsCatalogComponent");
        NodoMenu menuServicios = new NodoMenu("Servicios", "/servicios", "ServicesComponent");
        NodoMenu menuContacto = new NodoMenu("Contacto", "/contacto", "ContactFormComponent");

        raizPortal.agregarHijo(menuProductos);
        raizPortal.agregarHijo(menuServicios);
        raizPortal.agregarHijo(menuContacto);

        // Nivel 2: Submenús de Productos
        NodoMenu subElectronica = new NodoMenu("Electronica", "/productos/electronica", "TechCategoryComponent");
        NodoMenu subRopa = new NodoMenu("Ropa y Moda", "/productos/ropa", "FashionCategoryComponent");
        menuProductos.agregarHijo(subElectronica);
        menuProductos.agregarHijo(subRopa);

        // Nivel 3: Sub-categorías de Electrónica
        subElectronica.agregarHijo(new NodoMenu("Laptops", "/productos/electronica/laptops", "LaptopsListComponent"));
        subElectronica.agregarHijo(new NodoMenu("Smartphones", "/productos/electronica/smartphones", "PhonesListComponent"));

        // Nivel 2: Submenús de Servicios
        menuServicios.agregarHijo(new NodoMenu("Consultoria", "/servicios/consultoria", "ConsultingComponent"));
        menuServicios.agregarHijo(new NodoMenu("Soporte Tecnico", "/servicios/soporte", "SupportTicketComponent"));

        ArbolNarioMenu arbolMenu = new ArbolNarioMenu(raizPortal);

        System.out.println("\nRecorrido DFS (Depth-First Search - Jerarquía del Menú)");
        arbolMenu.imprimirDFS();

        System.out.println("\nRecorrido BFS (Breadth-First Search - Por Niveles de Profundidad)");
        arbolMenu.imprimirBFS();
    }
}

// 1. NODO MENU PARA ARBOL N-ARIO
class NodoMenu {
    private String titulo;
    private String enlace;
    private String componente;
    private List<NodoMenu> hijos;

    public NodoMenu(String titulo, String enlace, String componente) {
        this.titulo = titulo;
        this.enlace = enlace;
        this.componente = componente;
        this.hijos = new ArrayList<>();
    }

    public void agregarHijo(NodoMenu hijo) {
        this.hijos.add(hijo);
    }

    public String getTitulo() { return titulo; }
    public String getEnlace() { return enlace; }
    public String getComponente() { return componente; }
    public List<NodoMenu> getHijos() { return hijos; }

    @Override
    public String toString() {
        return titulo + " [" + enlace + "] -> (" + componente + ")";
    }
}

// 2. CLASE ARBOL N-ARIO
class ArbolNarioMenu {
    private NodoMenu raiz;

    public ArbolNarioMenu(NodoMenu raiz) {
        this.raiz = raiz;
    }

    public NodoMenu getRaiz() {
        return raiz;
    }

    // Recorrido DFS (Depth-First Search - Búsqueda en Profundidad)
    public void imprimirDFS() {
        dfsRec(this.raiz, 0);
    }

    private void dfsRec(NodoMenu nodo, int nivel) {
        if (nodo == null) return;

        // Sangría visual según nivel
        StringBuilder sangria = new StringBuilder();
        for (int i = 0; i < nivel; i++) {
            sangria.append("  |--");
        }

        System.out.println(sangria.toString() + " " + nodo.getTitulo() + " (" + nodo.getEnlace() + ")");

        for (NodoMenu hijo : nodo.getHijos()) {
            dfsRec(hijo, nivel + 1);
        }
    }

    // Recorrido BFS (Breadth-First Search - Búsqueda en Anchura / Por Niveles)
    public void imprimirBFS() {
        if (this.raiz == null) return;

        Queue<NodoMenu> cola = new LinkedList<>();
        cola.add(this.raiz);

        int nivel = 0;
        while (!cola.isEmpty()) {
            int tamanoNivel = cola.size();
            System.out.println("Nivel " + nivel + ":");

            for (int i = 0; i < tamanoNivel; i++) {
                NodoMenu actual = cola.poll();
                System.out.println("  - " + actual.toString());

                for (NodoMenu hijo : actual.getHijos()) {
                    cola.add(hijo);
                }
            }
            nivel++;
        }
    }
}
