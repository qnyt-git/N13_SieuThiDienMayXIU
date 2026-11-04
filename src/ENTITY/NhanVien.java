package ENTITY;

import java.time.LocalDate;

public class NhanVien {
	 private String maNhanVien;		// mã sinh tự động - không nhập thủ công
	    private String hoTenNhanVien; //chỉ chứa chữ cái và khoảng trắng + chữ cái đầu viết hoa
	    private boolean gioiTinh; //Nam hoặc nữ
	    private String soDienThoai; // tối đa 10 số + số 0 đứng đầu
	    private LocalDate ngaySinh;	// xổ lịch xuống chọn ngaySinh
	    private String trinhDoHocVan; //điền các thông tin về trình độ học vấn
	    
	    private String chucVu;		// nhân viên quản lý / nhân viên bán hàng / nhân viên kế toán / nhân viên kho / nhân viên CSKH
	    private float heSoLuong; // ví dụ: 2.1; 2.2; .....
	    private int luongCoBan;
	  
	    private boolean trangThai; //mặc định khi thêm nhân viên - trạng thái là "đang làm việc"
	    							// khi nào chỉnh sửa nhân viên mới là "đang làm việc"/"nghỉ"
	    
	    public NhanVien() {}

		public NhanVien(String maNhanVien, String hoTenNhanVien, boolean gioiTinh, String soDienThoai,
				LocalDate ngaySinh, String trinhDoHocVan, String chucVu, float heSoLuong, int luongCoBan,
				boolean trangThai) {
			super();
			this.maNhanVien = maNhanVien;
			this.hoTenNhanVien = hoTenNhanVien;
			this.gioiTinh = gioiTinh;
			this.soDienThoai = soDienThoai;
			this.ngaySinh = ngaySinh;
			this.trinhDoHocVan = trinhDoHocVan;
			this.chucVu = chucVu;
			this.heSoLuong = heSoLuong;
			this.luongCoBan = luongCoBan;
			this.trangThai = trangThai;
		}

		public String getMaNhanVien() {
			return maNhanVien;
		}

		public void setMaNhanVien(String maNhanVien) {
			this.maNhanVien = maNhanVien;
		}

		public String getHoTenNhanVien() {
			return hoTenNhanVien;
		}

		public void setHoTenNhanVien(String hoTenNhanVien) {
			this.hoTenNhanVien = hoTenNhanVien;
		}

		public boolean isGioiTinh() {
			return gioiTinh;
		}

		public void setGioiTinh(boolean gioiTinh) {
			this.gioiTinh = gioiTinh;
		}

		public String getSoDienThoai() {
			return soDienThoai;
		}

		public void setSoDienThoai(String soDienThoai) {
			this.soDienThoai = soDienThoai;
		}

		public LocalDate getNgaySinh() {
			return ngaySinh;
		}

		public void setNgaySinh(LocalDate ngaySinh) {
			this.ngaySinh = ngaySinh;
		}

		public String getTrinhDoHocVan() {
			return trinhDoHocVan;
		}

		public void setTrinhDoHocVan(String trinhDoHocVan) {
			this.trinhDoHocVan = trinhDoHocVan;
		}

		public String getChucVu() {
			return chucVu;
		}

		public void setChucVu(String chucVu) {
			this.chucVu = chucVu;
		}

		public float getHeSoLuong() {
			return heSoLuong;
		}

		public void setHeSoLuong(float heSoLuong) {
			this.heSoLuong = heSoLuong;
		}

		public int getLuongCoBan() {
			return luongCoBan;
		}

		public void setLuongCoBan(int luongCoBan) {
			this.luongCoBan = luongCoBan;
		}

		public boolean isTrangThai() {
			return trangThai;
		}

		public void setTrangThai(boolean trangThai) {
			this.trangThai = trangThai;
		}

		@Override
		public String toString() {
			return "NhanVien [maNhanVien=" + maNhanVien + ", hoTenNhanVien=" + hoTenNhanVien + ", gioiTinh=" + gioiTinh
					+ ", soDienThoai=" + soDienThoai + ", ngaySinh=" + ngaySinh + ", trinhDoHocVan=" + trinhDoHocVan
					+ ", chucVu=" + chucVu + ", heSoLuong=" + heSoLuong + ", luongCoBan=" + luongCoBan + ", trangThai="
					+ trangThai + "]";
		}
	    
	    
	

}
