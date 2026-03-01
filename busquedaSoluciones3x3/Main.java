package busquedaSoluciones3x3;

public class Main {
    final static private String HARD = "567408321";
    final static private String GOAL_STATE = "123804765";
    
    public static void main(String[] args) {
        String estadoInicial = HARD; 
        
        System.out.println("=== COMPARACION DE ALGORITMOS ===");
        System.out.println("Estado inicial: " + estadoInicial);
        System.out.println("Estado final: " + GOAL_STATE);
        System.out.println("=" .repeat(50));
        
        // Ejecutar BFS
        System.out.println("\nBUSQUEDA EN ANCHURA (BFS):");
        Solucion bfs = new Solucion(estadoInicial, "BFS", GOAL_STATE);
        bfs.solucionar();
        
        // Ejecutar DFS
        System.out.println("\nBUSQUEDA EN PROFUNDIDAD (DFS):");
        Solucion dfs = new Solucion(estadoInicial, "DFS", GOAL_STATE);
        dfs.solucionar();
        
        // Ejecutar UCS
        System.out.println("\nBUSQUEDA DE COSTO UNIFORME (UCS):");
        Solucion ucs = new Solucion(estadoInicial, "UCS", GOAL_STATE);
        ucs.solucionar();
    }
}