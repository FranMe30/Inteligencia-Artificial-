package busquedaSoluciones3x3;

import java.util.*;

public class NodeUtil {
    private static int[][] movimientos = {
        {1, 3}, {0, 2, 4}, {1, 5},
        {0, 4, 6}, {1, 3, 5, 7}, {2, 4, 8},
        {3, 7}, {4, 6, 8}, {5, 7}
    };
    
    public static List<String> getSuccessors(String estado) {
        List<String> sucesores = new ArrayList<>();
        int posVacia = estado.indexOf('0');
        
        for (int destino : movimientos[posVacia]) {
            String nuevoEstado = intercambiar(estado, posVacia, destino);
            sucesores.add(nuevoEstado);
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
}