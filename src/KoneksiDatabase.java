package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KoneksiDatabase {

    // 1. Deklarasi Konfigurasi sebagai Public Static Final
    // Digunakan 'public static final' agar bisa diakses dari kelas mana pun
    // dan nilainya tidak bisa diubah (final).
    public static final String URL = "jdbc:postgresql://localhost:5432/db_gym";
    public static final String USER = "postgres";
    public static final String PASS = "bakmi2";

    /**
     * 2. Metode Public Static untuk Mendapatkan Koneksi
     * Metode ini mengembalikan objek Connection.
     * Ia melempar (throws) SQLException, yang berarti kelas pemanggil (GUI) 
     * harus menangani kemungkinan kegagalan koneksi.
     * * @return Objek Connection
     * @throws SQLException Jika koneksi gagal
     */
    public static Connection getConnection() throws SQLException {
        // Driver akan otomatis menemukan URL, USER, dan PASS yang sudah kita definisikan.
        return DriverManager.getConnection(URL, USER, PASS);
    }
    
    /**
     * Metode main untuk menguji koneksi (opsional)
     */
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Koneksi ke database berhasil!");
        } catch (SQLException e) {
            System.err.println("Gagal terhubung ke database!");
            e.printStackTrace();
        }
    }
}