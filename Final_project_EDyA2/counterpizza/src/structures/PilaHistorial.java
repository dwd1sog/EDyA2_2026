package structures;

import models.AccionSeleccion;

public class PilaHistorial {

    private static class NodoAccion {
        AccionSeleccion accion;
        NodoAccion siguiente;

        public NodoAccion(AccionSeleccion accion) {
            this.accion = accion;
            this.siguiente = null;
        }
    }

    private NodoAccion cima;
    private int tamano;

    public PilaHistorial() {
        this.cima = null;
        this.tamano = 0;
    }

    public void apilar(AccionSeleccion accion) {
        NodoAccion nuevo = new NodoAccion(accion);
        nuevo.siguiente = cima;
        cima = nuevo;
        tamano++;
        System.out.println("-> Accion apilada en historial: " + accion);
    }

    public AccionSeleccion desapilar() {
        if (estaVacia()) {
            System.out.println("No hay mas acciones por deshacer en el historial.");
            return null;
        }
        AccionSeleccion accionDeshecha = cima.accion;
        cima = cima.siguiente;
        tamano--;
        System.out.println("<- Deshaciendo ultima accion: " + accionDeshecha);
        return accionDeshecha;
    }

    public AccionSeleccion verCima() {
        if (estaVacia()) {
            return null;
        }
        return cima.accion;
    }

    public boolean estaVacia() {
        return cima == null;
    }

    public int getTamano() {
        return tamano;
    }

    public void limpiar() {
        this.cima = null;
        this.tamano = 0;
    }

    public void mostrarHistorial() {
        if (estaVacia()) {
            System.out.println("El historial de elecciones esta vacio.");
            return;
        }

        System.out.println("\n--- HISTORIAL DE ELECCIONES EN LA INTERFAZ (Cima -> Base) ---");
        NodoAccion actual = cima;
        int nivel = tamano;
        while (actual != null) {
            String marca = (actual == cima) ? " [CIMA/ULTIMA ACCION] " : "                        ";
            System.out.println(marca + "Nivel " + nivel + ": " + actual.accion);
            actual = actual.siguiente;
            nivel--;
        }
        System.out.println("-----------------------------------------------------------\n");
    }
}
