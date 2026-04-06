package busquedaSoluciones5x5;

import java.util.*;

public class Solucion {

    String estadoActual;
    String estadoFinal;
    private List<String> camino = new ArrayList<>();
    private String tipoBusqueda;
    private int nodosVisitados = 0;
    private int nodosExpandidos = 0;
    private long tiempoEjecucion = 0;
    private Nodo nodoFinalEncontrado;
    private static final int LIMITE_PROFUNDIDAD = 25;

    public Solucion(String e, String tipo, String goal) {
        this.estadoActual = e;
        this.tipoBusqueda = tipo;
        this.estadoFinal = goal;
    }

    public void solucionar() {
        long inicio = System.currentTimeMillis();
        boolean encontrado = false;

        if (!NodeUtil.esEstadoValido(estadoActual) || !NodeUtil.esEstadoValido(estadoFinal)) {
            System.out.println("Error: Estados invalidos.");
            return;
        }

        switch (tipoBusqueda) {
            case "BFS":
                encontrado = bfs();
                break;
            case "DFS":
                encontrado = dfsConLimite();
                break;
            case "UCS":
                encontrado = uniformCostSearch();
                break;
        }

        tiempoEjecucion = System.currentTimeMillis() - inicio;

        if (encontrado) {
            System.out.println("Solucion encontrada!");
            System.out.println("Nodos expandidos: " + nodosExpandidos);
            System.out.println("Nodos visitados: " + nodosVisitados);
            System.out.println("Pasos: " + (camino.size() - 1));
            if (tipoBusqueda.equals("UCS"))
                System.out.println("Costo total: " + nodoFinalEncontrado.getTotalCost());
            System.out.println("Tiempo: " + tiempoEjecucion + " ms");
            
            if (camino.size() <= 20) {
                imprimirSolucion();
            } else {
                System.out.println("(Solucion muy larga para mostrar)");
            }
        } else {
            System.out.println("No se encontro solucion en el limite establecido.");
        }
    }
    
    public void resolverSinImprimir() {
        long inicio = System.currentTimeMillis();
        boolean encontrado = false;

        if (!NodeUtil.esEstadoValido(estadoActual) || !NodeUtil.esEstadoValido(estadoFinal)) {
            nodosExpandidos = -1;
            nodosVisitados = -1;
            tiempoEjecucion = -1;
            return;
        }

        // Reiniciar variables
        nodosVisitados = 0;
        nodosExpandidos = 0;
        camino.clear();
        nodoFinalEncontrado = null;

        switch (tipoBusqueda) {
            case "BFS":
                encontrado = bfs();
                break;
            case "DFS":
                encontrado = dfsConLimite();
                break;
            case "UCS":
                encontrado = uniformCostSearch();
                break;
        }

        tiempoEjecucion = System.currentTimeMillis() - inicio;
        
        if (!encontrado) {
            nodosExpandidos = -1;
            nodosVisitados = -1;
            tiempoEjecucion = -1;
        }
    }

    private boolean bfs() {
        Queue<Nodo> cola = new LinkedList<>();
        Set<String> visitados = new HashSet<>();

        Nodo raiz = new Nodo(estadoActual);
        cola.add(raiz);
        visitados.add(estadoActual);
        nodosVisitados = 1;
        nodosExpandidos = 0;

        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();
            nodosExpandidos++;

            if (actual.getState().equals(estadoFinal)) {
                reconstruirCaminoDesdeNodo(actual);
                return true;
            }

            for (String s : NodeUtil.getSuccessors(actual.getState())) {
                if (!visitados.contains(s)) {
                    visitados.add(s);
                    Nodo hijo = new Nodo(s, actual.nivel + 1, actual);
                    hijo.setTotalCost(actual.nivel + 1);
                    cola.add(hijo);
                    nodosVisitados++;
                }
            }
        }
        return false;
    }

    private boolean dfsConLimite() {
        Stack<Nodo> pila = new Stack<>();
        Set<String> visitados = new HashSet<>();

        Nodo raiz = new Nodo(estadoActual);
        pila.push(raiz);
        visitados.add(estadoActual);
        nodosVisitados = 1;
        nodosExpandidos = 0;

        while (!pila.isEmpty()) {
            Nodo actual = pila.pop();
            nodosExpandidos++;

            if (actual.nivel > LIMITE_PROFUNDIDAD) {
                continue;
            }

            if (actual.getState().equals(estadoFinal)) {
                reconstruirCaminoDesdeNodo(actual);
                return true;
            }

            List<String> sucesores = NodeUtil.getSuccessors(actual.getState());
            for (int i = sucesores.size() - 1; i >= 0; i--) {
                String s = sucesores.get(i);
                if (!visitados.contains(s)) {
                    visitados.add(s);
                    Nodo hijo = new Nodo(s, actual.nivel + 1, actual);
                    hijo.setTotalCost(actual.nivel + 1);
                    pila.push(hijo);
                    nodosVisitados++;
                }
            }
        }
        return false;
    }

    private boolean uniformCostSearch() {
        PriorityQueue<Nodo> cola = new PriorityQueue<>(new NodePriorityComparator());
        Map<String, Integer> costos = new HashMap<>();

        Nodo raiz = new Nodo(estadoActual);
        raiz.setTotalCost(0);

        cola.add(raiz);
        costos.put(estadoActual, 0);
        nodosVisitados = 1;
        nodosExpandidos = 0;

        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();
            
            if (costos.containsKey(actual.getState()) && costos.get(actual.getState()) < actual.getTotalCost()) {
                continue;
            }
            
            nodosExpandidos++;

            if (actual.getState().equals(estadoFinal)) {
                nodoFinalEncontrado = actual;
                reconstruirCaminoDesdeNodo(actual);
                return true;
            }

            for (String s : NodeUtil.getSuccessors(actual.getState())) {
                int nuevoCosto = actual.getTotalCost() + 1;

                if (!costos.containsKey(s) || nuevoCosto < costos.get(s)) {
                    costos.put(s, nuevoCosto);

                    Nodo hijo = new Nodo(s, actual.nivel + 1, actual);
                    hijo.setTotalCost(nuevoCosto);

                    cola.add(hijo);
                    nodosVisitados++;
                }
            }
        }
        return false;
    }

    private void reconstruirCaminoDesdeNodo(Nodo nodoFinal) {
        camino.clear();
        Stack<String> pila = new Stack<>();

        Nodo actual = nodoFinal;
        while (actual != null) {
            pila.push(actual.getState());
            actual = actual.getParent();
        }

        while (!pila.isEmpty()) {
            camino.add(pila.pop());
        }
    }

    private void imprimirSolucion() {
        System.out.println("\nCamino de solucion:");
        for (int i = 0; i < camino.size(); i++) {
            System.out.println("Paso " + i + ":");
            imprimirEstado(camino.get(i));
            System.out.println();
        }
    }

    private void imprimirEstado(String estado) {
        for (int i = 0; i < 25; i++) {
            System.out.print(estado.charAt(i) + " ");
            if ((i + 1) % 5 == 0)
                System.out.println();
        }
    }
    
    public int getNodosExpandidos() { return nodosExpandidos; }
    public int getNodosVisitados() { return nodosVisitados; }
    public long getTiempoEjecucion() { return tiempoEjecucion; }
    public int getLongitudSolucion() { return camino.size() - 1; }
}