package ENUM;

public enum TrangThaiPhieuDatHang {
    CHO_XU_LY("Chờ xử lý (chờ thanh toán)"),
    DA_THANH_TOAN("Đã thanh toán"),
    DA_HUY("Đã hủy");

    private final String moTa;

    TrangThaiPhieuDatHang(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}