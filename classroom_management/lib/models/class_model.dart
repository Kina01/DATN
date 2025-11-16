import 'user_model.dart';

class ClassSummary {
  final int classId;
  final String classCode;
  final String className;
  final String description;
  final String teacherName;
  final int studentCount;

  ClassSummary({
    required this.classId,
    required this.classCode,
    required this.className,
    required this.description,
    required this.teacherName,
    required this.studentCount,
  });

  factory ClassSummary.fromJson(Map<String, dynamic> json) {
    return ClassSummary(
      classId: json['classId'] ?? 0,
      classCode: json['classCode'] ?? '',
      className: json['className'] ?? '',
      description: json['description'] ?? '',
      teacherName: json['teacherName'] ?? '',
      studentCount: json['studentCount'] ?? 0,
    );
  }
}

class ClassResponse {
  final int classId;
  final String classCode;
  final String className;
  final String description;
  final User teacher;
  final List<User> students;
  final int studentCount;

  ClassResponse({
    required this.classId,
    required this.classCode,
    required this.className,
    required this.description,
    required this.teacher,
    required this.students,
    required this.studentCount,
  });

  factory ClassResponse.fromJson(Map<String, dynamic> json) {
    return ClassResponse(
      classId: json['classId'] ?? 0,
      classCode: json['classCode'] ?? '',
      className: json['className'] ?? '',
      description: json['description'] ?? '',
      teacher: User.fromJson(json['teacher'] ?? {}),
      students: (json['students'] as List? ?? []).map((e) => User.fromJson(e)).toList(),
      studentCount: json['studentCount'] ?? 0,
    );
  }
}

class CreateClassRequest {
  final String classCode;
  final String className;
  final String description;

  CreateClassRequest({
    required this.classCode,
    required this.className,
    required this.description,
  });

  Map<String, dynamic> toJson() {
    return {
      'classCode': classCode,
      'className': className,
      'description': description,
    };
  }
}

class ApiResponse<T> {
  final String message;
  final String status;
  final T? data;

  ApiResponse({
    required this.message,
    required this.status,
    this.data,
  });

  factory ApiResponse.fromJson(Map<String, dynamic> json, T Function(dynamic) fromJsonT) {
    return ApiResponse(
      message: json['message'] ?? '',
      status: json['status'] ?? '',
      data: json['data'] != null ? fromJsonT(json['data']) : null,
    );
  }

  bool get isSuccess => status == 'success';
}

class UpdateClassRequest {
  final String classCode;
  final String className;
  final String description;

  UpdateClassRequest({
    required this.classCode,
    required this.className,
    required this.description,
  });

  Map<String, dynamic> toJson() {
    return {
      'classCode': classCode,
      'className': className,
      'description': description,
    };
  }
}