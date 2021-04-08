package ru.yoomoney.gradle.plugin.weblate

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator
import com.fasterxml.jackson.module.kotlin.KotlinModule

/**
 * Jackson XML маппер
 */
val XML_MAPPER = XmlMapper(
        JacksonXmlModule().apply {
            setXMLTextElementName("innerText")
        })
        .configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)
        .setSerializationInclusion(Include.NON_NULL)
        .enable(INDENT_OUTPUT)
        .disable(FAIL_ON_UNKNOWN_PROPERTIES)
        .registerModule(KotlinModule())
