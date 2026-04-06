package busquedaSoluciones3x3;

import java.util.*;

public class Solucion {
    String estadoActual;
    String estadoFinal;
    private List<String> camino = new ArrayList<>();
    private String tipoBusqueda;
    private int nodosVisitados = 0;
    private long tiempoEjecucion = 0;
    private Nodo nodoFinalEncontrado; 
    
    private int[][] movimientos = {
        {1, 3}, {0, 2, 4}, {1, 5},
        {0, 4, 6}, {1, 3, 5, 7}, {2, 4, 8},
        {3, 7}, {4, 6, 8}, {5, 7}
    };
    
    public Solucion(String e, String tipo, String goal) {
        estadoActual = e;
        tipoBusqueda = tipo;
        estadoFinal = goal;
    }
    
    public void solucionar() {
        long inicio = System.currentTimeMillis();
        boolean encontrado;
        
        if (tipoBusqueda.equals("BFS")) {
            encontrado = bfs();
        } else if (tipoBusqueda.equals("DFS")) {
            encontrado = dfs();
        } else {
            encontrado = uniformCostSearch();
        }
        
        long fin = System.currentTimeMillis();
        tiempoEjecucion = fin - inicio;
        
        if (encontrado) {
            System.out.println("Solucion encontrada!");
            System.out.println("Estadisticas:");
            System.out.println("  • Nodos visitados: " + nodosVisitados);
            System.out.println("  • Pasos: " + (camino.size() - 1));
            if (tipoBusqueda.equals("UCS")) {
                System.out.println("  • Costo total: " + calcularCostoTotalUCS());
            }
            System.out.println("  • Tiempo: " + tiempoEjecucion + " ms");
            imprimirSolucion();
        } else {
            System.out.println("No se encontro solucion.");
        }
    }
    
    //Búsqueda en Anchura
    private boolean bfs() {
        Queue<Nodo> cola = new LinkedList<>();
        Set<String> visitados = new HashSet<>();
        
        Nodo raiz = new Nodo(estadoActual, 0, null);
        cola.add(raiz);
        visitados.add(estadoActual);
        nodosVisitados = 1;
        
        while (!cola.isEmpty()) {
            Nodo nodoActual = cola.poll();
            
            if (nodoActual.estado.equals(estadoFinal)) {
                reconstruirCaminoDesdeNodo(nodoActual);
                return true;
            }
            
            int posVacia = nodoActual.estado.indexOf('0'); 
            
            for (int destino : movimientos[posVacia]) {
                String nuevoEstado = intercambiar(nodoActual.estado, posVacia, destino);
                
                if (!visitados.contains(nuevoEstado)) {
                    visitados.add(nuevoEstado);
                    nodosVisitados++;
                    Nodo nodoHijo = new Nodo(nuevoEstado, nodoActual.nivel + 1, nodoActual);
                    cola.add(nodoHijo);
                }
            }
        }
        
        return false;
    }
    
    //Búsqueda en profundidad
    private boolean dfs() {
        Stack<Nodo> pila = new Stack<>();
        Set<String> visitados = new HashSet<>();
        
        Nodo raiz = new Nodo(estadoActual, 0, null);
        pila.push(raiz);
        visitados.add(estadoActual);
        nodosVisitados = 1;
        
        while (!pila.isEmpty()) {
            Nodo nodoActual = pila.pop();
            
            if (nodoActual.estado.equals(estadoFinal)) {
                reconstruirCaminoDesdeNodo(nodoActual);
                return true;
            }
            
            int posVacia = nodoActual.estado.indexOf('0');  
            
            int[] movs = movimientos[posVacia];
            for (int i = movs.length - 1; i >= 0; i--) {
                String nuevoEstado = intercambiar(nodoActual.estado, posVacia, movs[i]);
                
                if (!visitados.contains(nuevoEstado)) {
                    visitados.add(nuevoEstado);
                    nodosVisitados++;
                    Nodo nodoHijo = new Nodo(nuevoEstado, nodoActual.nivel + 1, nodoActual);
                    pila.push(nodoHijo);
                }
            }
        }
        
        return false;
    }
    
    // Búsqueda de Costo Uniforme 
    private boolean uniformCostSearch() {
        Set<String> stateSets = new HashSet<String>();
        int time = 0;
        
        // Crear nodo raíz
        Nodo node = new Nodo(estadoActual);
        node.setCost(0);
        
        // El comparador compara los valores de costo
        NodePriorityComparator nodePriorityComparator = new NodePriorityComparator();
        
        // Cola de prioridad ordenada por costo
        PriorityQueue<Nodo> nodePriorityQueue = new PriorityQueue<Nodo>(10, nodePriorityComparator);
        
        Nodo currentNode = node;
        nodePriorityQueue.add(currentNode);
        stateSets.add(currentNode.getState());
        nodosVisitados = 1;
        
        while (!nodePriorityQueue.isEmpty()) {
            currentNode = nodePriorityQueue.poll();
            time++;
            
            if (currentNode.getState().equals(estadoFinal)) {
                nodoFinalEncontrado = currentNode;
                reconstruirCaminoDesdeNodo(currentNode);
                return true;
            }
            
            stateSets.add(currentNode.getState()); // Visitados
            List<String> nodeSuccessors = NodeUtil.getSuccessors(currentNode.getState());
            
            for (String n : nodeSuccessors) {
                if (stateSets.contains(n))
                    continue;
                
                stateSets.add(n);
                Nodo child = new Nodo(n);
                currentNode.addChild(child);
                child.setParent(currentNode);
                
                // Calcular costo: cada movimiento cuesta 1
                int costoMovimiento = 1;
                child.setTotalCost(currentNode.getTotalCost() + costoMovimiento);
                
                nodePriorityQueue.add(child);
                nodosVisitados++;
            }
        }
        
        return false;
    }
    
    private void reconstruirCaminoDesdeNodo(Nodo nodoFinal) {
        camino.clear();
        
        Nodo actual = nodoFinal;
        Stack<String> pila = new Stack<>();
        
        while (actual != null) {
            pila.push(actual.estado);
            actual = actual.padre;
        }
        
        while (!pila.isEmpty()) {
            camino.add(pila.pop());
        }
    }
    
    private String intercambiar(String estado, int i, int j) {
        char[] chars = estado.toCharArray();
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
        return new String(chars);
    }
    
    private int calcularCostoTotalUCS() {
        if (nodoFinalEncontrado != null) {
            return nodoFinalEncontrado.getTotalCost();
        }
        return 0;
    }
    
    private void imprimirSolucion() {
        System.out.println("\nCamino de solucion (" + (camino.size() - 1) + " movimientos):");
        System.out.println("-" .repeat(40));
        
        // Mostrar solo primeros y últimos pasos si es muy largo
        if (camino.size() > 10) {
            for (int i = 0; i < 3; i++) {
                System.out.println("Paso " + i + ":");
                imprimirEstado(camino.get(i));
                System.out.println();
            }
            System.out.println("...\n");
            for (int i = camino.size() - 3; i < camino.size(); i++) {
                System.out.println("Paso " + i + ":");
                imprimirEstado(camino.get(i));
                System.out.println();
            }
        } else {
            for (int i = 0; i < camino.size(); i++) {
                System.out.println("Paso " + i + ":");
                imprimirEstado(camino.get(i));
                System.out.println();
            }
        }
    }
    
    private void imprimirEstado(String estado) {
        for (int i = 0; i < 9; i++) {
            System.out.print(estado.charAt(i) + " ");
            if ((i + 1) % 3 == 0) {
                System.out.println();
            }
        }
    }
}