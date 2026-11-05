package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NhaCungCapGUI extends JPanel {
    private JTextField txtMaNCC, txtTenNCC, txtSoDienThoai, txtDiaChi, txtEmail, txtTimKiem;
    private JComboBox<String> cboTrangThai;
    private JTable tblNhaCungCap;
    private JButton btnThem, btnSua, btnXoa;

    public NhaCungCapGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel lblTieuDe = new JLabel("Quản lý nhà cung cấp");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(3, 4, 12, 8));
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230)),
            new EmptyBorder(14, 16, 14, 16)
        ));

        txtMaNCC = new JTextField(); txtMaNCC.setEnabled(false);
        txtTenNCC = new JTextField();
        txtSoDienThoai = new JTextField();
        txtDiaChi = new JTextField();
        txtEmail = new JTextField();
        cboTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Ngừng"});

        panelForm.add(new JLabel("Mã NCC (tự động):")); panelForm.add(txtMaNCC);
        panelForm.add(new JLabel("Tên nhà cung cấp *:")); panelForm.add(txtTenNCC);
        panelForm.add(new JLabel("Số điện thoại *:")); panelForm.add(txtSoDienThoai);
        panelForm.add(new JLabel("Địa chỉ *:")); panelForm.add(txtDiaChi);
        panelForm.add(new JLabel("Email *:")); panelForm.add(txtEmail);
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

        tblNhaCungCap = new JTable(new DefaultTableModel(
            new Object[]{"Mã NCC", "Tên nhà cung cấp", "Số điện thoại", "Địa chỉ", "Email", "Trạng thái"}, 0));
        tblNhaCungCap.setRowHeight(28);

        JPanel panelTren = new JPanel(new BorderLayout(0, 12));
        panelTren.setOpaque(false);
        panelTren.add(panelForm, BorderLayout.NORTH);
        panelTren.add(panelBar, BorderLayout.CENTER);

        JPanel panelThan = new JPanel(new BorderLayout(0, 10));
        panelThan.setOpaque(false);
        panelThan.add(panelTren, BorderLayout.NORTH);
        panelThan.add(new JScrollPane(tblNhaCungCap), BorderLayout.CENTER);

        add(panelThan, BorderLayout.CENTER);
    }
}