
package com.campusride.backend.repository;

import com.campusride.backend.entity.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRequestRepository
        extends JpaRepository<RideRequest, Long> {

    // Find all requests made by a student
    List<RideRequest> findByStudentId(Long studentId);

    // Find all requests for a particular ride
    List<RideRequest> findByRideId(Long rideId);

    // Check whether a student has already requested a ride
    boolean existsByRideIdAndStudentId(
            Long rideId,
            Long studentId
    );
}