package com.example.lab4.grade;

import com.example.lab4.Student;
import com.example.lab4.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public Grade addGrade(Long studentId, Grade grade) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        grade.setStudent(student);
        return gradeRepository.save(grade);
    }

    public List<Grade> getGradesByStudentId(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    @Transactional
    public void deleteGrade(Long gradeId) {
        gradeRepository.deleteById(gradeId);
    }

    @Transactional
    public void deleteAllGradesByStudentId(Long studentId) {
        gradeRepository.deleteByStudentId(studentId);
    }

    public List<GradeDto> getGradesDtoByStudentId(Long studentId) {
        return gradeRepository.findByStudentId(studentId).stream()
                .map(this::toDto)
                .toList();
    }

    private GradeDto toDto(Grade grade) {
        GradeDto dto = new GradeDto();
        dto.setId(grade.getId());
        dto.setSubject(grade.getSubject());
        dto.setScore(grade.getScore());
        dto.setComment(grade.getComment());
        dto.setCreatedAt(grade.getCreatedAt());
        return dto;
    }
}