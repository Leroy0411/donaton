import { describe, it, expect, vi, beforeEach } from 'vitest';
import { renderHook, waitFor, act } from '@testing-library/react';
import { useDonaciones } from './useDonaciones';
import * as api from '../services/donatonApi';

vi.mock('../services/donatonApi');

describe('useDonaciones (Custom Hook)', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('carga las donaciones automáticamente al montar', async () => {
        api.getDonaciones.mockResolvedValue([{ id: 1, tipo: 'ROPA' }]);

        const { result } = renderHook(() => useDonaciones());

        expect(result.current.cargando).toBe(true);

        await waitFor(() => expect(result.current.cargando).toBe(false));

        expect(result.current.donaciones).toHaveLength(1);
        expect(result.current.error).toBeNull();
    });

    it('registra un error si falla la carga inicial', async () => {
        api.getDonaciones.mockRejectedValue(new Error('Falla de red'));

        const { result } = renderHook(() => useDonaciones());

        await waitFor(() => expect(result.current.cargando).toBe(false));

        expect(result.current.error).toBe('Falla de red');
    });

    it('crear() agrega la nueva donación al estado local', async () => {
        api.getDonaciones.mockResolvedValue([]);
        api.crearDonacion.mockResolvedValue({ id: 9, tipo: 'MEDICO' });

        const { result } = renderHook(() => useDonaciones());
        await waitFor(() => expect(result.current.cargando).toBe(false));

        await act(async () => {
            await result.current.crear({ tipo: 'MEDICO' });
        });

        expect(result.current.donaciones).toHaveLength(1);
        expect(result.current.donaciones[0].id).toBe(9);
    });

    it('actualizarEstado() refleja el nuevo estado en el item correspondiente', async () => {
        api.getDonaciones.mockResolvedValue([{ id: 1, tipo: 'ROPA', estado: 'RECIBIDA' }]);
        api.actualizarEstadoDonacion.mockResolvedValue({ id: 1, estado: 'EN_PROCESO' });

        const { result } = renderHook(() => useDonaciones());
        await waitFor(() => expect(result.current.cargando).toBe(false));

        await act(async () => {
            await result.current.actualizarEstado(1, 'EN_PROCESO');
        });

        expect(result.current.donaciones[0].estado).toBe('EN_PROCESO');
    });

    it('eliminar() remueve la donación del estado local', async () => {
        api.getDonaciones.mockResolvedValue([{ id: 1, tipo: 'ROPA' }]);
        api.eliminarDonacion.mockResolvedValue({});

        const { result } = renderHook(() => useDonaciones());
        await waitFor(() => expect(result.current.cargando).toBe(false));

        await act(async () => {
            await result.current.eliminar(1);
        });

        expect(result.current.donaciones).toHaveLength(0);
    });
});
