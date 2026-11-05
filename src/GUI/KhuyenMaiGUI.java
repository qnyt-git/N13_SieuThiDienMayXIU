package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class KhuyenMaiGUI extends JPanel {
    private JTextField txtMaKM, txtTenKM, txtGiaTriGiam, txtNgayBatDau, txtNgayKetThuc, txtTimKiem;
    private JComboBox<String> cboTrangThai;
    private JTable tblKhuyenMai;
    private JButton btnThem, btnSua, btnXoa;

    public KhuyenMaiGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel lblTieuDe = new JLabel("Chương trình khuyến mãi");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(3, 4, 12, 8));
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230)),
            new EmptyBorder(14, 16, 14, 16)
        ));

        txtMaKM = new JTextField(); txtMaKM.setEnabled(false);
        txtTenKM = new JTextField();
        txtGiaTriGiam = new JTextField();
        txtNgayBatDau = new JTextField("dd/MM/yyyy");
        txtNgayKetThuc = new JTextField("dd/MM/yyyy");
        cboTrangThai = new JComboBox<>(new String[]{"Đang áp dụng", "Ngừng"});

        panelForm.add(new JLabel("Mã KM (tự động):")); panelForm.add(txtMaKM);
        panelForm.add(new JLabel("Tên khuyến mãi *:")); panelForm.add(txtTenKM);
        panelForm.add(new JLabel("Giá trị giảm (%) *:")); panelForm.add(txtGiaTriGiam);
        panelForm.add(new JLabel("Ngày bắt đầu *:")); panelForm.add(txtNgayBatDau);
        panelForm.add(new JLabel("Ngày kết thúc *:")); panelForm.add(txtNgayKetThuc);
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

        tblKhuyenMai = new JTable(new DefaultTableModel(
            new Object[]{"Mã KM", "Tên khuyến mãi", "Giá trị giảm (%)", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"}, 0));
        tblKhuyenMai.setRowHeight(28);

        JPanel panelTren = new JPanel(new BorderLayout(0, 12));
        panelTren.setOpaque(false);
        panelTren.add(panelForm, BorderLayout.NORTH);
        panelTren.add(panelBar, BorderLayout.CENTER);

        JPanel panelThan = new JPanel(new BorderLayout(0, 10));
        panelThan.setOpaque(false);
        panelThan.add(panelTren, BorderLayout.NORTH);
        panelThan.add(new JScrollPane(tblKhuyenMai), BorderLayout.CENTER);

        add(panelThan, BorderLayout.CENTER);
    }
}