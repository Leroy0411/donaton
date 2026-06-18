import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import App from './App';

// Evitamos llamadas reales a la red mockeando los módulos que las realizan.
vi.mock('./services/donatonApi', () => ({
    getDashboard: vi.fn(() => new Promise(() => {})),
    getDonaciones: vi.fn(() => Promise.resolve([])),
    crearDonacion: vi.fn(),
    actualizarEstadoDonacion: vi.fn(),
    eliminarDonacion: vi.fn(),
}));

describe('App', () => {
    it('muestra el Dashboard por defecto al iniciar', () => {
        render(<App />);
        expect(screen.getByText('🤝 Donaton')).toBeInTheDocument();
        expect(screen.getByText(/Cargando dashboard/i)).toBeInTheDocument();
    });

    it('cambia a la vista de Registrar Donación al hacer clic en el botón de navegación', async () => {
        render(<App />);

        fireEvent.click(screen.getByText('Registrar Donación'));

        await waitFor(() =>
            expect(screen.getByText('📥 Registrar Donación')).toBeInTheDocument()
        );
    });
});
