package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class ThongKeGUI extends JPanel {
    private JComboBox<Integer> cboNam;
    private JLabel lblTongDoanhThuNam, lblTongThuChiNam;
    private JTable tblSanPham, tblDoanhThuThang, tblNhanVien, tblPhuongThuc, tblKhachHang;
    private JTable tblKhuyenMai, tblBaoHanh, tblThuChi, tblCanhBaoTonKho;

    public ThongKeGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel panelDau = new JPanel(new BorderLayout());
        panelDau.setOpaque(false);

        JLabel lblTieuDe = new JLabel("Thống kê số liệu");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        panelDau.add(lblTieuDe, BorderLayout.WEST);

        JPanel panelNam = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelNam.setOpaque(false);
        cboNam = new JComboBox<>();
        int namHienTai = LocalDate.now().getYear();
        for (int i = namHienTai; i >= namHienTai - 2; i--) {
            cboNam.addItem(i);
        }
        panelNam.add(new JLabel("Năm (doanh thu tháng, thu - chi):"));
        panelNam.add(cboNam);
        panelDau.add(panelNam, BorderLayout.EAST);

        add(panelDau, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 12));

        tblSanPham = taoBang("Mã SP", "Tên sản phẩm", "Đã bán", "Tồn kho", "Doanh thu");
        tabs.addTab("Sản phẩm (đã bán / tồn)", taoKhungBangVaThongKe(tblSanPham, "Số lượng đã bán theo sản phẩm"));

        tblDoanhThuThang = taoBang("Tháng", "Số hóa đơn", "Doanh thu");
        lblTongDoanhThuNam = new JLabel("Tổng doanh thu năm " + namHienTai + ": 0đ");
        lblTongDoanhThuNam.setFont(new Font("Arial", Font.BOLD, 13));
        lblTongDoanhThuNam.setForeground(new Color(14, 98, 195));
        JPanel tabDoanhThu = new JPanel(new BorderLayout(0, 8));
        tabDoanhThu.setOpaque(false);
        tabDoanhThu.add(lblTongDoanhThuNam, BorderLayout.NORTH);
        tabDoanhThu.add(taoKhungBangVaThongKe(tblDoanhThuThang, "Doanh thu theo tháng (triệu đồng)"), BorderLayout.CENTER);
        tabs.addTab("Doanh thu theo tháng", tabDoanhThu);

        tblNhanVien = taoBang("Mã NV", "Nhân viên", "Số hóa đơn", "Doanh thu", "Tỷ trọng");
        tabs.addTab("Theo nhân viên", taoKhungBangVaThongKe(tblNhanVien, "Doanh thu theo nhân viên (triệu đồng)"));

        tblPhuongThuc = taoBang("Phương thức", "Số hóa đơn", "Doanh thu", "Tỷ trọng");
        tabs.addTab("Phương thức thanh toán", taoKhungBangVaThongKe(tblPhuongThuc, "Cơ cấu doanh thu theo phương thức"));

        tblKhachHang = taoBang("Mã KH", "Khách hàng", "Số đơn", "Tổng chi tiêu", "Điểm tích lũy");
        tabs.addTab("Khách hàng mua nhiều", taoKhungBangVaThongKe(tblKhachHang, "Tổng chi tiêu - top khách hàng (triệu đồng)"));

        tblKhuyenMai = taoBang("Mã KM", "Chương trình", "Số hóa đơn", "Tổng tiền giảm", "Doanh thu sau giảm");
        tabs.addTab("Khuyến mãi", taoKhungBangVaThongKe(tblKhuyenMai, "Tổng tiền giảm theo chương trình (triệu đồng)"));

        tblBaoHanh = taoBang("Mã SP", "Sản phẩm", "Số lần bảo hành");
        tabs.addTab("Bảo hành", taoKhungBangVaThongKe(tblBaoHanh, "Phiếu bảo hành theo trạng thái"));

        tblThuChi = taoBang("Tháng", "Doanh thu", "Chi nhập kho", "Chênh lệch");
        lblTongThuChiNam = new JLabel("Năm " + namHienTai + ": thu 0đ  |  chi nhập kho 0đ  |  chênh lệch 0đ");
        lblTongThuChiNam.setFont(new Font("Arial", Font.BOLD, 13));
        lblTongThuChiNam.setForeground(new Color(14, 98, 195));
        JPanel tabThuChi = new JPanel(new BorderLayout(0, 8));
        tabThuChi.setOpaque(false);
        tabThuChi.add(lblTongThuChiNam, BorderLayout.NORTH);
        tabThuChi.add(taoKhungBangVaThongKe(tblThuChi, "Doanh thu bán hàng & Chi phí nhập kho"), BorderLayout.CENTER);
        tabs.addTab("Thu - chi theo tháng", tabThuChi);

        tblCanhBaoTonKho = taoBang("Mã SP", "Tên sản phẩm", "Tồn kho", "Bán 30 ngày qua", "Cảnh báo");
        tabs.addTab("Cảnh báo tồn kho", taoKhungBangVaThongKe(tblCanhBaoTonKho, "Tồn kho của sản phẩm cần chú ý"));

        add(tabs, BorderLayout.CENTER);
    }

    private JTable taoBang(String... tenCot) {
        JTable tbl = new JTable(new DefaultTableModel(tenCot, 0));
        tbl.setRowHeight(28);
        return tbl;
    }

    private JPanel taoKhungBangVaThongKe(JTable bang, String tieuDeKhungPhai) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 14, 0));
        panel.setOpaque(false);

        JPanel khungTrai = new JPanel(new BorderLayout());
        khungTrai.setBackground(Color.WHITE);
        khungTrai.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));
        khungTrai.add(new JScrollPane(bang), BorderLayout.CENTER);

        JPanel khungPhai = new JPanel(new BorderLayout());
        khungPhai.setBackground(Color.WHITE);
        khungPhai.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));
        JLabel lblPhai = new JLabel("   " + tieuDeKhungPhai);
        lblPhai.setFont(new Font("Arial", Font.BOLD, 13));
        lblPhai.setBorder(new EmptyBorder(10, 0, 10, 0));
        khungPhai.add(lblPhai, BorderLayout.NORTH);
        khungPhai.add(new JLabel("<html><center><i>[Khu vực hiển thị biểu đồ]</i></center></html>", JLabel.CENTER), BorderLayout.CENTER);

        panel.add(khungTrai);
        panel.add(khungPhai);
        return panel;
    }
}