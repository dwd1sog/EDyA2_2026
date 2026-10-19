# 🍕 Counter Pizza - Sistema de Despacho Táctico Mirage

**Materia:** Estructura de Datos (Java)  
**Proyecto Final:** Pizzería Táctica con Sistema de Logística y Ruteo Óptimo sobre Mapa de Juego (CS Mirage)  
**Lenguaje:** Java 21 LTS | **GUI:** Java Swing | **Persistencia:** JSON  

---

## 📋 1. Descripción General

**Counter Pizza** es una aplicación con interfaz gráfica de usuario (GUI) desarrollada en Java que combina la gestión comercial de una pizzería con un simulador de envíos tácticos sobre el mapa **Mirage** de *Counter-Strike*.

El sistema permite a los operadores tomar pedidos personalizados, encolarlos para su preparación, calcular la ruta de entrega más rápida evitando zonas de peligro con enemigos en tiempo real, y guardar el historial de transacciones en formato JSON.

---

## 🏛️ 2. Alineación con la Rúbrica Académica (5 Estructuras de Datos)

El proyecto cumple strictly con el requerimiento académico de implementar **5 estructuras de datos fundamentales**:

| # | Estructura | Clase Java | Categoría Rúbrica | Función en el Sistema |
|---|---|---|---|---|
| 1 | **Grafo Ponderado** | `structures.GrafoMapa` | Grafos | Representa la red de zonas y pasillos del mapa Mirage. Los vértices son ubicaciones (*T Spawn, A Site, B Site, Mid, etc.*) y las aristas son pasillos con su tiempo de desplazamiento en segundos. |
| 2 | **MinHeap (`PriorityQueue`)** | `structures.GrafoMapa` | Árboles / Heaps | Utilizado internamente por el algoritmo de **Dijkstra** para extraer eficientemente en tiempo $O(\log V)$ el nodo no visitado con menor tiempo acumulado. |
| 3 | **Cola (`Queue` - FIFO)** | `structures.ColaPedidos` | Listas / Colas | Administra la línea de atención de pedidos de la pizzería en estricto orden de llegada (*First In, First Out*). |
| 4 | **Pila (`Stack` - LIFO)** | `structures.PilaHistorial` | Listas / Pilas | Almacena el historial de modificaciones del pedido en curso, permitiendo la función **"Deshacer (Pila)"** (*Last In, First Out*). |
| 5 | **Trie (`ArbolTrie`)** | `structures.ArbolTrie` | Árboles / Tries | Árbol de prefijos que potencia el buscador en tiempo real con autocompletado y selección automática de sabores e ingredientes en la GUI. |

---

## 💻 3. Requisitos del Sistema

* **Java Development Kit (JDK):** Versión 21 LTS o superior.
* **Entorno de Desarrollo (IDE):** IntelliJ IDEA (Recomendado), Eclipse o NetBeans.
* **Sistema Operativo:** Windows, macOS o Linux.
* **Recursos Gráficos:** Archivo de imagen `mapa.png` (formato PNG real) ubicado en la carpeta `src/`.

---

## 📂 4. Estructura del Proyecto Java

```text
src/
├── Main.java                          <- Punto de entrada de la aplicación
├── gui/
│   ├── MainFrame.java                 <- Ventana principal (Dashboard de pedidos y mapa)
│   └── PanelMapaMirage.java           <- Panel gráfico responsivo del mapa Mirage
├── models/
│   ├── Pizza.java                     <- Modelo de datos de la pizza
│   ├── Usuario.java                   <- Modelo de datos del cliente
│   ├── Pedido.java                    <- Modelo de datos de la orden
│   └── AccionSeleccion.java           <- Modelo de acciones para la Pila
├── structures/
│   ├── GrafoMapa.java                 <- Grafo, Waypoints y Algoritmo de Dijkstra
│   ├── ColaPedidos.java               <- Estructura de Cola FIFO
│   ├── PilaHistorial.java             <- Estructura de Pila LIFO
│   └── ArbolTrie.java                 <- Estructura de Árbol Trie
├── persistence/
│   └── GestorPersistencia.java        <- Guardado automático en JSON
└── mapa.png                           <- Imagen de fondo del plano en madera
```

---

## 🚀 5. Guía de Instalación y Configuración Paso a Paso

1. **Clonar o Importar el Proyecto:**
   * Abre IntelliJ IDEA y selecciona **Open** ➔ Elige la carpeta raíz del proyecto.
