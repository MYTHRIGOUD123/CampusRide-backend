
package com.campusride.backend.controller;

import com.campusride.backend.entity.Ride;
import com.campusride.backend.service.RideService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rides")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://campus-ride-frontend.vercel.app"
})
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // Offer a new ride
    @PostMapping
    public ResponseEntity<?> offerRide(@RequestBody Ride ride) {
        try {
            Ride savedRide = rideService.offerRide(ride);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedRide);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Search for available rides
    @GetMapping("/search")
    public ResponseEntity<?> searchRides(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate travelDate) {

        try {
            List<Ride> rides = rideService.searchRides(
                    source,
                    destination,
                    travelDate
            );

            return ResponseEntity.ok(rides);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Get rides offered by a particular student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Ride>> getRidesByStudentId(
            @PathVariable Long studentId) {

        List<Ride> rides =
                rideService.getRidesByStudentId(studentId);

        return ResponseEntity.ok(rides);
    }
}
