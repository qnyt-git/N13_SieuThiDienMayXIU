package BUS;

import DAO.*;
import ENTITY.*;
import ENUM.*;
import java.util.*;
import java.time.*;

/** Tang nghiep vu trung gian giua GUI va DAO. GUI khong truy cap DAO truc tiep. */
public class NhanVienBUS {
    private final NhanVienDAO dao = new NhanVienDAO();

    public List<NhanVien> getAll() {
        return dao.getAll();
    }

    public boolean insert(NhanVien nv) {
        return dao.insert(nv);
    }

    public boolean update(NhanVien nv) {
        return dao.update(nv);
    }

    public boolean delete(NhanVien nv) {
        return dao.delete(nv);
    }

    public String taoMaMoi() {
        return dao.taoMaMoi();
    }

    public double tinhLuong(NhanVien nv) {
        return dao.tinhLuong(nv);
    }

    public boolean conLamViec(NhanVien nv) {
        return dao.conLamViec(nv);
    }

    public void nghiViec(NhanVien nv) {
        dao.nghiViec(nv);
    }

    public void capNhatThongTin(NhanVien nv, String hoTen, String sdt, LocalDate ngaySinh) {
        dao.capNhatThongTin(nv, hoTen, sdt, ngaySinh);
    }

    public void dangKyCaLam(NhanVien nv) {
        dao.dangKyCaLam(nv);
    }

    public int tuoi(NhanVien nv) {
        return dao.tuoi(nv);
    }

    public boolean hopLeNgaySinh(NhanVien nv) {
        return dao.hopLeNgaySinh(nv);
    }

    public String trangThaiText(NhanVien nv) {
        return dao.trangThaiText(nv);
    }

    public double heSoLuongDouble(NhanVien nv) {
        return dao.heSoLuongDouble(nv);
    }
    /** Kiem tra thong tin nhan vien truoc khi ghi CSDL. */
    public String kiemTraHopLe(NhanVien nv) {
        if (nv == null) return "Thông tin nhân viên không hợp lệ.";
        if (nv.getHoTenNhanVien() == null || nv.getHoTenNhanVien().trim().isEmpty()) return "Vui lòng nhập họ tên.";
        if (nv.getSoDienThoai() == null || !nv.getSoDienThoai().matches("0[0-9]{9}")) return "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0.";
        if (nv.getNgaySinh() == null || nv.getNgaySinh().isAfter(LocalDate.now()) || nv.getNgaySinh().plusYears(18).isAfter(LocalDate.now())) return "Nhân viên phải đủ 18 tuổi và ngày sinh hợp lệ.";
        if (nv.getHeSoLuong() <= 0 || nv.getLuongCoBan() <= 0) return "Hệ số lương và lương cơ bản phải lớn hơn 0.";
        return null;
    }
    public boolean luu(NhanVien nv, boolean sua) {
        String loi = kiemTraHopLe(nv);
        if (loi != null) throw new IllegalArgumentException(loi);
        return sua ? update(nv) : insert(nv);
    }
    public List<NhanVien> timKiem(String tuKhoa) {
        List<NhanVien> ketQua = new ArrayList<>();
        String q = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        for (NhanVien nv : getAll()) {
            if (q.isEmpty() || (nv.getMaNhanVien()!=null && nv.getMaNhanVien().toLowerCase().contains(q)) || (nv.getHoTenNhanVien()!=null && nv.getHoTenNhanVien().toLowerCase().contains(q)) || (nv.getSoDienThoai()!=null && nv.getSoDienThoai().contains(q))) ketQua.add(nv);
        }
        return ketQua;
    }
}
