
package algoritmos;

import java.util.ArrayList;
import java.util.Collection;
import modelo.Pedido;
import modelo.Cliente;

public class Buscador {

    public static Pedido busquedaLinealPedido(ArrayList<Pedido> lista, int codigo) {
        for (Pedido p : lista) {
            if (p.getId() == codigo) {
                return p;
            }
        }

        return null;

    }

    public static Cliente busquedaLinealCliente(Collection<Cliente> clientes, String nombre) {
        if (nombre == null || clientes == null) {
            return null;
        }

        for (Cliente c : clientes) {
            if (c.getNombre().equalsIgnoreCase(nombre.trim())) {
                return c;
            }
        }

        return null;
    }

    public static Pedido busquedaBinariaPedido(ArrayList<Pedido> ListaOrdenada, int codigo) {
        int inicio = 0;
        int fin = ListaOrdenada.size() - 1;

        while (inicio <= fin) {
            int medio = (inicio + fin) / 2;
            Pedido pedidoMedio = ListaOrdenada.get(medio);

            if (pedidoMedio.getId() == codigo) {
                return pedidoMedio;
            } else if (pedidoMedio.getId() < codigo) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }

        }

        return null;
    }

}
