package DAO;

import CONNECTDB.ConnectDB;
import ENTITY.ChiTietPhieuNhapKho;
import ENTITY.NhaCungCap;
import ENTITY.NhanVien;
import ENTITY.PhieuNhapKho;
import ENTITY.SanPham;
import ENUM.TrangThaiPhieuNhapKho;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PhieuNhapKhoDAO implements IDAO<PhieuNhapKho> {

    private String lastError = "";

    public String getLastError() {
        return lastError;
    }

    private static TrangThaiPhieuNhapKho docTrangThai(String s) {
        String x = s == null ? "" : s.trim();
        for (TrangThaiPhieuNhapKho t : TrangThaiPhieuNhapKho.values()) {
            if (t.name().equalsIgnoreCase(x) || t.getMoTa().equalsIgnoreCase(x)) return t;
        }
        return TrangThaiPhieuNhapKho.CHO_XAC_NHAN; // Mặc định khi lập phiếu
    }

    private static LocalDateTime toLdt(Timestamp t) {
        return t == null ? null : t.toLocalDateTime();
    }

    private static Timestamp toTs(LocalDateTime t) {
        return t == null ? null : Timestamp.valueOf(t);
    }

    @Override
    public List<PhieuNhapKho> getAll() {
        List<PhieuNhapKho> list = new ArrayList<>();
        Map<String, NhanVien> nvMap = new HashMap<>();
        for (NhanVien nv : new NhanVienDAO().getAll()) nvMap.put(nv.getMaNhanVien(), nv);
        
        Map<String, NhaCungCap> nccMap = new HashMap<>();
        for (NhaCungCap ncc : new NhaCungCapDAO().getAll()) nccMap.put(ncc.getMaNCC(), ncc);

        String sql = "SELECT * FROM PhieuNhapKho";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PhieuNhapKho pnk = new PhieuNhapKho(
                    rs.getString("maPhieuNhapKho"),
                    toLdt(rs.getTimestamp("ngayNhap")),
                    nvMap.get(rs.getString("maNhanVien")),
                    nccMap.get(rs.getString("maNCC")),
                    rs.getString("lyDoNhap"),
                    docTrangThai(rs.getString("trangThai"))
                );
                list.add(pnk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Lấy danh sách chi tiết của một phiếu nhập kho cụ thể */
    public List<ChiTietPhieuNhapKho> getChiTietByPhieu(PhieuNhapKho pnk) {
        List<ChiTietPhieuNhapKho> list = new ArrayList<>();
        if (pnk == null) return list;

        Map<String, SanPham> spMap = new HashMap<>();
        for (SanPham sp : new SanPhamDAO().getAll()) spMap.put(sp.getMaSanPham(), sp);

        String sql = "SELECT maPhieuNhapKho, maSanPham, soLuong, donGia FROM ChiTietPhieuNhapKho WHERE maPhieuNhapKho = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pnk.getMaPhieuNhapKho());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = spMap.get(rs.getString("maSanPham"));
                    if (sp != null) {
                        ChiTietPhieuNhapKho ct = new ChiTietPhieuNhapKho(
                            pnk,
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

    private boolean themChiTietDB(Connection con, PhieuNhapKho pnk, List<ChiTietPhieuNhapKho> chiTietList) {
        String sql = "INSERT INTO ChiTietPhieuNhapKho(maPhieuNhapKho, maSanPham, soLuong, donGia) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (ChiTietPhieuNhapKho ct : chiTietList) {
                ps.setString(1, pnk.getMaPhieuNhapKho());
                ps.setString(2, ct.getMaSanPham() != null ? ct.getMaSanPham().getMaSanPham() : null);
                ps.setInt(3, ct.getSoLuong());
                ps.setDouble(4, ct.getDonGia());
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (Exception e) {
            lastError = e.getMessage() == null ? e.toString() : e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    /** Thêm phiếu nhập kho kèm danh sách chi tiết trong Transaction */
    public boolean insertWithDetails(PhieuNhapKho pnk, List<ChiTietPhieuNhapKho> chiTietList) {
        lastError = "";
        Connection con = null;
        String sql = "INSERT INTO PhieuNhapKho(maPhieuNhapKho, ngayNhap, maNhanVien, maNCC, lyDoNhap, trangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            con = ConnectDB.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, pnk.getMaPhieuNhapKho());
                ps.setTimestamp(2, toTs(pnk.getNgayNhap()));
                ps.setString(3, pnk.getMaNhanVien() != null ? pnk.getMaNhanVien().getMaNhanVien() : null);
                ps.setString(4, pnk.getMaNCC() != null ? pnk.getMaNCC().getMaNCC() : null);
                ps.setString(5, pnk.getLyDoNhap());
                ps.setString(6, pnk.getTrangThai() != null ? pnk.getTrangThai().getMoTa() : TrangThaiPhieuNhapKho.CHO_XAC_NHAN.getMoTa());
                if (ps.executeUpdate() <= 0) {
                    con.rollback();
                    return false;
                }
            }

            if (chiTietList != null && !chiTietList.isEmpty()) {
                if (!themChiTietDB(con, pnk, chiTietList)) {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;
        } catch (Exception e) {
            lastError = e.getMessage() == null ? e.toString() : e.getMessage();
            e.printStackTrace();
            try { if (con != null) con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    @Override
    public boolean insert(PhieuNhapKho pnk) {
        return insertWithDetails(pnk, new ArrayList<>());
    }

    /** Cap nhat thong tin phieu va thay the toan bo chi tiet trong mot transaction. */
    public boolean updateWithDetails(PhieuNhapKho pnk, List<ChiTietPhieuNhapKho> chiTietList) {
        lastError = "";
        Connection con = null;
        String sql = "UPDATE PhieuNhapKho SET ngayNhap=?, maNhanVien=?, maNCC=?, lyDoNhap=?, trangThai=? WHERE maPhieuNhapKho=?";
        try {
            con = ConnectDB.getConnection();
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setTimestamp(1, toTs(pnk.getNgayNhap()));
                ps.setString(2, pnk.getMaNhanVien().getMaNhanVien());
                ps.setString(3, pnk.getMaNCC().getMaNCC());
                ps.setString(4, pnk.getLyDoNhap());
                ps.setString(5, pnk.getTrangThai().getMoTa());
                ps.setString(6, pnk.getMaPhieuNhapKho());
                if (ps.executeUpdate() == 0) { con.rollback(); return false; }
            }
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM ChiTietPhieuNhapKho WHERE maPhieuNhapKho=?")) {
                ps.setString(1, pnk.getMaPhieuNhapKho());
                ps.executeUpdate();
            }
            if (chiTietList != null && !chiTietList.isEmpty() && !themChiTietDB(con, pnk, chiTietList)) {
                con.rollback(); return false;
            }
            con.commit();
            return true;
        } catch (Exception e) {
            lastError = e.getMessage() == null ? e.toString() : e.getMessage();
            try { if (con != null) con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    @Override
    public boolean update(PhieuNhapKho pnk) {
        String sql = "UPDATE PhieuNhapKho SET ngayNhap = ?, maNhanVien = ?, maNCC = ?, lyDoNhap = ?, trangThai = ? WHERE maPhieuNhapKho = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, toTs(pnk.getNgayNhap()));
            ps.setString(2, pnk.getMaNhanVien() != null ? pnk.getMaNhanVien().getMaNhanVien() : null);
            ps.setString(3, pnk.getMaNCC() != null ? pnk.getMaNCC().getMaNCC() : null);
            ps.setString(4, pnk.getLyDoNhap());
            ps.setString(5, pnk.getTrangThai() != null ? pnk.getTrangThai().getMoTa() : null);
            ps.setString(6, pnk.getMaPhieuNhapKho());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(PhieuNhapKho pnk) {
        Connection con = null;
        try {
            con = ConnectDB.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement("DELETE FROM ChiTietPhieuNhapKho WHERE maPhieuNhapKho = ?")) {
                ps.setString(1, pnk.getMaPhieuNhapKho());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM PhieuNhapKho WHERE maPhieuNhapKho = ?")) {
                ps.setString(1, pnk.getMaPhieuNhapKho());
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
        String sql = "SELECT MAX(TRY_CAST(SUBSTRING(maPhieuNhapKho, 4, 10) AS INT)) FROM PhieuNhapKho";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) max = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("PNK%02d", max + 1);
    }

    // ===================== NGHIỆP VỤ XỬ LÝ (CHUYỂN TỪ ENTITY QUA) =====================

    public double tinhTongTien(List<ChiTietPhieuNhapKho> chiTietList) {
        if (chiTietList == null) return 0;
        double tong = 0;
        for (ChiTietPhieuNhapKho ct : chiTietList) {
            tong += ct.getSoLuong() * ct.getDonGia();
        }
        return tong;
    }

    /** Xác nhận phiếu nhập - cập nhật tồn kho sản phẩm (dành cho quản lý) */
    public void xacNhan(PhieuNhapKho pnk, List<ChiTietPhieuNhapKho> chiTietList, SanPhamDAO sanPhamDAO) {
        if (pnk == null) return;
        if (pnk.getTrangThai() != TrangThaiPhieuNhapKho.CHO_XAC_NHAN) {
            throw new IllegalStateException("Chỉ xác nhận được phiếu đang chờ xác nhận!");
        }
        if (chiTietList != null) {
            for (ChiTietPhieuNhapKho ct : chiTietList) {
                if (ct.getMaSanPham() != null) {
                    sanPhamDAO.nhapKho(ct.getMaSanPham(), ct.getSoLuong());
                }
            }
        }
        pnk.setTrangThai(TrangThaiPhieuNhapKho.DA_XAC_NHAN);
        update(pnk);
    }

    /** Hủy phiếu nhập (dành cho quản lý) */
    public void huyPhieu(PhieuNhapKho pnk) {
        if (pnk == null) return;
        if (pnk.getTrangThai() == TrangThaiPhieuNhapKho.DA_XAC_NHAN) {
            throw new IllegalStateException("Không thể hủy phiếu đã xác nhận!");
        }
        pnk.setTrangThai(TrangThaiPhieuNhapKho.DA_HUY);
        update(pnk);
    }

    public ChiTietPhieuNhapKho taoChiTiet(PhieuNhapKho pnk, SanPham sp, int soLuong, double donGia) {
        if (pnk == null) return null;
        return new ChiTietPhieuNhapKho(pnk, sp, soLuong, donGia);
    }

    public String trangThaiText(PhieuNhapKho pnk) {
        return (pnk == null || pnk.getTrangThai() == null) ? "" : pnk.getTrangThai().getMoTa();
    }
}
