
package com.campusride.backend.service;

import com.campusride.backend.entity.Ride;
import com.campusride.backend.entity.RideRequest;
import com.campusride.backend.repository.RideRepository;
import com.campusride.backend.repository.RideRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideRequestService {

    private final RideRequestRepository rideRequestRepository;
    private final RideRepository rideRepository;

    public RideRequestService(
            RideRequestRepository rideRequestRepository,
            RideRepository rideRepository) {
        this.rideRequestRepository = rideRequestRepository;
        this.rideRepository = rideRepository;
    }

    public RideRequest createRequest(Long rideId, Long studentId) {
        if (rideId == null || studentId == null) {
            throw new IllegalArgumentException(
                    "Ride ID and student ID are required");
        }

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Ride not found"));

        if (ride.getStudentId().equals(studentId)) {
            throw new IllegalArgumentException(
                    "You cannot request your own ride");
        }

        if (ride.getAvailableSeats() == null
                || ride.getAvailableSeats() < 1) {
            throw new IllegalArgumentException(
                    "No available seats for this ride");
        }

        if (rideRequestRepository
                .existsByRideIdAndStudentId(rideId, studentId)) {
            throw new IllegalArgumentException(
                    "You have already requested this ride");
        }

        RideRequest request = new RideRequest();
        request.setRideId(rideId);
        request.setStudentId(studentId);
        request.setStatus("PENDING");

        return rideRequestRepository.save(request);
    }

    public List<RideRequest> getRequestsByStudent(Long studentId) {
        return rideRequestRepository.findByStudentId(studentId);
    }

    public List<RideRequest> getRequestsForRide(Long rideId) {
        if (!rideRepository.existsById(rideId)) {
            throw new IllegalArgumentException("Ride not found");
        }

        return rideRequestRepository.findByRideId(rideId);
    }

    public RideRequest updateRequestStatus(Long requestId, String status) {
        if (status == null
                || (!status.equalsIgnoreCase("ACCEPTED")
                && !status.equalsIgnoreCase("REJECTED"))) {
            throw new IllegalArgumentException(
                    "Status must be ACCEPTED or REJECTED");
        }

        RideRequest request = rideRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Ride request not found"));

        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalArgumentException(
                    "This request has already been processed");
        }

        String newStatus = status.toUpperCase();

        if ("ACCEPTED".equals(newStatus)) {
            Ride ride = rideRepository.findById(request.getRideId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Ride not found"));

            if (ride.getAvailableSeats() == null
                    || ride.getAvailableSeats() < 1) {
                throw new IllegalArgumentException(
                        "No available seats for this ride");
            }

            ride.setAvailableSeats(ride.getAvailableSeats() - 1);
            rideRepository.save(ride);
        }

        request.setStatus(newStatus);
        return rideRequestRepository.save(request);
    }
}