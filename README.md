# Plataforma de Procesamiento de Órdenes con Microservicios

Este proyecto implementa una arquitectura de **microservicios desplegada en Azure Kubernetes Service (AKS)** para procesar órdenes de manera asincrónica. El sistema utiliza servicios de Azure para el procesamiento de eventos, almacenamiento de datos y observabilidad del sistema.

---

## Descripción del Sistema

La plataforma está compuesta por dos microservicios principales:

### `order-service`

Servicio encargado de recibir solicitudes HTTP para crear órdenes y publicar eventos en una cola de mensajes.

### `payment-processor`

Servicio consumidor que procesa los mensajes de la cola, ejecuta la lógica de negocio, persiste los datos y registra auditorías.

---

## Tecnologías Utilizadas

| Categoría      | Tecnología                  |
| -------------- | --------------------------- |
| Lenguaje       | Java 17                     |
| Framework      | Spring Boot, Spring Webflux |
| Contenedores   | Docker                      |
| Orquestación   | Kubernetes / AKS            |
| Mensajería     | Azure Queue Storage         |
| Base de Datos  | Azure Cosmos DB             |
| Almacenamiento | Azure Blob Storage          |
| Observabilidad | Loki, Promtail, Grafana     |

---

## Arquitectura del Sistema

El flujo de procesamiento de órdenes sigue el siguiente proceso:

1. El cliente envía una solicitud HTTP al **order-service**.
2. El servicio publica un evento en **Azure Queue Storage**.
3. El **payment-processor** consume el mensaje de la cola.
4. El servicio procesa la orden y la almacena en **Cosmos DB**.
5. Se genera un registro de auditoría en **Azure Blob Storage**.
6. Los logs generados por los servicios son recolectados para observabilidad.

> Diagrama de arquitectura incluido en: `diagramas.pptx` — Página 1

### Flujo Simplificado

```
Cliente
   ↓
LoadBalancer (AKS)
   ↓
order-service
   ↓
Azure Queue Storage
   ↓
payment-processor
   ↓
Cosmos DB
   ↓
Blob Storage
```

---

## Arquitectura de Observabilidad

El sistema cuenta con un pipeline de observabilidad que permite visualizar los logs generados por los microservicios.

> Diagrama incluido en: `diagramas.pptx` — Página 2

### Flujo de Logs

```
Microservicios
     ↓
  Promtail
     ↓
    Loki
     ↓
  Grafana
```

Los logs estructurados son recolectados por **Promtail**, almacenados en **Loki** y visualizados mediante **Grafana dashboards**.

Esto permite monitorear:

- Órdenes procesadas por minuto
- Persistencia de órdenes en la base de datos
- Eventos de auditoría
- Logs en tiempo real

---

## Cómo Ejecutar el Proyecto

### 1. Compilar los servicios

```bash
mvn clean package -DskipTests
```

### 2. Construir imágenes Docker

```bash
docker build -t order-service .
docker build -t payment-processor .
```

### 3. Desplegar en Kubernetes

```bash
kubectl apply -f k8s/
```

---

## Pruebas de API

Se incluye una colección de **Postman** para probar los endpoints del sistema.

Ubicación:

```
\order-platform\Brother Work.postman_collection.json
```

Ejemplo de solicitud para crear una orden:

```
POST /orders
```

```json
{
  "id": "ORD100",
  "customerId": "CUST100",
  "items": [
    {
      "productId": "PRD1",
      "quantity": 2
    }
  ],
  "totalAmount": 100
}
```

---

## Observabilidad

Los logs del sistema se generan en formato JSON e incluyen información relevante como:

- `timestamp`
- nombre del servicio
- nivel de log
- `traceId`

Estos logs permiten analizar el comportamiento del sistema y diagnosticar problemas.

Los dashboards de Grafana muestran métricas como:

- Órdenes procesadas
- Órdenes persistidas
- Eventos de auditoría
- Logs en tiempo real

---

## Configuraciones Extra

### `order-service`

Ruta: `\order-platform\order-service\src\main\resources\application-azure.yaml`

```yaml
azure:
  storage:
    queue:
      connection-string: ${QUEUE_CONNECTION_STRING} # Coloca tu cadena de conexion de las colas, o usa el .jar que ya las trae en target
      queue-name: orders-queue
```

### `payment-processor`

Ruta: `\order-platform\payment-processor\src\main\resources\application-azure.yaml`

```yaml
azure:
  cosmos:
    endpoint: ${COSMOS_URI} # URI
    key: ${COSMOS_KEY} # KEY
    database: orders-db
    container: orders
  storage:
    queue:
      connection-string: ${QUEUE_CONNECTION_STRING} # queue connection string
      queue-name: orders-queue
    blob:
      connection-string: ${BLOB_CONNECTION_STRING} # Blob connection string
      container-name: order-audit
```
