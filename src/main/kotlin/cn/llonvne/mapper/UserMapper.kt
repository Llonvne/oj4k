package cn.llonvne.mapper

import cn.llonvne.domain.User

interface UserMapper {
    fun selectUserByNameOrNull(username: String): User?
}