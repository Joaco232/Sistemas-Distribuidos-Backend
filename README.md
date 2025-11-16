# Sistemas-Distribuidos

## Ejecutar con Docker Compose

Este proyecto incluye un `docker-compose.yml` para levantar todos los componentes:
- Base de datos Postgres
- Backend Spring Boot
- Frontend (Dockerfile externo en `SSDD frontend/Sistemas-Distribuidos-Frontend/movienow-frontend/Dockerfile`)

### Requisitos
- Docker Desktop (con Docker Compose v2)

### Variables de entorno
Crea/edita el archivo `.env` en la raíz (ya se incluye un ejemplo):

- `POSTGRES_DB` (por defecto `movienow`)
- `POSTGRES_USER` (por defecto `movienow`)
- `POSTGRES_PASSWORD` (por defecto `movienow`)
- `JWT_SECRET` (longitud mínima 32 bytes; ya configurada con un valor seguro por defecto)
- `MOVIENOW_API_KEY` y `MOVIENOW_API_TOKEN` si quieres sobreescribir los valores por defecto del `application.yml`.
- `FRONTEND_CONTEXT` para apuntar al directorio del Dockerfile del frontend (sin comillas; admite espacios).

### Levantar los servicios (compose local)
En Windows (cmd):

```
cd C:\Users\srjua\Documents\SSDD
docker compose up -d --build
```

Si usas una versión antigua de Compose:

```
docker-compose up -d --build
```

### Puertos y URLs
- Backend: http://localhost:8080
- Frontend (servidor estático): http://localhost:3000
- Postgres expuesto en el puerto 5432 del host.

Nota: El frontend se construye con la URL del backend en `http://localhost:8080`. Si tu Dockerfile del frontend expone otro puerto distinto del 80, ajusta el mapeo en `docker-compose.yml` (por ejemplo `"3000:3000"`).

### Dependencias entre servicios
- El backend espera a que Postgres esté saludable antes de arrancar.
- El frontend depende del backend.

### Volúmenes
- `postgres_data`: datos persistentes de Postgres.

---

## Arquitectura de sistemas distribuidos (visión general)

Este sistema está compuesto por servicios desacoplados que se comunican entre sí a través de HTTP y, opcionalmente, mensajería (RabbitMQ). Cada servicio corre en su propio contenedor y se resuelve por nombre de servicio dentro de la red de Docker Compose.

- Frontend (SPA/estático):
  - Consume la API REST del backend vía HTTP.
  - Desplegado detrás de un servidor estático (p. ej., Nginx) dentro de su contenedor.
- Backend (Spring Boot):
  - Expone API REST para autenticación, usuarios, proveedores y películas.
  - Persiste datos en Postgres.
  - Puede publicar/consumir mensajes en RabbitMQ (p. ej., para enviar emails o procesar tareas asíncronas).
  - Emite métricas (recomendado habilitar Actuator + Micrometer Prometheus) para observabilidad.
- Postgres:
  - Base de datos relacional.
  - Almacenamiento persistente con volumen `postgres_data`.
- RabbitMQ (opcional en compose extendido):
  - Broker de mensajería para trabajos asíncronos y comunicación desacoplada.
  - Panel en `http://localhost:15672`.
- Prometheus (opcional):
  - Scrapea métricas del backend.
- Grafana (opcional):
  - Visualiza métricas de Prometheus.

Diagrama lógico (simplificado):

Frontend → Backend → Postgres
            ↓
          RabbitMQ (async)

Prometheus ← Backend (métricas) → Grafana (dashboards)

Beneficios distribuidos:
- Desacoplamiento (servicios independientes con contratos bien definidos).
- Escalabilidad horizontal (p. ej., `docker compose up --scale backend=2`).
- Resiliencia: si un servicio falla, los demás pueden continuar (hasta donde lo permita su dependencia).
- Observabilidad: métricas y paneles para detectar cuellos de botella.

---

## Compose extendido (imágenes preconstruidas + mensajería + observabilidad)

Además del compose local (con build de código fuente), puedes desplegar una variante con servicios adicionales y usando imágenes preconstruidas del backend/frontend. Este despliegue incluye:

