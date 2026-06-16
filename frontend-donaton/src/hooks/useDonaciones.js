// src/hooks/useDonaciones.js
//
// Hook personalizado que encapsula la lógica de estado y fetching
// de donaciones. Aplica separación de responsabilidades:
// los componentes no gestionan estado asíncrono directamente.

import { useState, useEffect, useCallback } from 'react';
import { getDonaciones, crearDonacion, actualizarEstadoDonacion, eliminarDonacion } from '../services/donatonApi';

export function useDonaciones() {
    const [donaciones, setDonaciones]   = useState([]);
    const [cargando,   setCargando]     = useState(false);
    const [error,      setError]        = useState(null);

    const cargar = useCallback(async () => {
        setCargando(true);
        setError(null);
        try {
            const data = await getDonaciones();
            setDonaciones(data);
        } catch (err) {
            setError(err.message || 'Error al cargar donaciones');
        } finally {
            setCargando(false);
        }
    }, []);

    useEffect(() => { cargar(); }, [cargar]);

    const crear = async (datos) => {
        const nueva = await crearDonacion(datos);
        setDonaciones(prev => [...prev, nueva]);
        return nueva;
    };

    const actualizarEstado = async (id, estado) => {
        const actualizada = await actualizarEstadoDonacion(id, estado);
        setDonaciones(prev =>
            prev.map(d => d.id === id ? { ...d, estado } : d)
        );
        return actualizada;
    };

    const eliminar = async (id) => {
        await eliminarDonacion(id);
        setDonaciones(prev => prev.filter(d => d.id !== id));
    };

    return { donaciones, cargando, error, cargar, crear, actualizarEstado, eliminar };
}
