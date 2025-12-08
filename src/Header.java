import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Header {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Selamat Datang");
        frame.setSize(650, 500);                 
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        Color creamMuda = new Color(250, 240, 230);
        frame.getContentPane().setBackground(creamMuda);

        ImageIcon icon = new ImageIcon(Header.class.getResource("/banner.png"));
        Image scaled = icon.getImage().getScaledInstance(450, 250, Image.SCALE_SMOOTH);
        JLabel foto = new JLabel(new ImageIcon(scaled));
        foto.setBounds(75, 20, 500, 250);
        frame.add(foto);

        JLabel judul1 = new JLabel("Selamat Datang di GUI Gym ");
        judul1.setBounds(150, 290, 400, 30);
        judul1.setFont(new Font("Arial", Font.BOLD, 26));
        frame.add(judul1);

        JLabel judul2 = new JLabel("Gabung Sekarang Juga!");
        judul2.setBounds(210, 320, 300, 25);
        judul2.setFont(new Font("Arial", Font.BOLD, 22));
        frame.add(judul2);

        JButton btnDaftar = new JButton("Daftar");
        btnDaftar.setBounds(250, 360, 120, 35);
        frame.add(btnDaftar);

        btnDaftar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Main.main(null);  
            }
        });

        frame.setVisible(true);
    }
}
