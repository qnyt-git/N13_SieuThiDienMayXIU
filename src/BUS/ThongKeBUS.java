package BUS;

import CONNECTDB.ConnectDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Cac truy van thong ke co ban. */
public class ThongKeBUS {

    public List<Object[]> thongKeSanPham() {
        List<Object[]> ds = new ArrayList<>();
        String sql = "SELECT sp.maSanPham, sp.tenSanPham, " +
                "ISNULL(SUM(ct.soLuong), 0) AS daBan, sp.soLuongTon, " +
                "ISNULL(SUM(ct.soLuong * ct.donGia), 0) AS doanhThu " +
                "FROM SanPham sp LEFT JOIN ChiTietHoaDon ct ON sp.maSanPham = ct.maSanPham " +
                "GROUP BY sp.maSanPham, sp.tenSanPham, sp.soLuongTon ORDER BY sp.maSanPham";
        try (Connection c = ConnectDB.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                ds.add(new Object[]{r.getString("maSanPham"), r.getString("tenSanPham"),
                        r.getInt("daBan"), r.getInt("soLuongTon"), r.getDouble("doanhThu")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public List<Object[]> thongKeDoanhThuTheoThang(int nam) {
        List<Object[]> ds = new ArrayList<>();
        String sql = "SELECT MONTH(hd.ngayLap) AS thang, COUNT(DISTINCT hd.maHoaDon) AS soHoaDon, " +
                "ISNULL(SUM(ct.soLuong * ct.donGia * (1 + ct.thueVAT)), 0) AS doanhThu " +
                "FROM HoaDon hd LEFT JOIN ChiTietHoaDon ct ON hd.maHoaDon = ct.maHoaDon " +
                "WHERE YEAR(hd.ngayLap) = ? AND hd.trangThai = N'Đã thanh toán' " +
                "GROUP BY MONTH(hd.ngayLap) ORDER BY MONTH(hd.ngayLap)";
        try (Connection c = ConnectDB.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, nam);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    ds.add(new Object[]{r.getInt("thang"), r.getInt("soHoaDon"), r.getDouble("doanhThu")});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
}
