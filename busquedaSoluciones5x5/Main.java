package busquedaSoluciones5x5;

import java.util.*;

public class Main {
    final static private String ESTADO_FINAL = "123456789ABCDEFGHIJKLMNO0";
    
    // Estados de prueba (resolubles)
    final static private String ESTADO_INICIAL_1 = "123456789ABCDEFGHIJK0LMNO"; // 1 movimiento
    final static private String ESTADO_INICIAL_2 = "123406789ABCD5EFGHIJKLMNO"; 
    final static private String ESTADO_INICIAL_3 = "1A23456789BCDEFGHIJKLMNO0"; 
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("       PUZZLE 24 (5x5) - COMPARATIVA       ");
        System.out.println("==========================================");
        System.out.println("El '0' representa el espacio vacio.\n");
        
        // Menu de opciones
        Scanner scanner = new Scanner(System.in);
        System.out.println("Seleccione una opcion:");
        System.out.println("1. Usar estado inicial predeterminado");
        System.out.println("2. Generar estado aleatorio");
        System.out.println("3. Ingresar estado manual");
        System.out.print("Opcion: ");
        
        int opcion = scanner.nextInt();
        scanner.nextLine(); // consumir nueva linea
        
        String estadoInicial = "";
        boolean mostrarCamino = false;
        
        switch (opcion) {
            case 1:
                estadoInicial = seleccionarEstadoPredeterminado(scanner);
                mostrarCamino = true;
                break;
            case 2:
                estadoInicial = generarEstadoAleatorioResoluble();
                mostrarCamino = false; // Muestra la tabla
                break;
            case 3:
                estadoInicial = ingresarEstadoManual(scanner);
                mostrarCamino = true;
                break;
            default:
                estadoInicial = ESTADO_INICIAL_1;
                mostrarCamino = true;
        }
        
        System.out.println("\nEstado inicial seleccionado:");
        imprimirEstado(estadoInicial);
        
        System.out.println("\nEstado final:");
        imprimirEstado(ESTADO_FINAL);
        
        if (mostrarCamino) {
            compararAlgoritmos(estadoInicial);
        } else {
            comparativaCompleta(estadoInicial);
        }
        
