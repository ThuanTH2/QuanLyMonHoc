package com.example.springdatajpa.controller;

import com.example.springdatajpa.entity.*;
import com.example.springdatajpa.repository.*;
import com.example.springdatajpa.service.AccountService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/diemthi")
public class DiemThiController {

    @Autowired
    private DiemThiRepository diemThiRepository;

    @Autowired
    private LopHocRepository lopHocRepository;

    @Autowired
    LopHocSinhVienRepository lopHocSinhVienRepository;

    @Autowired
    private GiangVienRepository giangVienRepository;

    @Autowired
    SinhVienRepository sinhVienRepository;

    @Autowired
    private AccountService accountService;

    @GetMapping("/list")
    public String list(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();

        Account.VaiTro currentRole = accountService.getCurrentUserRole(authentication);

        if (currentRole == Account.VaiTro.STUDENT) {
            // Sinh viên chỉ xem điểm của các lớp học mình đang học
            SinhVien sinhVien = sinhVienRepository.findByAccount_Email(currentPrincipalEmail);
            List<DiemThi> diemThiList = diemThiRepository.findByLopHocSinhVien_SinhVien(sinhVien);
            model.addAttribute("diemThiList", diemThiList);
            return "diemthi/student";
        } else if (currentRole == Account.VaiTro.LECTURER) {
            // Giảng viên chỉ xem và nhập điểm của các lớp mà họ dạy
            GiangVien giangVien = giangVienRepository.findByAccount_Email(currentPrincipalEmail);
            List<LopHoc> lopHocList = lopHocRepository.findByGiangVien(giangVien);
            model.addAttribute("lopHocList", lopHocList);
            return "diemthi/listLopHocxem";
        } else if (currentRole == Account.VaiTro.ADMIN) {
            // Admin có thể xem tất cả các điểm, chọn lớp học để xem điểm
            List<LopHoc> lopHocList = lopHocRepository.findAll();
            model.addAttribute("lopHocList", lopHocList);
            return "diemthi/listLopHocxem";
        }

        return "redirect:/home";
    }
    @GetMapping("/list/{maLopHoc}")
    public String listDiemByClass(@PathVariable("maLopHoc") Integer maLopHoc, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();

        Account.VaiTro currentRole = accountService.getCurrentUserRole(authentication);

        if (currentRole == Account.VaiTro.LECTURER || currentRole == Account.VaiTro.ADMIN) {
            List<DiemThi> diemThiList = diemThiRepository.findByLopHocSinhVien_LopHoc_MaLopHoc(maLopHoc);
            model.addAttribute("diemThiList", diemThiList);
            return "diemthi/listDiem";
        }

        else{
            return "redirect:/home/student";

        }
    }




    @GetMapping("/nhapdiem/{maLopHoc}")
    public String nhapDiem(@PathVariable("maLopHoc") Integer maLopHoc, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account.VaiTro currentRole = accountService.getCurrentUserRole(authentication);

        GiangVien giangVien = giangVienRepository.findByAccount_Email(authentication.getName());
        LopHoc lopHoc = lopHocRepository.findById(maLopHoc).orElse(null);
        if (lopHoc != null && (lopHoc.getGiangVien().equals(giangVien) || currentRole == Account.VaiTro.ADMIN)) {
            List<LopHocSinhVien> lopHocSinhViens = lopHocSinhVienRepository.findByLopHoc(lopHoc);
            model.addAttribute("lopHocSinhViens", lopHocSinhViens);
            return "diemthi/nhapDiem";
        }
        else{
            return "redirect:/home/student";

        }
    }

