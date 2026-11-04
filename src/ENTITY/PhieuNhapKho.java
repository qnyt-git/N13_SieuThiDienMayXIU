package ENTITY;

import java.time.LocalDateTime;

import ENUM.TrangThaiPhieuNhapKho;

public class PhieuNhapKho {

	private String maPhieuNhapKho;
    private LocalDateTime ngayNhap;
    private NhanVien maNhanVien;
    private NhaCungCap maNCC;
    private String lyDoNhap; 			
    
    private TrangThaiPhieuNhapKho trangThai; 	// khi lập phiếu nhập kho: trạng thái được đặt mặc định "Chờ xác nhận"
    											// khi chọn xác nhận: trạng thái chuyển sang "Đã xác nhận"
    												/** nhân viên kho chỉ có thể lập phiếu**/
    												/** nhân viên quản lý mới có quyền: xác nhận + hủy phiếu **/
    											// khi chọn hủy phiêú: trạng thái chuyển sang "Phiếu đã hủy"
    public PhieuNhapKho() {}
    
		public PhieuNhapKho(String maPhieuNhapKho, LocalDateTime ngayNhap,
							NhanVien maNhanVien, NhaCungCap maNCC, String lyDoNhap,
							TrangThaiPhieuNhapKho trangThai) {
		super();
		this.maPhieuNhapKho = maPhieuNhapKho;
		this.ngayNhap = ngayNhap;
		this.maNhanVien = maNhanVien;
		this.maNCC = maNCC;
		this.lyDoNhap = lyDoNhap;
		this.trangThai = trangThai;
	}

		public String getMaPhieuNhapKho() {
			return maPhieuNhapKho;
		}

		public void setMaPhieuNhapKho(String maPhieuNhapKho) {
			this.maPhieuNhapKho = maPhieuNhapKho;
		}

		public LocalDateTime getNgayNhap() {
			return ngayNhap;
		}

		public void setNgayNhap(LocalDateTime ngayNhap) {
			this.ngayNhap = ngayNhap;
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

		public String getLyDoNhap() {
			return lyDoNhap;
		}

		public void setLyDoNhap(String lyDoNhap) {
			this.lyDoNhap = lyDoNhap;
		}

		public TrangThaiPhieuNhapKho getTrangThai() {
			return trangThai;
		}

		public void setTrangThai(TrangThaiPhieuNhapKho trangThai) {
			this.trangThai = trangThai;
		}

		@Override
		public String toString() {
			return "PhieuNhapKho [maPhieuNhapKho=" + maPhieuNhapKho + ", ngayNhap=" + ngayNhap + ", maNhanVien="
					+ maNhanVien + ", maNCC=" + maNCC + ", lyDoNhap=" + lyDoNhap + ", trangThai=" + trangThai + "]";
		}

}