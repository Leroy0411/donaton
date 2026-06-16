import axios from 'axios'

const BFF_URL       = import.meta.env.VITE_BFF_URL       || 'http://localhost:8080'
const MS_DONACIONES = import.meta.env.VITE_MS_DONACIONES  || 'http://localhost:8081'
const MS_LOGISTICA  = import.meta.env.VITE_MS_LOGISTICA   || 'http://localhost:8082'

export const getDashboard = () =>
    axios.get(`${BFF_URL}/bff/dashboard`).then(r => r.data)

export const getDonaciones = () =>
    axios.get(`${MS_DONACIONES}/api/donaciones`).then(r => r.data)

export const crearDonacion = (datos) =>
    axios.post(`${MS_DONACIONES}/api/donaciones`, datos).then(r => r.data)

export const actualizarEstadoDonacion = (id, estado) =>
    axios.put(`${MS_DONACIONES}/api/donaciones/${id}/estado?estado=${estado}`).then(r => r.data)

export const eliminarDonacion = (id) =>
    axios.delete(`${MS_DONACIONES}/api/donaciones/${id}`).then(r => r.data)

export const getCentros = () =>
    axios.get(`${MS_LOGISTICA}/api/logistica/centros`).then(r => r.data)

export const crearCentro = (datos) =>
    axios.post(`${MS_LOGISTICA}/api/logistica/centros`, datos).then(r => r.data)

export const getEnvios = () =>
    axios.get(`${MS_LOGISTICA}/api/logistica/envios`).then(r => r.data)

export const despacharEnvio = (id) =>
    axios.put(`${MS_LOGISTICA}/api/logistica/envios/${id}/despachar`).then(r => r.data)

export const confirmarEntrega = (id, obs = '') =>
    axios.put(`${MS_LOGISTICA}/api/logistica/envios/${id}/entregar?observaciones=${obs}`).then(r => r.data)
