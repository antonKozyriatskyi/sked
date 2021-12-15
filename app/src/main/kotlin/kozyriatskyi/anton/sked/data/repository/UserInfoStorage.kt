package kozyriatskyi.anton.sked.data.repository

import android.content.SharedPreferences
import kozyriatskyi.anton.sked.data.pojo.Student
import kozyriatskyi.anton.sked.data.pojo.Teacher
import kozyriatskyi.anton.sked.data.pojo.User
import kozyriatskyi.anton.sked.util.edit

/**
 * Created by Anton on 14.07.2017.
 */

class UserInfoStorage(private val preferences: SharedPreferences) {

    companion object {
        const val KEY_FACULTY = "faculty"
        const val KEY_FACULTY_ID = "faculty_id"
        const val KEY_COURSE = "course"
        const val KEY_COURSE_ID = "course_id"
        const val KEY_GROUP = "group"
        const val KEY_GROUP_ID = "group_id"

        const val KEY_DEPARTMENT = "department"
        const val KEY_DEPARTMENT_ID = "department_id"
        const val KEY_TEACHER = "teacher"
        const val KEY_TEACHER_ID = "teacher_id"

        const val KEY_USER_TYPE = "user_type"
        const val TYPE_STUDENT = "type_student"
        const val TYPE_TEACHER = "type_teacher"

        const val KEY_FIRST_LAUNCH = "first_launch"
    }


    fun saveUser(user: User) {
        when (user) {
            is Student -> preferences.edit {
                putString(KEY_FACULTY, user.faculty)
                putString(KEY_FACULTY_ID, user.facultyId)
                putString(KEY_COURSE, user.course)
                putString(KEY_COURSE_ID, user.courseId)
                putString(KEY_GROUP, user.group)
                putString(KEY_GROUP_ID, user.groupId)
                putString(KEY_USER_TYPE, TYPE_STUDENT)
                putBoolean(KEY_FIRST_LAUNCH, false)
            }
            is Teacher -> preferences.edit {
                putString(KEY_DEPARTMENT, user.department)
                putString(KEY_DEPARTMENT_ID, user.departmentId)
                putString(KEY_TEACHER, user.teacher)
                putString(KEY_TEACHER_ID, user.teacherId)
                putString(KEY_USER_TYPE, TYPE_TEACHER)
                putBoolean(KEY_FIRST_LAUNCH, false)
            }
        }
    }

    fun getUser(): User = when (preferences.getString(KEY_USER_TYPE, "")) {
        TYPE_STUDENT -> Student().apply {
            this.faculty = preferences.getString(KEY_FACULTY, "")!!
            this.facultyId = preferences.getString(KEY_FACULTY_ID, "")!!
            this.course = preferences.getString(KEY_COURSE, "")!!
            this.courseId = preferences.getString(KEY_COURSE_ID, "")!!
            this.group = preferences.getString(KEY_GROUP, "")!!
            this.groupId = preferences.getString(KEY_GROUP_ID, "")!!
        }
        TYPE_TEACHER -> Teacher().apply {
            this.department = preferences.getString(KEY_DEPARTMENT, "")!!
            this.departmentId = preferences.getString(KEY_DEPARTMENT_ID, "")!!
            this.teacher = preferences.getString(KEY_TEACHER, "")!!
            this.teacherId = preferences.getString(KEY_TEACHER_ID, "")!!
        }
        else -> throw IllegalStateException("Wrong user type. Must be either TYPE_STUDENT or TYPE_TEACHER!")
    }

    fun getUserName(): String = getUser().name

    fun hasUser(): Boolean = preferences.contains(KEY_USER_TYPE)

}
