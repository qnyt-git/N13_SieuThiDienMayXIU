package ENTITY;

import java.time.LocalDateTime;

public class KhuyenMai {
		
	private String maKhuyenMai;					// mã khuyến mãi
    private String tenKhuyenMai;				// tên khuyến mãi		
    private LocalDateTime thoiGianBatDau;		// thời gian bắt đầu sự kiện khuyến mãi
    
    private LocalDateTime thoiGianKetThuc;		// thời gian kết thúc sự kiện khuyến mãi 
    											// khi đã qua thời gian kết thúc sự kiện khuyến mãi -> tự động disabled
    
    private double giaTriGiam;					// giá trị giảm: 1.000.000; 10%;.....
    private boolean trangThai;					// "đang diễn ra" / "đã kết thúc"
    
	public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, LocalDateTime thoiGianBatDau,
			LocalDateTime thoiGianKetThuc, double giaTriGiam, boolean trangThai) {
		super();
		this.maKhuyenMai = maKhuyenMai;
		this.tenKhuyenMai = tenKhuyenMai;
		this.thoiGianBatDau = thoiGianBatDau;
		this.thoiGianKetThuc = thoiGianKetThuc;
		this.giaTriGiam = giaTriGiam;
		this.trangThai = trangThai;
	}

	public String getMaKhuyenMai() {
		return maKhuyenMai;
	}

	public void setMaKhuyenMai(String maKhuyenMai) {
		this.maKhuyenMai = maKhuyenMai;
	}

	public String getTenKhuyenMai() {
		return tenKhuyenMai;
	}

	public void setTenKhuyenMai(String tenKhuyenMai) {
		this.tenKhuyenMai = tenKhuyenMai;
	}

	public LocalDateTime getThoiGianBatDau() {
		return thoiGianBatDau;
	}

	public void setThoiGianBatDau(LocalDateTime thoiGianBatDau) {
		this.thoiGianBatDau = thoiGianBatDau;
	}

	public LocalDateTime getThoiGianKetThuc() {
		return thoiGianKetThuc;
	}

	public void setThoiGianKetThuc(LocalDateTime thoiGianKetThuc) {
		this.thoiGianKetThuc = thoiGianKetThuc;
	}

	public double getGiaTriGiam() {
		return giaTriGiam;
	}

	public void setGiaTriGiam(double giaTriGiam) {
		this.giaTriGiam = giaTriGiam;
	}

	public boolean isTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}

	@Override
	public String toString() {
		return "KhuyenMai [maKhuyenMai=" + maKhuyenMai + ", tenKhuyenMai=" + tenKhuyenMai + ", thoiGianBatDau="
				+ thoiGianBatDau + ", thoiGianKetThuc=" + thoiGianKetThuc + ", giaTriGiam=" + giaTriGiam
				+ ", trangThai=" + trangThai + "]";
	}
    
    

}
