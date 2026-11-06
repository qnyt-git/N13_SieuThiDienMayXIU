package BUS;

import DAO.*;
import ENTITY.*;
import ENUM.*;
import java.util.*;
import java.time.*;

/** Tang nghiep vu trung gian giua GUI va DAO. GUI khong truy cap DAO truc tiep. */
public class SanPhamBUS {
    private final SanPhamDAO dao = new SanPhamDAO();

    public List<SanPham> getAll() {
        return dao.getAll();
    }

    public boolean insert(SanPham sp) {
        return dao.insert(sp);
    }

    public boolean update(SanPham sp) {
        return dao.update(sp);
    }

    public boolean delete(SanPham sp) {
        return dao.delete(sp);
    }

    public String taoMaMoi() {
        return dao.taoMaMoi();
    }

    public boolean conHang(SanPham sp) {
        return dao.conHang(sp);
    }

    public boolean nhapKho(SanPham sp, int soLuong) {
        return dao.nhapKho(sp, soLuong);
    }

    public boolean xuatKho(SanPham sp, int soLuong) {
        return dao.xuatKho(sp, soLuong);
    }

    public boolean coTheBan(SanPham sp) {
        return dao.coTheBan(sp);
    }

    public int baoHanhThang(SanPham sp) {
        return dao.baoHanhThang(sp);
    }

    public void datBaoHanhThang(SanPham sp, int thang) {
        dao.datBaoHanhThang(sp, thang);
    }

    public boolean coTheBanSoLuong(SanPham sp, int sl) {
        return dao.coTheBanSoLuong(sp, sl);
    }

    public boolean sapHetHang(SanPham sp, int nguong) {
        return dao.sapHetHang(sp, nguong);
    }

    public boolean conBaoHanh(SanPham sp, LocalDateTime ngayMua) {
        return dao.conBaoHanh(sp, ngayMua);
    }}
