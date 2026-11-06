USE master;
GO

IF EXISTS (SELECT name FROM sys.databases WHERE name = 'N13_XIU_DB')
BEGIN
    ALTER DATABASE N13_XIU_DB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE N13_XIU_DB;
END
GO

CREATE DATABASE N13_XIU_DB;
GO
USE N13_XIU_DB;
GO

-----------------------------------------------------------------
-- PHẦN 1: TẠO TABLE
-----------------------------------------------------------------

CREATE TABLE CaLamViec (
    maCa VARCHAR(20) PRIMARY KEY,
    tenCa NVARCHAR(50) NOT NULL,
    gioBatDau DATETIME2 NOT NULL,
    gioKetThuc DATETIME2 NOT NULL
);
GO

CREATE TABLE ChucVu (
    maChucVu VARCHAR(20) PRIMARY KEY,
    tenChucVu NVARCHAR(100) NOT NULL UNIQUE,
    viTriLamViec NVARCHAR(100) NOT NULL,
    moTaCongViec NVARCHAR(255),
    trangThai BIT NOT NULL DEFAULT 1
);
GO

CREATE TABLE NhaCungCap (
    maNCC VARCHAR(20) PRIMARY KEY,
    tenNCC NVARCHAR(100) NOT NULL,
    tenThuongHieu NVARCHAR(100),
    diaChi NVARCHAR(255),
    soDienThoai VARCHAR(20),
    email VARCHAR(100),
    trangThai BIT NOT NULL
);
GO

CREATE TABLE KhachHang (
    maKhachHang VARCHAR(20) PRIMARY KEY,
    hoTenKhachHang NVARCHAR(100) NOT NULL,
    gioiTinh BIT NOT NULL,
    soDienThoai VARCHAR(20) NOT NULL,
    diemTichLuy INT DEFAULT 0,
    LoaiKH NVARCHAR(50) DEFAULT N'Khách mới'
);
GO

CREATE TABLE NhanVien (
    maNhanVien VARCHAR(20) PRIMARY KEY,
    hoTenNhanVien NVARCHAR(100) NOT NULL,
    gioiTinh BIT NOT NULL,
    soDienThoai VARCHAR(20) NOT NULL,
    ngaySinh DATE NOT NULL,
    trinhDoHocVan NVARCHAR(100),
    maChucVu VARCHAR(20) NOT NULL,
    heSoLuong FLOAT NOT NULL,
    luongCoBan INT NOT NULL,
    trangThai BIT NOT NULL,
    CONSTRAINT FK_NhanVien_ChucVu FOREIGN KEY (maChucVu) REFERENCES ChucVu(maChucVu)
);
GO

CREATE TABLE KhuyenMai (
    maKhuyenMai VARCHAR(20) PRIMARY KEY,
    tenKhuyenMai NVARCHAR(100) NOT NULL,
    thoiGianBatDau DATETIME2 NOT NULL,
    thoiGianKetThuc DATETIME2 NOT NULL,
    giaTriGiam DECIMAL(18,2) NOT NULL,
    trangThai BIT NOT NULL
);
GO

CREATE TABLE SanPham (
    maSanPham VARCHAR(20) PRIMARY KEY,
    tenSanPham NVARCHAR(200) NOT NULL,
    maNCC VARCHAR(20) NOT NULL,
    giaBan DECIMAL(18,2) NOT NULL,
    soLuongTon INT NOT NULL,
    thoiHanBaoHanh NVARCHAR(50),
    moTaSanPham NVARCHAR(MAX),
    trangThai VARCHAR(50) NOT NULL,
    CONSTRAINT FK_SanPham_NCC FOREIGN KEY (maNCC) REFERENCES NhaCungCap(maNCC)
);
GO

CREATE TABLE TaiKhoanNhanVien (
    maTaiKhoan VARCHAR(20) PRIMARY KEY,
    tenDangNhap VARCHAR(50) NOT NULL UNIQUE,
    maNhanVien VARCHAR(20) NOT NULL UNIQUE,
    matKhau VARCHAR(100) NOT NULL,
    loaiQuyen VARCHAR(50) NOT NULL,
    trangThai BIT NOT NULL,
    CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
);
GO

CREATE TABLE PhanCa (
    maPhanCa VARCHAR(20) PRIMARY KEY,
    maNhanVien VARCHAR(20) NOT NULL,
    maCa VARCHAR(20) NOT NULL,
    ngayLam DATE NOT NULL,
    trangThai VARCHAR(50) NOT NULL,
    CONSTRAINT FK_PhanCa_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
    CONSTRAINT FK_PhanCa_CaLamViec FOREIGN KEY (maCa) REFERENCES CaLamViec(maCa)
);
GO

