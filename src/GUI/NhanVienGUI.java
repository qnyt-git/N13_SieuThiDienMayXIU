package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NhanVienGUI extends JPanel {
    private JTextField txtMaNV, txtHoTen, txtSoDienThoai, txtNgaySinh, txtTrinhDo, txtHeSoLuong, txtLuongCoBan, txtTimKiem;
    private JComboBox<String> cboGioiTinh, cboChucVu, cboTrangThai;
    private JTable tblNhanVien;
    private JButton btnThem, btnSua, btnXoa;

    public NhanVienGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel lblTieuDe = new JLabel("Quản lý nhân viên");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(5, 4, 12, 8));
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230)),
            new EmptyBorder(14, 16, 14, 16)
        ));

        txtMaNV = new JTextField(); txtMaNV.setEnabled(false);
        txtHoTen = new JTextField();
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cboChucVu = new JComboBox<>(new String[]{"Quản lý", "Bán hàng", "Thu ngân", "Thủ kho", "Chăm sóc khách hàng"});
        txtSoDienThoai = new JTextField();
        txtNgaySinh = new JTextField();
        txtTrinhDo = new JTextField();
        txtHeSoLuong = new JTextField();
        txtLuongCoBan = new JTextField();
        cboTrangThai = new JComboBox<>(new String[]{"Đang làm", "Nghỉ việc"});

        panelForm.add(new JLabel("Mã NV (tự động):")); panelForm.add(txtMaNV);
        panelForm.add(new JLabel("Họ tên *:")); panelForm.add(txtHoTen);
        panelForm.add(new JLabel("Giới tính *:")); panelForm.add(cboGioiTinh);
        panelForm.add(new JLabel("Chức vụ *:")); panelForm.add(cboChucVu);
        panelForm.add(new JLabel("Số điện thoại *:")); panelForm.add(txtSoDienThoai);
        panelForm.add(new JLabel("Ngày sinh *:")); panelForm.add(txtNgaySinh);
        panelForm.add(new JLabel("Trình độ học vấn *:")); panelForm.add(txtTrinhDo);
        panelForm.add(new JLabel("Hệ số lương *:")); panelForm.add(txtHeSoLuong);
        panelForm.add(new JLabel("Lương cơ bản (đ) *:")); panelForm.add(txtLuongCoBan);
        panelForm.add(new JLabel("Trạng thái *:")); panelForm.add(cboTrangThai);

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

        tblNhanVien = new JTable(new DefaultTableModel(new Object[]{
            "Mã NV", "Họ tên", "Giới tính", "Chức vụ", "Số điện thoại", "Ngày sinh", "Trình độ học vấn", "Hệ số lương", "Lương cơ bản", "Trạng thái"
        }, 0));
        tblNhanVien.setRowHeight(28);

        JPanel panelTren = new JPanel(new BorderLayout(0, 12));
        panelTren.setOpaque(false);
        panelTren.add(panelForm, BorderLayout.NORTH);
        panelTren.add(panelBar, BorderLayout.CENTER);

        JPanel panelThan = new JPanel(new BorderLayout(0, 10));
        panelThan.setOpaque(false);
        panelThan.add(panelTren, BorderLayout.NORTH);
        panelThan.add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);

        add(panelThan, BorderLayout.CENTER);
    }
}