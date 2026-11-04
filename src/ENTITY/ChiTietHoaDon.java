package ENTITY;

public class ChiTietHoaDon {

	private HoaDon maHoaDon;
    private SanPham maSanPham;
    private int soLuong;
    private double donGia;
    private float thueVAT;			// Thuế VAT: lấy chung 0.08 (8%)
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
