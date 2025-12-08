import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FormRegistrasiGym {

    private static Connection getConnection() throws SQLException {
        return KoneksiDatabase.getConnection();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Form Registrasi Member Gym");
        frame.setSize(650, 500);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color creamMuda = new Color(250, 240, 230);
        frame.getContentPane().setBackground(creamMuda);

        JLabel lblNama = new JLabel("Nama Member:");
        lblNama.setBounds(20, 20, 120, 25);
        frame.add(lblNama);

        JTextField txtNama = new JTextField();
        txtNama.setBounds(150, 20, 230, 25);
        frame.add(txtNama);

        JLabel lblUsia = new JLabel("Usia:");
        lblUsia.setBounds(20, 60, 120, 25);
        frame.add(lblUsia);

        JTextField txtUsia = new JTextField();
        txtUsia.setBounds(150, 60, 230, 25);
        frame.add(txtUsia);

        JLabel lblPaket = new JLabel("Paket Gym:");
        lblPaket.setBounds(20, 100, 120, 25);
        frame.add(lblPaket);

        String[] paketGym = {"Bulanan", "3 Bulan", "Tahunan"};
        JComboBox<String> cbPaket = new JComboBox<>(paketGym);
        cbPaket.setBounds(150, 100, 230, 25);
        frame.add(cbPaket);

        JButton btnSimpan = new JButton("Daftar Member");
        btnSimpan.setBounds(20, 150, 170, 30);
        frame.add(btnSimpan);

        JButton btnReset = new JButton("Reset");
        btnReset.setBounds(210, 150, 170, 30);
        frame.add(btnReset);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(400, 150, 170, 30);
        frame.add(btnDelete);

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Nama", "Usia", "Paket"}, 0
        );
        JTable tabelMember = new JTable(model);
        JScrollPane scroll = new JScrollPane(tabelMember);
        scroll.setBounds(20, 200, 600, 220);
        frame.add(scroll);

        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nama = txtNama.getText().trim();
                String usiaStr = txtUsia.getText().trim();
                String paket = cbPaket.getSelectedItem().toString();

                if (nama.isEmpty() || usiaStr.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Nama dan usia wajib diisi!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int usia;
                try {
                    usia = Integer.parseInt(usiaStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Usia harus berupa angka!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String sql = "INSERT INTO member_gym (nama, usia, paket) VALUES (?, ?, ?)";

                try (Connection conn = getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, nama);
                    stmt.setInt(2, usia);
                    stmt.setString(3, paket);
                    stmt.executeUpdate();

                    model.addRow(new Object[]{nama, usia, paket});

                    JOptionPane.showMessageDialog(frame,
                            "Member gym berhasil didaftarkan!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Gagal menyimpan ke database!\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtNama.setText("");
                txtUsia.setText("");
                cbPaket.setSelectedIndex(0);
                tabelMember.clearSelection();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tabelMember.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame,
                            "Pilih data yang ingin dihapus!",
                            "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Hapus dari table model
                model.removeRow(selectedRow);

                JOptionPane.showMessageDialog(frame,
                        "Data berhasil dihapus!",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        frame.setVisible(true);
    }
}

