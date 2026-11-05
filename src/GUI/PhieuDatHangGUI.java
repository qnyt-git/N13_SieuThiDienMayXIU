package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PhieuDatHangGUI extends JPanel {
    private JTable tblPhieuDat, tblChiTietPhieuDat;
    private JButton btnXacNhanPhieu, btnHuyPhieu, btnThanhToanPhieu;

    public PhieuDatHangGUI() {
        setLayout(new BorderLayout(0, 10));
        setBackground(new Color(235, 240, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Thanh nút chức năng phía trên
        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelNut.setOpaque(false);

        btnHuyPhieu = new JButton("Hủy phiếu");
        btnHuyPhieu.setBackground(new Color(220, 38, 38));
        btnHuyPhieu.setForeground(Color.WHITE);
        btnHuyPhieu.setFont(new Font("Arial", Font.BOLD, 13));
        btnHuyPhieu.setFocusPainted(false);

        btnXacNhanPhieu = new JButton("Xác nhận đặt hàng");
        btnXacNhanPhieu.setBackground(new Color(22, 163, 74));
        btnXacNhanPhieu.setForeground(Color.WHITE);
        btnXacNhanPhieu.setFont(new Font("Arial", Font.BOLD, 13));
        btnXacNhanPhieu.setFocusPainted(false);

        btnThanhToanPhieu = new JButton("Thanh toán phiếu này");
        btnThanhToanPhieu.setBackground(new Color(14, 98, 195));
        btnThanhToanPhieu.setForeground(Color.WHITE);
        btnThanhToanPhieu.setFont(new Font("Arial", Font.BOLD, 13));
        btnThanhToanPhieu.setFocusPainted(false);

        panelNut.add(btnHuyPhieu);
        panelNut.add(btnXacNhanPhieu);
        panelNut.add(btnThanhToanPhieu);

        // 2 Bảng: Danh sách phiếu đặt (trên) & Chi tiết phiếu đặt (dưới)
        tblPhieuDat = new JTable(new DefaultTableModel(
            new Object[]{"Mã phiếu", "Ngày đặt", "Khách hàng", "Nhân viên", "Tổng tiền", "Trạng thái", "Ghi chú"}, 0));
        tblPhieuDat.setRowHeight(28);

        tblChiTietPhieuDat = new JTable(new DefaultTableModel(
            new Object[]{"Mã SP", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"}, 0));
        tblChiTietPhieuDat.setRowHeight(28);

        JPanel panelBang = new JPanel(new GridLayout(2, 1, 0, 10));
        panelBang.setOpaque(false);
        panelBang.add(new JScrollPane(tblPhieuDat));
        panelBang.add(new JScrollPane(tblChiTietPhieuDat));

        add(panelNut, BorderLayout.NORTH);
        add(panelBang, BorderLayout.CENTER);
    }

    public JButton getBtnThanhToanPhieu() {
        return btnThanhToanPhieu;
    }
}