package busquedaSoluciones5x5;

import java.util.Comparator;

public class NodePriorityComparator implements Comparator<Nodo> {
    @Override
    public int compare(Nodo x, Nodo y) {
        return Integer.compare(x.getCostoTotal(), y.getCostoTotal());
    }
}