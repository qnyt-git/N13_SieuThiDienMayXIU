package GUI;

import DAO.NhaCungCapDAO;
import DAO.PhieuNhapKhoDAO;
import DAO.SanPhamDAO;
import ENTITY.*;
import ENUM.TrangThaiPhieuNhapKho;
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

public class NhapKhoGUI extends JPanel {
    private final PhieuNhapKhoDAO dao = new PhieuNhapKhoDAO();
    private final NhaCungCapDAO nccDAO = new NhaCungCapDAO();
    private final SanPhamDAO spDAO = new SanPhamDAO();
    private final NhanVien nhanVienDangNhap;
    private DefaultTableModel model;
    private JTable table;
    private List<PhieuNhapKho> danhSach = new ArrayList<>();

    public NhapKhoGUI(NhanVien nv) {
        nhanVienDangNhap=nv;
        setLayout(new BorderLayout(0,12));setBackground(new Color(245,247,250));setBorder(new EmptyBorder(18,22,18,22));
        JPanel top=new JPanel(new BorderLayout());top.setOpaque(false);
        JLabel title=new JLabel("Quản lý phiếu nhập kho linh kiện & thiết bị");title.setFont(new Font("Arial",Font.BOLD,20));top.add(title,BorderLayout.WEST);
        JPanel buttons=new JPanel(new FlowLayout(FlowLayout.RIGHT));buttons.setOpaque(false);
        JButton add=nut("+ Lập phiếu nhập"),edit=nut("Sửa phiếu"),del=nut("Xóa phiếu");
        buttons.add(add);buttons.add(edit);buttons.add(del);top.add(buttons,BorderLayout.EAST);add(top,BorderLayout.NORTH);
        model=new DefaultTableModel(new Object[]{"Mã phiếu nhập","Nhà cung cấp","Nhân viên lập","Ngày nhập","Lý do nhập","Trạng thái"},0){public boolean isCellEditable(int r,int c){return false;}};
        table=new JTable(model);table.setRowHeight(28);add(new JScrollPane(table),BorderLayout.CENTER);
        add.addActionListener(e->moForm(null));
        edit.addActionListener(e->{PhieuNhapKho p=chon();if(p!=null)moForm(p);});
        del.addActionListener(e->xoa());
        taiDuLieu();
    }

    private void taiDuLieu(){
        model.setRowCount(0);danhSach=dao.getAll();
        for(PhieuNhapKho p:danhSach)model.addRow(new Object[]{p.getMaPhieuNhapKho(),
            p.getMaNCC()!=null?p.getMaNCC().getTenNCC():"",
            p.getMaNhanVien()!=null?p.getMaNhanVien().getHoTenNhanVien():"",
            p.getNgayNhap(),p.getLyDoNhap(),p.getTrangThai()!=null?p.getTrangThai().getMoTa():""});
    }
    private PhieuNhapKho chon(){int r=table.getSelectedRow();if(r<0){JOptionPane.showMessageDialog(this,"Vui lòng chọn phiếu nhập kho.");return null;}return danhSach.get(table.convertRowIndexToModel(r));}

