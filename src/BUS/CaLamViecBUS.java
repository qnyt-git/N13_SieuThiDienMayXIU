package BUS;

import DAO.*;
import ENTITY.*;
import ENUM.*;
import java.util.*;
import java.time.*;

/** Tang nghiep vu trung gian giua GUI va DAO. GUI khong truy cap DAO truc tiep. */
public class CaLamViecBUS {
    private final CaLamViecDAO dao = new CaLamViecDAO();

    public List<CaLamViec> getAll() {
        return dao.getAll();
    }

    public boolean insert(CaLamViec c) {
        return dao.insert(c);
    }

    public boolean update(CaLamViec c) {
        return dao.update(c);
    }

    public boolean delete(CaLamViec c) {
        return dao.delete(c);
    }

    public String taoMaMoi() {
        return dao.taoMaMoi();
    }

    public boolean hopLe(CaLamViec c) {
        return dao.hopLe(c);
    }

    public long thoiLuongPhut(CaLamViec c) {
        return dao.thoiLuongPhut(c);
    }

    public boolean trongCa(CaLamViec c, java.time.LocalDateTime thoiDiem) {
        return dao.trongCa(c, thoiDiem);
    }

    public String gioBatDauText(CaLamViec c) {
        return dao.gioBatDauText(c);
    }

    public String gioKetThucText(CaLamViec c) {
        return dao.gioKetThucText(c);
    }

    public void datGio(CaLamViec c, java.time.LocalTime batDau, java.time.LocalTime ketThuc) {
        dao.datGio(c, batDau, ketThuc);
    }

    public boolean trongKhungGioChoPhep(CaLamViec c) {
        return dao.trongKhungGioChoPhep(c);
    }}
