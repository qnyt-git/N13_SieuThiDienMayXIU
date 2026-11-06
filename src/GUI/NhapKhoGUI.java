package GUI;

import DAO.NhaCungCapDAO;
import DAO.PhieuNhapKhoDAO;
import DAO.SanPhamDAO;
import ENTITY.ChiTietPhieuNhapKho;
import ENTITY.NhaCungCap;
import ENTITY.PhieuNhapKho;
import ENTITY.SanPham;
import ENUM.TrangThaiPhieuNhapKho;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NhapKhoGUI extends JPanel {
    private final PhieuNhapKhoDAO pnkDAO = new PhieuNhapKhoDAO();
    private final NhaCungCapDAO nccDAO = new NhaCungCapDAO();
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();

    private DefaultTableModel modelPNK;
    private JTable tblPNK;

    public NhapKhoGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel lblTieuDe = new JLabel("Quản lý phiếu nhập kho linh kiện & thiết bị");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        top.add(lblTieuDe, BorderLayout.WEST);

        JButton btnTaoPhieu = taoNut("+ Lập phiếu nhập", new Color(14, 98, 195), Color.WHITE);
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtn.setOpaque(false);
        pnlBtn.add(btnTaoPhieu);
        top.add(pnlBtn, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        modelPNK = new DefaultTableModel(new Object[]{
                "Mã phiếu nhập", "Nhà cung cấp", "Nhân viên lập", "Ngày nhập", "Lý do nhập", "Trạng thái"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblPNK = new JTable(modelPNK);
        tblPNK.setRowHeight(28);
        add(new JScrollPane(tblPNK), BorderLayout.CENTER);

        btnTaoPhieu.addActionListener(e -> moFormNhapKho());
        taiDuLieu();
    }

    private void taiDuLieu() {
        modelPNK.setRowCount(0);
        List<PhieuNhapKho> list = pnkDAO.getAll();
        for (PhieuNhapKho p : list) {
            modelPNK.addRow(new Object[]{
                    p.getMaPhieuNhapKho(),
                    p.getMaNCC() != null ? p.getMaNCC().getTenNCC() : "",
                    p.getMaNhanVien() != null ? p.getMaNhanVien().getHoTenNhanVien() : "",
                    p.getNgayNhap(),
                    p.getLyDoNhap(),
                    p.getTrangThai() != null ? p.getTrangThai().getMoTa() : ""
            });
        }
    }

    private void moFormNhapKho() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Lập phiếu nhập kho mới", Dialog.ModalityType.APPLICATION_MODAL);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(18, 22, 10, 22));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.anchor = GridBagConstraints.WEST;

        JTextField txtMa = new JTextField(pnkDAO.taoMaMoi(), 22);
        txtMa.setEditable(false);

        List<NhaCungCap> listNCC = nccDAO.getAll();
        JComboBox<NhaCungCap> cboNCC = new JComboBox<>(listNCC.toArray(new NhaCungCap[0]));

        List<SanPham> listSP = sanPhamDAO.getAll();
        JComboBox<SanPham> cboSP = new JComboBox<>(listSP.toArray(new SanPham[0]));

        JTextField txtSoLuong = new JTextField("10", 22);
        JTextField txtDonGia = new JTextField("5000000", 22);
        JTextField txtLyDo = new JTextField("Nhập hàng bổ sung siêu thị", 22);

        themDong(form, g, 0, "Mã phiếu nhập", txtMa);
        themDong(form, g, 1, "Nhà cung cấp *", cboNCC);
        themDong(form, g, 2, "Sản phẩm nhập *", cboSP);
        themDong(form, g, 3, "Số lượng *", txtSoLuong);
        themDong(form, g, 4, "Đơn giá nhập *", txtDonGia);
        themDong(form, g, 5, "Lý do nhập", txtLyDo);

        JButton btnLuu = taoNut("Lưu phiếu nhập", new Color(14, 98, 195), Color.WHITE);
        btnLuu.addActionListener(e -> {
            try {
                NhaCungCap ncc = (NhaCungCap) cboNCC.getSelectedItem();
                SanPham sp = (SanPham) cboSP.getSelectedItem();
                int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
                double donGia = Double.parseDouble(txtDonGia.getText().trim());

                PhieuNhapKho pnk = new PhieuNhapKho(
                        txtMa.getText().trim(),
                        LocalDateTime.now(),
                        null, // Nhân viên hiện tại
                        ncc,
                        txtLyDo.getText().trim(),
                        TrangThaiPhieuNhapKho.CHO_XAC_NHAN
                );

                List<ChiTietPhieuNhapKho> chiTietList = new ArrayList<>();
                chiTietList.add(new ChiTietPhieuNhapKho(pnk, sp, soLuong, donGia));

                if (pnkDAO.insertWithDetails(pnk, chiTietList)) {
                    JOptionPane.showMessageDialog(dialog, "Lập phiếu nhập kho thành công (Trạng thái: Chờ xác nhận)!");
                    dialog.dispose();
                    taiDuLieu();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lập phiếu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Số lượng và đơn giá phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
