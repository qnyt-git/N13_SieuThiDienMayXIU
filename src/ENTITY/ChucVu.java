package ENTITY;

public class ChucVu {
    private String maChucVu;
    private String tenChucVu;
    private String viTriLamViec;
    private String moTaCongViec;
    private boolean trangThai; // true: Đang hoạt động, false: Tạm ngưng

    public ChucVu() {}

    public ChucVu(String maChucVu, String tenChucVu, String viTriLamViec, String moTaCongViec, boolean trangThai) {
        this.maChucVu = maChucVu;
        this.tenChucVu = tenChucVu;
        this.viTriLamViec = viTriLamViec;
        this.moTaCongViec = moTaCongViec;
        this.trangThai = trangThai;
    }

    public String getMaChucVu() {
        return maChucVu;
    }

    public void setMaChucVu(String maChucVu) {
        this.maChucVu = maChucVu;
    }

    public String getTenChucVu() {
        return tenChucVu;
    }

    public void setTenChucVu(String tenChucVu) {
        this.tenChucVu = tenChucVu;
    }

    public String getViTriLamViec() {
        return viTriLamViec;
    }

    public void setViTriLamViec(String viTriLamViec) {
        this.viTriLamViec = viTriLamViec;
    }

    public String getMoTaCongViec() {
        return moTaCongViec;
    }

    public void setMoTaCongViec(String moTaCongViec) {
        this.moTaCongViec = moTaCongViec;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

	@Override
	public String toString() {
		return "ChucVu [maChucVu=" + maChucVu + ", tenChucVu=" + tenChucVu + ", viTriLamViec=" + viTriLamViec
				+ ", moTaCongViec=" + moTaCongViec + ", trangThai=" + trangThai + "]";
	}

    
}