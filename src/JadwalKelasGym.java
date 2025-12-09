package src;

import javax.swing.*; 
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.awt.Color;
import java.sql.*;

public class JadwalKelasGym {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Form Jadwal Kelas Gym"); 
        frame.setSize(900, 600);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color creamMuda = new Color(250, 240, 230);
        frame.getContentPane().setBackground(creamMuda);
        
        JLabel lblIDKelas = new JLabel("ID KELAS");
        lblIDKelas.setBounds(20, 20, 120, 25);
        frame.add(lblIDKelas);
        
        JTextField txtIDKelas = new JTextField(); 
        txtIDKelas.setBounds(150, 20, 200, 25);
        txtIDKelas.setEditable(false);
        txtIDKelas.setText("Auto");
        frame.add(txtIDKelas);
        
        JLabel lblNamaKelas = new JLabel("NAMA KELAS");
        lblNamaKelas.setBounds(20, 60, 120, 25);
        frame.add(lblNamaKelas);
        
        JTextField txtNamaKelas = new JTextField();
        txtNamaKelas.setBounds(150, 60, 200, 25);
        frame.add(txtNamaKelas);
        
        JLabel lblHari = new JLabel("HARI");
        lblHari.setBounds(20, 100, 120, 25);
        frame.add(lblHari);
        
        String[] hari = {"SENIN", "SELASA", "RABU", "KAMIS", "JUMAT", "SABTU", "MINGGU"};
        JComboBox<String> cbHari = new JComboBox<>(hari);
        cbHari.setBounds(150, 100, 200, 25);
        frame.add(cbHari);
        
        JLabel lblJamKelas = new JLabel("WAKTU");
        lblJamKelas.setBounds(20, 140, 120, 25);
        frame.add(lblJamKelas);
        
        JTextField txtJamKelas = new JTextField();
        txtJamKelas.setBounds(150, 140, 200, 25);
        frame.add(txtJamKelas);
        
        JLabel lblFormatJam = new JLabel("(Format: HH:MM:SS)");
        lblFormatJam.setBounds(360, 140, 150, 25);
        frame.add(lblFormatJam);
        
        JLabel lblInstruktur = new JLabel("INSTRUKTUR");
        lblInstruktur.setBounds(20, 180, 120, 25);
        frame.add(lblInstruktur);

        JComboBox<String> cbInstruktur = new JComboBox<>();
        cbInstruktur.setBounds(150, 180, 200, 25);
        frame.add(cbInstruktur);
        
        JButton btnSimpan = new JButton("CREATE");
btnSimpan.setBounds(20, 230, 100, 30);
btnSimpan.setBackground(new Color(16, 185, 129)); // Hijau Emerald
btnSimpan.setForeground(Color.WHITE);
frame.add(btnSimpan);
        
        JButton btnUpdate = new JButton("UPDATE");
btnUpdate.setBounds(130, 230, 100, 30);
btnUpdate.setBackground(new Color(251, 146, 60)); // Orange
btnUpdate.setForeground(Color.WHITE);
frame.add(btnUpdate);
        
        JButton btnDelete = new JButton("DELETE");
btnDelete.setBounds(240, 230, 100, 30);
btnDelete.setBackground(new Color(244, 63, 94)); // Merah Rose
btnDelete.setForeground(Color.WHITE);
frame.add(btnDelete);
        
