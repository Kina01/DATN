class TimeTable {
  final String subjectName;
  final String className;
  final String room;
  final int dayOfWeek;
  final int periodStart;
  final int periodEnd;

  TimeTable({
    required this.subjectName,
    required this.className,
    required this.room,
    required this.dayOfWeek,
    required this.periodStart,
    required this.periodEnd,
  });

  Map<String, dynamic> toJson() {
    return {
      'subjectName': subjectName,
      'className': className,
      'room': room,
      'dayOfWeek': dayOfWeek,
      'periodStart': periodStart,
      'periodEnd': periodEnd
    };
  }
}


// // time_table_model.dart
// import 'user_model.dart'; // Import file user model của bạn

// class TimeTableEntity {
//   int? idTimeTable;
//   String subjectName;
//   String className;
//   String room;
//   int dayOfWeek;
//   int periodStart;
//   int periodEnd;
//   User? user; // Sử dụng User model đã có

//   TimeTableEntity({
//     this.idTimeTable,
//     required this.subjectName,
//     required this.className,
//     required this.room,
//     required this.dayOfWeek,
//     required this.periodStart,
//     required this.periodEnd,
//     this.user,
//   });

//   // Factory method to create a TimeTableEntity from JSON
//   factory TimeTableEntity.fromJson(Map<String, dynamic> json) {
//     return TimeTableEntity(
//       idTimeTable: json['idTimeTable'],
//       subjectName: json['subjectName'] ?? '',
//       className: json['className'] ?? '',
//       room: json['room'] ?? '',
//       dayOfWeek: json['dayOfWeek'] ?? 0,
//       periodStart: json['periodStart'] ?? 0,
//       periodEnd: json['periodEnd'] ?? 0,
//       user: json['user'] != null ? User.fromJson(json['user']) : null,
//     );
//   }

//   // Convert TimeTableEntity to JSON
//   Map<String, dynamic> toJson() {
//     return {
//       'idTimeTable': idTimeTable,
//       'subjectName': subjectName,
//       'className': className,
//       'room': room,
//       'dayOfWeek': dayOfWeek,
//       'periodStart': periodStart,
//       'periodEnd': periodEnd,
//       'user': user?.toJson(),
//     };
//   }

//   // Copy with method for easy updates
//   TimeTableEntity copyWith({
//     int? idTimeTable,
//     String? subjectName,
//     String? className,
//     String? room,
//     int? dayOfWeek,
//     int? periodStart,
//     int? periodEnd,
//     User? user,
//   }) {
//     return TimeTableEntity(
//       idTimeTable: idTimeTable ?? this.idTimeTable,
//       subjectName: subjectName ?? this.subjectName,
//       className: className ?? this.className,
//       room: room ?? this.room,
//       dayOfWeek: dayOfWeek ?? this.dayOfWeek,
//       periodStart: periodStart ?? this.periodStart,
//       periodEnd: periodEnd ?? this.periodEnd,
//       user: user ?? this.user,
//     );
//   }

//   @override
//   String toString() {
//     return 'TimeTableEntity{idTimeTable: $idTimeTable, subjectName: $subjectName, className: $className, room: $room, dayOfWeek: $dayOfWeek, periodStart: $periodStart, periodEnd: $periodEnd, user: $user}';
//   }

//   @override
//   bool operator ==(Object other) =>
//       identical(this, other) ||
//       other is TimeTableEntity &&
//           runtimeType == other.runtimeType &&
//           idTimeTable == other.idTimeTable &&
//           subjectName == other.subjectName &&
//           className == other.className &&
//           room == other.room &&
//           dayOfWeek == other.dayOfWeek &&
//           periodStart == other.periodStart &&
//           periodEnd == other.periodEnd;

//   @override
//   int get hashCode =>
//       idTimeTable.hashCode ^
//       subjectName.hashCode ^
//       className.hashCode ^
//       room.hashCode ^
//       dayOfWeek.hashCode ^
//       periodStart.hashCode ^
//       periodEnd.hashCode;
// }

// // Helper class for creating time table requests (if you need to send only user ID)
// class TimeTableRequest {
//   String subjectName;
//   String className;
//   String room;
//   int dayOfWeek;
//   int periodStart;
//   int periodEnd;
//   int userId;

//   TimeTableRequest({
//     required this.subjectName,
//     required this.className,
//     required this.room,
//     required this.dayOfWeek,
//     required this.periodStart,
//     required this.periodEnd,
//     required this.userId,
//   });

//   Map<String, dynamic> toJson() {
//     return {
//       'subjectName': subjectName,
//       'className': className,
//       'room': room,
//       'dayOfWeek': dayOfWeek,
//       'periodStart': periodStart,
//       'periodEnd': periodEnd,
//       'user': {'id': userId},
//     };
//   }
// }

// // Extension methods for convenience
// extension TimeTableExtensions on TimeTableEntity {
//   String get dayOfWeekName {
//     switch (dayOfWeek) {
//       case 2:
//         return 'Thứ 2';
//       case 3:
//         return 'Thứ 3';
//       case 4:
//         return 'Thứ 4';
//       case 5:
//         return 'Thứ 5';
//       case 6:
//         return 'Thứ 6';
//       case 7:
//         return 'Thứ 7';
//       case 8:
//         return 'Chủ nhật';
//       default:
//         return 'Unknown';
//     }
//   }

//   String get periodRange {
//     return 'Tiết $periodStart - $periodEnd';
//   }

//   Duration get startTime {
//     // Giả sử mỗi tiết học 45 phút, bắt đầu từ 7:00
//     final startHour = 7 + ((periodStart - 1) * 45 ~/ 60);
//     final startMinute = ((periodStart - 1) * 45) % 60;
//     return Duration(hours: startHour, minutes: startMinute);
//   }

//   Duration get endTime {
//     // Giả sử mỗi tiết học 45 phút
//     final endHour = 7 + (periodEnd * 45 ~/ 60);
//     final endMinute = (periodEnd * 45) % 60;
//     return Duration(hours: endHour, minutes: endMinute);
//   }

//   String get timeRange {
//     final start = startTime;
//     final end = endTime;
//     return '${start.inHours}:${start.inMinutes.remainder(60).toString().padLeft(2, '0')} - ${end.inHours}:${end.inMinutes.remainder(60).toString().padLeft(2, '0')}';
//   }

//   bool conflictsWith(TimeTableEntity other) {
//     if (dayOfWeek != other.dayOfWeek) return false;
//     return periodStart <= other.periodEnd && periodEnd >= other.periodStart;
//   }
// }

// // Helper class for API responses
// class TimeTableResponse {
//   final bool success;
//   final String message;
//   final TimeTableEntity? data;

//   TimeTableResponse({
//     required this.success,
//     required this.message,
//     this.data,
//   });

//   factory TimeTableResponse.fromJson(Map<String, dynamic> json) {
//     return TimeTableResponse(
//       success: json['status'] == 'success',
//       message: json['message'] ?? '',
//       data: json['data'] != null ? TimeTableEntity.fromJson(json['data']) : null,
//     );
//   }
// }