# MS-Donaciones — Microservicio de Gestión de Donaciones

## Descripción

Microservicio central de la plataforma **Donaton**. Gestiona el ciclo de vida completo de una donación: recepción, validación, categorización y distribución.

**Puerto:** `8081`  
**Patrones implementados:** Repository Pattern · Factory Method

---

## Patrones de Diseño

### Repository Pattern
- **Interfaz:** `DonacionRepository`
- **Implementación:** `DonacionRepositoryImpl` (en memoria → reemplazable por JPA)
- **Beneficio:** La lógica de negocio nunca accede directamente a la base de datos; permite cambiar el motor de persistencia sin modificar el servicio.

### Factory Method (GoF)
- **Clase base abstracta:** `DonacionFactory`
- **Registro de fábricas:** `DonacionFactoryProvider`
- **Fábricas concretas:** `DonacionRopaFactory`, `DonacionAlimentoFactory`, `DonacionMedicoFactory`, `DonacionHigieneFactory`
- **Beneficio:** Agregar un nuevo tipo de donación solo requiere una nueva clase sin modificar el código existente (Open/Closed Principle).

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
- `DonacionServiceTest` — 8 pruebas (creación, consulta, actualización, eliminación)
- `DonacionRepositoryImplTest` — 7 pruebas (CRUD en memoria)

---

## Estructura del Proyecto

```
ms-donaciones/
├── src/main/java/donaton/msdonaciones/
│   ├── model/          → Donacion.java
│   ├── repository/     → DonacionRepository (interfaz) + Impl
│   ├── factory/        → DonacionFactory (abstracta) + fábricas concretas
│   │                     DonacionFactoryProvider (registro)
│   ├── service/        → DonacionService
│   └── controller/     → DonacionController
└── src/test/java/      → DonacionServiceTest, DonacionRepositoryImplTest
```
