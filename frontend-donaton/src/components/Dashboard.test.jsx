import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import Dashboard from './Dashboard';
import { getDashboard } from '../services/donatonApi';

vi.mock('../services/donatonApi');

describe('Dashboard', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('muestra el indicador de carga mientras llega la respuesta del BFF', () => {
        getDashboard.mockReturnValue(new Promise(() => {})); // nunca resuelve

        render(<Dashboard />);

        expect(screen.getByText(/Cargando dashboard/i)).toBeInTheDocument();
    });

    it('renderiza las estadísticas agregadas cuando el BFF responde', async () => {
        getDashboard.mockResolvedValue({
            totalDonaciones: 10,
            donacionesRecibidas: 4,
            donacionesEnProceso: 3,
            donacionesDistribuidas: 3,
            totalCentros: 2,
            centrosActivos: 2,
            centrosSaturados: 0,
            totalEnvios: 5,
            enviosPlanificados: 2,
            enviosEnCamino: 2,
            enviosEntregados: 1,
            alertas: [],
        });

        render(<Dashboard />);

        await waitFor(() => expect(screen.getByText('10')).toBeInTheDocument());
        expect(screen.getByText('Total donaciones')).toBeInTheDocument();
        expect(screen.getByText('Centros de Acopio')).toBeInTheDocument();
        expect(screen.getByText('Envíos')).toBeInTheDocument();
    });

    it('muestra las alertas del sistema cuando existen centros saturados', async () => {
        getDashboard.mockResolvedValue({
            totalDonaciones: 0, donacionesRecibidas: 0, donacionesEnProceso: 0, donacionesDistribuidas: 0,
            totalCentros: 1, centrosActivos: 0, centrosSaturados: 1,
            totalEnvios: 0, enviosPlanificados: 0, enviosEnCamino: 0, enviosEntregados: 0,
            alertas: ['🔴 1 centros de acopio saturados'],
        });

        render(<Dashboard />);

        await waitFor(() => expect(screen.getByText(/centros de acopio saturados/i)).toBeInTheDocument());
        expect(screen.getByText(/Alertas del Sistema/i)).toBeInTheDocument();
    });

    it('muestra un mensaje de error si la petición al BFF falla', async () => {
        getDashboard.mockRejectedValue(new Error('BFF no disponible'));

        render(<Dashboard />);

        await waitFor(() => expect(screen.getByText(/Error: BFF no disponible/i)).toBeInTheDocument());
    });
});
