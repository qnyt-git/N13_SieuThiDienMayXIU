package ENTITY;

public class ChiTietHoaDon {

	private HoaDon maHoaDon;
    private SanPham maSanPham;
    private int soLuong;
    private double donGia;
    private float thueVAT;  	// thuế này có thể chỉnh sửa trong tương lai
    							// khi chỉnh sửa thì thuế nhập vào ví dụ 23 thì sẽ đổi thành 0.23 (tại vì là %)
    							// khi nhập hóa đơn thuế được định sẵn dưới hóa đơn là 10% hay 8% gì đó
    							// không nhập thủ công thuế
    
    public ChiTietHoaDon() {}
    							
    						 
	public ChiTietHoaDon(HoaDon maHoaDon, SanPham maSanPham, int soLuong, double donGia, float thueVAT) {
		super();
		this.maHoaDon = maHoaDon;
		this.maSanPham = maSanPham;
		this.soLuong = soLuong;
		this.donGia = donGia;
		this.thueVAT = thueVAT;
	}
	public HoaDon getMaHoaDon() {
		return maHoaDon;
	}
	public void setMaHoaDon(HoaDon maHoaDon) {
		this.maHoaDon = maHoaDon;
	}
	public SanPham getMaSanPham() {
		return maSanPham;
	}
	public void setMaSanPham(SanPham maSanPham) {
		this.maSanPham = maSanPham;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	public double getDonGia() {
		return donGia;
	}
	public void setDonGia(double donGia) {
		this.donGia = donGia;
	}
	public float getThueVAT() {
		return thueVAT;
	}
	public void setThueVAT(float thueVAT) {
		this.thueVAT = thueVAT;
	}
	@Override
	public String toString() {
		return "ChiTietHoaDon [maHoaDon=" + maHoaDon + ", maSanPham=" + maSanPham + ", soLuong=" + soLuong + ", donGia="
				+ donGia + ", thueVAT=" + thueVAT + "]";
	}
    
    
}
