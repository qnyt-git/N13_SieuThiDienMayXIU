package ENTITY;

import java.time.LocalDateTime;

public class CaLamViec {
	private String maCa;
    private String tenCa;
    private LocalDateTime gioBatDau;
    private LocalDateTime gioKetThuc;
    
    public CaLamViec() {}
    
	public CaLamViec(String maCa, String tenCa, LocalDateTime gioBatDau, LocalDateTime gioKetThuc) {
		super();
		this.maCa = maCa;
		this.tenCa = tenCa;
		this.gioBatDau = gioBatDau;
		this.gioKetThuc = gioKetThuc;
	}
	public String getMaCa() {
		return maCa;
	}
	public void setMaCa(String maCa) {
		this.maCa = maCa;
	}
	public String getTenCa() {
		return tenCa;
	}
	public void setTenCa(String tenCa) {
		this.tenCa = tenCa;
	}
	public LocalDateTime getGioBatDau() {
		return gioBatDau;
	}
	public void setGioBatDau(LocalDateTime gioBatDau) {
		this.gioBatDau = gioBatDau;
	}
	public LocalDateTime getGioKetThuc() {
		return gioKetThuc;
	}
	public void setGioKetThuc(LocalDateTime gioKetThuc) {
		this.gioKetThuc = gioKetThuc;
	}
	@Override
	public String toString() {
		return "CaLamViec [maCa=" + maCa + ", tenCa=" + tenCa + ", gioBatDau=" + gioBatDau + ", gioKetThuc="
				+ gioKetThuc + "]";
	}
    
    
}
