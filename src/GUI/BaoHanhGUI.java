package GUI;

import DAO.KhachHangDAO;
import DAO.PhieuBaoHanhDAO;
import DAO.SanPhamDAO;
import ENTITY.*;
import ENUM.TrangThaiPhieuBaoHanh;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaoHanhGUI extends JPanel {
    private final PhieuBaoHanhDAO dao = new PhieuBaoHanhDAO();
    private final KhachHangDAO khDAO = new KhachHangDAO();
    private final SanPhamDAO spDAO = new SanPhamDAO();
    private final NhanVien nhanVienDangNhap;
    private DefaultTableModel model;
    private JTable table;

    public BaoHanhGUI(NhanVien nhanVienDangNhap) {
        this.nhanVienDangNhap = nhanVienDangNhap;
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel title = new JLabel("Quản lý tiếp nhận & xử lý bảo hành");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        top.add(title, BorderLayout.WEST);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);
        JButton add = nut("+ Lập phiếu bảo hành");
        JButton edit = nut("Sửa phiếu");
        JButton delete = nut("Xóa phiếu");
        buttons.add(add); buttons.add(edit); buttons.add(delete);
        top.add(buttons, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"Mã phiếu BH","Khách hàng","Nhân viên tiếp nhận","Ngày tiếp nhận","Ngày hẹn trả","Trạng thái"},0) {
            public boolean isCellEditable(int r,int c){ return false; }
        };
        table = new JTable(model);
        table.setRowHeight(28);
        add(new JScrollPane(table), BorderLayout.CENTER);

        add.addActionListener(e -> moForm(null));
        edit.addActionListener(e -> {
            PhieuBaoHanh p = getSelected();
            if (p != null) moForm(p);
        });
        delete.addActionListener(e -> xoaPhieu());
        taiDuLieu();
    }

    private List<PhieuBaoHanh> danhSach = new ArrayList<>();

    private void taiDuLieu() {
        model.setRowCount(0);
        danhSach = dao.getAll();
        for (PhieuBaoHanh p : danhSach) {
            model.addRow(new Object[]{p.getMaPhieubaoHanh(),
                p.getMaKhachHang()!=null?p.getMaKhachHang().getHoTenKhachHang():"",
                p.getMaNhanVien()!=null?p.getMaNhanVien().getHoTenNhanVien():"",
                p.getNgayTiepNhan(),p.getNgayHenTra(),
                p.getTrangThai()!=null?p.getTrangThai().getMoTa():""});
        }
    }

    private PhieuBaoHanh getSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this,"Vui lòng chọn phiếu bảo hành."); return null; }
        return danhSach.get(table.convertRowIndexToModel(row));
    }

    private void moForm(PhieuBaoHanh cu) {
        boolean sua = cu != null;
        JDialog d = new JDialog(SwingUtilities.getWindowAncestor(this), sua?"Cập nhật phiếu bảo hành":"Lập phiếu bảo hành mới", Dialog.ModalityType.APPLICATION_MODAL);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(16,20,12,20));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6,5,6,5); g.fill = GridBagConstraints.HORIZONTAL; g.anchor = GridBagConstraints.WEST;

        JTextField ma = new JTextField(sua?cu.getMaPhieubaoHanh():dao.taoMaMoi(),22); ma.setEditable(false);
        JComboBox<KhachHang> kh = new JComboBox<>(khDAO.getAll().toArray(new KhachHang[0]));
        JComboBox<SanPham> sp = new JComboBox<>(spDAO.getAll().toArray(new SanPham[0]));
        hienThiKhachHang(kh); hienThiSanPham(sp);
        JTextField tinhTrang = new JTextField(22), noiDung = new JTextField(22);
        JDateChooser ngayNhan = new JDateChooser(), ngayHen = new JDateChooser();
        ngayNhan.setDateFormatString("dd/MM/yyyy"); ngayHen.setDateFormatString("dd/MM/yyyy");
        ngayNhan.setDate(new Date()); ngayHen.setDate(Date.from(LocalDateTime.now().plusDays(3).atZone(ZoneId.systemDefault()).toInstant()));
        JComboBox<TrangThaiPhieuBaoHanh> trangThai = new JComboBox<>(TrangThaiPhieuBaoHanh.values());

        if (sua) {
            kh.setSelectedItem(cu.getMaKhachHang());
            ngayNhan.setDate(Date.from(cu.getNgayTiepNhan().atZone(ZoneId.systemDefault()).toInstant()));
            ngayHen.setDate(Date.from(cu.getNgayHenTra().atZone(ZoneId.systemDefault()).toInstant()));
            trangThai.setSelectedItem(cu.getTrangThai());
            List<ChiTietPhieuBaoHanh> ds = dao.getChiTietByPhieu(cu);
            if (!ds.isEmpty()) {
                sp.setSelectedItem(ds.get(0).getMaSanPham());
                tinhTrang.setText(ds.get(0).getTinhTrangSanPham());
                noiDung.setText(ds.get(0).getNoiDungBaoHanh());
            }
        }

        dong(form,g,0,"Mã phiếu BH",ma);
        dong(form,g,1,"Khách hàng *",kh);
        dong(form,g,2,"Sản phẩm lỗi *",sp);
        dong(form,g,3,"Tình trạng sản phẩm *",tinhTrang);
        dong(form,g,4,"Nội dung bảo hành *",noiDung);
        dong(form,g,5,"Ngày tiếp nhận *",ngayNhan);
        dong(form,g,6,"Ngày hẹn trả *",ngayHen);
        dong(form,g,7,"Trạng thái",trangThai);

        JButton save = nut(sua?"Lưu thay đổi":"Lưu phiếu");
        save.addActionListener(e -> {
            KhachHang k = (KhachHang)kh.getSelectedItem();
            SanPham s = (SanPham)sp.getSelectedItem();
            if (nhanVienDangNhap==null || k==null || s==null || tinhTrang.getText().trim().isEmpty()
                    || noiDung.getText().trim().isEmpty() || ngayNhan.getDate()==null || ngayHen.getDate()==null) {
                JOptionPane.showMessageDialog(d,"Vui lòng nhập đầy đủ thông tin."); return;
            }
            LocalDateTime n1=ngayNhan.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
            LocalDateTime n2=ngayHen.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
            if(n2.isBefore(n1)){JOptionPane.showMessageDialog(d,"Ngày hẹn trả không được trước ngày tiếp nhận.");return;}
            PhieuBaoHanh p = new PhieuBaoHanh(ma.getText(),k,nhanVienDangNhap,n1,n2,"",
                    (TrangThaiPhieuBaoHanh)trangThai.getSelectedItem());
            List<ChiTietPhieuBaoHanh> details=new ArrayList<>();
            details.add(new ChiTietPhieuBaoHanh(p,s,tinhTrang.getText().trim(),noiDung.getText().trim()));
            boolean ok = sua ? dao.updateWithDetails(p,details) : dao.insertWithDetails(p,details);
            if(ok){JOptionPane.showMessageDialog(d,sua?"Cập nhật phiếu thành công!":"Lập phiếu bảo hành thành công!");d.dispose();taiDuLieu();}
            else JOptionPane.showMessageDialog(d,"Không lưu được phiếu. "+dao.getLastError(),"Lỗi",JOptionPane.ERROR_MESSAGE);
        });
        JPanel bottom=new JPanel(new FlowLayout(FlowLayout.RIGHT)); bottom.setBackground(Color.WHITE); bottom.add(save);
        d.add(form,BorderLayout.CENTER);d.add(bottom,BorderLayout.SOUTH);d.setSize(700,510);d.setResizable(false);d.setLocationRelativeTo(this);d.setVisible(true);
    }

    private void xoaPhieu(){
        PhieuBaoHanh p=getSelected(); if(p==null)return;
        int c=JOptionPane.showConfirmDialog(this,"Bạn có chắc muốn xóa phiếu "+p.getMaPhieubaoHanh()+"?","Xác nhận",JOptionPane.YES_NO_OPTION);
        if(c==JOptionPane.YES_OPTION){if(dao.delete(p)){JOptionPane.showMessageDialog(this,"Đã xóa phiếu.");taiDuLieu();}
            else JOptionPane.showMessageDialog(this,"Xóa không thành công. Có thể phiếu đang được bảng khác tham chiếu.");}
    }
    private void hienThiKhachHang(JComboBox<KhachHang> c){c.setRenderer(new DefaultListCellRenderer(){public Component getListCellRendererComponent(JList<?> l,Object v,int i,boolean s,boolean f){super.getListCellRendererComponent(l,v,i,s,f);if(v instanceof KhachHang){KhachHang x=(KhachHang)v;setText(x.getMaKhachHang()+" - "+x.getHoTenKhachHang());}return this;}});}
    private void hienThiSanPham(JComboBox<SanPham> c){c.setRenderer(new DefaultListCellRenderer(){public Component getListCellRendererComponent(JList<?> l,Object v,int i,boolean s,boolean f){super.getListCellRendererComponent(l,v,i,s,f);if(v instanceof SanPham){SanPham x=(SanPham)v;setText(x.getMaSanPham()+" - "+x.getTenSanPham());}return this;}});}
    private void dong(JPanel p,GridBagConstraints g,int r,String label,JComponent c){g.gridy=r;g.gridx=0;g.weightx=0;p.add(new JLabel(label),g);g.gridx=1;g.weightx=1;c.setPreferredSize(new Dimension(440,28));p.add(c,g);}
    private JButton nut(String t){JButton b=new JButton(t);b.setBackground(new Color(14,98,195));b.setForeground(Color.WHITE);b.setFocusPainted(false);return b;}
}
