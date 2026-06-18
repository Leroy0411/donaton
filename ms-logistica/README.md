# MS-Logística — Microservicio de Logística y Distribución

## Descripción

Microservicio encargado de la gestión de centros de acopio, inventario disponible y planificación/seguimiento de envíos en la plataforma **Donaton**.

**Puerto:** `8082`
**Patrones implementados:** Repository Pattern · Observer
**Persistencia:** Spring Data JPA + base de datos H2 (archivo embebido)

---

## Patrones de Diseño

### Repository Pattern
- **Interfaces:** `CentroAcopioRepository`, `EnvioRepository`, ambas extienden `JpaRepository`
- **Persistencia real:** Hibernate gestiona automáticamente el CRUD sobre la base H2.
- **Beneficio:** Desacopla la lógica de negocio de la persistencia; cambiar de H2 a otro motor relacional en producción solo requiere ajustar `application.properties`.

### Observer (GoF)
- **Interfaz sujeto:** `EnvioObserver`
- **Observadores concretos:**
  - `AuditoriaEnvioObserver` — registra en bitácora todos los cambios de estado
  - `NotificacionEnvioObserver` — dispara alertas SMS/email según el nuevo estado
- **Beneficio:** Cada cambio de estado en un envío notifica automáticamente a todos los observadores registrados. Agregar nuevas reacciones (métricas, webhooks) solo requiere implementar `EnvioObserver` y registrarlo como bean de Spring.

---

## Persistencia de Datos (JPA)

- **Entidades:** `CentroAcopio` (tabla `centros_acopio`) y `Envio` (tabla `envios`), ambas anotadas con `@Entity`.
- **Motor:** H2 en modo archivo (`./data/logistica-db.mv.db`) — los datos persisten entre reinicios.
- **DDL automático:** `spring.jpa.hibernate.ddl-auto=update`.
- **Consola de administración:** `http://localhost:8082/h2-console`, URL `jdbc:h2:file:./data/logistica-db` (usuario `sa`, sin contraseña).
- **Pruebas de integración:** `LogisticaRepositoryTest` usa `@DataJpaTest` con H2 en memoria (perfil de test).

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
Documentación interactiva (Swagger UI): `http://localhost:8082/swagger-ui.html`
Especificación OpenAPI (JSON): `http://localhost:8082/api-docs`

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
- `LogisticaServiceTest` — Observer verificado con mocks, saturación de centros, eliminación de centros/envíos.
- `LogisticaControllerTest` — contrato HTTP de centros y envíos con `@WebMvcTest`.
- `EnvioObserversTest` — comportamiento de `AuditoriaEnvioObserver` y `NotificacionEnvioObserver`.
- `LogisticaRepositoryTest` — pruebas de integración JPA con `@DataJpaTest` sobre H2 en memoria.

---

## Estructura del Proyecto

```
ms-logistica/
├── src/main/java/donaton/mslogistica/
│   ├── model/      → CentroAcopio.java, Envio.java (entidades JPA)
│   ├── repository/ → CentroAcopioRepository, EnvioRepository (Spring Data JPA)
│   ├── observer/   → EnvioObserver (interfaz)
│   │                 AuditoriaEnvioObserver, NotificacionEnvioObserver
│   ├── service/    → LogisticaService (gestiona el patrón Observer)
│   └── controller/ → LogisticaController (con anotaciones OpenAPI)
├── src/main/resources/ → application.properties (config H2 + JPA + Swagger)
├── src/test/java/  → LogisticaServiceTest, LogisticaControllerTest,
│                      EnvioObserversTest, LogisticaRepositoryTest
└── src/test/resources/ → application.properties (perfil de test, H2 en memoria)
```
