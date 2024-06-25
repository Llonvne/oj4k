package cn.llonvne.service

import cn.llonvne.api.user.LoginProcessor
import cn.llonvne.api.user.LoginRequest
import cn.llonvne.api.user.LoginResult
import cn.llonvne.mapper.UserMapper

class LoginProcessorServiceProvider(
    private val userMapper: UserMapper
) : ServiceProvider<LoginProcessor> {
    override fun provide(): LoginProcessor {
        return LoginProcessor {
            when (it) {
                is LoginRequest.LoginViaToken -> TODO()
                is LoginRequest.LoginViaUsernameAndPassword -> viaUsernameAndPassword(it)
            }
        }
    }

    private fun viaUsernameAndPassword(loginRequest: LoginRequest.LoginViaUsernameAndPassword): LoginResult {
        val user = userMapper.selectUserByNameOrNull(loginRequest.username)
            ?: return LoginResult.LoginUserNotFound(loginRequest.username)

        if (user.password != loginRequest.password) {
            return LoginResult.WrongPassword(loginRequest.username)
        }

        return LoginResult.OK(loginRequest.username, user)
    }
}