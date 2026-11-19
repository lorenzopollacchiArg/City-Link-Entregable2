
package gestor;

import modelo.Cliente;
import modelo.Vehiculo;
import modelo.Pedido;
import modelo.Repartidor;
import excepciones.AlmacenamientoLlenoException;
import excepciones.CapacidadExcedidaException;
import excepciones.ClienteNoEncontradoException;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.TreeMap;
import modelo.PedidoUrgente;
import excepciones.PedidoInvalidoException;
import excepciones.SinPedidosPendientesException;

public class CityLink {

    private TreeMap<Integer, Cliente> clientes;
    private ArrayList<Pedido> pedidos;
    private ArrayList<Repartidor> repartidores;
    private Queue<Pedido> colaUrgente;
    private Queue<Pedido> colaNormal_Fragil;
    private Deque<String> EliminarPila;

    public CityLink() {
        this.clientes = new TreeMap<>();
        this.pedidos = new ArrayList<>();
        this.repartidores = new ArrayList<>();
        this.colaUrgente = new ArrayDeque<>();
        this.colaNormal_Fragil = new ArrayDeque<>();
        this.EliminarPila = new ArrayDeque<>();
    }

    // Metodos clientes con arbol treeMap

    public void agregarCliente(Cliente client) throws AlmacenamientoLlenoException {
        if (client == null) {
            throw new IllegalArgumentException("El cliente no puede estar vacio");
        }

        this.clientes.put(client.getCodigo(), client);

    }

    public Cliente buscarCliente(int codigo) throws ClienteNoEncontradoException {

        if (this.clientes.containsKey(codigo)) {
            return this.clientes.get(codigo);
        }

        throw new ClienteNoEncontradoException("No se encontró ningún cliente con el código: " + codigo);
    }

