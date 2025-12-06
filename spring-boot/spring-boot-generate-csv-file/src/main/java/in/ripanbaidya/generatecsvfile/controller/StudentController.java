package in.ripanbaidya.generatecsvfile.controller;

import in.ripanbaidya.generatecsvfile.service.CsvExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    

    private final CsvExportService csvExportService;

    @Autowired
    public StudentController(CsvExportService csvExportService) {
        this.csvExportService = csvExportService;
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportStudentsToCsv() {
        byte[] csvData = csvExportService.exportStudentsDataToCsv();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students.csv")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                .contentLength(csvData.length)
                .body(csvData);
    }
}