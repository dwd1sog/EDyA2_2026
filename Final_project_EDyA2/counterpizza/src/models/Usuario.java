package models;

public class Usuario {
    public String nombre;
    private String numeroContacto;
    private String nodoUbicacion; // Ej: "T Spawn", "Bombsite A", "Palace" (Posición en el mapa Mirage)

    //Constructor
    public Usuario(String nombre, String numeroContacto, String nodoUbicacion) {
        this.nombre = nombre;
        this.numeroContacto = numeroContacto;
        this.nodoUbicacion = nodoUbicacion;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroContacto() {
        return numeroContacto;
    }
    public void setNumeroContacto(String numeroContacto) {
        this.numeroContacto = numeroContacto;
    }

    public String getNodoUbicacion() {
        return nodoUbicacion;
    }
    public void setNodoUbicacion(String nodoUbicacion) {
        this.nodoUbicacion = nodoUbicacion;
    }

    @Override
    public String toString() {
        return "Usuario: " + nombre + " | Teléfono: " + numeroContacto + " | Ubicación en Mapa: " + nodoUbicacion;
    }
}