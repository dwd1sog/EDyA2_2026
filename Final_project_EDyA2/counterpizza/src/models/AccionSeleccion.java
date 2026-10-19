package models;

public class AccionSeleccion {

    public enum TipoAccion {
        SELECCIONAR_CATEGORIA,
        SELECCIONAR_SABOR,
        SELECCIONAR_TAMANO,
        AGREGAR_ADICIONAL
    }

    private TipoAccion tipo;
    private String valor;

    public AccionSeleccion(TipoAccion tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public TipoAccion getTipo() { return tipo; }
    public String getValor() { return valor; }

    @Override
    public String toString() {
        return "Accion: " + tipo + " -> " + valor;
    }
}
