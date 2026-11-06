package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import CONNECTDB.ConnectDB;
import ENTITY.NhanVien;

public class NhanVienDAO {

	public ArrayList<NhanVien> layDanhSachNhanVien() {
		ArrayList<NhanVien> danhsach = new ArrayList<NhanVien>();
		String sql = "SELECT * FROM NhanVien";
		
		//gọi kết nối db
		try {
			Connection con = ConnectDB.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			//duyệt qa các dòng dữ liệu
			while (rs.next()) {
				// Lấy dữ liệu
				String maNV 			= rs.getString("maNhanVien");
				String hoTenNV 			= rs.getString("hoTenNhanVien");
				boolean gt 				= rs.getBoolean("gioiTinh");
				String sdt				= rs.getString("soDienThoai");
				
				// chuyển kiểu dữ liệu Date qa LocalDate
				Date ngaySinh			= rs.getDate("ngaySinh");
				java.time.LocalDate ngS = (ngaySinh != null) ? ngaySinh.toLocalDate() : null;
				
				String trinhDoHocVan	= rs.getString("trinhDoHocVan");
				String chucVu			= rs.getString("chucVu");
				float heSoLuong 		= rs.getFloat("heSoLuong");
				int luongCoBan 			= rs.getInt("luongCoBan");
				boolean trangThai 		= rs.getBoolean("trangThai");
				
				NhanVien nv = new NhanVien(maNV, hoTenNV, gt, sdt, ngS, trinhDoHocVan, chucVu, heSoLuong, luongCoBan, trangThai);
				danhsach.add(nv);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return danhsach;
	}
	
	// tìm kiếm
	public NhanVien timTheoMa(String maNhanVien) throws SQLException{
		String sql = "SELECT maNhanVien, hoTenNhanVien, gioiTinh, chucVu, soDienThoai, ngaySinh, trinhDoHocVan, heSoLuong, luongCoBan, trangThai"
				+ " FROM NhanVien WHERE maNhanVien = ?";
		try (PreparedStatement pstmt = ConnectDB.getConnection().prepareStatement(sql)){
			pstmt.setString(1,maNhanVien); //gán giá trị vô ? để tìm 
			try (ResultSet rs = pstmt.executeQuery() ){ //thực thi truy vấn
				if(rs.next()) {
					// Lấy dữ liệu
					String maNV 			= rs.getString("maNhanVien");
					String hoTenNV 			= rs.getString("hoTenNhanVien");
					boolean gt 				= rs.getBoolean("gioiTinh");
					String sdt				= rs.getString("soDienThoai");
					
					// chuyển kiểu dữ liệu Date qa LocalDate
					Date ngaySinh			= rs.getDate("ngaySinh");
					java.time.LocalDate ngS = (ngaySinh != null) ? ngaySinh.toLocalDate() : null;
					
					String trinhDoHocVan	= rs.getString("trinhDoHocVan");
					String chucVu			= rs.getString("chucVu");
					float heSoLuong 		= rs.getFloat("heSoLuong");
					int luongCoBan 			= rs.getInt("luongCoBan");
					boolean trangThai 		= rs.getBoolean("trangThai");
					
					return new NhanVien(maNV, hoTenNV, gt, sdt, ngS, trinhDoHocVan, chucVu, heSoLuong, luongCoBan, trangThai);
				}
				return null;
			}
		}
		
	}
	
//	// thêm mới
//	public NhanVien themMoiNhanVien(NhanVien nv) throws SQLException {
//		
//	}
//	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
