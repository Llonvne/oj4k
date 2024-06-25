package cn.llonvne.mapper

import cn.llonvne.domain.User

class FakeUserMapper(
    private val users: MutableList<User> = mutableListOf()
) : UserMapper {
    override fun selectUserByNameOrNull(username: String): User? {
        return users.firstOrNull { it.name == username }
    }
}