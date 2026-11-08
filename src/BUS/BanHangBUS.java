package BUS;

import DAO.HoaDonDAO;
import DAO.KhachHangDAO;
import DAO.KhuyenMaiDAO;
import DAO.SanPhamDAO;
import ENTITY.ChiTietHoaDon;
import ENTITY.HoaDon;
import ENTITY.KhachHang;
import ENTITY.KhuyenMai;
import ENTITY.SanPham;
import ENUM.TrangThaiSanPham;

import java.util.ArrayList;
import java.util.List;

/**
 * BUS xu ly nghiep vu ban hang.
 * Ket noi GUI voi DAO.
 */
public class BanHangBUS {

    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();

    private String lastError = "";

    // ===================== SAN PHAM =====================

    public List<KhuyenMai> getDanhSachKhuyenMai() {
        return khuyenMaiDAO.getAll();
    }

    public List<SanPham> getDanhSachSanPham() {
        List<SanPham> tatCa = sanPhamDAO.getAll();
        List<SanPham> ketQua = new ArrayList<>();
        if (tatCa == null) return ketQua;

        for (SanPham sp : tatCa) {
            if (sp != null && sp.getTrangThai() != TrangThaiSanPham.TAM_NGUNG) {
                ketQua.add(sp);
            }
        }
        return ketQua;
    }

    // ===================== KHACH HANG =====================

    public List<KhachHang> getDanhSachKhachHang() {
        return khachHangDAO.getAll();
    }

    // ===================== TAO MA HOA DON =====================

    public String taoMaHoaDon() {
        return hoaDonDAO.taoMaMoi();
    }

    // ===================== TINH TIEN VAT & KHUYEN MAI =====================

    public static double tinhTienHang(ChiTietHoaDon ct) {
        if (ct == null) return 0;
        return Math.round(ct.getSoLuong() * ct.getDonGia());
    }

    public static double tinhTienVAT(ChiTietHoaDon ct) {
        if (ct == null) return 0;
        double tyLeVAT = Math.round(ct.getThueVAT() * 100.0) / 100.0;
        return Math.round(tinhTienHang(ct) * tyLeVAT);
    }

    public static double tinhThanhTien(ChiTietHoaDon ct) {
        return tinhTienHang(ct) + tinhTienVAT(ct);
    }

    public static double tinhTienGiam(KhuyenMai km, double tongTien) {
        if (km == null || tongTien <= 0 || km.getGiaTriGiam() <= 0) {
            return 0;
        }
        double val = km.getGiaTriGiam();
        double tienGiam;
        if (val <= 1.0) {
            tienGiam = Math.round(tongTien * val);
        } else if (val <= 100.0) {
            tienGiam = Math.round(tongTien * (val / 100.0));
        } else {
            tienGiam = val;
        }
        return Math.min(tongTien, Math.max(0, tienGiam));
    }

    public static String moTaMucGiam(KhuyenMai km) {
        if (km == null || km.getGiaTriGiam() <= 0) return "0%";
        double val = km.getGiaTriGiam();
        if (val <= 1.0) {
            return String.format("%.0f%%", val * 100);
        } else if (val <= 100.0) {
            return String.format("%.0f%%", val);
        } else {
            return String.format("%,.0f VNĐ", val);
        }
    }

    // ===================== THANH TOAN =====================

    /**
     * Xu ly lap hoa don va luu chi tiet hoa don.
     */
    public boolean thanhToan(
            HoaDon hoaDon,
            List<ChiTietHoaDon> gioHang) {

        lastError = "";

        if (hoaDon == null) {
            lastError = "Thông tin hóa đơn không hợp lệ!";
            return false;
        }

        if (gioHang == null || gioHang.isEmpty()) {
            lastError = "Giỏ hàng đang trống!";
            return false;
        }

        for (ChiTietHoaDon ct : gioHang) {

            if (ct == null
                    || ct.getMaSanPham() == null
                    || ct.getSoLuong() <= 0) {
                lastError = "Chi tiết sản phẩm trong giỏ không hợp lệ!";
                return false;
            }

            if (ct.getSoLuong() > ct.getMaSanPham().getSoLuongTon()) {
                lastError = "Sản phẩm " + ct.getMaSanPham().getTenSanPham()
                        + " không đủ số lượng tồn kho!";
                return false;
            }

            // Gan hoa don cho chi tiet
            ct.setMaHoaDon(hoaDon);
        }

        boolean ok = hoaDonDAO.insertWithDetails(hoaDon, gioHang);
        if (!ok) {
            lastError = hoaDonDAO.getLastError();
        }
        return ok;
    }

    // ===================== THONG BAO LOI =====================

    public String getLastError() {
        if (lastError != null && !lastError.isEmpty()) {
            return lastError;
        }
        return hoaDonDAO.getLastError();
    }
}