        scanner.close();
    }
    
    private static String seleccionarEstadoPredeterminado(Scanner scanner) {
        System.out.println("\nEstados disponibles:");
        System.out.println("1. Facil (1 movimiento)");
        System.out.println("2. Medio (puzzle revuelto)");
        System.out.println("3. Dificil (mas complejo)");
        System.out.print("Seleccione: ");
        
        int opcion = scanner.nextInt();
        switch (opcion) {
            case 1: return ESTADO_INICIAL_1;
            case 2: return ESTADO_INICIAL_2;
            case 3: return ESTADO_INICIAL_3;
            default: return ESTADO_INICIAL_1;
        }
    }
    
    private static String generarEstadoAleatorioResoluble() {
        System.out.println("\nGenerando estado aleatorio resoluble...");
        String estado = ESTADO_FINAL;
        Random rand = new Random();
        
        // Hacer movimientos aleatorios para generar un estado resoluble
        for (int i = 0; i < 30; i++) {
            List<String> sucesores = NodeUtil.getSuccessors(estado);
            estado = sucesores.get(rand.nextInt(sucesores.size()));
        }
        
        return estado;
    }
    
    private static String ingresarEstadoManual(Scanner scanner) {
        System.out.println("\nIngrese el estado (25 caracteres):");
        System.out.println("Use digitos 0-9 y letras A-O (0 para espacio vacio)");
        System.out.print("Estado: ");
        
        String estado = scanner.nextLine().toUpperCase().replaceAll("\\s", "");
        
        if (estado.length() != 25) {
            System.out.println("Estado invalido. Usando estado por defecto.");
            return ESTADO_INICIAL_1;
        }
        
        return estado;
    }
    
    private static void compararAlgoritmos(String estadoInicial) {

        System.out.println("\n==========================================");
        System.out.println("     ALGORITMOS NO INFORMADOS              ");
        System.out.println("==========================================\n");

        System.out.println("BFS:");
        Solucion bfs = new Solucion(estadoInicial, "BFS", ESTADO_FINAL);
        bfs.solucionar();

        System.out.println("\nDFS (limite):");
        Solucion dfs = new Solucion(estadoInicial, "DFS", ESTADO_FINAL);
        dfs.solucionar();

        System.out.println("\nUCS:");
        Solucion ucs = new Solucion(estadoInicial, "UCS", ESTADO_FINAL);
        ucs.solucionar();

        System.out.println("\n==========================================");
        System.out.println("     ALGORITMOS INFORMADOS                ");
        System.out.println("==========================================\n");

        System.out.println("A* Manhattan:");
        SolucionAStar astarM = new SolucionAStar(estadoInicial, ESTADO_FINAL, "Manhattan");
        astarM.solucionar();

        System.out.println("\nA* Conflicto Lineal:");
        SolucionAStar astarC = new SolucionAStar(estadoInicial, ESTADO_FINAL, "ConflictoLineal");
        astarC.solucionar();

        System.out.println("\nIDA* Manhattan:");
        SolucionIDAStar idaM = new SolucionIDAStar(estadoInicial, ESTADO_FINAL, "Manhattan");
        idaM.solucionar();

        System.out.println("\nIDA* Conflicto Lineal:");
        SolucionIDAStar idaC = new SolucionIDAStar(estadoInicial, ESTADO_FINAL, "ConflictoLineal");
        idaC.solucionar();
    }
    
    private static void comparativaCompleta(String estadoInicial) {
        System.out.println("\n=========================================================================");
        System.out.println("                             TABLA COMPARATIVA                             ");
        System.out.println("=========================================================================");
        System.out.println("Algoritmo             | Nodos Expand. | Nodos Visit. | Tiempo(ms) | Longitud");
        System.out.println("-------------------------------------------------------------------------");
        
        // BFS
        Solucion bfs = new Solucion(estadoInicial, "BFS", ESTADO_FINAL);
        bfs.resolverSinImprimir();
        System.out.printf("BFS                    | %-14d | %-12d | %-10d | %-9d%n", 
            bfs.getNodosExpandidos(), bfs.getNodosVisitados(), 
            bfs.getTiempoEjecucion(), bfs.getLongitudSolucion());
        
        // DFS
        Solucion dfs = new Solucion(estadoInicial, "DFS", ESTADO_FINAL);
        dfs.resolverSinImprimir();
        System.out.printf("DFS (limite)           | %-14d | %-12d | %-10d | %-9d%n", 
            dfs.getNodosExpandidos(), dfs.getNodosVisitados(), 
            dfs.getTiempoEjecucion(), dfs.getLongitudSolucion());
        
        // UCS
        Solucion ucs = new Solucion(estadoInicial, "UCS", ESTADO_FINAL);
        ucs.resolverSinImprimir();
        System.out.printf("UCS                    | %-14d | %-12d | %-10d | %-9d%n", 
            ucs.getNodosExpandidos(), ucs.getNodosVisitados(), 
            ucs.getTiempoEjecucion(), ucs.getLongitudSolucion());
        
        // A* Manhattan
        SolucionAStar astarM = new SolucionAStar(estadoInicial, ESTADO_FINAL, "Manhattan");
        astarM.resolverSinImprimir();
        System.out.printf("A* (Manhattan)         | %-14d | %-12d | %-10d | %-9d%n", 
            astarM.getNodosExpandidos(), astarM.getNodosVisitados(), 
            astarM.getTiempoEjecucion(), astarM.getLongitudSolucion());
        
        // A* Conflicto Lineal
        SolucionAStar astarC = new SolucionAStar(estadoInicial, ESTADO_FINAL, "ConflictoLineal");
        astarC.resolverSinImprimir();
        System.out.printf("A* (Conflicto Lineal)  | %-14d | %-12d | %-10d | %-9d%n", 
            astarC.getNodosExpandidos(), astarC.getNodosVisitados(), 
            astarC.getTiempoEjecucion(), astarC.getLongitudSolucion());
        
     // IDA* Manhattan
        SolucionIDAStar idaM = new SolucionIDAStar(estadoInicial, ESTADO_FINAL, "Manhattan");
        idaM.resolverSinImprimir();
        System.out.printf("IDA* (Manhattan)       | %-14d | %-12d | %-10d | %-9d%n", 
            idaM.getNodosExpandidos(), idaM.getNodosVisitados(), 
            idaM.getTiempoEjecucion(), idaM.getLongitudSolucion());

        // IDA* Conflicto Lineal
        SolucionIDAStar idaC = new SolucionIDAStar(estadoInicial, ESTADO_FINAL, "ConflictoLineal");
        idaC.resolverSinImprimir();
        System.out.printf("IDA* (Conflicto Lineal)| %-14d | %-12d | %-10d | %-9d%n", 
            idaC.getNodosExpandidos(), idaC.getNodosVisitados(), 
            idaC.getTiempoEjecucion(), idaC.getLongitudSolucion());
        
        System.out.println("=========================================================================");
        
     // Analisis
        System.out.println("\nANALISIS DE RENDIMIENTO:");
        System.out.println("- A* expande muchos menos nodos que BFS/DFS/UCS");
        System.out.println("- Conflicto Lineal suele mejorar a Manhattan en puzzles complejos");
        System.out.println("- BFS garantiza solucion optima pero explota el espacio de estados");
        System.out.println("- DFS no encontro solucion (valor -1) porque con limite de profundidad de 25 ");
        System.out.println("se pierde en ramas sin salida, demostrando que no es adecuado para puzzles grandes ");
        
        System.out.println("\nJUSTIFICACION DEL ALGORITMO SUGERIDO:");
        System.out.println("Debido a la explosion combinatoria del 24-Puzzle (5x5),");
        System.out.println("que posee aproximadamente 1.55 x 10^25 configuraciones posibles,");
        System.out.println("el uso de algoritmos que almacenan grandes cantidades de nodos");
        System.out.println("en memoria, como A*, se vuelve inviable en instancias complejas.");

        System.out.println("\nA* mantiene estructuras OPEN y CLOSED,");
        System.out.println("por lo que su complejidad en memoria es exponencial O(b^d),");
        System.out.println("lo cual puede provocar agotamiento de memoria en tableros grandes.");

        System.out.println("\nEn contraste, IDA* utiliza profundizacion iterativa basada en la");
        System.out.println("funcion f(n) = g(n) + h(n), pero solo almacena el camino actual,");
        System.out.println("por lo que su consumo de memoria es lineal O(d).");

        System.out.println("\nPor esta razon, IDA* es superior a A* en terminos de gestion");
        System.out.println("de memoria para el espacio de estados del 5x5,");
        System.out.println("siendo el algoritmo mas adecuado para este problema.");
    }
    
    private static void imprimirEstado(String estado) {
        for (int i = 0; i < 25; i++) {
            System.out.print(estado.charAt(i) + " ");
            if ((i + 1) % 5 == 0)
                System.out.println();
        }
    }
}