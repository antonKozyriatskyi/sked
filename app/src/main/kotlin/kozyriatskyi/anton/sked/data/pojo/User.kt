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
}

class Teacher : User() {
    var department = ""
    var departmentId = ""
    var teacher = ""
    var teacherId = ""

    override val name: String
        get() = teacher
}