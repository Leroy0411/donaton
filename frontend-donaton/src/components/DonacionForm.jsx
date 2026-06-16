// src/components/DonacionForm.jsx
// Formulario para registrar nuevas donaciones.
// Usa el hook useDonaciones (separación de responsabilidades).

import React, { useState } from 'react';
import { useDonaciones } from '../hooks/useDonaciones';

const TIPOS = ['ROPA', 'ALIMENTO', 'MEDICO', 'HIGIENE'];

const estilos = {
    form:    { background: '#fff', border: '1px solid #ddd', borderRadius: '8px', padding: '24px', maxWidth: '500px', boxShadow: '0 2px 6px rgba(0,0,0,.08)' },
    titulo:  { color: '#1a3c5e', marginBottom: '16px' },
    grupo:   { marginBottom: '14px' },
    label:   { display: 'block', color: '#555', marginBottom: '4px', fontSize: '0.9rem' },
    input:   { width: '100%', padding: '8px 10px', border: '1px solid #ccc', borderRadius: '4px', boxSizing: 'border-box' },
    boton:   { background: '#e67e22', color: '#fff', border: 'none', padding: '10px 24px', borderRadius: '4px', cursor: 'pointer', fontWeight: 'bold' },
    exito:   { background: '#d4edda', color: '#155724', padding: '10px', borderRadius: '4px', marginTop: '12px' },
    error:   { background: '#f8d7da', color: '#721c24', padding: '10px', borderRadius: '4px', marginTop: '12px' },
};

export default function DonacionForm() {
    const { crear } = useDonaciones();
    const [form, setForm] = useState({ tipo: 'ROPA', origen: '', cantidad: '', centroAcopioId: '', descripcion: '' });
    const [mensaje, setMensaje] = useState(null);
    const [tipoMensaje, setTipoMensaje] = useState('exito');

    const cambiar = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const enviar = async (e) => {
        e.preventDefault();
        try {
            await crear({ ...form, cantidad: Number(form.cantidad), centroAcopioId: Number(form.centroAcopioId) });
            setMensaje('✅ Donación registrada exitosamente');
            setTipoMensaje('exito');
            setForm({ tipo: 'ROPA', origen: '', cantidad: '', centroAcopioId: '', descripcion: '' });
        } catch (err) {
            setMensaje('❌ Error: ' + (err.response?.data?.error || err.message));
            setTipoMensaje('error');
        }
    };

    return (
        <form style={estilos.form} onSubmit={enviar}>
            <h2 style={estilos.titulo}>📥 Registrar Donación</h2>

            <div style={estilos.grupo}>
                <label style={estilos.label}>Tipo de donación</label>
                <select name="tipo" value={form.tipo} onChange={cambiar} style={estilos.input}>
                    {TIPOS.map(t => <option key={t} value={t}>{t}</option>)}
                </select>
            </div>

            <div style={estilos.grupo}>
                <label style={estilos.label}>Origen</label>
                <input name="origen" value={form.origen} onChange={cambiar} required style={estilos.input} placeholder="Ej: Santiago" />
            </div>

            <div style={estilos.grupo}>
                <label style={estilos.label}>Cantidad</label>
                <input name="cantidad" type="number" min="1" value={form.cantidad} onChange={cambiar} required style={estilos.input} />
            </div>

            <div style={estilos.grupo}>
                <label style={estilos.label}>ID Centro de Acopio</label>
                <input name="centroAcopioId" type="number" min="1" value={form.centroAcopioId} onChange={cambiar} required style={estilos.input} />
            </div>

            <div style={estilos.grupo}>
                <label style={estilos.label}>Descripción</label>
                <textarea name="descripcion" value={form.descripcion} onChange={cambiar} style={{ ...estilos.input, minHeight: '70px' }} />
            </div>

            <button type="submit" style={estilos.boton}>Registrar Donación</button>

            {mensaje && <div style={estilos[tipoMensaje]}>{mensaje}</div>}
        </form>
    );
}
