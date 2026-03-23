package backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SeatingService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private SeatingRecordRepository seatingRecordRepository;

    public List<SeatingRecord> generateSeating() {
        seatingRecordRepository.deleteAll();

        // Get all students grouped by department
        List<Student> allStudents = studentRepository.findAll();
        Map<String, List<Student>> deptMap = new LinkedHashMap<>();
        for (Student s : allStudents) {
            deptMap.computeIfAbsent(s.getDepartment(), k -> new ArrayList<>()).add(s);
        }

        // Round-robin interleaving
        List<Student> interleaved = new ArrayList<>();
        List<List<Student>> deptLists = new ArrayList<>(deptMap.values());
        int maxSize = deptLists.stream().mapToInt(List::size).max().orElse(0);
        for (int i = 0; i < maxSize; i++) {
            for (List<Student> deptList : deptLists) {
                if (i < deptList.size()) {
                    interleaved.add(deptList.get(i));
                }
            }
        }

        // Get halls and calculate equal distribution
        List<Hall> halls = hallRepository.findAll();
        int totalStudents = interleaved.size();
        int totalHalls = halls.size();

        // Calculate seats per hall equally
        int baseCount = totalStudents / totalHalls;
        int remainder = totalStudents % totalHalls;

        // Build seat slots per hall
        // Each bench has L and R = 2 seats
        List<SeatingRecord> records = new ArrayList<>();
        int studentIndex = 0;

        for (int h = 0; h < totalHalls; h++) {
            Hall hall = halls.get(h);
            // Give extra student to first halls if remainder exists
            int studentsForThisHall = baseCount + (h < remainder ? 1 : 0);
            int benchesNeeded = (int) Math.ceil(studentsForThisHall / 2.0);

            // Make sure we don't exceed hall capacity
            benchesNeeded = Math.min(benchesNeeded, hall.getTotalBenches());

            for (int bench = 1; bench <= benchesNeeded; bench++) {
                for (String side : new String[]{"L", "R"}) {
                    if (studentIndex >= interleaved.size()) break;
                    if (studentIndex >= (h == totalHalls - 1 ?
                            totalStudents :
                            studentIndex + studentsForThisHall - (bench - 1) * 2 - (side.equals("R") ? 1 : 0))) {
                    }
                    Student s = interleaved.get(studentIndex++);
                    SeatingRecord record = new SeatingRecord();
                    record.setRegNo(s.getRegNo());
                    record.setStudentName(s.getName());
                    record.setDepartment(s.getDepartment());
                    record.setBlock(hall.getBlock());
                    record.setHallName(hall.getHallName());
                    record.setBenchNo(bench);
                    record.setSide(side);
                    records.add(record);

                    studentsForThisHall--;
                    if (studentsForThisHall <= 0) break;
                }
                if (studentsForThisHall <= 0) break;
            }
        }

        return seatingRecordRepository.saveAll(records);
    }

    public Optional<SeatingRecord> findSeat(String regNo) {
        return seatingRecordRepository.findByRegNo(regNo);
    }

    public List<SeatingRecord> getAllSeating() {
        return seatingRecordRepository.findAll();
    }
}