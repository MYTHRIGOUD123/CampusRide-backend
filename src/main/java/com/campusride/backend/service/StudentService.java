
package com.campusride.backend.service;

import com.campusride.backend.entity.Student;
import com.campusride.backend.repository.StudentRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Register a new student
    public Student registerStudent(
            String name, String email, String password) {

        if (studentRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered");
        }

        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setPasswordHash(passwordEncoder.encode(password));

        return studentRepository.save(student);
    }

    // Login an existing student
    public Student loginStudent(String email, String password) {

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid email or password"));

        if (!passwordEncoder.matches(
                password, student.getPasswordHash())) {

            throw new IllegalArgumentException(
                    "Invalid email or password");
        }

        return student;
    }

    // Update student profile
    public Student updateProfile(
            Long id, String name, String email) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Student not found"));

        if (studentRepository.existsByEmailAndIdNot(email, id)) {
            throw new IllegalArgumentException(
                    "Email is already used by another student");
        }

        student.setName(name);
        student.setEmail(email);

        return studentRepository.save(student);
    }
}