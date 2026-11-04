package ENUM;

public enum TrangThaiSanPham {
	SAN_PHAM_MOI("Sản phẩm mới"), //đang bán sản phẩm mới
    DANG_BAN("Đang bán"),
    HET_HANG("Hết hàng"),
    TAM_NGUNG("Tạm ngưng kinh doanh");

    private final String moTa;

    TrangThaiSanPham(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}