
import 'package:flutter/material.dart';
import '../models/user_model.dart';

class NotificationScreen extends StatefulWidget {
  final User user;
  
  const NotificationScreen({Key? key, required this.user}) : super(key: key);
  
  @override
  _NotificationScreenState createState() => _NotificationScreenState();
}

class _NotificationScreenState extends State<NotificationScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Thông báo'),
        backgroundColor: Colors.blue[700],
        foregroundColor: Colors.white,
      ),
      body: Center(
        child: Text('Màn hình thông báo - ${widget.user.fullName}'),
      ),
    );
  }
}