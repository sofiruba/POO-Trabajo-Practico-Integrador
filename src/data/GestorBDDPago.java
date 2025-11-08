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
    public boolean guardar(int idInscripcion, float monto, String tipoPago, int cuotas) {
        if (conn == null) {
            System.err.println("❌ No hay conexión a la base de datos.");
            return false;
        }

        String sql = "INSERT INTO pago (idInscripcion, monto, tipo_pago, cuotas, fecha_pago) VALUES (?, ?, ?, ?, CURDATE())";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idInscripcion);
            ps.setFloat(2, monto);
            ps.setString(3, tipoPago);
            ps.setInt(4, cuotas);
            ps.executeUpdate();
            System.out.println("💾 Pago guardado en la base de datos.");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar el pago: " + e.getMessage());
        }
        return false;
    }
}
