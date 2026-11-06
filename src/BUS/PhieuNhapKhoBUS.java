package BUS;

import DAO.*;
import ENTITY.*;
import ENUM.*;
import java.util.*;
import java.time.*;

/** Tang nghiep vu trung gian giua GUI va DAO. GUI khong truy cap DAO truc tiep. */
public class PhieuNhapKhoBUS {
    private final PhieuNhapKhoDAO dao = new PhieuNhapKhoDAO();

    public List<PhieuNhapKho> getAll() {
        return dao.getAll();
    }

    public List<ChiTietPhieuNhapKho> getChiTietByPhieu(PhieuNhapKho pnk) {
        return dao.getChiTietByPhieu(pnk);
    }

    public boolean insertWithDetails(PhieuNhapKho pnk, List<ChiTietPhieuNhapKho> chiTietList) {
        return dao.insertWithDetails(pnk, chiTietList);
    }

    public boolean insert(PhieuNhapKho pnk) {
        return dao.insert(pnk);
    }

    public boolean update(PhieuNhapKho pnk) {
        return dao.update(pnk);
    }

    public boolean delete(PhieuNhapKho pnk) {
        return dao.delete(pnk);
    }

    public String taoMaMoi() {
        return dao.taoMaMoi();
    }

    public double tinhTongTien(List<ChiTietPhieuNhapKho> chiTietList) {
        return dao.tinhTongTien(chiTietList);
    }

    public void xacNhan(PhieuNhapKho pnk, List<ChiTietPhieuNhapKho> chiTietList, SanPhamDAO sanPhamDAO) {
        dao.xacNhan(pnk, chiTietList, sanPhamDAO);
    }

    public void huyPhieu(PhieuNhapKho pnk) {
        dao.huyPhieu(pnk);
    }

    public ChiTietPhieuNhapKho taoChiTiet(PhieuNhapKho pnk, SanPham sp, int soLuong, double donGia) {
        return dao.taoChiTiet(pnk, sp, soLuong, donGia);
    }

    public String trangThaiText(PhieuNhapKho pnk) {
        return dao.trangThaiText(pnk);
    }}
