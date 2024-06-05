package com.example.demo.Controller;

import com.example.demo.Model.SinhVien;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class SinhVienController {
    @GetMapping("/sinhvien")
    public String showForm(Model model) {
        model.addAttribute("sinhVien", new SinhVien());
        return "sinhvien/form-sinhvien";
    }

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.folder}")
    private String folderPath;

    @PostMapping("/sinhvien")
    public String submitForm(@Valid SinhVien sinhVien, BindingResult bindingResult,
                             @RequestParam("image") MultipartFile imageFile, Model model) {
//        if (bindingResult.hasErrors()) {
//            return "sinhvien/form-sinhvien";
//        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Lấy đường dẫn tuyệt đối của thư mục gốc dự án
                Path currentPath = Paths.get("").toAbsolutePath();
                // Tính toán đường dẫn tuyệt đối cho thư mục upload
                Path uploadDir = currentPath.resolve(uploadPath);

//                Path uploadDir = Paths.get(uploadPath);
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                // Tạo tên file duy nhất để tránh trùng lặp
//                String uniqueFileName = UUID.randomUUID().toString() + "-" + imageFile.getOriginalFilename();
//                Path filePath = uploadDir.resolve(uniqueFileName);

                String imagePath = uploadDir + "\\" + imageFile.getOriginalFilename();
                imageFile.transferTo(new File(imagePath));
                sinhVien.setImage(folderPath + "\\" + imageFile.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
                bindingResult.rejectValue("image", "error.sinhVien", "Unable to upload image");
                return "sinhvien/form-sinhvien";
            }
        }

        model.addAttribute("message", "Sinh viên đã được thêm thành công!");
        return "sinhvien/result-sinhvien";
    }
}