CREATE TABLE PhieuNhapKho (
    maPhieuNhapKho VARCHAR(20) PRIMARY KEY,
    ngayNhap DATETIME2 NOT NULL,
    maNhanVien VARCHAR(20) NOT NULL,
    maNCC VARCHAR(20) NOT NULL,
    lyDoNhap NVARCHAR(255),
    trangThai VARCHAR(50) NOT NULL,
    CONSTRAINT FK_PhieuNhap_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
    CONSTRAINT FK_PhieuNhap_NCC FOREIGN KEY (maNCC) REFERENCES NhaCungCap(maNCC)
);
GO

CREATE TABLE PhieuDatHang (
    maPhieuDatHang VARCHAR(20) PRIMARY KEY,
    maKhachHang VARCHAR(20) NOT NULL,
    maNhanVien VARCHAR(20) NOT NULL,
    ngayDatHang DATETIME2 NOT NULL,
    ghiChu NVARCHAR(255),
    trangThai VARCHAR(50) NOT NULL,
    CONSTRAINT FK_PhieuDat_KhachHang FOREIGN KEY (maKhachHang) REFERENCES KhachHang(maKhachHang),
    CONSTRAINT FK_PhieuDat_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
);
GO

CREATE TABLE PhieuBaoHanh (
    maPhieuBaoHanh VARCHAR(20) PRIMARY KEY,
    maKhachHang VARCHAR(20) NOT NULL,
    maNhanVien VARCHAR(20) NOT NULL,
    ngayTiepNhan DATETIME2 NOT NULL,
    ngayHenTra DATETIME2 NOT NULL,
    lyDoHuy NVARCHAR(255),
    trangThai VARCHAR(50) NOT NULL,
    CONSTRAINT FK_PBH_KhachHang FOREIGN KEY (maKhachHang) REFERENCES KhachHang(maKhachHang),
    CONSTRAINT FK_PBH_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
);
GO

CREATE TABLE ChiTietPhieuNhapKho (
    maPhieuNhapKho VARCHAR(20) NOT NULL,
    maSanPham VARCHAR(20) NOT NULL,
    soLuong INT NOT NULL,
    donGia DECIMAL(18,2) NOT NULL,
    PRIMARY KEY (maPhieuNhapKho, maSanPham),
    CONSTRAINT FK_CTPNK_Phieu FOREIGN KEY (maPhieuNhapKho) REFERENCES PhieuNhapKho(maPhieuNhapKho),
    CONSTRAINT FK_CTPNK_SanPham FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham)
);
GO

CREATE TABLE ChiTietPhieuDatHang (
    maPhieuDatHang VARCHAR(20) NOT NULL,
    maSanPham VARCHAR(20) NOT NULL,
    soLuong INT NOT NULL,
    donGia DECIMAL(18,2) NOT NULL,
    PRIMARY KEY (maPhieuDatHang, maSanPham),
    CONSTRAINT FK_CTPDH_Phieu FOREIGN KEY (maPhieuDatHang) REFERENCES PhieuDatHang(maPhieuDatHang),
    CONSTRAINT FK_CTPDH_SanPham FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham)
);
GO

CREATE TABLE HoaDon (
    maHoaDon VARCHAR(20) PRIMARY KEY,
    maPhieuDatHang VARCHAR(20) NULL,
    maKhachHang VARCHAR(20) NOT NULL,
    maNhanVien VARCHAR(20) NOT NULL,
    maNCC VARCHAR(20) NOT NULL,
    maKhuyenMai VARCHAR(20) NULL,
    phuongThucThanhToan VARCHAR(50) NOT NULL,
    ngayLap DATETIME2 NOT NULL,
    ghiChu NVARCHAR(255),
    trangThai VARCHAR(50) NOT NULL,
    CONSTRAINT FK_HD_PhieuDat FOREIGN KEY (maPhieuDatHang) REFERENCES PhieuDatHang(maPhieuDatHang),
    CONSTRAINT FK_HD_KhachHang FOREIGN KEY (maKhachHang) REFERENCES KhachHang(maKhachHang),
    CONSTRAINT FK_HD_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
    CONSTRAINT FK_HD_NCC FOREIGN KEY (maNCC) REFERENCES NhaCungCap(maNCC),
    CONSTRAINT FK_HD_KhuyenMai FOREIGN KEY (maKhuyenMai) REFERENCES KhuyenMai(maKhuyenMai)
);
GO

