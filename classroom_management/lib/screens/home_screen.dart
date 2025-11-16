import 'package:flutter/material.dart';
import '../models/user_model.dart';
import 'class_management_screen.dart';
import 'subject_management_screen.dart';

class HomeScreen extends StatelessWidget {
  final User user;

  HomeScreen({Key? key, required this.user}) : super(key: key);

  final List<Map<String, dynamic>> quickActions = [
    {
      'icon': Icons.group,
      'title': 'Lớp học',
      'color': Colors.blue,
      'onTap': (context, user) {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => ClassManagementScreen(user: user),
          ),
        );
      },
    },
    {
      'icon': Icons.assignment,
      'title': 'Điểm danh',
      'color': Colors.green,
      'route': '/attendance',
    },
    {
      'icon': Icons.grade,
      'title': 'Điểm số',
      'color': Colors.orange,
      'route': '/grades',
    },
    {
      'icon': Icons.calendar_today,
      'title': 'Lịch thi',
      'color': Colors.purple,
      'route': '/exams',
    },
    {
      'icon': Icons.subject,
      'title': 'Môn học',
      'color': Colors.purple,
      'onTap': (context, user) {
        if (user.isTeacher) {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => SubjectManagementScreen(user: user),
            ),
          );
        }
      },
    },

  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Trang chủ'),
        backgroundColor: Colors.blue[700],
        foregroundColor: Colors.white,
        elevation: 0,
      ),
      body: SingleChildScrollView(
        padding: EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              padding: EdgeInsets.all(20),
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  begin: Alignment.topLeft,
                  end: Alignment.bottomRight,
                  colors: [Colors.blue[700]!, Colors.blue[500]!],
                ),
                borderRadius: BorderRadius.circular(16),
              ),
              child: Row(
                children: [
                  CircleAvatar(
                    radius: 30,
                    backgroundColor: Colors.white,
                    child: Icon(
                      Icons.person,
                      size: 30,
                      color: Colors.blue[700],
                    ),
                  ),
                  SizedBox(width: 16),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        SizedBox(height: 4),
                        Text(
                          user.fullName,
                          style: TextStyle(
                            color: Colors.white,
                            fontSize: 20,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        SizedBox(height: 4),
                        Container(
                          padding:
                              EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                          decoration: BoxDecoration(
                            color: Colors.white.withOpacity(0.2),
                            borderRadius: BorderRadius.circular(12),
                          ),
                          child: Text(
                            user.isTeacher ? 'Giáo viên' : 'Sinh viên',
                            style: TextStyle(
                              color: Colors.white,
                              fontSize: 12,
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
            SizedBox(height: 24),
            Text(
              'Các chức năng',
              style: TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
                color: Colors.grey[800],
              ),
            ),
            SizedBox(height: 16),
            GridView.builder(
              shrinkWrap: true,
              physics: NeverScrollableScrollPhysics(),
              gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 2,
                crossAxisSpacing: 12,
                mainAxisSpacing: 12,
                childAspectRatio: 1.2,
              ),
              itemCount: quickActions.length,
              itemBuilder: (context, index) {
                final action = quickActions[index];
                return Card(
                  elevation: 2,
                  child: InkWell(
                    onTap: () {
                      action['onTap'](context, user);
                    },
                    borderRadius: BorderRadius.circular(12),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Container(
                          padding: EdgeInsets.all(12),
                          decoration: BoxDecoration(
                            color: action['color'].withOpacity(0.1),
                            shape: BoxShape.circle,
                          ),
                          child: Icon(
                            action['icon'],
                            color: action['color'],
                            size: 24,
                          ),
                        ),
                        SizedBox(height: 8),
                        Text(
                          action['title'],
                          style: TextStyle(
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                      ],
                    ),
                  ),
                );
              },
            ),
          ],
        ),
      ),
    );
  }
}
