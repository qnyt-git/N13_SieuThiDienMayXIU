package GUI;

import ENTITY.ChiTietHoaDon;
import ENTITY.HoaDon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import BUS.BanHangBUS;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Hien thi va in hoa don ban hang theo mau giay in nhiet 80mm. */
public class HoaDonInGUI extends JDialog {

    private static final int DO_RONG = 40;
    private static final DecimalFormat DINH_DANG_TIEN = new DecimalFormat("#,##0");
    private static final DateTimeFormatter DINH_DANG_NGAY =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final JTextArea txtHoaDon = new JTextArea();

    public HoaDonInGUI(Window owner, HoaDon hoaDon, List<ChiTietHoaDon> chiTiet) {
        super(owner, "Hóa đơn bán hàng - Điện Máy XIU", ModalityType.APPLICATION_MODAL);

        txtHoaDon.setText(taoNoiDung(hoaDon, chiTiet));
        txtHoaDon.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        txtHoaDon.setEditable(false);
        txtHoaDon.setLineWrap(false);
        txtHoaDon.setMargin(new Insets(8, 8, 8, 8));
        txtHoaDon.setBackground(Color.WHITE);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(245, 247, 250));
        panel.add(new JScrollPane(txtHoaDon), BorderLayout.CENTER);

        JButton btnIn = new JButton("In hóa đơn");
        btnIn.setBackground(new Color(14, 98, 195));
        btnIn.setForeground(Color.WHITE);
        btnIn.setFocusPainted(false);

        JButton btnDong = new JButton("Đóng");
        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelNut.setOpaque(false);
        panelNut.add(btnDong);
        panelNut.add(btnIn);
        panel.add(panelNut, BorderLayout.SOUTH);

        btnDong.addActionListener(e -> dispose());
        btnIn.addActionListener(e -> inHoaDon());

