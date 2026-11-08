package GUI;

import BUS.HoaDonBUS;
import ENTITY.HoaDon;
import ENTITY.ChiTietHoaDon;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class LichSuMuaHangGUI extends JPanel {
    private final HoaDonBUS hoaDonBUS = new HoaDonBUS();
    private DefaultTableModel modelLichSu;
    private JTable tblLichSu;
    private JTextField txtTimKiemKH;
    private List<HoaDon> danhSachHoaDon;

    public LichSuMuaHangGUI() {
        setLayout(new BorderLayout(0, 12)); setBackground(new Color(245,247,250));
        setBorder(new EmptyBorder(18,22,18,22));
        JPanel top = new JPanel(new BorderLayout(0,10)); top.setOpaque(false);
        JLabel title = new JLabel("Tra cứu lịch sử mua hàng của khách hàng");
        title.setFont(new Font("Arial",Font.BOLD,20)); title.setForeground(new Color(20,40,70));
        top.add(title,BorderLayout.NORTH);
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0)); search.setOpaque(false);
        txtTimKiemKH = new JTextField(24); txtTimKiemKH.setPreferredSize(new Dimension(240,32));
        JButton btnTim = taoNut("Tìm kiếm lịch sử",new Color(14,98,195),Color.WHITE);
        JButton btnChiTiet = taoNut("Xem chi tiết",new Color(22,145,90),Color.WHITE);
        search.add(new JLabel("Mã/Tên khách hàng hoặc SĐT:")); search.add(txtTimKiemKH); search.add(btnTim); search.add(btnChiTiet);
        top.add(search,BorderLayout.CENTER); add(top,BorderLayout.NORTH);
        modelLichSu = new DefaultTableModel(new Object[]{"Mã hóa đơn","Khách hàng","Nhân viên lập","Ngày lập","Phương thức TT","Trạng thái"},0) { public boolean isCellEditable(int r,int c){return false;} };
        tblLichSu = new JTable(modelLichSu); tblLichSu.setRowHeight(28); tblLichSu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tblLichSu),BorderLayout.CENTER);
        btnTim.addActionListener(e -> taiDuLieu()); btnChiTiet.addActionListener(e -> xemChiTiet());
        tblLichSu.addMouseListener(new java.awt.event.MouseAdapter(){ public void mouseClicked(java.awt.event.MouseEvent e){ if(e.getClickCount()==2) xemChiTiet(); }});
        taiDuLieu();
    }
    private void taiDuLieu(){
        modelLichSu.setRowCount(0); danhSachHoaDon=hoaDonBUS.getAll();
        String key=txtTimKiemKH.getText().trim().toLowerCase();
        for(HoaDon hd:danhSachHoaDon){
            String ten=hd.getMaKhachHang()!=null?hd.getMaKhachHang().getHoTenKhachHang():"";
            String sdt=hd.getMaKhachHang()!=null?hd.getMaKhachHang().getSoDienThoai():"";
            if((hd.getMaHoaDon()+" "+ten+" "+sdt).toLowerCase().contains(key)) modelLichSu.addRow(new Object[]{hd.getMaHoaDon(),ten+" ("+sdt+")",hd.getMaNhanVien()!=null?hd.getMaNhanVien().getHoTenNhanVien():"",hd.getNgayLap(),hd.getPhuongThucThanhToan(),hd.getTrangThai()!=null?hd.getTrangThai().getMoTa():""});
        }
    }
    private void xemChiTiet(){
        int row=tblLichSu.getSelectedRow();
        if(row<0){JOptionPane.showMessageDialog(this,"Vui lòng chọn một hóa đơn.");return;}
        String ma=String.valueOf(modelLichSu.getValueAt(tblLichSu.convertRowIndexToModel(row),0));
        HoaDon hoaDon=null; for(HoaDon hd:danhSachHoaDon) if(hd.getMaHoaDon().equals(ma)){hoaDon=hd;break;}
        if(hoaDon==null)return;
        List<ChiTietHoaDon> ds=hoaDonBUS.getChiTietByHoaDon(hoaDon);
        String tenKH=hoaDon.getMaKhachHang()!=null?hoaDon.getMaKhachHang().getHoTenKhachHang():"";
        NumberFormat nf=NumberFormat.getNumberInstance(new Locale("vi","VN"));
        JPanel panel=new JPanel(new BorderLayout(8,8));
        panel.add(new JLabel("Hóa đơn: "+ma+"     Khách hàng: "+tenKH+"     Ngày lập: "+hoaDon.getNgayLap()),BorderLayout.NORTH);
        DefaultTableModel model=new DefaultTableModel(new Object[]{"Mã SP","Tên sản phẩm","Số lượng","Đơn giá","VAT","Thành tiền"},0){public boolean isCellEditable(int r,int c){return false;}};
        double tong=0; for(ChiTietHoaDon ct:ds){double thanhTien=ct.getSoLuong()*ct.getDonGia()*(1+ct.getThueVAT());tong+=thanhTien;model.addRow(new Object[]{ct.getMaSanPham().getMaSanPham(),ct.getMaSanPham().getTenSanPham(),ct.getSoLuong(),nf.format(ct.getDonGia()),ct.getThueVAT(),nf.format(thanhTien)});}
        JTable table=new JTable(model);table.setRowHeight(26);panel.add(new JScrollPane(table),BorderLayout.CENTER);
        panel.add(new JLabel("Tổng tiền chi tiết (đã tính VAT): "+nf.format(tong)+" VNĐ"),BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(850,350));
        JOptionPane.showMessageDialog(this,panel,"Chi tiết hóa đơn",JOptionPane.INFORMATION_MESSAGE);
    }
    private JButton taoNut(String text,Color bg,Color fg){JButton b=new JButton(text);b.setBackground(bg);b.setForeground(fg);b.setFocusPainted(false);return b;}
}
