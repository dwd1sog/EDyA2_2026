public class DoublyLinkedList {
    public static void main(String[] args) {
        DoublyList miLista = new DoublyList();

        System.out.println("1. Agregando canciones:");
        miLista.insertarAlPrincipio("House Of Mirrors - Softcult");
        miLista.insertarAlFinal("Gaslight - Softcult");
        miLista.insertarAlFinal("Bird Song - Softcult");
        miLista.insertarAlFinal("Shortest Fuse - Softcult");
        miLista.insertarAlFinal("Drain - Softcult");

        System.out.println("\n2. Imprimiendo hacia adelante");
        miLista.imprimirAdelante();

        System.out.println("\n3. Imprimiendo hacia atrás");
        miLista.imprimirAtras();

        System.out.println("\n4. Probando tamaño y búsqueda");
        System.out.println("Total de canciones: " + miLista.obtenerTamano());
        System.out.println("Existe 'Bird Song - Softcult'?: " + miLista.buscar("Bird Song - Softcult"));

        System.out.println("\n5. Eliminando canciones");
        System.out.println("Eliminando 'House Of Mirrors - Softcult'...");
        miLista.eliminar("House Of Mirrors - Softcult");
        miLista.imprimirAdelante();

        System.out.println("\nEliminando 'Shortest Fuse - Softcult'...");
        miLista.eliminar("Shortest Fuse - Softcult");
        miLista.imprimirAdelante();

        System.out.println("\nImprimiendo de nuevo hacia atrás para verificar punteros 'anterior':");
        miLista.imprimirAtras();
    }
}

class Nodo {
    String dato;
    Nodo siguiente;
    Nodo anterior;

    public Nodo(String datoInicial) {
        this.dato = datoInicial;
        this.siguiente = null;
        this.anterior = null;
    }
}

class DoublyList {
    Nodo cabeza;
    Nodo cola;

    public DoublyList() {
        this.cabeza = null;
        this.cola = null;
    }

    public void insertarAlPrincipio(String datoNuevo) {
        Nodo nuevoNodo = new Nodo(datoNuevo);

        if (this.cabeza == null) {
            this.cabeza = nuevoNodo;
            this.cola = nuevoNodo;
            return;
        }

        nuevoNodo.siguiente = this.cabeza;
        this.cabeza.anterior = nuevoNodo;
        this.cabeza = nuevoNodo;
    }

    public void insertarAlFinal(String datoNuevo) {
        Nodo nuevoNodo = new Nodo(datoNuevo);

        if (this.cabeza == null) {
            this.cabeza = nuevoNodo;
            this.cola = nuevoNodo;
            return;
        }

        this.cola.siguiente = nuevoNodo;
        nuevoNodo.anterior = this.cola;
        this.cola = nuevoNodo;
    }

    public void imprimirAdelante() {
        Nodo temp = this.cabeza;
        System.out.println("Lista de canciones (Adelante):");

        while (temp != null) {
            System.out.println(" -> " + temp.dato);
            temp = temp.siguiente;
        }
    }

    public void imprimirAtras() {
        Nodo temp = this.cola;
        System.out.println("Lista de canciones (Atrás):");

        while (temp != null) {
            System.out.println(" <- " + temp.dato);
            temp = temp.anterior;
        }
    }

    public int obtenerTamano() {
        int contador = 0;
        Nodo temp = this.cabeza;

        while (temp != null) {
            contador++;
            temp = temp.siguiente;
        }

        return contador;
    }

    public boolean buscar(String datoBuscado) {
        Nodo temp = this.cabeza;

        while (temp != null) {
            if (temp.dato.equals(datoBuscado)) {
                return true;
            }
            temp = temp.siguiente;
        }

        return false;
    }

    public boolean eliminar(String datoAEliminar) {
        if (this.cabeza == null) {
            return false;
        }

        Nodo temp = this.cabeza;

        while (temp != null) {
            if (temp.dato.equals(datoAEliminar)) {

                if (temp == this.cabeza) {
                    this.cabeza = temp.siguiente;
                    if (this.cabeza != null) {
                        this.cabeza.anterior = null;
                    } else {
                        this.cola = null;
                    }
                }
                else if (temp == this.cola) {
                    this.cola = temp.anterior;
                    this.cola.siguiente = null;
                }
                else {
                    temp.anterior.siguiente = temp.siguiente;
                    temp.siguiente.anterior = temp.anterior;
                }

                return true;
            }
            temp = temp.siguiente;
        }

        return false;
    }
}