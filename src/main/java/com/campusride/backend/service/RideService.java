
package com.campusride.backend.service;

import com.campusride.backend.entity.Ride;
import com.campusride.backend.repository.RideRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public Ride offerRide(Ride ride) {
        if (ride.getStudentId() == null
                || ride.getStudentId() <= 0) {
            throw new IllegalArgumentException(
                    "Valid student ID is required");
        }

        if (ride.getSource() == null
                || ride.getSource().isBlank()
                || ride.getDestination() == null
                || ride.getDestination().isBlank()) {
            throw new IllegalArgumentException(
                    "Source and destination are required");
        }

        if (ride.getTravelDate() == null
                || ride.getTravelDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Travel date must be today or later");
        }

        if (ride.getAvailableSeats() == null
                || ride.getAvailableSeats() < 1) {
            throw new IllegalArgumentException(
                    "At least one available seat is required");
        }

        ride.setSource(ride.getSource().trim());
        ride.setDestination(ride.getDestination().trim());

        return rideRepository.save(ride);
    }

    public List<Ride> searchRides(
            String source,
            String destination,
            LocalDate travelDate) {

        if (source == null || source.isBlank()
                || destination == null || destination.isBlank()
                || travelDate == null) {
            throw new IllegalArgumentException(
                    "Source, destination and travel date are required");
        }

        return rideRepository
                .findBySourceContainingIgnoreCaseAndDestinationContainingIgnoreCaseAndTravelDateAndAvailableSeatsGreaterThan(
                        source.trim(),
                        destination.trim(),
                        travelDate,
                        0
                );
    }
    public List<Ride> getRidesByStudentId(Long studentId) {
    return rideRepository.findByStudentId(studentId);
}
}
