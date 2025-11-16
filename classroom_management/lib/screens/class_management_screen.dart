import 'package:flutter/material.dart';
import '../models/user_model.dart';
import '../models/class_model.dart';
import '../services/class_service.dart';
import 'student_management_screen.dart';

class ClassManagementScreen extends StatefulWidget {
  final User user;

  const ClassManagementScreen({Key? key, required this.user}) : super(key: key);

  @override
  _ClassManagementScreenState createState() => _ClassManagementScreenState();
}

class _ClassManagementScreenState extends State<ClassManagementScreen> {
  List<ClassSummary> _classes = [];
  bool _isLoading = true;
  String _errorMessage = '';

  @override
  void initState() {
    super.initState();
    _loadClasses();
  }

  Future<void> _loadClasses() async {
    setState(() {
      _isLoading = true;
      _errorMessage = '';
    });

    final response = widget.user.isTeacher
        ? await ClassService.getTeacherClasses()
        : await ClassService.getStudentClasses();

    setState(() {
      _isLoading = false;
      if (response.isSuccess && response.data != null) {
        _classes = response.data!;
      } else {
        _errorMessage = response.message;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title:
            Text(widget.user.isTeacher ? 'Quản lý lớp học' : 'Lớp học của tôi'),
        backgroundColor: Colors.blue[700],
        foregroundColor: Colors.white,
        actions: widget.user.isTeacher
            ? [
                IconButton(
                  icon: Icon(Icons.add),
                  onPressed: () {
                    _showCreateClassDialog();
                  },
                ),
              ]
            : null,
      ),
      body: _buildBody(),
      // floatingActionButton: widget.user.isTeacher
      //     ? FloatingActionButton(
      //         onPressed: () {
      //           _showCreateClassDialog();
      //         },
      //         backgroundColor: Colors.blue[700],
      //         child: Icon(Icons.add, color: Colors.white),
      //       )
      //     : null,
    );
  }

  Widget _buildBody() {
    if (_isLoading) {
      return Center(
        child: CircularProgressIndicator(),
      );
    }

    if (_errorMessage.isNotEmpty) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.error, size: 64, color: Colors.grey),
            SizedBox(height: 16),
            Text(
              _errorMessage,
              style: TextStyle(fontSize: 16, color: Colors.grey),
              textAlign: TextAlign.center,
            ),
            SizedBox(height: 16),
            ElevatedButton(
              onPressed: _loadClasses,
              child: Text('Thử lại'),
            ),
          ],
        ),
      );
    }

    if (_classes.isEmpty) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.school, size: 64, color: Colors.grey),
            SizedBox(height: 16),
            Text(
              widget.user.isTeacher
                  ? 'Bạn chưa có lớp học nào'
                  : 'Bạn chưa đăng ký lớp học nào',
              style: TextStyle(fontSize: 16, color: Colors.grey),
            ),
            SizedBox(height: 16),
            if (widget.user.isTeacher)
              ElevatedButton(
                onPressed: () {
                  _showCreateClassDialog();
                },
                child: Text('Tạo lớp học đầu tiên'),
              ),
          ],
        ),
      );
    }

    return ListView.builder(
      padding: EdgeInsets.all(16),
      itemCount: _classes.length,
      itemBuilder: (context, index) {
        final classData = _classes[index];
        return _buildClassCard(classData);
      },
    );
  }

  Widget _buildClassCard(ClassSummary classData) {
    return Card(
      margin: EdgeInsets.only(bottom: 12),
      child: ListTile(
        contentPadding: EdgeInsets.all(16),
        leading: Container(
          width: 50,
          height: 50,
          decoration: BoxDecoration(
            color: Colors.blue[50],
            borderRadius: BorderRadius.circular(8),
          ),
          child: Icon(
            Icons.school,
            color: Colors.blue[700],
          ),
        ),
        title: Text(
          classData.className,
          style: TextStyle(
            fontWeight: FontWeight.bold,
            fontSize: 16,
          ),
        ),
        subtitle: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            SizedBox(height: 4),
            Text('Mã lớp: ${classData.classCode}'),
            if (widget.user.isTeacher)
              Text('Giáo viên: ${classData.teacherName}'),
            SizedBox(height: 8),
            Row(
              children: [
                Icon(Icons.people, size: 16, color: Colors.grey),
                SizedBox(width: 4),
                Text('${classData.studentCount} học sinh'),
              ],
            ),
          ],
        ),
        trailing: widget.user.isTeacher ? _buildClassMenu(classData) : null,
        onTap: () {
          _showClassDetail(classData);
        },
      ),
    );
  }

  Widget _buildClassMenu(ClassSummary classData) {
    return PopupMenuButton(
      itemBuilder: (context) => [
        PopupMenuItem(
          child: Row(
            children: [
              Icon(Icons.people, size: 20),
              SizedBox(width: 8),
              Text('Quản lý học sinh'),
            ],
          ),
          onTap: () {
            _manageStudents(classData);
          },
        ),
        PopupMenuItem(
          child: Row(
            children: [
              Icon(Icons.edit, size: 20),
              SizedBox(width: 8),
              Text('Chỉnh sửa'),
            ],
          ),
          onTap: () {
            _editClass(classData);
          },
        ),
        PopupMenuItem(
          child: Row(
            children: [
              Icon(Icons.delete, size: 20, color: Colors.red),
              SizedBox(width: 8),
              Text('Xóa', style: TextStyle(color: Colors.red)),
            ],
          ),
          onTap: () {
            _deleteClass(classData);
          },
        ),
      ],
    );
  }

  void _showCreateClassDialog() {
    final classCodeController = TextEditingController();
    final classNameController = TextEditingController();
    final descriptionController = TextEditingController();

    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text('Tạo lớp học mới'),
        content: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: classCodeController,
                decoration: InputDecoration(
                  labelText: 'Mã lớp',
                  hintText: 'VD: KTPM K44',
                ),
              ),
              SizedBox(height: 16),
              TextField(
                controller: classNameController,
                decoration: InputDecoration(
                  labelText: 'Tên lớp',
                  hintText: 'VD: Kỹ thuật phần mềm K44',
                ),
              ),
              SizedBox(height: 16),
              TextField(
                controller: descriptionController,
                maxLines: 3,
                decoration: InputDecoration(
                  labelText: 'Mô tả',
                  hintText: 'Mô tả về lớp học...',
                ),
              ),
            ],
          ),
        ),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.pop(context);
            },
            child: Text('Hủy'),
          ),
          ElevatedButton(
            onPressed: () async {
              if (classCodeController.text.isEmpty ||
                  classNameController.text.isEmpty) {
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(content: Text('Vui lòng nhập mã lớp và tên lớp')),
                );
                return;
              }

              final request = CreateClassRequest(
                classCode: classCodeController.text,
                className: classNameController.text,
                description: descriptionController.text,
              );

              final response = await ClassService.createClass(request);

              if (response.isSuccess) {
                Navigator.pop(context);
                _loadClasses();
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(content: Text('Tạo lớp học thành công!')),
                );
              } else {
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(content: Text(response.message)),
                );
              }
            },
            child: Text('Tạo'),
          ),
        ],
      ),
    );
  }

  void _showClassDetail(ClassSummary classData) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text(classData.className),
        content: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            mainAxisSize: MainAxisSize.min,
            children: [
              Text('Mã lớp: ${classData.classCode}'),
              SizedBox(height: 8),
              Text('Giáo viên: ${classData.teacherName}'),
              SizedBox(height: 8),
              Text('Số học sinh: ${classData.studentCount}'),
              SizedBox(height: 8),
              if (classData.description.isNotEmpty)
                Text('Mô tả: ${classData.description}'),
            ],
          ),
        ),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.pop(context);
            },
            child: Text('Đóng'),
          ),
        ],
      ),
    );
  }

  void _manageStudents(ClassSummary classData) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => StudentManagementScreen(
          classData: classData,
          onStudentsUpdated: _loadClasses,
        ),
      ),
    );
  }

  void _editClass(ClassSummary classData) {
    final classCodeController =
        TextEditingController(text: classData.classCode);
    final classNameController =
        TextEditingController(text: classData.className);
    final descriptionController =
        TextEditingController(text: classData.description);

    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text('Chỉnh sửa lớp học'),
        content: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: classCodeController,
                decoration: InputDecoration(
                  labelText: 'Mã lớp',
                  hintText: 'VD: KTPM K44',
                ),
              ),
              SizedBox(height: 16),
              TextField(
                controller: classNameController,
                decoration: InputDecoration(
                  labelText: 'Tên lớp',
                  hintText: 'VD: Kỹ Thuật Phần Mềm K44',
                ),
              ),
              SizedBox(height: 16),
              TextField(
                controller: descriptionController,
                maxLines: 3,
                decoration: InputDecoration(
                  labelText: 'Mô tả',
                  hintText: 'Mô tả về lớp học...',
                ),
              ),
            ],
          ),
        ),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.pop(context);
            },
            child: Text('Hủy'),
          ),
          ElevatedButton(
            onPressed: () async {
              if (classCodeController.text.isEmpty ||
                  classNameController.text.isEmpty) {
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(content: Text('Vui lòng nhập mã lớp và tên lớp')),
                );
                return;
              }

              final request = UpdateClassRequest(
                classCode: classCodeController.text,
                className: classNameController.text,
                description: descriptionController.text,
              );

              final response =
                  await ClassService.updateClass(classData.classId, request);

              if (response.isSuccess) {
                Navigator.pop(context);
                _loadClasses(); // Reload danh sách
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(content: Text('Cập nhật lớp học thành công!')),
                );
              } else {
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(content: Text(response.message)),
                );
              }
            },
            child: Text('Cập nhật'),
          ),
        ],
      ),
    );
  }

  void _deleteClass(ClassSummary classData) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text('Xóa lớp học'),
        content: Text('Bạn có chắc chắn muốn xóa lớp ${classData.className}?'),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.pop(context);
            },
            child: Text('Hủy'),
          ),
          ElevatedButton(
            onPressed: () async {
              Navigator.pop(context);
              final response =
                  await ClassService.deleteClass(classData.classId);

              if (response.isSuccess) {
                _loadClasses(); // Reload danh sách
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(content: Text('Xóa lớp học thành công!')),
                );
              } else {
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(content: Text(response.message)),
                );
              }
            },
            style: ElevatedButton.styleFrom(backgroundColor: Colors.red),
            child: Text('Xóa'),
          ),
        ],
      ),
    );
  }
}
