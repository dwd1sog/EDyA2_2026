import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graphs {

    public static void main(String[] args) {
        System.out.println("GRAFO DE PERSONAS Y CIUDADES");

        GrafoRedSocial grafo = new GrafoRedSocial();

        // 1. Agregar Ciudades
        grafo.agregarCiudad("Bogota");
        grafo.agregarCiudad("Medellin");
        grafo.agregarCiudad("Cali");
        grafo.agregarCiudad("Cartagena");

        // 2. Agregar Personas y asociarlas a sus ciudades de residencia
        Persona p1 = new Persona("Carlos Gomez", 28);
        Persona p2 = new Persona("Ana Martinez", 32);
        Persona p3 = new Persona("Luis Fernandez", 22);
        Persona p4 = new Persona("Sofia Lopez", 25);
        Persona p5 = new Persona("David Bocanegra", 24);

        grafo.agregarPersona(p1, "Bogota");
        grafo.agregarPersona(p2, "Medellin");
        grafo.agregarPersona(p3, "Bogota");
        grafo.agregarPersona(p4, "Cali");
        grafo.agregarPersona(p5, "Bogota");

        // 3. Conectar Amistades entre personas
        grafo.agregarAmistad(p1.getNombre(), p3.getNombre());
        grafo.agregarAmistad(p1.getNombre(), p5.getNombre());
        grafo.agregarAmistad(p2.getNombre(), p4.getNombre());

        // 4. Imprimir la estructura completa del Grafo
        System.out.println("\nEstructura General del Grafo");
        grafo.imprimirGrafo();

        // 5. Consultar personas por ciudad
        System.out.println("\nPersonas que viven en Bogota");
        grafo.imprimirPersonasPorCiudad("Bogota");

        System.out.println("\nPersonas que viven en Medellin");
        grafo.imprimirPersonasPorCiudad("Medellin");

        System.out.println("\nPersonas que viven en Cartagena");
        grafo.imprimirPersonasPorCiudad("Cartagena");
    }
}

// 1. CLASE PERSONA
class Persona {
    private String nombre;
    private int edad;

    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }

    @Override
    public String toString() {
        return nombre + " (Edad: " + edad + ")";
    }
}

// 2. CLASE CIUDAD
class Ciudad {
    private String nombre;

    public Ciudad(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return "Ciudad: " + nombre;
    }
}

// 3. GRAFO CON LISTA DE ADYACENCIA
class GrafoRedSocial {
    private Map<String, List<String>> listaAdyacencia;
    private Map<String, Persona> mapaPersonas;
    private Map<String, Ciudad> mapaCiudades;
    private Map<String, List<Persona>> personasPorCiudad;

    public GrafoRedSocial() {
        this.listaAdyacencia = new HashMap<>();
        this.mapaPersonas = new HashMap<>();
        this.mapaCiudades = new HashMap<>();
        this.personasPorCiudad = new HashMap<>();
    }

    public void agregarCiudad(String nombreCiudad) {
        mapaCiudades.putIfAbsent(nombreCiudad, new Ciudad(nombreCiudad));
        listaAdyacencia.putIfAbsent(nombreCiudad, new ArrayList<>());
        personasPorCiudad.putIfAbsent(nombreCiudad, new ArrayList<>());
    }

    public void agregarPersona(Persona persona, String nombreCiudad) {
        if (!mapaCiudades.containsKey(nombreCiudad)) {
            agregarCiudad(nombreCiudad);
        }

        mapaPersonas.put(persona.getNombre(), persona);
        listaAdyacencia.putIfAbsent(persona.getNombre(), new ArrayList<>());
        
        // Conectar la persona con la ciudad
        listaAdyacencia.get(persona.getNombre()).add(nombreCiudad);
        listaAdyacencia.get(nombreCiudad).add(persona.getNombre());
        
        // Registrar en la lista de habitantes
        personasPorCiudad.get(nombreCiudad).add(persona);
    }

    public void agregarAmistad(String nombrePersona1, String nombrePersona2) {
        if (mapaPersonas.containsKey(nombrePersona1) && mapaPersonas.containsKey(nombrePersona2)) {
            listaAdyacencia.get(nombrePersona1).add(nombrePersona2);
            listaAdyacencia.get(nombrePersona2).add(nombrePersona1);
        }
    }

    public void imprimirPersonasPorCiudad(String nombreCiudad) {
        List<Persona> habitantes = personasPorCiudad.get(nombreCiudad);
        if (habitantes == null || habitantes.isEmpty()) {
            System.out.println("No hay personas registradas viviendo en " + nombreCiudad + ".");
            return;
        }

        System.out.println("Habitantes de " + nombreCiudad + ":");
        for (Persona p : habitantes) {
            System.out.println("  * " + p.toString());
        }
    }

    public void imprimirGrafo() {
        for (Map.Entry<String, List<String>> entry : listaAdyacencia.entrySet()) {
            System.out.println("Nodo [" + entry.getKey() + "] conectado con: " + entry.getValue());
        }
    }
}
