package ENUM;

public enum TrangThaiHoaDon {
    CHO_THANH_TOAN("Chờ xử lý (chờ thanh toán)"),
    DA_THANH_TOAN("Đã thanh toán");

    private final String moTa;

    TrangThaiHoaDon(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}