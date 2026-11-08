package DAO;

import CONNECTDB.ConnectDB;
import ENTITY.NhaCungCap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO của NhaCungCap.
 * Bao gồm các thao tác CSDL chuẩn (insert, update, delete, getAll, taoMaMoi)
 * và toàn bộ các phương thức xử lý nghiệp vụ được chuyển từ Entity qua.
 */
public class NhaCungCapDAO implements IDAO<NhaCungCap> {

    @Override
    public List<NhaCungCap> getAll() {
        List<NhaCungCap> list = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setMaNCC(rs.getString("maNCC"));
                ncc.setTenNCC(rs.getString("tenNCC"));
                ncc.setTenThuongHieu(rs.getString("tenThuongHieu"));
                ncc.setSoDienThoai(rs.getString("soDienThoai"));
                ncc.setDiaChi(rs.getString("diaChi"));
                ncc.setEmail(rs.getString("email"));
                ncc.setTrangThai(rs.getBoolean("trangThai"));
                list.add(ncc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean insert(NhaCungCap ncc) {
        String sql = "INSERT INTO NhaCungCap(maNCC, tenNCC, tenThuongHieu, soDienThoai, diaChi, email, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ncc.getMaNCC());
            ps.setString(2, ncc.getTenNCC());
            ps.setString(3, ncc.getTenThuongHieu());
            ps.setString(4, ncc.getSoDienThoai());
            ps.setString(5, ncc.getDiaChi());
            ps.setString(6, ncc.getEmail());
            ps.setBoolean(7, ncc.isTrangThai());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(NhaCungCap ncc) {
        String sql = "UPDATE NhaCungCap SET tenNCC = ?, tenThuongHieu = ?, soDienThoai = ?, diaChi = ?, email = ?, trangThai = ? WHERE maNCC = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getTenThuongHieu());
            ps.setString(3, ncc.getSoDienThoai());
            ps.setString(4, ncc.getDiaChi());
            ps.setString(5, ncc.getEmail());
            ps.setBoolean(6, ncc.isTrangThai());
            ps.setString(7, ncc.getMaNCC());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(NhaCungCap ncc) {
        String sql = "DELETE FROM NhaCungCap WHERE maNCC = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ncc.getMaNCC());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String taoMaMoi() {
        int max = 0;
        String sql = "SELECT MAX(TRY_CAST(SUBSTRING(maNCC, 4, 10) AS INT)) FROM NhaCungCap";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) max = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("NCC%02d", max + 1);
    }

    // ===================== CÁC PHƯƠNG THỨC XỬ LÝ (CHUYỂN TỪ ENTITY QUA) =====================

    public void capNhatThongTin(NhaCungCap ncc, String ten, String sdt, String diaChi, String email) {
        if (ncc == null) return;
        if (ten != null) ncc.setTenNCC(ten);
        if (sdt != null) ncc.setSoDienThoai(sdt);
        if (diaChi != null) ncc.setDiaChi(diaChi);
        if (email != null) ncc.setEmail(email);
    }

    public void dungHopTac(NhaCungCap ncc) { 
        if (ncc != null) ncc.setTrangThai(false); 
    }

    public void tiepTucHopTac(NhaCungCap ncc) { 
        if (ncc != null) ncc.setTrangThai(true); 
    }

    public String trangThaiText(NhaCungCap ncc) { 
        return ncc != null && ncc.isTrangThai() ? "Hợp tác" : "Tạm ngừng"; 
    }
}
