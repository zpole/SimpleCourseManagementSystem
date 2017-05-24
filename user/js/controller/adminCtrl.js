var app = angular.module('app',[]);
app.controller("adminCtrl",function($scope,$http){
//用户名
	$http.post("/course/user/info")
	.success(function(result){
		if(result.errorCode ==100){
			$scope.userName = result.value.name;
		}
		else if(result.errorCode == 404){
			alert(result.value)
			window.location.replace("/user/login.html");
		}
		else{
			alert(result.value)
		}
	})
//学生列表
	$scope.loadStudent = function(){
		$http.post("/course/admin/studentList")
		.success(function(result){
			if(result.errorCode == 100){
				$scope.studentList = result.value;
				console.log(result)
			}
			else{
				alert(result.value)
			}
		})
	}
	$scope.loadStudent();
//添加学生
	$scope.saveStudent = function(){
		var data = {};
		data.name = $scope.addStudentName;
		data.number = parseInt($scope.addStudentNumber);
		data.sex = $scope.addStudentSex;
		data.pwd = $scope.addStudentPwd;
		data.icon = "";
		$http.post("/course/admin/saveStudent",data)
		.success(function(result){
			if(result.errorCode == 100){
				$('.modal').modal('hide');
				$scope.loadStudent();
				$scope.addStudentName = '';
				$scope.addStudentNumber ='';
				$scope.addStudentSex = '';
				$scope.addStudentPwd = ''
			}
			else{
				alert(result.value)
			}

		})
	}
//编辑学生
	$scope.editStudent = function($index){
		var data = {};
		$scope.editStudentNumber = parseInt($scope.studentList[$index].number);
		$scope.editStudentName = $scope.studentList[$index].name;
		$scope.editStudentSex = $scope.studentList[$index].sex;
		$scope.editStudentPwd = '';
		data.icon = "";
		$scope.saveEditStudent = function(){
			data.id=$scope.studentList[$index].id;
			data.number = parseInt($scope.editStudentNumber);
			data.name = $scope.editStudentName;
			data.sex = $scope.editStudentSex;
			data.pwd = $scope.editStudentPwd;
			console.log(data);
			$http.post("/course/admin/saveStudent",data)
			.success(function(result){
				if(result.errorCode == 100){
					$('.modal').modal('hide');
					$scope.loadStudent();
					$scope.editStudentName = '';
					$scope.editStudentNumber ='';
					$scope.editStudentSex = '';
					$scope.editStudentPwd = '';
				}
				else{
					alert(result.value)
				}
			})
		}
	}
//删除学生
	$scope.wantDel = function($index){
		var id = $scope.studentList[$index].id;
		console.log(id);
		$scope.del = function(){
			$http.post("/course/admin/deleteStudent/"+id)
			.success(function(result){
				if(result.errorCode == 100){
					$('.modal').modal('hide');
					$scope.loadStudent();
				}
				else{
					alert(result.value)
				}
			})
		}
	}

//教师列表
	$scope.loadTeacher = function(){
		$http.post("/course/admin/teacherList")
		.success(function(result){
			if(result.errorCode == 100){
				$scope.teacherList = result.value;
				$scope.addLessonTeacher = $scope.teacherList[0].id
			}
			else{
				alert(result.value)
			}
		})
	}
	$scope.loadTeacher();
//添加教师
	$scope.saveTeacher = function(){
		var data = {};
		data.name = $scope.addTeacherName;
		data.account = parseInt($scope.addTeacherAccount);
		data.pwd = $scope.addTeacherPwd;
		console.log(data)
		$http.post("/course/admin/saveTeacher",data)
		.success(function(result){
			if(result.errorCode == 100){
				$('.modal').modal('hide');
				$scope.loadTeacher();
				$scope.addTeacherName = '';
				$scope.addTeacherAccount ='';
				$scope.addTeacherPwd = ''
			}
			else{
				alert(result.msg);
			}

		})
	}
//编辑教师
	$scope.editTeacher = function($index){
		var data = {};
		$scope.editTeacherAccount = parseInt($scope.teacherList[$index].account);
		$scope.editTeacherName = $scope.teacherList[$index].name;
		$scope.saveEditTeacher = function(){
			data.id=$scope.teacherList[$index].id;
			data.account = parseInt($scope.editTeacherAccount);
			data.name = $scope.editTeacherName;
			data.pwd = $scope.editTeacherPwd;
			console.log(data);
			$http.post("/course/admin/saveTeacher",data)
			.success(function(result){
				if(result.errorCode == 100){
					$('.modal').modal('hide');
					$scope.loadTeacher();
					$scope.editTeacherName = '';
					$scope.editTeacherAccount ='';
					$scope.editTeacherPwd = ''
				}
				else{
					alert(result.value)
				}

			})
		}

	}
//删除教师
	$scope.wantDelT = function($index){
		var id = $scope.teacherList[$index].id;
		console.log(id);
		$scope.delT = function(){
			$http.post("/course/admin/deleteTeacher/"+id)
			.success(function(result){
				if(result.errorCode == 100){
					$('.modal').modal('hide');
					$scope.loadTeacher();
				}
				else{
					alert(result.value)
				}

			})
		}
	}

//课程列表
	$scope.loadLesson = function(){
		$http.post("/course/course/list")
		.success(function(result){
			if(result.errorCode == 100){
				$scope.lessonList = result.value;

			}
			else{
				alert(result.value)
			}
		})
	}
	$scope.loadLesson();
//新建课程
	$scope.saveLesson = function(){
		var data = {};
		data.name = $scope.addLessonName;
		data.teacherId = $scope.addLessonTeacher;
		$http.post("/course/admin/saveCourse",data)
		.success(function(result){
			if(result.errorCode == 100){
				$('.modal').modal('hide');
				$scope.loadLesson();
			}
			else{
				alert(result.value)
			}
		})
	}
//编辑课程
	$scope.editLesson = function($index){
		$scope.editLessonName = $scope.lessonList[$index].name;
		$scope.editLessonTeacher = $scope.lessonList[$index].teacherId;
		$scope.saveEditLesson= function(){
			var data = {};
			data.id = $scope.lessonList[$index].id;
			data.name = $scope.editLessonName;
			data.teacherId = $scope.editLessonTeacher;
			$http.post("/course/admin/saveCourse",data)
			.success(function(result){
				if(result.errorCode == 100){
					$('.modal').modal('hide');
					$scope.loadLesson();
				}
				else{
					alert(result.value)
				}
			})
		}
	}
//删除课程
	$scope.wantDelL = function($index){
		var id = $scope.lessonList[$index].id;
		$scope.delL = function(){
			$http.post("/course/admin/deleteCourse/"+id)
			.success(function(result){
				if(result.errorCode == 100){
					$('.modal').modal('hide');
					$scope.loadLesson();
				}
				else{
					alert(result.value)
				}
			})
		}
	}
//课程查看学生
	$scope.watchStudent = function($index){
		$scope.lessonId = $scope.lessonList[$index].id;
		$scope.lessonStudent = $scope.lessonList[$index].students;
	}
//课程查看成绩分布
	$scope.watchGrades = function($index){
		var id = $scope.lessonList[$index].id;
		$http.post("/course/course/"+id+"/grades")
		.success(function(result){
			if(result.errorCode == 100){
				$scope.grades = result.value;
				$scope.grades100 = $scope.grades["100"];
				$scope.grades90 = $scope.grades["90-99"];
				$scope.grades80 = $scope.grades["80-89"];
				$scope.grades70 = $scope.grades["70-79"];
				$scope.grades60 = $scope.grades["60-69"];
				$scope.grades59 = $scope.grades["0-59"];
				$scope.gradesAvg = $scope.grades.avg;
			}
			else{
				alert(result.value)
			}
		})
	}
//课程删学生
	$scope.wantDelLessonStudent = function($index){
		$scope.selectDel = true;
		$scope.delLessonStudent = function(){
			var data = {};
			data.courserId = $scope.lessonId;
			data.studentId = $scope.lessonStudent[$index].id;
			console.log(data);
			$http.post("/course/admin/deleteCourseStudent?courseId="+data.courserId+"&studentId="+data.studentId,"")
			.success(function(result){
				if(result.errorCode == 100){
					alert("删除成功");
				}
				else{
					alert(result.value)
				}
			})
		}

	}
//课程加学生
	$scope.lessonAddStudent = function($index){
		$scope.selectLessonStudent = $scope.studentList[0].id;
		$scope.saveLessonStudent = function(){
			var data = {};
			data.courseId = $scope.lessonList[$index].id;
			data.studentId = $scope.selectLessonStudent;
			$http.post("/course/admin/addStudentCourse",data)
			.success(function(result){
				if(result.errorCode == 100){
					$('.modal').modal('hide');
					$scope.loadLesson();
				}
				else{
					alert(result.msg)
				}
			})
		}
	}

//修改密码
	$scope.savePwd = function(){
		var data = {};
		data.pwd = $scope.newPwd;
		data.oldPwd = $scope.oldPwd
		$http.post("/course/admin/changePwd",data)
		.success(function(result){
			if(result.errorCode == 100){
				alert("修改成功");
				$('.modal').modal('hide');
			}
			else{
				alert(result.value)
			}

		})
	}
//退出
	$scope.exit = function(){
		$http.post("/course/user/exit")
		.success(function(result){
			if(result.errorCode == 100){
				window.location.replace("/user/login.html");
			}
			else{
				alert(result.value)
			}
		})
	}
})
