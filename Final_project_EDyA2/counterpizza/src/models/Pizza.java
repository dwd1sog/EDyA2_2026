package models;

import java.util.ArrayList;
import java.util.List;

public class Pizza {
    private String categoria;// "Tradicional" o "Especial"
    private String sabor; // "Hawaiana", "Pepperoni", "Pollo-champi", "Excéntrica", etc.
    private String size; // "Small", "Medium", "Large"
    private List<String> adicionales; // "Pimienta", "Oregano", "Aji"
    private double precio;

    // Constructor
    public Pizza(String categoria, String sabor, String size) {
        this.categoria = categoria;
        this.sabor = sabor;
        this.size = size;
        this.adicionales = new ArrayList();
        this.precio = calcularPrecioBase();
    }

    // Metodo adicionales
    public void agregarAdicionales(String adicionales) {
        this.adicionales.add(adicionales);
        this.precio += 0.50; // Cada adicional suma $0.50
        }

    //Metodo calcular precio
    private double calcularPrecioBase() {
        double base = categoria.equalsIgnoreCase("Especial") ? 12.0 : 9.0;
        if (size.equalsIgnoreCase("Medium")) {
            base += 3.0;
        } else if (size.equalsIgnoreCase("Large")) {
            base += 6.0;
        }
        return base;
    }

    // Getters
    public String getCategoria() {
        return categoria;
    }
    public String getSabor() {
        return sabor;
    }
    public String getSize() {
        return size;
    }
    public List<String> getAdicionales() {
        return adicionales;
    }
    public double getPrecio() {
        return precio;
    }

    @Override public String toString() {
        return "Pizza " + categoria + " (" + sabor + ") - Tamaño: " + size + " | Precio: $" + precio;
    }
}
