// ==========================================
// 1. ESTRUCTURA 1: ÁRBOL TRIE (BÚSQUEDA Y AUTOCOMPLETADO)
// ==========================================
class TrieNode {
    constructor() {
        this.children = {};
        this.isEndOfWord = false;
        this.data = null;
    }
}

class ArbolTrie {
    constructor() {
        this.root = new TrieNode();
    }

    insert(palabra, data = null) {
        let node = this.root;
        const word = palabra.toLowerCase().trim();
        for (let char of word) {
            if (!node.children[char]) {
                node.children[char] = new TrieNode();
            }
            node = node.children[char];
        }
        node.isEndOfWord = true;
        node.data = data || { nombre: palabra };
    }

    buscarPorPrefijo(prefijo) {
        if (!prefijo || prefijo.trim() === "") return [];
        let node = this.root;
        const prefix = prefijo.toLowerCase().trim();
        for (let char of prefix) {
            if (!node.children[char]) return [];
            node = node.children[char];
        }
        const resultados = [];
        this._recolectarPalabras(node, resultados);
        return resultados;
    }

    _recolectarPalabras(node, resultados) {
        if (node.isEndOfWord && node.data) {
            resultados.push(node.data);
        }
        for (let char in node.children) {
            this._recolectarPalabras(node.children[char], resultados);
        }
    }
}

// ==========================================
// 2. ESTRUCTURA 2: PILA LIFO (DESHACER SELECCIONES)
// ==========================================
class PilaHistorial {
    constructor() {
        this.items = [];
    }

    push(accion) {
        this.items.push(accion);
    }

    pop() {
        if (this.isEmpty()) return null;
        return this.items.pop();
    }

    isEmpty() {
        return this.items.length === 0;
    }

    clear() {
        this.items = [];
    }
}

// ==========================================
// 3. ESTRUCTURA 3: COLA FIFO (FILA DE PEDIDOS)
// ==========================================
class ColaPedidos {
    constructor() {
        this.items = [];
    }

    enqueue(pedido) {
        this.items.push(pedido);
    }

    dequeue() {
        if (this.isEmpty()) return null;
        return this.items.shift();
    }

    peek() {
        if (this.isEmpty()) return null;
        return this.items[0];
    }

    isEmpty() {
        return this.items.length === 0;
    }

    getTodos() {
        return [...this.items];
    }
}

// ==========================================
// 4. ESTRUCTURAS 4 Y 5: MINHEAP + GRAFO PONDERADO (DIJKSTRA)
// ==========================================
class MinHeap {
    constructor() {
        this.heap = [];
    }

    insert(nodo, distancia) {
        this.heap.push({ nodo, distancia });
        this._bubbleUp(this.heap.length - 1);
    }

    extractMin() {
        if (this.isEmpty()) return null;
        const min = this.heap[0];
        const end = this.heap.pop();
        if (this.heap.length > 0) {
            this.heap[0] = end;
            this._sinkDown(0);
        }
        return min;
    }

    isEmpty() {
        return this.heap.length === 0;
    }

    _bubbleUp(n) {
        const element = this.heap[n];
        while (n > 0) {
            let parentN = Math.floor((n - 1) / 2);
            let parent = this.heap[parentN];
            if (element.distancia >= parent.distancia) break;
            this.heap[parentN] = element;
            this.heap[n] = parent;
            n = parentN;
        }
    }

    _sinkDown(n) {
        const length = this.heap.length;
        const element = this.heap[n];
        while (true) {
            let leftChildN = 2 * n + 1;
            let rightChildN = 2 * n + 2;
            let leftChild, rightChild;
            let swap = null;

            if (leftChildN < length) {
                leftChild = this.heap[leftChildN];
                if (leftChild.distancia < element.distancia) {
                    swap = leftChildN;
                }
            }
            if (rightChildN < length) {
                rightChild = this.heap[rightChildN];
                if (
                    (swap === null && rightChild.distancia < element.distancia) ||
                    (swap !== null && rightChild.distancia < leftChild.distancia)
                ) {
                    swap = rightChildN;
                }
            }
            if (swap === null) break;
            this.heap[n] = this.heap[swap];
            this.heap[swap] = element;
            n = swap;
        }
    }
}

class GrafoMapa {
    constructor() {
        this.mapa = new Map();
        this.inicializarMapaMirage();
    }

