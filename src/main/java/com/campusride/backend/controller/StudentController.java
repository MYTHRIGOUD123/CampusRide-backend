
package com.campusride.backend.controller;

import com.campusride.backend.entity.Student;
import com.campusride.backend.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://campus-ride-frontend.vercel.app"
})
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Register a new student
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerStudent(
            @RequestBody Map<String, String> request) {

        String name = request.get("name");
        String email = request.get("email");
        String password = request.get("password");

        if (name == null || name.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {

            return ResponseEntity.badRequest().body(
                    Map.of("message", "All fields are required"));
        }

        try {
            Student student = studentService.registerStudent(
                    name.trim(), email.trim(), password);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "message", "Registration successful!",
                            "id", student.getId(),
                            "name", student.getName(),
                            "email", student.getEmail()
                    ));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", ex.getMessage()));
        }
    }

    // Login an existing student
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginStudent(
            @RequestBody Map<String, String> request) {

        String email = request.get("email");
        String password = request.get("password");

        if (email == null || email.isBlank()
                || password == null || password.isBlank()) {

            return ResponseEntity.badRequest().body(
                    Map.of("message", "Email and password are required"));
        }

        try {
            Student student = studentService.loginStudent(
                    email.trim(), password);

            return ResponseEntity.ok(
                    Map.of(
                            "message", "Login successful!",
                            "id", student.getId(),
                            "name", student.getName(),
                            "email", student.getEmail()
                    ));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("message", ex.getMessage()));
        }
    }

    // Update student profile
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProfile(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String name = request.get("name");
        String email = request.get("email");

        if (name == null || name.isBlank()
                || email == null || email.isBlank()) {

            return ResponseEntity.badRequest().body(
                    Map.of("message", "Name and email are required"));
        }

        try {
            Student student = studentService.updateProfile(
                    id, name.trim(), email.trim());

            return ResponseEntity.ok(
                    Map.of(
                            "message", "Profile updated successfully!",
                            "id", student.getId(),
                            "name", student.getName(),
                            "email", student.getEmail()
                    ));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", ex.getMessage()));
        }
    }
}