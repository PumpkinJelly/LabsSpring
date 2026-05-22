package com.example.lab4;

import com.example.lab4.dto.CreateStudentDTO;
import com.example.lab4.dto.StudentResponseDTO;
import com.example.lab4.dto.UpdateStudentDTO;
import com.example.lab4.exception.EmailAlreadyExistsException;
import com.example.lab4.exception.StudentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    public StudentResponseDTO create(CreateStudentDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        Student student = new Student();
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setAge(dto.getAge());

        Student saved = repository.save(student);
        return toResponseDTO(saved);
    }

    public List<StudentResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Page<StudentResponseDTO> findAllPaginated(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::toResponseDTO);
    }

    public StudentResponseDTO findById(Long id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return toResponseDTO(student);
    }

    public StudentResponseDTO update(Long id, UpdateStudentDTO dto) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        if (dto.getFirstName() != null) {
            student.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            student.setLastName(dto.getLastName());
        }
        if (dto.getAge() != null) {
            student.setAge(dto.getAge());
        }
        if (dto.getEmail() != null) {
            if (!dto.getEmail().equals(student.getEmail()) &&
                    repository.existsByEmail(dto.getEmail())) {
                throw new EmailAlreadyExistsException(dto.getEmail());
            }
            student.setEmail(dto.getEmail());
        }

        Student updated = repository.save(student);
        return toResponseDTO(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private StudentResponseDTO toResponseDTO(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setAge(student.getAge());
        dto.setCreatedAt(student.getCreatedAt());
        return dto;
    }
}