        String[] kolom = {"ID Kelas", "Nama Kelas", "Hari", "Waktu", "Instruktur", "ID Instruktur"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0);
        JTable tableJadwal = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableJadwal);
        scrollPane.setBounds(20, 280, 840, 260);
        frame.add(scrollPane);
        
        String url = "jdbc:postgresql://localhost:5432/db_gym"; // menyambungkan ke database
        String user = "postgres";
        String password = "123";
        
        Runnable loadInstruktur = () -> {
            cbInstruktur.removeAllItems();
            try {
                Connection conn = DriverManager.getConnection(url, user, password);
                String sql = "SELECT id_instruktur, nama FROM instruktur_gym ORDER BY nama";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                
                while (rs.next()) {
                    int id = rs.getInt("id_instruktur");
                    String nama = rs.getString("nama");
                    cbInstruktur.addItem(id + " - " + nama);
                }
                
                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Belum berhasil, silahkan cek kembali!\n" + ex.getMessage(),
                    "GAGAL!!!", JOptionPane.ERROR_MESSAGE);
            }
        };
        
        Runnable loadData = () -> {
            model.setRowCount(0);
            try {
                Connection conn = DriverManager.getConnection(url, user, password);
                String sql = "SELECT jk.id_kelas, jk.nama_kelas, jk.hari, jk.jam_kelas, " + "ig.nama AS nama_instruktur, jk.id_instruktur " + "FROM jadwal_kelas jk " + "JOIN instruktur_gym ig ON jk.id_instruktur = ig.id_instruktur " + "ORDER BY jk.id_kelas";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("id_kelas"),
                        rs.getString("nama_kelas"),
                        rs.getString("hari"),
                        rs.getString("jam_kelas"),
                        rs.getString("nama_instruktur"),
                        rs.getInt("id_instruktur")
                    };
                    model.addRow(row);
                }
                
                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Gagal memuat data jadwal!\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        };
        
        loadInstruktur.run();
        loadData.run();
        
        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String namaKelas = txtNamaKelas.getText().trim();
                String hariKelas = cbHari.getSelectedItem().toString();
                String jamKelas = txtJamKelas.getText().trim();
                String instrukturStr = (String) cbInstruktur.getSelectedItem();
                
                if (namaKelas.isEmpty() || jamKelas.isEmpty() || instrukturStr == null) {
                    JOptionPane.showMessageDialog(frame, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int idInstruktur = Integer.parseInt(instrukturStr.split(" - ")[0]);
                
                try {
                    Connection conn = DriverManager.getConnection(url, user, password);
                    String sql = "INSERT INTO jadwal_kelas (nama_kelas, hari, jam_kelas, id_instruktur) " + "VALUES (?, ?, ?::time, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, namaKelas);
                    stmt.setString(2, hariKelas);
                    stmt.setString(3, jamKelas);
                    stmt.setInt(4, idInstruktur);
                    stmt.executeUpdate();
                    conn.close();
                    
                    JOptionPane.showMessageDialog(frame, "Jadwal kelas berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    loadData.run();
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Gagal menyimpan data!\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idKelas = txtIDKelas.getText().trim();
                String namaKelas = txtNamaKelas.getText().trim();
                String hariKelas = cbHari.getSelectedItem().toString();
                String jamKelas = txtJamKelas.getText().trim();
                String instrukturStr = (String) cbInstruktur.getSelectedItem();
                
                if (idKelas.equals("Auto") || idKelas.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Pilih data dari tabel terlebih dahulu!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (namaKelas.isEmpty() || jamKelas.isEmpty() || instrukturStr == null) {
                    JOptionPane.showMessageDialog(frame, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int idInstruktur = Integer.parseInt(instrukturStr.split(" - ")[0]);
                
                try {
                    Connection conn = DriverManager.getConnection(url, user, password);
                    String sql = "UPDATE jadwal_kelas SET nama_kelas=?, hari=?, jam_kelas=?::time, " + "id_instruktur=? WHERE id_kelas=?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, namaKelas);
                    stmt.setString(2, hariKelas);
                    stmt.setString(3, jamKelas);
                    stmt.setInt(4, idInstruktur);
                    stmt.setInt(5, Integer.parseInt(idKelas));
                    stmt.executeUpdate();
                    conn.close();
                    
                    JOptionPane.showMessageDialog(frame, "Data berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Gagal update data!\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idKelas = txtIDKelas.getText().trim();
                
                if (idKelas.equals("Auto") || idKelas.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Pilih data dari tabel terlebih dahulu!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int confirm = JOptionPane.showConfirmDialog(frame, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        Connection conn = DriverManager.getConnection(url, user, password);
                        String sql = "DELETE FROM jadwal_kelas WHERE id_kelas=?";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setInt(1, Integer.parseInt(idKelas));
                        stmt.executeUpdate();
                        conn.close();
                        
                        JOptionPane.showMessageDialog(frame, "Data berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Gagal menghapus data!\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        tableJadwal.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableJadwal.getSelectedRow();
                if (row != -1) {
                    txtIDKelas.setText(model.getValueAt(row, 0).toString());
                    txtNamaKelas.setText(model.getValueAt(row, 1).toString());
                    cbHari.setSelectedItem(model.getValueAt(row, 2).toString());
                    txtJamKelas.setText(model.getValueAt(row, 3).toString());
                    
                    int idInstruktur = (int) model.getValueAt(row, 5);
                    String namaInstruktur = model.getValueAt(row, 4).toString();
                    cbInstruktur.setSelectedItem(idInstruktur + " - " + namaInstruktur);
                }
            }
        });
        
        frame.setVisible(true);
    }
}