    agregarNodo(nombre) {
        if (!this.mapa.has(nombre)) {
            this.mapa.set(nombre, []);
        }
    }

    agregarCamino(origen, destino, tiempo) {
        this.agregarNodo(origen);
        this.agregarNodo(destino);
        this.mapa.get(origen).push({ destino, tiempoSegundos: tiempo, bloqueado: false });
        this.mapa.get(destino).push({ destino: origen, tiempoSegundos: tiempo, bloqueado: false });
    }

    cambiarEstadoCamino(nodoA, nodoB, bloqueado) {
        if (this.mapa.has(nodoA)) {
            for (let arista of this.mapa.get(nodoA)) {
                if (arista.destino.toLowerCase() === nodoB.toLowerCase()) {
                    arista.bloqueado = bloqueado;
                }
            }
        }
        if (this.mapa.has(nodoB)) {
            for (let arista of this.mapa.get(nodoB)) {
                if (arista.destino.toLowerCase() === nodoA.toLowerCase()) {
                    arista.bloqueado = bloqueado;
                }
            }
        }
    }

    esPuntoDePaso(nombre) {
        return nombre && nombre.startsWith("WP_");
    }

    getCaminosBloqueados() {
        const bloqueados = [];
        const procesados = new Set();
        for (let [origen, aristas] of this.mapa.entries()) {
            for (let arista of aristas) {
                if (arista.bloqueado) {
                    let c1 = `${origen}-${arista.destino}`;
                    let c2 = `${arista.destino}-${origen}`;
                    if (!procesados.has(c1) && !procesados.has(c2)) {
                        bloqueados.push([origen, arista.destino]);
                        procesados.add(c1);
                        procesados.add(c2);
                    }
                }
            }
        }
        return bloqueados;
    }

    generarEnemigoAleatorio() {
        const disponibles = [];
        for (let [origen, aristas] of this.mapa.entries()) {
            for (let arista of aristas) {
                if (!arista.bloqueado) {
                    disponibles.push([origen, arista.destino]);
                }
            }
        }
        if (disponibles.length === 0) return "¡Alerta Máxima! Todos los caminos están bloqueados.";

        const elegido = disponibles[Math.floor(Math.random() * disponibles.length)];
        this.cambiarEstadoCamino(elegido[0], elegido[1], true);
        return `¡ENEMIGO DETECTADO! Camino bloqueado entre: [${elegido[0]} - ${elegido[1]}]`;
    }

    reiniciarCaminos() {
        for (let aristas of this.mapa.values()) {
            for (let arista of aristas) {
                arista.bloqueado = false;
            }
        }
    }

    calcularRutaMasCorta(origen, destino) {
        if (!this.mapa.has(origen) || !this.mapa.has(destino)) {
            return { camino: [], tiempoTotal: 0, existeRuta: false };
        }

        const distancias = new Map();
        const predecesores = new Map();
        const minHeap = new MinHeap();

        for (let nodo of this.mapa.keys()) {
            distancias.set(nodo, Infinity);
        }

        distancias.set(origen, 0);
        minHeap.insert(origen, 0);

        while (!minHeap.isEmpty()) {
            const { nodo: u, distancia: distU } = minHeap.extractMin();

            if (distU > distancias.get(u)) continue;
            if (u.toLowerCase() === destino.toLowerCase()) break;

            const aristas = this.mapa.get(u) || [];
            for (let arista of aristas) {
                if (arista.bloqueado) continue;

                let v = arista.destino;
                let peso = arista.tiempoSegundos;

                if (distancias.get(u) + peso < distancias.get(v)) {
                    distancias.set(v, distancias.get(u) + peso);
                    predecesores.set(v, u);
                    minHeap.insert(v, distancias.get(v));
                }
            }
        }

        if (distancias.get(destino) === Infinity) {
            return { camino: [], tiempoTotal: 0, existeRuta: false };
        }

        const camino = [];
        let paso = destino;
        while (paso) {
            camino.unshift(paso);
            paso = predecesores.get(paso);
        }

        return { camino, tiempoTotal: distancias.get(destino), existeRuta: true };
    }

