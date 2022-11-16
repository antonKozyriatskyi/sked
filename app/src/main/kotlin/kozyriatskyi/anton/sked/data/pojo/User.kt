package kozyriatskyi.anton.sked.data.pojo

/**
 * Created by Anton on 08.07.2017.
 */

sealed class User {
    abstract val name: String
}

class Student : User() {
    var faculty = ""
    var facultyId = ""
    var course = ""
    var courseId = ""
    var group = ""
    var groupId = ""

    override val name: String
        get() = group

    override fun toString(): String {
        return "Student(faculty='$faculty', facultyId='$facultyId', course='$course', courseId='$courseId', group='$group', groupId='$groupId')"
    }
}

class Teacher : User() {
    var department = ""
    var departmentId = ""
    var teacher = ""
    var teacherId = ""

    override val name: String
        get() = teacher

    override fun toString(): String {
        return "Teacher(department='$department', departmentId='$departmentId', teacher='$teacher', teacherId='$teacherId')"
    }
}