- postgres (Base de datos)
- rabbitmq (Broker de mensajería + UI)
- backend (imagen pública), dependiente de postgres y rabbitmq
- frontend (imagen pública), dependiente del backend
- prometheus (scraping de métricas)
- grafana (visualización de métricas)

Cómo se vinculan:
- Backend ⇄ Postgres: mediante variables `POSTGRES_HOST=postgres`, `POSTGRES_PORT=5432`, `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`.
- Backend ⇄ RabbitMQ: `SPRING_RABBITMQ_HOST=rabbitmq`, `SPRING_RABBITMQ_PORT=5672`, `SPRING_RABBITMQ_USERNAME`, `SPRING_RABBITMQ_PASSWORD`.
- Frontend ⇄ Backend: el frontend llama a `http://localhost:<puerto-backend-expuesto>` desde el navegador; dentro de la red de Docker el backend se resuelve como `backend`.
- Prometheus ⇄ Backend: Prometheus necesita un `prometheus.yml` que apunte al endpoint de métricas del backend (p. ej., `backend:8080/actuator/prometheus`).
- Grafana ⇄ Prometheus: Grafana se conecta a Prometheus como fuente de datos.

Puertos típicos en host:
- Postgres: 5432
- RabbitMQ (AMQP): 5672
- RabbitMQ UI: 15672
- Backend: 8080
- Frontend: 3001 (expuesto como 80 en el contenedor)
- Prometheus: 9000 (redirigido a 9090 interno)
- Grafana: 4000 (redirigido a 3000 interno)

Variables críticas (no pongas secretos reales en el repo):
- `JWT_SECRET`: clave de al menos 32 bytes. Requerida por backend (HMAC-SHA256+).
- `SENDGRID_API_KEY`: si el backend envía emails, define esta variable en tu `.env` o en un gestor de secretos. No la publiques.
- Credenciales de Postgres y RabbitMQ: usa valores seguros.

Recomendaciones de observabilidad:
- Habilita Spring Boot Actuator y Micrometer Prometheus en el backend (dependencias y configuración) para exponer `/actuator/prometheus`.
- Crea un `prometheus.yml` en la raíz con, por ejemplo:

```
scrape_configs:
  - job_name: 'backend'
    metrics_path: /actuator/prometheus
    static_configs:
      - targets: ['backend:8080']
```

Luego mapea este archivo en el servicio `prometheus`.

Seguridad y CORS:
- CORS permitido por defecto para: `http://localhost:3000`, `http://localhost:5173`, `http://localhost:5174`.
- Configurable vía propiedad `cors.allowed-origins` o variable `CORS_ALLOWED_ORIGINS`.
- Autenticación: JWT vía `Authorization: Bearer <token>`.
- Nunca commitees `SENDGRID_API_KEY` ni `JWT_SECRET` reales.

Escalado y resiliencia:
- Escalar backend: `docker compose up -d --scale backend=2` (asegúrate de que la app sea stateless; con JWT es viable). Un balanceador (no incluido) mejoraría distribución.
- RabbitMQ permite reintentos y desacople para tareas pesadas (p. ej., envío de emails) evitando bloquear el hilo HTTP.

---

## API y funcionalidades

Base URL (local): `http://localhost:8080`

Autenticación: JWT via header `Authorization: Bearer <token>`.

- Algunos endpoints son públicos (por ejemplo registro de usuario, login, listado de proveedores, búsqueda de películas).
- Endpoints con `@PreAuthorize("isAuthenticated()")` o que usan `@AuthenticationPrincipal` requieren token.

### 1) Autenticación

POST `/auth/login`
- Body (JSON):
```
{
  "email": "user@email.com",
  "password": "TuPassw0rd!",
  "rememberMe": true
}
```
- Respuesta 200 (JSON):
```
{
  "token": "<jwt>",
  "timestamp": "2025-11-13T12:34:56",
  "email": "user@email.com",
  "ip": "127.0.0.1"
}
```
- Notas: si `rememberMe` es true, el token expira aprox. en 30 días; si no, en 24 horas.

### 2) Usuarios (`/user`)