    inicializarMapaMirage() {
        this.agregarCamino("T Spawn", "WP_T_Palace", 2);
        this.agregarCamino("WP_T_Palace", "Palace", 2);

        this.agregarCamino("T Spawn", "WP_T_Ramp", 2);
        this.agregarCamino("WP_T_Ramp", "A Ramp", 2);

        this.agregarCamino("T Spawn", "TV", 2);
        this.agregarCamino("TV", "Apps", 2);
        this.agregarCamino("TV", "Mid", 2);

        this.agregarCamino("Palace", "A Site", 3);
        this.agregarCamino("A Ramp", "A Site", 2);
        this.agregarCamino("A Site", "WP_Conn_A", 2);
        this.agregarCamino("WP_Conn_A", "Connector", 1);
        this.agregarCamino("A Site", "Jungle", 2);
        this.agregarCamino("A Site", "CT Spawn", 3);

        this.agregarCamino("Mid", "Window", 3);
        this.agregarCamino("Mid", "Connector", 2);
        this.agregarCamino("Mid", "WP_Mid_Short", 1);
        this.agregarCamino("WP_Mid_Short", "Short B", 3);

        this.agregarCamino("Ladder Room", "Window", 2);
        this.agregarCamino("Ladder Room", "WP_Mid_Short", 2);
        this.agregarCamino("Window", "Jungle", 2);
        this.agregarCamino("Jungle", "WP_CT_B", 2);

        this.agregarCamino("Short B", "B Site", 3);
        this.agregarCamino("Apps", "B Site", 3);

        this.agregarCamino("CT Spawn", "WP_CT_B", 2);
        this.agregarCamino("WP_CT_B", "B Site", 2);
        this.agregarCamino("Connector", "Jungle", 2);
    }
}

