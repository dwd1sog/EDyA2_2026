package gui;

import structures.GrafoMapa;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.List;

public class PanelMapaMirage extends JPanel {

    public static class PuntoRelativo {
        public double rx;
        public double ry;

        public PuntoRelativo(double rx, double ry) {
            this.rx = rx;
            this.ry = ry;
        }

        public Point aPixel(int ancho, int alto) {
            return new Point((int) Math.round(rx * ancho), (int) Math.round(ry * alto));
        }
    }

    private GrafoMapa grafo;
    private List<String> rutaActual;
    private Map<String, PuntoRelativo> posicionesRelativas;
    private BufferedImage imagenFondo;

    public PanelMapaMirage(GrafoMapa grafo) {
        this.grafo = grafo;
        this.rutaActual = new ArrayList<>();
        this.posicionesRelativas = new HashMap<>();
        this.setBackground(new Color(25, 28, 32));

        cargarImagenFondo();
        inicializarCoordenadasPorcentuales();
    }

    private void cargarImagenFondo() {
        String[] recursos = {"/mapa.png", "mapa.png", "/mirage_11347b32ec.png", "mirage_11347b32ec.png"};
        for (String res : recursos) {
            try {
                URL imgUrl = getClass().getResource(res);
                if (imgUrl != null) {
                    imagenFondo = ImageIO.read(imgUrl);
                    System.out.println("[INFO] Mapa cargado desde recurso classpath: " + res);
                    return;
                }
                InputStream is = getClass().getClassLoader().getResourceAsStream(res.startsWith("/") ? res.substring(1) : res);
                if (is != null) {
                    imagenFondo = ImageIO.read(is);
                    System.out.println("[INFO] Mapa cargado desde ClassLoader: " + res);
                    return;
                }
            } catch (Exception ignored) {}
        }

        String[] rutasArchivos = {
                "mapa.png",
                "src/mapa.png",
                "src/gui/mapa.png",
                "src/assets/mapa.png",
                "knowledge/mapa.png"
        };

        for (String ruta : rutasArchivos) {
            try {
                File file = new File(ruta);
                if (file.exists()) {
                    imagenFondo = ImageIO.read(file);
                    if (imagenFondo != null) {
                        System.out.println("[INFO] Mapa cargado desde archivo local: " + file.getAbsolutePath());
                        return;
                    }
                }
            } catch (Exception ignored) {}
        }

        System.err.println("[ADVERTENCIA] No se pudo cargar 'mapa.png'. Verifique que sea un archivo PNG valido.");
    }

    private void inicializarCoordenadasPorcentuales() {
        // Coordenadas Porcentuales (0.0 a 1.0) calibradas exactamente sobre la madera de mapa.png
        posicionesRelativas.put("T Spawn", new PuntoRelativo(0.92, 0.38));
        posicionesRelativas.put("WP_T_Palace", new PuntoRelativo(0.87, 0.70));
        posicionesRelativas.put("Palace", new PuntoRelativo(0.73, 0.85));

        posicionesRelativas.put("WP_T_Ramp", new PuntoRelativo(0.85, 0.60));
        posicionesRelativas.put("A Ramp", new PuntoRelativo(0.73, 0.70));
        posicionesRelativas.put("A Site", new PuntoRelativo(0.55, 0.80));

        posicionesRelativas.put("WP_Conn_A", new PuntoRelativo(0.52, 0.67));
        posicionesRelativas.put("Mid", new PuntoRelativo(0.55, 0.45));
        posicionesRelativas.put("Window", new PuntoRelativo(0.38, 0.45));
        posicionesRelativas.put("Connector", new PuntoRelativo(0.49, 0.54));

        posicionesRelativas.put("WP_Mid_Short", new PuntoRelativo(0.45, 0.35));
        posicionesRelativas.put("Short B", new PuntoRelativo(0.40, 0.25));
        posicionesRelativas.put("Ladder Room", new PuntoRelativo(0.38, 0.35));

        posicionesRelativas.put("TV", new PuntoRelativo(0.78, 0.14));
        posicionesRelativas.put("Apps", new PuntoRelativo(0.35, 0.11));
        posicionesRelativas.put("B Site", new PuntoRelativo(0.18, 0.30));

        posicionesRelativas.put("Jungle", new PuntoRelativo(0.38, 0.58));
        posicionesRelativas.put("CT Spawn", new PuntoRelativo(0.20, 0.78));
        posicionesRelativas.put("WP_CT_B", new PuntoRelativo(0.29, 0.55));
    }

