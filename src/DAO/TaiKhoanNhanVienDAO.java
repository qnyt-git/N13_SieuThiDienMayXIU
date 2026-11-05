package DAO;

import CONNECTDB.ConnectDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaiKhoanNhanVienDAO {

    public String[] kiemTraDangNhap(String tenDangNhap, String matKhau) {
        String[] ketQua = null;
        String sql = "SELECT nv.hoTenNhanVien, tk.loaiQuyen " +
                     "FROM TaiKhoanNhanVien tk " +
                     "JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien " +
                     "WHERE tk.tenDangNhap = ? AND tk.matKhau = ? AND tk.trangThai = 1";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, tenDangNhap);
            pstmt.setString(2, matKhau);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hoTen = rs.getString("hoTenNhanVien");
                    String loaiQuyen = rs.getString("loaiQuyen");
                    ketQua = new String[] { hoTen, loaiQuyen };
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ketQua;
    }
}