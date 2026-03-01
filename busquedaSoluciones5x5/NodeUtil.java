package busquedaSoluciones5x5;

import java.util.*;

public class NodeUtil {

    private static int[][] movimientos = new int[25][];

    static {
        for (int i = 0; i < 25; i++) {
            List<Integer> movs = new ArrayList<>();
            int fila = i / 5;
            int columna = i % 5;

            if (fila > 0) movs.add(i - 5);
            if (fila < 4) movs.add(i + 5);
            if (columna > 0) movs.add(i - 1);
            if (columna < 4) movs.add(i + 1);

            movimientos[i] = movs.stream().mapToInt(Integer::intValue).toArray();
        }
    }

    public static List<String> getSuccessors(String estado) {
        List<String> sucesores = new ArrayList<>();
        int posVacia = estado.indexOf('0');

        for (int destino : movimientos[posVacia]) {
            sucesores.add(intercambiar(estado, posVacia, destino));
        }

        return sucesores;
    }

    private static String intercambiar(String estado, int i, int j) {
        char[] chars = estado.toCharArray();
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
        return new String(chars);
    }

    public static boolean esEstadoValido(String estado) {
        if (estado == null || estado.length() != 25)
            return false;

        Set<Character> set = new HashSet<>();
        for (char c : estado.toCharArray()) {
            set.add(c);
        }

        return set.size() == 25 && estado.contains("0");
    }

    // =============================
    // HEURÍSTICAS
    // =============================

    public static int distanciaManhattan(String estado, String objetivo) {
        int distancia = 0;

        for (int i = 0; i < 25; i++) {
            char pieza = estado.charAt(i);
            if (pieza == '0') continue;

            int posObjetivo = objetivo.indexOf(pieza);

            int filaActual = i / 5;
            int colActual = i % 5;

            int filaObj = posObjetivo / 5;
            int colObj = posObjetivo % 5;

            distancia += Math.abs(filaActual - filaObj) +
                         Math.abs(colActual - colObj);
        }

        return distancia;
    }

    // Conflicto Lineal (extra)
    public static int conflictoLineal(String estado, String objetivo) {
        int conflicto = 0;

        // Filas
        for (int fila = 0; fila < 5; fila++) {
            for (int c1 = 0; c1 < 5; c1++) {
                for (int c2 = c1 + 1; c2 < 5; c2++) {

                    int i = fila * 5 + c1;
                    int j = fila * 5 + c2;

                    char p1 = estado.charAt(i);
                    char p2 = estado.charAt(j);

                    if (p1 == '0' || p2 == '0') continue;

                    int posObj1 = objetivo.indexOf(p1);
                    int posObj2 = objetivo.indexOf(p2);

                    int filaObj1 = posObj1 / 5;
                    int filaObj2 = posObj2 / 5;

                    if (filaObj1 == fila && filaObj2 == fila && posObj1 > posObj2) {
                        conflicto += 2;
                    }
                }
            }
        }

        return conflicto;
    }
}