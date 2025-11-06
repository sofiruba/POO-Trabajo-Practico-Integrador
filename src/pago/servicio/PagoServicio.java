package pago.servicio;

import inscripcion.Inscripcion;
import pago.Recibo;


public interface PagoServicio {
    Recibo pagar(Inscripcion inscripcion, float monto, String tipo, int cuotas);
}