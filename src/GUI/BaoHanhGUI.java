package GUI;

import DAO.KhachHangDAO;
import DAO.PhieuBaoHanhDAO;
import DAO.SanPhamDAO;
import ENTITY.ChiTietPhieuBaoHanh;
import ENTITY.KhachHang;
import ENTITY.NhanVien;
import ENTITY.PhieuBaoHanh;
import ENTITY.SanPham;
import ENUM.TrangThaiPhieuBaoHanh;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BaoHanhGUI extends JPanel {
    private final PhieuBaoHanhDAO pbhDAO = new PhieuBaoHanhDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();

    private DefaultTableModel modelPBH;
    private JTable tblPBH;

    public BaoHanhGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel lblTieuDe = new JLabel("Quản lý tiếp nhận & xử lý bảo hành");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        top.add(lblTieuDe, BorderLayout.WEST);

        JButton btnTaoPhieu = taoNut("+ Lập phiếu bảo hành", new Color(14, 98, 195), Color.WHITE);
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtn.setOpaque(false);
        pnlBtn.add(btnTaoPhieu);
        top.add(pnlBtn, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        modelPBH = new DefaultTableModel(new Object[]{
                "Mã phiếu BH", "Khách hàng", "Nhân viên tiếp nhận", "Ngày tiếp nhận", "Ngày hẹn trả", "Trạng thái"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblPBH = new JTable(modelPBH);
        tblPBH.setRowHeight(28);
        add(new JScrollPane(tblPBH), BorderLayout.CENTER);

        btnTaoPhieu.addActionListener(e -> moFormLapPhieu());
        taiDuLieu();
    }

    private void taiDuLieu() {
        modelPBH.setRowCount(0);
        List<PhieuBaoHanh> list = pbhDAO.getAll();
        for (PhieuBaoHanh p : list) {
            modelPBH.addRow(new Object[]{
                    p.getMaPhieubaoHanh(),
                    p.getMaKhachHang() != null ? p.getMaKhachHang().getHoTenKhachHang() : "",
                    p.getMaNhanVien() != null ? p.getMaNhanVien().getHoTenNhanVien() : "",
                    p.getNgayTiepNhan(),
                    p.getNgayHenTra(),
                    p.getTrangThai() != null ? p.getTrangThai().getMoTa() : ""
            });
        }
    }

    private void moFormLapPhieu() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Lập phiếu bảo hành mới", Dialog.ModalityType.APPLICATION_MODAL);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(18, 22, 10, 22));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.anchor = GridBagConstraints.WEST;

        JTextField txtMa = new JTextField(pbhDAO.taoMaMoi(), 22);
        txtMa.setEditable(false);

        List<KhachHang> listKH = khachHangDAO.getAll();
        JComboBox<KhachHang> cboKH = new JComboBox<>(listKH.toArray(new KhachHang[0]));

        List<SanPham> listSP = sanPhamDAO.getAll();
        JComboBox<SanPham> cboSP = new JComboBox<>(listSP.toArray(new SanPham[0]));

        JTextField txtTinhTrang = new JTextField(22);
        JTextField txtNoiDung = new JTextField(22);

        themDong(form, g, 0, "Mã phiếu BH", txtMa);
        themDong(form, g, 1, "Khách hàng *", cboKH);
        themDong(form, g, 2, "Sản phẩm lỗi *", cboSP);
        themDong(form, g, 3, "Tình trạng sản phẩm *", txtTinhTrang);
        themDong(form, g, 4, "Nội dung bảo hành *", txtNoiDung);

        JButton btnLuu = taoNut("Lưu phiếu", new Color(14, 98, 195), Color.WHITE);
        btnLuu.addActionListener(e -> {
            KhachHang kh = (KhachHang) cboKH.getSelectedItem();
            SanPham sp = (SanPham) cboSP.getSelectedItem();
            String tinhTrang = txtTinhTrang.getText().trim();
            String noiDung = txtNoiDung.getText().trim();

            if (tinhTrang.isEmpty() || noiDung.isEmpty() || kh == null || sp == null) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                return;
            }

            // Tạo phiếu bảo hành mặc định trạng thái TIEP_NHAN
            PhieuBaoHanh pbh = new PhieuBaoHanh(
                    txtMa.getText().trim(),
                    kh,
                    null, // Lấy nhân viên đăng nhập hiện tại
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(3), // Hẹn trả sau 3 ngày
                    "",
                    TrangThaiPhieuBaoHanh.TIEP_NHAN
            );

            List<ChiTietPhieuBaoHanh> chiTietList = new ArrayList<>();
            chiTietList.add(new ChiTietPhieuBaoHanh(pbh, sp, tinhTrang, noiDung));

            if (pbhDAO.insertWithDetails(pbh, chiTietList)) {
                JOptionPane.showMessageDialog(dialog, "Lập phiếu bảo hành thành công!");
                dialog.dispose();
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(dialog, "Lập phiếu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.add(btnLuu);
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(pnlBottom, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void themDong(JPanel p, GridBagConstraints g, int row, String label, JComponent c) {
        g.gridy = row; g.gridx = 0; g.weightx = 0;
        p.add(new JLabel(label), g);
        g.gridx = 1; g.weightx = 1;
        p.add(c, g);
    }

    private JButton taoNut(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(fg);
        b.setFocusPainted(false);
        return b;
    }
}
