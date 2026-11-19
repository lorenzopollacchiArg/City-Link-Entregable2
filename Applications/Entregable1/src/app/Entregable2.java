
package app;

import algoritmos.Buscador;
import algoritmos.intercalador;
import algoritmos.ordenador;
import excepciones.AlmacenamientoLlenoException;
import excepciones.CapacidadExcedidaException;
import excepciones.ClienteNoEncontradoException;
import excepciones.EstadoInvalidoException;
import excepciones.PedidoInvalidoException;
import excepciones.SinPedidosPendientesException;
import gestor.CityLink;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import modelo.Bicicleta;
import modelo.Camioneta;
import modelo.Cliente;
import modelo.Facturable;
import modelo.Moto;
import modelo.Pedido;
import modelo.PedidoFragil;
import modelo.PedidoNormal;
import modelo.PedidoUrgente;
import modelo.Rastreable;
import modelo.Repartidor;
import modelo.Vehiculo;

public class Entregable2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CityLink sistema = new CityLink();

        ejecutarPrecarga(sistema);
        int opcion = 0;

        do {
            try {
                mostrarMenu();
                System.out.println("Ingrese una opcion para continuar:");
                opcion = Integer.parseInt(scanner.nextLine().trim());

                switch (opcion) {
                    case 1:
                        gestionarClientes(sistema, scanner);
                        break;
                    case 2:
                        gestionarRepartidores(sistema, scanner);
                        break;
                    case 3:
                        registrarPedido(sistema, scanner);
                        break;
                    case 4:
                        atenderSiguientePedido(sistema);
                        break;
                    case 5:
                        registrarEntrega(sistema, scanner);
                        break;
                    case 6:
                        deshacerUltimaOperacion(sistema);
                        break;
                    case 7:
                        ordenarYListarPedidos(sistema, scanner);
                        break;
                    case 8:
                        buscarPedido(sistema, scanner);
                        break;
                    case 9:
                        intercalarTurnos(sistema);
                        break;
                    case 10:
                        System.out.println("Cerrando gestor. Muchas gracias.");
                        break;
                    default:
                        System.out.println("Opcion invalida, intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un valor numerico valido.");
            } catch (AlmacenamientoLlenoException e) {
                System.out.println("\n[ERROR DE NEGOCIO] " + e.getMessage());
            } catch (CapacidadExcedidaException e) {
                System.out.println("\n[ERROR DE NEGOCIO] " + e.getMessage());
            } catch (ClienteNoEncontradoException e) {
                System.out.println("\n[ERROR DE NEGOCIO] " + e.getMessage());
            } catch (EstadoInvalidoException e) {
                System.out.println("\n[ERROR DE ESTADO] " + e.getMessage());
            } catch (PedidoInvalidoException e) {
                System.out.println("\n[ERROR DE PEDIDO] " + e.getMessage());
            } catch (SinPedidosPendientesException e) {
                System.out.println("\n[ESTRUCTURA VACIA] " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error general: " + e.getMessage());
            } finally {
                System.out.println("...\n");
            }
        } while (opcion != 10);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("BIENVENIDO/A AL SISTEMA CITYLINK");
        System.out.println("1- Gestion de clientes (Alta, Baja, Buscar, Listar ordenados)");
        System.out.println("2- Gestion de repartidores (Alta, Listar historial y comisiones)");
        System.out.println("3- Registrar pedido (Normal | Urgente | Fragil)");
        System.out.println("4- Atender siguiente pedido (prioridad Urgentes)");
        System.out.println("5- Registrar entrega de pedido");
        System.out.println("6- Deshacer ultima operacion");
        System.out.println("7- Ordenar y listar pedidos");
        System.out.println("8- Buscar pedido por codigo (Lineal | Binaria)");
        System.out.println("9- Intercalar pedidos de dos turnos (Ma\u00f1ana | Tarde)");
        System.out.println("10- Salir");
    }

    private static void ejecutarPrecarga(CityLink sistema) {
        try {
            Cliente client1 = new Cliente("Lorenzo Pollacchi", "2657-300978", 1, "Zona centro");
            Cliente client2 = new Cliente("Juan Carlos Pollacchi", "2657-611910", 2, "Zona centro");
            Cliente client3 = new Cliente("Aero Club VM", "2657-123456", 3, "Zona Norte");
            Cliente client4 = new Cliente("Augusto Magallanes", "2657-123456", 4, "Zona Oeste");
            Cliente client5 = new Cliente("Carolina Enciso", "2657-652975", 5, "Zona Centro");

            sistema.agregarCliente(client1);
            sistema.agregarCliente(client2);
            sistema.agregarCliente(client3);
            sistema.agregarCliente(client4);
            sistema.agregarCliente(client5);

            Vehiculo bici = new Bicicleta("Venzo-001");
            Vehiculo moto = new Moto("A12345");
            Vehiculo camioneta = new Camioneta("AH828GL");

            Repartidor d1 = new Repartidor("2026-001", bici);
            Repartidor d2 = new Repartidor("2024-023", moto);
            Repartidor d3 = new Repartidor("2025-033", camioneta);

            sistema.agregarRepartidor(d1);
            sistema.agregarRepartidor(d2);
            sistema.agregarRepartidor(d3);

            Pedido order1 = new PedidoNormal(1, 5.0, 3.0, client1, "Fuerte Constitucional 182");
            Pedido order2 = new PedidoNormal(2, 10.0, 12.0, client2, "Avenida Mitre 1200");
            Pedido order3 = new PedidoUrgente(3, 22.0, 13.0, client3, "Aero Club Villa Mercedes");
            Pedido order4 = new PedidoFragil(4, 8.0, 3.0, client4, "Aeropuerto Villa Reynolds");
            Pedido order5 = new PedidoUrgente(5, 10.0, 12.0, client5, "Rivadavia 1200");
            Pedido order6 = new PedidoNormal(6, 30.0, 23.0, client5, "Aeropuerto San Luis LUQ");
            Pedido order7 = new PedidoFragil(7, 14.0, 4.0, client3, "Aero Club Villa Mercedes");
            Pedido order8 = new PedidoUrgente(8, 5.0, 3.0, client1, "Fuerte Constitucional 182");

            LocalDateTime manana = LocalDateTime.of(2026, 11, 18, 9, 30);
            LocalDateTime tarde = LocalDateTime.of(2026, 11, 18, 16, 15);

            order1.setFecha(manana);
            order2.setFecha(tarde);
            order3.setFecha(manana);
            order4.setFecha(tarde);
            order5.setFecha(manana);
            order6.setFecha(tarde);
            order7.setFecha(manana);
            order8.setFecha(tarde);

            sistema.agregarPedido(order1);
            sistema.agregarPedido(order2);
            sistema.agregarPedido(order3);
            sistema.agregarPedido(order4);
            sistema.agregarPedido(order5);
            sistema.agregarPedido(order6);
            sistema.agregarPedido(order7);
            sistema.agregarPedido(order8);

            System.out.println("Precarga exitosa.");
        } catch (Exception e) {
            System.out.println("[AVISO PRECARGA] " + e.getMessage());
        }
    }

    // opcion 1 Gestion de Clientes
    private static void gestionarClientes(CityLink sistema, Scanner scanner)
            throws AlmacenamientoLlenoException, ClienteNoEncontradoException {
        System.out.println("--- GESTION DE CLIENTES ---");
        System.out.println("1- Alta de cliente");
        System.out.println("2- Baja de cliente");
        System.out.println("3- Buscar cliente (por codigo o nombre)");
        System.out.println("4- Listar clientes ordenados por codigo");
        int subOpcion = Integer.parseInt(scanner.nextLine().trim());

        switch (subOpcion) {
            case 1:
                registrarCliente(sistema, scanner);
                break;
            case 2:
                System.out.println("Ingrese el codigo del cliente a eliminar:");
                int codEliminar = Integer.parseInt(scanner.nextLine().trim());
                sistema.eliminarCliente(codEliminar);
                System.out.println("Cliente eliminado correctamente.");
                break;
            case 3:
                buscarCliente(sistema, scanner);
                break;
            case 4:
                if (sistema.getClientes().isEmpty()) {
                    System.out.println("No hay clientes registrados.");
                } else {
                    System.out.println("Clientes ordenados por codigo:");
                    for (Cliente c : sistema.getClientes().values()) {
                        System.out.println("Codigo: " + c.getCodigo() + " | Nombre: " + c.getNombre()
                                + " | Tel: " + c.getTelefono() + " | Zona: " + c.getZona());
                    }
                }
                break;
            default:
                System.out.println("Opcion invalida.");
        }
    }

    private static void registrarCliente(CityLink sistema, Scanner scanner) throws AlmacenamientoLlenoException {
        System.out.println(" REGISTRAR CLIENTE ");
        System.out.println("Ingrese el nombre:");
        String nombre = scanner.nextLine().trim();
        System.out.println("Ingrese el numero de telefono:");
        String telefono = scanner.nextLine().trim();
        System.out.println("Ingrese el codigo numerico de cliente:");
        int codigo = Integer.parseInt(scanner.nextLine().trim());
        System.out.println("Ingrese la zona:");
        String zona = scanner.nextLine().trim();

        Cliente nuevo = new Cliente(nombre, telefono, codigo, zona);
        sistema.agregarCliente(nuevo);
        System.out.println("Cliente registrado con exito!");
    }

    private static void buscarCliente(CityLink sistema, Scanner scanner) throws ClienteNoEncontradoException {
        System.out.println("BUSCAR CLIENTE ");
        System.out.println("Ingrese como desea buscar al cliente: ");
        System.out.println("1- Por Codigo");
        System.out.println("2- Por Nombre");
        int opcion = Integer.parseInt(scanner.nextLine().trim());

        while (opcion < 1 || opcion > 2) {
            System.out.println("Ingrese una opcion valida, intende de nuevo");
            opcion = Integer.parseInt(scanner.nextLine().trim());
        }

        if (opcion == 1) {
            System.out.println("Ingrese codigo de cliente: ");
            int codigo = Integer.parseInt(scanner.nextLine().trim());
            Cliente c = sistema.buscarCliente(codigo);
            System.out.println("Cliente encontrado: " + c.getNombre() + " | Telefono: " + c.getTelefono() + " | Zona: "
                    + c.getZona());
        } else {
            System.out.println("Ingrese nombre del cliente:");
            String nombre = scanner.nextLine().trim();
            Cliente c = sistema.buscarCliente(nombre);
            System.out.println("Cliente encontrado - Codigo: " + c.getCodigo() + " | Nombre: " + c.getNombre()
                    + " | Telefono: " + c.getTelefono());
        }
    }

    // opcion 2 esGestion de Repartidores
    private static void gestionarRepartidores(CityLink sistema, Scanner scanner) throws AlmacenamientoLlenoException {
        System.out.println("GESTION DE REPARTIDORES");
        System.out.println("1- Registrar repartidor con vehiculo");
        System.out.println("2- Listar repartidores, estado e importes/comisiones");
        int subOpcion = Integer.parseInt(scanner.nextLine().trim());

        switch (subOpcion) {
            case 1:
                registarRepartidor(sistema, scanner);
                break;
            case 2:
                ArrayList<Repartidor> repartidores = sistema.getRepartidores();
                if (repartidores.isEmpty()) {
                    System.out.println("No hay repartidores registrados.");
                    return;
                }
                double comisionTotalGeneral = 0.0;
                for (Repartidor r : repartidores) {
                    Rastreable rastreable = r;
                    Facturable facturable = r;
                    double comision = facturable.calcularImporte();
                    comisionTotalGeneral += comision;
                    System.out.println(rastreable.obtenerEstadoActual() + " | Comision acumulada: $" + comision);
                }
                System.out.println("Comision total a abonar a la flota: $" + comisionTotalGeneral);
                break;
            default:
                System.out.println("Opcion invalida.");
        }
    }

    private static void registarRepartidor(CityLink sistema, Scanner scanner) throws AlmacenamientoLlenoException {
        System.out.println("REGISTRAR REPARTIDOR CON VEHICULO ");
        System.out.println("Ingrese el numero de  legajo:");
        String legajo = scanner.nextLine().trim();

        System.out.println("Seleccione el  tipo de vehiculo 1- Bicicleta, 2-  Moto, 3- Camioneta");
        int tipoVehiculo = Integer.parseInt(scanner.nextLine().trim());
        while (tipoVehiculo < 1 || tipoVehiculo > 3) {
            System.out.println("Opcion invalida, intente de nuevo");
            tipoVehiculo = Integer.parseInt(scanner.nextLine().trim());
        }

        System.out.println("Ingrese la patente del vehiculo:");
        String patente = scanner.nextLine().trim();
        while (patente == null || patente.length() == 0) {
            System.out.println("Ingrese la patente del vehiculo, es obligatorio");
            patente = scanner.nextLine().trim();
        }

        Vehiculo vehiculo;
        switch (tipoVehiculo) {
            case 1:
                vehiculo = new Bicicleta(patente);
                break;
            case 2:
                vehiculo = new Moto(patente);
                break;
            case 3:
                vehiculo = new Camioneta(patente);
                break;
            default:
                System.out.println("opcion invalida. Se asignara una bicicleta por defecto");
                vehiculo = new Bicicleta(patente);
        }

        Repartidor repartidor = new Repartidor(legajo, vehiculo);
        sistema.agregarRepartidor(repartidor);

        System.out.println("Repartidor registrado exitosamente");
    }

    // opcion 3 Registrar un Pedido
    private static void registrarPedido(CityLink sistema, Scanner scanner)
            throws AlmacenamientoLlenoException, ClienteNoEncontradoException, PedidoInvalidoException {
        System.out.println("REGISTRAR PEDIDO");
        System.out.println("Ingrese el ID del pedido (Solo numeros):");
        int id = Integer.parseInt(scanner.nextLine().trim());
        while (id < 0 || id > 1000000) {
            System.out.println("Ingrese un id valido, intente de nuevo");
            id = Integer.parseInt(scanner.nextLine().trim());
        }

        System.out.println("Ingrese peso en kg:");
        double peso = Double.parseDouble(scanner.nextLine().trim());
        while (peso < 0 || peso > 10000) {
            System.out.println(
                    "Ingrese un peso valido, Si el peso que ingreso es veridico consulte a atencion al cliente");
            peso = Double.parseDouble(scanner.nextLine().trim());
        }

        System.out.println("Ingrese distancia en km:");
        double distancia = Double.parseDouble(scanner.nextLine().trim());
        while (distancia < 0 || distancia > 10000) {
            System.out.println(
                    "Ingrese una distancia valida, Si la distanica que ingreso es veridica consulte a atencion al cliente");
            distancia = Double.parseDouble(scanner.nextLine().trim());
        }

        System.out.println("Ingrese codigo del cliente asociado:");
        int codCliente = Integer.parseInt(scanner.nextLine().trim());
        Cliente cliente = sistema.buscarCliente(codCliente);

        System.out.println("Ingrese direccion de destino:");
        String destino = scanner.nextLine().trim();
        while (destino == null || destino.length() == 0) {
            System.out.println("Ingrese un destino o dirreccion");
            destino = scanner.nextLine().trim();
        }

        System.out.println("Seleccione tipo de pedido (1- Normal, 2- Urgente, 3- Fragil):");
        int tipo = Integer.parseInt(scanner.nextLine().trim());
        while (tipo < 1 || tipo > 3) {
            System.out.println("Ingrese una opcion valida");
            tipo = Integer.parseInt(scanner.nextLine().trim());
        }

        Pedido pedido;
        switch (tipo) {
            case 1:
                pedido = new PedidoNormal(id, distancia, peso, cliente, destino);
                break;
            case 2:
                pedido = new PedidoUrgente(id, distancia, peso, cliente, destino);
                break;
            case 3:
                pedido = new PedidoFragil(id, distancia, peso, cliente, destino);
                break;
            default:
                pedido = new PedidoNormal(id, distancia, peso, cliente, destino);
        }

        sistema.agregarPedido(pedido);
        System.out.println("Pedido registrado con exito");
    }

    // opcion 4 Atender el siguiente pedido
    private static void atenderSiguientePedido(CityLink sistema)
            throws SinPedidosPendientesException, CapacidadExcedidaException {
        sistema.atenderSigPedido();
        System.out.println("Siguiente pedido de la cola atendido, asignado y puesto En Camino.");
    }

    // opcion 5 Registrar entrega
    private static void registrarEntrega(CityLink sistema, Scanner scanner) throws PedidoInvalidoException {
        System.out.println("REGISTRAR ENTREGA");
        System.out.println("Ingrese el ID del pedido entregado:");
        int id = Integer.parseInt(scanner.nextLine().trim());
        sistema.registrarEntrega(id);
        System.out.println("Entrega registrada y agregada al historial del repartidor.");
    }

    // opcion6  Deshacer ultima operacion
    private static void deshacerUltimaOperacion(CityLink sistema) throws SinPedidosPendientesException {
        String msg = sistema.eliminarUltimaOperacion();
        System.out.println(msg);
    }

    // opcion 7 Ordenar y listar pedidos
    private static void ordenarYListarPedidos(CityLink sistema, Scanner scanner) {
        ArrayList<Pedido> pedidos = sistema.getPedidos();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados en el sistema.");
            return;
        }

        System.out.println("Seleccione criterio de ordenamiento:");
        System.out.println("1- Costo | 2- Distancia | 3- Fecha | 4- ID");
        int opCriterio = Integer.parseInt(scanner.nextLine().trim());
        String criterio;
        switch (opCriterio) {
            case 1:
                criterio = "costo";
                break;
            case 2:
                criterio = "distancia";
                break;
            case 3:
                criterio = "fecha";
                break;
            default:
                criterio = "id";
                break;
        }

        System.out.println("Seleccione algoritmo: 1- Burbuja | 2- Merge Sort");
        int opAlgoritmo = Integer.parseInt(scanner.nextLine().trim());

        ArrayList<Pedido> ordenados = (opAlgoritmo == 2)
                ? ordenador.mergeSort(pedidos, criterio)
                : ordenador.ordenarPorBurbuja(pedidos, criterio);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.println("=== PEDIDOS ORDENADOS POR " + criterio.toUpperCase() + " ===");
        for (Pedido p : ordenados) {
            String infoVehiculo = (p.getVehiculoAsignado() != null)
                    ? p.getVehiculoAsignado().getPatente() + " (Tiempo: "
                            + p.getVehiculoAsignado().calcularTiempoEntrega(p.getDistanciaKm()) + " min)"
                    : "Sin asignar";
            System.out.println("ID: " + p.getId()
                    + " |  Tipo: " + p.getClass().getSimpleName()
                    + " | Costo: $" + p.calcularImporte()
                    + " | Dist: " + p.getDistanciaKm() + " km"
                    + " | Fecha: " + p.getFecha().format(fmt) + " (" + p.getTurno() + ")"
                    + " | Estado: " + p.obtenerEstadoActual()
                    + " | Vehiculo: " + infoVehiculo);
        }
    }

    // opcion 8 Buscar pedido por codigo (Lineal o Binaria)

    private static void buscarPedido(CityLink sistema, Scanner scanner) {
        System.out.println("BUSCAR PEDIDO POR CODIGO");
        System.out.println("Ingrese el ID del pedido:");
        int id = Integer.parseInt(scanner.nextLine().trim());

        System.out.println("Seleccione algoritmo: 1- Busqueda Lineal | 2- Busqueda Binaria");
        int tipoBusqueda = Integer.parseInt(scanner.nextLine().trim());

        Pedido encontrado;
        if (tipoBusqueda == 2) {
            ArrayList<Pedido> ordenadosPorId = ordenador.mergeSort(sistema.getPedidos(), "id");
            encontrado = Buscador.busquedaBinariaPedido(ordenadosPorId, id);
        } else {
            encontrado = Buscador.busquedaLinealPedido(sistema.getPedidos(), id);
        }

        if (encontrado != null) {
            System.out.println("Pedido encontrado -> ID: " + encontrado.getId()
                    + " | Cliente: " + encontrado.getCliente().getNombre()
                    + " | Destino: " + encontrado.getDestino()
                    + " | Distancia: " + encontrado.getDistanciaKm() + " km"
                    + " | Peso: " + encontrado.getPesoKg() + " kg"
                    + " | Estado: " + encontrado.obtenerEstadoActual());
        } else {
            System.out.println("Pedido no encontrado.");
        }
    }

    // OPCION 9: Intercalar pedidos de dos turnos

    private static void intercalarTurnos(CityLink sistema) {
        ArrayList<Pedido> turnoManana = new ArrayList<>();
        ArrayList<Pedido> turnoTarde = new ArrayList<>();

        for (Pedido p : sistema.getPedidos()) {
            if (!"Tarde".equalsIgnoreCase(p.getTurno())) {
                turnoManana.add(p);
            } else {
                turnoTarde.add(p);
            }
        }

        turnoManana = ordenador.mergeSort(turnoManana, "id");
        turnoTarde = ordenador.mergeSort(turnoTarde, "id");

        System.out.println("Pedidos Turno Ma\u00f1ana (" + turnoManana.size() + "):");
        for (Pedido p : turnoManana) {
            System.out.println("  ID: " + p.getId() + " - " + p.getDestino());
        }

        System.out.println("Pedidos Turno Tarde (" + turnoTarde.size() + "):");
        for (Pedido p : turnoTarde) {
            System.out.println("  ID: " + p.getId() + " - " + p.getDestino());
        }

        ArrayList<Pedido> intercalados = intercalador.intercalaPorId(turnoManana, turnoTarde);
        System.out.println("=== LISTADO INTERCALADO POR ID (" + intercalados.size() + " pedidos) ===");
        for (Pedido p : intercalados) {
            System.out.println("ID: " + p.getId() + " | Turno: " + p.getTurno()
                    + " | Destino: " + p.getDestino() + " | Importe: $" + p.calcularImporte());
        }
    }
}