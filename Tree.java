import java.util.ArrayList;
import java.util.List;

public class Tree {

    public static void main(String[] args) {
        ArbolBinario arbol = new ArbolBinario();

        System.out.println("ARBOL BINARIO DE BUSQUEDA");
        System.out.println("Insertando numeros en el arbol...");
        
        // Insertando valores de prueba
        int[] numeros = {25, 15, 50, 10, 22, 35, 70, 12, 18, 44, 66, 90};
        for (int num : numeros) {
            arbol.insertar(num);
        }

        System.out.println("\n Recorrido InOrder (L - N - R)");
        arbol.inOrder();

        System.out.println("\nRecorrido PreOrder (N - L - R)");
        arbol.preOrder();

        System.out.println("\n Recorrido PostOrder (L - R - N)");
        arbol.postOrder();

        System.out.println("\nBusqueda de Valores (contains)");
        int[] valoresABuscar = {22, 35, 100, 12, 5};
        for (int v : valoresABuscar) {
            boolean existe = arbol.contiene(v);
            System.out.println("El valor " + v + " existe en el arbol?: " + existe);
        }
    }
}

// 1. NODO BINARIO
class NodoBinario {
    int valor;
    NodoBinario izquierda;
    NodoBinario derecha;

    public NodoBinario(int valor) {
        this.valor = valor;
        this.izquierda = null;
        this.derecha = null;
    }

    public boolean esHoja() {
        return this.izquierda == null && this.derecha == null;
    }
}

// 2. CLASE ARBOL BINARIO
class ArbolBinario {
    private NodoBinario raiz;

    public ArbolBinario() {
        this.raiz = null;
    }

    // Insertar un nuevo valor en el Arbol Binario de Busqueda (BST)
    public void insertar(int valor) {
        NodoBinario nuevo = new NodoBinario(valor);
        if (this.raiz == null) {
            this.raiz = nuevo;
            return;
        }

        NodoBinario actual = this.raiz;
        while (true) {
            if (valor < actual.valor) {
                if (actual.izquierda == null) {
                    actual.izquierda = nuevo;
                    return;
                }
                actual = actual.izquierda;
            } else if (valor > actual.valor) {
                if (actual.derecha == null) {
                    actual.derecha = nuevo;
                    return;
                }
                actual = actual.derecha;
            } else {
                // El valor ya existe, se omite duplicado
                return;
            }
        }
    }

    // Metodo de busqueda
    public boolean contiene(int valor) {
        NodoBinario actual = this.raiz;
        while (actual != null) {
            if (valor == actual.valor) {
                return true;
            } else if (valor < actual.valor) {
                actual = actual.izquierda;
            } else {
                actual = actual.derecha;
            }
        }
        return false;
    }

    // Recorrido InOrder: Izquierda -> Nodo -> Derecha
    public void inOrder() {
        inOrderRec(this.raiz);
        System.out.println();
    }

    private void inOrderRec(NodoBinario nodo) {
        if (nodo != null) {
            inOrderRec(nodo.izquierda);
            System.out.print(nodo.valor + " ");
            inOrderRec(nodo.derecha);
        }
    }

    // Recorrido PreOrder: Nodo -> Izquierda -> Derecha
    public void preOrder() {
        preOrderRec(this.raiz);
        System.out.println();
    }

    private void preOrderRec(NodoBinario nodo) {
        if (nodo != null) {
            System.out.print(nodo.valor + " ");
            preOrderRec(nodo.izquierda);
            preOrderRec(nodo.derecha);
        }
    }

    // Recorrido PostOrder: Izquierda -> Derecha -> Nodo
    public void postOrder() {
        postOrderRec(this.raiz);
        System.out.println();
    }

    private void postOrderRec(NodoBinario nodo) {
        if (nodo != null) {
            postOrderRec(nodo.izquierda);
            postOrderRec(nodo.derecha);
            System.out.print(nodo.valor + " ");
        }
    }
}
