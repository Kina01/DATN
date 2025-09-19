import 'package:flutter/material.dart';

class HomeScreen extends StatefulWidget {
  final int userId;
  final String fullname;
  final String role;

  const HomeScreen({
    Key? key,
    required this.userId,
    required this.fullname,
    required this.role,
  }) : super(key: key);

  @override
  _HomeScreenState createState() => _HomeScreenState();
}


class _HomeScreenState extends State<HomeScreen> {
  

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Thời khóa biểu'),
        backgroundColor: Colors.blue,
        foregroundColor: Colors.white,
        // actions: [
        //   if (widget.role == 'SV')

        // ],
      ),
      body: const Center(
        child: Text(
          'Chào mừng bạn đến với lớp học',
          style: TextStyle(
            fontSize: 24,
            fontWeight: FontWeight.bold,
            color: Colors.blue,
          ),
        ),
      ),
    );
  }
}