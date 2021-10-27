package ru.yoomoney.gradle.plugin.weblate.task

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.ContentType
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import ru.yoomoney.gradle.plugin.weblate.XML_MAPPER
import ru.yoomoney.gradle.plugin.weblate.model.yoomoney.Key
import ru.yoomoney.gradle.plugin.weblate.model.yoomoney.KeySet
import ru.yoomoney.gradle.plugin.weblate.model.yoomoney.Project
import ru.yoomoney.gradle.plugin.weblate.model.yoomoney.YooMoneyDocument
import ru.yoomoney.gradle.plugin.weblate.model.yoomoney.Value
import ru.yoomoney.gradle.plugin.weblate.model.xliff.Translation
import ru.yoomoney.gradle.plugin.weblate.model.xliff.Xliff
import ru.yoomoney.gradle.plugin.weblate.retryOnException
import java.io.File
import java.net.URL

/**
 * Задача на выгрузку файлов переводов из Weblate
 *
 * @author Ilya Doroshenko
 * @since 23.06.2020
 */
open class DownloadTranslationsTask : DefaultTask() {
    private val log = Logging.getLogger(DownloadTranslationsTask::class.java)

    companion object {
        val DEFAULT_TASK_NAME = "downloadWeblateTranslations"
        val DEFAULT_TASK_DESCRIPTION = "Download translations from weblate"
    }

    @OutputDirectory
    lateinit var destDir: File

    @Input
    lateinit var weblateBaseUrl: URL

    @Input
    lateinit var weblateApiToken: String

    @Input
    lateinit var project: String

    @Input
    lateinit var languages: Set<String>

    @Input
    lateinit var components: Set<String>

    @Input
    var skipOnWeblateError: Boolean = false

    @TaskAction
    fun loadTranslationsAsXliff() {
        log.lifecycle("Downloading weblate translations...")

        languages.forEach { lang ->
            val destLanguageDir = destDir.resolve(lang)
            destLanguageDir.mkdirs()

            components.forEach {
                val xliffTranslation = try {
                    downloadTranslations(lang, project, it)
                } catch (ex: Throwable) {
                    if (skipOnWeblateError) {
                        log.error("Error during download translation. Ignore due to skipOnWeblateError is true", ex)
                        return
                    }
                    throw ex
                }

                // для обратной совместимости в с логикой i18n-utils сохраняем в формате ЮМани
                val yooMoneyDocument = mapXliffToYooMoneyFormat(xliffTranslation, it)
                saveAsYooMoneyXml(yooMoneyDocument, destLanguageDir)
            }
        }

        log.lifecycle("Downloading finished")
    }

    private fun downloadTranslations(lang: String, project: String, component: String): Xliff {
        log.lifecycle("download {}:{} from {}", project, lang, weblateBaseUrl)
        return loadTranslationsAsXliff(project, component, lang)
    }

    private fun saveAsYooMoneyXml(yooMoneyDocument: YooMoneyDocument, destLanguageDir: File) {
        val targetFile = destLanguageDir.resolve(yooMoneyDocument.project.id + "." +
                yooMoneyDocument.project.keySet.id + ".xml")

        targetFile.writeText(XML_MAPPER.writeValueAsString(yooMoneyDocument))
    }

    private fun mapXliffToYooMoneyFormat(xliff: Xliff, component: String): YooMoneyDocument {
        val translations = xliff.file.body.translations

        return YooMoneyDocument(Project(
            id = project,
            keySet = KeySet(
                id = component,
                keys = translations.map {
                    Key(id = it.id,
                        context = if (it.notes.isEmpty()) "" else it.notes.get(0).value,
                        // всегда false, на данный момент plural режим не поддерживаются в i18m-utils
                        plural = "False",
                        value = Value(
                            language = xliff.file.targetLanguage,
                            status = "approved",
                            value = getTranslationValue(it)
                        )
                    )
                }
            )))
    }

    /**
     * Получение значения перевода.
     * Если значение для запрошенного языка отсутствует в target
     * в качестве fallback значения берется source язык
     */
    private fun getTranslationValue(translation: Translation): String {
        return if (translation.target.value.isNotBlank()) {
            translation.target.value
        } else {
            translation.source.value
        }
    }

    private fun loadTranslationsAsXliff(
        project: String,
        component: String,
        language: String
    ): Xliff {
        HttpClient {
            install(JsonFeature) {
                serializer = JacksonSerializer(jackson = XML_MAPPER)
                acceptContentTypes = acceptContentTypes + ContentType("application", "x-xliff+xml")
            }
        }.use {
            return runBlocking {
                retryOnException(3) {
                    it.get<Xliff> {
                        url("$weblateBaseUrl/api/translations/$project/$component/$language/file")
                        parameter("format", "xliff")
                        headers {
                            append("Authorization", weblateApiToken)
                        }
                    }
                }
            }
        }
    }
}
