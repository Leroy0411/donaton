# MS-Donaciones — Microservicio de Gestión de Donaciones

## Descripción

Microservicio central de la plataforma **Donaton**. Gestiona el ciclo de vida completo de una donación: recepción, validación, categorización y distribución.

**Puerto:** `8081`
**Patrones implementados:** Repository Pattern · Factory Method
**Persistencia:** Spring Data JPA + base de datos H2 (archivo embebido)

---

## Patrones de Diseño

### Repository Pattern
- **Interfaz:** `DonacionRepository`, extiende `JpaRepository<Donacion, Long>`
- **Persistencia real:** Hibernate gestiona automáticamente el CRUD sobre la base H2; no existe código SQL manual.
- **Beneficio:** La lógica de negocio (`DonacionService`) nunca accede directamente al motor de base de datos. Cambiar de H2 a PostgreSQL/MySQL en producción solo requiere ajustar `application.properties`.

### Factory Method (GoF)
- **Clase base abstracta:** `DonacionFactory`
- **Registro de fábricas:** `DonacionFactoryProvider`
- **Fábricas concretas:** `DonacionRopaFactory`, `DonacionAlimentoFactory`, `DonacionMedicoFactory`, `DonacionHigieneFactory`
- **Beneficio:** Agregar un nuevo tipo de donación solo requiere una nueva clase sin modificar el código existente (Open/Closed Principle).

---

## Persistencia de Datos (JPA)

- **Entidad:** `Donacion` (anotada con `@Entity`, tabla `donaciones`).
- **Motor:** H2 en modo archivo (`./data/donaciones-db.mv.db`), por lo que los datos **sobreviven a reinicios** del microservicio.
- **DDL automático:** `spring.jpa.hibernate.ddl-auto=update` — Hibernate crea/actualiza el esquema según la entidad.
- **Consola de administración:** con el servicio corriendo, abrir `http://localhost:8081/h2-console` y usar la URL `jdbc:h2:file:./data/donaciones-db` (usuario `sa`, sin contraseña) para inspeccionar las tablas directamente.
- **Pruebas de integración:** `DonacionRepositoryTest` usa `@DataJpaTest` con una base H2 en memoria (perfil de test) para validar el correcto funcionamiento de la capa de persistencia sin afectar los datos reales.

---

## Requisitos

- Java 21
- Maven 3.9+

---

## Instalación y Ejecución

```bash
# Clonar repositorio
git clone <URL_REPOSITORIO>
cd ms-donaciones

# Compilar
mvn clean compile

# Ejecutar pruebas unitarias (con reporte JaCoCo)
mvn test

# Iniciar el servidor
mvn spring-boot:run
```

El servicio estará disponible en: `http://localhost:8081`
Documentación interactiva (Swagger UI): `http://localhost:8081/swagger-ui.html`
Especificación OpenAPI (JSON): `http://localhost:8081/api-docs`

---

## Endpoints REST

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/donaciones` | Listar todas las donaciones |
| GET | `/api/donaciones/{id}` | Obtener donación por ID |
| GET | `/api/donaciones/estado/{estado}` | Filtrar por estado (`RECIBIDA`, `EN_PROCESO`, `DISTRIBUIDA`) |
| GET | `/api/donaciones/centro/{centroId}` | Filtrar por centro de acopio |
| POST | `/api/donaciones` | Crear donación (usa Factory Method) |
| PUT | `/api/donaciones/{id}/estado` | Actualizar estado |
| DELETE | `/api/donaciones/{id}` | Eliminar donación |

### Ejemplo de creación (POST)

```json
{
  "tipo": "ROPA",
  "origen": "Santiago Centro",
  "cantidad": 150,
  "centroAcopioId": 1,
  "descripcion": "Ropa de invierno - poleras y chaquetas"
}
```

Tipos válidos: `ROPA` | `ALIMENTO` | `MEDICO` | `HIGIENE`

---

## Pruebas Unitarias

```bash
# Ejecutar pruebas
mvn test

# Ver reporte de cobertura (JaCoCo)
# Abrir: target/site/jacoco/index.html
```

Clases cubiertas:
- `DonacionServiceTest` — pruebas de creación, consulta, actualización y eliminación (con mocks de Mockito).
- `DonacionControllerTest` — pruebas del contrato HTTP con `@WebMvcTest` (status codes, payloads, errores).
- `DonacionFactoriesImplTest` — pruebas de las reglas de negocio de cada fábrica concreta.
- `DonacionFactoryProviderTest` — pruebas de resolución de fábricas por tipo.
- `DonacionRepositoryTest` — pruebas de integración JPA con `@DataJpaTest` sobre H2 en memoria.

---

## Estructura del Proyecto

```
ms-donaciones/
├── src/main/java/donaton/msdonaciones/
│   ├── model/          → Donacion.java (entidad JPA)
│   ├── repository/     → DonacionRepository (Spring Data JPA)
│   ├── factory/        → DonacionFactory (abstracta) + fábricas concretas
│   │                     DonacionFactoryProvider (registro)
│   ├── service/        → DonacionService
│   └── controller/     → DonacionController (con anotaciones OpenAPI)
├── src/main/resources/ → application.properties (config H2 + JPA + Swagger)
├── src/test/java/      → DonacionServiceTest, DonacionControllerTest,
│                          DonacionFactoriesImplTest, DonacionFactoryProviderTest,
│                          DonacionRepositoryTest
└── src/test/resources/ → application.properties (perfil de test, H2 en memoria)
```
