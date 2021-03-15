package com.oss.digihealth.nur.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.oss.digihealth.doc.ui.login.model.login_response_model.UserDetails
import com.oss.digihealth.nur.ui.login.model.UserDetailsDao

@Database(entities = [UserDetails::class], version = 4, exportSchema = false)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun userDetailsDao(): UserDetailsDao?

    companion object {
        private var INSTANCE: com.oss.digihealth.nur.db.RoomDatabase? = null

        /**
         * from developers android, made my own singleton
         *
         * @param context
         * @return
         */
        fun getInstance(context: Context): com.oss.digihealth.nur.db.RoomDatabase? {
            if (com.oss.digihealth.nur.db.RoomDatabase.Companion.INSTANCE == null) {
                synchronized(com.oss.digihealth.nur.db.RoomDatabase::class.java) {
                    if (com.oss.digihealth.nur.db.RoomDatabase.Companion.INSTANCE == null) {
                        com.oss.digihealth.nur.db.RoomDatabase.Companion.INSTANCE =
                            Room.databaseBuilder(
                                context.applicationContext,
                                com.oss.digihealth.nur.db.RoomDatabase::class.java, "hmis_doctor_db"
                            )
                                .addCallback(com.oss.digihealth.nur.db.RoomDatabase.Companion.sRoomDatabaseCallback)
                                .fallbackToDestructiveMigration()
                                .build()
                    }
                }
            }
            return com.oss.digihealth.nur.db.RoomDatabase.Companion.INSTANCE
        }

        private val sRoomDatabaseCallback: Callback =
            object : Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                }
            }
    }
}