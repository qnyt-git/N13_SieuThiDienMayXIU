package BUS;

import DAO.*;
import ENTITY.*;
import ENUM.*;
import java.util.*;
import java.time.*;

/** Tang nghiep vu trung gian giua GUI va DAO. GUI khong truy cap DAO truc tiep. */
public class TaiKhoanNhanVienBUS {
    private final TaiKhoanNhanVienDAO dao = new TaiKhoanNhanVienDAO();

    public String[] kiemTraDangNhap(String tenDangNhap, String matKhau) {
        return dao.kiemTraDangNhap(tenDangNhap, matKhau);
    }

    public List<TaiKhoanNhanVien> getAll() {
        return dao.getAll();
    }

    public boolean insert(TaiKhoanNhanVien tk) {
        return dao.insert(tk);
    }

    public boolean update(TaiKhoanNhanVien tk) {
        return dao.update(tk);
    }

    public boolean delete(TaiKhoanNhanVien tk) {
        return dao.delete(tk);
    }

    public String taoMaMoi() {
        return dao.taoMaMoi();
    }

    public boolean doiMatKhau(TaiKhoanNhanVien tk, String matKhauCu, String matKhauMoi) {
        return dao.doiMatKhau(tk, matKhauCu, matKhauMoi);
    }

    public void doiTrangThai(TaiKhoanNhanVien tk) {
        dao.doiTrangThai(tk);
    }

    public boolean laQuanLy(TaiKhoanNhanVien tk) {
        return dao.laQuanLy(tk);
    }}
