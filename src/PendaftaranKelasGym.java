package src;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class PendaftaranKelasGym extends JFrame {
    // Deklarasi komponen input
    private JTextField txtIdPendaftaran, txtTanggal;
    private JComboBox<String> cbMember, cbKelas;
    private JTextArea txtCatatan;
    private JTable tabelPendaftaran;
    private DefaultTableModel modelTabel;
    
    // Deklarasi tombol
    private JButton btnSimpan, btnUpdate, btnHapus, btnReset, btnKeluar;

    private Connection koneksi;

    public PendaftaranKelasGym() {
        buatInterface();
        koneksiDatabase();
        muatDataMember();
        muatDataKelas();
        muatDataTabel();
    }

    private void buatInterface() {
        setTitle("Sistem Pendaftaran Kelas Gym");
        setSize(914, 700);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(250, 232, 220));

        // Membuat panel untuk form input
        JPanel panelInput = new JPanel();
        panelInput.setLayout(null);
        panelInput.setBounds(20, 20, 860, 280);
        panelInput.setBorder(BorderFactory.createTitledBorder("Informasi Pendaftaran"));
        panelInput.setBackground(Color.WHITE);

        panelInput.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 73, 94), 2),
            " Informasi Pendaftaran ",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 14),
            new Color(52, 73, 94)
        ));

        add(panelInput);

        // Komponen Form
        tambahKomponenForm(panelInput);
        tambahPanelTombol(panelInput);
        buatTabelData();
        // tambahkanEventListeners();
        
        setVisible(true);
    }

    private void tambahKomponenForm(JPanel panelInput) {
        // ID Pendaftaran
        JLabel lblId = new JLabel("ID Pendaftaran:");
        lblId.setBounds(20, 30, 120, 25);
        panelInput.add(lblId);

        txtIdPendaftaran = new JTextField();
        txtIdPendaftaran.setBounds(140, 30, 260, 25);
        txtIdPendaftaran.setEditable(false);
        txtIdPendaftaran.setBackground(new Color(230, 230, 230));
        panelInput.add(txtIdPendaftaran);

        // Label dan combo Member
        JLabel labelMember = new JLabel("Nama Member:");
        labelMember.setBounds(20, 70, 120, 25);
        panelInput.add(labelMember);

        cbMember = new JComboBox<>();
        cbMember.setBounds(140, 70, 260, 25);
        panelInput.add(cbMember);

        // Label dan combo Kelas
        JLabel labelKelas = new JLabel("Pilih Kelas:");
        labelKelas.setBounds(450, 30, 120, 25);
        panelInput.add(labelKelas);

        cbKelas = new JComboBox<>();
        cbKelas.setBounds(580, 30, 260, 25);
        panelInput.add(cbKelas);

        // Label dan field Tanggal
        JLabel labelTanggal = new JLabel("Tanggal Daftar:");
        labelTanggal.setBounds(450, 70, 120, 25);
        panelInput.add(labelTanggal);

        txtTanggal = new JTextField(LocalDate.now().toString());
        txtTanggal.setBounds(580, 70, 260, 25);
        txtTanggal.setToolTipText("Format: YYYY-MM-DD");
        panelInput.add(txtTanggal);

        // Label dan area Catatan
        JLabel labelCatatan = new JLabel("Catatan:");
        labelCatatan.setBounds(20, 110, 120, 25);
        panelInput.add(labelCatatan);

        txtCatatan = new JTextArea();
        txtCatatan.setLineWrap(true);
        txtCatatan.setWrapStyleWord(true);
        JScrollPane scrollCatatan = new JScrollPane(txtCatatan);
        scrollCatatan.setBounds(140, 110, 700, 80);
        panelInput.add(scrollCatatan);
    }

    private void tambahPanelTombol(JPanel panelInput) {
        JPanel panelTombol = new JPanel(new GridLayout(1, 6, 10, 10));
        panelTombol.setBounds(150, 210, 670, 40);
        panelTombol.setBackground(Color.WHITE);
        panelInput.add(panelTombol);

        btnSimpan  = buatButton("Simpan",  new Color(46, 204, 113));
        btnUpdate  = buatButton("Update",  new Color(52, 152, 219));
        btnHapus   = buatButton("Delete",  new Color(231, 76, 60));
        btnReset   = buatButton("Reset",   new Color(189, 195, 199));
        btnKeluar  = buatButton("Keluar",  new Color(52, 73, 94));

        panelTombol.add(btnSimpan);
        panelTombol.add(btnUpdate);
        panelTombol.add(btnHapus);
        panelTombol.add(btnReset);
        panelTombol.add(btnKeluar);
    }


    private JButton buatButton(String text, Color bg) {
        JButton btn = new JButton(text);

        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color hover = bg.darker();

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                btn.setBackground(bg.darker().darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                btn.setBackground(hover);
            }
        });

        return btn;
    }

    private void buatTabelData() {
        String[] kolom = {
            "ID", "ID Member", "Nama Member",
            "ID Kelas", "Info Kelas", "Tanggal", "Catatan"
        };

        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tabelPendaftaran = new JTable(modelTabel);
        tabelPendaftaran.setRowHeight(25);
        tabelPendaftaran.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelPendaftaran.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabelPendaftaran.setGridColor(new Color(220, 220, 220));
        // tabelPendaftaran.setFont(new Font("SansSerif", Font.PLAIN, 12));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        tabelPendaftaran.getColumnModel().getColumn(0).setCellRenderer(center);
        tabelPendaftaran.getColumnModel().getColumn(1).setCellRenderer(center);
        tabelPendaftaran.getColumnModel().getColumn(3).setCellRenderer(center);
        tabelPendaftaran.getColumnModel().getColumn(5).setCellRenderer(center);

        DefaultTableCellRenderer header = new DefaultTableCellRenderer();
        header.setBackground(new Color(52, 73, 94));
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setForeground(Color.WHITE);
        header.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < tabelPendaftaran.getColumnCount(); i++) {
            tabelPendaftaran.getColumnModel()
                    .getColumn(i).setHeaderRenderer(header);
        }

        int[] lebar = {50, 60, 120, 60, 240, 80, 240};
        for (int i = 0; i < lebar.length; i++) {
            tabelPendaftaran.getColumnModel()
                    .getColumn(i).setPreferredWidth(lebar[i]);
        }

        JScrollPane scroll = new JScrollPane(tabelPendaftaran);
        scroll.setBounds(20, 320, 860, 330);

        scroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 73, 94), 2),
            " Daftar Pendaftaran Kelas Gym ",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 14),
            new Color(52, 73, 94)
        ));

        add(scroll);

        btnSimpan.addActionListener(e -> simpanDataBaru());
        btnUpdate.addActionListener(e -> perbaruiData());
        btnHapus.addActionListener(e -> hapusData());;
        btnReset.addActionListener(e -> bersihkanForm());
        btnKeluar.addActionListener(e -> konfirmasiKeluar());

        tabelPendaftaran.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pilihBarisTabel();
            }
        });
    }

    private void pilihBarisTabel() {
        int barisTerpilih = tabelPendaftaran.getSelectedRow();
        if (barisTerpilih != -1) {
            txtIdPendaftaran.setText(modelTabel.getValueAt(barisTerpilih, 0).toString());
            
            String idMember = modelTabel.getValueAt(barisTerpilih, 1).toString();
            String namaMember = modelTabel.getValueAt(barisTerpilih, 2).toString();
            
            for (int i = 0; i < cbMember.getItemCount(); i++) {
                String item = cbMember.getItemAt(i);
                if (item.startsWith(idMember + " - " + namaMember)) {
                    cbMember.setSelectedIndex(i);
                    break;
                }
            }
            
            String idKelas = modelTabel.getValueAt(barisTerpilih, 3).toString();
            String namaKelas = modelTabel.getValueAt(barisTerpilih, 4).toString();
            
            for (int i = 0; i < cbKelas.getItemCount(); i++) {
                String item = cbKelas.getItemAt(i);
                if (item.startsWith(idKelas + " - " + namaKelas)) {
                    cbKelas.setSelectedIndex(i);
                    break;
                }
            }
            
            txtTanggal.setText(modelTabel.getValueAt(barisTerpilih, 5).toString());
            Object catatan = modelTabel.getValueAt(barisTerpilih, 6);
            txtCatatan.setText(catatan != null ? catatan.toString() : "");
        }
    }

    private void koneksiDatabase() {
        try {
            koneksi = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/db_gym",
                "postgres",
                "bakmi2"
            );
            System.out.println("Koneksi database berhasil");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Koneksi ke database gagal!\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void muatDataMember() {
        try {
            cbMember.removeAllItems();
            cbMember.addItem(" Pilih Member ");

            String sql = "SELECT id_member, nama, paket FROM member_gym ORDER BY id_member ASC";
            
            Statement perintah = koneksi.createStatement();
            ResultSet hasil = perintah.executeQuery(sql);
            
            while (hasil.next()) {
                cbMember.addItem(hasil.getInt("id_member") + " - " + 
                            hasil.getString("nama") + " (" + 
                            hasil.getString("paket") + ")");
            }
            
            hasil.close();
            perintah.close();

            cbMember.setSelectedIndex(0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Gagal memuat data tabel!\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void muatDataKelas() {
        try {
            cbKelas.removeAllItems();
            cbKelas.addItem(" Pilih Kelas ");

            String sql =
                "SELECT jk.id_kelas, jk.nama_kelas, jk.hari, " +
                "TO_CHAR(jk.jam_kelas, 'HH24:MI') AS jam_kelas, " +
                "ig.nama AS nama_instruktur " +
                "FROM jadwal_kelas jk " +
                "JOIN instruktur_gym ig ON jk.id_instruktur = ig.id_instruktur " +
                "ORDER BY jk.id_kelas ASC";

            Statement perintah = koneksi.createStatement();
            ResultSet hasil = perintah.executeQuery(sql);

            while (hasil.next()) {
                cbKelas.addItem(
                    hasil.getInt("id_kelas") + " - " +
                    hasil.getString("nama_kelas") + " - " +
                    hasil.getString("hari") + " " +
                    hasil.getString("jam_kelas") +
                    " (Instruktur: " + hasil.getString("nama_instruktur") + ")"
                );
            }

            hasil.close();
            perintah.close();

            cbKelas.setSelectedIndex(0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Gagal memuat data kelas!\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void muatDataTabel() {
        try {
            modelTabel.setRowCount(0);

            String sql =
                "SELECT p.id_pendaftaran, p.id_member, m.nama AS nama_member, " +
                "p.id_kelas, " +
                "j.nama_kelas || ' - ' || j.hari || ' ' || " +
                "TO_CHAR(j.jam_kelas, 'HH24:MI') || " +
                "' (Instruktur: ' || i.nama || ')' AS info_kelas, " +
                "p.tanggal_daftar, p.catatan " +
                "FROM pendaftaran_kelas p " +
                "JOIN member_gym m ON p.id_member = m.id_member " +
                "JOIN jadwal_kelas j ON p.id_kelas = j.id_kelas " +
                "JOIN instruktur_gym i ON j.id_instruktur = i.id_instruktur " +
                "ORDER BY p.id_pendaftaran DESC";

            Statement perintah = koneksi.createStatement();
            ResultSet hasil = perintah.executeQuery(sql);

            while (hasil.next()) {
                modelTabel.addRow(new Object[]{
                    hasil.getInt("id_pendaftaran"),
                    hasil.getInt("id_member"),
                    hasil.getString("nama_member"),
                    hasil.getInt("id_kelas"),
                    hasil.getString("info_kelas"),
                    hasil.getDate("tanggal_daftar"),
                    hasil.getString("catatan")
                });
            }

            hasil.close();
            perintah.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Gagal memuat data tabel!\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validasiInput() {
        if (cbMember.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this,
                "Pilih member terlebih dahulu!",
                "Validasi",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (cbKelas.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this,
                "Pilih kelas terlebih dahulu!",
                "Validasi",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String tanggal = txtTanggal.getText().trim();
        if (tanggal.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Tanggal harus diisi!",
                "Validasi",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!tanggal.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this,
                "Format tanggal harus YYYY-MM-DD!",
                "Validasi",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private int getIdMemberTerpilih() {
        if (cbMember.getSelectedIndex() <= 0) {
            return -1;
        }
        String selected = cbMember.getSelectedItem().toString();
        return Integer.parseInt(selected.split(" - ")[0]);
    }


    private int getIdKelasTerpilih() {
        if (cbKelas.getSelectedIndex() <= 0) {
            return -1;
        }
        String selected = cbKelas.getSelectedItem().toString();
        return Integer.parseInt(selected.split(" - ")[0]);
    }

    private void simpanDataBaru() {
        if (!validasiInput()) return;

        try {
            String sql = "INSERT INTO pendaftaran_kelas (id_member, id_kelas, tanggal_daftar, catatan) " +
                        "VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = koneksi.prepareStatement(sql);
            pstmt.setInt(1, getIdMemberTerpilih());
            pstmt.setInt(2, getIdKelasTerpilih());
            pstmt.setDate(3, Date.valueOf(txtTanggal.getText().trim()));
            
            String catatan = txtCatatan.getText().trim();
            pstmt.setString(4, catatan.isEmpty() ? null : catatan);
            
            pstmt.executeUpdate();
            pstmt.close();
            
            JOptionPane.showMessageDialog(this,
                "Pendaftaran kelas berhasil disimpan!",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
            
            muatDataTabel();
            bersihkanForm();
        } catch (SQLException ex) {
            tampilkanError("Gagal menyimpan data", ex);
        }
    }

    private void perbaruiData() {
        if (txtIdPendaftaran.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Pilih data dari tabel terlebih dahulu!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validasiInput()) return;

        try {
            String sql = "UPDATE pendaftaran_kelas SET id_member=?, id_kelas=?, " +
                        "tanggal_daftar=?, catatan=? WHERE id_pendaftaran=?";
            PreparedStatement pstmt = koneksi.prepareStatement(sql);
            pstmt.setInt(1, getIdMemberTerpilih());
            pstmt.setInt(2, getIdKelasTerpilih());
            pstmt.setDate(3, Date.valueOf(txtTanggal.getText().trim()));
            
            String catatan = txtCatatan.getText().trim();
            pstmt.setString(4, catatan.isEmpty() ? null : catatan);
            pstmt.setInt(5, Integer.parseInt(txtIdPendaftaran.getText()));
            
            int result = pstmt.executeUpdate();
            pstmt.close();
            
            if (result > 0) {
                JOptionPane.showMessageDialog(this,
                    "Data berhasil diupdate!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
                muatDataTabel();
                bersihkanForm();
            }
        } catch (SQLException ex) {
            tampilkanError("Gagal mengupdate data", ex);
        }
    }

    private void hapusData() {
        if (txtIdPendaftaran.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Pilih data dari tabel terlebih dahulu!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus data pendaftaran ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM pendaftaran_kelas WHERE id_pendaftaran=?";
                PreparedStatement pstmt = koneksi.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(txtIdPendaftaran.getText()));
                
                int result = pstmt.executeUpdate();
                pstmt.close();
                
                if (result > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Data berhasil dihapus!",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                    muatDataTabel();
                    bersihkanForm();
                }
            } catch (SQLException ex) {
                tampilkanError("Gagal menghapus data", ex);
            }
        }
    }

    private void bersihkanForm() {
        txtIdPendaftaran.setText("");
        if (cbMember.getItemCount() > 0) cbMember.setSelectedIndex(0);
        if (cbKelas.getItemCount() > 0) cbKelas.setSelectedIndex(0);
        txtTanggal.setText(LocalDate.now().toString());
        txtCatatan.setText("");
        tabelPendaftaran.clearSelection();
    }

    private void konfirmasiKeluar() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin ingin keluar dari aplikasi?",
            "Konfirmasi Keluar",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            tutupKoneksi();
            dispose();
        }
    }

    private void tampilkanError(String pesan, SQLException ex) {
        JOptionPane.showMessageDialog(this,
            pesan + "\n\nDetail Error:\n" + ex.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }

    private void tutupKoneksi() {
        try {
            if (koneksi != null && !koneksi.isClosed()) {
                koneksi.close();
                System.out.println("✓ Koneksi database ditutup");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new PendaftaranKelasGym());
    }
}
