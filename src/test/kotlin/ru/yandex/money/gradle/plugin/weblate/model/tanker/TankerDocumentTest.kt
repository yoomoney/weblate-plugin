package ru.yandex.money.gradle.plugin.weblate.model.tanker

import org.custommonkey.xmlunit.XMLAssert
import org.testng.annotations.Test
import org.unitils.reflectionassert.ReflectionAssert
import ru.yandex.money.gradle.plugin.weblate.XML_MAPPER

class TankerDocumentTest {

    @Test
    fun shouldDeserialize() {
        val resource = this::class.java.getResource("tanker.xml").readText()
        val actual = XML_MAPPER.readValue(resource, TankerDocument::class.java)
        val expected = getDummyTankerDocument()

        ReflectionAssert.assertReflectionEquals(expected, actual)
    }

    @Test
    fun shouldSerialize() {
        val tankerDocument = getDummyTankerDocument()
        val actual = XML_MAPPER.writeValueAsString(tankerDocument)
        val expected = this::class.java.getResource("tanker.xml").readText()

        XMLAssert.assertXMLEqual(expected, actual)
    }

    private fun getDummyTankerDocument(): TankerDocument {
        return TankerDocument(
                Project("yamoney", KeySet(
                        "api_kiosk",
                        listOf(
                                Key(id = "RUB", plural = "False", value = Value("ru", "approved", "руб.")),
                                Key(id = "a3_counter_hint", plural = "False", context = "Подсказка для ввода значений счетчиков",
                                        value = Value("ru", "approved", "Через точку. Например: 500.12"))
                        )
                )))
    }
}