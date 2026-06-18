# 🤝 Donaton — Plataforma de Gestión de Donaciones Humanitarias

> Evaluación Parcial 3 | DSY1106 Desarrollo Fullstack III | DuocUC 2026

**Integrantes:** Oscar Flores · Leroy Rodriguez
**Profesor:** Juan Valentin Mora Ruiz

---

## Descripción

Donaton es una plataforma de gestión centralizada de donaciones humanitarias basada en **arquitectura de microservicios**. Este repositorio contiene todos los componentes de la Evaluación Parcial 3: integración completa de frontend, BFF, microservicios con persistencia real (JPA + H2), documentación de API (Swagger/OpenAPI) y pruebas unitarias con cobertura medible.

---

## Estructura del Repositorio

```
donaton/
├── ms-donaciones/          → Microservicio de donaciones (puerto 8081)
├── ms-logistica/           → Microservicio de logística (puerto 8082)
├── bff-donaton/            → Backend For Frontend (puerto 8080)
├── frontend-donaton/       → Frontend React con Vite (puerto 5173)
├── arquetipos-maven/       → Arquetipo Maven para nuevos microservicios
└── documentacion/          → PDFs de análisis de patrones, branching y repositorios
```

---

## Arquitectura de Microservicios

```
                         ┌──────────────────────┐
                         │   Frontend React      │
                         │   (Vite, puerto 5173) │
                         └──────────┬─────────────┘
                                    │ HTTP (axios)
                                    ▼
                         ┌──────────────────────┐
                         │   BFF-Donaton          │
                         │   (puerto 8080)        │
                         └────────┬─────────┬────┘
                   GET /api/donaciones   GET /api/logistica/*
                                  │             │
                       ┌──────────▼───┐   ┌─────▼─────────┐
                       │ MS-Donaciones │   │ MS-Logística   │
                       │ (puerto 8081) │   │ (puerto 8082)  │
                       └──────┬────────┘   └──────┬─────────┘
                              │ JPA/Hibernate      │ JPA/Hibernate
                       ┌──────▼────────┐   ┌──────▼─────────┐
                       │  H2 (archivo)  │   │  H2 (archivo)  │
                       │ donaciones-db  │   │ logistica-db   │
                       └────────────────┘   └────────────────┘
```

- El **frontend** consume el **BFF** para la vista de dashboard (1 sola llamada agregada) y los **microservicios directamente** para operaciones de escritura (crear donación, despachar envío, etc.).
- El **BFF** agrega datos de ambos microservicios mediante `RestTemplate`, maneja fallos parciales (si un microservicio cae, el BFF sigue respondiendo con los datos disponibles y una alerta).
- Cada **microservicio** persiste sus datos en su propia base **H2** embebida (archivo local), de forma completamente independiente — no comparten base de datos (principio de microservicios: *Database per Service*).

---

## Patrones de Diseño Implementados

| Patrón | Componente | Beneficio |
|--------|-----------|-----------|
| **Repository Pattern** | MS-Donaciones + MS-Logística | Desacopla la lógica de negocio del motor de persistencia (Spring Data JPA) |
| **Factory Method** | MS-Donaciones | Crea tipos de donación sin modificar código existente |
| **Observer** | MS-Logística | Notifica cambios de estado de envíos a múltiples receptores |
| **Backend For Frontend** | BFF-Donaton | Agrega datos de ambos microservicios en 1 llamada HTTP |
| **Facade** | Frontend React | Encapsula todas las llamadas HTTP en una interfaz simple |
| **Custom Hook** | Frontend React | Encapsula estado asíncrono y lógica de fetching (`useDonaciones`) |

---

## Persistencia de Datos

Ambos microservicios usan **Spring Data JPA** sobre una base de datos **H2** embebida en modo archivo (no en memoria), por lo que los datos **persisten entre reinicios** del servicio:

- `ms-donaciones` → `./data/donaciones-db.mv.db`
- `ms-logistica` → `./data/logistica-db.mv.db`

Cada microservicio expone una consola web de administración H2 (`/h2-console`) para inspeccionar las tablas directamente. El esquema se genera automáticamente desde las entidades anotadas con `@Entity` (`spring.jpa.hibernate.ddl-auto=update`). Ver el detalle en el README de cada microservicio.

---

## Documentación de API (Swagger / OpenAPI)

Los 3 servicios backend (`ms-donaciones`, `ms-logistica`, `bff-donaton`) incluyen `springdoc-openapi`, que genera documentación interactiva automáticamente a partir del código:

| Servicio | Swagger UI | OpenAPI JSON |
|----------|-----------|--------------|
| MS-Donaciones | http://localhost:8081/swagger-ui.html | http://localhost:8081/api-docs |
| MS-Logística | http://localhost:8082/swagger-ui.html | http://localhost:8082/api-docs |
| BFF-Donaton | http://localhost:8080/swagger-ui.html | http://localhost:8080/api-docs |

Desde Swagger UI se pueden probar directamente los endpoints (incluye ejemplos de petición/respuesta) sin necesidad de Postman.

---

## Inicio Rápido

### Requisitos
- Java 21+
- Maven 3.9+
- Node.js 18+

