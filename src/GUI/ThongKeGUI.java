package GUI;

import BUS.ThongKeBUS;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class ThongKeGUI extends JPanel {
    private final ThongKeBUS thongKeBUS = new ThongKeBUS();
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
        for (int i = namHienTai; i >= namHienTai - 5; i--) {
            cboNam.addItem(i);
        }
        panelNam.add(new JLabel("Năm (doanh thu tháng, thu - chi):"));
        panelNam.add(cboNam);
        panelDau.add(panelNam, BorderLayout.EAST);

        add(panelDau, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 12));

        tblSanPham = taoBang("Mã SP", "Tên sản phẩm", "Đã bán", "Tồn kho", "Doanh thu");
        tabs.addTab("Sản phẩm (đã bán / tồn)", taoKhungBangVaBieuDo(tblSanPham, "Số lượng đã bán theo sản phẩm", taoBieuDoCotSanPham()));

        tblDoanhThuThang = taoBang("Tháng", "Số hóa đơn", "Doanh thu");
        lblTongDoanhThuNam = new JLabel("Tổng doanh thu năm " + namHienTai + ": 0đ");
        lblTongDoanhThuNam.setFont(new Font("Arial", Font.BOLD, 13));
        lblTongDoanhThuNam.setForeground(new Color(14, 98, 195));
        JPanel tabDoanhThu = new JPanel(new BorderLayout(0, 8));
        tabDoanhThu.setOpaque(false);
        tabDoanhThu.add(lblTongDoanhThuNam, BorderLayout.NORTH);
        tabDoanhThu.add(taoKhungBangVaBieuDo(tblDoanhThuThang, "Doanh thu theo tháng (triệu đồng)", taoBieuDoDuongDoanhThu()), BorderLayout.CENTER);
        tabs.addTab("Doanh thu theo tháng", tabDoanhThu);

        tblNhanVien = taoBang("Mã NV", "Nhân viên", "Số hóa đơn", "Doanh thu", "Tỷ trọng");
        tabs.addTab("Theo nhân viên", taoKhungBangVaBieuDo(tblNhanVien, "Doanh thu theo nhân viên (triệu đồng)", taoBieuDoCotNhanVien()));

        tblPhuongThuc = taoBang("Phương thức", "Số hóa đơn", "Doanh thu", "Tỷ trọng");
        tabs.addTab("Phương thức thanh toán", taoKhungBangVaBieuDo(tblPhuongThuc, "Cơ cấu doanh thu theo phương thức", taoBieuDoTronThanhToan()));

        tblKhachHang = taoBang("Mã KH", "Khách hàng", "Số đơn", "Tổng chi tiêu", "Điểm tích lũy");
        tabs.addTab("Khách hàng mua nhiều", taoKhungBangVaBieuDo(tblKhachHang, "Tổng chi tiêu - top khách hàng (triệu đồng)", taoBieuDoCotKhachHang()));

        tblKhuyenMai = taoBang("Mã KM", "Chương trình", "Số hóa đơn", "Tổng tiền giảm", "Doanh thu sau giảm");
        tabs.addTab("Khuyến mãi", taoKhungBangVaBieuDo(tblKhuyenMai, "Tổng tiền giảm theo chương trình (triệu đồng)", taoBieuDoTrongSuot()));

        tblBaoHanh = taoBang("Mã SP", "Sản phẩm", "Số lần bảo hành");
        tabs.addTab("Bảo hành", taoKhungBangVaBieuDo(tblBaoHanh, "Phiếu bảo hành theo trạng thái", taoBieuDoTrongSuot()));

        tblThuChi = taoBang("Tháng", "Doanh thu", "Chi nhập kho", "Chênh lệch");
        lblTongThuChiNam = new JLabel("Năm " + namHienTai + ": thu 0đ  |  chi nhập kho 0đ  |  chênh lệch 0đ");
        lblTongThuChiNam.setFont(new Font("Arial", Font.BOLD, 13));
        lblTongThuChiNam.setForeground(new Color(14, 98, 195));
        JPanel tabThuChi = new JPanel(new BorderLayout(0, 8));
        tabThuChi.setOpaque(false);
        tabThuChi.add(lblTongThuChiNam, BorderLayout.NORTH);
        tabThuChi.add(taoKhungBangVaBieuDo(tblThuChi, "Doanh thu bán hàng & Chi phí nhập kho", taoBieuDoDuongDoanhThu()), BorderLayout.CENTER);
        tabs.addTab("Thu - chi theo tháng", tabThuChi);

        tblCanhBaoTonKho = taoBang("Mã SP", "Tên sản phẩm", "Tồn kho", "Bán 30 ngày qua", "Cảnh báo");
        tabs.addTab("Cảnh báo tồn kho", taoKhungBangVaBieuDo(tblCanhBaoTonKho, "Tồn kho của sản phẩm cần chú ý", taoBieuDoTrongSuot()));

        add(tabs, BorderLayout.CENTER);
        cboNam.addActionListener(e -> taiDuLieuThongKe());
        taiDuLieuThongKe();
    }

    private void taiDuLieuThongKe() {
        // Nap du lieu san pham tu co so du lieu
        DefaultTableModel modelSP = (DefaultTableModel) tblSanPham.getModel();
        modelSP.setRowCount(0);
        for (Object[] dong : thongKeBUS.thongKeSanPham()) {
            modelSP.addRow(dong);
        }

        // Nap doanh thu theo thang cua nam duoc chon
        int nam = (Integer) cboNam.getSelectedItem();
        DefaultTableModel modelDT = (DefaultTableModel) tblDoanhThuThang.getModel();
        modelDT.setRowCount(0);
        double tong = 0;
        for (Object[] dong : thongKeBUS.thongKeDoanhThuTheoThang(nam)) {
            modelDT.addRow(dong);
            tong += ((Number) dong[2]).doubleValue();
        }
        lblTongDoanhThuNam.setText("Tổng doanh thu năm " + nam + ": "
                + String.format("%,.0f", tong) + " đ");
    }

    private JTable taoBang(String... tenCot) {
        JTable tbl = new JTable(new DefaultTableModel(tenCot, 0));
        tbl.setRowHeight(28);
        return tbl;
    }

    private JPanel taoKhungBangVaBieuDo(JTable bang, String tieuDeKhungPhai, JPanel panelBieuDo) {
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
        khungPhai.add(panelBieuDo, BorderLayout.CENTER);

        panel.add(khungTrai);
        panel.add(khungPhai);
        return panel;
    }

    private JPanel taoBieuDoCotSanPham() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(45, "Đã bán", "Tivi Sony");
        dataset.addValue(30, "Đã bán", "Tủ lạnh LG");
        dataset.addValue(60, "Đã bán", "Máy giặt");
        dataset.addValue(20, "Đã bán", "Điều hòa");

        JFreeChart barChart = ChartFactory.createBarChart("", "", "Số lượng", dataset);
        return new ChartPanel(barChart);
    }

    private JPanel taoBieuDoDuongDoanhThu() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 1; i <= 6; i++) {
            dataset.addValue(i * 15.5, "Doanh thu", "Tháng " + i);
        }

        JFreeChart lineChart = ChartFactory.createLineChart("", "", "Triệu VNĐ", dataset);
        return new ChartPanel(lineChart);
    }

    private JPanel taoBieuDoTronThanhToan() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Tiền mặt", 45.0);
        dataset.setValue("Chuyển khoản QR", 35.0);
        dataset.setValue("Thẻ ngân hàng", 20.0);

        JFreeChart pieChart = ChartFactory.createPieChart("", dataset, true, true, false);
        return new ChartPanel(pieChart);
    }

    private JPanel taoBieuDoCotNhanVien() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(120, "Doanh thu", "Nguyễn Văn A");
        dataset.addValue(95, "Doanh thu", "Trần Thị B");
        dataset.addValue(150, "Doanh thu", "Lê Văn C");

        JFreeChart chart = ChartFactory.createBarChart("", "", "Triệu VNĐ", dataset);
        return new ChartPanel(chart);
    }

    private JPanel taoBieuDoCotKhachHang() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(45, "Chi tiêu", "KH. Trần Văn Quân");
        dataset.addValue(38, "Chi tiêu", "KH. Lê Thị Hoa");

        JFreeChart chart = ChartFactory.createBarChart("", "", "Triệu VNĐ", dataset);
        return new ChartPanel(chart);
    }

    private JPanel taoBieuDoTrongSuot() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("<html><center><i>[Chưa có dữ liệu biểu đồ]</i></center></html>", JLabel.CENTER);
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }
}