    private void moForm(PhieuNhapKho cu){
        boolean sua=cu!=null;
        if (sua && cu.getTrangThai() != TrangThaiPhieuNhapKho.CHO_XAC_NHAN) {
            JOptionPane.showMessageDialog(this, "Chỉ được sửa phiếu đang chờ xác nhận để tránh sai lệch tồn kho.");
            return;
        }
        JDialog d=new JDialog(SwingUtilities.getWindowAncestor(this),sua?"Cập nhật phiếu nhập kho":"Lập phiếu nhập kho mới",Dialog.ModalityType.APPLICATION_MODAL);
        JPanel form=new JPanel(new GridBagLayout());form.setBackground(Color.WHITE);form.setBorder(new EmptyBorder(16,20,12,20));
        GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(6,5,6,5);g.fill=GridBagConstraints.HORIZONTAL;g.anchor=GridBagConstraints.WEST;
        JTextField ma=new JTextField(sua?cu.getMaPhieuNhapKho():dao.taoMaMoi(),22);ma.setEditable(false);
        JComboBox<NhaCungCap> ncc=new JComboBox<>(nccDAO.getAll().toArray(new NhaCungCap[0]));
        JComboBox<SanPham> sp=new JComboBox<>();
        ncc.setRenderer(new DefaultListCellRenderer(){public Component getListCellRendererComponent(JList<?> l,Object v,int i,boolean s,boolean f){super.getListCellRendererComponent(l,v,i,s,f);if(v instanceof NhaCungCap){NhaCungCap x=(NhaCungCap)v;setText(x.getMaNCC()+" - "+x.getTenNCC());}return this;}});
        sp.setRenderer(new DefaultListCellRenderer(){public Component getListCellRendererComponent(JList<?> l,Object v,int i,boolean s,boolean f){super.getListCellRendererComponent(l,v,i,s,f);if(v instanceof SanPham){SanPham x=(SanPham)v;setText(x.getMaSanPham()+" - "+x.getTenSanPham());}return this;}});
        Runnable loc=()->{NhaCungCap x=(NhaCungCap)ncc.getSelectedItem();sp.removeAllItems();for(SanPham xsp:spDAO.getAll())if(x!=null&&xsp.getMaNCC()!=null&&x.getMaNCC().equals(xsp.getMaNCC().getMaNCC()))sp.addItem(xsp);};
        ncc.addActionListener(e->loc.run());loc.run();
        JTextField soLuong=new JTextField(22),donGia=new JTextField(22),lyDo=new JTextField(22);
        JDateChooser ngayNhap=new JDateChooser();ngayNhap.setDateFormatString("dd/MM/yyyy");ngayNhap.setDate(new Date());
        JComboBox<TrangThaiPhieuNhapKho> trangThai=new JComboBox<>(TrangThaiPhieuNhapKho.values());
        if(sua){
            ncc.setSelectedItem(cu.getMaNCC());loc.run();
            ngayNhap.setDate(Date.from(cu.getNgayNhap().atZone(ZoneId.systemDefault()).toInstant()));
            lyDo.setText(cu.getLyDoNhap()==null?"":cu.getLyDoNhap());trangThai.setSelectedItem(cu.getTrangThai());
            List<ChiTietPhieuNhapKho> ds=dao.getChiTietByPhieu(cu);
            if(!ds.isEmpty()){sp.setSelectedItem(ds.get(0).getMaSanPham());soLuong.setText(String.valueOf(ds.get(0).getSoLuong()));donGia.setText(String.valueOf(ds.get(0).getDonGia()));}
        }
        dong(form,g,0,"Mã phiếu nhập",ma);dong(form,g,1,"Nhà cung cấp *",ncc);dong(form,g,2,"Sản phẩm nhập *",sp);
        dong(form,g,3,"Số lượng *",soLuong);dong(form,g,4,"Đơn giá nhập *",donGia);dong(form,g,5,"Ngày nhập *",ngayNhap);
        dong(form,g,6,"Lý do nhập",lyDo);dong(form,g,7,"Trạng thái",trangThai);
        JButton save=nut(sua?"Lưu thay đổi":"Lưu phiếu nhập");
        save.addActionListener(e->{
            try{
                NhaCungCap x=(NhaCungCap)ncc.getSelectedItem();SanPham y=(SanPham)sp.getSelectedItem();
                if(nhanVienDangNhap==null||x==null||y==null||ngayNhap.getDate()==null){JOptionPane.showMessageDialog(d,"Vui lòng chọn đầy đủ nhà cung cấp, sản phẩm và ngày nhập.");return;}
                int sl=Integer.parseInt(soLuong.getText().trim());double gia=Double.parseDouble(donGia.getText().trim());
                if(sl<=0||gia<=0){JOptionPane.showMessageDialog(d,"Số lượng và đơn giá phải lớn hơn 0.");return;}
                LocalDateTime date=ngayNhap.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
                PhieuNhapKho p=new PhieuNhapKho(ma.getText(),date,nhanVienDangNhap,x,lyDo.getText().trim(),(TrangThaiPhieuNhapKho)trangThai.getSelectedItem());
                List<ChiTietPhieuNhapKho> details=new ArrayList<>();details.add(new ChiTietPhieuNhapKho(p,y,sl,gia));
                boolean ok=sua?dao.updateWithDetails(p,details):dao.insertWithDetails(p,details);
                if(ok){JOptionPane.showMessageDialog(d,sua?"Cập nhật phiếu nhập kho thành công!":"Lập phiếu nhập kho thành công!");d.dispose();taiDuLieu();}
                else JOptionPane.showMessageDialog(d,"Không lưu được phiếu. "+dao.getLastError(),"Lỗi",JOptionPane.ERROR_MESSAGE);
            }catch(NumberFormatException ex){JOptionPane.showMessageDialog(d,"Số lượng và đơn giá phải là số hợp lệ.");}
        });
        JPanel bottom=new JPanel(new FlowLayout(FlowLayout.RIGHT));bottom.setBackground(Color.WHITE);bottom.add(save);
        d.add(form,BorderLayout.CENTER);d.add(bottom,BorderLayout.SOUTH);d.setSize(700,500);d.setResizable(false);d.setLocationRelativeTo(this);d.setVisible(true);
    }
    private void xoa(){PhieuNhapKho p=chon();if(p==null)return;
        if (p.getTrangThai() != TrangThaiPhieuNhapKho.CHO_XAC_NHAN) {
            JOptionPane.showMessageDialog(this, "Chỉ được xóa phiếu đang chờ xác nhận.");
            return;
        }
        int c=JOptionPane.showConfirmDialog(this,"Bạn có chắc muốn xóa phiếu "+p.getMaPhieuNhapKho()+"?","Xác nhận",JOptionPane.YES_NO_OPTION);if(c==JOptionPane.YES_OPTION){if(dao.delete(p)){JOptionPane.showMessageDialog(this,"Đã xóa phiếu nhập.");taiDuLieu();}else JOptionPane.showMessageDialog(this,"Xóa không thành công. Có thể phiếu đang được bảng khác tham chiếu.");}}
    private void dong(JPanel p,GridBagConstraints g,int r,String label,JComponent c){g.gridy=r;g.gridx=0;g.weightx=0;p.add(new JLabel(label),g);g.gridx=1;g.weightx=1;c.setPreferredSize(new Dimension(440,28));p.add(c,g);}
    private JButton nut(String t){JButton b=new JButton(t);b.setBackground(new Color(14,98,195));b.setForeground(Color.WHITE);b.setFocusPainted(false);return b;}
}
