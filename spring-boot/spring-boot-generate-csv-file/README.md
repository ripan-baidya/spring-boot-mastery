## Step-by-Step Implementation to Generate a CSV file in Spring Boot using Apache Commons CSV

### **Step 1: Add Dependencies**
Ensure you have these dependencies in your `pom.xml`:

```xml
<!-- Spring Boot Starter Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- PostgreSQL Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Apache Commons CSV for CSV generation -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-csv</artifactId>
    <version>1.10.0</version>
</dependency>

<!-- Optional: For OpenCSV (alternative) -->
<dependency>
    <groupId>com.opencsv</groupId>
    <artifactId>opencsv</artifactId>
    <version>5.9</version>
</dependency>
```

### **Step 2: Entity Class**
First, ensure you have a proper Student entity:

```java
@Entity
@Table(name = "students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(unique = true)
    private String email;
    
    // Add other fields as needed
    
    // Constructors
    public Student() {}
    
    public Student(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
```

### **Step 3: Repository Interface**
```java
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // You can add custom queries if needed
}
```

### **Step 4: CSV Service Class**
Create a service to handle CSV generation:

```java
@Service
public class CsvExportService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    public byte[] exportStudentsToCsv() {
        List<Student> students = studentRepository.findAll();
        return generateCsv(students);
    }
    
    private byte[] generateCsv(List<Student> students) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(outputStream);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("ID", "Name", "Phone Number", "Email"))) {
            
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
            throw new RuntimeException("Failed to generate CSV", e);
        }
    }
}
```

### **Step 5: Controller to Download CSV**
```java
@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    private CsvExportService csvExportService;
    
    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportStudentsToCsv() {
        byte[] csvData = csvExportService.exportStudentsToCsv();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students.csv")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                .contentLength(csvData.length)
                .body(csvData);
    }
}
```


#
### **Step 6: Testing the Endpoint**
1. Run your Spring Boot application
2. Access the endpoint: `http://localhost:8080/api/students/export/csv`
3. The browser should automatically download a CSV file named `students.csv`

### **Step 7: Optional - Add Query Parameters**
For filtered exports:

```java
@GetMapping("/export/csv")
public ResponseEntity<byte[]> exportFilteredStudents(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String email) {
    
    List<Student> students;
    
    if (name != null || email != null) {
        // Add custom repository methods for filtering
        students = studentRepository.findByNameContainingOrEmailContaining(name, email);
    } else {
        students = studentRepository.findAll();
    }
    
    byte[] csvData = csvExportService.generateCsv(students);
    
    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students.csv")
            .header(HttpHeaders.CONTENT_TYPE, "text/csv")
            .contentLength(csvData.length)
            .body(csvData);
}
```

### **Key Points to Remember:**

1. **CSV Formatting**: Always escape fields containing commas, quotes, or newlines
2. **Memory Management**: Use streaming approach for large datasets
3. **Headers**: Set proper HTTP headers for file download
4. **Error Handling**: Add proper exception handling in production
5. **Security**: Add authentication/authorization if needed
6. **Performance**: Consider adding pagination for very large exports