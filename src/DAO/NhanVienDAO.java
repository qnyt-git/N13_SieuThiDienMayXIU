package DAO;

import CONNECTDB.ConnectDB;
import ENTITY.NhanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO của NhanVien.
 * Bao gồm các thao tác CSDL (insert, update, delete, getAll, taoMaMoi)
 * và toàn bộ các phương thức xử lý nghiệp vụ, tính lương, kiểm tra tuổi được chuyển từ Entity qua.
 */
public class NhanVienDAO implements IDAO<NhanVien> {

    @Override
    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT maNhanVien, hoTenNhanVien, gioiTinh, soDienThoai, ngaySinh, trinhDoHocVan, chucVu, heSoLuong, luongCoBan, trangThai FROM NhanVien";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNhanVien(rs.getString("maNhanVien"));
                nv.setHoTenNhanVien(rs.getString("hoTenNhanVien"));
                nv.setGioiTinh(rs.getBoolean("gioiTinh"));
                nv.setChucVu(rs.getString("chucVu"));
                nv.setSoDienThoai(rs.getString("soDienThoai"));
                
                Date ns = rs.getDate("ngaySinh");
                if (ns != null) nv.setNgaySinh(ns.toLocalDate());
                
                nv.setTrinhDoHocVan(rs.getString("trinhDoHocVan"));
                nv.setHeSoLuong(rs.getFloat("heSoLuong"));
                nv.setLuongCoBan(rs.getInt("luongCoBan"));
                nv.setTrangThai(rs.getBoolean("trangThai"));
                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean insert(NhanVien nv) {
        String sql = "INSERT INTO NhanVien(maNhanVien, hoTenNhanVien, gioiTinh, chucVu, soDienThoai, ngaySinh, trinhDoHocVan, heSoLuong, luongCoBan, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNhanVien());
            ps.setString(2, nv.getHoTenNhanVien());
            ps.setBoolean(3, nv.isGioiTinh());
            ps.setString(4, nv.getChucVu());
            ps.setString(5, nv.getSoDienThoai());
            ps.setDate(6, nv.getNgaySinh() != null ? Date.valueOf(nv.getNgaySinh()) : null);
            ps.setString(7, nv.getTrinhDoHocVan());
            ps.setFloat(8, nv.getHeSoLuong());
            ps.setInt(9, nv.getLuongCoBan());
            ps.setBoolean(10, nv.isTrangThai());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(NhanVien nv) {
        String sql = "UPDATE NhanVien SET hoTenNhanVien = ?, gioiTinh = ?, chucVu = ?, soDienThoai = ?, ngaySinh = ?, trinhDoHocVan = ?, heSoLuong = ?, luongCoBan = ?, trangThai = ? WHERE maNhanVien = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getHoTenNhanVien());
            ps.setBoolean(2, nv.isGioiTinh());
            ps.setString(3, nv.getChucVu());
            ps.setString(4, nv.getSoDienThoai());
            ps.setDate(5, nv.getNgaySinh() != null ? Date.valueOf(nv.getNgaySinh()) : null);
            ps.setString(6, nv.getTrinhDoHocVan());
            ps.setFloat(7, nv.getHeSoLuong());
            ps.setInt(8, nv.getLuongCoBan());
            ps.setBoolean(9, nv.isTrangThai());
            ps.setString(10, nv.getMaNhanVien());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(NhanVien nv) {
        String sql = "DELETE FROM NhanVien WHERE maNhanVien = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNhanVien());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String taoMaMoi() {
        int max = 0;
        String sql = "SELECT MAX(TRY_CAST(SUBSTRING(maNhanVien, 3, 10) AS INT)) FROM NhanVien";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) max = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("NV%02d", max + 1);
    }

    // ===================== CÁC PHƯƠNG THỨC XỬ LÝ (CHUYỂN TỪ ENTITY QUA) =====================

    /** Tính lương */
    public double tinhLuong(NhanVien nv) {
        if (nv == null) return 0;
        return nv.getHeSoLuong() * nv.getLuongCoBan();
    }

    /** Kiểm tra còn làm việc */
    public boolean conLamViec(NhanVien nv) { 
        return nv != null && nv.isTrangThai(); 
    }

    /** Nghỉ việc */
    public void nghiViec(NhanVien nv) { 
        if (nv != null) nv.setTrangThai(false); 
    }

    /** Cập nhật thông tin cá nhân */
    public void capNhatThongTin(NhanVien nv, String hoTen, String sdt, LocalDate ngaySinh) {
        if (nv == null) return;
        if (hoTen != null && !hoTen.isEmpty()) nv.setHoTenNhanVien(hoTen);
        if (sdt != null && !sdt.isEmpty()) nv.setSoDienThoai(sdt);
        if (ngaySinh != null) nv.setNgaySinh(ngaySinh);
    }

    /** Đăng ký ca (placeholder) */
    public void dangKyCaLam(NhanVien nv) {
        // sẽ được xử lý bởi PhanCa
    }

    /** Tuổi tính đến hôm nay. */
    public int tuoi(NhanVien nv) {
        if (nv == null || nv.getNgaySinh() == null) return 0;
        return Period.between(nv.getNgaySinh(), LocalDate.now()).getYears();
    }

    /** Ngày sinh hợp lệ: không ở tương lai và đủ 18 tuổi. */
    public boolean hopLeNgaySinh(NhanVien nv) {
        if (nv == null || nv.getNgaySinh() == null) return false;
        return !nv.getNgaySinh().isAfter(LocalDate.now()) && tuoi(nv) >= 18;
    }

    public String trangThaiText(NhanVien nv) { 
        return nv != null && nv.isTrangThai() ? "Đang làm" : "Nghỉ việc"; 
    }

    /** Hệ số lương dạng double (tránh sai số float). */
    public double heSoLuongDouble(NhanVien nv) { 
        if (nv == null) return 0;
        return Double.parseDouble(Float.toString(nv.getHeSoLuong())); 
    }
}
