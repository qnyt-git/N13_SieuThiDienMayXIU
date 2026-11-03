package ENTITY;

public class NhaCungCap {

	private String maNCC;		
    private String tenNCC;			// tên công ty
    private String tenThuongHieu; 	// panasonic, acer,.....
    private String diaChi;			// địa chỉ công ty
    private String soDienThoai;		
    private String email;
    private boolean trangThai; 		// còn hợp tác - tạm dừng
	public NhaCungCap(String maNCC, String tenNCC, String tenThuongHieu, String diaChi, String soDienThoai,
			String email, boolean trangThai) {
		super();
		this.maNCC = maNCC;
		this.tenNCC = tenNCC;
		this.tenThuongHieu = tenThuongHieu;
		this.diaChi = diaChi;
		this.soDienThoai = soDienThoai;
		this.email = email;
		this.trangThai = trangThai;
	}
	public String getMaNCC() {
		return maNCC;
	}
	public void setMaNCC(String maNCC) {
		this.maNCC = maNCC;
	}
	public String getTenNCC() {
		return tenNCC;
	}
	public void setTenNCC(String tenNCC) {
		this.tenNCC = tenNCC;
	}
	public String getTenThuongHieu() {
		return tenThuongHieu;
	}
	public void setTenThuongHieu(String tenThuongHieu) {
		this.tenThuongHieu = tenThuongHieu;
	}
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	public String getSoDienThoai() {
		return soDienThoai;
	}
	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isTrangThai() {
		return trangThai;
	}
	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}
	@Override
	public String toString() {
		return "NhaCungCap [maNCC=" + maNCC + ", tenNCC=" + tenNCC + ", tenThuongHieu=" + tenThuongHieu + ", diaChi="
				+ diaChi + ", soDienThoai=" + soDienThoai + ", email=" + email + ", trangThai=" + trangThai + "]";
	}

    
}
