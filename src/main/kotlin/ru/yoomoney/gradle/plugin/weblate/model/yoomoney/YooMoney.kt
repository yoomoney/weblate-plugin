package ru.yoomoney.gradle.plugin.weblate.model.yoomoney

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

/**
 * Модель перевода проекта в формате ЮМани
 *
 * Пример:
 * ```
 * <?xml version="1.0" encoding="utf-8"?>
 *  <tanker>
 *   <project id="yoomoney">
 *    <keyset id="payment_name">
 *     <key id="ba_return_payment" is_plural="False">
 *      <context>Возврат: &lt;имя возвращаемого платежа&gt;</context>
 *      <value language="ru" status="approved">Возврат денег с профессионального счета</value>
 *     </key>
 *    </keyset>
 *   </project>
 *  </tanker>
 * ```
 *
 * @author Ilya Doroshenko
 * @since 23.06.2020
 */

@JacksonXmlRootElement(localName = "tanker")
data class YooMoneyDocument(
    @JacksonXmlProperty(localName = "project")
    val project: Project
)

data class Project(
    @JacksonXmlProperty(localName = "id", isAttribute = true)
    val id: String,

    @JacksonXmlProperty(localName = "keyset")
    val keySet: KeySet
)

data class KeySet(
    @JacksonXmlProperty(localName = "id", isAttribute = true)
    val id: String,

    @JacksonXmlProperty(localName = "key")
    @JacksonXmlElementWrapper(useWrapping = false)
    val keys: List<Key>
)

data class Key(
    @JacksonXmlProperty(localName = "id", isAttribute = true)
    val id: String,

    @JacksonXmlProperty(localName = "is_plural", isAttribute = true)
    val plural: String,

    @JacksonXmlProperty(localName = "context")
    val context: String? = null,

    @JacksonXmlProperty(localName = "value")
    val value: Value
)

data class Value(
    @JacksonXmlProperty(localName = "language", isAttribute = true)
    val language: String,

    @JacksonXmlProperty(localName = "status", isAttribute = true)
    val status: String,

    @JacksonXmlProperty(localName = "innerText")
    @JacksonXmlText
    val value: String? = null
)
