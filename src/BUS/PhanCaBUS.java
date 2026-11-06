package BUS;

import DAO.*;
import ENTITY.*;
import ENUM.*;
import java.util.*;
import java.time.*;

/** Tang nghiep vu trung gian giua GUI va DAO. GUI khong truy cap DAO truc tiep. */
public class PhanCaBUS {
    private final PhanCaDAO dao = new PhanCaDAO();

    public List<PhanCa> getAll() {
        return dao.getAll();
    }

    public boolean insert(PhanCa pc) {
        return dao.insert(pc);
    }

    public boolean update(PhanCa pc) {
        return dao.update(pc);
    }

    public boolean delete(PhanCa pc) {
        return dao.delete(pc);
    }

    public String taoMaMoi() {
        return dao.taoMaMoi();
    }

    public void danhDauDaLam(PhanCa pc) {
        dao.danhDauDaLam(pc);
    }

    public void danhDauNghi(PhanCa pc) {
        dao.danhDauNghi(pc);
    }

    public boolean laNgayHomNay(PhanCa pc) {
        return dao.laNgayHomNay(pc);
    }

    public boolean trungVoi(PhanCa pc, PhanCa other) {
        return dao.trungVoi(pc, other);
    }

    public boolean daNghi(PhanCa pc) {
        return dao.daNghi(pc);
    }

    public boolean thuocTuan(PhanCa pc, LocalDate thuHai) {
        return dao.thuocTuan(pc, thuHai);
    }}
