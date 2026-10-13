import structures.GrafoMapa;

public class Main {
    public static void main(String[] args) {
        GrafoMapa mirage = new GrafoMapa();

        System.out.println("--- RUTA INICIAL (T Spawn ➔ B Site) ---");
        System.out.println(mirage.calcularRutaMasCorta("T Spawn", "B Site"));

        System.out.println("\\n--- SIMULANDO EVENTO ENEMY SPAWN ---");
        // Bloqueamos intencionalmente la ruta directa por Apps
        mirage.cambiarEstadoCamino("T Spawn", "Apps", true);
        System.out.println("X Se detectó enemigo en T Spawn ➔ Apps");

        System.out.println("\\n--- RUTA RECALCULADA AUTOMÁTICAMENTE ---");
        System.out.println(mirage.calcularRutaMasCorta("T Spawn", "B Site"));
    }
}