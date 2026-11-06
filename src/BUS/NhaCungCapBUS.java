package BUS;

import DAO.*;
import ENTITY.*;
import ENUM.*;
import java.util.*;
import java.time.*;

/** Tang nghiep vu trung gian giua GUI va DAO. GUI khong truy cap DAO truc tiep. */
public class NhaCungCapBUS {
    private final NhaCungCapDAO dao = new NhaCungCapDAO();

    public List<NhaCungCap> getAll() {
        return dao.getAll();
    }

    public boolean insert(NhaCungCap ncc) {
        return dao.insert(ncc);
    }

    public boolean update(NhaCungCap ncc) {
        return dao.update(ncc);
    }

    public boolean delete(NhaCungCap ncc) {
        return dao.delete(ncc);
    }

    public String taoMaMoi() {
        return dao.taoMaMoi();
    }

    public void capNhatThongTin(NhaCungCap ncc, String ten, String sdt, String diaChi, String email) {
        dao.capNhatThongTin(ncc, ten, sdt, diaChi, email);
    }

    public void dungHopTac(NhaCungCap ncc) {
        dao.dungHopTac(ncc);
    }

    public void tiepTucHopTac(NhaCungCap ncc) {
        dao.tiepTucHopTac(ncc);
    }

    public String trangThaiText(NhaCungCap ncc) {
        return dao.trangThaiText(ncc);
    }}
