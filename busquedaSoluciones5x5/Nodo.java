package busquedaSoluciones5x5;

import java.util.*;

public class Nodo {
    private String estado;
    int nivel;
    Nodo padre;
    private int costoAcumulado;  // g(n)
    private int heuristica;       // h(n)
    private int costoTotal;       // f(n) = g(n) + h(n)
    private List<Nodo> hijos;

    public Nodo(String estado, int nivel, Nodo padre) {
        this.estado = estado;
        this.nivel = nivel;
        this.padre = padre;
        this.costoAcumulado = nivel;
        this.costoTotal = nivel; // Por defecto, costoTotal = costoAcumulado
        this.hijos = new ArrayList<>();
    }

    public Nodo(String estado) {
        this(estado, 0, null);
    }

    public String getState() {
        return estado;
    }

    public int getCostoAcumulado() {
        return costoAcumulado;
    }

    public int getHeuristica() {
        return heuristica;
    }

    public void setHeuristica(int heuristica) {
        this.heuristica = heuristica;
        this.costoTotal = this.costoAcumulado + this.heuristica;
    }

    public int getCostoTotal() {
        return costoTotal;
    }
    
    // Métodos para compatibilidad con la clase Solucion original
    public int getTotalCost() {
        return costoTotal;
    }
    
    public void setTotalCost(int cost) {
        this.costoTotal = cost;
        this.costoAcumulado = cost; // En BFS/DFS/UCS, el costo acumulado es el total
    }

    public Nodo getParent() {
        return padre;
    }

    public void setParent(Nodo parent) {
        this.padre = parent;
    }

    public void addChild(Nodo child) {
        hijos.add(child);
    }

    public List<Nodo> getChildren() {
        return hijos;
    }

    @Override
    public String toString() {
        return "Nodo{estado='" + estado + "', g=" + costoAcumulado + 
               ", h=" + heuristica + ", f=" + costoTotal + "}";
    }
}