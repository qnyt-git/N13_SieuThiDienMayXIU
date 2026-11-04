package ENTITY;

import java.time.LocalDateTime;

import ENUM.TrangThaiPhieuDatHang;

public class PhieuDatHang {
	
	private String maPhieuDatHang;					// mã phiếu đặt hàng
    private KhachHang maKhachHang;					// liên kết khách hàng
    private NhanVien maNhanVien;					// liên kết nhân viên lập phiếu đặt
    private LocalDateTime ngayDatHang;				// ngày lập phiếu đặt
    private String ghiChu;
    //private KhuyenMai maKhuyenMai; 				// khuyến mãi được chọn  khi tới bước thanh toán
    
    private TrangThaiPhieuDatHang trangThai; 		// khi lập phiếu đăt: trạng thái mặc định là "Chờ xử lý (chờ thanh toán)"
													// khi thanh tóan hóa đơn, trạng thái chuyển sang "đã thanh toán"
    												// khi muốn hủy phiếu đựat - chọn chức năng hủy phiếu và trạng thái phiếu "Đã hủy"

	public PhieuDatHang(String maPhieuDatHang, KhachHang maKhachHang, NhanVien maNhanVien, LocalDateTime ngayDatHang,
			String ghiChu, TrangThaiPhieuDatHang trangThai) {
		super();
		this.maPhieuDatHang = maPhieuDatHang;
		this.maKhachHang = maKhachHang;
		this.maNhanVien = maNhanVien;
		this.ngayDatHang = ngayDatHang;
		this.ghiChu = ghiChu;
		this.trangThai = trangThai;
	}

	public String getMaPhieuDatHang() {
		return maPhieuDatHang;
	}

	public void setMaPhieuDatHang(String maPhieuDatHang) {
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

	public LocalDateTime getNgayDatHang() {
		return ngayDatHang;
	}

	public void setNgayDatHang(LocalDateTime ngayDatHang) {
		this.ngayDatHang = ngayDatHang;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

	public TrangThaiPhieuDatHang getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(TrangThaiPhieuDatHang trangThai) {
		this.trangThai = trangThai;
	}

	@Override
	public String toString() {
		return "PhieuDatHang [maPhieuDatHang=" + maPhieuDatHang + ", maKhachHang=" + maKhachHang + ", maNhanVien="
				+ maNhanVien + ", ngayDatHang=" + ngayDatHang + ", ghiChu=" + ghiChu + ", trangThai=" + trangThai + "]";
	}
    
    
}
