package ENTITY;

import java.time.LocalDate;

import ENUM.TrangThaiPhanCa;

public class PhanCa {

	 	private String maPhanCa;
	    private NhanVien maNhanVien;
	    private CaLamViec maCa;
	    private LocalDate ngayLam;
	    private TrangThaiPhanCa trangThai;		// "đã phân ca" / "đã làm" / "Nghỉ"
	    
	    public PhanCa() {}
	    
		public PhanCa(String maPhanCa, NhanVien maNhanVien, CaLamViec maCa, LocalDate ngayLam,
				TrangThaiPhanCa trangThai) {
			super();
			this.maPhanCa = maPhanCa;
			this.maNhanVien = maNhanVien;
			this.maCa = maCa;
			this.ngayLam = ngayLam;
			this.trangThai = trangThai;
		}
		public String getMaPhanCa() {
			return maPhanCa;
		}
		public void setMaPhanCa(String maPhanCa) {
			this.maPhanCa = maPhanCa;
		}
		public NhanVien getMaNhanVien() {
			return maNhanVien;
		}
		public void setMaNhanVien(NhanVien maNhanVien) {
			this.maNhanVien = maNhanVien;
		}
		public CaLamViec getMaCa() {
			return maCa;
		}
		public void setMaCa(CaLamViec maCa) {
			this.maCa = maCa;
		}
		public LocalDate getNgayLam() {
			return ngayLam;
		}
		public void setNgayLam(LocalDate ngayLam) {
			this.ngayLam = ngayLam;
		}
		public TrangThaiPhanCa getTrangThai() {
			return trangThai;
		}
		public void setTrangThai(TrangThaiPhanCa trangThai) {
			this.trangThai = trangThai;
		}
		@Override
		public String toString() {
			return "PhanCa [maPhanCa=" + maPhanCa + ", maNhanVien=" + maNhanVien + ", maCa=" + maCa + ", ngayLam="
					+ ngayLam + ", trangThai=" + trangThai + "]";
		}
	    
	    
	    
	    
}
