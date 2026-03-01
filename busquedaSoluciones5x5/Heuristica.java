package busquedaSoluciones5x5;

import java.util.*;

public class Heuristica {
    
    // Mapa de posiciones objetivo para cada ficha
    private static final Map<Character, int[]> posicionesObjetivo = new HashMap<>();
    
    static {
        // Inicializar posiciones objetivo (formato: [fila, columna])
        String estadoFinal = "123456789ABCDEFGHIJKLMNO0";
        for (int i = 0; i < 25; i++) {
            char ficha = estadoFinal.charAt(i);
            posicionesObjetivo.put(ficha, new int[]{i / 5, i % 5});
        }
    }
    
    /**
     * Distancia de Manhattan: suma de distancias de cada ficha a su posición objetivo
     */
    public static int distanciaManhattan(String estado) {
        int distanciaTotal = 0;
        
        for (int i = 0; i < 25; i++) {
            char ficha = estado.charAt(i);
            if (ficha == '0') continue; // Espacio vacío no cuenta
            
            int filaActual = i / 5;
            int colActual = i % 5;
            
            int[] posObj = posicionesObjetivo.get(ficha);
            int filaObj = posObj[0];
            int colObj = posObj[1];
            
            distanciaTotal += Math.abs(filaActual - filaObj) + Math.abs(colActual - colObj);
        }
        
        return distanciaTotal;
    }
    
    /**
     * Distancia de Manhattan con Conflicto Lineal
     * Añade penalizaciones por fichas que están en su fila/columna correcta pero en orden inverso
     */
    public static int manhattanConConflictoLineal(String estado) {
        int distancia = distanciaManhattan(estado);
        
        // Penalizaciones por conflictos lineales (2 puntos por cada conflicto)
        distancia += 2 * (conflictosLinealesFilas(estado) + conflictosLinealesColumnas(estado));
        
        return distancia;
    }
    
    /**
     * Detecta conflictos lineales en filas
     */
    private static int conflictosLinealesFilas(String estado) {
        int conflictos = 0;
        
        for (int fila = 0; fila < 5; fila++) {
            List<Integer> fichasEnFila = new ArrayList<>();
            List<Integer> posicionesObjetivoFila = new ArrayList<>();
            
            // Identificar fichas que pertenecen a esta fila en el estado objetivo
            for (int col = 0; col < 5; col++) {
                int indice = fila * 5 + col;
                char ficha = estado.charAt(indice);
                
                if (ficha == '0') continue;
                
                int[] posObj = posicionesObjetivo.get(ficha);
                if (posObj[0] == fila) { // La ficha debería estar en esta fila
                    fichasEnFila.add(col);
                    posicionesObjetivoFila.add(posObj[1]);
                }
            }
            
            // Contar conflictos (pares invertidos)
            for (int i = 0; i < fichasEnFila.size(); i++) {
                for (int j = i + 1; j < fichasEnFila.size(); j++) {
                    // Conflicto si la ficha que debería estar a la izquierda está a la derecha
                    if ((fichasEnFila.get(i) < fichasEnFila.get(j) && 
                         posicionesObjetivoFila.get(i) > posicionesObjetivoFila.get(j)) ||
                        (fichasEnFila.get(i) > fichasEnFila.get(j) && 
                         posicionesObjetivoFila.get(i) < posicionesObjetivoFila.get(j))) {
                        conflictos++;
                    }
                }
            }
        }
        
        return conflictos;
    }
    
    /**
     * Detecta conflictos lineales en columnas
     */
    private static int conflictosLinealesColumnas(String estado) {
        int conflictos = 0;
        
        for (int col = 0; col < 5; col++) {
            List<Integer> fichasEnColumna = new ArrayList<>();
            List<Integer> posicionesObjetivoColumna = new ArrayList<>();
            
            // Identificar fichas que pertenecen a esta columna en el estado objetivo
            for (int fila = 0; fila < 5; fila++) {
                int indice = fila * 5 + col;
                char ficha = estado.charAt(indice);
                
                if (ficha == '0') continue;
                
                int[] posObj = posicionesObjetivo.get(ficha);
                if (posObj[1] == col) { // La ficha debería estar en esta columna
                    fichasEnColumna.add(fila);
                    posicionesObjetivoColumna.add(posObj[0]);
                }
            }
            
            // Contar conflictos
            for (int i = 0; i < fichasEnColumna.size(); i++) {
                for (int j = i + 1; j < fichasEnColumna.size(); j++) {
                    if ((fichasEnColumna.get(i) < fichasEnColumna.get(j) && 
                         posicionesObjetivoColumna.get(i) > posicionesObjetivoColumna.get(j)) ||
                        (fichasEnColumna.get(i) > fichasEnColumna.get(j) && 
                         posicionesObjetivoColumna.get(i) < posicionesObjetivoColumna.get(j))) {
                        conflictos++;
                    }
                }
            }
        }
        
        return conflictos;
    }
}