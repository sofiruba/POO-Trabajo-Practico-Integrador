package modelos.pago;

import modelos.inscripcion.Inscripcion;

public interface PagoServicio {
    Recibo pagar(Inscripcion inscripcion, float monto, String tipo, int cuotas);
}