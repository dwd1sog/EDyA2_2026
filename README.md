# Estructura de Ramas del Repositorio

Este repositorio contiene el desarrollo modular y progresivo del proyecto final y los desafíos prácticos del curso de Estructura de Datos realizado en el lenguaje Java, así como la migración final a la web.

## Descripción de Ramas

### 1. main

- **Propósito:** Rama principal por defecto del repositorio.
- **Contenido:** Contiene este archivo README.md como una guía de estructuración e información.

### 2. Graph

- **Propósito:** Desarrollo e implementación de teoría de grafos y algoritmos de ruta corta.
- **Contenido:**
  - Solución al Desafío 10 (Red social entre personas y ciudades con lista de adyacencia).

### 3. Lists

- **Propósito:** Trabajos prácticos y ejercicios sobre listas enlazadas.
- **Contenido:**
  - Estructuras de listas simplemente enlazadas, dobles y circulares basadas en la Clase 03.
  - Métodos de inserción, recorrido y manipulación de punteros.

### 4. N_aryTrees

- **Propósito:** Implementación de árboles N-arios y estructuras jerárquicas multinivel.
- **Contenido:**
  - Solución al Desafío 09.
  - Algoritmos de recorrido DFS (Profundidad) y BFS (Anchura por Niveles).

### 5. Queues

- **Propósito:** Implementación de estructuras lineales de acceso FIFO (First In, First Out).
- **Contenido:**
  - Procesamiento de tickets de pedidos en orden estricto de llegada.

### 6. Stacks

- **Propósito:** Implementación de estructuras lineales de acceso LIFO (Last In, First Out).
- **Contenido:**
  - Lógica de desapilar una lista utilizando una torre de libros.

### 7. Trees

- **Propósito:** Desarrollo de estructuras jerárquicas binarias.
- **Contenido:**
  - Árbol Binario de Búsqueda (BST) con inserción, búsqueda y recorridos InOrder, PreOrder y PostOrder.

### 8. TriesNHeaps

- **Propósito:** Estructuras avanzadas de búsqueda y colas de prioridad.
- **Contenido:**
  - Clase `ArbolTrie.java` para autocompletado eficiente por prefijos.
  - Implementación de `MinHeap` para ordenamiento por prioridad.
  - Solución al Desafío 11 para el ranking de los K productos más populares.

### 9. projectApp

- **Propósito:** Contiene el código fuente completo de la aplicación de escritorio desarrollada en Java Swing con interfaz gráfica (GUI). Implementa el sistema táctico de despacho de pedidos \*\*Counter Pizza\*\* integrado con el diorama del mapa Mirage.
- **Arquitectura:** Clases de la interfaz de usuario (`MainFrame`, `PanelMapaMirage`), lógica de enrutamiento y estructuras de datos (`GrafoMapa`, `ArbolTrie`, `PilaHistorial`, `GestorPersistencia`).
- **Estructuras de Datos:** Integración de las 5 estructuras requeridas (Grafo Ponderado, MinHeap, Cola FIFO, Pila LIFO y Árbol Trie).
- **Persistencia:** Registro de transacciones e historial de pedidos mediante archivo JSON local (`historial_pedidos.json`).
- **Renderizado Visual:** Renderizado de nodos, aristas y trazado dinámico de rutas de Dijkstra con soporte para desvíos tácticos ante bloqueos de enemigos.

### 10. projectWeb

- **Propósito:** Migración completa del proyecto a entorno web para despliegue en servidor público.
- **Contenido:**
  - Código en HTML5, CSS3 y JavaScript puro (Vanilla JS).
  - Traducción de las 5 estructuras de datos a JavaScript.
  - Interfaz con Lienzo Canvas HTML5 y mapa interactivo de Mirage.
  - Persistencia NoSQL mediante `localStorage`.
  - Configuración lista para despliegue en Netlify.
