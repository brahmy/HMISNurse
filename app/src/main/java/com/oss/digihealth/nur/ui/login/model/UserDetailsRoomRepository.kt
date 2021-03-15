package com.oss.digihealth.nur.ui.login.model

import android.app.Application
import android.os.AsyncTask
import com.oss.digihealth.doc.ui.login.model.login_response_model.UserDetails
import com.oss.digihealth.nur.db.RoomDatabase
import java.util.concurrent.ExecutionException

class UserDetailsRoomRepository(var application: Application) {
    private var roomDatabase: RoomDatabase? = null
    private var userDetailsDao: UserDetailsDao? = null

    init {
        roomDatabase = RoomDatabase.getInstance(application)
        userDetailsDao = roomDatabase!!.userDetailsDao()
    }


    fun insertData(user: UserDetails) {
        InsertUserDetails(userDetailsDao!!).execute(user)
    }

    class InsertUserDetails(private val userDetailsDao: UserDetailsDao) :
        AsyncTask<UserDetails, Void, Void>() {

        override fun doInBackground(vararg params: UserDetails?): Void? {
            userDetailsDao.deleteAndInsert(params[0])
            return null
        }

    }

    fun getUserDetails(): UserDetails? {
        try {
            return GetUserDetails().execute().get()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return null
    }

    private inner class GetUserDetails :
        AsyncTask<Void, Void, UserDetails>() {

        override fun doInBackground(vararg url: Void): UserDetails? {
            return userDetailsDao?.userDetails
        }
    }
}