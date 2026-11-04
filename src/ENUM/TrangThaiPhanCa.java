package ENUM;

public enum TrangThaiPhanCa {
    DA_PHAN_CA("Đã phân ca"),
    DA_LAM("Đã làm"),
    NGHI("Nghỉ");

    private final String moTa;

    TrangThaiPhanCa(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}