
package algoritmos;

import java.util.ArrayList;
import modelo.Pedido;

/**
 *
 * @author lorenzopollacchi
 */
public class intercalador {

    public static ArrayList<Pedido> intercalaPorId(ArrayList<Pedido> listaA, ArrayList<Pedido> listaB) {
        ArrayList<Pedido> resultado = new ArrayList<>();
        int i = 0; // contador lista a
        int j = 0; // contador lista b

        while (i < listaA.size() && j < listaB.size()) {
            Pedido p1 = listaA.get(i);
            Pedido p2 = listaB.get(j);

            if (p1.getId() <= p2.getId()) {
                resultado.add(p1);
                i++;
            } else {
                resultado.add(p2);
                j++;
            }

        }

        while (i < listaA.size()) {
            resultado.add(listaA.get(i));
            i++;
        }

        while (j < listaB.size()) {
            resultado.add(listaB.get(j));
            j++;
        }

        return resultado;

    }

}
