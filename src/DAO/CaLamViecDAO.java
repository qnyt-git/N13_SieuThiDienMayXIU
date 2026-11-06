package DAO;

import CONNECTDB.ConnectDB;
import ENTITY.CaLamViec;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CaLamViecDAO {

    private static final DateTimeFormatter GIO = DateTimeFormatter.ofPattern("HH:mm");

    // ===== CÁC PHƯƠNG THỨC THAO TÁC CSDL (CRUD & TẠO MÃ) =====

    public List<CaLamViec> getAll() {
        List<CaLamViec> list = new ArrayList<>();
        String sql = "SELECT * FROM CaLamViec";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CaLamViec c = new CaLamViec();
                c.setMaCa(rs.getString("maCa"));
                c.setTenCa(rs.getString("tenCa"));
                Timestamp start = rs.getTimestamp("gioBatDau");
                if (start != null) c.setGioBatDau(start.toLocalDateTime());
                Timestamp end = rs.getTimestamp("gioKetThuc");
                if (end != null) c.setGioKetThuc(end.toLocalDateTime());
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(CaLamViec c) {
        String sql = "INSERT INTO CaLamViec(maCa, tenCa, gioBatDau, gioKetThuc) VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getMaCa());
            ps.setString(2, c.getTenCa());
            ps.setTimestamp(3, c.getGioBatDau() != null ? Timestamp.valueOf(c.getGioBatDau()) : null);
            ps.setTimestamp(4, c.getGioKetThuc() != null ? Timestamp.valueOf(c.getGioKetThuc()) : null);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(CaLamViec c) {
        String sql = "UPDATE CaLamViec SET tenCa = ?, gioBatDau = ?, gioKetThuc = ? WHERE maCa = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getTenCa());
            ps.setTimestamp(2, c.getGioBatDau() != null ? Timestamp.valueOf(c.getGioBatDau()) : null);
            ps.setTimestamp(3, c.getGioKetThuc() != null ? Timestamp.valueOf(c.getGioKetThuc()) : null);
            ps.setString(4, c.getMaCa());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(CaLamViec c) {
        String sql = "DELETE FROM CaLamViec WHERE maCa = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getMaCa());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String taoMaMoi() {
        int max = 0;
        String sql = "SELECT MAX(TRY_CAST(SUBSTRING(maCa, 3, 10) AS INT)) FROM CaLamViec";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) max = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("CA%03d", max + 1);
    }

    // ===== CÁC PHƯƠNG THỨC XỬ LÝ (CHUYỂN TỪ ENTITY QUA) =====

    /** Kiểm tra giờ kết thúc > giờ bắt đầu */
    public boolean hopLe(CaLamViec c) {
        return c.getGioBatDau() != null && c.getGioKetThuc() != null
            && c.getGioKetThuc().isAfter(c.getGioBatDau());
    }

    /** Thời lượng ca (phút) */
    public long thoiLuongPhut(CaLamViec c) {
        if (!hopLe(c)) return 0;
        return java.time.Duration.between(c.getGioBatDau(), c.getGioKetThuc()).toMinutes();
    }

    /** Kiểm tra 1 thời điểm có nằm trong ca */
    public boolean trongCa(CaLamViec c, java.time.LocalDateTime thoiDiem) {
        return thoiDiem != null
            && !thoiDiem.isBefore(c.getGioBatDau())
            && !thoiDiem.isAfter(c.getGioKetThuc());
    }

    public String gioBatDauText(CaLamViec c) { 
        return c.getGioBatDau() == null ? "" : c.getGioBatDau().format(GIO); 
    }

    public String gioKetThucText(CaLamViec c) { 
        return c.getGioKetThuc() == null ? "" : c.getGioKetThuc().format(GIO); 
    }

    /** Đặt giờ bắt đầu/kết thúc theo ngày hôm nay. */
    public void datGio(CaLamViec c, java.time.LocalTime batDau, java.time.LocalTime ketThuc) {
        java.time.LocalDate ngay = java.time.LocalDate.now();
        c.setGioBatDau(ngay.atTime(batDau));
        c.setGioKetThuc(ngay.atTime(ketThuc));
    }

    /** Ca làm phải nằm trong khung 06:00 - 23:00. */
    public boolean trongKhungGioChoPhep(CaLamViec c) {
        return c.getGioBatDau() != null && c.getGioKetThuc() != null
            && !c.getGioBatDau().toLocalTime().isBefore(java.time.LocalTime.of(6, 0))
            && !c.getGioKetThuc().toLocalTime().isAfter(java.time.LocalTime.of(23, 0));
    }
}