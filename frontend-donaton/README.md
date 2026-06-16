# Frontend Donaton — React SPA

## Descripción

Interfaz de usuario responsive para la plataforma **Donaton**, construida con React 18. Se comunica con el BFF y directamente con los microservicios.

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

# Iniciar en modo desarrollo
npm start
# → http://localhost:3000

# Construir para producción
npm run build

# Ejecutar pruebas
npm test
```

---

## Variables de Entorno

Crear un archivo `.env` en la raíz del proyecto:

```env
REACT_APP_BFF_URL=http://localhost:8080
REACT_APP_MS_DONACIONES=http://localhost:8081
REACT_APP_MS_LOGISTICA=http://localhost:8082
```

---

## Estructura

```
frontend-donaton/
├── public/
├── src/
│   ├── components/
│   │   ├── Dashboard.jsx      → Vista principal (consume BFF)
│   │   └── DonacionForm.jsx   → Formulario de registro
│   ├── hooks/
│   │   └── useDonaciones.js   → Hook personalizado (Custom Hook pattern)
│   ├── services/
│   │   └── donatonApi.js      → Facade: encapsula llamadas HTTP
│   └── App.jsx                → Componente raíz con navegación
└── package.json
```
