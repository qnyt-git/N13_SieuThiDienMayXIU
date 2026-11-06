package GUI;

import DAO.HoaDonDAO;
import ENTITY.HoaDon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LichSuMuaHangGUI extends JPanel {
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private DefaultTableModel modelLichSu;
    private JTable tblLichSu;
    private JTextField txtTimKiemKH;

    public LichSuMuaHangGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setOpaque(false);
        JLabel lblTieuDe = new JLabel("Tra cứu lịch sử mua hàng của khách hàng");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        top.add(lblTieuDe, BorderLayout.NORTH);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlSearch.setOpaque(false);
        txtTimKiemKH = new JTextField(24);
        txtTimKiemKH.setPreferredSize(new Dimension(240, 32));
        JButton btnTim = taoNut("Tìm kiếm lịch sử", new Color(14, 98, 195), Color.WHITE);
        pnlSearch.add(new JLabel("Mã/Tên khách hàng hoặc SĐT:"));
        pnlSearch.add(txtTimKiemKH);
        pnlSearch.add(btnTim);
        top.add(pnlSearch, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);

        modelLichSu = new DefaultTableModel(new Object[]{
                "Mã hóa đơn", "Khách hàng", "Nhân viên lập", "Ngày lập", "Phương thức TT", "Trạng thái"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblLichSu = new JTable(modelLichSu);
        tblLichSu.setRowHeight(28);
        add(new JScrollPane(tblLichSu), BorderLayout.CENTER);

        btnTim.addActionListener(e -> taiDuLieu());
        taiDuLieu();
    }

    private void taiDuLieu() {
        modelLichSu.setRowCount(0);
        List<HoaDon> list = hoaDonDAO.getAll();
        String tuKhoa = txtTimKiemKH.getText().trim().toLowerCase();

        for (HoaDon hd : list) {
            String tenKH = (hd.getMaKhachHang() != null) ? hd.getMaKhachHang().getHoTenKhachHang() : "";
            String sdtKH = (hd.getMaKhachHang() != null) ? hd.getMaKhachHang().getSoDienThoai() : "";
            String thongTin = (hd.getMaHoaDon() + " " + tenKH + " " + sdtKH).toLowerCase();

            if (tuKhoa.isEmpty() || thongTin.contains(tuKhoa)) {
                modelLichSu.addRow(new Object[]{
                        hd.getMaHoaDon(),
                        tenKH + " (" + sdtKH + ")",
                        hd.getMaNhanVien() != null ? hd.getMaNhanVien().getHoTenNhanVien() : "",
                        hd.getNgayLap(),
                        hd.getPhuongThucThanhToan(),
                        hd.getTrangThai() != null ? hd.getTrangThai().getMoTa() : ""
                });
            }
        }
    }

    private JButton taoNut(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(fg);
        b.setFocusPainted(false);
        return b;
    }
}
