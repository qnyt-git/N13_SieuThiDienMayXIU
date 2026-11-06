package DAO;

import CONNECTDB.ConnectDB;
import ENTITY.ChiTietPhieuDatHang;
import ENTITY.KhachHang;
import ENTITY.NhanVien;
import ENTITY.PhieuDatHang;
import ENTITY.SanPham;
import ENUM.TrangThaiPhieuDatHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PhieuDatHangDAO implements IDAO<PhieuDatHang> {

    private static TrangThaiPhieuDatHang docTrangThai(String s) {
        String x = s == null ? "" : s.trim();
        for (TrangThaiPhieuDatHang t : TrangThaiPhieuDatHang.values()) {
            if (t.name().equalsIgnoreCase(x) || t.getMoTa().equalsIgnoreCase(x)) return t;
        }
        return TrangThaiPhieuDatHang.CHO_XU_LY; // Mặc định khi lập phiếu
    }

    private static LocalDateTime toLdt(Timestamp t) {
        return t == null ? null : t.toLocalDateTime();
    }

    private static Timestamp toTs(LocalDateTime t) {
        return t == null ? null : Timestamp.valueOf(t);
    }

    @Override
    public List<PhieuDatHang> getAll() {
        List<PhieuDatHang> list = new ArrayList<>();
        Map<String, KhachHang> khMap = new HashMap<>();
        for (KhachHang kh : new KhachHangDAO().getAll()) khMap.put(kh.getMaKhachHang(), kh);
        
        Map<String, NhanVien> nvMap = new HashMap<>();
        for (NhanVien nv : new NhanVienDAO().getAll()) nvMap.put(nv.getMaNhanVien(), nv);

        String sql = "SELECT * FROM PhieuDatHang";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PhieuDatHang pdh = new PhieuDatHang(
                    rs.getString("maPhieuDatHang"),
                    khMap.get(rs.getString("maKhachHang")),
                    nvMap.get(rs.getString("maNhanVien")),
                    toLdt(rs.getTimestamp("ngayDatHang")),
                    rs.getString("ghiChu"),
                    docTrangThai(rs.getString("trangThai"))
                );
                list.add(pdh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Lấy danh sách chi tiết của một phiếu đặt hàng cụ thể */
    public List<ChiTietPhieuDatHang> getChiTietByPhieu(PhieuDatHang pdh) {
        List<ChiTietPhieuDatHang> list = new ArrayList<>();
        if (pdh == null) return list;

        Map<String, SanPham> spMap = new HashMap<>();
        for (SanPham sp : new SanPhamDAO().getAll()) spMap.put(sp.getMaSanPham(), sp);

        String sql = "SELECT maPhieuDatHang, maSanPham, soLuong, donGia FROM ChiTietPhieuDatHang WHERE maPhieuDatHang = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pdh.getMaPhieuDatHang());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = spMap.get(rs.getString("maSanPham"));
                    if (sp != null) {
                        ChiTietPhieuDatHang ct = new ChiTietPhieuDatHang(
                            pdh,
                            sp,
                            rs.getInt("soLuong"),
                            rs.getDouble("donGia")
                        );
                        list.add(ct);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private boolean themChiTietDB(Connection con, PhieuDatHang pdh, List<ChiTietPhieuDatHang> chiTietList) {
        String sql = "INSERT INTO ChiTietPhieuDatHang(maPhieuDatHang, maSanPham, soLuong, donGia) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (ChiTietPhieuDatHang ct : chiTietList) {
                ps.setString(1, pdh.getMaPhieuDatHang());
                ps.setString(2, ct.getMaSanPham() != null ? ct.getMaSanPham().getMaSanPham() : null);
                ps.setInt(3, ct.getSoLuong());
                ps.setDouble(4, ct.getDonGia());
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Thêm phiếu đặt hàng kèm danh sách chi tiết trong Transaction */
    public boolean insertWithDetails(PhieuDatHang pdh, List<ChiTietPhieuDatHang> chiTietList) {
        Connection con = null;
        String sql = "INSERT INTO PhieuDatHang(maPhieuDatHang, maKhachHang, maNhanVien, ngayDatHang, ghiChu, trangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            con = ConnectDB.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, pdh.getMaPhieuDatHang());
                ps.setString(2, pdh.getMaKhachHang() != null ? pdh.getMaKhachHang().getMaKhachHang() : null);
                ps.setString(3, pdh.getMaNhanVien() != null ? pdh.getMaNhanVien().getMaNhanVien() : null);
                ps.setTimestamp(4, toTs(pdh.getNgayDatHang()));
                ps.setString(5, pdh.getGhiChu());
                ps.setString(6, pdh.getTrangThai() != null ? pdh.getTrangThai().getMoTa() : TrangThaiPhieuDatHang.CHO_XU_LY.getMoTa());
                if (ps.executeUpdate() <= 0) {
                    con.rollback();
                    return false;
                }
            }

            if (chiTietList != null && !chiTietList.isEmpty()) {
                if (!themChiTietDB(con, pdh, chiTietList)) {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try { if (con != null) con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    @Override
    public boolean insert(PhieuDatHang pdh) {
        return insertWithDetails(pdh, new ArrayList<>());
    }

    @Override
    public boolean update(PhieuDatHang pdh) {
        String sql = "UPDATE PhieuDatHang SET maKhachHang = ?, maNhanVien = ?, ngayDatHang = ?, ghiChu = ?, trangThai = ? WHERE maPhieuDatHang = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pdh.getMaKhachHang() != null ? pdh.getMaKhachHang().getMaKhachHang() : null);
            ps.setString(2, pdh.getMaNhanVien() != null ? pdh.getMaNhanVien().getMaNhanVien() : null);
            ps.setTimestamp(3, toTs(pdh.getNgayDatHang()));
            ps.setString(4, pdh.getGhiChu());
            ps.setString(5, pdh.getTrangThai() != null ? pdh.getTrangThai().getMoTa() : null);
            ps.setString(6, pdh.getMaPhieuDatHang());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(PhieuDatHang pdh) {
        Connection con = null;
        try {
            con = ConnectDB.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement("DELETE FROM ChiTietPhieuDatHang WHERE maPhieuDatHang = ?")) {
                ps.setString(1, pdh.getMaPhieuDatHang());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM PhieuDatHang WHERE maPhieuDatHang = ?")) {
                ps.setString(1, pdh.getMaPhieuDatHang());
                if (ps.executeUpdate() <= 0) {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try { if (con != null) con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    @Override
    public String taoMaMoi() {
        int max = 0;
        String sql = "SELECT MAX(TRY_CAST(SUBSTRING(maPhieuDatHang, 4, 10) AS INT)) FROM PhieuDatHang";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) max = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("PDH%02d", max + 1);
    }

    // ===================== NGHIỆP VỤ XỬ LÝ (CHUYỂN TỪ ENTITY QUA) =====================

    public double tinhTongTien(List<ChiTietPhieuDatHang> chiTietList) {
        if (chiTietList == null) return 0;
        double tong = 0;
        for (ChiTietPhieuDatHang ct : chiTietList) {
            tong += ct.getSoLuong() * ct.getDonGia();
        }
        return tong;
    }

    public void thanhToan(PhieuDatHang pdh) {
        if (pdh != null) {
            pdh.setTrangThai(TrangThaiPhieuDatHang.DA_THANH_TOAN);
            update(pdh);
        }
    }

    public void huyDat(PhieuDatHang pdh) {
        if (pdh != null) {
            pdh.setTrangThai(TrangThaiPhieuDatHang.DA_HUY);
            update(pdh);
        }
    }

    public boolean daThanhToan(PhieuDatHang pdh) {
        if (pdh == null || pdh.getTrangThai() == null) return false;
        return pdh.getTrangThai() == TrangThaiPhieuDatHang.DA_THANH_TOAN;
    }

    public ChiTietPhieuDatHang taoChiTiet(PhieuDatHang pdh, SanPham sp, int soLuong, double donGia) {
        if (pdh == null) return null;
        return new ChiTietPhieuDatHang(pdh, sp, soLuong, donGia);
    }

    public String trangThaiText(PhieuDatHang pdh) {
        return (pdh == null || pdh.getTrangThai() == null) ? "" : pdh.getTrangThai().getMoTa();
    }
}