        setContentPane(panel);
        setSize(430, 650);
        setMinimumSize(new Dimension(400, 500));
        setLocationRelativeTo(owner);
    }

    private String taoNoiDung(HoaDon hd, List<ChiTietHoaDon> ds) {
        StringBuilder sb = new StringBuilder();
        double tongTienHang = 0;
        double tongVAT = 0;

        sb.append(canGiua("ĐIỆN MÁY XIU")).append("\n");
        sb.append(canGiua("HÓA ĐƠN BÁN HÀNG")).append("\n");
        sb.append(canGiua("Địa Chỉ: 288,QuangTrung,Gò Vấp,")).append("\n");
        sb.append(canGiua("TPHCM")).append("\n");
        sb.append(keNgang()).append("\n");

        sb.append("Mã hóa đơn: ").append(anToan(hd.getMaHoaDon())).append("\n");
        sb.append("Ngày lập: ").append(hd.getNgayLap() == null
                ? "" : hd.getNgayLap().format(DINH_DANG_NGAY)).append("\n");
        sb.append("Khách hàng: ").append(hd.getMaKhachHang() == null
                ? "Khách lẻ" : anToan(hd.getMaKhachHang().getHoTenKhachHang())).append("\n");

        if (hd.getMaKhachHang() != null && hd.getMaKhachHang().getSoDienThoai() != null) {
            sb.append("SĐT: ").append(hd.getMaKhachHang().getSoDienThoai()).append("\n");
        }
        sb.append("Nhân viên: ").append(hd.getMaNhanVien() == null
                ? "" : anToan(hd.getMaNhanVien().getHoTenNhanVien())).append("\n");
        sb.append("Thanh toán: ").append(hd.getPhuongThucThanhToan() == null
                ? "" : hd.getPhuongThucThanhToan().getMoTa()).append("\n");
        sb.append(keNgang()).append("\n");
        sb.append("CHI TIẾT HÀNG HÓA\n");
        sb.append("STT  TÊN SẢN PHẨM\n");

        int stt = 1;
        if (ds != null) {
            for (ChiTietHoaDon ct : ds) {
                if (ct == null || ct.getMaSanPham() == null) continue;

                String tenSP = anToan(ct.getMaSanPham().getTenSanPham());
                sb.append(String.format("%02d.  ", stt++))
                        .append(catDong(tenSP, DO_RONG - 5).replace("\n", "\n     "))
                        .append("\n");

                double tienHang = BanHangBUS.tinhTienHang(ct);
                double tienThue = BanHangBUS.tinhTienVAT(ct);
                double thanhTien = BanHangBUS.tinhThanhTien(ct);
                tongTienHang += tienHang;
                tongVAT += tienThue;
                
                String dong = ct.getSoLuong() + " x " + DINH_DANG_TIEN.format(ct.getDonGia());
                sb.append(canTraiPhai(dong, DINH_DANG_TIEN.format(thanhTien))).append("\n");
                sb.append("VAT ").append(Math.round(ct.getThueVAT() * 100)).append("%: ")
                        .append(DINH_DANG_TIEN.format(tienThue)).append("\n");
            }
        }

        sb.append(keNgang()).append("\n");
        sb.append(canTraiPhai("Tiền hàng:", DINH_DANG_TIEN.format(tongTienHang))).append("\n");
        sb.append(canTraiPhai("Tổng VAT:", DINH_DANG_TIEN.format(tongVAT))).append("\n");
        sb.append(keNgang()).append("\n");
        double tongTruocGiam = tongTienHang + tongVAT;
        double tienGiam = 0;
        if (hd.getMaKhuyenMai() != null) {
            // Lay truc tiep thong tin khuyen mai da duoc gan va luu trong HoaDon.
            String maKM = anToan(hd.getMaKhuyenMai().getMaKhuyenMai());
            String tenKM = anToan(hd.getMaKhuyenMai().getTenKhuyenMai());
            double phanTramGiam = Math.max(0,
                    Math.min(100, hd.getMaKhuyenMai().getGiaTriGiam()));
            tienGiam = tongTruocGiam * phanTramGiam / 100.0;

            sb.append("KHUYẾN MÃI ÁP DỤNG\n");
            sb.append("Mã KM: ").append(maKM).append("\n");
            sb.append("Tên KM: ").append(tenKM).append("\n");
            sb.append(canTraiPhai("Mức giảm:",
                    String.format("%.0f%%", phanTramGiam))).append("\n");
            sb.append(canTraiPhai("Tiền giảm:",
                    DINH_DANG_TIEN.format(tienGiam))).append("\n");
        } else {
            sb.append("Khuyến mãi: Không áp dụng\n");
        }
        sb.append(canTraiPhai("TỔNG THANH TOÁN:",
                DINH_DANG_TIEN.format(Math.max(0, tongTruocGiam - tienGiam)))).append("\n");
        sb.append(keNgang()).append("\n\n");
        sb.append(canGiua("ĐIỆN MÁY XIU KÍNH CHÀO QUÝ KHÁCH!")).append("\n");
        sb.append(canGiua("Cảm ơn quý khách đã mua hàng.")).append("\n");
        return sb.toString();
    }

    private void inHoaDon() {
        try {
            boolean inThanhCong = txtHoaDon.print();
            if (inThanhCong) {
                JOptionPane.showMessageDialog(this, "Đã gửi hóa đơn đến máy in.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Không thể in hóa đơn: " + e.getMessage(),
                    "Lỗi in hóa đơn", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String keNgang() {
        return "----------------------------------------";
    }

    private String canGiua(String text) {
        if (text.length() >= DO_RONG) return text;
        int khoangTrang = (DO_RONG - text.length()) / 2;
        return lapKyTu(' ', khoangTrang) + text;
    }

    private String canTraiPhai(String trai, String phai) {
        int soKhoangTrang = DO_RONG - trai.length() - phai.length();
        if (soKhoangTrang < 1) soKhoangTrang = 1;
        return trai + lapKyTu(' ', soKhoangTrang) + phai;
    }

    private String catDong(String text, int doDai) {
        if (text == null || text.isEmpty()) return "";
        StringBuilder ketQua = new StringBuilder();
        for (int i = 0; i < text.length(); i += doDai) {
            if (ketQua.length() > 0) ketQua.append("\n");
            ketQua.append(text, i, Math.min(i + doDai, text.length()));
        }
        return ketQua.toString();
    }

    private String lapKyTu(char kyTu, int soLan) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < soLan; i++) sb.append(kyTu);
        return sb.toString();
    }

    private String anToan(String text) {
        return text == null ? "" : text;
    }
}
