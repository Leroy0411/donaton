# MS-Logística — Microservicio de Logística y Distribución

## Descripción

Microservicio encargado de la gestión de centros de acopio, inventario disponible y planificación/seguimiento de envíos en la plataforma **Donaton**.

**Puerto:** `8082`  
**Patrones implementados:** Repository Pattern · Observer

---

## Patrones de Diseño

### Repository Pattern
- **Interfaces:** `CentroAcopioRepository`, `EnvioRepository`
- **Implementaciones:** `CentroAcopioRepositoryImpl`, `EnvioRepositoryImpl`
- **Beneficio:** Desacopla la lógica de negocio de la persistencia; intercambiable con JPA sin modificar los servicios.

### Observer (GoF)
- **Interfaz sujeto:** `EnvioObserver`
- **Observadores concretos:**
  - `AuditoriaEnvioObserver` — registra en bitácora todos los cambios de estado
  - `NotificacionEnvioObserver` — dispara alertas SMS/email según el nuevo estado
- **Beneficio:** Cada cambio de estado en un envío notifica automáticamente a todos los observadores registrados. Agregar nuevas reacciones (métricas, webhooks) solo requiere implementar `EnvioObserver` y registrarlo como bean de Spring.

---

## Requisitos

- Java 21
- Maven 3.9+

---

## Instalación y Ejecución

```bash
git clone <URL_REPOSITORIO>
cd ms-logistica

mvn clean compile
mvn test
mvn spring-boot:run
```

El servicio estará disponible en: `http://localhost:8082`

---

## Endpoints REST

### Centros de Acopio

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/logistica/centros` | Listar todos los centros |
| GET | `/api/logistica/centros/activos` | Centros con estado ACTIVO |
| GET | `/api/logistica/centros/con-capacidad` | Centros activos con espacio disponible |
| POST | `/api/logistica/centros` | Crear centro de acopio |
| PUT | `/api/logistica/centros/{id}/ocupacion?ocupacion=N` | Actualizar ocupación |
| DELETE | `/api/logistica/centros/{id}` | Eliminar centro |

### Envíos (con notificación Observer)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/logistica/envios` | Listar todos los envíos |
| GET | `/api/logistica/envios/estado/{estado}` | Filtrar por estado |
| POST | `/api/logistica/envios` | Crear envío (estado: PLANIFICADO) |
| PUT | `/api/logistica/envios/{id}/despachar` | Marcar EN_CAMINO → notifica observadores |
| PUT | `/api/logistica/envios/{id}/entregar` | Confirmar ENTREGADO → notifica observadores |
| PUT | `/api/logistica/envios/{id}/cancelar` | Cancelar → notifica observadores |
| DELETE | `/api/logistica/envios/{id}` | Eliminar envío |

---

## Pruebas Unitarias

```bash
mvn test
# Reporte JaCoCo: target/site/jacoco/index.html
```

Clases cubiertas:
- `LogisticaServiceTest` — 7 pruebas (Observer verificado con mocks, saturación de centros)

---

## Estructura del Proyecto

```
ms-logistica/
├── src/main/java/donaton/mslogistica/
│   ├── model/      → CentroAcopio.java, Envio.java
│   ├── repository/ → CentroAcopioRepository + Impl, EnvioRepository + Impl
│   ├── observer/   → EnvioObserver (interfaz)
│   │                 AuditoriaEnvioObserver, NotificacionEnvioObserver
│   ├── service/    → LogisticaService (gestiona el patrón Observer)
│   └── controller/ → LogisticaController
└── src/test/java/  → LogisticaServiceTest
```
