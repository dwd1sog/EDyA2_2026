public class LinkedList{
    public static void main (String[] args){
        List miLista = new List();
        
        System.out.println("1. Agregando canciones:");
        miLista.insertarAlPrincipio("House Of Mirrors - Softcult");
        miLista.insertarAlFinal("Gaslight - Softcult");
        miLista.insertarAlFinal("Bird Song - Softcult");
        miLista.insertarAlFinal("Shortest Fuse - Softcult");
        miLista.insertarAlFinal("Drain - Softcult");
        
        miLista.imprimirLista();
        
        System.out.println("\n2. Tamaño de la lista");
        System.out.println("Total de canciones: " + miLista.obtenerTamano());

        System.out.println("\n3. Buscando canción...");
        System.out.println("Existe 'Bird Song - Softcult'?: " + miLista.buscar("Bird Song - Softcult"));
        System.out.println("Existe 'Spoiled - Softcult'?: " + miLista.buscar("Spoiled - Softcult"));

        System.out.println("\n4. Eliminando canciones");
        System.out.println("Eliminando 'House Of Mirrors - Softcult'...");
        miLista.eliminar("House Of Mirrors - Softcult");
        miLista.imprimirLista();

        System.out.println("\nEliminando 'Shortest Fuse - Softcult'...");
        miLista.eliminar("Shortest Fuse - Softcult");
        miLista.imprimirLista();

        System.out.println("\nTamaño final: " + miLista.obtenerTamano());
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

class List {
    Nodo cabeza;

    public List() {
        this.cabeza = null;
    }

    public void insertarAlPrincipio(String datoNuevo) {
        Nodo nuevoNodo = new Nodo(datoNuevo);
        nuevoNodo.siguiente = this.cabeza;
        this.cabeza = nuevoNodo;
    }

    public void insertarAlFinal(String datoNuevo) {
        Nodo nuevoNodo = new Nodo(datoNuevo);

        if (this.cabeza == null) {
            this.cabeza = nuevoNodo;
            return;
        }

        Nodo temp = this.cabeza;
        while (temp.siguiente != null) {
            temp = temp.siguiente;
        }

        temp.siguiente = nuevoNodo;
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

        if (this.cabeza.dato.equals(datoAEliminar)) {
            this.cabeza = this.cabeza.siguiente;
            return true;
        }

        Nodo temp = this.cabeza;
        while (temp.siguiente != null) {
            if (temp.siguiente.dato.equals(datoAEliminar)) {
                temp.siguiente = temp.siguiente.siguiente;
                return true;
            }
            temp = temp.siguiente;
        }

        return false;
    }

    public void imprimirLista() {
        Nodo temp = this.cabeza;
        System.out.println("Lista de canciones agregadas:");

        while (temp != null) {
            System.out.println(" -> " + temp.dato);
            temp = temp.siguiente;
        }

        System.out.println("No existen mas canciones.");
    }
}