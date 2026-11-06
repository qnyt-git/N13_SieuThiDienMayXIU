package DAO;

import CONNECTDB.ConnectDB;
import ENTITY.ChiTietPhieuBaoHanh;
import ENTITY.KhachHang;
import ENTITY.NhanVien;
import ENTITY.PhieuBaoHanh;
import ENTITY.SanPham;
import ENUM.TrangThaiPhieuBaoHanh;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhieuBaoHanhDAO implements IDAO<PhieuBaoHanh> {

    private static TrangThaiPhieuBaoHanh docTrangThai(String s) {
        String x = s == null ? "" : s.trim();
        for (TrangThaiPhieuBaoHanh t : TrangThaiPhieuBaoHanh.values()) {
            if (t.name().equalsIgnoreCase(x) || t.getMoTa().equalsIgnoreCase(x)) return t;
        }
        return TrangThaiPhieuBaoHanh.TIEP_NHAN;
    }

    private static LocalDateTime toLdt(Timestamp t) {
        return t == null ? null : t.toLocalDateTime();
    }

    private static Timestamp toTs(LocalDateTime t) {
        return t == null ? null : Timestamp.valueOf(t);
    }

    @Override
    public List<PhieuBaoHanh> getAll() {
        List<PhieuBaoHanh> list = new ArrayList<>();
        Map<String, KhachHang> khMap = new HashMap<>();
        for (KhachHang kh : new KhachHangDAO().getAll()) khMap.put(kh.getMaKhachHang(), kh);
        
        Map<String, NhanVien> nvMap = new HashMap<>();
        for (NhanVien nv : new NhanVienDAO().getAll()) nvMap.put(nv.getMaNhanVien(), nv);

        String sql = "SELECT * FROM PhieuBaoHanh";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PhieuBaoHanh pbh = new PhieuBaoHanh(
                    rs.getString("maPhieuBaoHanh"),
                    khMap.get(rs.getString("maKhachHang")),
                    nvMap.get(rs.getString("maNhanVien")),
                    toLdt(rs.getTimestamp("ngayTiepNhan")),
                    toLdt(rs.getTimestamp("ngayHenTra")),
                    rs.getString("lyDoHuy"),
                    docTrangThai(rs.getString("trangThai"))
                );
                list.add(pbh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Lấy danh sách chi tiết của một phiếu bảo hành cụ thể */
    public List<ChiTietPhieuBaoHanh> getChiTietByPhieu(PhieuBaoHanh pbh) {
        List<ChiTietPhieuBaoHanh> list = new ArrayList<>();
        if (pbh == null) return list;

        Map<String, SanPham> spMap = new HashMap<>();
        for (SanPham sp : new SanPhamDAO().getAll()) spMap.put(sp.getMaSanPham(), sp);

        String sql = "SELECT maPhieuBaoHanh, maSanPham, tinhTrangSanPham, noiDungBaoHanh FROM ChiTietPhieuBaoHanh WHERE maPhieuBaoHanh = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pbh.getMaPhieubaoHanh());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = spMap.get(rs.getString("maSanPham"));
                    if (sp != null) {
                        ChiTietPhieuBaoHanh ct = new ChiTietPhieuBaoHanh(
                            pbh,
                            sp,
                            rs.getString("tinhTrangSanPham"),
                            rs.getString("noiDungBaoHanh")
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

    private boolean themChiTietDB(Connection con, PhieuBaoHanh pbh, List<ChiTietPhieuBaoHanh> chiTietList) {
        String sql = "INSERT INTO ChiTietPhieuBaoHanh(maPhieuBaoHanh, maSanPham, tinhTrangSanPham, noiDungBaoHanh) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (ChiTietPhieuBaoHanh ct : chiTietList) {
                ps.setString(1, pbh.getMaPhieubaoHanh());
                ps.setString(2, ct.getMaSanPham() != null ? ct.getMaSanPham().getMaSanPham() : null);
                ps.setString(3, ct.getTinhTrangSanPham());
                ps.setString(4, ct.getNoiDungBaoHanh());
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Thêm phiếu bảo hành kèm theo danh sách chi tiết trong một giao dịch (Transaction) */
    public boolean insertWithDetails(PhieuBaoHanh pbh, List<ChiTietPhieuBaoHanh> chiTietList) {
        Connection con = null;
        String sql = "INSERT INTO PhieuBaoHanh(maPhieuBaoHanh, maKhachHang, maNhanVien, ngayTiepNhan, ngayHenTra, lyDoHuy, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            con = ConnectDB.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, pbh.getMaPhieubaoHanh());
                ps.setString(2, pbh.getMaKhachHang() != null ? pbh.getMaKhachHang().getMaKhachHang() : null);
                ps.setString(3, pbh.getMaNhanVien() != null ? pbh.getMaNhanVien().getMaNhanVien() : null);
                ps.setTimestamp(4, toTs(pbh.getNgayTiepNhan()));
                ps.setTimestamp(5, toTs(pbh.getNgayHenTra()));
                ps.setString(6, pbh.getLyDoHuy());
                ps.setString(7, pbh.getTrangThai() != null ? pbh.getTrangThai().getMoTa() : null);
                if (ps.executeUpdate() <= 0) {
                    con.rollback();
                    return false;
                }
            }

            if (chiTietList != null && !chiTietList.isEmpty()) {
                if (!themChiTietDB(con, pbh, chiTietList)) {
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
    public boolean insert(PhieuBaoHanh pbh) {
        return insertWithDetails(pbh, new ArrayList<>());
    }

    @Override
    public boolean update(PhieuBaoHanh pbh) {
        String sql = "UPDATE PhieuBaoHanh SET maKhachHang = ?, maNhanVien = ?, ngayTiepNhan = ?, ngayHenTra = ?, lyDoHuy = ?, trangThai = ? WHERE maPhieuBaoHanh = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pbh.getMaKhachHang() != null ? pbh.getMaKhachHang().getMaKhachHang() : null);
            ps.setString(2, pbh.getMaNhanVien() != null ? pbh.getMaNhanVien().getMaNhanVien() : null);
            ps.setTimestamp(3, toTs(pbh.getNgayTiepNhan()));
            ps.setTimestamp(4, toTs(pbh.getNgayHenTra()));
            ps.setString(5, pbh.getLyDoHuy());
            ps.setString(6, pbh.getTrangThai() != null ? pbh.getTrangThai().getMoTa() : null);
            ps.setString(7, pbh.getMaPhieubaoHanh());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(PhieuBaoHanh pbh) {
        Connection con = null;
        try {
            con = ConnectDB.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement("DELETE FROM ChiTietPhieuBaoHanh WHERE maPhieuBaoHanh = ?")) {
                ps.setString(1, pbh.getMaPhieubaoHanh());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM PhieuBaoHanh WHERE maPhieuBaoHanh = ?")) {
                ps.setString(1, pbh.getMaPhieubaoHanh());
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
        String sql = "SELECT MAX(TRY_CAST(SUBSTRING(maPhieuBaoHanh, 4, 10) AS INT)) FROM PhieuBaoHanh";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) max = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("PBH%02d", max + 1);
    }
}