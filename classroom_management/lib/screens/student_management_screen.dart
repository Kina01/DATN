import 'package:flutter/material.dart';
import '../models/user_model.dart';
import '../models/class_model.dart';
import '../services/student_service.dart';

class StudentManagementScreen extends StatefulWidget {
  final ClassSummary classData;
  final VoidCallback onStudentsUpdated;

  const StudentManagementScreen({
    Key? key,
    required this.classData,
    required this.onStudentsUpdated,
  }) : super(key: key);

  @override
  _StudentManagementScreenState createState() =>
      _StudentManagementScreenState();
}

class _StudentManagementScreenState extends State<StudentManagementScreen> {
  List<User> _students = [];
  List<User> _availableStudents = [];
  bool _isLoading = true;
  bool _isAddingStudent = false;
  String _errorMessage = '';
  String _searchQuery = '';

  @override
  void initState() {
    super.initState();
    _loadStudents();
    _loadAvailableStudents();
  }

  Future<void> _loadStudents() async {
    setState(() {
      _isLoading = true;
      _errorMessage = '';
    });

    final response =
        await StudentService.getStudentsInClass(widget.classData.classId);

    setState(() {
      _isLoading = false;
      if (response.isSuccess && response.data != null) {
        _students = response.data!;
      } else {
        _errorMessage = response.message;
      }
    });
  }

  Future<void> _loadAvailableStudents() async {
    await _searchStudents('');
  }

