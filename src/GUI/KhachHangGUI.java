package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class KhachHangGUI extends JPanel {
    private JTextField txtMaKH, txtHoTen, txtSoDienThoai, txtDiemTichLuy, txtTimKiem;
    private JComboBox<String> cboLoaiKH;
    private JTable tblKhachHang;
    private JButton btnThem, btnSua, btnXoa;

    public KhachHangGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel lblTieuDe = new JLabel("Quản lý khách hàng");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(3, 4, 12, 8));
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230)),
            new EmptyBorder(14, 16, 14, 16)
        ));

        txtMaKH = new JTextField(); txtMaKH.setEnabled(false);
        txtHoTen = new JTextField();
        txtSoDienThoai = new JTextField();
        txtDiemTichLuy = new JTextField("0");
        cboLoaiKH = new JComboBox<>(new String[]{"KH_MOI", "KH_VIP"});

        panelForm.add(new JLabel("Mã KH (tự động):")); panelForm.add(txtMaKH);
        panelForm.add(new JLabel("Họ tên *:")); panelForm.add(txtHoTen);
        panelForm.add(new JLabel("Số điện thoại *:")); panelForm.add(txtSoDienThoai);
        panelForm.add(new JLabel("Điểm tích lũy *:")); panelForm.add(txtDiemTichLuy);
        panelForm.add(new JLabel("Loại khách hàng *:")); panelForm.add(cboLoaiKH);
        panelForm.add(new JLabel("")); panelForm.add(new JLabel(""));

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

        tblKhachHang = new JTable(new DefaultTableModel(
            new Object[]{"Mã KH", "Họ tên", "Số điện thoại", "Điểm tích lũy", "Loại khách hàng"}, 0));
        tblKhachHang.setRowHeight(28);

        JPanel panelTren = new JPanel(new BorderLayout(0, 12));
        panelTren.setOpaque(false);
        panelTren.add(panelForm, BorderLayout.NORTH);
        panelTren.add(panelBar, BorderLayout.CENTER);

        JPanel panelThan = new JPanel(new BorderLayout(0, 10));
        panelThan.setOpaque(false);
        panelThan.add(panelTren, BorderLayout.NORTH);
        panelThan.add(new JScrollPane(tblKhachHang), BorderLayout.CENTER);

        add(panelThan, BorderLayout.CENTER);
    }
}