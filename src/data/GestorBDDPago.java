package data;

import java.sql.*;

public class GestorBDDPago {
    private Connection conn;

    public GestorBDDPago() {
        try {
            // Cambiá la contraseña por la tuya
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/plataforma_cursos?serverTimezone=UTC",
                    "root",
                    "mysql"
            );
            System.out.println("✅ Conectado a la base de datos (pagos).");
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con la BDD: " + e.getMessage());
        }
    }

    // Ejemplo: guardar pago
    // Archivo: GestorBDDPago.java

// Cambiamos a que devuelva el ID del pago generado
// Archivo: GestorBDDPago.java (Modificando el método guardar)

// Cambiamos el tipo de retorno a boolean
public boolean guardar(int idInscripcion, float monto, String tipoPago, int cuotas) {
    if (conn == null) {
        System.err.println("❌ No hay conexión a la base de datos.");
        return false;
    }
    
    // Asumimos que resolviste el problema de la columna 'cuotas' en tu BDD. 
    // Si no, usa la versión sin 'cuotas' o descomenta esta línea:
    String sql = "INSERT INTO pago (idInscripcion, monto, metodo_pago, cuotas, fecha_pago) VALUES (?, ?, ?, ?, CURDATE())";

    try (PreparedStatement ps = conn.prepareStatement(sql)) { 
        ps.setInt(1, idInscripcion);
        ps.setFloat(2, monto);
        ps.setString(3, tipoPago);
        ps.setInt(4, cuotas); // Si 'cuotas' existe en tu BDD
        
        // Solo verificamos si la ejecución fue exitosa (más de 0 filas afectadas)
        if (ps.executeUpdate() > 0) {
            System.out.println("💾 Pago guardado en la base de datos.");
            return true; // Éxito
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al guardar el pago: " + e.getMessage());
    }
    return false; // Fallo
}
}