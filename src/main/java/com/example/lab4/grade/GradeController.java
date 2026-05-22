package com.example.lab4.grade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/students/{studentId}/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    public ResponseEntity<List<GradeDto>> addGrade(@PathVariable Long studentId, @RequestBody Grade grade) {
        gradeService.addGrade(studentId, grade);
        return ResponseEntity.ok(gradeService.getGradesDtoByStudentId(studentId));
    }

    @GetMapping
    public ResponseEntity<List<GradeDto>> getGrades(@PathVariable Long studentId) {
        return ResponseEntity.ok(gradeService.getGradesDtoByStudentId(studentId));
    }

    @DeleteMapping("/{gradeId}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long gradeId) {
        gradeService.deleteGrade(gradeId);
        return ResponseEntity.noContent().build();
    }
}