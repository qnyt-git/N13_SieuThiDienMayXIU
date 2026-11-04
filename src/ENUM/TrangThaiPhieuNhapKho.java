package ENUM;

public enum TrangThaiPhieuNhapKho {
    CHO_XAC_NHAN("Chờ xác nhận"),
    DA_XAC_NHAN("Đã xác nhận"),
    DA_HUY("Phiếu đã hủy");

    private final String moTa;

    TrangThaiPhieuNhapKho(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}