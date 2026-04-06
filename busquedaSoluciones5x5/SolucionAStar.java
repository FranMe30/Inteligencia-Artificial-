package busquedaSoluciones5x5;

import java.util.*;

public class SolucionAStar {
    private String estadoInicial;
    private String estadoFinal;
    private List<String> camino = new ArrayList<>();
    private String tipoHeuristica;
    private int nodosVisitados = 0;
    private long tiempoEjecucion = 0;
    private Nodo nodoFinalEncontrado;
    
    private int nodosExpandidos = 0;
    private int longitudSolucion = 0;
    
    public SolucionAStar(String inicial, String goal, String heuristica) {
        this.estadoInicial = inicial;
        this.estadoFinal = goal;
        this.tipoHeuristica = heuristica;
    }
    
    public void solucionar() {
        long inicio = System.currentTimeMillis();
        
        if (!NodeUtil.esEstadoValido(estadoInicial) || !NodeUtil.esEstadoValido(estadoFinal)) {
            System.out.println("Error: Estados invalidos.");
            return;
        }
        
        boolean encontrado = aStar();
        tiempoEjecucion = System.currentTimeMillis() - inicio;
        
        if (encontrado) {
            System.out.println("Solucion encontrada!");
            System.out.println("Heuristica: " + tipoHeuristica);
            System.out.println("Nodos expandidos: " + nodosExpandidos);
            System.out.println("Nodos visitados: " + nodosVisitados);
            System.out.println("Longitud solucion: " + longitudSolucion);
            System.out.println("Tiempo: " + tiempoEjecucion + " ms");
            
            if (camino.size() <= 20) {
                imprimirSolucion();
            }
        } else {
            System.out.println("No se encontro solucion.");
        }
    }
    
    public void resolverSinImprimir() {
        long inicio = System.currentTimeMillis();
        
        if (!NodeUtil.esEstadoValido(estadoInicial) || !NodeUtil.esEstadoValido(estadoFinal)) {
            nodosExpandidos = -1;
            nodosVisitados = -1;
            longitudSolucion = -1;
            tiempoEjecucion = -1;
            return;
        }
        
        // Reiniciar variables
        nodosVisitados = 0;
        nodosExpandidos = 0;
        camino.clear();
        longitudSolucion = 0;
        nodoFinalEncontrado = null;
        
        boolean encontrado = aStar();
        tiempoEjecucion = System.currentTimeMillis() - inicio;
        
        if (!encontrado) {
            nodosExpandidos = -1;
            nodosVisitados = -1;
            longitudSolucion = -1;
            tiempoEjecucion = -1;
        }
    }
    
    private boolean aStar() {
        PriorityQueue<Nodo> cola = new PriorityQueue<>(new NodePriorityComparator());
        Map<String, Integer> costoAcumuladoMap = new HashMap<>();
        Set<String> expandidos = new HashSet<>();
        
        Nodo raiz = new Nodo(estadoInicial);
        raiz.setHeuristica(calcularHeuristica(estadoInicial));
        
        cola.add(raiz);
        costoAcumuladoMap.put(estadoInicial, 0);
        nodosVisitados = 1;
        nodosExpandidos = 0;
        
        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();
            
            if (expandidos.contains(actual.getState())) {
                continue;
            }
            
            expandidos.add(actual.getState());
            nodosExpandidos++;
            
            if (actual.getState().equals(estadoFinal)) {
                nodoFinalEncontrado = actual;
                reconstruirCamino(actual);
                longitudSolucion = camino.size() - 1;
                return true;
            }
            
            for (String sucesor : NodeUtil.getSuccessors(actual.getState())) {
                int nuevoCostoAcumulado = actual.getCostoAcumulado() + 1;
                
                if (!costoAcumuladoMap.containsKey(sucesor) || 
                    nuevoCostoAcumulado < costoAcumuladoMap.get(sucesor)) {
                    
                    costoAcumuladoMap.put(sucesor, nuevoCostoAcumulado);
                    
                    Nodo hijo = new Nodo(sucesor, nuevoCostoAcumulado, actual);
                    hijo.setHeuristica(calcularHeuristica(sucesor));
                    
                    cola.add(hijo);
                    nodosVisitados++;
                }
            }
        }
        
        return false;
    }
    
    private int calcularHeuristica(String estado) {
        if (tipoHeuristica.equals("Manhattan")) {
            return Heuristica.distanciaManhattan(estado);
        } else {
            return Heuristica.manhattanConConflictoLineal(estado);
        }
    }
    
    private void reconstruirCamino(Nodo nodoFinal) {
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
    public int getLongitudSolucion() { return longitudSolucion; }
}