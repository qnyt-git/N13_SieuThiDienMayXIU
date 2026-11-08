package BUS;

import DAO.ChucVuDAO;
import ENTITY.ChucVu;
import java.util.List;

public class ChucVuBUS {
    private final ChucVuDAO dao=new ChucVuDAO();
    public List<ChucVu> getAll(){return dao.getAll();}
    public boolean them(ChucVu cv){
        if(cv==null||cv.getTenChucVu()==null||cv.getTenChucVu().trim().isEmpty()||cv.getViTriLamViec()==null||cv.getViTriLamViec().trim().isEmpty())return false;
        return dao.insert(cv);
    }
    public String taoMaMoi(){return dao.taoMaMoi();}
}
