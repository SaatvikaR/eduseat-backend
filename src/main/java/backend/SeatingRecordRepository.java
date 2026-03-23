package backend;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SeatingRecordRepository extends JpaRepository<SeatingRecord, Long> {
    Optional<SeatingRecord> findByRegNo(String regNo);
    void deleteAllByHallName(String hallName);
}

