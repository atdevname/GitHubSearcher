package com.atdev.githubproject.profile.auth.data

import com.atdev.githubproject.profile.auth.login.Result
import com.atdev.githubproject.profile.auth.model.LoggedInUser
import java.io.IOException
import java.util.*

class LoginDataSource {
    fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(UUID.randomUUID().toString(), username)
            Result.Success(fakeUser)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    //TODO:: Firebase Auth

    fun logout() {
        //TODO:: logout with github also
    }
}