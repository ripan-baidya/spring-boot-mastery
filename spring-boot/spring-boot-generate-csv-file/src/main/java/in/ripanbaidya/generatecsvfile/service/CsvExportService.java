package in.ripanbaidya.generatecsvfile.service;

import in.ripanbaidya.generatecsvfile.entity.Student;
import in.ripanbaidya.generatecsvfile.repository.StudentRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

@Service
public class CsvExportService {

    private final StudentRepository studentRepository;

    @Autowired
    public CsvExportService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public byte[] exportStudentsDataToCsv() {
        List<Student> students = studentRepository.findAll();
        return generateCsv(students);
    }

    private byte[] generateCsv(List<Student> students) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(outputStream);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("ID", "Name", "Phone Number", "Email")
             )
        ) {
            for (Student student : students) {
                csvPrinter.printRecord(
                        student.getId(),
                        student.getName(),
                        student.getPhoneNumber(),
                        student.getEmail()
                );
            }

            csvPrinter.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Csv file.");
        }
    }
}
