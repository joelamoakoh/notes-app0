
import java.io.File
import kotlin.Throws
import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import models.Note
import persistence.Serializer
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception

class YAMLSerializer(private val file: File) : Serializer {

    override fun read(): Any? {
        TODO("Not yet implemented")
    }
}


    @Throws(Exception::class)
    override fun write(obj: Any?) {
        val xStream = XStream(DomDriver())
        val outputStream = xStream.createObjectOutputStream(FileWriter(file))
        outputStream.writeObject(obj)
        outputStream.close()
    }




