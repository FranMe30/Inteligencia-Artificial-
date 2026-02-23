package ARBOL;

class Arbol {
    Nodo raiz;

 
    boolean vacio() {
        return raiz == null;
    }

    void insertar(String nombre) {
        raiz = insertarRec(raiz, nombre);
    }

    private Nodo insertarRec(Nodo actual, String nombre) {
        if (actual == null) {
            return new Nodo(nombre);
        }
        if (nombre.compareTo(actual.nombre) < 0) {
            actual.izq = insertarRec(actual.izq, nombre);
        } else if (nombre.compareTo(actual.nombre) > 0) {
            actual.der = insertarRec(actual.der, nombre);
        }
        return actual;
    }

    // Buscar un nodo por nombre
    Nodo buscarNodo(String nombre) {
        return buscarRec(raiz, nombre);
    }

    private Nodo buscarRec(Nodo actual, String nombre) {
        if (actual == null || actual.nombre.equals(nombre)) {
            return actual;
        }
        if (nombre.compareTo(actual.nombre) < 0) {
            return buscarRec(actual.izq, nombre);
        } else {
            return buscarRec(actual.der, nombre);
        }
    }

    // Recorrido en orden (izquierda - raíz - derecha)
    void inOrden() {
        inOrdenRec(raiz);
        System.out.println();
    }

    private void inOrdenRec(Nodo actual) {
        if (actual != null) {
            inOrdenRec(actual.izq);
            System.out.print(actual.nombre + " ");
            inOrdenRec(actual.der);
        }
    }
}