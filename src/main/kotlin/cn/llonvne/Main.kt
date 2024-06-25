package cn.llonvne

import org.http4k.cloudnative.env.Environment

fun main() {
    OnlineJudgeSystemServer(Environment.ENV).start()
}