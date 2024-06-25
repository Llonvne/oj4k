package cn.llonvne.serial

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlin.io.encoding.ExperimentalEncodingApi

@Serializable(with = Base64Serializer::class)
/**
 * Boxed Value will be serialized with base64 encoded
 */
class Base64<T>(val value: T)

private class Base64Serializer<T>(
    private val serializer: KSerializer<T>
) : KSerializer<Base64<T>> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Base64", PrimitiveKind.STRING)

    @OptIn(ExperimentalEncodingApi::class)
    override fun serialize(encoder: Encoder, value: Base64<T>) {
        val encodedValue = kotlin.io.encoding.Base64.encode(
            Json.encodeToString(serializer, value.value).encodeToByteArray()
        )
        encoder.encodeString(encodedValue)
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun deserialize(decoder: Decoder): Base64<T> {
        val decodedString = kotlin.io.encoding.Base64.decode(
            decoder.decodeString().encodeToByteArray()
        ).decodeToString()
        val decodedValue = Json.decodeFromString(serializer, decodedString)
        return Base64(decodedValue)
    }
}