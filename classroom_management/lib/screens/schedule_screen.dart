import 'package:flutter/material.dart';
import '../models/user_model.dart';

class ScheduleScreen extends StatefulWidget {
  final User user;
  
  const ScheduleScreen({Key? key, required this.user}) : super(key: key);
  
  @override
  _ScheduleScreenState createState() => _ScheduleScreenState();
}

class _ScheduleScreenState extends State<ScheduleScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Lịch học'),
        backgroundColor: Colors.blue[700],
        foregroundColor: Colors.white,
      ),
      body: Center(
        child: Text('Màn hình lịch học - ${widget.user.fullName}'),
      ),
    );
  }
}