    @PostMapping("/nhapdiem/{maLopHoc}")
    public String nhapDiem(@PathVariable("maLopHoc") Integer maLopHoc,
                           @RequestParam("diemQuaTrinh") List<Float> diemQuaTrinhList,
                           @RequestParam("diemCuoiKy") List<Float> diemCuoiKyList, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account.VaiTro currentRole = accountService.getCurrentUserRole(authentication);


        LopHoc lopHoc = lopHocRepository.findById(maLopHoc).orElse(null);



        // Validate if the lecturer or admin has access to this class
        GiangVien giangVien = giangVienRepository.findByAccount_Email(authentication.getName());
        if (!lopHoc.getGiangVien().equals(giangVien) && currentRole != Account.VaiTro.ADMIN) {
            return "redirect:/home/student";
        }

        List<LopHocSinhVien> lopHocSinhViens = lopHocSinhVienRepository.findByLopHoc(lopHoc);


        // Process each student's grade submission
        for (int i = 0; i < lopHocSinhViens.size(); i++) {
            LopHocSinhVien lopHocSinhVien = lopHocSinhViens.get(i);

            DiemThi diemThi = diemThiRepository.findByLopHocSinhVien(lopHocSinhVien);
            if (diemThi != null && diemThi.getKhoa()) {
                // Nếu điểm đã khóa, bỏ qua cập nhật
                continue;
            }
            if (diemThi == null) {
                diemThi = new DiemThi();
                diemThi.setLopHocSinhVien(lopHocSinhVien);
                diemThi.setDiemQuaTrinh(diemQuaTrinhList.get(i));
                diemThi.setDiemCuoiKy(diemCuoiKyList.get(i));
                diemThi.setNgayCapNhat(new Date());
                diemThi.setKhoa(false);
                diemThiRepository.save(diemThi);
            }

        }

        return "redirect:/diemthi/list/{maLopHoc}";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam("id") int maDiemThi) {
        DiemThi diemThi = diemThiRepository.findById(maDiemThi).orElse(null);
        if (diemThi != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Account.VaiTro currentRole = accountService.getCurrentUserRole(authentication);

            GiangVien giangVien = giangVienRepository.findByAccount_Email(authentication.getName());
            if (diemThi.getLopHocSinhVien().getLopHoc().getGiangVien().equals(giangVien) || currentRole == Account.VaiTro.ADMIN) {
                model.addAttribute("diemThi", diemThi);
                return "diemthi/edit";
            }
        }
        return "redirect:/diemthi/list";
    }
    @PostMapping("/edit")
    public String edit(@ModelAttribute("diemThi") DiemThi diemThi, Model model) {
        DiemThi existingDiemThi = diemThiRepository.findById(diemThi.getMaDiemThi()).orElse(null);

        if (existingDiemThi != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Account.VaiTro currentRole = accountService.getCurrentUserRole(authentication);

            GiangVien giangVien = giangVienRepository.findByAccount_Email(authentication.getName());
            if ((existingDiemThi.getLopHocSinhVien().getLopHoc().getGiangVien().equals(giangVien) && !existingDiemThi.getKhoa()) || currentRole == Account.VaiTro.ADMIN) {
                existingDiemThi.setDiemQuaTrinh(diemThi.getDiemQuaTrinh());
                existingDiemThi.setDiemCuoiKy(diemThi.getDiemCuoiKy());

                diemThiRepository.save(existingDiemThi);

                return "redirect:/diemthi/list/" + existingDiemThi.getLopHocSinhVien().getLopHoc().getMaLopHoc();
            } else {
                model.addAttribute("error", "Đã khóa điểm không thể sửa");
                model.addAttribute("diemThi", existingDiemThi); // Giữ lại thông tin điểm thi để hiển thị lại trên form
                return "diemthi/edit";
            }
        } else {
            model.addAttribute("error", "Không tìm thấy điểm thi hoặc thông tin liên quan bị thiếu.");
        }
        return "redirect:/diemthi/list";
    }

    @GetMapping("/khoa/{maLopHoc}")
    public String khoaDiem(@PathVariable("maLopHoc") Integer maLopHoc) {
        LopHoc lopHoc = lopHocRepository.findById(maLopHoc).orElse(null);
        if (lopHoc != null) {
            List<LopHocSinhVien> lopHocSinhViens = lopHocSinhVienRepository.findByLopHoc(lopHoc);
            for (LopHocSinhVien lhs : lopHocSinhViens) {
                DiemThi diemThi = diemThiRepository.findByLopHocSinhVien(lhs);
                if (diemThi != null) {
                    diemThi.setKhoa(true);
                    diemThiRepository.save(diemThi);
                }
            }
        }
        return "redirect:/diemthi/SecurityPoint";
    }

    @GetMapping("/mokhoa/{maLopHoc}")
    public String moKhoaDiem(@PathVariable("maLopHoc") Integer maLopHoc) {
        LopHoc lopHoc = lopHocRepository.findById(maLopHoc).orElse(null);
        if (lopHoc != null) {
            List<LopHocSinhVien> lopHocSinhViens = lopHocSinhVienRepository.findByLopHoc(lopHoc);
            for (LopHocSinhVien lhs : lopHocSinhViens) {
                DiemThi diemThi = diemThiRepository.findByLopHocSinhVien(lhs);
                if (diemThi != null) {
                    diemThi.setKhoa(false);
                    diemThiRepository.save(diemThi);
                }
            }
        }
        return "redirect:/diemthi/SecurityPoint";
    }

    @GetMapping("/SecurityPoint")
    public String SecurityPoint(Model model){
        List<LopHoc> lopHocList = lopHocRepository.findAll();
        model.addAttribute("lopHocList", lopHocList);
        return "diemthi/listKhoaDiem";
    }

