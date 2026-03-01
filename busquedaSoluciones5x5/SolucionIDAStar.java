package busquedaSoluciones5x5;
import java.util.*;

public class SolucionIDAStar {

    private String estadoInicial;
    private String estadoFinal;
    private String tipoHeuristica;

    private List<String> camino = new ArrayList<>();

    private int nodosExpandidos = 0;
    private int nodosVisitados = 0;
    private long tiempoEjecucion = 0;
    private int longitudSolucion = 0;

    private static final int FOUND = -1;

    public SolucionIDAStar(String inicial, String goal, String heuristica) {
        this.estadoInicial = inicial;
        this.estadoFinal = goal;
        this.tipoHeuristica = heuristica;
    }

    public void solucionar() {

        long inicio = System.currentTimeMillis();

        if (!NodeUtil.esEstadoValido(estadoInicial) ||
            !NodeUtil.esEstadoValido(estadoFinal)) {
            System.out.println("Error: Estados invalidos.");
            return;
        }

        int limite = calcularHeuristica(estadoInicial);
        Nodo raiz = new Nodo(estadoInicial);

        while (true) {

            Set<String> visitadosEnCamino = new HashSet<>();
            visitadosEnCamino.add(estadoInicial);

            int resultado = dfs(raiz, 0, limite, visitadosEnCamino);

            if (resultado == FOUND) {
                tiempoEjecucion = System.currentTimeMillis() - inicio;
                longitudSolucion = camino.size() - 1;
                imprimirResultado();
                return;
            }

            if (resultado == Integer.MAX_VALUE) {
                System.out.println("No se encontro solucion.");
                return;
            }

            limite = resultado;
        }
    }

    public void resolverSinImprimir() {

        long inicio = System.currentTimeMillis();

        nodosVisitados = 0;
        nodosExpandidos = 0;
        camino.clear();
        longitudSolucion = 0;

        if (!NodeUtil.esEstadoValido(estadoInicial) ||
            !NodeUtil.esEstadoValido(estadoFinal)) {
            nodosExpandidos = -1;
            nodosVisitados = -1;
            longitudSolucion = -1;
            tiempoEjecucion = -1;
            return;
        }

        int limite = calcularHeuristica(estadoInicial);
        Nodo raiz = new Nodo(estadoInicial);

        while (true) {

            Set<String> visitadosEnCamino = new HashSet<>();
            visitadosEnCamino.add(estadoInicial);

            int resultado = dfs(raiz, 0, limite, visitadosEnCamino);

            if (resultado == FOUND) {
                tiempoEjecucion = System.currentTimeMillis() - inicio;
                longitudSolucion = camino.size() - 1;
                return;
            }

            if (resultado == Integer.MAX_VALUE) {
                nodosExpandidos = -1;
                nodosVisitados = -1;
                longitudSolucion = -1;
                tiempoEjecucion = -1;
                return;
            }

            limite = resultado;
        }
    }

    private int dfs(Nodo actual, int g, int limite, Set<String> visitados) {

        int h = calcularHeuristica(actual.getState());
        int f = g + h;

        if (f > limite)
            return f;

        if (actual.getState().equals(estadoFinal)) {
            reconstruirCamino(actual);
            return FOUND;
        }

        int min = Integer.MAX_VALUE;

        nodosExpandidos++;

        for (String sucesor : NodeUtil.getSuccessors(actual.getState())) {

            // Evitar regresar al padre (movimiento inverso inmediato)
            if (actual.getParent() != null &&
                sucesor.equals(actual.getParent().getState())) {
                continue;
            }

            if (!visitados.contains(sucesor)) {

                nodosVisitados++;

                Nodo hijo = new Nodo(sucesor, g + 1, actual);

                visitados.add(sucesor);

                int resultado = dfs(hijo, g + 1, limite, visitados);

                if (resultado == FOUND)
                    return FOUND;

                if (resultado < min)
                    min = resultado;

                visitados.remove(sucesor);
            }
        }

        return min;
    }

    private int calcularHeuristica(String estado) {

        if (tipoHeuristica.equals("Manhattan"))
            return Heuristica.distanciaManhattan(estado);
        else
            return Heuristica.manhattanConConflictoLineal(estado);
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

    private void imprimirResultado() {

        System.out.println("Solucion encontrada con IDA*");
        System.out.println("Heuristica: " + tipoHeuristica);
        System.out.println("Nodos expandidos: " + nodosExpandidos);
        System.out.println("Nodos visitados: " + nodosVisitados);
        System.out.println("Longitud solucion: " + longitudSolucion);
        System.out.println("Tiempo: " + tiempoEjecucion + " ms");

        if (camino.size() <= 20)
            imprimirSolucion();
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