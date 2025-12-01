package Gym;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class FormPendaftaranKelas {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Form Pendaftaran Kelas Gym");
        frame.setSize(650, 500);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblMember = new JLabel("Member:");
        lblMember.setBounds(20, 20, 120, 25);
        frame.add(lblMember);

        JComboBox<String> cbMember = new JComboBox<>();
        cbMember.setBounds(230, 20, 380, 25);
        frame.add(cbMember);


        // ================== EVENT DELETE ===================
        btnHapus.addActionListener(e -> {

            int baris = table.getSelectedRow();
            if (baris == -1) {
                JOptionPane.showMessageDialog(frame, "Pilih data pada tabel!");
                return;
            }

            int id = (int) model.getValueAt(baris, 0);

            try {
                Connection conn = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/db_gym",
                        "postgres",
                        "bakmi2"
                );

                PreparedStatement pst =
                        conn.prepareStatement("DELETE FROM pendaftaran_kelas WHERE id_pendaftaran = ?");
                pst.setInt(1, id);
                pst.executeUpdate();

                conn.close();

                model.removeRow(baris);

                JOptionPane.showMessageDialog(frame, "Data berhasil dihapus!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Gagal hapus: " + ex.getMessage());
            }
        });

        // ================== EVENT RESET ===================
        btnReset.addActionListener(e -> {
            cbMember.setSelectedIndex(0);
            cbKelas.setSelectedIndex(0);
            txtTanggal.setText("");
            txtCatatan.setText("");
        });

        frame.setVisible(true);
    }
}