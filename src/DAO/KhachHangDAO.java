package DAO;

import CONNECTDB.ConnectDB;
import ENTITY.KhachHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO của KhachHang.
 * Thực hiện các thao tác CRUD và xử lý nghiệp vụ liên quan đến khách hàng.
 */
public class KhachHangDAO implements IDAO<KhachHang> {

    @Override
    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT maKhachHang, hoTenKhachHang, gioiTinh, soDienThoai, diemTichLuy, LoaiKH FROM KhachHang";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                KhachHang kh = new KhachHang(
                    rs.getString("maKhachHang"),
                    rs.getString("hoTenKhachHang"),
                    rs.getBoolean("gioiTinh"),
                    rs.getString("soDienThoai"),
                    rs.getInt("diemTichLuy"),
                    rs.getString("LoaiKH")
                );
                list.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean insert(KhachHang kh) {
        String sql = "INSERT INTO KhachHang(maKhachHang, hoTenKhachHang, gioiTinh, soDienThoai, diemTichLuy, LoaiKH) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKhachHang());
            ps.setString(2, kh.getHoTenKhachHang());
            ps.setBoolean(3, kh.isGioiTinh());
            ps.setString(4, kh.getSoDienThoai());
            ps.setInt(5, kh.getDiemTichLuy());
            ps.setString(6, kh.getLoaiKH());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(KhachHang kh) {
        String sql = "UPDATE KhachHang SET hoTenKhachHang = ?, gioiTinh = ?, soDienThoai = ?, diemTichLuy = ?, LoaiKH = ? WHERE maKhachHang = ?";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getHoTenKhachHang());
            ps.setBoolean(2, kh.isGioiTinh());
            ps.setString(3, kh.getSoDienThoai());
            ps.setInt(4, kh.getDiemTichLuy());
            ps.setString(5, kh.getLoaiKH());
            ps.setString(6, kh.getMaKhachHang());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(KhachHang kh) {
        String sql = "DELETE FROM KhachHang WHERE maKhachHang = ?";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKhachHang());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String taoMaMoi() {
        int max = 0;
        String sql = "SELECT MAX(TRY_CAST(SUBSTRING(maKhachHang, 3, 10) AS INT)) FROM KhachHang";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) max = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("KH%02d", max + 1);
    }

    // ===== CÁC PHƯƠNG THỨC XỬ LÝ NGHIỆP VỤ (ĐƯA TỪ ENTITY QUA DAO) =====

    public void capNhatThongTin(KhachHang kh) {
        // Logic xử lý cập nhật thông tin bổ sung nếu cần
    }

    public void capNhatDiemTichLuy(KhachHang kh, int diem) {
        if (kh == null) return;
        kh.setDiemTichLuy(kh.getDiemTichLuy() + diem);
        update(kh); // Cập nhật trực tiếp xuống CSDL
    }

    public void xemLichSuMuaHang(KhachHang kh) {
        // Logic xử lý xem lịch sử mua hàng của khách
    }

    public void congDiemTuHoaDon(KhachHang kh, double thanhTien) {
        if (kh == null) return;
        int diemThem = (int) (thanhTien / 100_000);
        capNhatDiemTichLuy(kh, diemThem);
    }

    
}