POST `/user`
- Registrar un nuevo usuario.
- Body (JSON) AddUserDTO:
```
{
  "email": "user@email.com",
  "password": "TuPassw0rd!",
  "name": "Nombre Apellido",
  "birthDate": "2000-01-01",
  "platformsSubscribed": [
    { "id": 8, "name": "Netflix" }
  ],
  "favoriteGenres": ["ACTION", "DRAMA"]
}
```
- Respuesta 200 (ApiResponse):
```
{
  "timestamp": "13-11-2025 12:34:56",
  "status": 200,
  "message": "Usuario registrado exitosamente."
}
```

PATCH `/user/password` (requiere JWT)
- Cambiar contraseña.
- Headers: `Authorization: Bearer <token>`
- Body (JSON) ChangePasswordDTO:
```
{
  "currentPassword": "Passw0rd!",
  "newPassword": "Nuev4Pass!"
}
```
- Respuesta 200 (ApiResponse):
```
{
  "timestamp": "13-11-2025 12:34:56",
  "status": 200,
  "message": "Contrasena actualizada con exito."
}
```

PATCH `/user/name` (requiere JWT)
- Cambiar nombre.
- Body (JSON) ChangeNameDTO:
```
{
  "newName": "Nuevo Nombre"
}
```
- Respuesta 200 (ApiResponse):
```
{
  "timestamp": "13-11-2025 12:34:56",
  "status": 200,
  "message": "Nombre de usuario actualizado con exito."
}
```

GET `/user/me` (requiere JWT)
- Devuelve el perfil del usuario autenticado (UserProfileDTO):
```
{
  "id": 1,
  "email": "user@email.com",
  "name": "Nombre",
  "birthDate": "2000-01-01",
  "platformsSubscribed": [ { "id": 8, "name": "Netflix" } ],
  "favoriteGenres": ["ACTION", "DRAMA"]
}
```

GET `/user/platforms` (requiere JWT)
- Lista plataformas del usuario (array de Provider).

PUT `/user/platforms` (requiere JWT)
- Reemplaza las plataformas del usuario.
- Body (JSON) AddMyProvidersDTO:
```
{ "proversList": [8, 9, 384] }
```
- Respuesta 200 (ApiResponse):
```
{
  "timestamp": "13-11-2025 12:34:56",
  "status": 200,
  "message": "Plataformas actualizadas con exito"
}
```

### 3) Proveedores (`/provider`)

POST `/provider/save`
- Carga todas las plataformas de terceros (TMDB) a la base local.
- Respuesta 200 (ApiResponse): mensaje de confirmación.

GET `/provider/all`
- Devuelve todas las plataformas (array de Provider).

### 4) Películas (`/movie`)

GET `/movie/name/search`
- Búsqueda por nombre.
- Query params:
  - `name` (string, requerido)
  - `page` (int, por defecto 1)
  - `language` (string, por defecto `es-MX`)
  - `include_adult` (boolean, por defecto `false`)
- Respuesta 200 (MovieForCardPageDTO):
```
{
  "page": 1,
  "results": [ { /* MovieForCardDTO */ } ],
  "total_pages": 50,
  "total_results": 1000
}
```

GET `/movie/id/search`
- Detalles por ID.
- Query params:
  - `id` (int, requerido)
  - `language` (string, por defecto `es`)
- Respuesta 200 (MovieDetailsDTO): objeto con detalles completos de la película.

### Formato de errores (GlobalExceptionHandler)
- Respuesta de error (ApiErrorResponse):
```
{
  "timestamp": "13-11-2025 12:34:56",
  "status": 400,
  "error": "Bad Request",
  "message": "Error de validación",
  "path": "/user",
  "validationErrors": {
    "email": "Formato de email no valido."
  }
}
```
- Tipos manejados: validación con `@Valid`, violaciones de constraints, credenciales inválidas, acceso denegado, errores de conversión de JSON (enums, fechas), integridad de datos, y genéricos 500.

### Seguridad y CORS
- CORS permitido por defecto para: `http://localhost:3000`, `http://localhost:5173`, `http://localhost:5174`.
- Configurable vía propiedad `cors.allowed-origins` (o variable `CORS_ALLOWED_ORIGINS` en Docker).
- Autenticación con JWT en header `Authorization: Bearer <token>`.

