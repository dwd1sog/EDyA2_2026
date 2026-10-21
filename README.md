# Counter Pizza - Sistema Táctico de Despacho Mirage (Versión Web)

## Descripción General

Counter Pizza Web es una aplicación web interactiva que combina la gestión comercial de una pizzería con la logística de envíos sobre el mapa táctico Mirage de Counter-Strike. La aplicación permite armar pedidos personalizados, encolarlos en una fila de atención FIFO, visualizar la ruta de entrega más rápida calculada mediante el algoritmo de Dijkstra sobre un lienzo Canvas HTML5, e identificar bloqueos dinámicos por enemigos en tiempo real.

## Arquitectura y Tecnologías Utilizadas

- **HTML5:** Estructura semántica de la interfaz, dividida en panel de controles, mapa Canvas y tablas de datos.
- **CSS3:** Diseño responsivo con paleta de colores táctica (fuegos, dorados, tonos oscuros) y tablas de datos.
- **JavaScript (Vanilla JS):** Lógica del cliente sin dependencias externas, incluyendo las 5 estructuras de datos fundamentales.
- **HTML5 Canvas API:** Renderizado dinámico del mapa de madera, nodos de posición, waypoints invisibles y trazado de rutas.
- **localStorage API:** Persistencia NoSQL local en el navegador para almacenar el historial de transacciones.

## Estructuras de Datos Implementadas

1. **Árbol Trie (ArbolTrie):** Utilizado en el campo de búsqueda del menú para autocompletar nombres de pizzas en tiempo real según el prefijo ingresado.
2. **Pila LIFO (PilaHistorial):** Administra el historial de acciones en el formulario, permitiendo deshacer elecciones de ingredientes o tamaño en orden cronológico inverso.
3. **Cola FIFO (ColaPedidos):** Mantiene la fila de atención de pedidos pendientes, procesándolos estrictamente en el orden en que fueron añadidos.
4. **Grafo Ponderado (GrafoMapa):** Modela la red de pasillos y habitaciones del mapa Mirage, asignando tiempos de recorrido en segundos entre cada punto.
5. **Cola de Prioridad / MinHeap:** Utilizada internamente dentro del algoritmo de Dijkstra para extraer el nodo con la menor distancia acumulada con complejidad O((V+E) log V).

## Estructura de Archivos del Proyecto

```text
├── index.html       Estructura principal y marcado de la interfaz
├── styles.css       Estilos visuales, temas oscuros y componentes
├── app.js           Estructuras de datos, lógica del mapa y eventos
└── mapa.png         Imagen de fondo con el plano en madera de Mirage
```

## Funcionalidades Principales

- **Búsqueda por Prefijos:** Sugerencias dinámicas al escribir en el campo de pizza.
- **Cálculo de Precios:** Actualización automática según tamaño (Personal, Mediana, Familiar) y cantidad de adicionales.
- **Simulación de Enemigos:** Botón para bloquear tramos aleatorios del mapa. Dijkstra redirige el trayecto automáticamente bordando zonas alternativas como A Ramp, Palace, Connector o Jungle.
- **Waypoints Invisibles:** Puntos de paso intermedios (WP\_) para doblar esquinas sin atravesar muros en la representación gráfica.
- **Historial Persistente:** Los pedidos despachados se guardan en localStorage y se mantienen disponibles aun si se recarga la página.
