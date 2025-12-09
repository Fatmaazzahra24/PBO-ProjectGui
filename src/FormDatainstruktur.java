package src;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Image;
import java.sql.*;

public class FormDatainstruktur {

    private static JTextField txtId, txtNama, txtUsia, txtKeahlian, txtNoTelp;
    private static JTable tabelInstruktur;
    private static DefaultTableModel model;
    private static JFrame frame;

    public static void main(String[] args) {

        frame = new JFrame("Form 2 - Data Instruktur Gym");
        frame.setSize(650, 500);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        Color creamMuda   = new Color(250, 240, 230);  
        Color warnaAksi   = new Color(255, 193, 7);
        Color warnaDanger = new Color(220, 53, 69);
        Color warnaReset  = new Color(108, 117, 125);

        frame.getContentPane().setBackground(creamMuda);

        frame.add(new JLabel("ID Instruktur:")).setBounds(20, 20, 100, 25);
        txtId = new JTextField();
        txtId.setBounds(130, 20, 250, 25);
        txtId.setEditable(false);
        frame.add(txtId);

        frame.add(new JLabel("Nama:")).setBounds(20, 60, 100, 25);
        txtNama = new JTextField();
        txtNama.setBounds(130, 60, 250, 25);
        frame.add(txtNama);

        frame.add(new JLabel("Usia:")).setBounds(20, 100, 100, 25);
        txtUsia = new JTextField();
        txtUsia.setBounds(130, 100, 250, 25);
        frame.add(txtUsia);

        frame.add(new JLabel("Keahlian:")).setBounds(20, 140, 100, 25);
        txtKeahlian = new JTextField();
        txtKeahlian.setBounds(130, 140, 250, 25);
        frame.add(txtKeahlian);

        frame.add(new JLabel("No. Telepon:")).setBounds(20, 180, 100, 25);
        txtNoTelp = new JTextField();
        txtNoTelp.setBounds(130, 180, 250, 25);
        frame.add(txtNoTelp);

        // Gambar
        try {
            ImageIcon originalIcon = new ImageIcon(FormDatainstruktur.class.getResource("/gym2.png"));
            Image originalImage = originalIcon.getImage();
            Image scaledImage  = originalImage.getScaledInstance(260, 260, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            JLabel lblImage = new JLabel(scaledIcon);
            lblImage.setBounds(420, 40, 200, 200);
            frame.add(lblImage);

        } catch (Exception e) {}

        JButton btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(20, 230, 100, 30);
        btnSimpan.setBackground(warnaAksi);
        frame.add(btnSimpan);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(130, 230, 100, 30);
        btnUpdate.setBackground(warnaAksi);
        frame.add(btnUpdate);

        JButton btnHapus = new JButton("Delete");
        btnHapus.setBounds(240, 230, 100, 30);
        btnHapus.setBackground(warnaDanger);
        btnHapus.setForeground(Color.WHITE);
        frame.add(btnHapus);

        JButton btnReset = new JButton("Reset");
        btnReset.setBounds(350, 230, 100, 30);
        btnReset.setBackground(warnaReset);
        btnReset.setForeground(Color.WHITE);
        frame.add(btnReset);

        model = new DefaultTableModel(new String[]{"ID", "Nama", "Usia", "Keahlian", "No. Telepon"}, 0);
        tabelInstruktur = new JTable(model);
        JScrollPane scroll = new JScrollPane(tabelInstruktur);
        scroll.setBounds(20, 280, 600, 170);
        frame.add(scroll);

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
        if (nama.matches(".*\\d+.*")) {
            JOptionPane.showMessageDialog(null, "Nama tidak boleh mengandung angka!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        int usia;
        try {
            usia = Integer.parseInt(usiaStr);
            if (usia <= 0) {
                JOptionPane.showMessageDialog(null, "Usia harus lebih dari 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Usia harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!noTelp.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Nomor Telepon harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (noTelp.length() > 13) {
            JOptionPane.showMessageDialog(null, "Nomor Telepon maksimal 13 digit!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private static void simpanInstruktur() {
        if (!validasiInput()) return;

        String sql = "INSERT INTO instruktur_gym (nama, usia, keahlian, no_telp) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, txtNama.getText().trim());
            stmt.setInt(2, Integer.parseInt(txtUsia.getText().trim()));
            stmt.setString(3, txtKeahlian.getText().trim());
            stmt.setString(4, txtNoTelp.getText().trim());
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data instruktur berhasil disimpan");
            resetForm();
            tampilDataInstruktur();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan data!\n" + ex.getMessage());
        }
    }

    private static void updateInstruktur() {
        if (txtId.getText().trim().isEmpty() || !validasiInput()) return;

        String sql = "UPDATE instruktur_gym SET nama = ?, usia = ?, keahlian = ?, no_telp = ? WHERE id_instruktur = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, txtNama.getText().trim());
            stmt.setInt(2, Integer.parseInt(txtUsia.getText().trim()));
            stmt.setString(3, txtKeahlian.getText().trim());
            stmt.setString(4, txtNoTelp.getText().trim());
            stmt.setInt(5, Integer.parseInt(txtId.getText().trim()));
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data instruktur berhasil diupdate");
            resetForm();
            tampilDataInstruktur();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Gagal update data!\n" + ex.getMessage());
        }
    }

    private static void hapusInstruktur() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pilih data terlebih dahulu!");
            return;
        }
        int konfirmasi = JOptionPane.showConfirmDialog(null, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (konfirmasi != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM instruktur_gym WHERE id_instruktur = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, Integer.parseInt(txtId.getText().trim()));
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
            resetForm();
            tampilDataInstruktur();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus data!\n" + ex.getMessage());
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
                model.addRow(new Object[]{
                        rs.getInt("id_instruktur"),
                        rs.getString("nama"),
                        rs.getInt("usia"),
                        rs.getString("keahlian"),
                        rs.getString("no_telp")
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Gagal menampilkan data!\n" + ex.getMessage());
        }
    }
}
