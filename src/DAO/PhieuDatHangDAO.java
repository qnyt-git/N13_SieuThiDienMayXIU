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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO của PhieuDatHang.
 * Tuân thủ mô hình 3 lớp: Đưa toàn bộ thao tác CSDL và các phương thức xử lý từ Entity sang DAO.
 */
public class PhieuDatHangDAO implements IDAO<PhieuDatHang> {

    private String lastError = "";

    public String getLastError() {
        return lastError;
    }

    private TrangThaiPhieuDatHang docTrangThai(String s) {
        String x = s == null ? "" : s.trim();
        for (TrangThaiPhieuDatHang t : TrangThaiPhieuDatHang.values()) {
            if (t.name().equalsIgnoreCase(x) || t.getMoTa().equalsIgnoreCase(x)) {
                return t;
            }
        }
        String lower = x.toLowerCase();
        if (lower.contains("hủy") || lower.contains("huy")) {
            return TrangThaiPhieuDatHang.DA_HUY;
        }
        if ((lower.contains("thanh toán") || lower.contains("thanh toan"))
                && !lower.contains("chờ") && !lower.contains("cho")) {
            return TrangThaiPhieuDatHang.DA_THANH_TOAN;
        }
        return TrangThaiPhieuDatHang.CHO_XU_LY;
    }

    private LocalDateTime toLdt(Timestamp t) {
        return t == null ? null : t.toLocalDateTime();
    }

    private Timestamp toTs(LocalDateTime t) {
        return t == null ? null : Timestamp.valueOf(t);
    }

