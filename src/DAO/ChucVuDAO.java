package DAO;

import CONNECTDB.ConnectDB;
import ENTITY.ChucVu;
import java.sql.*;
import java.util.*;

public class ChucVuDAO {
    public List<ChucVu> getAll(){
        List<ChucVu> ds=new ArrayList<>();
        String sql="SELECT * FROM ChucVu ORDER BY maChucVu";
        try(Connection c=ConnectDB.getConnection();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){
            while(r.next()) ds.add(new ChucVu(r.getString("maChucVu"),r.getString("tenChucVu"),r.getString("viTriLamViec"),r.getString("moTaCongViec"),r.getBoolean("trangThai")));
        }catch(Exception e){e.printStackTrace();}
        return ds;
    }
    public boolean insert(ChucVu cv){
        String sql="INSERT INTO ChucVu(maChucVu,tenChucVu,viTriLamViec,moTaCongViec,trangThai) VALUES(?,?,?,?,?)";
        try(Connection c=ConnectDB.getConnection();PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,cv.getMaChucVu());p.setString(2,cv.getTenChucVu());p.setString(3,cv.getViTriLamViec());p.setString(4,cv.getMoTaCongViec());p.setBoolean(5,cv.isTrangThai());return p.executeUpdate()>0;
        }catch(Exception e){e.printStackTrace();return false;}
    }
    public String taoMaMoi(){
        int max=0;try(Connection c=ConnectDB.getConnection();PreparedStatement p=c.prepareStatement("SELECT MAX(TRY_CAST(SUBSTRING(maChucVu,3,10) AS INT)) FROM ChucVu");ResultSet r=p.executeQuery()){if(r.next())max=r.getInt(1);}catch(Exception e){e.printStackTrace();}
        return String.format("CV%02d",max+1);
    }
}
