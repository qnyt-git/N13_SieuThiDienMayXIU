package ENTITY;

import java.time.LocalDateTime;

import ENUM.TrangThaiPhieuBaoHanh;

public class PhieuBaoHanh {
	 	private String maPhieubaoHanh;				// mã phiếu bảo 
	    private KhachHang maKhachHang;				// thông tin khách hàng gửi sản phẩm bảo hành
	    private NhanVien maNhanVien;				// thông tin nhân viên tiếp tiếp nhận bảo hành này
	    private LocalDateTime ngayTiepNhan;			// ngày lập ra phiếu bảo hành này
	    private LocalDateTime ngayHenTra;			// ngày hẹn khách đến lấy hàng (ngày lấy có thể sớm hơn dự kiến chứ không được trễ hơn)
	    private String lyDoHuy;        				// Lý do hủy phiếu (dùng khi khách từ chối phí hoặc hủy ngang)		
	    
	    private TrangThaiPhieuBaoHanh trangThai;

		public PhieuBaoHanh(String maPhieubaoHanh, KhachHang maKhachHang, NhanVien maNhanVien,
				LocalDateTime ngayTiepNhan, LocalDateTime ngayHenTra, String lyDoHuy, TrangThaiPhieuBaoHanh trangThai) {
			super();
			this.maPhieubaoHanh = maPhieubaoHanh;
			this.maKhachHang = maKhachHang;
			this.maNhanVien = maNhanVien;
			this.ngayTiepNhan = ngayTiepNhan;
			this.ngayHenTra = ngayHenTra;
			this.lyDoHuy = lyDoHuy;
			this.trangThai = trangThai;
		}

		public String getMaPhieubaoHanh() {
			return maPhieubaoHanh;
		}

		public void setMaPhieubaoHanh(String maPhieubaoHanh) {
			this.maPhieubaoHanh = maPhieubaoHanh;
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

		public LocalDateTime getNgayTiepNhan() {
			return ngayTiepNhan;
		}

		public void setNgayTiepNhan(LocalDateTime ngayTiepNhan) {
			this.ngayTiepNhan = ngayTiepNhan;
		}

		public LocalDateTime getNgayHenTra() {
			return ngayHenTra;
		}

		public void setNgayHenTra(LocalDateTime ngayHenTra) {
			this.ngayHenTra = ngayHenTra;
		}

		public String getLyDoHuy() {
			return lyDoHuy;
		}

		public void setLyDoHuy(String lyDoHuy) {
			this.lyDoHuy = lyDoHuy;
		}

		public TrangThaiPhieuBaoHanh getTrangThai() {
			return trangThai;
		}

		public void setTrangThai(TrangThaiPhieuBaoHanh trangThai) {
			this.trangThai = trangThai;
		}

		@Override
		public String toString() {
			return "PhieuBaoHanh [maPhieubaoHanh=" + maPhieubaoHanh + ", maKhachHang=" + maKhachHang + ", maNhanVien="
					+ maNhanVien + ", ngayTiepNhan=" + ngayTiepNhan + ", ngayHenTra=" + ngayHenTra + ", lyDoHuy="
					+ lyDoHuy + ", trangThai=" + trangThai + "]";
		}
	    
	 
}
