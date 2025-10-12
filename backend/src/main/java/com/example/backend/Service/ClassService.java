package com.example.backend.Service;

import com.example.backend.DTO.ClassDTO;
import com.example.backend.DTO.UserDTO;
import com.example.backend.Model.ClassEntity;
import com.example.backend.Model.ClassStudent;
import com.example.backend.Model.User;
import com.example.backend.Repository.ClassRepository;
import com.example.backend.Repository.ClassStudentRepository;
import com.example.backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
// import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassStudentRepository classStudentRepository;

    // Tạo lớp mới
    @Transactional
    public ClassEntity createClass(ClassDTO.CreateClassRequest createClassRequest, Long teacherId) {
        // Kiểm tra mã lớp đã tồn tại chưa
        if (classRepository.existsByClassCode(createClassRequest.getClassCode())) {
            throw new RuntimeException("Mã lớp đã tồn tại: " + createClassRequest.getClassCode());
        }

        // Lấy thông tin giáo viên
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giáo viên"));

        // Đảm bảo người dùng có role TEACHER
        if (teacher.getRole() != User.Role.TEACHER) {
            throw new RuntimeException("Chỉ giáo viên được phép tạo lớp");
        }

        // Tạo entity từ DTO request
        ClassEntity classEntity = new ClassEntity();
        classEntity.setClassCode(createClassRequest.getClassCode());
        classEntity.setClassName(createClassRequest.getClassName());
        classEntity.setDescription(createClassRequest.getDescription());
        classEntity.setTeacher(teacher);

        return classRepository.save(classEntity);
    }

    // Cập nhật lớp
    @Transactional
    public ClassEntity updateClass(Long classId, ClassDTO.UpdateClassRequest updateClassRequest, Long teacherId) {
        ClassEntity existingClass = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp"));

        // Kiểm tra giáo viên có quyền sửa lớp này không
        if (!existingClass.getTeacher().getUserId().equals(teacherId)) {
            throw new RuntimeException("Bạn không có quyền sửa lớp này");
        }

        // Kiểm tra mã lớp mới không trùng với lớp khác
        if (!existingClass.getClassCode().equals(updateClassRequest.getClassCode()) &&
                classRepository.existsByClassCode(updateClassRequest.getClassCode())) {
            throw new RuntimeException("Mã lớp đã tồn tại: " + updateClassRequest.getClassCode());
        }

        // Cập nhật thông tin từ DTO
        existingClass.setClassCode(updateClassRequest.getClassCode());
        existingClass.setClassName(updateClassRequest.getClassName());
        existingClass.setDescription(updateClassRequest.getDescription());

        return classRepository.save(existingClass);
    }

    // Xóa lớp
    @Transactional
    public void deleteClass(Long classId, Long teacherId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp"));

        // Kiểm tra giáo viên có quyền xóa lớp này không
        if (!classEntity.getTeacher().getUserId().equals(teacherId)) {
            throw new RuntimeException("Bạn không có quyền xóa lớp này");
        }

        classRepository.delete(classEntity);
    }

    // Thêm sinh viên vào lớp
    @Transactional
    public ClassStudent addStudentToClass(Long classId, Long studentId, Long teacherId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));

        // Kiểm tra giáo viên có quyền thêm sinh viên vào lớp này không
        if (!classEntity.getTeacher().getUserId().equals(teacherId)) {
            throw new RuntimeException("Bạn không có quyền thêm sinh viên vào lớp này");
        }

        // Kiểm tra sinh viên đã có trong lớp chưa (SỬA LẠI)
        boolean alreadyEnrolled = classStudentRepository.existsByClassObjAndStudent(classEntity, student);
        if (alreadyEnrolled) {
            throw new RuntimeException("Sinh viên đã có trong lớp học này");
        }

        // Tạo mới ClassStudent
        ClassStudent classStudent = new ClassStudent();
        classStudent.setClassObj(classEntity);
        classStudent.setStudent(student);

        return classStudentRepository.save(classStudent);
    }

    // Xóa sinh viên khỏi lớp
    @Transactional
    public void removeStudentFromClass(Long classId, Long studentId, Long teacherId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học"));

        // Kiểm tra giáo viên có quyền xóa sinh viên khỏi lớp này không
        if (!classEntity.getTeacher().getUserId().equals(teacherId)) {
            throw new RuntimeException("Bạn không có quyền xóa sinh viên khỏi lớp này");
        }

        // Tìm và xóa ClassStudent (SỬA LẠI)
        ClassStudent classStudent = classStudentRepository.findByClassIdAndStudentId(classId, studentId)
                .orElseThrow(() -> new RuntimeException("Sinh viên không có trong lớp học này"));

        classStudentRepository.delete(classStudent);
    }

    // Lấy danh sách sinh viên trong lớp
    public List<UserDTO> getStudentsInClass(Long classId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học"));

        return classEntity.getClassStudents().stream()
                .map(ClassStudent::getStudent)
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Lấy danh sách lớp của giáo viên (dùng DTO)
    public List<ClassDTO.ClassSummary> getClassesByTeacher(Long teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giáo viên"));
        List<ClassEntity> classes = classRepository.findByTeacher(teacher);

        return classes.stream()
                .map(ClassDTO.ClassSummary::fromEntity)
                .collect(Collectors.toList());
    }

    // Lấy thông tin chi tiết lớp (dùng DTO)
    public ClassDTO.ClassResponse getClassDetail(Long classId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học"));

        return ClassDTO.ClassResponse.fromEntity(classEntity);
    }

    // Lấy danh sách lớp sinh viên đã đăng ký (dùng DTO)
    public List<ClassDTO.ClassSummary> getClassesByStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));
        List<ClassEntity> classes = classRepository.findByStudentEnrolled(student);

        return classes.stream()
                .map(ClassDTO.ClassSummary::fromEntity)
                .collect(Collectors.toList());
    }

    // Lấy thông tin lớp theo ID
    public ClassEntity getClassById(Long classId) {
        return classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp"));
    }

    // // Tìm kiếm lớp theo mã hoặc tên
    // public List<ClassEntity> searchClasses(String keyword) {
    //     return classRepository.findAll().stream()
    //             .filter(c -> c.getClassCode().toLowerCase().contains(keyword.toLowerCase()) ||
    //                     c.getClassName().toLowerCase().contains(keyword.toLowerCase()))
    //             .toList();
    // }

}