    public void setRutaActual(List<String> ruta) {
        this.rutaActual = (ruta != null) ? ruta : new ArrayList<>();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto = getHeight();

        // 1. Dibujar Imagen de Fondo Responsiva
        if (imagenFondo != null) {
            g2d.drawImage(imagenFondo, 0, 0, ancho, alto, this);
        } else {
            g2d.setColor(new Color(25, 28, 32));
            g2d.fillRect(0, 0, ancho, alto);
            g2d.setColor(new Color(40, 45, 55));
            for (int i = 0; i < ancho; i += 40) g2d.drawLine(i, 0, i, alto);
            for (int j = 0; j < alto; j += 40) g2d.drawLine(0, j, ancho, j);
        }

        // Convertir coordenadas relativas a pixeles del panel actual
        Map<String, Point> pixeles = new HashMap<>();
        for (Map.Entry<String, PuntoRelativo> entry : posicionesRelativas.entrySet()) {
            pixeles.put(entry.getKey(), entry.getValue().aPixel(ancho, alto));
        }

        // 2. Dibujar Conexiones Base del Grafo
        g2d.setStroke(new BasicStroke(3));

        Map<String, List<GrafoMapa.Arista>> mapaAdyacencia = grafo.getMapa();
        Set<String> conexionesDibujadas = new HashSet<>();

        for (Map.Entry<String, List<GrafoMapa.Arista>> entry : mapaAdyacencia.entrySet()) {
            String origen = entry.getKey();
            Point p1 = pixeles.get(origen);

            if (p1 == null) continue;

            for (GrafoMapa.Arista arista : entry.getValue()) {
                String destino = arista.getDestino();
                Point p2 = pixeles.get(destino);

                if (p2 == null) continue;

                String clave = origen.compareTo(destino) < 0 ? origen + "-" + destino : destino + "-" + origen;

                if (!conexionesDibujadas.contains(clave)) {
                    conexionesDibujadas.add(clave);
                    if (!arista.isBloqueado()) {
                        g2d.setColor(new Color(40, 40, 40, 180));
                        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                    }
                }
            }
        }

        // 3. Dibujar Enemigos en Pasillos Bloqueados
        List<String[]> bloqueados = grafo.getCaminosBloqueados();
        for (String[] par : bloqueados) {
            Point p1 = pixeles.get(par[0]);
            Point p2 = pixeles.get(par[1]);

            if (p1 != null && p2 != null) {
                g2d.setStroke(new BasicStroke(5));
                g2d.setColor(new Color(220, 30, 30));
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);

                int midX = (p1.x + p2.x) / 2;
                int midY = (p1.y + p2.y) / 2;

                g2d.setColor(new Color(180, 20, 20));
                g2d.fillOval(midX - 14, midY - 14, 28, 28);
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(midX - 14, midY - 14, 28, 28);

                dibujarTextoConSombra(g2d, "ENEMIGO", midX - 24, midY + 22, Color.RED, Color.BLACK, new Font("SansSerif", Font.BOLD, 11));
            }
        }

        // 4. Dibujar Ruta Optima Activa (Dijkstra)
        if (rutaActual != null && rutaActual.size() > 1) {
            g2d.setStroke(new BasicStroke(6));
            g2d.setColor(new Color(230, 175, 40));

            for (int i = 0; i < rutaActual.size() - 1; i++) {
                Point p1 = pixeles.get(rutaActual.get(i));
                Point p2 = pixeles.get(rutaActual.get(i + 1));
                if (p1 != null && p2 != null) {
                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }

        // 5. Dibujar Nodos Principales (Ocultando Waypoints WP_)
        for (Map.Entry<String, PuntoRelativo> entry : posicionesRelativas.entrySet()) {
            String nombre = entry.getKey();

            if (grafo.esPuntoDePaso(nombre)) {
                continue;
            }

            Point p = pixeles.get(nombre);
            if (p == null) continue;

            boolean esDeRuta = rutaActual != null && rutaActual.contains(nombre);
            boolean esInicio = rutaActual != null && !rutaActual.isEmpty() && rutaActual.get(0).equals(nombre);
            boolean esFin = rutaActual != null && !rutaActual.isEmpty() && rutaActual.get(rutaActual.size() - 1).equals(nombre);

            if (esInicio) {
                g2d.setColor(new Color(40, 200, 60)); // Verde
            } else if (esFin) {
                g2d.setColor(new Color(230, 50, 50)); // Rojo
            } else if (esDeRuta) {
                g2d.setColor(new Color(230, 175, 40)); // Dorado
            } else {
                g2d.setColor(new Color(50, 60, 75));
            }

            int radio = esDeRuta ? 24 : 18;
            g2d.fillOval(p.x - radio / 2, p.y - radio / 2, radio, radio);
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(p.x - radio / 2, p.y - radio / 2, radio, radio);

            // Nombre con sombra
            dibujarTextoConSombra(g2d, nombre, p.x - 24, p.y - 14, Color.WHITE, Color.BLACK, new Font("SansSerif", Font.BOLD, 12));
        }
    }

    private void dibujarTextoConSombra(Graphics2D g2d, String texto, int x, int y, Color colorTexto, Color colorSombra, Font font) {
        g2d.setFont(font);
        g2d.setColor(colorSombra);
        g2d.drawString(texto, x + 1, y + 1);
        g2d.drawString(texto, x - 1, y - 1);
        g2d.drawString(texto, x + 1, y - 1);
        g2d.drawString(texto, x - 1, y + 1);

        g2d.setColor(colorTexto);
        g2d.drawString(texto, x, y);
    }
}
