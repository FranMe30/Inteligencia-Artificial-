package ARBOL;

public class Main {
    public static void main(String[] args) {
        Arbol arbol = new Arbol();

       
        arbol.insertar("Maria");
        arbol.insertar("Francisco");
        arbol.insertar("Gabriel");
        arbol.insertar("Lanna");
        arbol.insertar("Miguel");

        
        System.out.print("Recorrido inorden: ");
        arbol.inOrden();

        String buscar = "Andrea";
        Nodo encontrado = arbol.buscarNodo(buscar);
        if (encontrado != null) {
            System.out.println("\nEl nombre '" + buscar + "' fue encontrado.");
        } else {
            System.out.println("\nEl nombre '" + buscar + "' NO está en el árbol.");
        }
    }

}
