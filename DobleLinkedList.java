public class DobleLinkedList{
    public static void main (String[] args){
        LinkedList miLista = new LinkedList();
        miLista.insertarAlPrincipio(10);
        miLista.insertarAlFiinal(20);
        miLista.insertarAlFiinal(30);
        miLista.imprimirLista();
    }
}

class Nodo{
    int dato;
    Nodo siguiente;

    public Nodo(int datoInicial){
        this.dato = datoInicial;
        this.siguiente = null;
    }
}

class LinkedList{
    
    Nodo cabeza;

    public LinkedList(){
        this.cabeza = null;
    }

    public void insertarAlPrincipio(int datoNuevo){
        Nodo nuevoNodo = new Nodo(datoNuevo);
        nuevoNodo.siguiente = this.cabeza;
        this.cabeza = nuevoNodo;
    }

    public void insertarAlFiinal(int datoNuevo){
        Nodo nuevoNodo = new Nodo(datoNuevo);

        if (this.cabeza == null){
            this.cabeza = nuevoNodo;
            return;
        }
        
        Nodo temp = this.cabeza;

        while (temp.siguiente != null) { 
            temp = temp.siguiente;
        }

        temp.siguiente = nuevoNodo;
    }
    public void imprimirLista(){
        
        Nodo temp = this.cabeza;
        
        while (temp != null) {
            System.out.print(temp.dato + " -> ");
            temp = temp.siguiente;
        }

        System.out.print("null");
    }
}
