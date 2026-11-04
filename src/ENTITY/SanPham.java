package ENTITY;

import ENUM.TrangThaiSanPham;

public class SanPham {

	private String maSanPham;
	private String tenSanPham;
	private NhaCungCap maNCC;
	private double giaBan;
	private int soLuongTon;
	private String thoiHanBaoHanh; // 1 năm/12 tháng; 2 năm......
	private String moTaSanPham;
	private TrangThaiSanPham trangThai; // đang bán/hết hàng/tạm ngưng
	public SanPham(String maSanPham, String tenSanPham, NhaCungCap maNCC, double giaBan, int soLuongTon,
			String thoiHanBaoHanh, String moTaSanPham, TrangThaiSanPham trangThai) {
		super();
		this.maSanPham = maSanPham;
		this.tenSanPham = tenSanPham;
		this.maNCC = maNCC;
		this.giaBan = giaBan;
		this.soLuongTon = soLuongTon;
		this.thoiHanBaoHanh = thoiHanBaoHanh;
		this.moTaSanPham = moTaSanPham;
		this.trangThai = trangThai;
	}
	public String getMaSanPham() {
		return maSanPham;
	}
	public void setMaSanPham(String maSanPham) {
		this.maSanPham = maSanPham;
	}
	public String getTenSanPham() {
		return tenSanPham;
	}
	public void setTenSanPham(String tenSanPham) {
		this.tenSanPham = tenSanPham;
	}
	public NhaCungCap getMaNCC() {
		return maNCC;
	}
	public void setMaNCC(NhaCungCap maNCC) {
		this.maNCC = maNCC;
	}
	public double getGiaBan() {
		return giaBan;
	}
	public void setGiaBan(double giaBan) {
		this.giaBan = giaBan;
	}
	public int getSoLuongTon() {
		return soLuongTon;
	}
	public void setSoLuongTon(int soLuongTon) {
		this.soLuongTon = soLuongTon;
	}
	public String getThoiHanBaoHanh() {
		return thoiHanBaoHanh;
	}
	public void setThoiHanBaoHanh(String thoiHanBaoHanh) {
		this.thoiHanBaoHanh = thoiHanBaoHanh;
	}
	public String getMoTaSanPham() {
		return moTaSanPham;
	}
	public void setMoTaSanPham(String moTaSanPham) {
		this.moTaSanPham = moTaSanPham;
	}
	public TrangThaiSanPham getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(TrangThaiSanPham trangThai) {
		this.trangThai = trangThai;
	}
	@Override
	public String toString() {
		return "SanPham [maSanPham=" + maSanPham + ", tenSanPham=" + tenSanPham + ", maNCC=" + maNCC + ", giaBan="
				+ giaBan + ", soLuongTon=" + soLuongTon + ", thoiHanBaoHanh=" + thoiHanBaoHanh + ", moTaSanPham="
				+ moTaSanPham + ", trangThai=" + trangThai + "]";
	}
	
	
	
}
