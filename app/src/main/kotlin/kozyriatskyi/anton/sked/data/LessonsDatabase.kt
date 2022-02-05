package kozyriatskyi.anton.sked.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kozyriatskyi.anton.sked.data.pojo.LessonDb
import kozyriatskyi.anton.sked.util.logI


/**
 * Created by Anton on 27.07.2017.
 */

private const val DB_VERSION = 4
private const val DB_NAME = "lessons.db"

@Database(entities = [LessonDb::class], version = DB_VERSION)
@TypeConverters(LocalDateSqliteConverter::class)
abstract class LessonsDatabase : RoomDatabase() {

    companion object {

        private var INSTANCE: LessonsDatabase? = null

        fun getInstance(context: Context): LessonsDatabase {
            if (INSTANCE == null) {
                synchronized(LessonsDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                                context.applicationContext,
                                LessonsDatabase::class.java, DB_NAME)
                                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                                .build()
                    }
                }
            }

            return INSTANCE!!
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {

            override fun migrate(database: SupportSQLiteDatabase) {
                this.logI("Migrating from $startVersion to $endVersion")
                dropIndex(database)
                renameColumn(database)
            }

            private fun dropIndex(database: SupportSQLiteDatabase) {
                database.execSQL("DROP INDEX IF EXISTS index_lessons_name;")
            }

            private fun renameColumn(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE lessons RENAME TO lessons_old;")
                database.execSQL("CREATE TABLE lessons (" +
                        "`uid` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "`date` TEXT, " +
                        "`number` TEXT, " +
                        "`type` TEXT, " +
                        "`cabinet` TEXT, " +
                        "`short_name` TEXT, " +
                        "`name` TEXT, " +
                        "`added_on_date` TEXT, " +
                        "`added_on_time` TEXT, " +
                        "`who` TEXT " +
                        ");")
                database.execSQL("INSERT INTO lessons(" +
                        "`date`, " +
                        "`number`, " +
                        "`type`, " +
                        "`cabinet`, " +
                        "`short_name`, " +
                        "`name`, " +
                        "`added_on_date`, " +
                        "`added_on_time`, " +
                        "`who` " +
                        ") " +
                        "SELECT " +
                        "`date`, " +
                        "`number`, " +
                        "`type`, " +
                        "`cabinet`, " +
                        "`short_info`, " +
                        "`name`, " +
                        "`added_on_date`, " +
                        "`added_on_time`, " +
                        "`who` " +
                        "FROM lessons_old;")
                database.execSQL("DROP TABLE lessons_old;")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE lessons RENAME TO lessons_old;")
                database.execSQL("CREATE TABLE lessons (" +
                        "`uid` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "`date` TEXT, " +
                        "`number` TEXT, " +
                        "`type` TEXT, " +
                        "`cabinet` TEXT, " +
                        "`short_name` TEXT, " +
                        "`name` TEXT, " +
                        "`added_on_date` TEXT, " +
                        "`added_on_time` TEXT, " +
                        "`who` TEXT, " +
                        "`who_short` TEXT " +
                        ");")

                database.execSQL("INSERT INTO lessons (" +
                        "`date`, " +
                        "`number`, " +
                        "`type`, " +
                        "`cabinet`, " +
                        "`short_name`, " +
                        "`name`, " +
                        "`added_on_date`, " +
                        "`added_on_time`, " +
                        "`who`, " +
                        "`who_short`" +
                        ") " +
                        "SELECT " +
                        "`date`, " +
                        "`number`, " +
                        "`type`, " +
                        "`cabinet`, " +
                        "`short_name`, " +
                        "`name`, " +
                        "`added_on_date`, " +
                        "`added_on_time`, " +
                        "`who`, " +
                        "`who` " +
                        "FROM lessons_old;")
                database.execSQL("DROP TABLE lessons_old;")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE lessons RENAME TO lessons_old;")
                database.execSQL("""
                    CREATE TABLE lessons (
                        `uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `date` TEXT NOT NULL,
                        `number` TEXT NOT NULL,
                        `type` TEXT NOT NULL,
                        `cabinet` TEXT NOT NULL,
                        `short_name` TEXT NOT NULL,
                        `name` TEXT NOT NULL,
                        `added_on_date` TEXT NOT NULL,
                        `added_on_time` TEXT NOT NULL,
                        `who` TEXT NOT NULL,
                        `who_short` TEXT NOT NULL
                        );
                        """)

                database.execSQL("INSERT INTO lessons (" +
                        "`date`, " +
                        "`number`, " +
                        "`type`, " +
                        "`cabinet`, " +
                        "`short_name`, " +
                        "`name`, " +
                        "`added_on_date`, " +
                        "`added_on_time`, " +
                        "`who`, " +
                        "`who_short`" +
                        ") " +
                        "SELECT " +
                        "`date`, " +
                        "`number`, " +
                        "`type`, " +
                        "`cabinet`, " +
                        "`short_name`, " +
                        "`name`, " +
                        "`added_on_date`, " +
                        "`added_on_time`, " +
                        "`who`, " +
                        "`who_short` " +
                        "FROM lessons_old;")
                database.execSQL("DROP TABLE lessons_old;")
            }

        }
    }

    abstract fun scheduleDao(): LessonsDao
}