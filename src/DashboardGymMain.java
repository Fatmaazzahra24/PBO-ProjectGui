import javax.swing.*;
import java.awt.*;

public class DashboardGymMain extends JFrame {

    public DashboardGymMain() {

        setTitle("Aplikasi Manajemen Gym");
        setSize(650, 500);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color creamMuda = new Color(250, 240, 230);
        getContentPane().setBackground(creamMuda);

        try {
            ImageIcon icon = new ImageIcon(DashboardGymMain.class.getResource("/banner.png"));
            Image scaled = icon.getImage().getScaledInstance(450, 220, Image.SCALE_SMOOTH);
            JLabel foto = new JLabel(new ImageIcon(scaled));
            foto.setBounds(100, 20, 450, 220);
            add(foto);
        } catch (Exception e) {}

        JLabel lblTitle = new JLabel("Selamat Datang di GUI Gym", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBounds(75, 240, 500, 30);
        add(lblTitle);

        JLabel lblSub = new JLabel("Silakan pilih menu di bawah ini", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSub.setBounds(75, 270, 500, 25);
        add(lblSub);

        int btnWidth = 200;
        int btnHeight = 40;
        int gap = 30;
        int totalWidth = btnWidth * 2 + gap;
        int startX = (650 - totalWidth) / 2;
        int row1Y = 320;
        int row2Y = 370;

        JButton btnMember = new JButton("Registrasi Member");
        btnMember.setBounds(startX, row1Y, btnWidth, btnHeight);
        add(btnMember);
        btnMember.addActionListener(e -> FormRegistrasiGym.main(null));

        JButton btnInstruktur = new JButton("Data Instruktur");
        btnInstruktur.setBounds(startX + btnWidth + gap, row1Y, btnWidth, btnHeight);
        add(btnInstruktur);
        btnInstruktur.addActionListener(e -> FormDatainstruktur.main(null));

        JButton btnJadwal = new JButton("Jadwal Kelas Gym");
        btnJadwal.setBounds(startX, row2Y, btnWidth, btnHeight);
        add(btnJadwal);
        btnJadwal.addActionListener(e -> JadwalKelasGym.main(null));

        JButton btnKelas = new JButton("Pendaftaran Kelas Gym");
        btnKelas.setBounds(startX + btnWidth + gap, row2Y, btnWidth, btnHeight);
        add(btnKelas);
        btnKelas.addActionListener(e -> PendaftaranKelasGym.main(null));

        JButton btnKeluar = new JButton("Keluar");
        btnKeluar.setBounds(startX + btnWidth + gap, row2Y, btnWidth, btnHeight);
        add(btnKeluar);
        btnKeluar.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        new DashboardGymMain().setVisible(true);
    }
}
