public class CircularLinkedList {
    public static void main(String[] args) {
        CircularList miLista = new CircularList();

        System.out.println("1. Agregando canciones a la lista circular...");
        miLista.insertarAlPrincipio("House Of Mirrors - Softcult");
        miLista.insertarAlFinal("Gaslight - Softcult");
        miLista.insertarAlFinal("Bird Song - Softcult");
        miLista.insertarAlFinal("Shortest Fuse - Softcult");
        miLista.insertarAlFinal("Drain - Softcult");

        System.out.println("\n2. Imprimiendo recorrido de un ciclo");
        miLista.imprimirLista();

        System.out.println("\n3. Demostrando circularidad (2 vueltas en bucle)");
        miLista.imprimirVueltas(2);

        System.out.println("\n4. Tamaño y Búsqueda");
        System.out.println("Total de canciones: " + miLista.obtenerTamano());
        System.out.println("¿Existe 'Bird Song - Softcult'?: " + miLista.buscar("Bird Song - Softcult"));

        System.out.println("\n5. Eliminando canciones");
        System.out.println("Eliminando 'House Of Mirrors - Softcult'...");
        miLista.eliminar("House Of Mirrors - Softcult");
        miLista.imprimirLista();

        System.out.println("\nEliminando 'Drain - Softcult'...");
        miLista.eliminar("Drain - Softcult");
        miLista.imprimirLista();
    }
}

class Nodo {
    String dato;
    Nodo siguiente;

    public Nodo(String datoInicial) {
        this.dato = datoInicial;
        this.siguiente = null;
    }
}

class CircularList {
    Nodo cabeza;
    Nodo cola;

    public CircularList() {
        this.cabeza = null;
        this.cola = null;
    }

    public void insertarAlPrincipio(String datoNuevo) {
        Nodo nuevoNodo = new Nodo(datoNuevo);

        if (this.cabeza == null) {
            this.cabeza = nuevoNodo;
            this.cola = nuevoNodo;
            nuevoNodo.siguiente = this.cabeza;
            return;
        }

        nuevoNodo.siguiente = this.cabeza;
        this.cabeza = nuevoNodo;
        this.cola.siguiente = this.cabeza;
    }

    public void insertarAlFinal(String datoNuevo) {
        Nodo nuevoNodo = new Nodo(datoNuevo);

        if (this.cabeza == null) {
            this.cabeza = nuevoNodo;
            this.cola = nuevoNodo;
            nuevoNodo.siguiente = this.cabeza;
            return;
        }

        this.cola.siguiente = nuevoNodo;
        this.cola = nuevoNodo;
        this.cola.siguiente = this.cabeza;
    }

    public void imprimirLista() {
        if (this.cabeza == null) {
            System.out.println("La lista está vacía.");
            return;
        }

        Nodo temp = this.cabeza;
        System.out.println("Lista Circular de canciones:");

        do {
            System.out.println(" -> " + temp.dato);
            temp = temp.siguiente;
        } while (temp != this.cabeza);

    }

    public void imprimirVueltas(int numeroDeVueltas) {
        if (this.cabeza == null) {
            System.out.println("La lista está vacía.");
            return;
        }

        Nodo temp = this.cabeza;
        int tamano = obtenerTamano();
        int totalPasos = tamano * numeroDeVueltas;

        System.out.println("Reproduciendo " + numeroDeVueltas + " vueltas continuas:");

        for (int i = 0; i < totalPasos; i++) {
            System.out.println(" [Paso " + (i + 1) + "]: " + temp.dato);
            temp = temp.siguiente;
        }
    }

    public int obtenerTamano() {
        if (this.cabeza == null) {
            return 0;
        }

        int contador = 0;
        Nodo temp = this.cabeza;

        do {
            contador++;
            temp = temp.siguiente;
        } while (temp != this.cabeza);

        return contador;
    }

    public boolean buscar(String datoBuscado) {
        if (this.cabeza == null) {
            return false;
        }

        Nodo temp = this.cabeza;

        do {
            if (temp.dato.equals(datoBuscado)) {
                return true;
            }
            temp = temp.siguiente;
        } while (temp != this.cabeza);

        return false;
    }

    public boolean eliminar(String datoAEliminar) {
        if (this.cabeza == null) {
            return false;
        }

        Nodo temp = this.cabeza;
        Nodo anterior = this.cola;

        do {
            if (temp.dato.equals(datoAEliminar)) {

                if (this.cabeza == this.cola) {
                    this.cabeza = null;
                    this.cola = null;
                    return true;
                }

                if (temp == this.cabeza) {
                    this.cabeza = this.cabeza.siguiente;
                    this.cola.siguiente = this.cabeza;
                }
                else if (temp == this.cola) {
                    this.cola = anterior;
                    this.cola.siguiente = this.cabeza;
                }
                else {
                    anterior.siguiente = temp.siguiente;
                }

                return true;
            }

            anterior = temp;
            temp = temp.siguiente;
        } while (temp != this.cabeza);

        return false;
    }
}