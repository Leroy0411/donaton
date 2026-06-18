import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import DonacionForm from './DonacionForm';
import { useDonaciones } from '../hooks/useDonaciones';

vi.mock('../hooks/useDonaciones');

describe('DonacionForm', () => {
    const crearMock = vi.fn();

    beforeEach(() => {
        vi.clearAllMocks();
        useDonaciones.mockReturnValue({ crear: crearMock });
    });

    function llenarFormulario() {
        fireEvent.change(screen.getByPlaceholderText('Ej: Santiago'), { target: { value: 'Santiago' } });
        const inputs = document.querySelectorAll('input');
        fireEvent.change(inputs[1], { target: { value: '15' } }); // cantidad
        fireEvent.change(inputs[2], { target: { value: '2' } });  // centroAcopioId
    }

    it('renderiza todos los campos del formulario', () => {
        render(<DonacionForm />);

        expect(screen.getByText('📥 Registrar Donación')).toBeInTheDocument();
        expect(screen.getByPlaceholderText('Ej: Santiago')).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /Registrar Donación/i })).toBeInTheDocument();
    });

    it('al enviar datos válidos llama a crear() con el payload correcto', async () => {
        crearMock.mockResolvedValue({ id: 1 });
        render(<DonacionForm />);

        llenarFormulario();
        fireEvent.click(screen.getByRole('button', { name: /Registrar Donación/i }));

        await waitFor(() => expect(crearMock).toHaveBeenCalledTimes(1));
        const payload = crearMock.mock.calls[0][0];
        expect(payload.tipo).toBe('ROPA');
        expect(payload.origen).toBe('Santiago');
        expect(payload.cantidad).toBe(15);
        expect(payload.centroAcopioId).toBe(2);
    });

    it('muestra mensaje de éxito cuando la donación se registra correctamente', async () => {
        crearMock.mockResolvedValue({ id: 1 });
        render(<DonacionForm />);

        llenarFormulario();
        fireEvent.click(screen.getByRole('button', { name: /Registrar Donación/i }));

        await waitFor(() =>
            expect(screen.getByText(/Donación registrada exitosamente/i)).toBeInTheDocument()
        );
    });

    it('muestra mensaje de error cuando la API rechaza la solicitud', async () => {
        crearMock.mockRejectedValue({ response: { data: { error: 'Tipo no soportado' } } });
        render(<DonacionForm />);

        llenarFormulario();
        fireEvent.click(screen.getByRole('button', { name: /Registrar Donación/i }));

        await waitFor(() =>
            expect(screen.getByText(/Tipo no soportado/i)).toBeInTheDocument()
        );
    });

    it('permite cambiar el tipo de donación mediante el select', () => {
        render(<DonacionForm />);

        const select = screen.getByDisplayValue('ROPA');
        fireEvent.change(select, { target: { value: 'MEDICO' } });

        expect(select.value).toBe('MEDICO');
    });
});
