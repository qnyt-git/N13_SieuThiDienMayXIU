package ENUM;

public enum PhuongThucThanhToan {
    TIEN_MAT("Tiền mặt"),
    CHUYEN_KHOAN("Chuyển khoản ngân hàng"),
    THE_NGAN_HANG("Thẻ ngân hàng (ATM/Visa/Master)"),
    VI_DIEN_TU("Ví điện tử (MoMo/ShopeePay)");

    private final String moTa;

    PhuongThucThanhToan(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}