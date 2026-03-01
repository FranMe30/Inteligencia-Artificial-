package busquedaSoluciones3x3;

import java.util.Comparator;

public class NodePriorityComparator implements Comparator<Nodo> {
 
    @Override
    public int compare(Nodo x, Nodo y) {
        if (x.getTotalCost() < y.getTotalCost()) {
            return -1;
        }
        if (x.getTotalCost() > y.getTotalCost()) {
            return 1;
        }
        return 0;
    }
}