CREATE TABLE ChiTietHoaDon (
    maHoaDon VARCHAR(20) NOT NULL,
    maSanPham VARCHAR(20) NOT NULL,
    soLuong INT NOT NULL,
    donGia DECIMAL(18,2) NOT NULL,
    thueVAT FLOAT NOT NULL,
    PRIMARY KEY (maHoaDon, maSanPham),
    CONSTRAINT FK_CTHD_HoaDon FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon),
    CONSTRAINT FK_CTHD_SanPham FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham)
);
GO

CREATE TABLE ChiTietPhieuBaoHanh (
    maPhieuBaoHanh VARCHAR(20) NOT NULL,
    maSanPham VARCHAR(20) NOT NULL,
    tinhTrangSanPham NVARCHAR(255),
    noiDungBaoHanh NVARCHAR(255),
    PRIMARY KEY (maPhieuBaoHanh, maSanPham),
    CONSTRAINT FK_CTPBH_Phieu FOREIGN KEY (maPhieuBaoHanh) REFERENCES PhieuBaoHanh(maPhieuBaoHanh),
    CONSTRAINT FK_CTPBH_SanPham FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham)
);
GO

-----------------------------------------------------------------
-- PHẦN 2: THÊM DỮ LIỆU MẪU
-----------------------------------------------------------------

INSERT INTO CaLamViec VALUES 
('CA01', N'Ca Sáng', '2026-06-01 08:00:00', '2026-06-01 12:00:00'),
('CA02', N'Ca Chiều', '2026-06-01 13:00:00', '2026-06-01 17:00:00'),
('CA03', N'Ca Tối', '2026-06-01 18:00:00', '2026-06-01 22:00:00'),
('CA04', N'Ca Đêm', '2026-06-01 22:00:00', '2026-06-02 06:00:00'),
('CA05', N'Ca Hành Chính', '2026-06-01 08:00:00', '2026-06-01 17:00:00');
GO

INSERT INTO ChucVu VALUES 
('CV01', N'Quản lý', N'Văn phòng điều hành', N'Quản lý nhân sự, duyệt phiếu nhập kho, phân ca và toàn bộ hệ thống', 1),
('CV02', N'Nhân viên bán hàng', N'Quầy trưng bày & Thu ngân', N'Tư vấn khách hàng, lập phiếu đặt hàng, lập hóa đơn và thu tiền', 1),
('CV03', N'Nhân viên kho', N'Kho hàng trung tâm', N'Kiểm kê hàng tồn, lập phiếu nhập kho và sắp xếp hàng hóa', 1),
('CV04', N'Nhân viên kế toán', N'Văn phòng điều hành', N'Đối soát hóa đơn, quản lý thu chi và lập báo cáo thống kê doanh thu', 1),
('CV05', N'Nhân viên CSKH', N'Quầy dịch vụ & Bảo hành', N'Tiếp nhận sản phẩm lỗi, lập phiếu bảo hành và liên hệ hãng sửa chữa', 1);
GO

INSERT INTO NhaCungCap VALUES 
('NCC01', N'Công ty TNHH Panasonic Việt Nam', N'Panasonic', N'Hà Nội', '0901111111', 'panasonic@vn.com', 1),
('NCC02', N'Công ty CP Acer Việt Nam', N'Acer', N'TP.HCM', '0902222222', 'acer@vn.com', 1),
('NCC03', N'Công ty TNHH Samsung Electronics', N'Samsung', N'Bắc Ninh', '0903333333', 'samsung@vn.com', 1),
('NCC04', N'Công ty CP Điện máy LG', N'LG', N'Hải Phòng', '0904444444', 'lg@vn.com', 1),
('NCC05', N'Công ty TNHH Toshiba Việt Nam', N'Toshiba', N'TP.HCM', '0905555555', 'toshiba@vn.com', 1);
GO

INSERT INTO KhachHang VALUES 
('KH01', N'Nguyễn Văn An', 1, '0911111111', 100, N'Khách mới'),
('KH02', N'Trần Thị Bình', 0, '0912222222', 500, N'Khách VIP'),
('KH03', N'Lê Văn Cường', 1, '0913333333', 50, N'Khách mới'),
('KH04', N'Phạm Thị Dung', 0, '0914444444', 1200, N'Khách VIP'),
('KH05', N'Hoàng Văn Em', 1, '0915555555', 200, N'Khách mới');
GO

