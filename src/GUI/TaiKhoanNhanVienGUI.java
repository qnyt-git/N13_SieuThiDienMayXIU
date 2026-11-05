package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TaiKhoanNhanVienGUI extends JPanel {
    private JTextField txtMaTK, txtTenDangNhap, txtTimKiem;
    private JPasswordField txtMatKhau;
    private JComboBox<String> cboLoaiQuyen, cboTrangThai, cboNhanVien;
    private JTable tblTaiKhoan;
    private JButton btnThem, btnSua, btnXoa;

    public TaiKhoanNhanVienGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel lblTieuDe = new JLabel("Quản lý tài khoản nhân viên");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(3, 4, 12, 8));
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230)),
            new EmptyBorder(14, 16, 14, 16)
        ));

        txtMaTK = new JTextField(); txtMaTK.setEnabled(false);
        txtTenDangNhap = new JTextField();
        txtMatKhau = new JPasswordField();
        cboLoaiQuyen = new JComboBox<>();
        cboTrangThai = new JComboBox<>();
        cboNhanVien = new JComboBox<>();

        panelForm.add(new JLabel("Mã tài khoản (tự động):")); panelForm.add(txtMaTK);
        panelForm.add(new JLabel("Tên đăng nhập *:")); panelForm.add(txtTenDangNhap);
        panelForm.add(new JLabel("Mật khẩu *:")); panelForm.add(txtMatKhau);
        panelForm.add(new JLabel("Loại quyền *:")); panelForm.add(cboLoaiQuyen);
        panelForm.add(new JLabel("Trạng thái *:")); panelForm.add(cboTrangThai);
        panelForm.add(new JLabel("Nhân viên *:")); panelForm.add(cboNhanVien);

        JPanel panelBar = new JPanel(new BorderLayout());
        panelBar.setOpaque(false);
        JPanel pTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pTim.setOpaque(false);
        txtTimKiem = new JTextField(24);
        pTim.add(new JLabel("Tìm kiếm:")); pTim.add(txtTimKiem);

        JPanel pNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pNut.setOpaque(false);
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa"); btnXoa.setBackground(new Color(220, 38, 38)); btnXoa.setForeground(Color.WHITE);
        btnThem = new JButton("+ Thêm mới"); btnThem.setBackground(new Color(14, 98, 195)); btnThem.setForeground(Color.WHITE);
        pNut.add(btnSua); pNut.add(btnXoa); pNut.add(btnThem);
        panelBar.add(pTim, BorderLayout.WEST); panelBar.add(pNut, BorderLayout.EAST);

        tblTaiKhoan = new JTable(new DefaultTableModel(
            new Object[]{"Mã tài khoản", "Tên đăng nhập", "Mật khẩu", "Loại quyền", "Trạng thái", "Nhân viên"}, 0));
        tblTaiKhoan.setRowHeight(28);

        JPanel panelTren = new JPanel(new BorderLayout(0, 12));
        panelTren.setOpaque(false);
        panelTren.add(panelForm, BorderLayout.NORTH);
        panelTren.add(panelBar, BorderLayout.CENTER);

        JPanel panelThan = new JPanel(new BorderLayout(0, 10));
        panelThan.setOpaque(false);
        panelThan.add(panelTren, BorderLayout.NORTH);
        panelThan.add(new JScrollPane(tblTaiKhoan), BorderLayout.CENTER);

        add(panelThan, BorderLayout.CENTER);
    }
}