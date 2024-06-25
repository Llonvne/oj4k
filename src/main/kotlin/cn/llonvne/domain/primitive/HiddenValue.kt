package cn.llonvne.domain.primitive

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * This `KSerializer` will serialize `String` type to "<HIDDEN>",
 * and use the normal `String` deserializer during deserialization.
 */
object HiddenStringSerializer : KSerializer<String> {
    private val stringSerializer = String.serializer()

    override fun deserialize(decoder: Decoder): String {
        return stringSerializer.deserialize(decoder)
    }

    override val descriptor: SerialDescriptor = stringSerializer.descriptor

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString("<HIDDEN>")
    }
}

