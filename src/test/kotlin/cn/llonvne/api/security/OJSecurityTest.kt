package cn.llonvne.api.security

import cn.llonvne.api.Api
import cn.llonvne.mapper.FakeProblemMapper
import cn.llonvne.mapper.FakeUserMapper
import org.http4k.core.RequestContexts
import org.http4k.events.AutoMarshallingEvents
import org.http4k.format.KotlinxSerialization
import org.http4k.lens.RequestContextKey

class OJSecurityTest {
    private val contexts = RequestContexts()
    val api = Api(
        RequestContextKey.required(contexts),
        FakeUserMapper(),
        FakeProblemMapper(),
        events = AutoMarshallingEvents(KotlinxSerialization)
    )
}