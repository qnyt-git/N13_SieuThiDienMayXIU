package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LichSuMuaHangGUI extends JPanel {
    private JTextField txtMaHoacSdt;
    private JButton btnTim;
    private JLabel lblThongTinKhach, lblTongKet;
    private JTable tblLichSuHoaDon, tblChiTietHoaDon;

    public LichSuMuaHangGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel panelTren = new JPanel(new BorderLayout(0, 12));
        panelTren.setOpaque(false);

        JLabel lblTieuDe = new JLabel("Lịch sử mua hàng");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        panelTren.add(lblTieuDe, BorderLayout.NORTH);

        JPanel panelKhungTim = new JPanel(new BorderLayout(0, 8));
        panelKhungTim.setBackground(Color.WHITE);
        panelKhungTim.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230)),
            new EmptyBorder(12, 14, 12, 14)
        ));

        JPanel panelHangTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelHangTim.setBackground(Color.WHITE);
        txtMaHoacSdt = new JTextField(20);
        btnTim = new JButton("Tìm");
        btnTim.setBackground(new Color(14, 98, 195)); btnTim.setForeground(Color.WHITE);

        panelHangTim.add(new JLabel("Mã KH / SĐT:"));
        panelHangTim.add(txtMaHoacSdt);
        panelHangTim.add(btnTim);

        JPanel panelThongTin = new JPanel(new GridLayout(2, 1, 0, 4));
        panelThongTin.setBackground(Color.WHITE);
        lblThongTinKhach = new JLabel("Nhập mã hoặc số điện thoại khách hàng rồi bấm Tìm.");
        lblThongTinKhach.setFont(new Font("Arial", Font.BOLD, 13));
        lblTongKet = new JLabel(" ");

        panelThongTin.add(lblThongTinKhach);
        panelThongTin.add(lblTongKet);

        panelKhungTim.add(panelHangTim, BorderLayout.NORTH);
        panelKhungTim.add(panelThongTin, BorderLayout.CENTER);
        panelTren.add(panelKhungTim, BorderLayout.CENTER);

        tblLichSuHoaDon = new JTable(new DefaultTableModel(
            new Object[]{"Mã HĐ", "Ngày lập", "Nhân viên", "Phương thức", "Thành tiền", "Trạng thái"}, 0));
        tblLichSuHoaDon.setRowHeight(28);

        tblChiTietHoaDon = new JTable(new DefaultTableModel(
            new Object[]{"Mã SP", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"}, 0));
        tblChiTietHoaDon.setRowHeight(28);

        JPanel panelBang = new JPanel(new GridLayout(2, 1, 0, 10));
        panelBang.setOpaque(false);
        panelBang.add(new JScrollPane(tblLichSuHoaDon));
        panelBang.add(new JScrollPane(tblChiTietHoaDon));

        add(panelTren, BorderLayout.NORTH);
        add(panelBang, BorderLayout.CENTER);
    }
}