// ==========================================
// 5. CONTROLADOR PRINCIPAL Y CANVAS
// ==========================================
document.addEventListener("DOMContentLoaded", () => {
    // Instancias de Estructuras de Datos
    const trie = new ArbolTrie();
    const pilaHistorial = new PilaHistorial();
    const colaPedidos = new ColaPedidos();
    const grafo = new GrafoMapa();

    // Cargar Catálogo en Trie
    const catalogoPizzas = [
        { nombre: "Pepperoni Especial", precioBase: 16.00 },
        { nombre: "Hawaiana Deluxe", precioBase: 15.50 },
        { nombre: "Pollo BBQ Táctico", precioBase: 17.00 },
        { nombre: "Carnivora Mirage", precioBase: 18.50 },
        { nombre: "Vegetariana A Site", precioBase: 14.50 },
        { nombre: "Cuatro Quesos B Site", precioBase: 16.50 },
        { nombre: "Margarita Clásica", precioBase: 13.00 }
    ];
    catalogoPizzas.forEach(p => trie.insert(p.nombre, p));

    // Elementos del DOM
    const inputPizza = document.getElementById("inputPizza");
    const sugerenciasTrie = document.getElementById("sugerenciasTrie");
    const selectTamano = document.getElementById("selectTamano");
    const toppingCbs = document.querySelectorAll(".topping-cb");
    const inputCliente = document.getElementById("inputCliente");
    const selectDestino = document.getElementById("selectDestino");
    const summaryNombre = document.getElementById("summaryNombre");
    const summaryToppings = document.getElementById("summaryToppings");
    const summaryPrecio = document.getElementById("summaryPrecio");

    const btnDeshacer = document.getElementById("btnDeshacer");
    const btnEncolar = document.getElementById("btnEncolar");
    const btnGenerarEnemigo = document.getElementById("btnGenerarEnemigo");
    const btnReiniciarMapa = document.getElementById("btnReiniciarMapa");
    const btnDespachar = document.getElementById("btnDespachar");
    const btnLimpiarHistorial = document.getElementById("btnLimpiarHistorial");

    const statusAlert = document.getElementById("statusAlert");
    const statusText = document.getElementById("statusText");

    const tablaColaBody = document.getElementById("tablaColaBody");
    const tablaHistorialBody = document.getElementById("tablaHistorialBody");

    const canvas = document.getElementById("mapaCanvas");
    const ctx = canvas.getContext("2d");
    const fallbackOverlay = document.getElementById("fallbackImagePicker");
    const fileInputMapa = document.getElementById("fileInputMapa");

    // Coordenadas Relativas Porcentuales sobre mapa.png
    const posicionesRelativas = {
        "T Spawn": { rx: 0.92, ry: 0.38 },
        "WP_T_Palace": { rx: 0.87, ry: 0.70 },
        "Palace": { rx: 0.73, ry: 0.85 },
        "WP_T_Ramp": { rx: 0.85, ry: 0.60 },
        "A Ramp": { rx: 0.73, ry: 0.70 },
        "A Site": { rx: 0.55, ry: 0.80 },
        "Mid": { rx: 0.55, ry: 0.45 },
        "Window": { rx: 0.38, ry: 0.45 },
        "Connector": { rx: 0.49, ry: 0.54 },
        "WP_Mid_Short": { rx: 0.45, ry: 0.35 },
        "Short B": { rx: 0.40, ry: 0.25 },
        "Ladder Room": { rx: 0.38, ry: 0.35 },
        "TV": { rx: 0.78, ry: 0.14 },
        "Apps": { rx: 0.35, ry: 0.11 },
        "B Site": { rx: 0.18, ry: 0.30 },
        "Jungle": { rx: 0.38, ry: 0.58 },
        "CT Spawn": { rx: 0.20, ry: 0.78 },
        "WP_CT_B": { rx: 0.29, ry: 0.55 },
        "WP_Conn_A": { rx: 0.52, ry: 0.67 }
    };

    let rutaActual = [];
    let pedidoEnDespacho = null;

    // Cargar Imagen Fondo
    let imagenFondo = new Image();
    let imagenCargada = false;

    function cargarImagenFondo(src) {
        imagenFondo.onload = () => {
            imagenCargada = true;
            fallbackOverlay.classList.add("hidden");
            renderizarMapa();
        };
        imagenFondo.onerror = () => {
            if (src !== "mapa.png") {
                cargarImagenFondo("mapa.png");
            } else {
                imagenCargada = false;
                fallbackOverlay.classList.remove("hidden");
                renderizarMapa();
            }
        };
        imagenFondo.src = src;
    }

    cargarImagenFondo("mapa.png");

    fileInputMapa.addEventListener("change", (e) => {
        if (e.target.files && e.target.files[0]) {
            const reader = new FileReader();
            reader.onload = (event) => {
                cargarImagenFondo(event.target.result);
            };
            reader.readAsDataURL(e.target.files[0]);
        }
    });

    // --- MANEJO DEL AUTOCOMPLETADO TRIE ---
    let pizzaSeleccionadaObj = catalogoPizzas[0];

    inputPizza.addEventListener("input", () => {
        const query = inputPizza.value;
        pilaHistorial.push({ tipo: "input_pizza", valorPrevio: summaryNombre.textContent });
        
        if (!query.trim()) {
            sugerenciasTrie.innerHTML = "";
            return;
        }

        const resultados = trie.buscarPorPrefijo(query);
        sugerenciasTrie.innerHTML = "";

        resultados.forEach(p => {
            const item = document.createElement("div");
            item.className = "suggestion-item";
            item.innerHTML = `<span>${p.nombre}</span> <strong>$${p.precioBase.toFixed(2)}</strong>`;
            item.addEventListener("click", () => {
                inputPizza.value = p.nombre;
                pizzaSeleccionadaObj = p;
                sugerenciasTrie.innerHTML = "";
                actualizarResumenPizza();
            });
            sugerenciasTrie.appendChild(item);
        });
    });

    document.addEventListener("click", (e) => {
        if (!inputPizza.contains(e.target) && !sugerenciasTrie.contains(e.target)) {
            sugerenciasTrie.innerHTML = "";
        }
    });

    selectTamano.addEventListener("change", () => {
        pilaHistorial.push({ tipo: "tamano" });
        actualizarResumenPizza();
    });

    toppingCbs.forEach(cb => {
        cb.addEventListener("change", () => {
            pilaHistorial.push({ tipo: "topping", checkbox: cb });
            actualizarResumenPizza();
        });
    });

    function actualizarResumenPizza() {
        const nombrePizza = pizzaSeleccionadaObj ? pizzaSeleccionadaObj.nombre : (inputPizza.value || "Pepperoni Especial");
        let precioBase = pizzaSeleccionadaObj ? pizzaSeleccionadaObj.precioBase : 16.00;

        // Ajuste según tamaño
        const tamano = selectTamano.value;
        if (tamano === "Personal") precioBase -= 3.00;
        if (tamano === "Familiar") precioBase += 4.00;

        // Toppings
        const toppingsElegidos = [];
        let extraToppings = 0;
        toppingCbs.forEach(cb => {
            if (cb.checked) {
                toppingsElegidos.push(cb.value);
                extraToppings += parseFloat(cb.getAttribute("data-precio") || "1.50");
            }
        });

        const precioTotal = precioBase + extraToppings;

        summaryNombre.textContent = `${nombrePizza} (${tamano})`;
        summaryToppings.textContent = toppingsElegidos.length > 0 ? toppingsElegidos.join(", ") : "Ninguno";
        summaryPrecio.textContent = `$${precioTotal.toFixed(2)}`;
    }

    actualizarResumenPizza();

    // --- ACCIÓN DESHACER (PILA LIFO) ---
    btnDeshacer.addEventListener("click", () => {
        if (pilaHistorial.isEmpty()) {
            alert("No hay acciones previas para deshacer.");
            return;
        }
        const ultimaAccion = pilaHistorial.pop();
        if (ultimaAccion.tipo === "topping") {
            ultimaAccion.checkbox.checked = !ultimaAccion.checkbox.checked;
        } else if (ultimaAccion.tipo === "input_pizza") {
            inputPizza.value = "";
        }
        actualizarResumenPizza();
    });

    // --- ACCIÓN ENCOLAR (COLA FIFO) ---
    let contadorPedidos = 1;

    btnEncolar.addEventListener("click", () => {
        const cliente = inputCliente.value.trim() || "Cliente Anonimo";
        const destino = selectDestino.value;

        const pedido = {
            id: contadorPedidos++,
            cliente: cliente,
            detallePizza: summaryNombre.textContent,
            toppings: summaryToppings.textContent,
            destino: destino,
            precio: summaryPrecio.textContent,
            estado: "Pendiente"
        };

        colaPedidos.enqueue(pedido);
        renderizarTablaCola();
        statusText.textContent = `Pedido #${pedido.id} para ${cliente} añadido a la cola FIFO.`;
    });

    function renderizarTablaCola() {
        const pedidos = colaPedidos.getTodos();
        if (pedidos.length === 0) {
            tablaColaBody.innerHTML = `<tr><td colspan="7" class="empty-msg">No hay pedidos pendientes en la cola FIFO.</td></tr>`;
            return;
        }

        tablaColaBody.innerHTML = pedidos.map((p, index) => `
            <tr>
                <td><strong>#${p.id}</strong></td>
                <td>${p.cliente}</td>
                <td>${p.detallePizza}</td>
                <td>${p.toppings}</td>
                <td><span class="badge" style="background:#2d3542; color:#e6af2e;">${p.destino}</span></td>
                <td><strong>${p.precio}</strong></td>
                <td><span class="badge-status ${index === 0 ? 'badge-active' : 'badge-pending'}">${index === 0 ? 'Siguiente' : 'En Espera'}</span></td>
            </tr>
        `).join("");
    }

    // --- DESPACHAR PEDIDO (DIJKSTRA Y GRAPH) ---
    btnDespachar.addEventListener("click", () => {
        if (colaPedidos.isEmpty()) {
            alert("La cola FIFO de pedidos está vacía.");
            return;
        }

        pedidoEnDespacho = colaPedidos.dequeue();
        renderizarTablaCola();

        const origen = "T Spawn";
        const destino = pedidoEnDespacho.destino;

        const resultado = grafo.calcularRutaMasCorta(origen, destino);

        if (!resultado.existeRuta) {
            rutaActual = [];
            statusAlert.style.borderColor = "#e63946";
            statusText.textContent = `⚠️ ¡BLOQUEO TOTAL! No existe ruta segura desde ${origen} hasta ${destino}. Todos los accesos tienen enemigos.`;
        } else {
            rutaActual = resultado.camino;
            statusAlert.style.borderColor = "#2a9d8f";
            statusText.textContent = `🚀 Pedido #${pedidoEnDespacho.id} para ${pedidoEnDespacho.cliente} | Ruta: ${resultado.camino.filter(n => !n.startsWith("WP_")).join(" -> ")} | Tiempo: ${resultado.tiempoTotal}s`;

            // Guardar en Historial (localStorage)
            guardarHistorialLocalStorage(pedidoEnDespacho, resultado);
        }

        renderizarMapa();
    });

    // --- BLOQUEOS Y ENEMIGOS ---
    btnGenerarEnemigo.addEventListener("click", () => {
        const msg = grafo.generarEnemigoAleatorio();
        statusAlert.style.borderColor = "#e63946";
        statusText.textContent = msg;

        // Re-calcular si hay pedido activo
        if (pedidoEnDespacho) {
            const res = grafo.calcularRutaMasCorta("T Spawn", pedidoEnDespacho.destino);
            if (res.existeRuta) {
                rutaActual = res.camino;
            } else {
                rutaActual = [];
            }
        }

        renderizarMapa();
    });

    btnReiniciarMapa.addEventListener("click", () => {
        grafo.reiniciarCaminos();
        statusAlert.style.borderColor = "#e6af2e";
        statusText.textContent = "Mapa despejado. Todas las rutas desbloqueadas.";

        if (pedidoEnDespacho) {
            const res = grafo.calcularRutaMasCorta("T Spawn", pedidoEnDespacho.destino);
            if (res.existeRuta) rutaActual = res.camino;
        }

        renderizarMapa();
    });

    // --- LOCALSTORAGE HISTORIAL ---
    function guardarHistorialLocalStorage(pedido, resultadoRuta) {
        const historial = JSON.parse(localStorage.getItem("counter_pizza_historial") || "[]");
        historial.unshift({
            id: pedido.id,
            fecha: new Date().toLocaleTimeString(),
            cliente: pedido.cliente,
            detalle: `${pedido.detallePizza} (${pedido.toppings})`,
            ruta: resultadoRuta.camino.filter(n => !n.startsWith("WP_")).join(" -> "),
            tiempo: `${resultadoRuta.tiempoTotal} seg`,
            monto: pedido.precio
        });
        localStorage.setItem("counter_pizza_historial", JSON.stringify(historial));
        renderizarTablaHistorial();
    }

    function renderizarTablaHistorial() {
        const historial = JSON.parse(localStorage.getItem("counter_pizza_historial") || "[]");
        if (historial.length === 0) {
            tablaHistorialBody.innerHTML = `<tr><td colspan="7" class="empty-msg">No hay historial registrado.</td></tr>`;
            return;
        }

        tablaHistorialBody.innerHTML = historial.map(h => `
            <tr>
                <td><strong>#${h.id}</strong></td>
                <td>${h.fecha}</td>
                <td>${h.cliente}</td>
                <td>${h.detalle}</td>
                <td><small>${h.ruta}</small></td>
                <td><strong>${h.tiempo}</strong></td>
                <td><span style="color:#2a9d8f;">${h.monto}</span></td>
            </tr>
        `).join("");
    }

    btnLimpiarHistorial.addEventListener("click", () => {
        if (confirm("¿Deseas borrar todo el historial guardado en localStorage?")) {
            localStorage.removeItem("counter_pizza_historial");
            renderizarTablaHistorial();
        }
    });

    renderizarTablaHistorial();

    // --- RENDERIZADO DEL MAPA CANVAS ---
    function renderizarMapa() {
        const w = canvas.width;
        const h = canvas.height;

        ctx.clearRect(0, 0, w, h);

        // 1. Fondo
        if (imagenCargada) {
            ctx.drawImage(imagenFondo, 0, 0, w, h);
        } else {
            ctx.fillStyle = "#161a20";
            ctx.fillRect(0, 0, w, h);
            ctx.strokeStyle = "#28303d";
            ctx.lineWidth = 1;
            for (let x = 0; x < w; x += 40) {
                ctx.beginPath(); ctx.moveTo(x, 0); ctx.lineTo(x, h); ctx.stroke();
            }
            for (let y = 0; y < h; y += 40) {
                ctx.beginPath(); ctx.moveTo(0, y); ctx.lineTo(w, y); ctx.stroke();
            }
        }

        // Obtener Pixeles
        const pixeles = {};
        for (let name in posicionesRelativas) {
            const rel = posicionesRelativas[name];
            pixeles[name] = { x: Math.round(rel.rx * w), y: Math.round(rel.ry * h) };
        }

        // 2. Conexiones Base del Grafo
        ctx.lineWidth = 3;
        const conexionesDibujadas = new Set();

        for (let [origen, aristas] of grafo.mapa.entries()) {
            const p1 = pixeles[origen];
            if (!p1) continue;

            for (let arista of aristas) {
                const destino = arista.destino;
                const p2 = pixeles[destino];
                if (!p2) continue;

                let clave = origen < destino ? `${origen}-${destino}` : `${destino}-${origen}`;
                if (!conexionesDibujadas.has(clave)) {
                    conexionesDibujadas.add(clave);
                    if (!arista.bloqueado) {
                        ctx.strokeStyle = "rgba(40, 40, 40, 0.7)";
                        ctx.beginPath();
                        ctx.moveTo(p1.x, p1.y);
                        ctx.lineTo(p2.x, p2.y);
                        ctx.stroke();
                    }
                }
            }
        }

        // 3. Enemigos Bloqueados (Línea Roja y Badge)
        const bloqueados = grafo.getCaminosBloqueados();
        for (let [origen, destino] of bloqueados) {
            const p1 = pixeles[origen];
            const p2 = pixeles[destino];
            if (p1 && p2) {
                ctx.lineWidth = 5;
                ctx.strokeStyle = "#e63946";
                ctx.beginPath();
                ctx.moveTo(p1.x, p1.y);
                ctx.lineTo(p2.x, p2.y);
                ctx.stroke();

                const midX = (p1.x + p2.x) / 2;
                const midY = (p1.y + p2.y) / 2;

                ctx.fillStyle = "#d62839";
                ctx.beginPath();
                ctx.arc(midX, midY, 12, 0, 2 * Math.PI);
                ctx.fill();
                ctx.strokeStyle = "#ffffff";
                ctx.lineWidth = 2;
                ctx.stroke();

                dibujarTextoConSombra("ENEMIGO", midX - 22, midY + 22, "#e63946", "#000000", "bold 11px sans-serif");
            }
        }

        // 4. Ruta Dorada Dijkstra
        if (rutaActual && rutaActual.length > 1) {
            ctx.lineWidth = 6;
            ctx.strokeStyle = "#e6af2e";
            ctx.beginPath();
            for (let i = 0; i < rutaActual.length - 1; i++) {
                const p1 = pixeles[rutaActual[i]];
                const p2 = pixeles[rutaActual[i + 1]];
                if (p1 && p2) {
                    if (i === 0) ctx.moveTo(p1.x, p1.y);
                    ctx.lineTo(p2.x, p2.y);
                }
            }
            ctx.stroke();
        }

        // 5. Nodos Principales
        for (let nombre in posicionesRelativas) {
            if (grafo.esPuntoDePaso(nombre)) continue;

            const p = pixeles[nombre];
            if (!p) continue;

            const esDeRuta = rutaActual && rutaActual.includes(nombre);
            const esInicio = rutaActual && rutaActual.length > 0 && rutaActual[0] === nombre;
            const esFin = rutaActual && rutaActual.length > 0 && rutaActual[rutaActual.length - 1] === nombre;

            if (esInicio) ctx.fillStyle = "#2a9d8f";
            else if (esFin) ctx.fillStyle = "#e63946";
            else if (esDeRuta) ctx.fillStyle = "#e6af2e";
            else ctx.fillStyle = "#3a4556";

            const radio = esDeRuta ? 11 : 8;
            ctx.beginPath();
            ctx.arc(p.x, p.y, radio, 0, 2 * Math.PI);
            ctx.fill();

            ctx.strokeStyle = "#ffffff";
            ctx.lineWidth = 2;
            ctx.stroke();

            dibujarTextoConSombra(nombre, p.x - 20, p.y - 12, "#ffffff", "#000000", "bold 12px sans-serif");
        }
    }

    function dibujarTextoConSombra(texto, x, y, colorTexto, colorSombra, font) {
        ctx.font = font;
        ctx.fillStyle = colorSombra;
        ctx.fillText(texto, x + 1, y + 1);
        ctx.fillText(texto, x - 1, y - 1);
        ctx.fillText(texto, x + 1, y - 1);
        ctx.fillText(texto, x - 1, y + 1);

        ctx.fillStyle = colorTexto;
        ctx.fillText(texto, x, y);
    }

    window.addEventListener("resize", renderizarMapa);
});
