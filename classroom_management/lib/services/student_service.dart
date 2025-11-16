import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/class_model.dart';
import '../models/user_model.dart';
import '../services/auth_service.dart';

class StudentService {
  static const String baseUrl = 'http://192.168.89.215:8080/api';
  // static const String baseUrl = 'http://172.50.169.165:8080/api';
  
  // Lấy danh sách sinh viên trong lớp
  static Future<ApiResponse<List<User>>> getStudentsInClass(int classId) async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/classes/$classId/students'),
        headers: {
          'Content-Type': 'application/json',
        },
      );

      if (response.statusCode == 200) {
        final Map<String, dynamic> data = json.decode(response.body);
        return ApiResponse.fromJson(
          data,
          (jsonData) => (jsonData as List).map((e) => User.fromJson(e)).toList(),
        );
      } else {
        return ApiResponse(
          message: 'Lỗi kết nối: ${response.statusCode}',
          status: 'error',
        );
      }
    } catch (e) {
      return ApiResponse(
        message: 'Lỗi kết nối: $e',
        status: 'error',
      );
    }
  }

  // Thêm sinh viên vào lớp
  static Future<ApiResponse<dynamic>> addStudentToClass(int classId, int studentId) async {
    try {
      final user = await AuthService.getUser();
      if (user == null) {
        return ApiResponse(
          message: 'Chưa đăng nhập',
          status: 'error',
        );
      }

      final response = await http.post(
        Uri.parse('$baseUrl/classes/$classId/students/$studentId'),
        headers: {
          'Content-Type': 'application/json',
          'User-ID': user.userId.toString(),
        },
      );

      if (response.statusCode == 200) {
        final Map<String, dynamic> data = json.decode(response.body);
        return ApiResponse.fromJson(data, (jsonData) => jsonData);
      } else {
        return ApiResponse(
          message: 'Lỗi kết nối: ${response.statusCode}',
          status: 'error',
        );
      }
    } catch (e) {
      return ApiResponse(
        message: 'Lỗi kết nối: $e',
        status: 'error',
      );
    }
  }

  // Xóa sinh viên khỏi lớp
  static Future<ApiResponse<void>> removeStudentFromClass(int classId, int studentId) async {
    try {
      final user = await AuthService.getUser();
      if (user == null) {
        return ApiResponse(
          message: 'Chưa đăng nhập',
          status: 'error',
        );
      }

      final response = await http.delete(
        Uri.parse('$baseUrl/classes/$classId/delete-students/$studentId'),
        headers: {
          'Content-Type': 'application/json',
          'User-ID': user.userId.toString(),
        },
      );

      if (response.statusCode == 200) {
        final Map<String, dynamic> data = json.decode(response.body);
        return ApiResponse.fromJson(data, (jsonData) => null);
      } else {
        return ApiResponse(
          message: 'Lỗi kết nối: ${response.statusCode}',
          status: 'error',
        );
      }
    } catch (e) {
      return ApiResponse(
        message: 'Lỗi kết nối: $e',
        status: 'error',
      );
    }
  }

  // Tìm kiếm sinh viên 
  static Future<ApiResponse<List<User>>> searchStudents(String keyword) async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/classes/search-students?keyword=$keyword'),
        headers: {
          'Content-Type': 'application/json',
        },
      );

      if (response.statusCode == 200) {
        final Map<String, dynamic> data = json.decode(response.body);
        return ApiResponse.fromJson(
          data,
          (jsonData) => (jsonData as List).map((e) => User.fromJson(e)).toList(),
        );
      } else {
        return ApiResponse(
          message: 'Lỗi kết nối: ${response.statusCode}',
          status: 'error',
        );
      }
    } catch (e) {
      return ApiResponse(
        message: 'Lỗi kết nối: $e',
        status: 'error',
      );
    }
  }
}