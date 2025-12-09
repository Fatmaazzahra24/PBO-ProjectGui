package src;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class DashboardGymMain {

    public DashboardGymMain() {
        JFrame frame = new JFrame("Dashboard Gym");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

       // Palettw warna
        Color cream = new Color(250, 240, 230);
        Color kuning = new Color(243, 210, 107);
        Color kuningHover = new Color(255, 220, 120);
        Color darkText = new Color(51, 51, 51);

        frame.getContentPane().setBackground(cream);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(cream);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        JPanel panelBanner = new JPanel(new BorderLayout());
        panelBanner.setBackground(cream);
        panelBanner.setPreferredSize(new Dimension(0, 280));
        panelBanner.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JLabel banner = new JLabel();
        banner.setHorizontalAlignment(JLabel.CENTER);

        ImageIcon icon = new ImageIcon(getClass().getResource("banner.png"));
        Image img = icon.getImage().getScaledInstance(350, 250, Image.SCALE_SMOOTH);
        banner.setIcon(new ImageIcon(img));

        panelBanner.add(banner, BorderLayout.CENTER);
        mainPanel.add(panelBanner, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(cream);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        //judul
        JLabel judul = new JLabel("PILIH MENU", JLabel.CENTER);
        judul.setFont(new Font("Segoe UI", Font.BOLD, 36));
        judul.setForeground(darkText);
        judul.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        contentPanel.add(judul, BorderLayout.NORTH);

        JPanel panelMenuWrapper = new JPanel(new GridBagLayout());
        panelMenuWrapper.setBackground(cream);

        JPanel panelMenu = new JPanel(new GridLayout(2, 2, 25, 25));
        panelMenu.setBackground(cream);
        panelMenu.setPreferredSize(new Dimension(900, 400));

        JButton btnRegis = buatButtonModern("Registrasi Member",
            "Daftarkan member baru", kuning, kuningHover);
        JButton btnInstruktur = buatButtonModern("Data Instruktur",
            "Kelola data instruktur", kuning, kuningHover);
        JButton btnJadwal = buatButtonModern("Jadwal Kelas",
            "Atur jadwal kelas gym", kuning, kuningHover);
        JButton btnDaftar = buatButtonModern("Pendaftaran Kelas",
            "Daftar member ke kelas", kuning, kuningHover);

        panelMenu.add(btnRegis);
        panelMenu.add(btnInstruktur);
        panelMenu.add(btnJadwal);
        panelMenu.add(btnDaftar);

        panelMenuWrapper.add(panelMenu);
        contentPanel.add(panelMenuWrapper, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        frame.add(mainPanel, BorderLayout.CENTER);

        // button
        btnRegis.addActionListener(e -> new FormRegistrasiGym().setVisible(true));
        btnInstruktur.addActionListener(e -> new FormDatainstruktur().setVisible(true));
        btnJadwal.addActionListener(e -> new JadwalKelasGym().setVisible(true));
        btnDaftar.addActionListener(e -> new PendaftaranKelasGym().setVisible(true));

        frame.setVisible(true);
    }

    private JButton buatButtonModern(String text, String subtitle, Color bg, Color hoverBg) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(10, 5));
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        //teks
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        // Judul button
        JLabel titleLabel = new JLabel(text);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // isi button
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(80, 80, 80));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(subtitleLabel);

        btn.add(textPanel, BorderLayout.CENTER);
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hoverBg);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new DashboardGymMain());
    }
}