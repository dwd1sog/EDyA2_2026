package models;

public class Pedido {
    private static int contadorId = 1;
    private int idPedido;
    private Usuario usuario;
    private Pizza pizza;
    private String estado; // "En espera", "En preparacion", "En camino", "Entregado"

    public Pedido(Usuario usuario, Pizza pizza) {
        this.idPedido = contadorId++;
        this.usuario = usuario;
        this.pizza = pizza;
        this.estado = "En espera";
    }

    public int getIdPedido() {
        return idPedido;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public Pizza getPizza() {
        return pizza;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override public String toString() {
        return "Pedido #" + idPedido +
                " | Cliente: " + usuario.getNombre() +
                " | Destino (Nodo): " + usuario.getNodoUbicacion() +
                " | Pizza: " + pizza.getSabor() + " (" + pizza.getSize() + ")" +
                " | Total: $" + pizza.getPrecio() +
                " | Estado: " + estado;
    }
}
