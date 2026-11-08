package GUI;

import BUS.BanHangBUS;
import DAO.HoaDonDAO;
import ENTITY.ChiTietHoaDon;
import ENTITY.HoaDon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HoaDonGUI extends JPanel {
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private DefaultTableModel modelHoaDon;
    private JTable tblHoaDon;
    private JTextField txtTimKiem;
    private JLabel lblCount;
    private List<HoaDon> danhSachHoaDon = new ArrayList<>();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public HoaDonGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        // Phần tiêu đề và bộ lọc tìm kiếm phía trên
        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        JLabel lblTieuDe = new JLabel("Quản lý hóa đơn bán hàng");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        top.add(lblTieuDe, BorderLayout.NORTH);

        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        txtTimKiem = new JTextField(24);
        txtTimKiem.setPreferredSize(new Dimension(240, 32));
        left.add(new JLabel("Tìm kiếm (Mã HĐ / Khách hàng):"));
        left.add(txtTimKiem);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        JButton btnChiTiet = taoNut("Xem chi tiết hóa đơn", new Color(14, 98, 195), Color.WHITE);
        buttons.add(btnChiTiet);

        bar.add(left, BorderLayout.WEST);
        bar.add(buttons, BorderLayout.EAST);
        top.add(bar, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        // Bảng hiển thị danh sách hóa đơn
        modelHoaDon = new DefaultTableModel(new Object[]{
                "Mã hóa đơn", "Khách hàng", "Nhân viên lập", "Ngày lập", "Phương thức TT", "Trạng thái"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblHoaDon = new JTable(modelHoaDon);
        tblHoaDon.setRowHeight(28);
        tblHoaDon.setFont(new Font("Arial", Font.PLAIN, 13));
        tblHoaDon.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);

        // Nhãn thống kê số lượng bản ghi phía dưới
        lblCount = new JLabel("Đang tải dữ liệu...");
        lblCount.setForeground(new Color(100, 116, 139));
        add(lblCount, BorderLayout.SOUTH);

        // Đăng ký sự kiện
        btnChiTiet.addActionListener(e -> xemChiTietHoaDon());
        
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { locDuLieu(); }
            public void removeUpdate(DocumentEvent e) { locDuLieu(); }
            public void changedUpdate(DocumentEvent e) { locDuLieu(); }
        });

        taiDuLieu();
    }

    public void taiDuLieu() {
        try {
            danhSachHoaDon = hoaDonDAO.getAll();
            hienThiDanhSach(danhSachHoaDon);
            if (danhSachHoaDon.isEmpty()) {
                lblCount.setText("Hiển thị 0 bản ghi – kiểm tra dữ liệu bảng HoaDon trong SQL Server");
            }
        } catch (Exception e) {
            lblCount.setText("Không tải được dữ liệu hóa đơn");
        }
    }

    private void hienThiDanhSach(List<HoaDon> ds) {
        modelHoaDon.setRowCount(0);
        for (HoaDon hd : ds) {
            String tenKH = (hd.getMaKhachHang() != null) ? hd.getMaKhachHang().getHoTenKhachHang() : "";
            String tenNV = (hd.getMaNhanVien() != null) ? hd.getMaNhanVien().getHoTenNhanVien() : "";
            modelHoaDon.addRow(new Object[]{
                    hd.getMaHoaDon(),
                    tenKH,
                    tenNV,
                    hd.getNgayLap() != null ? hd.getNgayLap().format(FORMATTER) : "",
                    hd.getPhuongThucThanhToan() != null ? hd.getPhuongThucThanhToan().getMoTa() : "",
                    hd.getTrangThai() != null ? hd.getTrangThai().getMoTa() : ""
            });
        }
        lblCount.setText("Hiển thị " + ds.size() + " bản ghi");
    }

    private void locDuLieu() {
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
        List<HoaDon> ketQua = new ArrayList<>();
        for (HoaDon hd : danhSachHoaDon) {
            String tenKH = (hd.getMaKhachHang() != null) ? hd.getMaKhachHang().getHoTenKhachHang() : "";
            String thongTin = (hd.getMaHoaDon() + " " + tenKH).toLowerCase();
            if (thongTin.contains(tuKhoa)) {
                ketQua.add(hd);
            }
        }
        hienThiDanhSach(ketQua);
    }

    private HoaDon hoaDonDangChon() {
        int row = tblHoaDon.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn từ bảng.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        String maHD = String.valueOf(modelHoaDon.getValueAt(tblHoaDon.convertRowIndexToModel(row), 0));
        for (HoaDon hd : danhSachHoaDon) {
            if (hd.getMaHoaDon().equals(maHD)) {
                return hd;
            }
        }
        return null;
    }

    private void xemChiTietHoaDon() {
        HoaDon hd = hoaDonDangChon();
        if (hd == null) return;

        // Lấy danh sách sản phẩm chi tiết của hóa đơn thông qua DAO
        List<ChiTietHoaDon> chiTiets = hoaDonDAO.getChiTietByHoaDon(hd);
        
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Chi tiết hóa đơn: " + hd.getMaHoaDon(), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(760, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel pnlInfo = new JPanel(new GridLayout(3, 2, 8, 4));
        pnlInfo.setBorder(new EmptyBorder(12, 12, 0, 12));
        pnlInfo.add(new JLabel("Mã hóa đơn: " + hd.getMaHoaDon()));
        pnlInfo.add(new JLabel("Ngày lập: " + (hd.getNgayLap() != null ? hd.getNgayLap().format(FORMATTER) : "")));
        pnlInfo.add(new JLabel("Khách hàng: " + (hd.getMaKhachHang() != null ? hd.getMaKhachHang().getHoTenKhachHang() : "")));
        pnlInfo.add(new JLabel("Nhân viên lập: " + (hd.getMaNhanVien() != null ? hd.getMaNhanVien().getHoTenNhanVien() : "")));
        pnlInfo.add(new JLabel("Phương thức TT: " + (hd.getPhuongThucThanhToan() != null ? hd.getPhuongThucThanhToan().getMoTa() : "")));
        pnlInfo.add(new JLabel("Trạng thái: " + (hd.getTrangThai() != null ? hd.getTrangThai().getMoTa() : "")));

        DefaultTableModel modelCT = new DefaultTableModel(
                new Object[]{"Mã SP", "Tên sản phẩm", "Nhà cung cấp", "Nơi sản xuất", "Số lượng", "Đơn giá", "VAT", "Thành tiền"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        double tongTien = 0;
        for (ChiTietHoaDon ct : chiTiets) {
            double thanhTien = BanHangBUS.tinhThanhTien(ct);
            tongTien += thanhTien;
            String tenNCC = (ct.getMaSanPham() != null && ct.getMaSanPham().getMaNCC() != null)
                    ? ct.getMaSanPham().getMaNCC().getTenThuongHieu()
                    : "";
            String noiSX = (ct.getMaSanPham() != null && ct.getMaSanPham().getNoiSanXuat() != null)
                    ? ct.getMaSanPham().getNoiSanXuat()
                    : "";
            modelCT.addRow(new Object[]{
                    ct.getMaSanPham() != null ? ct.getMaSanPham().getMaSanPham() : "",
                    ct.getMaSanPham() != null ? ct.getMaSanPham().getTenSanPham() : "",
                    tenNCC,
                    noiSX,
                    ct.getSoLuong(),
                    String.format("%,.0f", ct.getDonGia()),
                    Math.round(ct.getThueVAT() * 100) + "%",
                    String.format("%,.0f VNĐ", thanhTien)
            });
        }

        double tienGiam = BanHangBUS.tinhTienGiam(hd.getMaKhuyenMai(), tongTien);
        double tongThanhToan = Math.max(0, tongTien - tienGiam);

        JTable tblCT = new JTable(modelCT);
        tblCT.setRowHeight(24);

        JLabel lblTong = new JLabel(
                "Tiền hàng (gồm VAT): " + String.format("%,.0f VNĐ", tongTien)
                + "  |  Giảm giá: " + String.format("%,.0f VNĐ", tienGiam)
                + "  |  Tổng thanh toán: " + String.format("%,.0f VNĐ", tongThanhToan),
                JLabel.RIGHT
        );
        lblTong.setFont(new Font("Arial", Font.BOLD, 14));
        lblTong.setBorder(new EmptyBorder(8, 12, 12, 12));

        dialog.add(pnlInfo, BorderLayout.NORTH);
        dialog.add(new JScrollPane(tblCT), BorderLayout.CENTER);
        dialog.add(lblTong, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JButton taoNut(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setFocusPainted(false);
        return b;
    }
}