### Ejemplos rápidos (Windows cmd + curl)

Login y uso del token:
```
curl -s -X POST http://localhost:8080/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"user@email.com\",\"password\":\"TuPassw0rd!\",\"rememberMe\":true}"
```

Con el token (reemplaza `<jwt>`):
```
curl -s http://localhost:8080/user/me ^
  -H "Authorization: Bearer <jwt>"
```

Buscar películas:
```
curl -s "http://localhost:8080/movie/name/search?name=matrix&page=1&language=es-MX&include_adult=false"
```

---

## Uso detallado de RabbitMQ en este proyecto

RabbitMQ se utiliza para desacoplar acciones asíncronas del flujo HTTP del backend. En este caso, cuando se registra un usuario, el backend publica un evento en RabbitMQ y un consumidor independiente envía el email de bienvenida con SendGrid. Esto evita bloquear la respuesta del endpoint mientras se envía el correo.

Topología (AMQP):
- Exchange: DirectExchange `user.exchange`
- Routing key: `user.created`
- Queue: `user.created.queue`
- Binding: `user.created.queue` ←(routing-key `user.created`)— `user.exchange`

Productor (publicación del evento):
- Clase: `UserEventPublisher` (usa `RabbitTemplate`)
- Método: `publishUserCreated(String email)` → `convertAndSend(exchange, routingKey, email)`
- Cuándo se invoca: en `UserService.addNewUser(...)` luego de persistir el usuario.
- Payload del mensaje: String con el email del usuario recién registrado.

Consumidor (procesamiento del evento):
- Clase: `UserEventListener`
- Anotación: `@RabbitListener(queues = "${app.rabbitmq.queue}")`
- Método: `handleUserCreated(String email, @Header("amqp_receivedRoutingKey") String routingKey)`
- Acción: llama a `EmailService.sendWelcomeEmail(email)` para enviar un correo de bienvenida usando SendGrid.
- Manejo de errores: se captura la excepción y se loguea; no hay reintentos automáticos ni DLQ por defecto.

Configuración (application.yml):
```
spring:
  rabbitmq:
    host: ${SPRING_RABBITMQ_HOST:localhost}
    port: ${SPRING_RABBITMQ_PORT:5672}
    username: ${SPRING_RABBITMQ_USERNAME:user}
    password: ${SPRING_RABBITMQ_PASSWORD:user}

app:
  rabbitmq:
    exchange: user.exchange
    routing-key: user.created
    queue: user.created.queue
```

Compose extendido (servicio `rabbitmq`):
- Puertos: 5672 (AMQP) y 15672 (UI de management)
- Credenciales por defecto (ejemplo): `user/user`
- El backend se conecta a `rabbitmq:5672` dentro de la red de Docker con las variables `SPRING_RABBITMQ_*`.

Prueba end-to-end (compose extendido):
1) Levanta servicios (backend, rabbitmq, frontend opcional, etc.).
2) Registra un usuario (POST `/user`).
3) Abre la UI de RabbitMQ: http://localhost:15672 (user/user). Verás la cola `user.created.queue` y los mensajes publicados.
4) Observa logs del backend: deberá aparecer "Mensaje recibido desde RabbitMQ" y el email. Si configuraste `SENDGRID_API_KEY`, se enviará el correo real.

Ejemplo de request para registrar usuario (Windows cmd):
```
curl -s -X POST http://localhost:8080/user ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"nuevo@correo.com\",\"password\":\"TuPassw0rd!\",\"name\":\"Nombre\",\"birthDate\":\"2000-01-01\"}"
```

Buenas prácticas y siguientes pasos (opcional):
- Reintentos y DLQ: añade una Dead Letter Exchange/Queue y políticas de reintento/backoff para mayor robustez.
- Serialización: usar JSON (p. ej., un objeto `UserCreatedEvent` con id/email/timestamp) en lugar de un String simple.
- Idempotencia: asegura que el consumidor pueda procesar el mismo evento más de una vez sin efectos adversos.
- Observabilidad: exporta métricas de colas/consumo y traceo distribuido para detectar cuellos de botella.
