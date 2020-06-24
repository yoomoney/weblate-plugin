package ru.yandex.money.gradle.plugin.weblate.model.xliff

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

/**
 * Модель спецификации [xliff 1.1](http://www.oasis-open.org/committees/xliff/documents/xliff-specification.htm)
 *
 * Пример:
 * ```
 * <?xml version='1.0' encoding='UTF-8'?>
 * <xliff xmlns="urn:oasis:names:tc:xliff:document:1.1" version="1.1">
 *   <file source-language="ru" target-language="en">
 *     <body>
 *       <trans-unit id=".button">
 *         <source>Мои карты</source>
 *         <target></target>
 *         <note from="developer"></note>
 *       </trans-unit>
 *     </body>
 *   </file>
 *   </xliff>
 * ```
 *
 * @author Ilya Doroshenko (ivdoroshenko@yamoney.ru)
 * @since 23.06.2020
 */

@JacksonXmlRootElement(namespace = "urn:oasis:names:tc:xliff:document:1.1", localName = "xliff")
data class Xliff(
    @JacksonXmlProperty(localName = "version", isAttribute = true)
    val version: String,

    @JacksonXmlProperty(localName = "file")
    val file: File
)

data class File(
    @JacksonXmlProperty(localName = "source-language", isAttribute = true)
    val sourceLanguage: String,

    @JacksonXmlProperty(localName = "target-language", isAttribute = true)
    val targetLanguage: String,

    @JacksonXmlProperty(localName = "body")
    val body: Body
)

class Body {
    @JacksonXmlProperty(localName = "trans-unit")
    @JacksonXmlElementWrapper(useWrapping = false)
    var translations: MutableList<Translation> = mutableListOf()
        set(value) {
            field.addAll(value)
        }

    override fun toString(): String {
        return "Body(translations=$translations)"
    }
}

data class Translation(
    @JacksonXmlProperty(localName = "id", isAttribute = true)
    val id: String,

    @JacksonXmlProperty(localName = "source")
    val source: String,

    @JacksonXmlProperty(localName = "target")
    val target: String = "",

    @JacksonXmlProperty(localName = "note")
    val note: String = ""
)