2. **Configurar el JDK:**
   * Ve a `File` ➔ `Project Structure` ➔ `Project`.
   * Asegúrate de que el **SDK** esté configurado en **Java 21**.
3. **Verificar el Recurso Gráfico:**
   * Asegúrate de que la imagen **`mapa.png`** esté guardada en la carpeta **`src/`** del proyecto.
   * *Nota importante:* Debe ser un archivo formato PNG auténtico (si proviene de WebP, debe guardarse mediante Paint u otro editor como `.png`).
4. **Ejecutar la Aplicación:**
   * Abre `src/main/Main.java`.
   * Presiona `Shift + F10` o haz clic en el botón verde de **Run**.

---

## 🎮 6. Manual de Usuario / Guía de Interacción

### Paso 1: Búsqueda y Selección Automática (Trie)
1. En el panel izquierdo, escribe las primeras letras del sabor deseado en el **Buscador de Menú (Trie)** (ejemplo: `"Pep"`, `"Excent"`, `"Aji"`).
2. El sistema filtrará las opciones en tiempo real y **seleccionará automáticamente el Sabor, Categoría y Adicionales** correspondientes en los menús desplegables.

### Paso 2: Personalización del Pedido y Precio Dinámico
1. Selecciona la **Categoría** (*Tradicional: $9.00 / Especial: $12.00*).
2. Selecciona el **Tamaño** (*Small: +$0.00 / Medium: +$3.00 / Large: +$6.00*).
3. Marca los **Adicionales** (*Orégano, Ají, Pimienta: +$0.50 c/u*).
4. El label amarillo de **TOTAL** se re-calculará instantáneamente en tiempo real.

### Paso 3: Función Deshacer (Pila LIFO)
* Si cometiste un error al elegir un ingrediente o tamaño, presiona el botón **`Deshacer (Pila)`**.
* La Pila revertirá la última acción realizada y ajustará el formulario y el precio dinámico de inmediato.

### Paso 4: Datos del Cliente y Confirmación (Cola FIFO)
1. Ingresa el **Nombre del Cliente** y **Teléfono**.
2. Selecciona la **Ubicación en Mirage** (*A Site, B Site, Mid, Palace, Apps, CT Spawn, etc.*).
3. Haz clic en **`CONFIRMAR Y ENCOLAR PEDIDO`**.
4. El pedido ingresará a la tabla inferior de la **Cola de Atención (Queue)** con estado `"En espera"`.

### Paso 5: Simulación Táctica y Ruteo Óptimo (Grafo + Dijkstra)
* **Generar Enemigo Aleatorio:** Al hacer clic en este botón, un pasillo del mapa se bloqueará simulando la presencia de un enemigo. La arista afectada se pintará de **rojo brillante con un emblema de alerta**.
* **Despachar Siguiente Pedido:** Al presionar este botón, la Cola desencolará al cliente en turno (FIFO) y el Grafo ejecutará el algoritmo de **Dijkstra** desde `T Spawn` hasta la ubicación destino.
* **Ruta Óptima:** El mapa trazará la ruta en **línea dorada**, bordeando las paredes mediante nodos de paso (*Waypoints*) y evitando los pasillos bloqueados por enemigos.

---

## 💾 7. Persistencia de Datos (JSON)

Cada vez que un pedido es despachado con éxito, la clase `GestorPersistencia` registra automáticamente la transacción en el archivo **`historial_pedidos.json`** en la raíz del proyecto con la siguiente estructura:

```json
[
  {
    "idPedido": 1,
    "fechaHora": "2026-10-18 22:00:00",
    "cliente": "S1mple",
    "contacto": "3001234567",
    "destino": "B Site",
    "pizza": "Pepperoni (Large)",
    "montoTotal": 18.0,
    "tiempoEntregaSegundos": 7,
    "estado": "Entregado"
  }
]
```

---

## 🔧 8. Arquitectura y Solución de Problemas

* **Mapa Responsivo:** El panel `PanelMapaMirage` utiliza coordenadas porcentuales normalizadas (`0.0` a `1.0`), garantizando que los nodos y líneas se adaptaten automáticamente al estirar o maximizar la ventana.
* **Nodos Invisibles (*Waypoints*):** Los nodos con prefijo `WP_` permiten que la línea de envío doble en esquinas y pasillos sin dibujar círculos ni texto innecesario en la GUI.
* **Solución si el mapa no carga:** Verifica que `mapa.png` esté dentro de `src/` y que no sea un archivo WebP renombrado.
