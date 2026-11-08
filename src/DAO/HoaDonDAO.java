package DAO;

import CONNECTDB.ConnectDB;
import ENTITY.ChiTietHoaDon;
import ENTITY.HoaDon;
import ENTITY.KhachHang;
import ENTITY.KhuyenMai;
import ENTITY.NhaCungCap;
import ENTITY.NhanVien;
import ENTITY.PhieuDatHang;
import ENTITY.SanPham;
import ENUM.PhuongThucThanhToan;
import ENUM.TrangThaiHoaDon;
import ENUM.TrangThaiSanPham;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HoaDonDAO implements IDAO<HoaDon> {

    private String lastError = "";

    // Lay loi SQL gan nhat
    public String getLastError() {
        return lastError;
    }

    // ===================== CHUYEN DOI DU LIEU =====================

    private PhuongThucThanhToan docPhuongThuc(String s) {
        String x = s == null ? "" : s.trim();

        for (PhuongThucThanhToan t : PhuongThucThanhToan.values()) {
            if (t.name().equalsIgnoreCase(x)
                    || t.getMoTa().equalsIgnoreCase(x)) {
                return t;
            }
        }

        // Xu ly truong hop cot VARCHAR(50) trong SQL lam loi dau tieng Viet cua du lieu mau
        String lower = x.toLowerCase();
        if (lower.contains("khoản") || lower.contains("khoan") || lower.contains("chuyển") || lower.contains("chuyen")) {
            return PhuongThucThanhToan.CHUYEN_KHOAN;
        }
        if (lower.contains("atm") || lower.contains("visa") || lower.contains("master") || lower.contains("thẻ") || lower.contains("the")) {
            return PhuongThucThanhToan.THE_NGAN_HANG;
        }
        if (lower.contains("momo") || lower.contains("shopee") || lower.contains("ví") || lower.contains("vi ")) {
            return PhuongThucThanhToan.VI_DIEN_TU;
        }

        return PhuongThucThanhToan.TIEN_MAT;
    }

    private TrangThaiHoaDon docTrangThai(String s) {
        String x = s == null ? "" : s.trim();

        for (TrangThaiHoaDon t : TrangThaiHoaDon.values()) {
            if (t.name().equalsIgnoreCase(x)
                    || t.getMoTa().equalsIgnoreCase(x)) {
                return t;
            }
        }

        // Xu ly truong hop cot VARCHAR(50) trong SQL bien chu "Đã thanh toán" thanh "?a thanh toan" hoac "Ða thanh toan"
        String lower = x.toLowerCase();
        if (lower.contains("chờ") || lower.contains("cho ") || lower.contains("xử lý") || lower.contains("xu ly")) {
            return TrangThaiHoaDon.CHO_THANH_TOAN;
        }
        if (lower.contains("thanh toán") || lower.contains("thanh toan") || lower.contains("da_thanh_toan")) {
            return TrangThaiHoaDon.DA_THANH_TOAN;
        }

        return TrangThaiHoaDon.CHO_THANH_TOAN;
    }

    private LocalDateTime toLdt(Timestamp t) {
        return t == null ? null : t.toLocalDateTime();
    }

    private Timestamp toTs(LocalDateTime t) {
        return t == null ? null : Timestamp.valueOf(t);
    }

    // ===================== DOC DU LIEU =====================

    @Override
    public List<HoaDon> getAll() {

        List<HoaDon> dsHoaDon = new ArrayList<>();

        Map<String, KhachHang> khMap = new HashMap<>();
        for (KhachHang k : new KhachHangDAO().getAll()) {
            khMap.put(k.getMaKhachHang(), k);
        }

        Map<String, NhanVien> nvMap = new HashMap<>();
        for (NhanVien n : new NhanVienDAO().getAll()) {
            nvMap.put(n.getMaNhanVien(), n);
        }

        Map<String, KhuyenMai> kmMap = new HashMap<>();
        for (KhuyenMai k : new KhuyenMaiDAO().getAll()) {
            kmMap.put(k.getMaKhuyenMai(), k);
        }

        Map<String, PhieuDatHang> pdhMap = new HashMap<>();
        for (PhieuDatHang p : new PhieuDatHangDAO().getAll()) {
            pdhMap.put(p.getMaPhieuDatHang(), p);
        }

        String sql = "SELECT * FROM HoaDon ORDER BY ngayLap DESC, maHoaDon DESC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                HoaDon hd = new HoaDon(
                        rs.getString("maHoaDon"),
                        pdhMap.get(rs.getString("maPhieuDatHang")),
                        khMap.get(rs.getString("maKhachHang")),
                        nvMap.get(rs.getString("maNhanVien")),
                        kmMap.get(rs.getString("maKhuyenMai")),
                        docPhuongThuc(
                                rs.getString("phuongThucThanhToan")),
                        toLdt(rs.getTimestamp("ngayLap")),
                        rs.getString("ghiChu"),
                        docTrangThai(rs.getString("trangThai"))
                );

                dsHoaDon.add(hd);
            }

        } catch (Exception e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }

        return dsHoaDon;
    }

    // ===================== CHI TIET HOA DON =====================

    /**
     * Lay danh sach chi tiet cua mot hoa don.
     */
    public List<ChiTietHoaDon> getChiTietByHoaDon(HoaDon hd) {

        List<ChiTietHoaDon> list = new ArrayList<>();

        if (hd == null) {
            return list;
        }

        Map<String, SanPham> spMap = new HashMap<>();

        for (SanPham s : new SanPhamDAO().getAll()) {
            spMap.put(s.getMaSanPham(), s);
        }

        String sql = "SELECT maHoaDon, maSanPham, soLuong, "
                + "donGia, thueVAT "
                + "FROM ChiTietHoaDon WHERE maHoaDon = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hd.getMaHoaDon());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    SanPham s = spMap.get(rs.getString("maSanPham"));

                    if (s != null) {

                        ChiTietHoaDon ct = new ChiTietHoaDon(
                                hd,
                                s,
                                rs.getInt("soLuong"),
                                rs.getDouble("donGia"),
                                rs.getFloat("thueVAT")
                        );

                        list.add(ct);
                    }
                }
            }

        } catch (Exception e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }

        return list;
    }

    public List<NhaCungCap> getDanhSachNhaCungCapByHoaDon(HoaDon hd) {
        Map<String, NhaCungCap> mapNcc = new LinkedHashMap<>();
        for (ChiTietHoaDon ct : getChiTietByHoaDon(hd)) {
            if (ct.getMaSanPham() != null && ct.getMaSanPham().getMaNCC() != null) {
                NhaCungCap ncc = ct.getMaSanPham().getMaNCC();
                mapNcc.put(ncc.getMaNCC(), ncc);
            }
        }
        return new ArrayList<>(mapNcc.values());
    }

    // ===================== THEM HOA DON =====================

    /**
     * Them thong tin hoa don vao database.
     * Luu phuongThucThanhToan.name() va trangThai.name() de khong bi loi font tren cot VARCHAR(50).
     */
    private boolean themHoaDonDB(Connection con, HoaDon h)
            throws SQLException {

        String sql = "INSERT INTO HoaDon "
                + "(maHoaDon, maPhieuDatHang, maKhachHang, "
                + "maNhanVien, maKhuyenMai, "
                + "phuongThucThanhToan, ngayLap, ghiChu, trangThai) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, h.getMaHoaDon());

            ps.setString(2,
                    h.getMaPhieuDatHang() != null
                    ? h.getMaPhieuDatHang().getMaPhieuDatHang()
                    : null);

            ps.setString(3,
                    h.getMaKhachHang() != null
                    ? h.getMaKhachHang().getMaKhachHang()
                    : null);

            ps.setString(4,
                    h.getMaNhanVien() != null
                    ? h.getMaNhanVien().getMaNhanVien()
                    : null);

            ps.setString(5,
                    h.getMaKhuyenMai() != null
                    ? h.getMaKhuyenMai().getMaKhuyenMai()
                    : null);

            ps.setString(6,
                    h.getPhuongThucThanhToan() != null
                    ? h.getPhuongThucThanhToan().name()
                    : null);

            ps.setTimestamp(7, toTs(h.getNgayLap()));
            ps.setString(8, h.getGhiChu());

            ps.setString(9,
                    h.getTrangThai() != null
                    ? h.getTrangThai().name()
                    : null);

            return ps.executeUpdate() > 0;
        }
    }

    // ===================== THEM CHI TIET HOA DON =====================

    /**
     * Them danh sach chi tiet hoa don va cap nhat tru ton kho san pham.
     */
    private boolean themChiTietDB(
            Connection con,
            HoaDon h,
            List<ChiTietHoaDon> chiTietList) throws SQLException {

        String sql = "INSERT INTO ChiTietHoaDon "
                + "(maHoaDon, maSanPham, soLuong, donGia, thueVAT) "
                + "VALUES (?, ?, ?, ?, ?)";

        String sqlTruKho = "UPDATE SanPham "
                + "SET soLuongTon = soLuongTon - ?, "
                + "    trangThai = CASE WHEN soLuongTon - ? <= 0 THEN ? ELSE trangThai END "
                + "WHERE maSanPham = ? AND soLuongTon >= ?";

        if (chiTietList == null || chiTietList.isEmpty()) {
            return true;
        }

        try (PreparedStatement ps = con.prepareStatement(sql);
             PreparedStatement psKho = con.prepareStatement(sqlTruKho)) {

            for (ChiTietHoaDon ct : chiTietList) {

                if (ct == null) {
                    throw new SQLException(
                            "Chi tiet hoa don khong hop le!");
                }

                if (ct.getMaSanPham() == null) {
                    throw new SQLException(
                            "San pham trong hoa don khong ton tai!");
                }

                if (ct.getSoLuong() <= 0) {
                    throw new SQLException(
                            "So luong san pham phai lon hon 0!");
                }

                String maSP = ct.getMaSanPham().getMaSanPham();
                int soLuongMua = ct.getSoLuong();

                ps.setString(1, h.getMaHoaDon());
                ps.setString(2, maSP);
                ps.setInt(3, soLuongMua);
                ps.setDouble(4, ct.getDonGia());
                ps.setFloat(5, ct.getThueVAT());

                ps.addBatch();

                // Cap nhat tru so luong ton kho cua san pham trong CSDL
                psKho.setInt(1, soLuongMua);
                psKho.setInt(2, soLuongMua);
                psKho.setString(3, TrangThaiSanPham.HET_HANG.name());
                psKho.setString(4, maSP);
                psKho.setInt(5, soLuongMua);

                if (psKho.executeUpdate() <= 0) {
                    throw new SQLException(
                            "San pham " + maSP + " khong du so luong ton kho!");
                }
            }

            ps.executeBatch();

            return true;
        }
    }

    // ===================== TRANSACTION HOA DON =====================

    /**
     * Them hoa don va chi tiet hoa don trong cung mot transaction.
     */
    public boolean insertWithDetails(
            HoaDon h,
            List<ChiTietHoaDon> chiTietList) {

        lastError = "";

        if (h == null) {
            lastError = "Thong tin hoa don khong ton tai!";
            return false;
        }

        Connection con = null;
        boolean autoCommitCu = true;

        try {

            con = ConnectDB.getConnection();

            if (con == null) {
                throw new SQLException(
                        "Khong ket noi duoc SQL Server!");
            }

            autoCommitCu = con.getAutoCommit();

            // Bat dau transaction
            con.setAutoCommit(false);

            // Buoc 1: Them hoa don
            if (!themHoaDonDB(con, h)) {
                throw new SQLException(
                        "Khong them duoc hoa don!");
            }

            // Buoc 2: Them chi tiet hoa don neu co
            if (chiTietList != null && !chiTietList.isEmpty()) {

                if (!themChiTietDB(con, h, chiTietList)) {
                    throw new SQLException(
                            "Khong them duoc chi tiet hoa don!");
                }
            }

            // Buoc 3: Luu tat ca thay doi
            con.commit();

            return true;

        } catch (Exception e) {

            lastError = e.getMessage() != null
                    ? e.getMessage()
                    : e.getClass().getSimpleName();

            // Neu co loi thi rollback
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    lastError += " | Loi rollback: "
                            + ex.getMessage();
                }
            }

            System.err.println(
                    "LOI LAP HOA DON: " + lastError);

            e.printStackTrace();

            return false;

        } finally {

            if (con != null) {

                try {
                    con.setAutoCommit(autoCommitCu);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ===================== INSERT =====================

    @Override
    public boolean insert(HoaDon h) {

        // Them hoa don khong kem chi tiet
        return insertWithDetails(h, new ArrayList<>());
    }

    // ===================== UPDATE =====================

    @Override
    public boolean update(HoaDon h) {

        lastError = "";

        String sql = "UPDATE HoaDon SET "
                + "maPhieuDatHang = ?, "
                + "maKhachHang = ?, "
                + "maNhanVien = ?, "
                + "maKhuyenMai = ?, "
                + "phuongThucThanhToan = ?, "
                + "ngayLap = ?, "
                + "ghiChu = ?, "
                + "trangThai = ? "
                + "WHERE maHoaDon = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1,
                    h.getMaPhieuDatHang() != null
                    ? h.getMaPhieuDatHang().getMaPhieuDatHang()
                    : null);

            ps.setString(2,
                    h.getMaKhachHang() != null
                    ? h.getMaKhachHang().getMaKhachHang()
                    : null);

            ps.setString(3,
                    h.getMaNhanVien() != null
                    ? h.getMaNhanVien().getMaNhanVien()
                    : null);

            ps.setString(4,
                    h.getMaKhuyenMai() != null
                    ? h.getMaKhuyenMai().getMaKhuyenMai()
                    : null);

            ps.setString(5,
                    h.getPhuongThucThanhToan() != null
                    ? h.getPhuongThucThanhToan().name()
                    : null);

            ps.setTimestamp(6, toTs(h.getNgayLap()));
            ps.setString(7, h.getGhiChu());

            ps.setString(8,
                    h.getTrangThai() != null
                    ? h.getTrangThai().name()
                    : null);

            ps.setString(9, h.getMaHoaDon());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            lastError = e.getMessage();
            e.printStackTrace();

            return false;
        }
    }

    // ===================== DELETE =====================

    @Override
    public boolean delete(HoaDon h) {

        System.out.println(
                "Loi nghiep vu: Hoa don tai chinh "
                + "khong duoc phep xoa hoac huy!");

        return false;
    }

    // ===================== TAO MA MOI =====================

    @Override
    public String taoMaMoi() {

        int max = 0;

        String sql = "SELECT MAX(TRY_CAST("
                + "SUBSTRING(maHoaDon, 3, 10) AS INT)) "
                + "FROM HoaDon";

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

        return String.format("HD%02d", max + 1);
    }
}