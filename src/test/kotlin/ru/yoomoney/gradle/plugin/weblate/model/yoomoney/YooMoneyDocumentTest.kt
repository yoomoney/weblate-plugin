package ru.yoomoney.gradle.plugin.weblate.model.yoomoney

import org.custommonkey.xmlunit.XMLAssert
import org.testng.annotations.Test
import org.unitils.reflectionassert.ReflectionAssert
import ru.yoomoney.gradle.plugin.weblate.XML_MAPPER

class YooMoneyDocumentTest {

    @Test
    fun shouldDeserialize() {
        val resource = this::class.java.getResource("yoomoney.xml").readText()
        val actual = XML_MAPPER.readValue(resource, YooMoneyDocument::class.java)
        val expected = getDummyYooMoneyDocument()

        ReflectionAssert.assertReflectionEquals(expected, actual)
    }

    @Test
    fun shouldSerialize() {
        val yooMoneyDocument = getDummyYooMoneyDocument()
        val actual = XML_MAPPER.writeValueAsString(yooMoneyDocument)
        val expected = this::class.java.getResource("yoomoney.xml").readText()

        XMLAssert.assertXMLEqual(expected, actual)
    }

    private fun getDummyYooMoneyDocument(): YooMoneyDocument {
        return YooMoneyDocument(
                Project("yoomoney", KeySet(
                        "api_kiosk",
                        listOf(
                                Key(id = "RUB", plural = "False", value = Value("ru", "approved", "руб.")),
                                Key(id = "a3_counter_hint", plural = "False", context = "Подсказка для ввода значений счетчиков",
                                        value = Value("ru", "approved", "Через точку. Например: 500.12"))
                        )
                )))
    }
}