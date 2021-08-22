package support.schemas

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object DateSerializer : KSerializer<Date> {
    private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Date) = encoder.encodeString(formatter.format(value))
    override fun deserialize(decoder: Decoder): Date = try {
        formatter.parse(decoder.decodeString())
    } catch (err: ParseException) {
        throw SerializationException(err.message, err)
    }
}
