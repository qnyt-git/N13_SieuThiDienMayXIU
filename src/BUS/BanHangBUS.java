
package BUS;

import DAO.HoaDonDAO;
import DAO.KhachHangDAO;
import DAO.SanPhamDAO;
import DAO.KhuyenMaiDAO;
import ENTITY.KhuyenMai;
import ENTITY.ChiTietHoaDon;
import ENTITY.HoaDon;
import ENTITY.KhachHang;
import ENTITY.SanPham;

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

    // ===================== SAN PHAM =====================
    public List<KhuyenMai> getDanhSachKhuyenMai() {
        return new KhuyenMaiDAO().getAll();
    }
    public List<SanPham> getDanhSachSanPham() {
        return sanPhamDAO.getAll();
    }

    // ===================== KHACH HANG =====================

    public List<KhachHang> getDanhSachKhachHang() {
        return khachHangDAO.getAll();
    }

    // ===================== TAO MA HOA DON =====================

    public String taoMaHoaDon() {
        return hoaDonDAO.taoMaMoi();
    }

    // ===================== THANH TOAN =====================

    /**
     * Xu ly lap hoa don va luu chi tiet hoa don.
     */
    public boolean thanhToan(
            HoaDon hoaDon,
            List<ChiTietHoaDon> gioHang) {

        if (hoaDon == null) {
            return false;
        }

        if (gioHang == null || gioHang.isEmpty()) {
            return false;
        }

        for (ChiTietHoaDon ct : gioHang) {

            if (ct == null
                    || ct.getMaSanPham() == null
                    || ct.getSoLuong() <= 0) {
                return false;
            }

            // Gan hoa don cho chi tiet
            ct.setMaHoaDon(hoaDon);
        }

        return hoaDonDAO.insertWithDetails(hoaDon, gioHang);
    }

    // ===================== THONG BAO LOI =====================

    public String getLastError() {
        return hoaDonDAO.getLastError();
    }
}
