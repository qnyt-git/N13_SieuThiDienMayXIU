package ENTITY;

public class KhachHang {

	private String maKhachHang;		// mã sinh tự động
    private String hoTenKhachHang;	// chỉ chứa chữ cái và khoảng trắng + viết hoa chữ cái đầu
    private boolean gioiTinh;		// nam/nữ
    private String soDienThoai;		// đủ 10 số + số 0 đứng đầu			
    private int diemTichLuy;		
    
    private String LoaiKH; 			// Khách mới / Khách vip
    								// sử dụng String để có thể thêm/xóa/sửa nhiều loại khách hàng khác
    public KhachHang() {}

	public KhachHang(String maKhachHang, String hoTenKhachHang, boolean gioiTinh, String soDienThoai, int diemTichLuy,
			String loaiKH) {
		super();
		this.maKhachHang = maKhachHang;
		this.hoTenKhachHang = hoTenKhachHang;
		this.gioiTinh = gioiTinh;
		this.soDienThoai = soDienThoai;
		this.diemTichLuy = diemTichLuy;
		LoaiKH = loaiKH;
	}

	public String getMaKhachHang() {
		return maKhachHang;
	}

	public void setMaKhachHang(String maKhachHang) {
		this.maKhachHang = maKhachHang;
	}

	public String getHoTenKhachHang() {
		return hoTenKhachHang;
	}

	public void setHoTenKhachHang(String hoTenKhachHang) {
		this.hoTenKhachHang = hoTenKhachHang;
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

	public int getDiemTichLuy() {
		return diemTichLuy;
	}

	public void setDiemTichLuy(int diemTichLuy) {
		this.diemTichLuy = diemTichLuy;
	}

	public String getLoaiKH() {
		return LoaiKH;
	}

	public void setLoaiKH(String loaiKH) {
		LoaiKH = loaiKH;
	}

	@Override
	public String toString() {
		return "KhachHang [maKhachHang=" + maKhachHang + ", hoTenKhachHang=" + hoTenKhachHang + ", gioiTinh=" + gioiTinh
				+ ", soDienThoai=" + soDienThoai + ", diemTichLuy=" + diemTichLuy + ", LoaiKH=" + LoaiKH + "]";
	}
    
    

}
