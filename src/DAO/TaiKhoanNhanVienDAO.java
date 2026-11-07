package DAO;

import CONNECTDB.ConnectDB;
import ENTITY.NhanVien;
import ENTITY.TaiKhoanNhanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TaiKhoanNhanVienDAO implements IDAO<TaiKhoanNhanVien> {

    /** Kiểm tra đăng nhập, trả về mảng [hoTenNhanVien, loaiQuyen] hoặc null nếu thất bại */
    public String[] kiemTraDangNhap(String tenDangNhap, String matKhau) {
    String[] ketQua = null;

    String sql = "SELECT nv.maNhanVien, nv.hoTenNhanVien, tk.loaiQuyen "
            + "FROM TaiKhoanNhanVien tk "
            + "JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien "
            + "WHERE tk.tenDangNhap = ? AND tk.matKhau = ? "
            + "AND tk.trangThai = 1";

    try (Connection con = ConnectDB.getConnection();
         PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setString(1, tenDangNhap);
        pstmt.setString(2, matKhau);

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                String maNV = rs.getString("maNhanVien");
                String hoTen = rs.getString("hoTenNhanVien");
                String loaiQuyen = rs.getString("loaiQuyen");

                ketQua = new String[]{maNV, hoTen, loaiQuyen};
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return ketQua;
}

    @Override
    public List<TaiKhoanNhanVien> getAll() {
        List<TaiKhoanNhanVien> list = new ArrayList<>();
        Map<String, NhanVien> nvMap = new HashMap<>();
        for (NhanVien nv : new NhanVienDAO().getAll()) {
            nvMap.put(nv.getMaNhanVien(), nv);
        }

        String sql = "SELECT * FROM TaiKhoanNhanVien";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TaiKhoanNhanVien tk = new TaiKhoanNhanVien(
                    rs.getString("maTaiKhoan"),
                    rs.getString("tenDangNhap"),
                    nvMap.get(rs.getString("maNhanVien")),
                    rs.getString("matKhau"),
                    rs.getString("loaiQuyen"),
                    rs.getBoolean("trangThai")
                );
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean insert(TaiKhoanNhanVien tk) {
        String sql = "INSERT INTO TaiKhoanNhanVien(maTaiKhoan, tenDangNhap, maNhanVien, matKhau, loaiQuyen, trangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tk.getMaTaiKhoan());
            ps.setString(2, tk.getTenDangNhap());
            ps.setString(3, tk.getMaNhanVien() != null ? tk.getMaNhanVien().getMaNhanVien() : null);
            ps.setString(4, tk.getMatKhau());
            ps.setString(5, tk.getLoaiQuyen());
            ps.setBoolean(6, tk.isTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(TaiKhoanNhanVien tk) {
        String sql = "UPDATE TaiKhoanNhanVien SET tenDangNhap = ?, maNhanVien = ?, matKhau = ?, loaiQuyen = ?, trangThai = ? WHERE maTaiKhoan = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tk.getTenDangNhap());
            ps.setString(2, tk.getMaNhanVien() != null ? tk.getMaNhanVien().getMaNhanVien() : null);
            ps.setString(3, tk.getMatKhau());
            ps.setString(4, tk.getLoaiQuyen());
            ps.setBoolean(5, tk.isTrangThai());
            ps.setString(6, tk.getMaTaiKhoan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(TaiKhoanNhanVien tk) {
        String sql = "DELETE FROM TaiKhoanNhanVien WHERE maTaiKhoan = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tk.getMaTaiKhoan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String taoMaMoi() {
        int max = 0;
        String sql = "SELECT MAX(TRY_CAST(SUBSTRING(maTaiKhoan, 3, 10) AS INT)) FROM TaiKhoanNhanVien";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) max = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.format("TK%02d", max + 1);
    }

    // ===================== CÁC PHƯƠNG THỨC NGHIỆP VỤ BỔ SUNG =====================

    /** Đổi mật khẩu tài khoản */
    public boolean doiMatKhau(TaiKhoanNhanVien tk, String matKhauCu, String matKhauMoi) {
        if (tk == null || !tk.getMatKhau().equals(matKhauCu)) return false;
        if (matKhauMoi == null || matKhauMoi.length() < 6) return false;
        tk.setMatKhau(matKhauMoi);
        return update(tk);
    }

    /** Đổi trạng thái hoạt động / vô hiệu hóa */
    public void doiTrangThai(TaiKhoanNhanVien tk) {
        if (tk == null) return;
        tk.setTrangThai(!tk.isTrangThai());
        update(tk);
    }

    /** Kiểm tra xem tài khoản có phải quản lý không */
    public boolean laQuanLy(TaiKhoanNhanVien tk) {
        return tk != null && "QUAN_LY".equalsIgnoreCase(tk.getLoaiQuyen());
    }
}