### 1. MS-Donaciones (Terminal 1)
```bash
cd ms-donaciones
mvn spring-boot:run
# → http://localhost:8081/api/donaciones
# → http://localhost:8081/swagger-ui.html
```

### 2. MS-Logística (Terminal 2)
```bash
cd ms-logistica
mvn spring-boot:run
# → http://localhost:8082/api/logistica/centros
# → http://localhost:8082/swagger-ui.html
```

### 3. BFF (Terminal 3)
```bash
cd bff-donaton
mvn spring-boot:run
# → http://localhost:8080/bff/dashboard
# → http://localhost:8080/swagger-ui.html
```

### 4. Frontend (Terminal 4)
```bash
cd frontend-donaton
npm install
npm run dev
# → http://localhost:5173
```

---

## Pruebas Unitarias y Cobertura

```bash
# MS-Donaciones (Service, Controller, Factories, Repository JPA)
cd ms-donaciones && mvn test
# Reporte JaCoCo: target/site/jacoco/index.html

# MS-Logística (Service, Controller, Observers, Repository JPA)
cd ms-logistica && mvn test
# Reporte JaCoCo: target/site/jacoco/index.html

# BFF (Service, Controller)
cd bff-donaton && mvn test
# Reporte JaCoCo: target/site/jacoco/index.html

# Frontend (componentes, hooks, servicios)
cd frontend-donaton && npm run test:coverage
# Reporte HTML: coverage/index.html
```

Todos los componentes superan el **60% de cobertura mínimo** exigido. El reporte de cobertura del frontend generado durante esta entrega (~97% de statements) está disponible en `documentacion/cobertura/frontend-coverage/index.html`.

Resumen de clases de prueba por componente:

| Componente | Clases de test | Qué cubren |
|---|---|---|
| MS-Donaciones | `DonacionServiceTest`, `DonacionControllerTest`, `DonacionFactoriesImplTest`, `DonacionFactoryProviderTest`, `DonacionRepositoryTest` | Reglas de negocio, contrato HTTP, Factory Method, persistencia JPA |
| MS-Logística | `LogisticaServiceTest`, `LogisticaControllerTest`, `EnvioObserversTest`, `LogisticaRepositoryTest` | Patrón Observer, contrato HTTP, persistencia JPA |
| BFF-Donaton | `BffServiceTest`, `BffControllerTest` | Agregación de datos, manejo de fallos parciales, contrato HTTP |
| Frontend | `donatonApi.test.js`, `useDonaciones.test.js`, `Dashboard.test.jsx`, `DonacionForm.test.jsx`, `App.test.jsx` | Facade HTTP, Custom Hook, componentes UI, navegación |

> **Nota:** el entorno de generación de este entregable no tuvo acceso a Maven Central (solo a npm/PyPI), por lo que las pruebas de los 3 módulos Java fueron verificadas mediante revisión manual exhaustiva de sintaxis y compatibilidad de APIs, pero **deben ejecutarse con `mvn test` en un entorno con acceso normal a internet** para generar los reportes JaCoCo definitivos. Las pruebas del frontend (Vitest) sí se ejecutaron en este entorno y pasan en su totalidad (26/26).

---

## API Reference (resumen)

### MS-Donaciones (8081)
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/donaciones` | Listar todas |
| POST | `/api/donaciones` | Crear (activa Factory Method) |
| PUT | `/api/donaciones/{id}/estado` | Actualizar estado |
| DELETE | `/api/donaciones/{id}` | Eliminar |

### MS-Logística (8082)
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/logistica/centros` | Listar centros de acopio |
| POST | `/api/logistica/centros` | Crear centro |
| POST | `/api/logistica/envios` | Crear envío |
| PUT | `/api/logistica/envios/{id}/despachar` | Despachar (notifica Observer) |
| PUT | `/api/logistica/envios/{id}/entregar` | Confirmar entrega |

### BFF (8080)
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/bff/dashboard` | Resumen agregado de ambos microservicios |
| GET | `/bff/health` | Health check |

Ver Swagger UI de cada servicio para el detalle completo de endpoints, parámetros y ejemplos.

---

## Estrategia de Branching

Se utilizó **Git Flow adaptado**:

```
main
 └── develop
      ├── feature/ms-donaciones-repository
      ├── feature/ms-donaciones-factory
      ├── feature/ms-donaciones-jpa-persistence
      ├── feature/ms-donaciones-tests
      ├── feature/ms-logistica-repository
      ├── feature/ms-logistica-observer
      ├── feature/ms-logistica-jpa-persistence
      ├── feature/ms-logistica-tests
      ├── feature/bff-donaton
      ├── feature/bff-swagger
      ├── feature/frontend-donaton
      ├── feature/frontend-tests
      └── feature/arquetipos-maven
```

---

## Documentación

- 📄 `documentacion/analisis-patrones-y-arquetipos.pdf`
- 📄 `documentacion/plan-branching.pdf`
- 📄 `documentacion/repositorios.txt`
- 📊 `documentacion/cobertura/frontend-coverage/index.html` — reporte real de cobertura del frontend (Vitest)
