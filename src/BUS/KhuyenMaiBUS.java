package BUS;

import DAO.*;
import ENTITY.*;
import ENUM.*;
import java.util.*;
import java.time.*;

/** Tang nghiep vu trung gian giua GUI va DAO. GUI khong truy cap DAO truc tiep. */
public class KhuyenMaiBUS {
    private final KhuyenMaiDAO dao = new KhuyenMaiDAO();

    public List<KhuyenMai> getAll() {
        return dao.getAll();
    }

    public boolean insert(KhuyenMai km) {
        return dao.insert(km);
    }

    public boolean update(KhuyenMai km) {
        return dao.update(km);
    }

    public boolean delete(KhuyenMai km) {
        return dao.delete(km);
    }

    public String taoMaMoi() {
        return dao.taoMaMoi();
    }

    public boolean conHieuLuc(KhuyenMai km) {
        return dao.conHieuLuc(km);
    }

    public double tinhTienGiam(KhuyenMai km, double tongTien) {
        return dao.tinhTienGiam(km, tongTien);
    }

    public double apDung(KhuyenMai km, double tongTien) {
        return dao.apDung(km, tongTien);
    }

    public void giaHan(KhuyenMai km, LocalDateTime ngayKetThucMoi) {
        dao.giaHan(km, ngayKetThucMoi);
    }

    public String trangThaiText(KhuyenMai km) {
        return dao.trangThaiText(km);
    }

    public void datThoiGian(KhuyenMai km, LocalDate batDau, LocalDate ketThuc) {
        dao.datThoiGian(km, batDau, ketThuc);
    }

    public boolean hopLe(KhuyenMai km) {
        return dao.hopLe(km);
    }

    public double tinhTienGiamDaApDung(KhuyenMai km, double tongTien) {
        return dao.tinhTienGiamDaApDung(km, tongTien);
    }}
