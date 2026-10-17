
package com.campusride.backend.controller;

import com.campusride.backend.entity.RideRequest;
import com.campusride.backend.service.RideRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ride-requests")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://campus-ride-frontend.vercel.app"
})
public class RideRequestController {

    private final RideRequestService rideRequestService;

    public RideRequestController(
            RideRequestService rideRequestService) {
        this.rideRequestService = rideRequestService;
    }

    // Send a request to join a ride
    @PostMapping
    public ResponseEntity<?> createRequest(
            @RequestBody Map<String, Long> requestData) {
        try {
            Long rideId = requestData.get("rideId");
            Long studentId = requestData.get("studentId");

            RideRequest savedRequest =
                    rideRequestService.createRequest(rideId, studentId);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedRequest);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // View requests sent by a student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<RideRequest>> getRequestsByStudent(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                rideRequestService.getRequestsByStudent(studentId));
    }

    // View requests for a particular ride
    @GetMapping("/ride/{rideId}")
    public ResponseEntity<?> getRequestsForRide(
            @PathVariable Long rideId) {
        try {
            return ResponseEntity.ok(
                    rideRequestService.getRequestsForRide(rideId));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Accept or reject a request
    @PutMapping("/{requestId}/status")
    public ResponseEntity<?> updateRequestStatus(
            @PathVariable Long requestId,
            @RequestBody Map<String, String> requestData) {
        try {
            String status = requestData.get("status");

            RideRequest updatedRequest =
                    rideRequestService.updateRequestStatus(
                            requestId, status);

            return ResponseEntity.ok(updatedRequest);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }
}