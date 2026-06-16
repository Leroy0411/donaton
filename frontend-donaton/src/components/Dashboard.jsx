// src/components/Dashboard.jsx
// Componente principal del dashboard.
// Consume el endpoint /bff/dashboard (BFF) que agrega datos en una sola llamada.

import React, { useState, useEffect } from 'react';
import { getDashboard } from '../services/donatonApi';

const estilos = {
    contenedor: { fontFamily: 'Arial, sans-serif', padding: '24px', maxWidth: '1100px', margin: '0 auto' },
    titulo: { color: '#1a3c5e', borderBottom: '2px solid #e67e22', paddingBottom: '8px' },
    grid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '16px', marginTop: '20px' },
    tarjeta: { background: '#fff', border: '1px solid #ddd', borderRadius: '8px', padding: '20px', boxShadow: '0 2px 6px rgba(0,0,0,.08)' },
    numero: { fontSize: '2.2rem', fontWeight: 'bold', color: '#e67e22', margin: '8px 0' },
    etiqueta: { color: '#666', fontSize: '0.9rem' },
    alerta: { background: '#fff3cd', border: '1px solid #ffc107', borderRadius: '6px', padding: '10px 14px', margin: '8px 0', color: '#856404' },
    seccion: { marginTop: '28px' },
};

function Tarjeta({ titulo, valor, color }) {
    return (
        <div style={estilos.tarjeta}>
            <div style={estilos.etiqueta}>{titulo}</div>
            <div style={{ ...estilos.numero, color: color || '#e67e22' }}>{valor}</div>
        </div>
    );
}

export default function Dashboard() {
    const [datos,    setDatos]    = useState(null);
    const [cargando, setCargando] = useState(true);
    const [error,    setError]    = useState(null);

    useEffect(() => {
        getDashboard()
            .then(setDatos)
            .catch(err => setError(err.message))
            .finally(() => setCargando(false));
    }, []);

    if (cargando) return <p style={{ padding: 24 }}>Cargando dashboard...</p>;
    if (error)    return <p style={{ padding: 24, color: 'red' }}>Error: {error}</p>;
    if (!datos)   return null;

    return (
        <div style={estilos.contenedor}>
            <h1 style={estilos.titulo}>📦 Dashboard Donaton</h1>
            <p style={{ color: '#555' }}>Vista agregada por el BFF — una sola llamada HTTP desde el frontend.</p>

            {/* Alertas */}
            {datos.alertas?.length > 0 && (
                <div style={estilos.seccion}>
                    <h3>⚠ Alertas del Sistema</h3>
                    {datos.alertas.map((a, i) => (
                        <div key={i} style={estilos.alerta}>{a}</div>
                    ))}
                </div>
            )}

            {/* Donaciones */}
            <div style={estilos.seccion}>
                <h2 style={{ color: '#1a3c5e' }}>Donaciones</h2>
                <div style={estilos.grid}>
                    <Tarjeta titulo="Total donaciones"   valor={datos.totalDonaciones}      color="#1a3c5e" />
                    <Tarjeta titulo="Recibidas"          valor={datos.donacionesRecibidas}   color="#2ecc71" />
                    <Tarjeta titulo="En proceso"         valor={datos.donacionesEnProceso}   color="#e67e22" />
                    <Tarjeta titulo="Distribuidas"       valor={datos.donacionesDistribuidas} color="#27ae60" />
                </div>
            </div>

            {/* Centros de Acopio */}
            <div style={estilos.seccion}>
                <h2 style={{ color: '#1a3c5e' }}>Centros de Acopio</h2>
                <div style={estilos.grid}>
                    <Tarjeta titulo="Total centros"  valor={datos.totalCentros}    color="#1a3c5e" />
                    <Tarjeta titulo="Activos"        valor={datos.centrosActivos}  color="#2ecc71" />
                    <Tarjeta titulo="Saturados"      valor={datos.centrosSaturados} color="#e74c3c" />
                </div>
            </div>

            {/* Envíos */}
            <div style={estilos.seccion}>
                <h2 style={{ color: '#1a3c5e' }}>Envíos</h2>
                <div style={estilos.grid}>
                    <Tarjeta titulo="Total envíos"   valor={datos.totalEnvios}       color="#1a3c5e" />
                    <Tarjeta titulo="Planificados"   valor={datos.enviosPlanificados} color="#3498db" />
                    <Tarjeta titulo="En camino"      valor={datos.enviosEnCamino}    color="#e67e22" />
                    <Tarjeta titulo="Entregados"     valor={datos.enviosEntregados}  color="#27ae60" />
                </div>
            </div>
        </div>
    );
}
