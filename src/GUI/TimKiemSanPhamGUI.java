package GUI;

import DAO.SanPhamDAO;
import ENTITY.SanPham;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class TimKiemSanPhamGUI extends JPanel {
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private DefaultTableModel modelSP;
    private JTable tblSP;
    private JTextField txtTuKhoa;
    private JComboBox<String> cboLocTrangThai;
    private List<SanPham> danhSachGoc = new ArrayList<>();

    public TimKiemSanPhamGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setOpaque(false);
        JLabel lblTieuDe = new JLabel("Tìm kiếm và tra cứu sản phẩm nâng cao");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        top.add(lblTieuDe, BorderLayout.NORTH);

        // Thanh điều kiện tìm kiếm và lọc
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlFilter.setOpaque(false);
        
        txtTuKhoa = new JTextField(22);
        txtTuKhoa.setPreferredSize(new Dimension(220, 32));
        
        cboLocTrangThai = new JComboBox<>(new String[]{"Tất cả trạng thái", "Sản phẩm mới", "Đang bán", "Hết hàng", "Tạm ngưng"});
        
        pnlFilter.add(new JLabel("Từ khóa (Mã/Tên/NCC/Nơi SX):"));
        pnlFilter.add(txtTuKhoa);
        pnlFilter.add(new JLabel("Trạng thái:"));
        pnlFilter.add(cboLocTrangThai);

        top.add(pnlFilter, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        modelSP = new DefaultTableModel(new Object[]{
                "Mã SP", "Tên sản phẩm", "Nhà cung cấp", "Nơi sản xuất", "Giá bán (VNĐ)", "Tồn kho", "Bảo hành", "Trạng thái"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblSP = new JTable(modelSP);
        tblSP.setRowHeight(28);
        add(new JScrollPane(tblSP), BorderLayout.CENTER);

        // Lắng nghe sự kiện tìm kiếm thời gian thực (Real-time search)
        txtTuKhoa.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { thucHienTimKiem(); }
            public void removeUpdate(DocumentEvent e) { thucHienTimKiem(); }
            public void changedUpdate(DocumentEvent e) { thucHienTimKiem(); }
        });

        cboLocTrangThai.addActionListener(e -> thucHienTimKiem());

        // Tự động tải lại dữ liệu khi mở màn hình Tìm kiếm sản phẩm
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                taiDuLieu();
            }
        });

        taiDuLieu();
    }

    private void taiDuLieu() {
        danhSachGoc = sanPhamDAO.getAll();
        thucHienTimKiem();
    }

    private void hienThiDanhSach(List<SanPham> list) {
        modelSP.setRowCount(0);
        for (SanPham sp : list) {
            String tenNCC = (sp.getMaNCC() != null) ? sp.getMaNCC().getTenNCC() : "";
            String noiSX = (sp.getNoiSanXuat() != null) ? sp.getNoiSanXuat() : "";
            modelSP.addRow(new Object[]{
                    sp.getMaSanPham(),
                    sp.getTenSanPham(),
                    tenNCC,
                    noiSX,
                    String.format("%,.0f", sp.getGiaBan()),
                    sp.getSoLuongTon(),
                    sp.getThoiHanBaoHanh(),
                    sp.getTrangThai() != null ? sp.getTrangThai().getMoTa() : ""
            });
        }
    }

    private void thucHienTimKiem() {
        String tuKhoa = txtTuKhoa.getText().trim().toLowerCase();
        String trangThaiChon = (String) cboLocTrangThai.getSelectedItem();

        List<SanPham> ketQua = new ArrayList<>();
        for (SanPham sp : danhSachGoc) {
            String tenNCC = (sp.getMaNCC() != null) ? sp.getMaNCC().getTenNCC() : "";
            String noiSX = (sp.getNoiSanXuat() != null) ? sp.getNoiSanXuat() : "";
            String thongTin = (sp.getMaSanPham() + " " + sp.getTenSanPham() + " " + tenNCC + " " + noiSX).toLowerCase();
            
            boolean thoaManTuKhoa = thongTin.contains(tuKhoa);
            boolean thoaManTrangThai = trangThaiChon.equals("Tất cả trạng thái") || 
                    (sp.getTrangThai() != null && sp.getTrangThai().getMoTa().equalsIgnoreCase(trangThaiChon));

            if (thoaManTuKhoa && thoaManTrangThai) {
                ketQua.add(sp);
            }
        }
        hienThiDanhSach(ketQua);
    }
}