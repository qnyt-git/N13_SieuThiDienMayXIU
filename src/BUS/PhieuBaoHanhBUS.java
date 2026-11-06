package BUS;

import DAO.*;
import ENTITY.*;
import ENUM.*;
import java.util.*;
import java.time.*;

/** Tang nghiep vu trung gian giua GUI va DAO. GUI khong truy cap DAO truc tiep. */
public class PhieuBaoHanhBUS {
    private final PhieuBaoHanhDAO dao = new PhieuBaoHanhDAO();

    public List<PhieuBaoHanh> getAll() {
        return dao.getAll();
    }

    public List<ChiTietPhieuBaoHanh> getChiTietByPhieu(PhieuBaoHanh pbh) {
        return dao.getChiTietByPhieu(pbh);
    }

    public boolean insertWithDetails(PhieuBaoHanh pbh, List<ChiTietPhieuBaoHanh> chiTietList) {
        return dao.insertWithDetails(pbh, chiTietList);
    }

    public boolean insert(PhieuBaoHanh pbh) {
        return dao.insert(pbh);
    }

    public boolean update(PhieuBaoHanh pbh) {
        return dao.update(pbh);
    }

    public boolean delete(PhieuBaoHanh pbh) {
        return dao.delete(pbh);
    }

    public String taoMaMoi() {
        return dao.taoMaMoi();
    }}
