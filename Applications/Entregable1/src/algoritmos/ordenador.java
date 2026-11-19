package algoritmos;

import java.util.ArrayList;
import modelo.Pedido;

public class ordenador {

    // Metodo auxiliar que compara dos pedidos segun el criterio elegido. Devuelve >
    // 0 si p1 es mayor que p2, < 0 si p1 es menor que p2, y 0 si son iguales.

    private static int comparar(Pedido p1, Pedido p2, String criterio) {

        if ("costo".equalsIgnoreCase(criterio)) {
            return Double.compare(p1.calcularImporte(), p2.calcularImporte());
        } else if ("distancia".equalsIgnoreCase(criterio)) {

            return Double.compare(p1.getDistanciaKm(), p2.getDistanciaKm());
        } else if ("fecha".equalsIgnoreCase(criterio)) {
            return p1.getFecha().compareTo(p2.getFecha());

        } else {
            return Integer.compare(p1.getId(), p2.getId());

        }
    }

    // Algoritmo 1: Ordenamiento por Burbuja (Bubble Sort).Compara pares de
    // elementos adyacentes y los intercambia si estan desordenados,haciendo que en
    // cada pasada el mayor "burbujee" hacia el final de la lista.

    public static ArrayList<Pedido> ordenarPorBurbuja(ArrayList<Pedido> original, String criterio) {
        ArrayList<Pedido> lista = new ArrayList<>(original);
        int n = lista.size();

        for (int i = 0; i < n - 1; i++) {

            for (int j = 0; j < n - 1 - i; j++) {

                Pedido actual = lista.get(j);
                Pedido siguiente = lista.get(j + 1);

                // Si el elemento actual es mayor que el siguiente, se intercambian
                if (comparar(actual, siguiente, criterio) > 0) {

                    lista.set(j, siguiente);
                    lista.set(j + 1, actual);
                }
            }
        }

        return lista;
    }

    // Algoritmo 2: Ordenamiento Merge Sort (Divide y venceras),Divide
    // recursivamente la lista en dos mitades hasta tener listas de 0 o 1 elemento,y
    // luego las combina en orden con el metodo mezclar.

    public static ArrayList<Pedido> mergeSort(ArrayList<Pedido> lista, String criterio) {
        // Caso base: una lista vacia o de 1 solo elemento ya esta ordenada
        if (lista.size() <= 1) {
            return new ArrayList<>(lista);
        }
        int medio = lista.size() / 2;

        ArrayList<Pedido> izquierda = new ArrayList<>();
        for (int i = 0; i < medio; i++) {
            izquierda.add(lista.get(i));
        }

        ArrayList<Pedido> derecha = new ArrayList<>();
        for (int i = medio; i < lista.size(); i++) {
            derecha.add(lista.get(i));
        }

        // Llamadas recursivas para ordenar cada mitad
        izquierda = mergeSort(izquierda, criterio);
        derecha = mergeSort(derecha, criterio);
        // Combina ambas mitades ya ordenadas
        return mezclar(izquierda, derecha, criterio);
    }

    // Metodo auxiliar de Merge Sort que intercala dos mitades ya ordenadas
    // utilizando el criterio seleccionado.

    private static ArrayList<Pedido> mezclar(ArrayList<Pedido> izq, ArrayList<Pedido> der, String criterio) {
        ArrayList<Pedido> resultado = new ArrayList<>();
        int i = 0;
        int j = 0;

        while (i < izq.size() && j < der.size()) {
            Pedido p1 = izq.get(i);
            Pedido p2 = der.get(j);

            if (comparar(p1, p2, criterio) <= 0) {
                resultado.add(p1);
                i++;
            } else {
                resultado.add(p2);
                j++;
            }
        }

        while (i < izq.size()) {
            resultado.add(izq.get(i));
            i++;
        }

        while (j < der.size()) {
            resultado.add(der.get(j));
            j++;
        }

        return resultado;
    }
}
