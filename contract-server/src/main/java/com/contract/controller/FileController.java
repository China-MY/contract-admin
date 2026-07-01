package com.contract.controller;

import com.contract.common.Result;
import com.contract.entity.ProjectDocument;
import com.contract.repository.ProjectDocumentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final ProjectDocumentRepository documentRepository;

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    public FileController(ProjectDocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @PostMapping("/upload")
    public Result<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("projectNo") String projectNo,
            @RequestParam(value = "projectName", required = false) String projectName,
            @RequestParam(value = "remark", required = false) String remark) {
        if (file.isEmpty()) return Result.error("文件不能为空");

        try {
            // 创建项目目录
            String projectDir = uploadDir + File.separator + projectNo;
            Path dirPath = Paths.get(projectDir);
            if (!Files.exists(dirPath)) Files.createDirectories(dirPath);

            // 保存文件
            String originalName = file.getOriginalFilename();
            String ext = "";
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }
            String storedName = UUID.randomUUID().toString() + ext;
            Path filePath = dirPath.resolve(storedName);
            Files.copy(file.getInputStream(), filePath);

            // 保存记录
            ProjectDocument doc = new ProjectDocument();
            doc.setProjectNo(projectNo);
            doc.setProjectName(projectName);
            doc.setFileName(storedName);
            doc.setOriginalName(originalName);
            doc.setFilePath(filePath.toString());
            doc.setFileSize(file.getSize());
            doc.setFileType(file.getContentType());
            doc.setRemark(remark);
            documentRepository.save(doc);

            return Result.ok(doc);
        } catch (IOException e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }

    @GetMapping("/project/{projectNo}")
    public Result<?> listByProject(@PathVariable String projectNo) {
        List<ProjectDocument> docs = documentRepository.findByProjectNoOrderByUploadTimeDesc(projectNo);
        return Result.ok(docs);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        ProjectDocument doc = documentRepository.findById(id).orElse(null);
        if (doc == null) return Result.error("文件不存在");
        // 删除物理文件
        try {
            Files.deleteIfExists(Paths.get(doc.getFilePath()));
        } catch (IOException ignored) {}
        documentRepository.deleteById(id);
        return Result.ok("删除成功");
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        ProjectDocument doc = documentRepository.findById(id).orElse(null);
        if (doc == null) return ResponseEntity.notFound().build();

        File file = new File(doc.getFilePath());
        if (!file.exists()) return ResponseEntity.notFound().build();

        Resource resource = new FileSystemResource(file);
        String encodedName = URLEncoder.encode(doc.getOriginalName() != null ? doc.getOriginalName() : doc.getFileName(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedName)
                .body(resource);
    }
}