  Future<void> _searchStudents(String query) async {
    setState(() {
      _searchQuery = query;
      _isLoading = true;
    });

    final response = await StudentService.searchStudents(query);

    setState(() {
      _isLoading = false;
      if (response.isSuccess && response.data != null) {
        _availableStudents = response.data!;
      } else {
        _availableStudents = [];
        // Chỉ hiển thị lỗi khi có query (không hiển thị khi load lần đầu)
        if (query.isNotEmpty) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('Lỗi tìm kiếm: ${response.message}')),
          );
        }
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Quản lý sinh viên - ${widget.classData.className}'),
        backgroundColor: Colors.blue[700],
        foregroundColor: Colors.white,
        actions: [
          IconButton(
            icon: Icon(Icons.person_add),
            onPressed: () {
              _showAddStudentDialog();
            },
          ),
        ],
      ),
      body: Column(
        children: [
          Expanded(
            child: _buildStudentList(),
          ),
        ],
      ),
    );
  }

  Widget _buildStatItem(String title, String value, IconData icon) {
    return Column(
      children: [
        Icon(icon, color: Colors.blue[700], size: 24),
        SizedBox(height: 4),
        Text(
          value,
          style: TextStyle(
            fontSize: 18,
            fontWeight: FontWeight.bold,
            color: Colors.blue[700],
          ),
        ),
        Text(
          title,
          style: TextStyle(
            fontSize: 12,
            color: Colors.grey[600],
          ),
        ),
      ],
    );
  }

  Widget _buildStudentList() {
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
              onPressed: _loadStudents,
              child: Text('Thử lại'),
            ),
          ],
        ),
      );
    }

    if (_students.isEmpty) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.people_outline, size: 64, color: Colors.grey),
            SizedBox(height: 16),
            Text(
              'Chưa có sinh viên nào trong lớp',
              style: TextStyle(fontSize: 16, color: Colors.grey),
            ),
            SizedBox(height: 16),
            ElevatedButton(
              onPressed: () {
                _showAddStudentDialog();
              },
              child: Text('Thêm sinh viên đầu tiên'),
            ),
          ],
        ),
      );
    }

    return ListView.builder(
      padding: EdgeInsets.all(16),
      itemCount: _students.length,
      itemBuilder: (context, index) {
        final student = _students[index];
        return _buildStudentCard(student);
      },
    );
  }

  Widget _buildStudentCard(User student) {
    return Card(
      margin: EdgeInsets.only(bottom: 8),
      child: ListTile(
        contentPadding: EdgeInsets.all(12),
        leading: CircleAvatar(
          backgroundColor: Colors.blue[100],
          child: Icon(
            Icons.person,
            color: Colors.blue[700],
          ),
        ),
        title: Text(
          student.fullName,
          style: TextStyle(
            fontWeight: FontWeight.w500,
          ),
        ),
        subtitle: Text(student.email),
        trailing: IconButton(
          icon: Icon(Icons.delete, color: Colors.red),
          onPressed: () {
            _removeStudent(student);
          },
        ),
        onTap: () {
          _showStudentDetail(student);
        },
      ),
    );
  }

  void _showAddStudentDialog() {
    showDialog(
      context: context,
      builder: (context) => StatefulBuilder(
        builder: (context, setDialogState) {
          return AlertDialog(
            title: Text('Thêm sinh viên vào lớp'),
            content: Container(
              width: double.maxFinite,
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  // Search bar - KHÔNG cần setDialogState ở đây
                  TextField(
                    decoration: InputDecoration(
                      hintText: 'Tìm kiếm sinh viên...',
                      prefixIcon: Icon(Icons.search),
                      border: OutlineInputBorder(),
                    ),
                    onChanged: (value) async {
                      await _searchStudents(value);
                      // CHỈ rebuild phần danh sách
                      setDialogState(() {});
                    },
                  ),
                  SizedBox(height: 16),

                  // Available students list - CHỈ phần này cần rebuild
                  if (_isAddingStudent)
                    Center(child: CircularProgressIndicator())
                  else
                    Container(
                      height: 300,
                      child: _availableStudents.isEmpty
                          ? Center(
                              child: Text(
                                _searchQuery.isEmpty
                                    ? 'Không có sinh viên nào'
                                    : 'Không tìm thấy sinh viên phù hợp',
                                style: TextStyle(color: Colors.grey),
                              ),
                            )
                          : ListView.builder(
                              itemCount: _availableStudents.length,
                              itemBuilder: (context, index) {
                                final student = _availableStudents[index];
                                final isAlreadyInClass = _students
                                    .any((s) => s.userId == student.userId);

                                return ListTile(
                                  leading: CircleAvatar(
                                    backgroundColor: Colors.green[100],
                                    child: Icon(
                                      Icons.person,
                                      color: Colors.green[700],
                                    ),
                                  ),
                                  title: Text(student.fullName),
                                  subtitle: Text(student.email),
                                  trailing: isAlreadyInClass
                                      ? Container(
                                          padding: const EdgeInsets.symmetric(
                                              horizontal: 8, vertical: 4),
                                          decoration: BoxDecoration(
                                            color: Colors.grey[300],
                                            borderRadius:
                                                BorderRadius.circular(12),
                                          ),
                                          child: const Text(
                                            'Đã thêm',
                                            style: TextStyle(fontSize: 12),
                                          ),
                                        )
                                      : ElevatedButton(
                                          onPressed: () {
                                            _addStudentToClass(student);
                                          },
                                          child: const Text('Thêm'),
                                        ),
                                );
                              },
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
                child: Text('Đóng'),
              ),
            ],
          );
        },
      ),
    );
  }

  void _addStudentToClass(User student) async {
    setState(() {
      _isAddingStudent = true;
    });

    final response = await StudentService.addStudentToClass(
      widget.classData.classId,
      student.userId,
    );

    setState(() {
      _isAddingStudent = false;
    });

    if (response.isSuccess) {
      Navigator.pop(context); // Đóng dialog
      _loadStudents(); // Reload danh sách
      widget.onStudentsUpdated();
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('Đã thêm ${student.fullName} vào lớp'),
          backgroundColor: Colors.green,
        ),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(response.message),
          backgroundColor: Colors.red,
        ),
      );
    }
  }

  void _removeStudent(User student) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text('Xóa sinh viên'),
        content: Text(
            'Bạn có chắc chắn muốn xóa ${student.fullName} khỏi lớp ${widget.classData.className}?'),
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

              final response = await StudentService.removeStudentFromClass(
                widget.classData.classId,
                student.userId,
              );

              if (response.isSuccess) {
                _loadStudents(); // Reload danh sách
                widget.onStudentsUpdated();
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text('Đã xóa ${student.fullName} khỏi lớp'),
                    backgroundColor: Colors.green,
                  ),
                );
              } else {
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text(response.message),
                    backgroundColor: Colors.red,
                  ),
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

  void _showStudentDetail(User student) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text('Thông tin sinh viên'),
        content: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            mainAxisSize: MainAxisSize.min,
            children: [
              Center(
                child: CircleAvatar(
                  radius: 40,
                  backgroundColor: Colors.blue[100],
                  child: Icon(
                    Icons.person,
                    size: 40,
                    color: Colors.blue[700],
                  ),
                ),
              ),
              SizedBox(height: 16),
              _buildDetailItem('Họ tên', student.fullName),
              _buildDetailItem('Email', student.email),
              _buildDetailItem('Vai trò', 'Sinh viên'),
              _buildDetailItem('Lớp', widget.classData.className),
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

  Widget _buildDetailItem(String label, String value) {
    return Padding(
      padding: EdgeInsets.symmetric(vertical: 4),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            '$label: ',
            style: TextStyle(
              fontWeight: FontWeight.bold,
              color: Colors.grey[700],
            ),
          ),
          Expanded(
            child: Text(value),
          ),
        ],
      ),
    );
  }
}
