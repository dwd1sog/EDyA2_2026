public class DobleLinkedList{
    public static void main (String[] args){
        List miLista = new List();
        miLista.insertarAlPrincipio("House Of Mirrors - Softcult");
        miLista.insertarAlFiinal("Gaslight - Softcult");
        miLista.insertarAlFiinal("Bird Song - Softcult");
        miLista.insertarAlFiinal("Shortest Fuse - Softcult");
        miLista.insertarAlFiinal("Drain - Softcult");
        miLista.imprimirLista();
    }
}

class Nodo{
    String dato;
    Nodo siguiente;

    public Nodo(String datoInicial){
        this.dato = datoInicial;
        this.siguiente = null;
    }
}

class List{
    
    Nodo cabeza;

    public List(){
        this.cabeza = null;
    }

    public void insertarAlPrincipio(String datoNuevo){
        Nodo nuevoNodo = new Nodo(datoNuevo);
        nuevoNodo.siguiente = this.cabeza;
        this.cabeza = nuevoNodo;
    }

    public void insertarAlFiinal(String datoNuevo){
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
        System.out.println("Lista de canciones agregadas:\n");
        
        while (temp != null) {
            System.out.println(temp.dato);
            temp = temp.siguiente;
        }

        System.out.print("\nNo existen mas canciones.");
    }
}
