# 🤝 Donaton — Plataforma de Gestión de Donaciones Humanitarias

> Evaluación Parcial 2 | DSY1106 Desarrollo Fullstack III | DuocUC 2026

**Integrantes:** Oscar Flores · Leroy Rodriguez  
**Profesor:** Juan Valentin Mora Ruiz

---

## Descripción

Donaton es una plataforma de gestión centralizada de donaciones humanitarias basada en **arquitectura de microservicios**. Este repositorio contiene todos los componentes de la Evaluación Parcial 2.

---

## Estructura del Repositorio

```
donaton/
├── ms-donaciones/          → Microservicio de donaciones (puerto 8081)
├── ms-logistica/           → Microservicio de logística (puerto 8082)
├── bff-donaton/            → Backend For Frontend (puerto 8080)
├── frontend-donaton/       → Frontend React con Vite (puerto 5173)
├── arquetipos-maven/       → Arquetipo Maven para nuevos microservicios
└── documentacion/          → PDFs de análisis de patrones y branching
```

---

## Patrones de Diseño Implementados

| Patrón | Componente | Beneficio |
|--------|-----------|-----------|
| **Repository Pattern** | MS-Donaciones + MS-Logística | Desacopla lógica de negocio de la persistencia |
| **Factory Method** | MS-Donaciones | Crea tipos de donación sin modificar código existente |
| **Observer** | MS-Logística | Notifica cambios de estado de envíos a múltiples receptores |
| **Backend For Frontend** | BFF-Donaton | Agrega datos de ambos microservicios en 1 llamada HTTP |
| **Facade** | Frontend React | Encapsula todas las llamadas HTTP en una interfaz simple |

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
```

### 2. MS-Logística (Terminal 2)
```bash
cd ms-logistica
mvn spring-boot:run
# → http://localhost:8082/api/logistica/centros
```

### 3. BFF (Terminal 3)
```bash
cd bff-donaton
mvn spring-boot:run
# → http://localhost:8080/bff/dashboard
```

### 4. Frontend (Terminal 4)
```bash
cd frontend-donaton
npm install
npm run dev
# → http://localhost:5173
```

---

## Pruebas Unitarias

```bash
# MS-Donaciones — 15 pruebas
cd ms-donaciones && mvn test

# MS-Logística — 7 pruebas
cd ms-logistica && mvn test

# BFF — 5 pruebas
cd bff-donaton && mvn test

# Ver cobertura JaCoCo
open target/site/jacoco/index.html
```

---

## API Reference

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

---

## Estrategia de Branching

Se utilizó **Git Flow adaptado**:

```
main
 └── develop
      ├── feature/ms-donaciones-repository
      ├── feature/ms-donaciones-factory
      ├── feature/ms-donaciones-tests
      ├── feature/ms-logistica-repository
      ├── feature/ms-logistica-observer
      ├── feature/ms-logistica-tests
      ├── feature/bff-donaton
      ├── feature/frontend-donaton
      └── feature/arquetipos-maven
```

---

## Documentación

- 📄 `documentacion/analisis-patrones-y-arquetipos.pdf`
- 📄 `documentacion/plan-branching.pdf`
- 📄 `documentacion/repositorios.txt`