INSERT INTO NhanVien VALUES 
('NV01', N'Trần Văn Quản', 1, '0981111111', '1995-05-10', N'Đại học', 'CV01', 3.0, 15000000, 1),
('NV02', N'Lê Thị Bích', 0, '0982222222', '1998-08-15', N'Cao đẳng', 'CV02', 2.2, 8000000, 1),
('NV03', N'Nguyễn Văn Chung', 1, '0983333333', '2000-12-20', N'Trung cấp', 'CV03', 2.1, 7500000, 1),
('NV04', N'Phạm Thị Đào', 0, '0984444444', '1997-03-25', N'Đại học', 'CV04', 2.5, 10000000, 1),
('NV05', N'Hoàng Văn Nam', 1, '0985555555', '1999-07-30', N'Cao đẳng', 'CV05', 2.2, 8000000, 1);
GO

INSERT INTO KhuyenMai VALUES 
('KM01', N'Khuyến mãi khai trương', '2026-06-01 00:00:00', '2026-06-30 23:59:59', 0.10, 1),
('KM02', N'Giảm giá mùa hè', '2026-07-01 00:00:00', '2026-07-31 23:59:59', 500000.00, 1),
('KM03', N'Tri ân khách hàng', '2026-08-01 00:00:00', '2026-08-15 23:59:59', 0.15, 0),
('KM04', N'Siêu sale cuối tuần', '2026-06-05 00:00:00', '2026-06-07 23:59:59', 0.20, 1),
('KM05', N'Mừng Quốc Khánh', '2026-09-01 00:00:00', '2026-09-05 23:59:59', 200000.00, 0);
GO

INSERT INTO SanPham VALUES 
('SP01', N'Tủ lạnh Panasonic Inverter', 'NCC01', 12000000, 15, N'2 năm', N'Tủ lạnh tiết kiệm điện', 'SAN_PHAM_MOI'),
('SP02', N'Laptop Acer Aspire 5', 'NCC02', 15500000, 10, N'1 năm', N'Laptop văn phòng', 'DANG_BAN'),
('SP03', N'Tivi Samsung 55 inch', 'NCC03', 18000000, 8, N'2 năm', N'Tivi 4K sắc nét', 'DANG_BAN'),
('SP04', N'Máy giặt LG Inverter', 'NCC04', 9500000, 12, N'2 năm', N'Máy giặt cửa ngang', 'SAN_PHAM_MOI'),
('SP05', N'Lò vi sóng Toshiba', 'NCC05', 2500000, 20, N'1 năm', N'Lò vi sóng có nướng', 'HET_HANG');
GO

INSERT INTO TaiKhoanNhanVien VALUES 
('TK01', 'admin01', 'NV01', '123456', 'QUAN_LY', 1),
('TK02', 'banhang01', 'NV02', '123456', 'BAN_HANG', 1),
('TK03', 'kho01', 'NV03', '123456', 'KHO', 1),
('TK04', 'ketoan01', 'NV04', '123456', 'KE_TOAN', 1),
('TK05', 'cskh01', 'NV05', '123456', 'CSKH', 1);
GO

INSERT INTO PhanCa VALUES 
('PC01', 'NV01', 'CA01', '2026-06-06', 'Đã phân ca'),
('PC02', 'NV02', 'CA02', '2026-06-06', 'Đã phân ca'),
('PC03', 'NV03', 'CA01', '2026-06-06', 'Đã làm'),
('PC04', 'NV04', 'CA02', '2026-06-06', 'Đã làm'),
('PC05', 'NV05', 'CA03', '2026-06-06', 'Nghỉ');
GO

INSERT INTO PhieuNhapKho VALUES 
('PNK01', '2026-06-01 09:00:00', 'NV03', 'NCC01', N'Nhập thêm tủ lạnh', 'Đã xác nhận'),
('PNK02', '2026-06-02 09:00:00', 'NV03', 'NCC02', N'Nhập thêm laptop', 'Đã xác nhận'),
('PNK03', '2026-06-03 09:00:00', 'NV03', 'NCC03', N'Nhập thêm tivi', 'Chờ xác nhận'),
('PNK04', '2026-06-04 09:00:00', 'NV03', 'NCC04', N'Nhập thêm máy giặt', 'Phiếu đã hủy'),
('PNK05', '2026-06-05 09:00:00', 'NV03', 'NCC05', N'Nhập thêm lò vi sóng', 'Đã xác nhận');
GO

