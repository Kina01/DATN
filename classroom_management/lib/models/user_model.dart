class User {
  final String id;
  final String fullname;
  final String email;
  final DateTime? createdAt;

  User({
    required this.id,
    required this.fullname,
    required this.email,
    this.createdAt,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      id: json['id'] ?? '',
      fullname: json['fullname'] ?? '',
      email: json['email'] ?? '',
      createdAt: json['createdAt'] != null ? DateTime.parse(json['createdAt']) : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'fullname': fullname,
      'email': email,
      'createdAt': createdAt?.toIso8601String(),
    };
  }
}

class RegisterRequest {
  final String fullname;
  final String email;
  final String password;
  final String verificationCode;

  RegisterRequest({
    required this.fullname,
    required this.email,
    required this.password,
    required this.verificationCode,
  });

  Map<String, dynamic> toJson() {
    return {
      'fullname': fullname,
      'email': email,
      'password': password,
      'verificationCode': verificationCode,
    };
  }
}

class VerificationRequest {
  final String email;

  VerificationRequest({required this.email});

  Map<String, dynamic> toJson() {
    return {
      'email': email,
    };
  }
}

class ApiResponse {
  final String status;
  final String message;
  final dynamic data;

  ApiResponse({
    required this.status,
    required this.message,
    this.data,
  });

  factory ApiResponse.fromJson(Map<String, dynamic> json) {
    return ApiResponse(
      status: json['status'] ?? '',
      message: json['message'] ?? '',
      data: json['data'],
    );
  }
}