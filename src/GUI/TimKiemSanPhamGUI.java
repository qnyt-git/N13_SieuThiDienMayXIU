package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TimKiemSanPhamGUI extends JPanel {
    private JTextField txtTuKhoa, txtGiaTu, txtGiaDen;
    private JCheckBox chkConHang;
    private JButton btnTimKiem, btnLamMoi;
    private JTable tblKetQua;
    private JLabel lblSoLuongKetQua;

    public TimKiemSanPhamGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel panelTren = new JPanel(new BorderLayout(0, 12));
        panelTren.setOpaque(false);

        JLabel lblTieuDe = new JLabel("Tìm kiếm sản phẩm");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        panelTren.add(lblTieuDe, BorderLayout.NORTH);

        JPanel panelTieuChi = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panelTieuChi.setBackground(Color.WHITE);
        panelTieuChi.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));

        txtTuKhoa = new JTextField(18);
        txtGiaTu = new JTextField(9);
        txtGiaDen = new JTextField(9);
        chkConHang = new JCheckBox("Chỉ hiển thị còn hàng");
        chkConHang.setBackground(Color.WHITE);

        btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setBackground(new Color(14, 98, 195)); btnTimKiem.setForeground(Color.WHITE);
        btnLamMoi = new JButton("Làm mới");

        panelTieuChi.add(new JLabel("Mã / tên / mã vạch:")); panelTieuChi.add(txtTuKhoa);
        panelTieuChi.add(new JLabel("Giá từ:")); panelTieuChi.add(txtGiaTu);
        panelTieuChi.add(new JLabel("đến:")); panelTieuChi.add(txtGiaDen);
        panelTieuChi.add(chkConHang);
        panelTieuChi.add(btnTimKiem);
        panelTieuChi.add(btnLamMoi);

        panelTren.add(panelTieuChi, BorderLayout.CENTER);
        add(panelTren, BorderLayout.NORTH);

        tblKetQua = new JTable(new DefaultTableModel(
            new Object[]{"Mã SP", "Tên sản phẩm", "Mã vạch", "Giá bán", "Số lượng tồn", "Bảo hành (tháng)", "Trạng thái"}, 0));
        tblKetQua.setRowHeight(28);
        add(new JScrollPane(tblKetQua), BorderLayout.CENTER);

        lblSoLuongKetQua = new JLabel("Tìm thấy 0 sản phẩm");
        add(lblSoLuongKetQua, BorderLayout.SOUTH);
    }
}