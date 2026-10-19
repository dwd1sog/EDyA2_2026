package gui;

import models.AccionSeleccion;
import models.Pedido;
import models.Pizza;
import models.Usuario;
import persistence.GestorPersistencia;
import structures.ArbolTrie;
import structures.ColaPedidos;
import structures.GrafoMapa;
import structures.PilaHistorial;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class MainFrame extends JFrame {

    private GrafoMapa mapaMirage;
    private ColaPedidos colaPedidos;
    private PilaHistorial pilaDeshacer;
    private ArbolTrie buscadorTrie;

    private JTextField txtCliente;
    private JTextField txtContacto;
    private JComboBox<String> cbDestinoNodo;
    private JComboBox<String> cbCategoria;
    private JComboBox<String> cbSabor;
    private JComboBox<String> cbTamano;
    private JCheckBox chkOregano, chkAji, chkPimienta;
    private JTextField txtBuscadorTrie;
    private JLabel lblSugerenciasTrie;
    private JLabel lblPrecioTotal;

    private DefaultTableModel tablaModelo;
    private PanelMapaMirage panelMapa;
    private JLabel lblEstadoRuta;

    public MainFrame() {
        super("COUNTER PIZZA - SISTEMA DE DESPACHO TACTICO MIRAGE");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1150, 750);
        this.setLocationRelativeTo(null);

        inicializarEstructuras();
        construirUI();
    }

    private void inicializarEstructuras() {
        mapaMirage = new GrafoMapa();
        colaPedidos = new ColaPedidos();
        pilaDeshacer = new PilaHistorial();
        buscadorTrie = new ArbolTrie();

        buscadorTrie.insertar("Pepperoni");
        buscadorTrie.insertar("Hawaiana");
        buscadorTrie.insertar("Pollo y Champiñon");
        buscadorTrie.insertar("Excentrica");
        buscadorTrie.insertar("Pueblerina");
        buscadorTrie.insertar("De la casa");
        buscadorTrie.insertar("Oregano");
        buscadorTrie.insertar("Aji");
        buscadorTrie.insertar("Pimienta");
    }

    private void construirUI() {
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBackground(new Color(30, 33, 38));
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        container.add(crearPanelIzquierdo(), BorderLayout.WEST);
        container.add(crearPanelDerecho(), BorderLayout.CENTER);
        container.add(crearPanelInferior(), BorderLayout.SOUTH);

        this.add(container);
    }

    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(350, 0));
        panel.setBackground(new Color(40, 44, 52));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(230, 175, 40)),
                " ARMADO DE PEDIDO ", 0, 0, new Font("SansSerif", Font.BOLD, 12), new Color(230, 175, 40)));

        // Estilos de Etiquetas
        Font fontLabel = new Font("SansSerif", Font.BOLD, 11);
        Color colorLabel = Color.WHITE;

        // 1. Buscador Trie
        JLabel lblTituloTrie = new JLabel("Buscador de Menu (Trie):");
        lblTituloTrie.setForeground(colorLabel);
        lblTituloTrie.setFont(fontLabel);

        txtBuscadorTrie = new JTextField();
        txtBuscadorTrie.setBackground(new Color(25, 28, 32));
        txtBuscadorTrie.setForeground(Color.WHITE);
        txtBuscadorTrie.setCaretColor(Color.WHITE);

        lblSugerenciasTrie = new JLabel("Buscador Trie: Escribe para buscar...");
        lblSugerenciasTrie.setForeground(new Color(230, 175, 40));
        lblSugerenciasTrie.setFont(new Font("SansSerif", Font.ITALIC, 10));

        txtBuscadorTrie.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = txtBuscadorTrie.getText().trim();
                List<String> sugerencias = buscadorTrie.obtenerSugerenciasPorPrefijo(texto);

                if (texto.isEmpty() || sugerencias.isEmpty()) {
                    lblSugerenciasTrie.setText("Buscador Trie: Sin resultados");
                } else {
                    lblSugerenciasTrie.setText("Sugerencias: " + String.join(", ", sugerencias));

                    String primera = sugerencias.get(0);
                    for (int i = 0; i < cbSabor.getItemCount(); i++) {
                        if (cbSabor.getItemAt(i).equalsIgnoreCase(primera)) {
                            cbSabor.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });

        // 2. Opciones de Pizza
        cbCategoria = new JComboBox<>(new String[]{"Tradicional", "Especial"});
        cbSabor = new JComboBox<>(new String[]{"Pepperoni", "Hawaiana", "Pollo y Champiñon", "Excentrica", "Pueblerina", "De la casa"});
        cbTamano = new JComboBox<>(new String[]{"Small", "Medium", "Large"});

        chkOregano = new JCheckBox("Oregano (+$0.50)");
        chkAji = new JCheckBox("Aji (+$0.50)");
        chkPimienta = new JCheckBox("Pimienta (+$0.50)");

        estilarCheckBox(chkOregano);
        estilarCheckBox(chkAji);
        estilarCheckBox(chkPimienta);

        // 3. Datos del Cliente
        txtCliente = new JTextField("S1mple");
        txtCliente.setBackground(new Color(25, 28, 32));
        txtCliente.setForeground(Color.WHITE);
        txtCliente.setCaretColor(Color.WHITE);

        txtContacto = new JTextField("3001234567");
        txtContacto.setBackground(new Color(25, 28, 32));
        txtContacto.setForeground(Color.WHITE);
        txtContacto.setCaretColor(Color.WHITE);

        // 4. Ubicacion en Mirage
        String[] nodos = {"A Site", "B Site", "Mid", "Palace", "Apps", "Underpass", "Jungle", "CT Spawn"};
        cbDestinoNodo = new JComboBox<>(nodos);

        // Registros en Pila y Actualizacion de Precio Dinamico
        cbCategoria.addActionListener(e -> {
            pilaDeshacer.apilar(new AccionSeleccion(AccionSeleccion.TipoAccion.SELECCIONAR_CATEGORIA, (String) cbCategoria.getSelectedItem()));
            actualizarPrecio();
        });

        cbSabor.addActionListener(e -> {
            pilaDeshacer.apilar(new AccionSeleccion(AccionSeleccion.TipoAccion.SELECCIONAR_SABOR, (String) cbSabor.getSelectedItem()));
            actualizarPrecio();
        });

        cbTamano.addActionListener(e -> {
            pilaDeshacer.apilar(new AccionSeleccion(AccionSeleccion.TipoAccion.SELECCIONAR_TAMANO, (String) cbTamano.getSelectedItem()));
            actualizarPrecio();
        });

        chkOregano.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                pilaDeshacer.apilar(new AccionSeleccion(AccionSeleccion.TipoAccion.AGREGAR_ADICIONAL, "Oregano"));
            }
            actualizarPrecio();
        });

        chkAji.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                pilaDeshacer.apilar(new AccionSeleccion(AccionSeleccion.TipoAccion.AGREGAR_ADICIONAL, "Aji"));
            }
            actualizarPrecio();
        });

        chkPimienta.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                pilaDeshacer.apilar(new AccionSeleccion(AccionSeleccion.TipoAccion.AGREGAR_ADICIONAL, "Pimienta"));
            }
            actualizarPrecio();
        });

        lblPrecioTotal = new JLabel("TOTAL: $15.00", JLabel.CENTER);
        lblPrecioTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblPrecioTotal.setForeground(new Color(230, 175, 40));

        // Botones
        JButton btnDeshacer = new JButton("Deshacer (Pila)");
        btnDeshacer.setBackground(new Color(70, 75, 85));
        btnDeshacer.setForeground(Color.WHITE);
        btnDeshacer.addActionListener(e -> ejecutarDeshacerPila());

        JButton btnPedir = new JButton("CONFIRMAR Y ENCOLAR PEDIDO");
        btnPedir.setBackground(new Color(50, 150, 50));
        btnPedir.setForeground(Color.WHITE);
        btnPedir.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnPedir.addActionListener(e -> agregarPedidoACola());

        // Ensamblar en ORDEN SOLICITADO
        panel.add(lblTituloTrie);
        panel.add(txtBuscadorTrie);
        panel.add(lblSugerenciasTrie);
        panel.add(Box.createVerticalStrut(8));

        panel.add(crearEtiqueta("1. Categoria:", fontLabel, colorLabel));
        panel.add(cbCategoria);
        panel.add(crearEtiqueta("2. Sabor:", fontLabel, colorLabel));
        panel.add(cbSabor);
        panel.add(crearEtiqueta("3. Tamano:", fontLabel, colorLabel));
        panel.add(cbTamano);
        panel.add(Box.createVerticalStrut(5));

        panel.add(chkOregano);
        panel.add(chkAji);
        panel.add(chkPimienta);
        panel.add(Box.createVerticalStrut(10));

        panel.add(crearEtiqueta("4. Nombre Cliente:", fontLabel, colorLabel));
        panel.add(txtCliente);
        panel.add(crearEtiqueta("5. Telefono Contacto:", fontLabel, colorLabel));
        panel.add(txtContacto);
        panel.add(Box.createVerticalStrut(5));

        panel.add(crearEtiqueta("6. Destino en Mirage:", fontLabel, colorLabel));
        panel.add(cbDestinoNodo);
        panel.add(Box.createVerticalStrut(10));

        panel.add(lblPrecioTotal);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnDeshacer);
        panel.add(Box.createVerticalStrut(5));
        panel.add(btnPedir);

        actualizarPrecio();
        return panel;
    }

    private void ejecutarDeshacerPila() {
        AccionSeleccion accion = pilaDeshacer.desapilar();
        if (accion == null) {
            JOptionPane.showMessageDialog(this, "No hay acciones para deshacer en el historial.");
            return;
        }

        switch (accion.getTipo()) {
            case AGREGAR_ADICIONAL:
                if (accion.getValor().equalsIgnoreCase("Oregano")) chkOregano.setSelected(false);
                if (accion.getValor().equalsIgnoreCase("Aji")) chkAji.setSelected(false);
                if (accion.getValor().equalsIgnoreCase("Pimienta")) chkPimienta.setSelected(false);
                break;
            case SELECCIONAR_TAMANO:
                cbTamano.setSelectedIndex(0);
                break;
            case SELECCIONAR_SABOR:
                cbSabor.setSelectedIndex(0);
                break;
            case SELECCIONAR_CATEGORIA:
                cbCategoria.setSelectedIndex(0);
                break;
        }
        actualizarPrecio();
        JOptionPane.showMessageDialog(this, "Deshecho: " + accion.getTipo() + " -> " + accion.getValor());
    }

    private JLabel crearEtiqueta(String texto, Font f, Color c) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(f);
        lbl.setForeground(c);
        return lbl;
    }

    private void estilarCheckBox(JCheckBox chk) {
        chk.setOpaque(false);
        chk.setForeground(Color.WHITE);
        chk.setFont(new Font("SansSerif", Font.PLAIN, 11));
    }

    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(40, 44, 52));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(230, 175, 40)),
                " MAPA TACTICO MIRAGE (GRAFO + DIJKSTRA) ", 0, 0, new Font("SansSerif", Font.BOLD, 12), new Color(230, 175, 40)));

        panelMapa = new PanelMapaMirage(mapaMirage);
        panel.add(panelMapa, BorderLayout.CENTER);

        JPanel pnlControlesMapa = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlControlesMapa.setOpaque(false);

        JButton btnEnemigo = new JButton("GENERAR ENEMIGO ALEATORIO");
        btnEnemigo.setBackground(new Color(180, 50, 50));
        btnEnemigo.setForeground(Color.WHITE);
        btnEnemigo.addActionListener(e -> {
            String alerta = mapaMirage.generarEnemigoAleatorio();
            JOptionPane.showMessageDialog(this, alerta, "Alerta de Enemigo", JOptionPane.WARNING_MESSAGE);
            panelMapa.repaint();
        });

        JButton btnReiniciar = new JButton("Limpiar Mapa");
        btnReiniciar.setBackground(new Color(70, 75, 85));
        btnReiniciar.setForeground(Color.WHITE);
        btnReiniciar.addActionListener(e -> {
            mapaMirage.reiniciarCaminos();
            JOptionPane.showMessageDialog(this, "Caminos despejados.");
            panelMapa.repaint();
        });

        lblEstadoRuta = new JLabel("Estado: Esperando despacho...", JLabel.CENTER);
        lblEstadoRuta.setForeground(Color.WHITE);

        pnlControlesMapa.add(btnEnemigo);
        pnlControlesMapa.add(btnReiniciar);

        panel.add(pnlControlesMapa, BorderLayout.NORTH);
        panel.add(lblEstadoRuta, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setPreferredSize(new Dimension(0, 180));
        panel.setBackground(new Color(40, 44, 52));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(230, 175, 40)),
                " COLA DE ATENCION DE PEDIDOS (QUEUE - FIFO) ", 0, 0, new Font("SansSerif", Font.BOLD, 12), new Color(230, 175, 40)));

        String[] columnas = {"ID", "Cliente", "Destino", "Pizza", "Monto", "Estado"};
        tablaModelo = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(tablaModelo);
        tabla.setBackground(new Color(30, 33, 38));
        tabla.setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabla.setDefaultRenderer(Object.class, centerRenderer);

        JButton btnDespachar = new JButton("DESPACHAR SIGUIENTE PEDIDO (DESENCOLAR)");
        btnDespachar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnDespachar.setBackground(new Color(230, 175, 40));
        btnDespachar.setForeground(Color.BLACK);
        btnDespachar.addActionListener(e -> despacharSiguientePedido());

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(btnDespachar, BorderLayout.SOUTH);

        return panel;
    }

    private void actualizarPrecio() {
        double total = cbCategoria.getSelectedItem().equals("Especial") ? 12.0 : 9.0;
        if (cbTamano.getSelectedItem().equals("Medium")) total += 3.0;
        if (cbTamano.getSelectedItem().equals("Large")) total += 6.0;
        if (chkOregano.isSelected()) total += 0.50;
        if (chkAji.isSelected()) total += 0.50;
        if (chkPimienta.isSelected()) total += 0.50;
        lblPrecioTotal.setText(String.format("TOTAL: $%.2f", total));
    }

    private void agregarPedidoACola() {
        Usuario usuario = new Usuario(txtCliente.getText(), txtContacto.getText(), (String) cbDestinoNodo.getSelectedItem());
        Pizza pizza = new Pizza((String) cbCategoria.getSelectedItem(), (String) cbSabor.getSelectedItem(), (String) cbTamano.getSelectedItem());

        if (chkOregano.isSelected()) pizza.agregarAdicionales("Oregano");
        if (chkAji.isSelected()) pizza.agregarAdicionales("Aji");
        if (chkPimienta.isSelected()) pizza.agregarAdicionales("Pimienta");

        Pedido nuevo = new Pedido(usuario, pizza);
        colaPedidos.encolar(nuevo);

        tablaModelo.addRow(new Object[]{
                nuevo.getIdPedido(), usuario.getNombre(), usuario.getNodoUbicacion(),
                pizza.getSabor() + " (" + pizza.getSize() + ")", "$" + pizza.getPrecio(), nuevo.getEstado()
        });
    }

    private void despacharSiguientePedido() {
        if (colaPedidos.estaVacia()) {
            JOptionPane.showMessageDialog(this, "No hay pedidos en la cola de espera.");
            return;
        }

        Pedido atendido = colaPedidos.desencolar();
        String destino = atendido.getUsuario().getNodoUbicacion();

        GrafoMapa.ResultadoRuta resultado = mapaMirage.calcularRutaMasCorta("T Spawn", destino);

        if (!resultado.isExisteRuta()) {
            lblEstadoRuta.setText("NO HAY RUTA DISPONIBLE A " + destino + ". CAMINOS BLOQUEADOS POR ENEMIGOS.");
            JOptionPane.showMessageDialog(this, "Todos los caminos hacia " + destino + " estan bloqueados. Despeje las vias.", "Bloqueo Critico", JOptionPane.ERROR_MESSAGE);
            return;
        }

        panelMapa.setRutaActual(resultado.getCamino());
        lblEstadoRuta.setText(resultado.toString());

        tablaModelo.setValueAt("En camino / Entregado", 0, 5);
        tablaModelo.removeRow(0);

        GestorPersistencia.registrarTransaccion(atendido, resultado.getTiempoTotal());
    }
}