    @GetMapping("/excel")
    public void exportToExcel(@RequestParam("maLopHoc") Integer maLopHoc, HttpServletResponse response) {
        try {
            List<DiemThi> diemThiList = diemThiRepository.findByLopHocSinhVien_LopHoc_MaLopHoc(maLopHoc);

            // Kiểm tra nếu danh sách rỗng
            if (diemThiList.isEmpty()) {
                throw new IllegalArgumentException("Không có dữ liệu điểm cho lớp học này.");
            }

            // Tạo workbook mới
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Danh sách điểm");

            // Header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Mã Sinh Viên");
            headerRow.createCell(1).setCellValue("Tên Sinh Viên");
            headerRow.createCell(2).setCellValue("Môn Học");
            headerRow.createCell(3).setCellValue("Điểm Quá Trình");
            headerRow.createCell(4).setCellValue("Điểm Cuối Kỳ");
            headerRow.createCell(5).setCellValue("Điểm Tổng Kết");

            // Data rows
            int rowNum = 1;
            for (DiemThi diemThi : diemThiList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(diemThi.getLopHocSinhVien().getSinhVien().getMaSinhVien());
                row.createCell(1).setCellValue(diemThi.getLopHocSinhVien().getSinhVien().getTenSinhVien());
                row.createCell(2).setCellValue(diemThi.getLopHocSinhVien().getLopHoc().getMonHoc().getTenMonHoc());
                row.createCell(3).setCellValue(diemThi.getDiemQuaTrinh());
                row.createCell(4).setCellValue(diemThi.getDiemCuoiKy());

                // Tính điểm tổng kết theo công thức 0.3 * điểm quá trình + 0.7 * điểm cuối kỳ
                double diemTongKet = 0.3 * diemThi.getDiemQuaTrinh() + 0.7 * diemThi.getDiemCuoiKy();
                row.createCell(5).setCellValue(diemTongKet);
            }

            // Thiết lập header cho response
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"diemthi.xlsx\"");

            // Ghi workbook vào response
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            // Xử lý exception nếu có lỗi khi ghi xuất Excel
            e.printStackTrace();
            // Hoặc có thể xử lý thông báo lỗi hoặc redirect người dùng đến trang lỗi tùy theo yêu cầu của bạn
        } catch (IllegalArgumentException e) {
            // Xử lý exception nếu không có dữ liệu để xuất
            e.printStackTrace();
            // Hoặc có thể xử lý thông báo lỗi hoặc redirect người dùng đến trang lỗi tùy theo yêu cầu của bạn
        }
    }


    @GetMapping("/thongke/{maLopHoc}")
    public String thongKe(@PathVariable("maLopHoc") Integer maLopHoc, Model model) {
        LopHoc lopHoc = lopHocRepository.findById(maLopHoc).orElse(null);
        // Lấy danh sách sinh viên trong lớp học
        List<LopHocSinhVien> lopHocSinhViens = lopHocSinhVienRepository.findByLopHoc(lopHoc);

        // Tính tổng số sinh viên
        int totalStudents = lopHocSinhViens.size();

        // Đếm số sinh viên qua môn và số sinh viên phải học lại
        int passedStudents = 0;
        int retakeStudents = 0;
        double sumdiem =0.0;
        for (LopHocSinhVien lhs : lopHocSinhViens) {
            DiemThi diemThi = diemThiRepository.findByLopHocSinhVien(lhs);
            if (diemThi != null && !diemThi.getKhoa()) {
                // Tính điểm tổng kết theo công thức 0.3 * điểm quá trình + 0.7 * điểm cuối kỳ
                double diemTongKet = 0.3 * diemThi.getDiemQuaTrinh() + 0.7 * diemThi.getDiemCuoiKy();
                sumdiem+=diemTongKet;
                if (diemTongKet >= 4.0) {
                    passedStudents++;
                } else {
                    retakeStudents++;
                }
            }
        }
        double avg=sumdiem/totalStudents;
        double pt= (double) passedStudents/totalStudents *100.0;

        // Lấy thông tin về lớp học để hiển thị tên lớp và môn học
        if (lopHoc != null) {
            model.addAttribute("lopHoc", lopHoc);
            model.addAttribute("totalStudents", totalStudents);
            model.addAttribute("passedStudents", passedStudents);
            model.addAttribute("retakeStudents", retakeStudents);
            model.addAttribute("avg", avg);
            model.addAttribute("pt", pt);
            return "diemthi/thongKe";
        }

        return "redirect:/diemthi/list";
    }


}


