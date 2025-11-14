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
- `JWT_SECRET` (se recomienda cambiarlo de `changeme-super-secret`)
- `MOVIENOW_API_KEY` y `MOVIENOW_API_TOKEN` si quieres sobreescribir los valores por defecto del `application.yml`.

### Levantar los servicios
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
- Frontend (Nginx u otro server estático): http://localhost:3000
- Postgres expuesto en el puerto 5432 del host.

Nota: El frontend se construye con la URL del backend en `http://localhost:8080`. Si tu Dockerfile del frontend expone otro puerto distinto del 80, ajusta el mapeo en `docker-compose.yml` (por ejemplo `"3000:3000"`).

### Dependencias entre servicios
- El backend espera a que Postgres esté saludable antes de arrancar.
- El frontend depende del backend.

### Volúmenes
- `postgres_data`: datos persistentes de Postgres.

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
