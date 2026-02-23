package ARBOL;
class Nodo {
    String nombre;  
    Nodo izq;
    Nodo der;

    Nodo(String nombre) {
        this.nombre = nombre;
        this.izq = null;
        this.der = null;
    }
}