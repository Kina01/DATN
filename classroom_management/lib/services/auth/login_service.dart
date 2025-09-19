import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:classroom_management/models/user_model.dart';

class LoginService {
  final String baseUrl = 'http://192.168.89.215:8080';

  Future<ApiResponse> login(String email, String password) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/user/login'),
        headers: {'Content-Type': 'application/json'},
        body: json.encode({
          'email': email,
          'password': password,
        }),
      );

      final responseData = json.decode(response.body);

      // Xử lý response từ backend Java
      if (response.statusCode == 200) {
        // Thành công - backend trả về các field riêng lẻ
        return ApiResponse(
          status: 'success',
          message: responseData['message'] ?? 'Đăng nhập thành công',
          data: responseData, // Chuyển toàn bộ response vào data
        );
      } else {
        // Thất bại
        return ApiResponse(
          status: 'error',
          message: responseData['message'] ?? 'Đăng nhập thất bại',
        );
      }
    } catch (e) {
      return ApiResponse(
        status: 'error',
        message: 'Lỗi kết nối: $e',
      );
    }
  }
}
