
package com.campusride.backend.repository;

import com.campusride.backend.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findBySourceContainingIgnoreCaseAndDestinationContainingIgnoreCaseAndTravelDateAndAvailableSeatsGreaterThan(
            String source,
            String destination,
            LocalDate travelDate,
            Integer seats
    );
    List<Ride> findByStudentId(Long studentId);
}
