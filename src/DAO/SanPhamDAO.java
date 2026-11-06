package DAO;

import CONNECTDB.ConnectDB;
import ENTITY.NhaCungCap;
import ENTITY.SanPham;
import ENUM.TrangThaiSanPham;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SanPhamDAO implements IDAO<SanPham> {

    private static TrangThaiSanPham docTrangThai(String s) {
        String x = s == null ? "" : s.trim();
        for (TrangThaiSanPham t : TrangThaiSanPham.values()) {
            if (t.name().equalsIgnoreCase(x) || t.getMoTa().equalsIgnoreCase(x)) return t;
        }
        return TrangThaiSanPham.DANG_BAN;
    }

    @Override
    public List<SanPham> getAll() {
        List<SanPham> list = new ArrayList<>();
        Map<String, NhaCungCap> nccMap = new HashMap<>();
        for (NhaCungCap ncc : new NhaCungCapDAO().getAll()) {
            nccMap.put(ncc.getMaNCC(), ncc);
        }

        String sql = "SELECT * FROM SanPham";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SanPham sp = new SanPham(
                    rs.getString("maSanPham"),
                    rs.getString("tenSanPham"),
                    nccMap.get(rs.getString("maNCC")),
                    rs.getDouble("giaBan"),
                    rs.getInt("soLuongTon"),
                    rs.getString("thoiHanBaoHanh"),
                    rs.getString("moTaSanPham"),
                    docTrangThai(rs.getString("trangThai"))
                );
                list.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean insert(SanPham sp) {
        String sql = "INSERT INTO SanPham(maSanPham, tenSanPham, maNCC, giaBan, soLuongTon, thoiHanBaoHanh, moTaSanPham, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sp.getMaSanPham());
            ps.setString(2, sp.getTenSanPham());
            ps.setString(3, sp.getMaNCC() != null ? sp.getMaNCC().getMaNCC() : null);
            ps.setDouble(4, sp.getGiaBan());
            ps.setInt(5, sp.getSoLuongTon());
            ps.setString(6, sp.getThoiHanBaoHanh());
            ps.setString(7, sp.getMoTaSanPham());
            ps.setString(8, sp.getTrangThai() != null ? sp.getTrangThai().name() : TrangThaiSanPham.DANG_BAN.name());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(SanPham sp) {
        String sql = "UPDATE SanPham SET tenSanPham = ?, maNCC = ?, giaBan = ?, soLuongTon = ?, thoiHanBaoHanh = ?, moTaSanPham = ?, trangThai = ? WHERE maSanPham = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSanPham());
            ps.setString(2, sp.getMaNCC() != null ? sp.getMaNCC().getMaNCC() : null);
            ps.setDouble(3, sp.getGiaBan());
            ps.setInt(4, sp.getSoLuongTon());
            ps.setString(5, sp.getThoiHanBaoHanh());
            ps.setString(6, sp.getMoTaSanPham());
            ps.setString(7, sp.getTrangThai() != null ? sp.getTrangThai().name() : null);
            ps.setString(8, sp.getMaSanPham());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(SanPham sp) {
        String sql = "DELETE FROM SanPham WHERE maSanPham = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sp.getMaSanPham());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String taoMaMoi() {
        int max = 0;
        String sql = "SELECT MAX(TRY_CAST(SUBSTRING(maSanPham, 3, 10) AS INT)) FROM SanPham";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) max = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("SP%02d", max + 1);
    }

    // ===================== NGHIỆP VỤ XỬ LÝ (CHUYỂN TỪ ENTITY QUA) =====================

    /** Kiểm tra còn hàng để bán */
    public boolean conHang(SanPham sp) {
        return sp != null && sp.getSoLuongTon() > 0 && sp.getTrangThai() != TrangThaiSanPham.HET_HANG;
    }

    /** Cập nhật tồn kho (nhập thêm) và lưu CSDL */
    public boolean nhapKho(SanPham sp, int soLuong) {
        if (sp == null || soLuong <= 0) return false;
        sp.setSoLuongTon(sp.getSoLuongTon() + soLuong);
        if (sp.getTrangThai() == TrangThaiSanPham.HET_HANG) {
            sp.setTrangThai(TrangThaiSanPham.DANG_BAN);
        }
        return update(sp);
    }

    /** Xuất kho (bán) và lưu CSDL */
    public boolean xuatKho(SanPham sp, int soLuong) {
        if (sp == null || soLuong <= 0 || soLuong > sp.getSoLuongTon()) return false;
        sp.setSoLuongTon(sp.getSoLuongTon() - soLuong);
        if (sp.getSoLuongTon() == 0) {
            sp.setTrangThai(TrangThaiSanPham.HET_HANG);
        }
        return update(sp);
    }

    /** Kiểm tra có đang bán được không */
    public boolean coTheBan(SanPham sp) {
        return sp != null && (sp.getTrangThai() == TrangThaiSanPham.DANG_BAN || sp.getTrangThai() == TrangThaiSanPham.SAN_PHAM_MOI);
    }

    /** "24 tháng" -> 24. */
    public int baoHanhThang(SanPham sp) {
        if (sp == null || sp.getThoiHanBaoHanh() == null) return 0;
        String s = sp.getThoiHanBaoHanh().replaceAll("\\D", "");
        return s.isEmpty() ? 0 : Integer.parseInt(s);
    }

    public void datBaoHanhThang(SanPham sp, int thang) {
        if (sp != null) {
            sp.setThoiHanBaoHanh(thang + " tháng");
            update(sp);
        }
    }

    public boolean coTheBanSoLuong(SanPham sp, int sl) {
        return coTheBan(sp) && sl > 0 && sp != null && sl <= sp.getSoLuongTon();
    }

    public boolean sapHetHang(SanPham sp, int nguong) {
        return coTheBan(sp) && sp != null && sp.getSoLuongTon() < nguong;
    }

    public boolean conBaoHanh(SanPham sp, LocalDateTime ngayMua) {
        return sp != null && ngayMua != null && !ngayMua.plusMonths(baoHanhThang(sp))
                .isBefore(LocalDateTime.now());
    }
}
