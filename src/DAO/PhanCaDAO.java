	package DAO;
	
	import CONNECTDB.ConnectDB;
	import ENTITY.CaLamViec;
	import ENTITY.NhanVien;
	import ENTITY.PhanCa;
	import ENUM.TrangThaiPhanCa;
	
	import java.sql.Connection;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.Date;
	import java.time.LocalDate;
	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	
	/**
	 * DAO của PhanCa.
	 * Bao gồm các thao tác CSDL (insert, update, delete, getAll, taoMaMoi)
	 * và toàn bộ các phương thức xử lý nghiệp vụ, kiểm tra trùng lặp lịch phân ca được chuyển từ Entity qua.
	 */
	public class PhanCaDAO implements IDAO<PhanCa> {
	
	    private static TrangThaiPhanCa docTrangThai(String s) {
	        String x = s == null ? "" : s.trim();
	        for (TrangThaiPhanCa t : TrangThaiPhanCa.values()) {
	            if (t.name().equalsIgnoreCase(x) || t.getMoTa().equalsIgnoreCase(x)) return t;
	        }
	        return TrangThaiPhanCa.DA_PHAN_CA;
	    }
	
	    @Override
	    public List<PhanCa> getAll() {
	        List<PhanCa> list = new ArrayList<>();
	        
	        // Map tra cứu NhanVien và CaLamViec để tối ưu kết nối
	        Map<String, NhanVien> nvMap = new HashMap<>();
	        for (NhanVien nv : new NhanVienDAO().getAll()) nvMap.put(nv.getMaNhanVien(), nv);
	        
	        Map<String, CaLamViec> caMap = new HashMap<>();
	        for (CaLamViec ca : new CaLamViecDAO().getAll()) caMap.put(ca.getMaCa(), ca);
	
	        String sql = "SELECT * FROM PhanCa";
	        try (Connection con = ConnectDB.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                PhanCa pc = new PhanCa();
	                pc.setMaPhanCa(rs.getString("maPhanCa"));
	                pc.setMaNhanVien(nvMap.get(rs.getString("maNhanVien")));
	                pc.setMaCa(caMap.get(rs.getString("maCa")));
	                
	                Date nl = rs.getDate("ngayLam");
	                if (nl != null) pc.setNgayLam(nl.toLocalDate());
	                
	                pc.setTrangThai(docTrangThai(rs.getString("trangThai")));
	                list.add(pc);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return list;
	    }
	
	    @Override
	    public boolean insert(PhanCa pc) {
	        String sql = "INSERT INTO PhanCa(maPhanCa, maNhanVien, maCa, ngayLam, trangThai) VALUES (?, ?, ?, ?, ?)";
	        try (Connection con = ConnectDB.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, pc.getMaPhanCa());
	            ps.setString(2, pc.getMaNhanVien() != null ? pc.getMaNhanVien().getMaNhanVien() : null);
	            ps.setString(3, pc.getMaCa() != null ? pc.getMaCa().getMaCa() : null);
	            ps.setDate(4, pc.getNgayLam() != null ? Date.valueOf(pc.getNgayLam()) : null);
	            ps.setString(5, pc.getTrangThai() != null ? pc.getTrangThai().getMoTa() : null);
	            return ps.executeUpdate() > 0;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
	
	    @Override
	    public boolean update(PhanCa pc) {
	        String sql = "UPDATE PhanCa SET maNhanVien = ?, maCa = ?, ngayLam = ?, trangThai = ? WHERE maPhanCa = ?";
	        try (Connection con = ConnectDB.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, pc.getMaNhanVien() != null ? pc.getMaNhanVien().getMaNhanVien() : null);
	            ps.setString(2, pc.getMaCa() != null ? pc.getMaCa().getMaCa() : null);
	            ps.setDate(3, pc.getNgayLam() != null ? Date.valueOf(pc.getNgayLam()) : null);
	            ps.setString(4, pc.getTrangThai() != null ? pc.getTrangThai().getMoTa() : null);
	            ps.setString(5, pc.getMaPhanCa());
	            return ps.executeUpdate() > 0;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
	
	    @Override
	    public boolean delete(PhanCa pc) {
	        String sql = "DELETE FROM PhanCa WHERE maPhanCa = ?";
	        try (Connection con = ConnectDB.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, pc.getMaPhanCa());
	            return ps.executeUpdate() > 0;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
	
	    @Override
	    public String taoMaMoi() {
	        int max = 0;
	        String sql = "SELECT MAX(TRY_CAST(SUBSTRING(maPhanCa, 3, 10) AS INT)) FROM PhanCa";
	        try (Connection con = ConnectDB.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) max = rs.getInt(1);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return String.format("PC%02d", max + 1);
	    }
	
	    // ===================== CÁC PHƯƠNG THỨC XỬ LÝ (CHUYỂN TỪ ENTITY QUA) =====================
	
	    public void danhDauDaLam(PhanCa pc) { 
	        if (pc != null) pc.setTrangThai(TrangThaiPhanCa.DA_LAM); 
	    }
	
	    public void danhDauNghi(PhanCa pc) { 
	        if (pc != null) pc.setTrangThai(TrangThaiPhanCa.NGHI); 
	    }
	
	    public boolean laNgayHomNay(PhanCa pc) {
	        return pc != null && pc.getNgayLam() != null && pc.getNgayLam().equals(LocalDate.now());
	    }
	
	    /** Kiểm tra tổ hợp (NV + Ngày + Ca) trùng */
	    public boolean trungVoi(PhanCa pc, PhanCa other) {
	        if (pc == null || other == null || pc.getMaNhanVien() == null || other.getMaNhanVien() == null 
	            || pc.getMaCa() == null || other.getMaCa() == null || pc.getNgayLam() == null || other.getNgayLam() == null) {
	            return false;
	        }
	        return pc.getMaNhanVien().getMaNhanVien().equals(other.getMaNhanVien().getMaNhanVien())
	            && pc.getNgayLam().equals(other.getNgayLam())
	            && pc.getMaCa().getMaCa().equals(other.getMaCa().getMaCa());
	    }
	
	    public boolean daNghi(PhanCa pc) { 
	        return pc != null && pc.getTrangThai() == TrangThaiPhanCa.NGHI; 
	    }
	
	    /** Ngày làm nằm trong tuần bắt đầu từ thuHai (thứ Hai). */
	    public boolean thuocTuan(PhanCa pc, LocalDate thuHai) {
	        return pc != null && pc.getNgayLam() != null && thuHai != null 
	            && !pc.getNgayLam().isBefore(thuHai) && !pc.getNgayLam().isAfter(thuHai.plusDays(6));
	    }
	}