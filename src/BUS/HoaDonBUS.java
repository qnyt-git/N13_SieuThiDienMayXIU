package BUS;

import DAO.*;
import ENTITY.*;
import ENUM.*;
import java.util.*;
import java.time.*;

/** Tang nghiep vu trung gian giua GUI va DAO. GUI khong truy cap DAO truc tiep. */
public class HoaDonBUS {
    private final HoaDonDAO dao = new HoaDonDAO();

    public List<HoaDon> getAll() {
        return dao.getAll();
    }

    public List<ChiTietHoaDon> getChiTietByHoaDon(HoaDon hd) {
        return dao.getChiTietByHoaDon(hd);
    }

    public boolean insertWithDetails(HoaDon h, List<ChiTietHoaDon> chiTietList) {
        return dao.insertWithDetails(h, chiTietList);
    }

    public boolean insert(HoaDon h) {
        return dao.insert(h);
    }

    public boolean update(HoaDon h) {
        return dao.update(h);
    }

    public boolean delete(HoaDon h) {
        return dao.delete(h);
    }

    public String taoMaMoi() {
        return dao.taoMaMoi();
    }}
