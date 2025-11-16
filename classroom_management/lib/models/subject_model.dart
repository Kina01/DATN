// models/subject_model.dart
import 'user_model.dart';

class Subject {
  final int subjectId;
  final String subjectCode;
  final String subjectName;
  final int? credits;
  final User? createdBy;
  final String? createdAt;

  Subject({
    required this.subjectId,
    required this.subjectCode,
    required this.subjectName,
    this.credits,
    this.createdBy,
    this.createdAt,
  });

  factory Subject.fromJson(Map<String, dynamic> json) {
    return Subject(
      subjectId: json['subjectId'] ?? 0,
      subjectCode: json['subjectCode'] ?? '',
      subjectName: json['subjectName'] ?? '',
      credits: json['credits'],
      createdBy: json['createdBy'] != null ? User.fromJson(json['createdBy']) : null,
      createdAt: json['createdAt'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'subjectId': subjectId,
      'subjectCode': subjectCode,
      'subjectName': subjectName,
      'credits': credits,
      'createdBy': createdBy?.toJson(),
      'createdAt': createdAt,
    };
  }
}

class SubjectSummary {
  final int subjectId;
  final String subjectCode;
  final String subjectName;
  final int? credits;
  final String? teacherName;

  SubjectSummary({
    required this.subjectId,
    required this.subjectCode,
    required this.subjectName,
    this.credits,
    this.teacherName,
  });

  factory SubjectSummary.fromJson(Map<String, dynamic> json) {
    return SubjectSummary(
      subjectId: json['subjectId'] ?? 0,
      subjectCode: json['subjectCode'] ?? '',
      subjectName: json['subjectName'] ?? '',
      credits: json['credits'],
      teacherName: json['teacherName'] ?? json['createdBy']?['fullName'],
    );
  }
}

class CreateSubjectRequest {
  final String subjectCode;
  final String subjectName;
  final int? credits;

  CreateSubjectRequest({
    required this.subjectCode,
    required this.subjectName,
    this.credits,
  });

  Map<String, dynamic> toJson() {
    return {
      'subjectCode': subjectCode,
      'subjectName': subjectName,
      'credits': credits,
    };
  }
}

class UpdateSubjectRequest {
  final String subjectCode;
  final String subjectName;
  final int? credits;

  UpdateSubjectRequest({
    required this.subjectCode,
    required this.subjectName,
    this.credits,
  });

  Map<String, dynamic> toJson() {
    return {
      'subjectCode': subjectCode,
      'subjectName': subjectName,
      'credits': credits,
    };
  }
}