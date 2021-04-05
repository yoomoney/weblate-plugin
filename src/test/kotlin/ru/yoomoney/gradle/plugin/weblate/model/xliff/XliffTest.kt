package ru.yoomoney.gradle.plugin.weblate.model.xliff

import org.testng.annotations.Test
import org.unitils.reflectionassert.ReflectionAssert
import ru.yoomoney.gradle.plugin.weblate.XML_MAPPER

class XliffTest {

    @Test
    fun shouldDeserialize() {
        val resource = this::class.java.getResource("xliff.xml").readText()
        val actual = XML_MAPPER.readValue(resource, Xliff::class.java)
        val expected = getDummyXliffDocument()

        ReflectionAssert.assertReflectionEquals(expected, actual)
    }

    private fun getDummyXliffDocument(): Xliff {
        return Xliff(version = "1.1",
            file = File(
                sourceLanguage = "ru",
                targetLanguage = "ru",
                body = Body().apply {
                    translations = mutableListOf(
                        Translation(
                            id = ".button",
                            source = Source("Мои карты"),
                            target = Target(value = "Мои карты"),
                            note = "Комментарий"
                        ),
                        Translation(id = ".description",
                            source = Source("Можно платить"),
                            target = Target(value = "Можно платить")
                        )
                    )
                }
            ))
    }
}