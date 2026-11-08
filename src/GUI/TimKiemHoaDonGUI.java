package GUI;

import BUS.HoaDonBUS;
import ENTITY.ChiTietHoaDon;
import ENTITY.HoaDon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/** Giao diện tra cứu hóa đơn theo mã, khách hàng và ngày lập. */
public class TimKiemHoaDonGUI extends JPanel {
    private final HoaDonBUS hoaDonBUS = new HoaDonBUS();
    private final List<HoaDon> danhSachHoaDon = new ArrayList<>();

    private JTextField txtMaHoaDon;
    private JTextField txtKhachHang;
    private JTextField txtTuNgay;
    private JTextField txtDenNgay;
    private JTable tblHoaDon;
    private DefaultTableModel model;
    private final DateTimeFormatter dinhDangNgay = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TimKiemHoaDonGUI() {
        setLayout(new BorderLayout(10, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel tieuDe = new JLabel("Tra cứu hóa đơn");
        tieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        tieuDe.setForeground(new Color(20, 40, 70));
        add(tieuDe, BorderLayout.NORTH);

        JPanel pnlChinh = new JPanel(new BorderLayout(8, 10));
        pnlChinh.setOpaque(false);

        JPanel pnlTimKiem = new JPanel(new GridBagLayout());
        pnlTimKiem.setBackground(Color.WHITE);
        pnlTimKiem.setBorder(BorderFactory.createTitledBorder("Thông tin tìm kiếm"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(7, 8, 7, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtMaHoaDon = new JTextField(15);
        txtKhachHang = new JTextField(15);
        txtTuNgay = new JTextField(10);
        txtDenNgay = new JTextField(10);
        txtTuNgay.setToolTipText("Nhập ngày theo dạng yyyy-MM-dd");
        txtDenNgay.setToolTipText("Nhập ngày theo dạng yyyy-MM-dd");

        themTruong(pnlTimKiem, g, 0, 0, "Mã hóa đơn:", txtMaHoaDon);
        themTruong(pnlTimKiem, g, 0, 2, "Khách hàng:", txtKhachHang);
        themTruong(pnlTimKiem, g, 1, 0, "Từ ngày:", txtTuNgay);
        themTruong(pnlTimKiem, g, 1, 2, "Đến ngày:", txtDenNgay);

        JButton btnTim = taoNut("Tìm kiếm");
        JButton btnLamMoi = new JButton("Làm mới");
        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlNut.setOpaque(false);
        pnlNut.add(btnLamMoi);
        pnlNut.add(btnTim);
        g.gridx = 4; g.gridy = 1; g.gridwidth = 2; g.weightx = 0;
        pnlTimKiem.add(pnlNut, g);

        model = new DefaultTableModel(new Object[]{
                "Mã hóa đơn", "Khách hàng", "Nhân viên", "Ngày lập",
                "Phương thức thanh toán", "Trạng thái"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblHoaDon = new JTable(model);
        tblHoaDon.setRowHeight(27);
        tblHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnChiTiet = taoNut("Xem chi tiết hóa đơn");
        JPanel pnlDuoi = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlDuoi.setOpaque(false);
        pnlDuoi.add(btnChiTiet);

        pnlChinh.add(pnlTimKiem, BorderLayout.NORTH);
        pnlChinh.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);
        pnlChinh.add(pnlDuoi, BorderLayout.SOUTH);
        add(pnlChinh, BorderLayout.CENTER);

        btnTim.addActionListener(e -> timKiem());
        btnLamMoi.addActionListener(e -> {
            txtMaHoaDon.setText("");
            txtKhachHang.setText("");
            txtTuNgay.setText("");
            txtDenNgay.setText("");
            taiDuLieu(hoaDonBUS.getAll());
        });
        btnChiTiet.addActionListener(e -> xemChiTiet());
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) xemChiTiet();
            }
        });

        taiDuLieu(hoaDonBUS.getAll());
    }

    private void themTruong(JPanel p, GridBagConstraints g, int row, int col,
                            String ten, JTextField txt) {
        g.gridy = row; g.gridx = col; g.weightx = 0; g.gridwidth = 1;
        p.add(new JLabel(ten), g);
        g.gridx = col + 1; g.weightx = 1;
        p.add(txt, g);
    }

    private JButton taoNut(String ten) {
        JButton b = new JButton(ten);
        b.setBackground(new Color(14, 98, 195));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        return b;
    }

    private void taiDuLieu(List<HoaDon> ds) {
        danhSachHoaDon.clear();
        danhSachHoaDon.addAll(ds);
        model.setRowCount(0);
        for (HoaDon hd : danhSachHoaDon) {
            model.addRow(new Object[]{
                    hd.getMaHoaDon(),
                    hd.getMaKhachHang() == null ? "" : hd.getMaKhachHang().getHoTenKhachHang(),
                    hd.getMaNhanVien() == null ? "" : hd.getMaNhanVien().getHoTenNhanVien(),
                    hd.getNgayLap() == null ? "" : hd.getNgayLap().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    hd.getPhuongThucThanhToan() == null ? "" : hd.getPhuongThucThanhToan().getMoTa(),
                    hd.getTrangThai() == null ? "" : hd.getTrangThai().getMoTa()
            });
        }
    }

    private void timKiem() {
        String ma = txtMaHoaDon.getText().trim().toLowerCase();
        String tenKH = txtKhachHang.getText().trim().toLowerCase();
        LocalDate tuNgay = null;
        LocalDate denNgay = null;

        try {
            if (!txtTuNgay.getText().trim().isEmpty()) tuNgay = LocalDate.parse(txtTuNgay.getText().trim(), dinhDangNgay);
            if (!txtDenNgay.getText().trim().isEmpty()) denNgay = LocalDate.parse(txtDenNgay.getText().trim(), dinhDangNgay);
            if (tuNgay != null && denNgay != null && denNgay.isBefore(tuNgay)) {
                JOptionPane.showMessageDialog(this, "Đến ngày không được trước Từ ngày!");
                return;
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Ngày phải đúng định dạng yyyy-MM-dd (ví dụ 2026-11-08).");
            return;
        }

        List<HoaDon> ketQua = new ArrayList<>();
        for (HoaDon hd : hoaDonBUS.getAll()) {
            String maHD = hd.getMaHoaDon() == null ? "" : hd.getMaHoaDon().toLowerCase();
            String ten = hd.getMaKhachHang() == null || hd.getMaKhachHang().getHoTenKhachHang() == null
                    ? "" : hd.getMaKhachHang().getHoTenKhachHang().toLowerCase();
            LocalDate ngayLap = hd.getNgayLap() == null ? null : hd.getNgayLap().toLocalDate();

            boolean dungMa = ma.isEmpty() || maHD.contains(ma);
            boolean dungTen = tenKH.isEmpty() || ten.contains(tenKH);
            boolean dungTuNgay = tuNgay == null || (ngayLap != null && !ngayLap.isBefore(tuNgay));
            boolean dungDenNgay = denNgay == null || (ngayLap != null && !ngayLap.isAfter(denNgay));

            if (dungMa && dungTen && dungTuNgay && dungDenNgay) ketQua.add(hd);
        }
        taiDuLieu(ketQua);
        if (ketQua.isEmpty()) JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn phù hợp.");
    }

    private void xemChiTiet() {
        int dong = tblHoaDon.getSelectedRow();
        if (dong < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn!");
            return;
        }

        HoaDon hd = danhSachHoaDon.get(tblHoaDon.convertRowIndexToModel(dong));
        List<ChiTietHoaDon> chiTiet = hoaDonBUS.getChiTietByHoaDon(hd);

        String[] cot = {"Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Đơn giá", "VAT", "Thành tiền"};
        DefaultTableModel modelCT = new DefaultTableModel(cot, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        DecimalFormat tien = new DecimalFormat("#,##0.##");
        double tong = 0;
        for (ChiTietHoaDon ct : chiTiet) {
            double thanhTien = ct.getSoLuong() * ct.getDonGia() * (1 + ct.getThueVAT());
            tong += thanhTien;
            modelCT.addRow(new Object[]{
                    ct.getMaSanPham() == null ? "" : ct.getMaSanPham().getMaSanPham(),
                    ct.getMaSanPham() == null ? "" : ct.getMaSanPham().getTenSanPham(),
                    ct.getSoLuong(), tien.format(ct.getDonGia()),
                    (ct.getThueVAT() * 100) + "%", tien.format(thanhTien)
            });
        }

        JTable tblCT = new JTable(modelCT);
        tblCT.setRowHeight(25);
        JPanel noiDung = new JPanel(new BorderLayout(8, 8));
        noiDung.add(new JLabel("Hóa đơn: " + hd.getMaHoaDon()
                + "     |     Khách hàng: " + (hd.getMaKhachHang() == null ? "" : hd.getMaKhachHang().getHoTenKhachHang())), BorderLayout.NORTH);
        noiDung.add(new JScrollPane(tblCT), BorderLayout.CENTER);
        JLabel lblTong = new JLabel("Tổng tiền (đã gồm VAT): " + tien.format(tong) + " VNĐ");
        lblTong.setFont(new Font("Arial", Font.BOLD, 14));
        noiDung.add(lblTong, BorderLayout.SOUTH);

        if (chiTiet.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hóa đơn này chưa có chi tiết hoặc không đọc được chi tiết.");
            return;
        }
        JOptionPane.showMessageDialog(this, noiDung, "Chi tiết hóa đơn", JOptionPane.PLAIN_MESSAGE);
    }
}
