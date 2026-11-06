package BUS;

import DAO.*;
import ENTITY.*;
import ENUM.*;
import java.util.*;
import java.time.*;

/** Tang nghiep vu trung gian giua GUI va DAO. GUI khong truy cap DAO truc tiep. */
public class PhieuDatHangBUS {
    private final PhieuDatHangDAO dao = new PhieuDatHangDAO();

    public List<PhieuDatHang> getAll() {
        return dao.getAll();
    }

    public List<ChiTietPhieuDatHang> getChiTietByPhieu(PhieuDatHang pdh) {
        return dao.getChiTietByPhieu(pdh);
    }

    public boolean insertWithDetails(PhieuDatHang pdh, List<ChiTietPhieuDatHang> chiTietList) {
        return dao.insertWithDetails(pdh, chiTietList);
    }

    public boolean insert(PhieuDatHang pdh) {
        return dao.insert(pdh);
    }

    public boolean update(PhieuDatHang pdh) {
        return dao.update(pdh);
    }

    public boolean delete(PhieuDatHang pdh) {
        return dao.delete(pdh);
    }

    public String taoMaMoi() {
        return dao.taoMaMoi();
    }

    public double tinhTongTien(List<ChiTietPhieuDatHang> chiTietList) {
        return dao.tinhTongTien(chiTietList);
    }

    public void thanhToan(PhieuDatHang pdh) {
        dao.thanhToan(pdh);
    }

    public void huyDat(PhieuDatHang pdh) {
        dao.huyDat(pdh);
    }

    public boolean daThanhToan(PhieuDatHang pdh) {
        return dao.daThanhToan(pdh);
    }

    public ChiTietPhieuDatHang taoChiTiet(PhieuDatHang pdh, SanPham sp, int soLuong, double donGia) {
        return dao.taoChiTiet(pdh, sp, soLuong, donGia);
    }

    public String trangThaiText(PhieuDatHang pdh) {
        return dao.trangThaiText(pdh);
    }}
