package GUI;

import BUS.PhieuDatHangBUS;
import ENTITY.ChiTietPhieuDatHang;
import ENTITY.PhieuDatHang;
import ENUM.TrangThaiPhieuDatHang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PhieuDatHangGUI extends JPanel {
    private final PhieuDatHangBUS pdhBUS = new PhieuDatHangBUS();
    private final DefaultTableModel modelPDH;
    private final JTable tblPDH;
    private final JTextField txtTimKiem;
    private final JLabel lblCount;
    private final JButton btnThanhToanPhieu;
    private List<PhieuDatHang> danhSachPDH = new ArrayList<>();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public PhieuDatHangGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        JLabel lblTieuDe = new JLabel("Quản lý phiếu đặt hàng");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        top.add(lblTieuDe, BorderLayout.NORTH);

        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        txtTimKiem = new JTextField(24);
        txtTimKiem.setPreferredSize(new Dimension(240, 32));
        left.add(new JLabel("Tìm kiếm:"));
        left.add(txtTimKiem);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        JButton btnChiTiet = taoNut("Xem chi tiết", Color.WHITE, new Color(14, 98, 195));
        btnThanhToanPhieu = taoNut("Thanh toán phiếu này", new Color(22, 163, 74), Color.WHITE);
        JButton btnXoa = taoNut("Hủy phiếu", new Color(220, 38, 38), Color.WHITE);
        buttons.add(btnChiTiet);
        buttons.add(btnThanhToanPhieu);
        buttons.add(btnXoa);
        bar.add(left, BorderLayout.WEST);
        bar.add(buttons, BorderLayout.EAST);
        top.add(bar, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        modelPDH = new DefaultTableModel(new Object[]{
                "Mã phiếu đặt", "Khách hàng", "Nhân viên lập", "Ngày đặt", "Ghi chú", "Trạng thái"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblPDH = new JTable(modelPDH);
        tblPDH.setRowHeight(28);
        tblPDH.setFont(new Font("Arial", Font.PLAIN, 13));
        tblPDH.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblPDH.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tblPDH), BorderLayout.CENTER);

        lblCount = new JLabel("Đang tải dữ liệu...");
        lblCount.setForeground(new Color(100, 116, 139));
        add(lblCount, BorderLayout.SOUTH);

        btnChiTiet.addActionListener(e -> xemChiTiet());
        btnXoa.addActionListener(e -> xoaPDH());
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { locDuLieu(); }
            public void removeUpdate(DocumentEvent e) { locDuLieu(); }
            public void changedUpdate(DocumentEvent e) { locDuLieu(); }
        });

        taiDuLieu();
    }

    public JButton getBtnThanhToanPhieu() {
        return btnThanhToanPhieu;
    }

    public void taiDuLieu() {
        try {
            danhSachPDH = pdhBUS.getAll();
            hienThiDanhSach(danhSachPDH);
            if (danhSachPDH.isEmpty()) {
                lblCount.setText("Hiển thị 0 bản ghi – kiểm tra dữ liệu bảng PhieuDatHang trong SQL Server");
            }
        } catch (Exception e) {
            lblCount.setText("Không tải được dữ liệu phiếu đặt hàng");
        }
    }

    private void hienThiDanhSach(List<PhieuDatHang> ds) {
        modelPDH.setRowCount(0);
        for (PhieuDatHang p : ds) {
            String tenKH = (p.getMaKhachHang() != null) ? p.getMaKhachHang().getHoTenKhachHang() : "";
            String tenNV = (p.getMaNhanVien() != null) ? p.getMaNhanVien().getHoTenNhanVien() : "";
            modelPDH.addRow(new Object[]{
                    p.getMaPhieuDatHang(),
                    tenKH,
                    tenNV,
                    p.getNgayDatHang() != null ? p.getNgayDatHang().format(FORMATTER) : "",
                    p.getGhiChu(),
                    pdhBUS.trangThaiText(p)
            });
        }
        lblCount.setText("Hiển thị " + ds.size() + " bản ghi");
    }

    private void locDuLieu() {
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
        List<PhieuDatHang> ketQua = new ArrayList<>();
        for (PhieuDatHang p : danhSachPDH) {
            String tenKH = (p.getMaKhachHang() != null) ? p.getMaKhachHang().getHoTenKhachHang() : "";
            String thongTin = (p.getMaPhieuDatHang() + " " + tenKH).toLowerCase();
            if (thongTin.contains(tuKhoa)) ketQua.add(p);
        }
        hienThiDanhSach(ketQua);
    }

    public PhieuDatHang pdhDangChon() {
        int row = tblPDH.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu đặt hàng.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        String ma = String.valueOf(modelPDH.getValueAt(tblPDH.convertRowIndexToModel(row), 0));
        for (PhieuDatHang p : danhSachPDH) if (p.getMaPhieuDatHang().equals(ma)) return p;
        return null;
    }

    private void xemChiTiet() {
        PhieuDatHang p = pdhDangChon();
        if (p == null) return;

        List<ChiTietPhieuDatHang> chiTiets = pdhBUS.getChiTietByPhieu(p);

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                "Chi tiết phiếu đặt hàng: " + p.getMaPhieuDatHang(), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(620, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel pnlInfo = new JPanel(new GridLayout(3, 2, 8, 4));
        pnlInfo.setBorder(new EmptyBorder(12, 12, 0, 12));
        pnlInfo.add(new JLabel("Mã phiếu đặt: " + p.getMaPhieuDatHang()));
        pnlInfo.add(new JLabel("Ngày đặt: " + (p.getNgayDatHang() != null ? p.getNgayDatHang().format(FORMATTER) : "")));
        pnlInfo.add(new JLabel("Khách hàng: " + (p.getMaKhachHang() != null ? p.getMaKhachHang().getHoTenKhachHang() : "")));
        pnlInfo.add(new JLabel("Nhân viên lập: " + (p.getMaNhanVien() != null ? p.getMaNhanVien().getHoTenNhanVien() : "")));
        pnlInfo.add(new JLabel("Ghi chú: " + (p.getGhiChu() != null ? p.getGhiChu() : "")));
        pnlInfo.add(new JLabel("Trạng thái: " + pdhBUS.trangThaiText(p)));

        DefaultTableModel modelCT = new DefaultTableModel(
                new Object[]{"Mã SP", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        for (ChiTietPhieuDatHang ct : chiTiets) {
            double thanhTien = Math.round(ct.getSoLuong() * ct.getDonGia());
            modelCT.addRow(new Object[]{
                    ct.getMaSanPham() != null ? ct.getMaSanPham().getMaSanPham() : "",
                    ct.getMaSanPham() != null ? ct.getMaSanPham().getTenSanPham() : "",
                    ct.getSoLuong(),
                    String.format("%,.0f", ct.getDonGia()),
                    String.format("%,.0f VNĐ", thanhTien)
            });
        }

        double tongTien = pdhBUS.tinhTongTien(chiTiets);

        JTable tblCT = new JTable(modelCT);
        tblCT.setRowHeight(24);

        JLabel lblTong = new JLabel("Tổng tiền phiếu đặt: " + String.format("%,.0f VNĐ", tongTien), JLabel.RIGHT);
        lblTong.setFont(new Font("Arial", Font.BOLD, 14));
        lblTong.setBorder(new EmptyBorder(8, 12, 12, 12));

        dialog.add(pnlInfo, BorderLayout.NORTH);
        dialog.add(new JScrollPane(tblCT), BorderLayout.CENTER);
        dialog.add(lblTong, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void xoaPDH() {
        PhieuDatHang p = pdhDangChon();
        if (p == null) return;
        if (pdhBUS.daThanhToan(p)) {
            JOptionPane.showMessageDialog(this, "Phiếu đã lập hóa đơn thanh toán, không thể hủy!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (p.getTrangThai() == TrangThaiPhieuDatHang.DA_HUY) {
            JOptionPane.showMessageDialog(this, "Phiếu này đã được hủy trước đó!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int chon = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn hủy phiếu đặt hàng " + p.getMaPhieuDatHang() + "?",
                "Xác nhận hủy", JOptionPane.YES_NO_OPTION);
        if (chon == JOptionPane.YES_OPTION) {
            pdhBUS.huyDat(p);
            JOptionPane.showMessageDialog(this, "Đã hủy phiếu đặt hàng thành công.");
            taiDuLieu();
        }
    }

    private JButton taoNut(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(fg);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setFocusPainted(false);
        return b;
    }
}