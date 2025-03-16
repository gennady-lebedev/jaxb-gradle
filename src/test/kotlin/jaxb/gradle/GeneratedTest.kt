package jaxb.gradle

import jakarta.xml.bind.JAXBContext
import jaxb.gradle.jaxb.CtPerson
import jaxb.gradle.jaxb.CtTeam
import org.junit.jupiter.api.Test
import java.io.StringReader
import java.math.BigDecimal
import java.sql.Timestamp
import javax.xml.transform.stream.StreamSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class GeneratedTest {

    @Test
    fun unmarshallerTest() {
        val text = GeneratedTest::class.java.getResource("/example.xml")!!.readText()
        assertTrue(text.isNotBlank(), "File 'example.xml' should be available")

        val jaxb = JAXBContext.newInstance(CtTeam::class.java).createUnmarshaller()
        val parsed = jaxb.unmarshal(StreamSource(StringReader(text)), CtTeam::class.java).value

        assertEquals("Alpha Team", parsed.name, "Parsing team name failed")
        assertEquals(
            Timestamp.valueOf("2025-01-02 03:04:05.678"), parsed.formed, "Parsing team name failed"
        )
        assertEquals(3, parsed.members.size, "Parsing team members failed")
    }
}