# BFF-Donaton — Backend For Frontend

## Descripción

Capa de composición entre el frontend React y los microservicios internos de la plataforma **Donaton**. Agrega y transforma datos de múltiples servicios en responses optimizados para cada vista del cliente.

**Puerto:** `8080`  
**Patrón implementado:** Backend For Frontend (BFF)

---

## Patrón BFF

El BFF actúa como adaptador inteligente entre el frontend y los microservicios:

```
React App
    │
    │  1 llamada HTTP
    ▼
BFF (puerto 8080)
    ├── MS-Donaciones (8081) → GET /api/donaciones
    └── MS-Logística  (8082) → GET /api/logistica/centros
                              → GET /api/logistica/envios
    │
    │  1 response agregado
    ▼
DashboardResumenDTO
```

**Beneficio:** El frontend realiza **1 llamada** en lugar de 3, recibiendo un payload optimizado. Si los microservicios no están disponibles, el BFF devuelve alertas y los datos disponibles sin fallar.

---

## Requisitos

- Java 21
- Maven 3.9+
- MS-Donaciones corriendo en puerto 8081 (opcional para pruebas unitarias)
- MS-Logística corriendo en puerto 8082 (opcional para pruebas unitarias)

---

## Instalación y Ejecución

```bash
# 1. Iniciar microservicios primero
cd ms-donaciones && mvn spring-boot:run &
cd ms-logistica  && mvn spring-boot:run &

# 2. Iniciar el BFF
cd bff-donaton
mvn clean compile
mvn test
mvn spring-boot:run
```

BFF disponible en: `http://localhost:8080`
Documentación interactiva (Swagger UI): `http://localhost:8080/swagger-ui.html`
Especificación OpenAPI (JSON): `http://localhost:8080/api-docs`

---

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/bff/dashboard` | Resumen agregado: donaciones + centros + envíos |
| GET | `/bff/health` | Health check del BFF |

### Respuesta `/bff/dashboard`

```json
{
  "totalDonaciones": 15,
  "donacionesRecibidas": 8,
  "donacionesEnProceso": 5,
  "donacionesDistribuidas": 2,
  "totalCentros": 4,
  "centrosActivos": 3,
  "centrosSaturados": 1,
  "totalEnvios": 10,
  "enviosPlanificados": 3,
  "enviosEnCamino": 4,
  "enviosEntregados": 3,
  "alertas": ["🔴 1 centros de acopio saturados"]
}
```

---

## Configuración

En `src/main/resources/application.properties`:

```properties
ms.donaciones.url=http://localhost:8081
ms.logistica.url=http://localhost:8082
```

Para sobreescribir con variables de entorno:

```bash
MS_DONACIONES_URL=http://ms-donaciones:8081 mvn spring-boot:run
```

---

## Pruebas Unitarias

```bash
mvn test
# Reporte JaCoCo: target/site/jacoco/index.html
```

Clases cubiertas:
- `BffServiceTest` — agregación correcta, manejo de fallos parciales de microservicios.
- `BffControllerTest` — contrato HTTP de `/bff/dashboard` y `/bff/health` con `@WebMvcTest`.
