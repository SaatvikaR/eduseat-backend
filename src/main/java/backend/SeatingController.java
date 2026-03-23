package backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://eduseat-frontend.vercel.app/")
public class SeatingController {

    @Autowired
    private SeatingService seatingService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private HallRepository hallRepository;

    // Add a hall
    @PostMapping("/halls")
    public Hall addHall(@RequestBody Hall hall) {
        return hallRepository.save(hall);
    }

    // Get all halls
    @GetMapping("/halls")
    public List<Hall> getHalls() {
        return hallRepository.findAll();
    }

    // Add a student
    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    // Add multiple students (CSV upload)
    @PostMapping("/students/bulk")
    public List<Student> addStudents(@RequestBody List<Student> students) {
        return studentRepository.saveAll(students);
    }

    // Get all students
    @GetMapping("/students")
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    // Delete all students
    @DeleteMapping("/students")
    public ResponseEntity<?> deleteStudents() {
        studentRepository.deleteAll();
        return ResponseEntity.ok("All students deleted");
    }

    // Generate seating
    @PostMapping("/generate")
    public List<SeatingRecord> generateSeating() {
        return seatingService.generateSeating();
    }

    // Get all seating records
    @GetMapping("/seating")
    public List<SeatingRecord> getAllSeating() {
        return seatingService.getAllSeating();
    }

    // Find seat by reg number
    @GetMapping("/seat/{regNo}")
    public ResponseEntity<?> findSeat(@PathVariable String regNo) {
        Optional<SeatingRecord> record = seatingService.findSeat(regNo);
        if (record.isPresent()) {
            return ResponseEntity.ok(record.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Delete all halls
    @DeleteMapping("/halls")
    public ResponseEntity<?> deleteHalls() {
        hallRepository.deleteAll();
        return ResponseEntity.ok("All halls deleted");
    }
}

