package structures;

import models.Pedido;

public class ColaPedidos {
    // Clase interna para el manejo de nodos de la cola
    private static class NodoPedido {
        Pedido pedido;
        NodoPedido siguiente;
        public NodoPedido(Pedido pedido) {
            this.pedido = pedido;
            this.siguiente = null;
        }
    }

    private NodoPedido frente;
    private NodoPedido fin;
    private int tamano;

    public ColaPedidos() {
        this.frente = null;
        this.fin = null;
        this.tamano = 0;
    }

    // Enqueue: Agregar pedido al final de la cola (FIFO)
    public void encolar(Pedido pedido) {
        NodoPedido nuevo = new NodoPedido(pedido);
        if (estaVacia()) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        } tamano++;
        System.out.println("-> Pedido #" + pedido.getIdPedido() + " ingresado a la cola de espera.");
    }

    // Dequeue: Extraer y atender el pedido mas antiguo (Frente)
    public Pedido desencolar() {
        if (estaVacia()) {
            System.out.println("La cola de pedidos esta vacia. No hay pedidos por atender.");
            return null;
        }
        Pedido pedidoAtendido = frente.pedido;
        frente = frente.siguiente;
        if (frente == null) {
            fin = null;
        }
        tamano--;
        pedidoAtendido.setEstado("En camino");
        return pedidoAtendido;
    }

    // Peek: Consultar el pedido en el frente sin retirarlo
    public Pedido verSiguiente() {
        if (estaVacia()) {
            return null;
        }
        return frente.pedido;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int getTamano() {
        return tamano;
    }

    // Imprimir el estado actual de la cola
    public void mostrarCola() {
        if (estaVacia()) {
            System.out.println("La cola de pedidos esta actualmente vacia.");
            return;
        }
        System.out.println("\n--- COLA DE PEDIDOS PENDIENTES (Frente -> Fin) ---");
        NodoPedido actual = frente;
        int posicion = 1;
        while (actual != null) {
            System.out.println(posicion + ". " + actual.pedido);
            actual = actual.siguiente;
            posicion++;
        }
        System.out.println("--------------------------------------------------\\n");
    }
}
