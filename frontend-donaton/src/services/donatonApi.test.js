import { describe, it, expect, vi, beforeEach } from 'vitest';
import axios from 'axios';
import {
    getDashboard,
    getDonaciones,
    crearDonacion,
    actualizarEstadoDonacion,
    eliminarDonacion,
    getCentros,
    crearCentro,
    getEnvios,
    despacharEnvio,
    confirmarEntrega,
} from '../services/donatonApi';

vi.mock('axios');

describe('donatonApi (Facade de llamadas HTTP)', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('getDashboard llama al endpoint /bff/dashboard y retorna los datos', async () => {
        axios.get.mockResolvedValue({ data: { totalDonaciones: 3 } });

        const resultado = await getDashboard();

        expect(axios.get).toHaveBeenCalledWith(expect.stringContaining('/bff/dashboard'));
        expect(resultado).toEqual({ totalDonaciones: 3 });
    });

    it('getDonaciones llama al endpoint de MS-Donaciones', async () => {
        axios.get.mockResolvedValue({ data: [{ id: 1, tipo: 'ROPA' }] });

        const resultado = await getDonaciones();

        expect(axios.get).toHaveBeenCalledWith(expect.stringContaining('/api/donaciones'));
        expect(resultado).toHaveLength(1);
    });

    it('crearDonacion realiza un POST con los datos enviados', async () => {
        const nueva = { tipo: 'ALIMENTO', origen: 'Maipú', cantidad: 10, centroAcopioId: 1 };
        axios.post.mockResolvedValue({ data: { id: 5, ...nueva } });

        const resultado = await crearDonacion(nueva);

        expect(axios.post).toHaveBeenCalledWith(expect.stringContaining('/api/donaciones'), nueva);
        expect(resultado.id).toBe(5);
    });

    it('actualizarEstadoDonacion realiza un PUT con el estado como query param', async () => {
        axios.put.mockResolvedValue({ data: { id: 1, estado: 'EN_PROCESO' } });

        await actualizarEstadoDonacion(1, 'EN_PROCESO');

        expect(axios.put).toHaveBeenCalledWith(expect.stringContaining('/api/donaciones/1/estado?estado=EN_PROCESO'));
    });

    it('eliminarDonacion realiza un DELETE al endpoint correcto', async () => {
        axios.delete.mockResolvedValue({ data: { mensaje: 'ok' } });

        await eliminarDonacion(7);

        expect(axios.delete).toHaveBeenCalledWith(expect.stringContaining('/api/donaciones/7'));
    });

    it('getCentros llama al endpoint de centros de MS-Logística', async () => {
        axios.get.mockResolvedValue({ data: [] });

        await getCentros();

        expect(axios.get).toHaveBeenCalledWith(expect.stringContaining('/api/logistica/centros'));
    });

    it('crearCentro realiza un POST con los datos del centro', async () => {
        const centro = { nombre: 'Centro X', capacidadMaxima: 50 };
        axios.post.mockResolvedValue({ data: { id: 1, ...centro } });

        await crearCentro(centro);

        expect(axios.post).toHaveBeenCalledWith(expect.stringContaining('/api/logistica/centros'), centro);
    });

    it('getEnvios llama al endpoint de envíos de MS-Logística', async () => {
        axios.get.mockResolvedValue({ data: [] });

        await getEnvios();

        expect(axios.get).toHaveBeenCalledWith(expect.stringContaining('/api/logistica/envios'));
    });

    it('despacharEnvio realiza un PUT al endpoint de despacho', async () => {
        axios.put.mockResolvedValue({ data: { estado: 'EN_CAMINO' } });

        await despacharEnvio(2);

        expect(axios.put).toHaveBeenCalledWith(expect.stringContaining('/api/logistica/envios/2/despachar'));
    });

    it('confirmarEntrega realiza un PUT con observaciones como query param', async () => {
        axios.put.mockResolvedValue({ data: { estado: 'ENTREGADO' } });

        await confirmarEntrega(2, 'Todo OK');

        expect(axios.put).toHaveBeenCalledWith(expect.stringContaining('observaciones=Todo OK'));
    });
});