    public Cliente buscarCliente(String nombre) throws ClienteNoEncontradoException {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre de búsqueda no puede ser nulo.");
        }
        for (Cliente c : this.clientes.values()) {
            if (c.getNombre().equalsIgnoreCase(nombre.trim())) {
                return c;
            }

        }
        throw new ClienteNoEncontradoException("No se encontró ningún cliente con el nombre: " + nombre);
    }

    public void eliminarCliente(int codigo) throws ClienteNoEncontradoException {
        if (!this.clientes.containsKey(codigo)) {
            throw new ClienteNoEncontradoException("No se encontro ningun cliente con el codigo: " + codigo);
        }
        this.clientes.remove(codigo);
    }

    // metodos para repartidores

    public void agregarRepartidor(Repartidor dealer) throws AlmacenamientoLlenoException {
        if (dealer == null) {
            throw new IllegalArgumentException("El repartidor no puede estar vacio");
        }
        this.repartidores.add(dealer);
    }

    public Repartidor buscarRepartidor(String legajo) {
        if (legajo == null) {
            return null;
        }
        for (Repartidor r : this.repartidores) {
            if (r.getLegajo().equalsIgnoreCase(legajo.trim())) {
                return r;
            }
        }

        return null;
    }

    public void asignarPedido(Pedido order) throws CapacidadExcedidaException {

        if (order == null) {
            throw new CapacidadExcedidaException("El pedido no puede estar vacio");
        }

        boolean asignado = false;

        for (Repartidor r : this.repartidores) {
            Vehiculo v = r.getVehiculoAsignado();
            if (v != null && v.puedeTransportar(order)) {
                order.setVehiculoAsignado(v);
                r.asignarPedido(order);
                asignado = true;
                break;
            }

        }
        if (!asignado) {
            throw new CapacidadExcedidaException(
                    "No hay ningun repartidor disponible con un vehiculo para este pedido.");
        }
    }

    // metodos para pedidos

    public Pedido buscarPedido(int id) {

        for (Pedido p : this.pedidos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public boolean eliminarPedido(int id) {

        Pedido p = buscarPedido(id);

        if (p == null) {
            return false;
        }

        this.pedidos.remove(p);
        this.colaUrgente.remove(p);
        this.colaNormal_Fragil.remove(p);
        return true;

    }

    public void agregarPedido(Pedido order) throws AlmacenamientoLlenoException, PedidoInvalidoException {
        if (order == null) {
            throw new IllegalArgumentException("El pedido no puede estar vacio");
        }

        if (buscarPedido(order.getId()) != null) {
            throw new PedidoInvalidoException("El pedido ya existe");
        }

        this.pedidos.add(order);

        if (order instanceof PedidoUrgente) {

            this.colaUrgente.offer(order);

        } else {
            this.colaNormal_Fragil.offer(order);
        }

        this.EliminarPila.push("pedido alta:  " + order.getId());

    }

    public void asignarPedido(Pedido p, Vehiculo v) throws CapacidadExcedidaException {
        if (p == null || v == null) {
            throw new IllegalArgumentException("El pedido y el vehículo no pueden ser nulos.");
        }
        if (!v.puedeTransportar(p)) {
            throw new CapacidadExcedidaException(
                    "El vehículo no puede transportar el pedido (excede peso o es frágil).");
        }
        p.setVehiculoAsignado(v);
    }

    public void atenderSigPedido() throws SinPedidosPendientesException, CapacidadExcedidaException {

        Pedido p = null;

        if (!this.colaUrgente.isEmpty()) {
            p = this.colaUrgente.poll(); // saca el primero de la cola urgente

        } else {
            if (!this.colaNormal_Fragil.isEmpty()) {
                p = this.colaNormal_Fragil.poll(); // scac el primero de la cola normal o fragil
            }
        }

        if (p == null) {
            throw new SinPedidosPendientesException("No hay pedidos pendientes en las colas para atender.");
        }

        asignarPedido(p);
        p.cambiarEstado("En camino");
        this.EliminarPila.push("Atender: " + p.getId());

    }

    public void registrarEntrega(int idPedido) throws PedidoInvalidoException {
        Pedido p = buscarPedido(idPedido);
        if (p == null) {
            throw new PedidoInvalidoException("No se encontro ningun pedido con el id: " + idPedido);
        }

        Repartidor repartidorAsignado = null;

        for (Repartidor r : this.repartidores) {
            if (r.getPedidosAsignados().contains(p)) {
                repartidorAsignado = r;
                break;

            }
        }

        if (repartidorAsignado == null) {
            throw new PedidoInvalidoException("El repartidor no existe o no tiene pedidos asignados");
        }

        repartidorAsignado.registrarEntregas(p);

        this.EliminarPila.push("Entrega: " + p.getId());

    }

    public String eliminarUltimaOperacion() throws SinPedidosPendientesException {

        if (this.EliminarPila.isEmpty()) {
            throw new SinPedidosPendientesException("La pila esta vacia: no hay operaciones para deshacer.");

        }

        String operacion = this.EliminarPila.pop();

        String[] partes = operacion.split(":");

        String tipo = partes[0].trim();
        int id = Integer.parseInt(partes[1].trim());

        Pedido p = buscarPedido(id);

        if ("pedido alta".equals(tipo)) {
            eliminarPedido(id);
        } else if ("atender".equalsIgnoreCase(tipo)) {
            p.setEstado("Pendiente");
            p.setVehiculoAsignado(null);

            for (Repartidor r : this.repartidores) {
                r.getPedidosAsignados().remove(p);
            }

            if (p instanceof PedidoUrgente) {
                this.colaUrgente.offer(p);

            }

            else {
                this.colaNormal_Fragil.offer(p);
            }
        }

        if ("entrega".equalsIgnoreCase(tipo)) {
            p.setEstado("En camino");

            for (Repartidor r : this.repartidores) {
                r.getHistorialPedidos().remove(p);
            }
        }

        return "Se elimino: " + operacion;

    }

    public TreeMap<Integer, Cliente> getClientes() {
        return this.clientes;
    }

    public ArrayList<Pedido> getPedidos() {
        return this.pedidos;
    }

    public ArrayList<Repartidor> getRepartidores() {
        return this.repartidores;
    }

    public int getCantidadPedidos() {
        return pedidos.size();
    }

    public int getCantidadRepartidores() {
        return repartidores.size();
    }

    public int getCantidadClientes() {
        return clientes.size();
    }

}
