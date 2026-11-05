package GUI;

import javax.swing.*;
import java.awt.*;

public class BanHangGUI extends JPanel {
    private JTabbedPane tabs;
    private GioHangGUI tabBanHang;
    private GioHangGUI tabLapPhieuDat;
    private PhieuDatHangGUI tabPhieuDatHang;
    private HoaDonGUI tabHoaDon;

    public BanHangGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(235, 240, 245));
        setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        // Tiêu đề màn hình
        JLabel lblTieuDe = new JLabel("Bán hàng & thanh toán");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDe.setForeground(new Color(12, 68, 136));
        add(lblTieuDe, BorderLayout.NORTH);

        // Khởi tạo các GUI con tương ứng với từng tab
        tabBanHang = new GioHangGUI(true);          // Tab 1: Bán hàng & thanh toán
        tabLapPhieuDat = new GioHangGUI(false);     // Tab 2: Lập phiếu đặt hàng
        tabPhieuDatHang = new PhieuDatHangGUI();    // Tab 3: Danh sách phiếu đặt hàng
        tabHoaDon = new HoaDonGUI();                // Tab 4: Danh sách hóa đơn

        // Gắn các GUI con vào JTabbedPane của BanHangGUI
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 14));
        tabs.addTab("Bán hàng & thanh toán", tabBanHang);
        tabs.addTab("Lập phiếu đặt hàng", tabLapPhieuDat);
        tabs.addTab("Phiếu đặt hàng", tabPhieuDatHang);
        tabs.addTab("Hóa đơn", tabHoaDon);

        // Khi bấm "Thanh toán phiếu này" ở Tab 3 -> tự động chuyển về Tab 0 (Bán hàng)
        tabPhieuDatHang.getBtnThanhToanPhieu().addActionListener(e -> {
            tabs.setSelectedIndex(0);
        });

        add(tabs, BorderLayout.CENTER);
    }

    // Các Getter để Controller gọi tới từng tab khi cần
    public GioHangGUI getTabBanHang() { return tabBanHang; }
    public GioHangGUI getTabLapPhieuDat() { return tabLapPhieuDat; }
    public PhieuDatHangGUI getTabPhieuDatHang() { return tabPhieuDatHang; }
    public HoaDonGUI getTabHoaDon() { return tabHoaDon; }
}