package ENTITY;

import ENUM.LoaiQuyen;

public class TaiKhoanNhanVien {

    private String maTaiKhoan;		// mã sinh tự động
    private String tenDangNhap;		// 
    private NhanVien maNhanVien;	// 	
    private String matKhau;			// phải ẩn/hiện được mật khẩu
    private LoaiQuyen loaiQuyen; 	// có thể thêm/xóa/sửa loại quyền
    private boolean trangThai; 		//hoạt động - vô hiệu hóa
	public TaiKhoanNhanVien(String maTaiKhoan, String tenDangNhap, NhanVien maNhanVien, String matKhau,
			LoaiQuyen loaiQuyen, boolean trangThai) {
		super();
		this.maTaiKhoan = maTaiKhoan;
		this.tenDangNhap = tenDangNhap;
		this.maNhanVien = maNhanVien;
		this.matKhau = matKhau;
		this.loaiQuyen = loaiQuyen;
		this.trangThai = trangThai;
	}
	public String getMaTaiKhoan() {
		return maTaiKhoan;
	}
	public void setMaTaiKhoan(String maTaiKhoan) {
		this.maTaiKhoan = maTaiKhoan;
	}
	public String getTenDangNhap() {
		return tenDangNhap;
	}
	public void setTenDangNhap(String tenDangNhap) {
		this.tenDangNhap = tenDangNhap;
	}
	public NhanVien getMaNhanVien() {
		return maNhanVien;
	}
	public void setMaNhanVien(NhanVien maNhanVien) {
		this.maNhanVien = maNhanVien;
	}
	public String getMatKhau() {
		return matKhau;
	}
	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}
	public LoaiQuyen getLoaiQuyen() {
		return loaiQuyen;
	}
	public void setLoaiQuyen(LoaiQuyen loaiQuyen) {
		this.loaiQuyen = loaiQuyen;
	}
	public boolean isTrangThai() {
		return trangThai;
	}
	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}
	@Override
	public String toString() {
		return "TaiKhoanNhanVien [maTaiKhoan=" + maTaiKhoan + ", tenDangNhap=" + tenDangNhap + ", maNhanVien="
				+ maNhanVien + ", matKhau=" + matKhau + ", loaiQuyen=" + loaiQuyen + ", trangThai=" + trangThai + "]";
	}
    
    
}
