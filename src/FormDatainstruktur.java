package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class FormDatainstruktur {

    private static JTextField txtId, txtNama, txtUsia, txtKeahlian, txtNoTelp;
    private static JTable tabelInstruktur;
    private static DefaultTableModel model;

    public static void main(String[] args) {

        JFrame frame = new JFrame("Form 2 - Data Instruktur Gym");
        frame.setSize(650, 500);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new JLabel("ID Instruktur:")).setBounds(20, 20, 100, 25);
        txtId = new JTextField();
        txtId.setBounds(130, 20, 150, 25);
        txtId.setEditable(false);
        frame.add(txtId);

        frame.add(new JLabel("Nama:")).setBounds(20, 60, 100, 25);
        txtNama = new JTextField();
        txtNama.setBounds(130, 60, 200, 25);
        frame.add(txtNama);

        frame.add(new JLabel("Usia:")).setBounds(20, 100, 100, 25);
        txtUsia = new JTextField();
        txtUsia.setBounds(130, 100, 100, 25);
        frame.add(txtUsia);

        frame.add(new JLabel("Keahlian:")).setBounds(20, 140, 100, 25);
        txtKeahlian = new JTextField();
        txtKeahlian.setBounds(130, 140, 200, 25);
        frame.add(txtKeahlian);

        frame.add(new JLabel("No. Telepon:")).setBounds(20, 180, 100, 25);
        txtNoTelp = new JTextField();
        txtNoTelp.setBounds(130, 180, 200, 25);
        frame.add(txtNoTelp);

        JButton btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(380, 20, 100, 30);
        frame.add(btnSimpan);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(380, 60, 100, 30);
        frame.add(btnUpdate);

        JButton btnHapus = new JButton("Delete");
        btnHapus.setBounds(380, 100, 100, 30);
        frame.add(btnHapus);

        JButton btnReset = new JButton("Reset");
        btnReset.setBounds(380, 140, 100, 30);
        frame.add(btnReset);

        // TABEL INSTRUKTUR
        model = new DefaultTableModel(new String[] { "ID", "Nama", "Usia", "Keahlian", "No. Telepon" }, 0);
        tabelInstruktur = new JTable(model);
        JScrollPane scroll = new JScrollPane(tabelInstruktur);
        scroll.setBounds(20, 230, 600, 200);
        frame.add(scroll);

        // EVENT KLIK TABEL
        tabelInstruktur.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int baris = tabelInstruktur.getSelectedRow();
                if (baris >= 0) {
                    txtId.setText(model.getValueAt(baris, 0).toString());
                    txtNama.setText(model.getValueAt(baris, 1).toString());
                    txtUsia.setText(model.getValueAt(baris, 2).toString());
                    txtKeahlian.setText(model.getValueAt(baris, 3).toString());
                    txtNoTelp.setText(model.getValueAt(baris, 4).toString());
                }
            }
        });

        // --- EVENT BUTTON ---
        btnSimpan.addActionListener(e -> simpanInstruktur());
        btnUpdate.addActionListener(e -> updateInstruktur());
        btnHapus.addActionListener(e -> hapusInstruktur());
        btnReset.addActionListener(e -> resetForm());

        tampilDataInstruktur();
        frame.setVisible(true);
    }

    private static Connection getConnection() throws SQLException {
        return KoneksiDatabase.getConnection();
    }

    private static boolean validasiInput() {
        String nama = txtNama.getText().trim();
        String usiaStr = txtUsia.getText().trim();
        String keahlian = txtKeahlian.getText().trim();
        String noTelp = txtNoTelp.getText().trim();

        if (nama.isEmpty() || usiaStr.isEmpty() || keahlian.isEmpty() || noTelp.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Semua field wajib diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            Integer.parseInt(usiaStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Usia harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private static void simpanInstruktur() {
        if (!validasiInput())
            return;
        String sql = "INSERT INTO instruktur_gym (nama, usia, keahlian, no_telp) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, txtNama.getText().trim());
            stmt.setInt(2, Integer.parseInt(txtUsia.getText().trim()));
            stmt.setString(3, txtKeahlian.getText().trim());
            stmt.setString(4, txtNoTelp.getText().trim());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data instruktur berhasil disimpan", "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
            resetForm();
            tampilDataInstruktur();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Gagal menyimpan data! Pastikan koneksi database benar.\n" + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void updateInstruktur() {
        if (txtId.getText().trim().isEmpty() || !validasiInput())
            return;
        String sql = "UPDATE instruktur_gym SET nama = ?, usia = ?, keahlian = ?, no_telp = ? WHERE id_instruktur = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, txtNama.getText().trim());
            stmt.setInt(2, Integer.parseInt(txtUsia.getText().trim()));
            stmt.setString(3, txtKeahlian.getText().trim());
            stmt.setString(4, txtNoTelp.getText().trim());
            stmt.setInt(5, Integer.parseInt(txtId.getText().trim()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data instruktur berhasil diupdate", "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
            resetForm();
            tampilDataInstruktur();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Gagal mengupdate data!\n" + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void hapusInstruktur() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pilih data di tabel terlebih dahulu untuk dihapus!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int konfirmasi = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data ini?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
        if (konfirmasi != JOptionPane.YES_OPTION)
            return;

        String sql = "DELETE FROM instruktur_gym WHERE id_instruktur = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(txtId.getText().trim()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data instruktur berhasil dihapus", "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
            resetForm();
            tampilDataInstruktur();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus data!\n" + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void resetForm() {
        txtId.setText("");
        txtNama.setText("");
        txtUsia.setText("");
        txtKeahlian.setText("");
        txtNoTelp.setText("");
        tabelInstruktur.clearSelection();
    }

    private static void tampilDataInstruktur() {
        model.setRowCount(0);
        String sql = "SELECT id_instruktur, nama, usia, keahlian, no_telp FROM instruktur_gym ORDER BY id_instruktur";
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("id_instruktur"),
                        rs.getString("nama"),
                        rs.getInt("usia"),
                        rs.getString("keahlian"),
                        rs.getString("no_telp")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Gagal menampilkan data dari database! Cek KoneksiDatabase.java.\n" + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}