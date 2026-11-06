package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HoaDonGUI extends JPanel {
    private JTextField txtTimHoaDon;
    private JComboBox<String> cboTrangThaiHd;
    private JTable tblHoaDon, tblChiTietHoaDon;
    private JButton btnXemInHd;
    private JLabel lblThongKeHomNay;

    public HoaDonGUI() {
        setLayout(new BorderLayout(0, 10));
        setBackground(new Color(235, 240, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Thanh tìm kiếm và nút chức năng
        JPanel panelTren = new JPanel(new BorderLayout(0, 6));
        panelTren.setOpaque(false);

        JPanel panelTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelTim.setOpaque(false);
        txtTimHoaDon = new JTextField(20);
        cboTrangThaiHd = new JComboBox<>(new String[]{"Tất cả", "CHO_THANH_TOAN", "DA_THANH_TOAN"});
        panelTim.add(new JLabel("Tìm kiếm:"));
        panelTim.add(txtTimHoaDon);
        panelTim.add(new JLabel("Trạng thái:"));
        panelTim.add(cboTrangThaiHd);

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelNut.setOpaque(false);
        btnXemInHd = new JButton("Xem / In hóa đơn");
        btnXemInHd.setBackground(Color.WHITE);
        btnXemInHd.setForeground(new Color(14, 98, 195));
        btnXemInHd.setFont(new Font("Arial", Font.BOLD, 13));
        btnXemInHd.setFocusPainted(false);

        panelNut.add(btnXemInHd);

        JPanel panelThanhCongCu = new JPanel(new BorderLayout());
        panelThanhCongCu.setOpaque(false);
        panelThanhCongCu.add(panelTim, BorderLayout.WEST);
        panelThanhCongCu.add(panelNut, BorderLayout.EAST);

        lblThongKeHomNay = new JLabel("Hôm nay: 0 hóa đơn đã thanh toán, doanh thu 0 đ");
        lblThongKeHomNay.setFont(new Font("Arial", Font.BOLD, 13));
        lblThongKeHomNay.setForeground(new Color(14, 98, 195));

        panelTren.add(panelThanhCongCu, BorderLayout.NORTH);
        panelTren.add(lblThongKeHomNay, BorderLayout.SOUTH);

        // 2 Bảng: Danh sách hóa đơn (trên) & Chi tiết hóa đơn (dưới)
        DefaultTableModel modelHoaDon = new DefaultTableModel(new Object[]{
            "Mã HĐ", "Ngày lập", "Khách hàng", "Nhân viên", "Khuyến mãi",
            "Phương thức", "Tổng tiền", "Giảm giá", "Thành tiền", "Trạng thái", "Ghi chú", "Phiếu đặt hàng"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblHoaDon = new JTable(modelHoaDon);
        tblHoaDon.setRowHeight(28);

        DefaultTableModel modelChiTiet = new DefaultTableModel(
            new Object[]{"Mã SP", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblChiTietHoaDon = new JTable(modelChiTiet);
        tblChiTietHoaDon.setRowHeight(28);

        JPanel panelBang = new JPanel(new GridLayout(2, 1, 0, 10));
        panelBang.setOpaque(false);
        panelBang.add(new JScrollPane(tblHoaDon));
        panelBang.add(new JScrollPane(tblChiTietHoaDon));

        add(panelTren, BorderLayout.NORTH);
        add(panelBang, BorderLayout.CENTER);
    }
}