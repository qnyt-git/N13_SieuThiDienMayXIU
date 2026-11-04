package GUI;

import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JFrame {
    private JTextField txtTaiKhoan;
    private JPasswordField txtMatKhau;
    private JCheckBox chkHienMatKhau;
    private JButton btnDangNhap, btnQuenMatKhau;

    public LoginGUI() {
        // Cấu hình cửa sổ chính
        setTitle("XIU - Đăng nhập hệ thống");
        setSize(860, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Hiển thị giữa màn hình
        setResizable(false);

        // Chia cửa sổ làm 2 phần (Trái và Phải) bằng GridLayout
        setLayout(new GridLayout(1, 2));

        // ================= PHẦN BÊN TRÁI (Màu xanh đậm) =================
        JPanel panelTrai = new JPanel();
        panelTrai.setBackground(new Color(12, 68, 136)); // Màu xanh dương đặc trưng
        panelTrai.setLayout(new BoxLayout(panelTrai, BoxLayout.Y_AXIS));
        panelTrai.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));

        JLabel lblLogo = new JLabel("XIU");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 70));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTieuDeTrai = new JLabel("Siêu thị điện máy XIU");
        lblTieuDeTrai.setForeground(Color.WHITE);
        lblTieuDeTrai.setFont(new Font("Arial", Font.BOLD, 30));
        lblTieuDeTrai.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblMoTa = new JLabel("Mua sắm thông minh - Cuộc sống tiện nghi");
        lblMoTa.setForeground(new Color(200, 220, 245));
        lblMoTa.setFont(new Font("Arial", Font.ITALIC, 16));
        lblMoTa.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelTrai.add(Box.createVerticalGlue());
        panelTrai.add(lblLogo);
        panelTrai.add(Box.createRigidArea(new Dimension(0, 20)));
        panelTrai.add(lblTieuDeTrai);
        panelTrai.add(Box.createRigidArea(new Dimension(0, 10)));
        panelTrai.add(lblMoTa);
        panelTrai.add(Box.createVerticalGlue());

        // ================= PHẦN BÊN PHẢI (Khung đăng nhập) =================
        JPanel panelPhai = new JPanel();
        panelPhai.setBackground(new Color(235, 240, 245)); // Nền xám nhạt bên phải
        panelPhai.setLayout(new GridBagLayout()); // Căn giữa card trắng

        // Khung card màu trắng chứa form
        JPanel panelKhung = new JPanel();
        panelKhung.setBackground(Color.WHITE);
        panelKhung.setPreferredSize(new Dimension(365, 430));
        panelKhung.setLayout(new BoxLayout(panelKhung, BoxLayout.Y_AXIS));
        panelKhung.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 215, 220), 1),
            BorderFactory.createEmptyBorder(30, 35, 30, 35)
        ));

        // --- BOX 1: Tiêu đề đăng nhập (Căn giữa) ---
        Box box1 = Box.createVerticalBox();
        box1.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblTieuDeDangNhap = new JLabel("Đăng nhập hệ thống");
        lblTieuDeDangNhap.setFont(new Font("Arial", Font.CENTER_BASELINE, 30));
        lblTieuDeDangNhap.setAlignmentX(Component.CENTER_ALIGNMENT);
        box1.add(lblTieuDeDangNhap);

        // --- BOX 2: Tên đăng nhập (Căn sát trái) ---
        Box box2 = Box.createVerticalBox();
        box2.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblTaiKhoan = new JLabel("Tên đăng nhập");
        lblTaiKhoan.setFont(new Font("Arial", Font.BOLD, 14));
        lblTaiKhoan.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtTaiKhoan = new JTextField();
        txtTaiKhoan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtTaiKhoan.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTaiKhoan.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        box2.add(lblTaiKhoan);
        box2.add(Box.createRigidArea(new Dimension(0, 5)));
        box2.add(txtTaiKhoan);

        // --- BOX 3: Mật khẩu (Căn sát trái) ---
        Box box3 = Box.createVerticalBox();
        box3.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblMatKhau = new JLabel("Mật khẩu");
        lblMatKhau.setFont(new Font("Arial", Font.BOLD, 14));
        lblMatKhau.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtMatKhau = new JPasswordField();
        txtMatKhau.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtMatKhau.setFont(new Font("Arial", Font.PLAIN, 14));
        txtMatKhau.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        box3.add(lblMatKhau);
        box3.add(Box.createRigidArea(new Dimension(0, 5)));
        box3.add(txtMatKhau);

        // --- BOX 4: Checkbox hiện mật khẩu (Căn sát trái) ---
        Box box4 = Box.createVerticalBox();
        box4.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkHienMatKhau = new JCheckBox("Hiện mật khẩu");
        chkHienMatKhau.setBackground(Color.WHITE);
        chkHienMatKhau.setFont(new Font("Arial", Font.PLAIN, 12));
        chkHienMatKhau.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkHienMatKhau.addActionListener(e -> {
            if (chkHienMatKhau.isSelected()) {
                txtMatKhau.setEchoChar((char) 0); // Hiện mật khẩu
            } else {
                txtMatKhau.setEchoChar('•'); // Ẩn mật khẩu dạng dấu chấm
            }
        });
        box4.add(chkHienMatKhau);

        // --- BOX 5: Nút Đăng nhập ---
        Box box5 = Box.createVerticalBox();
        box5.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnDangNhap = new JButton("Đăng nhập");
        btnDangNhap.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnDangNhap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnDangNhap.setBackground(new Color(14, 98, 195));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFont(new Font("Arial", Font.BOLD, 14));
        btnDangNhap.setFocusPainted(false);
        box5.add(btnDangNhap);

        // --- BOX 6: Nút Quên mật khẩu ---
        Box box6 = Box.createVerticalBox();
        box6.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnQuenMatKhau = new JButton("Quên mật khẩu?");
        btnQuenMatKhau.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnQuenMatKhau.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnQuenMatKhau.setBackground(Color.WHITE);
        btnQuenMatKhau.setForeground(new Color(14, 98, 195));
        btnQuenMatKhau.setFont(new Font("Arial", Font.BOLD, 13));
        btnQuenMatKhau.setBorder(BorderFactory.createLineBorder(new Color(14, 98, 195)));
        btnQuenMatKhau.setFocusPainted(false);
        box6.add(btnQuenMatKhau);

        // Đưa các box vào khung card trắng kèm khoảng cách hợp lý
        panelKhung.add(box1);
        panelKhung.add(Box.createRigidArea(new Dimension(0, 20)));
        panelKhung.add(box2);
        panelKhung.add(Box.createRigidArea(new Dimension(0, 15)));
        panelKhung.add(box3);
        panelKhung.add(Box.createRigidArea(new Dimension(0, 10)));
        panelKhung.add(box4);
        panelKhung.add(Box.createRigidArea(new Dimension(0, 20)));
        panelKhung.add(box5);
        panelKhung.add(Box.createRigidArea(new Dimension(0, 10)));
        panelKhung.add(box6);

        // Đưa khung trắng vào panel bên phải
        panelPhai.add(panelKhung);

        // Gắn 2 panel chính vào cửa sổ JFrame
        add(panelTrai);
        add(panelPhai);
    }

    // Hàm main chạy thử trực tiếp
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginGUI().setVisible(true);
        });
    }
}