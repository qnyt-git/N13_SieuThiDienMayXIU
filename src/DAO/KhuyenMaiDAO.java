package DAO;

import CONNECTDB.ConnectDB;
import ENTITY.KhuyenMai;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class KhuyenMaiDAO implements IDAO<KhuyenMai> {

    @Override
    public List<KhuyenMai> getAll() {
        List<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKhuyenMai(rs.getString("maKhuyenMai"));
                km.setTenKhuyenMai(rs.getString("tenKhuyenMai"));
                
                Timestamp bd = rs.getTimestamp("thoiGianBatDau");
                if (bd != null) km.setThoiGianBatDau(bd.toLocalDateTime());
                
                Timestamp kt = rs.getTimestamp("thoiGianKetThuc");
                if (kt != null) km.setThoiGianKetThuc(kt.toLocalDateTime());
                
                km.setGiaTriGiam(rs.getDouble("giaTriGiam"));
                km.setTrangThai(rs.getBoolean("trangThai"));
                list.add(km);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean insert(KhuyenMai km) {
        String sql = "INSERT INTO KhuyenMai(maKhuyenMai, tenKhuyenMai, thoiGianBatDau, thoiGianKetThuc, giaTriGiam, trangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getMaKhuyenMai());
            ps.setString(2, km.getTenKhuyenMai());
            ps.setTimestamp(3, km.getThoiGianBatDau() != null ? Timestamp.valueOf(km.getThoiGianBatDau()) : null);
            ps.setTimestamp(4, km.getThoiGianKetThuc() != null ? Timestamp.valueOf(km.getThoiGianKetThuc()) : null);
            ps.setDouble(5, km.getGiaTriGiam());
            ps.setBoolean(6, km.isTrangThai());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET tenKhuyenMai = ?, thoiGianBatDau = ?, thoiGianKetThuc = ?, giaTriGiam = ?, trangThai = ? WHERE maKhuyenMai = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getTenKhuyenMai());
            ps.setTimestamp(2, km.getThoiGianBatDau() != null ? Timestamp.valueOf(km.getThoiGianBatDau()) : null);
            ps.setTimestamp(3, km.getThoiGianKetThuc() != null ? Timestamp.valueOf(km.getThoiGianKetThuc()) : null);
            ps.setDouble(4, km.getGiaTriGiam());
            ps.setBoolean(5, km.isTrangThai());
            ps.setString(6, km.getMaKhuyenMai());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(KhuyenMai km) {
        String sql = "DELETE FROM KhuyenMai WHERE maKhuyenMai = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getMaKhuyenMai());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String taoMaMoi() {
        int max = 0;
        String sql = "SELECT MAX(TRY_CAST(SUBSTRING(maKhuyenMai, 3, 10) AS INT)) FROM KhuyenMai";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) max = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("KM%02d", max + 1);
    }

    // ===================== CÁC PHƯƠNG THỨC XỬ LÝ (CHUYỂN TỪ ENTITY QUA) =====================

    /** Kiểm tra khuyến mãi còn hiệu lực */
    public boolean conHieuLuc(KhuyenMai km) {
        if (km == null || !km.isTrangThai()) return false;
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(km.getThoiGianBatDau()) && now.isBefore(km.getThoiGianKetThuc());
    }

    /** Tính tiền giảm từ tổng tiền */
    public double tinhTienGiam(KhuyenMai km, double tongTien) {
        if (!conHieuLuc(km)) return 0;
        return tongTien * (km.getGiaTriGiam() / 100.0);
    }

    /** Áp dụng giảm giá */
    public double apDung(KhuyenMai km, double tongTien) {
        return tongTien - tinhTienGiam(km, tongTien);
    }

    /** Gia hạn */
    public void giaHan(KhuyenMai km, LocalDateTime ngayKetThucMoi) {
        if (km != null && ngayKetThucMoi.isAfter(km.getThoiGianBatDau())) {
            km.setThoiGianKetThuc(ngayKetThucMoi);
        }
    }

    public String trangThaiText(KhuyenMai km) { 
        return km != null && km.isTrangThai() ? "Đang áp dụng" : "Ngừng"; 
    }

    /** Đặt thời gian theo ngày: từ 00:00 ngày bắt đầu đến 23:59:59 ngày kết thúc. */
    public void datThoiGian(KhuyenMai km, LocalDate batDau, LocalDate ketThuc) {
        if (km == null) return;
        km.setThoiGianBatDau(batDau.atStartOfDay());
        km.setThoiGianKetThuc(ketThuc.atTime(23, 59, 59));
    }

    /** Miền giá trị: 0 < giá trị giảm <= 100 và ngày kết thúc không trước ngày bắt đầu. */
    public boolean hopLe(KhuyenMai km) {
        if (km == null) return false;
        return km.getGiaTriGiam() > 0 && km.getGiaTriGiam() <= 100 
            && km.getThoiGianBatDau() != null && km.getThoiGianKetThuc() != null
            && !km.getThoiGianKetThuc().isBefore(km.getThoiGianBatDau());
    }

    /** Tiền giảm theo % đã áp dụng lúc lập hóa đơn (không phụ thuộc hạn hiện tại của khuyến mãi). */
    public double tinhTienGiamDaApDung(KhuyenMai km, double tongTien) {
        if (km == null) return 0;
        return tongTien * (km.getGiaTriGiam() / 100.0);
    }
}
