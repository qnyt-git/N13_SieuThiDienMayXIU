package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SanPhamGUI extends JPanel {
    private JTextField txtMaSP, txtTenSP, txtGiaBan, txtSoLuongTon, txtBaoHanh, txtTimKiem;
    private JComboBox<String> cboTrangThai;
    private JTable tblSanPham;
    private JButton btnThem, btnSua, btnXoa;

    public SanPhamGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel lblTieuDe = new JLabel("Quản lý sản phẩm");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(3, 4, 12, 8));
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230)),
            new EmptyBorder(14, 16, 14, 16)
        ));

        txtMaSP = new JTextField(); txtMaSP.setEnabled(false);
        txtTenSP = new JTextField();
        txtGiaBan = new JTextField();
        txtSoLuongTon = new JTextField();
        txtBaoHanh = new JTextField();
        cboTrangThai = new JComboBox<>(new String[]{"DANG_BAN", "HET_HANG", "NGUNG_BAN"});

        panelForm.add(new JLabel("Mã SP (tự động):")); panelForm.add(txtMaSP);
        panelForm.add(new JLabel("Tên sản phẩm *:")); panelForm.add(txtTenSP);
        panelForm.add(new JLabel("Giá bán (đ) *:")); panelForm.add(txtGiaBan);
        panelForm.add(new JLabel("Số lượng tồn *:")); panelForm.add(txtSoLuongTon);
        panelForm.add(new JLabel("Bảo hành (tháng) *:")); panelForm.add(txtBaoHanh);
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

        tblSanPham = new JTable(new DefaultTableModel(
            new Object[]{"Mã SP", "Tên sản phẩm", "Giá bán", "Số lượng tồn", "Bảo hành (tháng)", "Trạng thái"}, 0));
        tblSanPham.setRowHeight(28);

        JPanel panelTren = new JPanel(new BorderLayout(0, 12));
        panelTren.setOpaque(false);
        panelTren.add(panelForm, BorderLayout.NORTH);
        panelTren.add(panelBar, BorderLayout.CENTER);

        JPanel panelThan = new JPanel(new BorderLayout(0, 10));
        panelThan.setOpaque(false);
        panelThan.add(panelTren, BorderLayout.NORTH);
        panelThan.add(new JScrollPane(tblSanPham), BorderLayout.CENTER);

        add(panelThan, BorderLayout.CENTER);
    }
}