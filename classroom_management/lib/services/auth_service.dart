import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../models/user_model.dart';

class AuthService {
  static const String baseUrl = 'http://192.168.89.215:8080/api/auth';
  // static const String baseUrl = 'http://172.50.169.165:8080/api/auth';
  
  static Future<LoginResponse> login(String email, String password) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/login'),
        headers: {
          'Content-Type': 'application/json',
        },
        body: json.encode({
          'email': email,
          'password': password,
        }),
      );

      if (response.statusCode == 200) {
        final Map<String, dynamic> data = json.decode(response.body);
        return LoginResponse.fromJson(data);
      } else if (response.statusCode == 400) {
        final Map<String, dynamic> data = json.decode(response.body);
        return LoginResponse.fromJson(data);
      } else {
        return LoginResponse(
          message: 'Lỗi kết nối: ${response.statusCode}',
          status: 'error',
        );
      }
    } catch (e) {
      return LoginResponse(
        message: 'Lỗi kết nối: $e',
        status: 'error',
      );
    }
  }

  static Future<void> saveUser(User user) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('user', json.encode(user.toJson()));
  }

  static Future<User?> getUser() async {
    final prefs = await SharedPreferences.getInstance();
    final userString = prefs.getString('user');
    if (userString != null) {
      final Map<String, dynamic> userMap = json.decode(userString);
      return User.fromJson(userMap);
    }
    return null;
  }

  static Future<void> logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('user');
  }
  
  static Future<bool> isLoggedIn() async {
    final user = await getUser();
    return user != null;
  }
}