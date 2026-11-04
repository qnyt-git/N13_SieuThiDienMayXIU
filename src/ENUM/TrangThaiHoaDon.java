package ENUM;

public enum TrangThaiHoaDon {
    CHO_THANH_TOAN("Chờ xử lý (chờ thanh toán)"),
    DA_THANH_TOAN("Đã thanh toán"),
    DA_HUY("Đã hủy hóa đơn");

    private final String moTa;

    TrangThaiHoaDon(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}