INSERT INTO PhieuDatHang VALUES 
('PDH01', 'KH01', 'NV02', '2026-06-06 10:00:00', N'Giao giờ hành chính', 'Chờ xử lý (chờ thanh toán)'),
('PDH02', 'KH02', 'NV02', '2026-06-06 11:00:00', N'Khách gọi điện đặt', 'Đã thanh toán'),
('PDH03', 'KH03', 'NV02', '2026-06-06 12:00:00', N'Giao gấp', 'Đã hủy'),
('PDH04', 'KH04', 'NV02', '2026-06-06 13:00:00', N'Để tại cửa hàng', 'Chờ xử lý (chờ thanh toán)'),
('PDH05', 'KH05', 'NV02', '2026-06-06 14:00:00', N'Giao buổi chiều', 'Đã thanh toán');
GO

INSERT INTO PhieuBaoHanh VALUES 
('PBH01', 'KH01', 'NV05', '2026-06-06 08:30:00', '2026-06-13 08:30:00', NULL, 'Tiếp nhận sản phẩm từ khách hàng'),
('PBH02', 'KH02', 'NV05', '2026-06-06 09:00:00', '2026-06-10 09:00:00', NULL, 'Sản phẩm đã được gửi về hãng'),
('PBH03', 'KH03', 'NV05', '2026-06-06 09:30:00', '2026-06-08 09:30:00', NULL, 'SPBH đã xong - chờ khách nhận'),
('PBH04', 'KH04', 'NV05', '2026-06-06 10:00:00', '2026-06-15 10:00:00', NULL, 'Bảo hành có tính phí'),
('PBH05', 'KH05', 'NV05', '2026-06-06 10:30:00', '2026-06-12 10:30:00', N'Khách không chịu phí', 'Đã hủy bảo hành');
GO

INSERT INTO ChiTietPhieuNhapKho VALUES 
('PNK01', 'SP01', 5, 11000000),
('PNK02', 'SP02', 4, 14000000),
('PNK03', 'SP03', 3, 17000000),
('PNK04', 'SP04', 6, 9000000),
('PNK05', 'SP05', 10, 2300000);
GO

INSERT INTO ChiTietPhieuDatHang VALUES 
('PDH01', 'SP01', 1, 12000000),
('PDH02', 'SP02', 1, 15500000),
('PDH03', 'SP03', 1, 18000000),
('PDH04', 'SP04', 2, 9500000),
('PDH05', 'SP05', 3, 2500000);
GO

INSERT INTO HoaDon VALUES 
('HD01', 'PDH02', 'KH02', 'NV02', 'NCC01', 'KM01', 'Tiền mặt', '2026-06-06 11:30:00', N'Đã thanh toán đủ', 'Đã thanh toán'),
('HD02', 'PDH05', 'KH05', 'NV02', 'NCC02', 'KM02', 'Chuyển khoản ngân hàng', '2026-06-06 14:30:00', N'Thanh toán qua Momo', 'Đã thanh toán'),
('HD03', NULL, 'KH01', 'NV02', 'NCC03', NULL, 'Tiền mặt', '2026-06-06 15:00:00', N'Mua trực tiếp tại quầy', 'Đã thanh toán'),
('HD04', NULL, 'KH03', 'NV02', 'NCC04', 'KM04', 'Thẻ ngân hàng (ATM/Visa/Master)', '2026-06-06 15:30:00', N'Quẹt thẻ POS', 'Chờ xử lý (chờ thanh toán)'),
('HD05', NULL, 'KH04', 'NV02', 'NCC05', NULL, 'Ví điện tử (MoMo/ShopeePay)', '2026-06-06 16:00:00', N'Thanh toán chuyển khoản', 'Đã thanh toán');
GO

INSERT INTO ChiTietHoaDon VALUES 
('HD01', 'SP02', 1, 15500000, 0.10),
('HD02', 'SP05', 3, 2500000, 0.08),
('HD03', 'SP01', 1, 12000000, 0.10),
('HD04', 'SP04', 2, 9500000, 0.10),
('HD05', 'SP03', 1, 18000000, 0.10);
GO

INSERT INTO ChiTietPhieuBaoHanh VALUES 
('PBH01', 'SP01', N'Lỗi nguồn', N'Kiểm tra bo mạch chính'),
('PBH02', 'SP02', N'Hỏng bàn phím', N'Thay bàn phím mới'),
('PBH03', 'SP03', N'Mất nguồn', N'Sửa chữa nguồn'),
('PBH04', 'SP04', N'Kẹt lồng giặt', N'Vệ sinh và sửa lồng'),
('PBH05', 'SP05', N'Không nóng', N'Thay cầu chì nhiệt');
GO