    @Override
    public List<PhieuDatHang> getAll() {
        List<PhieuDatHang> list = new ArrayList<>();

        Map<String, KhachHang> khMap = new HashMap<>();
        for (KhachHang k : new KhachHangDAO().getAll()) {
            khMap.put(k.getMaKhachHang(), k);
        }

        Map<String, NhanVien> nvMap = new HashMap<>();
        for (NhanVien n : new NhanVienDAO().getAll()) {
            nvMap.put(n.getMaNhanVien(), n);
        }

        String sql = "SELECT * FROM PhieuDatHang ORDER BY ngayDatHang DESC, maPhieuDatHang DESC";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PhieuDatHang p = new PhieuDatHang(
                        rs.getString("maPhieuDatHang"),
                        khMap.get(rs.getString("maKhachHang")),
                        nvMap.get(rs.getString("maNhanVien")),
                        toLdt(rs.getTimestamp("ngayDatHang")),
                        rs.getString("ghiChu"),
                        docTrangThai(rs.getString("trangThai"))
                );
                list.add(p);
            }
        } catch (Exception e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách chi tiết sản phẩm của một phiếu đặt hàng.
     */
    public List<ChiTietPhieuDatHang> getChiTietByPhieu(PhieuDatHang pdh) {
        List<ChiTietPhieuDatHang> list = new ArrayList<>();
        if (pdh == null) return list;

        Map<String, SanPham> spMap = new HashMap<>();
        for (SanPham s : new SanPhamDAO().getAll()) {
            spMap.put(s.getMaSanPham(), s);
        }

        String sql = "SELECT maPhieuDatHang, maSanPham, soLuong, donGia "
                + "FROM ChiTietPhieuDatHang WHERE maPhieuDatHang = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pdh.getMaPhieuDatHang());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham s = spMap.get(rs.getString("maSanPham"));
                    if (s != null) {
                        list.add(new ChiTietPhieuDatHang(
                                pdh,
                                s,
                                rs.getInt("soLuong"),
                                rs.getDouble("donGia")
                        ));
                    }
                }
            }
        } catch (Exception e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thêm phiếu đặt hàng kèm danh sách chi tiết trong cùng một Transaction.
     */
    public boolean insertWithDetails(PhieuDatHang pdh, List<ChiTietPhieuDatHang> chiTietList) {
        lastError = "";
        if (pdh == null) {
            lastError = "Thông tin phiếu đặt hàng không tồn tại!";
            return false;
        }

        String sqlPdh = "INSERT INTO PhieuDatHang "
                + "(maPhieuDatHang, maKhachHang, maNhanVien, ngayDatHang, ghiChu, trangThai) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        String sqlCt = "INSERT INTO ChiTietPhieuDatHang "
                + "(maPhieuDatHang, maSanPham, soLuong, donGia) VALUES (?, ?, ?, ?)";

        Connection con = null;
        boolean autoCommitCu = true;
        try {
            con = ConnectDB.getConnection();
            if (con == null) {
                throw new SQLException("Không kết nối được SQL Server!");
            }
            autoCommitCu = con.getAutoCommit();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sqlPdh)) {
                ps.setString(1, pdh.getMaPhieuDatHang());
                ps.setString(2, pdh.getMaKhachHang() != null ? pdh.getMaKhachHang().getMaKhachHang() : null);
                ps.setString(3, pdh.getMaNhanVien() != null ? pdh.getMaNhanVien().getMaNhanVien() : null);
                ps.setTimestamp(4, toTs(pdh.getNgayDatHang()));
                ps.setString(5, pdh.getGhiChu());
                ps.setString(6, pdh.getTrangThai() != null ? pdh.getTrangThai().name() : TrangThaiPhieuDatHang.CHO_XU_LY.name());
                if (ps.executeUpdate() <= 0) {
                    throw new SQLException("Không thêm được phiếu đặt hàng!");
                }
            }

            if (chiTietList != null && !chiTietList.isEmpty()) {
                try (PreparedStatement psCt = con.prepareStatement(sqlCt)) {
                    for (ChiTietPhieuDatHang ct : chiTietList) {
                        if (ct == null || ct.getMaSanPham() == null || ct.getSoLuong() <= 0) {
                            throw new SQLException("Chi tiết phiếu đặt hàng không hợp lệ!");
                        }
                        psCt.setString(1, pdh.getMaPhieuDatHang());
                        psCt.setString(2, ct.getMaSanPham().getMaSanPham());
                        psCt.setInt(3, ct.getSoLuong());
                        psCt.setDouble(4, ct.getDonGia());
                        psCt.addBatch();
                    }
                    psCt.executeBatch();
                }
            }

            con.commit();
            return true;
        } catch (Exception e) {
            lastError = e.getMessage();
            if (con != null) {
                try { con.rollback(); } catch (SQLException ignored) {}
            }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(autoCommitCu); } catch (SQLException ignored) {}
                try { con.close(); } catch (SQLException ignored) {}
            }
        }
    }

    @Override
    public boolean insert(PhieuDatHang p) {
        return insertWithDetails(p, new ArrayList<>());
    }

    @Override
    public boolean update(PhieuDatHang p) {
        lastError = "";
        String sql = "UPDATE PhieuDatHang SET maKhachHang = ?, maNhanVien = ?, "
                + "ngayDatHang = ?, ghiChu = ?, trangThai = ? WHERE maPhieuDatHang = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getMaKhachHang() != null ? p.getMaKhachHang().getMaKhachHang() : null);
            ps.setString(2, p.getMaNhanVien() != null ? p.getMaNhanVien().getMaNhanVien() : null);
            ps.setTimestamp(3, toTs(p.getNgayDatHang()));
            ps.setString(4, p.getGhiChu());
            ps.setString(5, p.getTrangThai() != null ? p.getTrangThai().name() : null);
            ps.setString(6, p.getMaPhieuDatHang());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            lastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(PhieuDatHang p) {
        if (p == null) return false;
        p.setTrangThai(TrangThaiPhieuDatHang.DA_HUY);
        return update(p);
    }

    @Override
    public String taoMaMoi() {
        int max = 0;
        String sql = "SELECT MAX(TRY_CAST(SUBSTRING(maPhieuDatHang, 4, 10) AS INT)) FROM PhieuDatHang";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                max = rs.getInt(1);
            }
        } catch (Exception e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return String.format("PDH%02d", max + 1);
    }

    // ===================== NGHIỆP VỤ XỬ LÝ (CHUYỂN TỪ ENTITY QUA) =====================

    public double tinhTongTien(List<ChiTietPhieuDatHang> chiTietList) {
        if (chiTietList == null) return 0;
        double tong = 0;
        for (ChiTietPhieuDatHang c : chiTietList) {
            if (c != null) {
                tong += Math.round(c.getSoLuong() * c.getDonGia());
            }
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
        return pdh != null && pdh.getTrangThai() == TrangThaiPhieuDatHang.DA_THANH_TOAN;
    }

    public ChiTietPhieuDatHang taoChiTiet(PhieuDatHang pdh, SanPham sp, int soLuong, double donGia) {
        return new ChiTietPhieuDatHang(pdh, sp, soLuong, donGia);
    }

    public String trangThaiText(PhieuDatHang pdh) {
        if (pdh == null || pdh.getTrangThai() == null) return "";
        return pdh.getTrangThai().getMoTa();
    }
}