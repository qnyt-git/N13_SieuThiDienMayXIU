package ENUM;

public enum TrangThaiPhieuBaoHanh {
//	Nhánh 2: Sản phẩm bảo hành chính hãng (Gửi đi hãng)
//	- khi lập phiếu bảo hành     					➔Trạng thái 1: TIẾP NHẬN
//	Button: [Gửi về hãng] 							➔ Trạng thái 2: Sản phẩm đã được gửi về hãng
//	Button: [Nhận SPBH từ hãng] 					➔ Trạng thái 3: SPBH đã xong - chờ khách nhận
//	Button: [Hoàn tất] 								➔ Trạng thái 4: Đã trả - hoàn tất bảo hành
//	
//	Nhánh 3: Ngoài phạm vi bảo hành (Tính phí hoặc Hủy)
//	- khi lập phiếu bảo hành     						➔Trạng thái 1: TIẾP NHẬN
//	Button: [Bảo hành tính phí]                			➔ Trạng thái 2: Bảo hành có tính phí (chờ sửa xong)
//	Sửa xong giao khách, bấm [Hoàn tất] 				➔ Trạng thái 3: Đã trả - hoàn tất bảo hành
//	Hoặc khách không chịu tốn phí, bấm [Hủy] 			➔ Trạng thái: Đã hủy bảo hành (trả máy nguyên trạng cho khách).
	
	// do có bảo hành có tính phí nên sẽ có "phương thức tính phí bảo hành" và ta sẽ làm theo nghiệp vụ
	// 1. kiểm tra sản phẩm "bảo hành tính phí" xem sản phẩm sửa hết nhiêu tiền -> sau đó báo cho khách
	// khách đồng ý -> tiếp tục qui trình
	// khách từ chối -> hủy phiếu và trả hàng
	
	
	TIEP_NHAN("Tiếp nhận sản phẩm từ khách hàng"),
    DA_GUI_HANG("Sản phẩm đã được gửi về hãng"),
    CHO_KHACH_NHAN_TU_HANG("SPBH đã xong - chờ khách nhận"),
    BAO_HANH_TINH_PHI("Bảo hành có tính phí"),
    HOAN_TAT("Đã trả - hoàn tất bảo hành"),
    DA_HUY("Đã hủy bảo hành");

    private final String moTa;

    TrangThaiPhieuBaoHanh(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}