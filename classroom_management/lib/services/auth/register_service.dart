import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:classroom_management/models/user_model.dart';

class RegisterService {
  final String baseUrl = 'http://192.168.89.215:8080';

  // Gửi mã xác minh
  Future<ApiResponse> sendVerificationCode(String email) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/user/send-verification'),
        headers: {'Content-Type': 'application/json'},
        body: json.encode({'email': email}),
      );

      return ApiResponse.fromJson(json.decode(response.body));
    } catch (e) {
      return ApiResponse(
        status: 'error',
        message: 'Không thể kết nối đến server',
      );
    }
  }

  // Xác minh mã OTP
  Future<ApiResponse> verifyCode(String email, String code) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/user/verify-otp'),
        headers: {'Content-Type': 'application/json'},
        body: json.encode({'email': email, 'verificationCode': code}),
      );
      if (response.statusCode == 200) {
        return ApiResponse.fromJson(json.decode(response.body));
      } else {
        return ApiResponse(
          status: 'error',
          message: 'Lỗi xác thực: ${response.statusCode}',
        );
      }
    } catch (e) {
      return ApiResponse(
        status: 'error',
        message: 'Không thể kết nối đến server',
      );
    }
  }

  // Kiểm tra email tồn tại
  Future<ApiResponse> checkEmailExists(String email) async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/user/check-email?email=$email'),
      );

      return ApiResponse.fromJson(json.decode(response.body));
    } catch (e) {
      return ApiResponse(
        status: 'error',
        message: 'Không thể kết nối đến server',
      );
    }
  }

  // Đăng ký tài khoản
  Future<ApiResponse> register(RegisterRequest request) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/user/register'),
        headers: {'Content-Type': 'application/json'},
        body: json.encode(request.toJson()),
      );

      return ApiResponse.fromJson(json.decode(response.body));
    } catch (e) {
      return ApiResponse(
        status: 'error',
        message: 'Không thể kết nối đến server',
      );
    }
  }
}
