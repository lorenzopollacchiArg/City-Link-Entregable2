# CityLink - Sistema de Gestión de Reparto (Entregable 2)



## Integrantes
- Lorenzo Pollacchi
-Tiziano Aneine

---

## Descripción del proyecto:
CityLink es una aplicación de consola en Java para gestionar los envíos de una empresa de reparto urbano que permite administrar clientes, repartidores con sus vehículos ya sean bicicleta, moto y camioneta, y pedidos normales, urgentes y frágiles, calculando costos, tiempos estimados de entrega y comisione

En la entrega del  **Entregable 2** partimos del modelo orientado a objetos y las excepciones del Entregable 1 y reemplazamos los arreglos estáticos por las **estructuras dinámicas de java.util**, además de crear a mano los **algoritmos de búsqueda, ordenamiento e intercalación**.

---

## Estructuras dinámicas y algoritmos implementados

### Estructuras de java.util:
- **Árbol de búsqueda (TreeMap<Integer, Cliente>):** almacena a los clientes usando su código como clave que permite alta, baja, búsqueda rápida por código y listado ordenado automáticamente
- **Listas (ArrayList):** usadas para guardar los repartidores, la lista general de pedidos, los pedidos asignados a cada repartidor, su historial de entregas y los pedidos separados por turno (mañana y tarde)
- **Colas (Queue<Pedido>con ArrayDeque):** manejan los pedidos pendientes por orden de llegada (FIFO). Hay una cola de urgentes y otra de normales/frágiles; siempre se atiende primero la de urgentes.
- **Pila (Deque<String>` con ArrayDeque):** guarda las últimas operaciones realizadas (alta de pedido, atención y entrega) bajo el principio LIFO para poder usar la opción **Deshacer**.

### Algoritmos propios (paquete `algoritmos`)
Escritos a mano sin usar `Collections.sort`, `List.sort` ni `binarySearch`:
- **Buscador.java:** búsqueda lineal de pedido por código, búsqueda lineal de cliente por nombre y búsqueda binaria iterativa de pedido por código.
- **ordenador.java:** ordenamiento por **Burbuja** y **Merge Sort** (divide y vencerás), permitiendo elegir el criterio: costo, distancia, fecha o ID.
- **intercalador.java:** combina en un solo recorrido dos listas ya ordenadas (pedidos del turno mañana y turno tarde) sin volver a ordenar.

### Manejo de estructuras vacías
Se agregó la excepción propia (SinPedidosPendientesException) para avisar al usuario cuando intenta atender un pedido con las colas vacías o deshacer cuando la pila no tiene operaciones, sin que el programa se corte.

---

## Estructura de paquetes
- `modelo`: `Cliente`, `Pedido`, `PedidoNormal`, `PedidoUrgente`, `PedidoFragil`, `Vehiculo`, `Bicicleta`, `Moto`, `Camioneta`, `Repartidor`, `Rastreable`, `Facturable`.
- `gestor`: `CityLink` (lógica del negocio y estructuras dinámicas).
- `excepciones`: `ClienteNoEncontradoException`, `PedidoInvalidoException`, `CapacidadExcedidaException`, `AlmacenamientoLlenoException`, `EstadoInvalidoException`, `SinPedidosPendientesException`.
- `algoritmos`: `Buscador`, `ordenador`, `intercalador`.
- `app`: `Entregable2` (menú por consola y datos precargados).

---

## Cómo compilar y ejecutar

### Desde consola (parado en la carpeta `src`)
1. Compilar:
   ```bash
   javac -d ../build app/*.java algoritmos/*.java gestor/*.java modelo/*.java excepciones/*.java
   ```
2. Ejecutar:
   ```bash
   java -cp ../build app.Entregable2
   ```

### Desde IDE (NetBeans / VS Code)
Abrir el proyecto y ejecutar la clase principal `app.Entregable2`. El sistema inicia automáticamente con datos precargados (5 clientes, 3 repartidores y 8 pedidos de los tres tipos).

---

## Diagrama de clases
*(Reemplazar con la imagen del diagrama actualizado)*

![Diagrama de clases](./diagrama_clases.png)

---

## Pruebas realizadas

### Casos normales
| Prueba | Acción realizada | Resultado esperado / obtenido |
| :--- | :--- | :--- |
| Listado ordenado de clientes | Opción 1 → 4 (recorrido del `TreeMap`) | Muestra los 5 clientes ordenados de menor a mayor código (1 al 5). |
| Prioridad en colas | Opción 4 (Atender siguiente pedido) | Saca primero el pedido ID 3 (Urgente), lo asigna a la moto y lo pasa a `En camino`. |
| Registro de entrega y comisión | Opción 5 con ID 3 y luego Opción 2 → 2 | Pasa el pedido a `ENTREGADO`, lo guarda en el historial y suma el 10% a la comisión del repartidor. |
| Deshacer operación | Opción 6 después de entregar el pedido 3 | Desapila `Entrega: 3`, vuelve el pedido a `En camino` y lo quita del historial. |
| Ordenamiento con ambos algoritmos | Opción 7 eligiendo Costo (Burbuja) y Distancia (Merge Sort) | Lista los 8 pedidos ordenados correctamente en ambos casos. |
| Búsqueda binaria | Opción 8 buscando ID 3 con búsqueda binaria | Ordena por ID y encuentra el pedido 3 mostrando sus datos. |
| Intercalación de turnos | Opción 9 | Toma Turno Mañana (1, 3, 5, 7) y Turno Tarde (2, 4, 6, 8) y genera el listado intercalado (1 al 8). |

### Casos borde
| Caso borde | Acción realizada | Resultado obtenido |
| :--- | :--- | :--- |
| Colas vacías | Atender pedidos (Opción 4) hasta vaciar ambas colas y volver a intentar | Se lanza `SinPedidosPendientesException`, muestra `[ESTRUCTURA VACIA]` y el menú sigue funcionando. |
| Pila vacía | Ejecutar Opción 6 (Deshacer) hasta vaciar la pila y volver a intentar | Se lanza `SinPedidosPendientesException` avisando que no hay operaciones para deshacer. |
| Elemento inexistente | Buscar pedido ID `999` (Opción 8, lineal y binaria) o cliente inexistente | Informa `Pedido no encontrado.` / `[ERROR DE NEGOCIO]` sin cortar la ejecución. |
| Un solo elemento | Ordenar o buscar cuando hay un solo pedido cargado | Tanto Burbuja como Merge Sort (caso base `size <= 1`) y las búsquedas responden correctamente sin error de índices. |
| Dato inválido por teclado | Ingresar texto donde se espera un número en el menú | Se captura `NumberFormatException` y pide volver a ingresar la opción. |

---

## Declaración de uso de IA

- **Herramienta utilizada:** Gemini
- 
- **Para qué se utilizó:** En este proyecto usamos la IA como un copiloto organizativo y técnico. En primer lugar, la utilizamos al inicio para armar un to-do list detallado, lo que nos permitió organizar las tareas por etapas y llevar un seguimiento claro de lo que íbamos haciendo sin perdernos ni confundirnos. Después, durante el desarrollo, recurrimos a ella principalmente para consultar teoría —por ejemplo, para afianzar cómo funcionan las estructuras dinámicas o los algoritmos de búsqueda y ordenamiento— y para analizar errores puntuales o casos borde cuando algo fallaba y para poder dejar este readme prolijo recurrimos a la ia para averigurar algunos metodos para que quede prolijo y parecido a un documento de google (goggle docs).
- 
- **Cómo se verificó el resultado:** Compilamos el proyecto completo con `javac` y probamos manualmente por consola cada una de las 10 opciones del menú (tanto casos normales como casos borde) revisando línea por línea el código para poder explicarlo en la defensa oral.
