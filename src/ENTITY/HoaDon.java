package ENTITY;

import java.time.LocalDateTime;

import ENUM.PhuongThucThanhToan;
import ENUM.TrangThaiHoaDon;

public class HoaDon {
	
	private String maHoaDon;							// mã hóa đơn
    private PhieuDatHang maPhieuDatHang;				// liên kết phiếu đặt hàng
    private KhachHang maKhachHang;						// liên kết khách hàng
    private NhanVien maNhanVien;						// liên kết nhân viên
    private NhaCungCap maNCC;							// liên kết nhà cung cấp
    private KhuyenMai maKhuyenMai;						// liên kết khuyến mãi	
    private PhuongThucThanhToan phuongThucThanhToan;	// chọn phương thức thanh toán
    private LocalDateTime ngayLap;						// ngày lập hóa đơn
    private String ghiChu;
    
    private TrangThaiHoaDon trangThai;					// khi lập hóa đơn: trạng thái mặc định là "Chờ xử lý (chờ thanh toán)"
    													// khi thanh tóan hóa đơn, trạng thái chuyển sang "đã thanh toán"
    public HoaDon() {}
    
	public HoaDon(String maHoaDon, PhieuDatHang maPhieuDatHang, KhachHang maKhachHang, NhanVien maNhanVien,
			NhaCungCap maNCC, KhuyenMai maKhuyenMai, PhuongThucThanhToan phuongThucThanhToan, LocalDateTime ngayLap,
			String ghiChu, TrangThaiHoaDon trangThai) {
		super();
		this.maHoaDon = maHoaDon;
		this.maPhieuDatHang = maPhieuDatHang;
		this.maKhachHang = maKhachHang;
		this.maNhanVien = maNhanVien;
		this.maNCC = maNCC;
		this.maKhuyenMai = maKhuyenMai;
		this.phuongThucThanhToan = phuongThucThanhToan;
		this.ngayLap = ngayLap;
		this.ghiChu = ghiChu;
		this.trangThai = trangThai;
	}
	public String getMaHoaDon() {
		return maHoaDon;
	}
	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}
	public PhieuDatHang getMaPhieuDatHang() {
		return maPhieuDatHang;
	}
	public void setMaPhieuDatHang(PhieuDatHang maPhieuDatHang) {
		this.maPhieuDatHang = maPhieuDatHang;
	}
	public KhachHang getMaKhachHang() {
		return maKhachHang;
	}
	public void setMaKhachHang(KhachHang maKhachHang) {
		this.maKhachHang = maKhachHang;
	}
	public NhanVien getMaNhanVien() {
		return maNhanVien;
	}
	public void setMaNhanVien(NhanVien maNhanVien) {
		this.maNhanVien = maNhanVien;
	}
	public NhaCungCap getMaNCC() {
		return maNCC;
	}
	public void setMaNCC(NhaCungCap maNCC) {
		this.maNCC = maNCC;
	}
	public KhuyenMai getMaKhuyenMai() {
		return maKhuyenMai;
	}
	public void setMaKhuyenMai(KhuyenMai maKhuyenMai) {
		this.maKhuyenMai = maKhuyenMai;
	}
	public PhuongThucThanhToan getPhuongThucThanhToan() {
		return phuongThucThanhToan;
	}
	public void setPhuongThucThanhToan(PhuongThucThanhToan phuongThucThanhToan) {
		this.phuongThucThanhToan = phuongThucThanhToan;
	}
	public LocalDateTime getNgayLap() {
		return ngayLap;
	}
	public void setNgayLap(LocalDateTime ngayLap) {
		this.ngayLap = ngayLap;
	}
	public String getGhiChu() {
		return ghiChu;
	}
	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}
	public TrangThaiHoaDon getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(TrangThaiHoaDon trangThai) {
		this.trangThai = trangThai;
	}
	@Override
	public String toString() {
		return "HoaDon [maHoaDon=" + maHoaDon + ", maPhieuDatHang=" + maPhieuDatHang + ", maKhachHang=" + maKhachHang
				+ ", maNhanVien=" + maNhanVien + ", maNCC=" + maNCC + ", maKhuyenMai=" + maKhuyenMai
				+ ", phuongThucThanhToan=" + phuongThucThanhToan + ", ngayLap=" + ngayLap + ", ghiChu=" + ghiChu
				+ ", trangThai=" + trangThai + "]";
	}










}