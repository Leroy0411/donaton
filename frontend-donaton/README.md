# Frontend Donaton — React SPA

## Descripción

Interfaz de usuario responsive para la plataforma **Donaton**, construida con React 18 + Vite. Se comunica con el BFF y directamente con los microservicios.

---

## Patrones de Diseño Aplicados

### Facade (`src/services/donatonApi.js`)
Encapsula todas las llamadas HTTP en una única interfaz semántica. Los componentes nunca usan `axios` directamente; solo invocan métodos como `getDashboard()` o `crearDonacion()`.

### Custom Hook (`src/hooks/useDonaciones.js`)
Encapsula el estado asíncrono y la lógica de fetching. Separa la gestión de datos de la presentación (equivalente al patrón Repository en el frontend).

---

## Requisitos

- Node.js 18+
- npm 9+

---

## Instalación y Ejecución

```bash
cd frontend-donaton

# Instalar dependencias
npm install

# Iniciar en modo desarrollo (Vite)
npm run dev
# → http://localhost:5173

# Construir para producción
npm run build

# Previsualizar build de producción
npm run preview

# Ejecutar pruebas unitarias (Vitest)
npm test

# Ejecutar pruebas en modo watch
npm run test:watch

# Ejecutar pruebas con reporte de cobertura
npm run test:coverage
```

---

## Variables de Entorno

Este proyecto usa **Vite**, por lo que las variables de entorno expuestas al cliente deben tener el prefijo `VITE_`. Crear un archivo `.env` en la raíz del proyecto:

```env
VITE_BFF_URL=http://localhost:8080
VITE_MS_DONACIONES=http://localhost:8081
VITE_MS_LOGISTICA=http://localhost:8082
```

Si no se define el archivo `.env`, `src/services/donatonApi.js` usa por defecto esos mismos valores (`localhost:8080/8081/8082`), por lo que el frontend funciona out-of-the-box en un entorno de desarrollo local.

---

## Pruebas Unitarias

El proyecto usa **Vitest** + **React Testing Library** para pruebas de componentes, hooks y servicios.

```bash
npm test
```

Cobertura obtenida (ver `npm run test:coverage`): **~97% de statements**, muy por encima del mínimo del 60% exigido.

Archivos de prueba:
- `src/services/donatonApi.test.js` — pruebas del Facade de llamadas HTTP (mockeando axios).
- `src/hooks/useDonaciones.test.js` — pruebas del Custom Hook (carga, creación, actualización, eliminación).
- `src/components/Dashboard.test.jsx` — pruebas del dashboard (carga, datos agregados, alertas, errores).
- `src/components/DonacionForm.test.jsx` — pruebas del formulario (envío, validación, mensajes de éxito/error).
- `src/App.test.jsx` — pruebas de navegación entre vistas.

El reporte HTML de cobertura se genera en `coverage/index.html` tras ejecutar `npm run test:coverage`.

---

## Estructura

```
frontend-donaton/
├── public/
├── src/
│   ├── components/
│   │   ├── Dashboard.jsx          → Vista principal (consume BFF)
│   │   ├── Dashboard.test.jsx
│   │   ├── DonacionForm.jsx       → Formulario de registro
│   │   └── DonacionForm.test.jsx
│   ├── hooks/
│   │   ├── useDonaciones.js       → Hook personalizado (Custom Hook pattern)
│   │   └── useDonaciones.test.js
│   ├── services/
│   │   ├── donatonApi.js          → Facade: encapsula llamadas HTTP
│   │   └── donatonApi.test.js
│   ├── test/
│   │   └── setup.js               → Configuración global de Vitest (jest-dom)
│   ├── App.jsx                    → Componente raíz con navegación
│   └── App.test.jsx
├── vite.config.js                 → Config de Vite + Vitest (jsdom, coverage)
└── package.json
```
