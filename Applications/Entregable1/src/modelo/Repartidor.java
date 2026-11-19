package modelo;

import java.util.ArrayList;

public class Repartidor implements Rastreable, Facturable {

    private String legajo;
    private Vehiculo vehiculoAsignado;
    private double comision;
    private ArrayList<Pedido> pedidosAsignados;
    private ArrayList<Pedido> historialPedidos;

    public Repartidor() {
        this.comision = 0.10;
        this.pedidosAsignados = new ArrayList<Pedido>();
        this.historialPedidos = new ArrayList<Pedido>();
    }

    public Repartidor(String legajo, Vehiculo vehiculoAsignado) {
        this.legajo = legajo;
        this.vehiculoAsignado = vehiculoAsignado;
        this.comision = 0.10; // 10% fijado por regla de negocio
        this.pedidosAsignados = new ArrayList<Pedido>();
        this.historialPedidos = new ArrayList<Pedido>();
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        if (legajo == null) {
            throw new IllegalArgumentException("El legajo no debe estar vacio");
        }
        this.legajo = legajo;
    }

    public Vehiculo getVehiculoAsignado() {
        return vehiculoAsignado;
    }

    public void setVehiculoAsignado(Vehiculo vehiculoAsignado) {
        if (vehiculoAsignado == null) {
            throw new IllegalArgumentException("El repartidor debe tener un vehiuclo asignado");
        }
        this.vehiculoAsignado = vehiculoAsignado;
    }

    public double getComision() {
        return comision;
    }

    public void setComision(double comision) {
        if (comision <= 0) {
            throw new IllegalArgumentException("El repartidor debe tener una comision asignada");
        }
        this.comision = comision;
    }

    public ArrayList<Pedido> getPedidosAsignados() {
        return pedidosAsignados;
    }

    public void setPedidosAsignados(ArrayList<Pedido> pedidosAsignados) {
        this.pedidosAsignados = pedidosAsignados;
    }

    public ArrayList<Pedido> getHistorialPedidos() {
        return historialPedidos;
    }

    public void setHistorialPedidos(ArrayList<Pedido> historialPedidos) {
        this.historialPedidos = historialPedidos;
    }

    public void asignarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }
        this.pedidosAsignados.add(pedido);
    }

    public String registrarEntregas(Pedido pedido) {
        pedido.cambiarEstado("Entregado");
        historialPedidos.add(pedido);

        return "Pedido" + pedido.getId() + "entregado exitosamente";

    }

    @Override

    public double calcularImporte() {
        double totalEntregado = 0.0;

        for (Pedido p : pedidosAsignados) {
            if ("entregado".equalsIgnoreCase(p.getEstado())) {
                totalEntregado += p.calcularImporte();
            }
        }

        return totalEntregado * this.comision;

    }

    @Override

    public String obtenerEstadoActual() {
        String infoVehiculo;
        if (vehiculoAsignado != null) {
            infoVehiculo = vehiculoAsignado.getPatente();
        } else {
            infoVehiculo = "Sin vehiculo";
        }
        return "Estado del pedido: Repartidor:" + legajo + "Vehiculo:" + infoVehiculo + "Numero de pedidos:"
                + historialPedidos.size();
    }

}
