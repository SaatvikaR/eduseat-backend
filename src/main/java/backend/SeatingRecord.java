package backend;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "seating_records")
public class SeatingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String regNo;
    private String studentName;
    private String department;
    private String block;
    private String hallName;
    private int benchNo;
    private String side; // L or R
}
