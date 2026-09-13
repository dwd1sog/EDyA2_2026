public class DobleLinkedList{
    public static void main (String[] args){
        Nodo nodoA = new Nodo(10);
        Nodo nodoB = new Nodo(20);
        nodoA.siguiente = nodoB;
        //System.out.println(nodoA.dato);
        //System.out.println(nodoB.dato);
        System.out.println(nodoA.siguiente.dato);
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
}
