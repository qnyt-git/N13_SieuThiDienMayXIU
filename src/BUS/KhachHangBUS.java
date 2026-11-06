package BUS;

import DAO.*;
import ENTITY.*;
import ENUM.*;
import java.util.*;
import java.time.*;

/** Tang nghiep vu trung gian giua GUI va DAO. GUI khong truy cap DAO truc tiep. */
public class KhachHangBUS {
    private final KhachHangDAO dao = new KhachHangDAO();

    public List<KhachHang> getAll() {
        return dao.getAll();
    }

    public boolean insert(KhachHang kh) {
        return dao.insert(kh);
    }

    public boolean update(KhachHang kh) {
        return dao.update(kh);
    }

    public boolean delete(KhachHang kh) {
        return dao.delete(kh);
    }

    public String taoMaMoi() {
        return dao.taoMaMoi();
    }

    public void capNhatThongTin(KhachHang kh) {
        dao.capNhatThongTin(kh);
    }

    public void capNhatDiemTichLuy(KhachHang kh, int diem) {
        dao.capNhatDiemTichLuy(kh, diem);
    }

    public void xemLichSuMuaHang(KhachHang kh) {
        dao.xemLichSuMuaHang(kh);
    }

    public void congDiemTuHoaDon(KhachHang kh, double thanhTien) {
        dao.congDiemTuHoaDon(kh, thanhTien);
    }}
