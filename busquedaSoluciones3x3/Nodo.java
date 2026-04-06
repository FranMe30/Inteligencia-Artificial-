package busquedaSoluciones3x3;

import java.util.*;

public class Nodo {
    String estado;
    int nivel;       
    Nodo padre;
    private int totalCost;
    private List<Nodo> hijos;
    
    public Nodo(String estado, int nivel, Nodo padre) {
        this.estado = estado;
        this.nivel = nivel;
        this.padre = padre;
        this.totalCost = 0;
        this.hijos = new ArrayList<>();
    }
    
    public Nodo(String estado) {
        this.estado = estado;
        this.nivel = 0;
        this.padre = null;
        this.totalCost = 0;
        this.hijos = new ArrayList<>();
    }
    
    public String getState() {
        return estado;
    }
    
    public int getTotalCost() {
        return totalCost;
    }
    
    public void setTotalCost(int cost) {
        this.totalCost = cost;
    }
    
    public void setCost(int cost) {
        this.totalCost = cost;
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
        return "Nodo{estado='" + estado + "', nivel=" + nivel + ", costo=" + totalCost + "}";
    }
}