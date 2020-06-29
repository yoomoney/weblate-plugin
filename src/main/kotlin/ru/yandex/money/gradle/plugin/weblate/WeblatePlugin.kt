package ru.yandex.money.gradle.plugin.weblate

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.yandex.money.gradle.plugin.weblate.settings.PluginSettings
import ru.yandex.money.gradle.plugin.weblate.task.DownloadTranslationsTask
import java.io.File
import java.net.URL

/**
 * Плагин для взаимодействия с Weblate
 *
 * @author Ilya Doroshenko (ivdoroshenko@yamoney.ru)
 * @since 23.06.2020
 */
class WeblatePlugin : Plugin<Project> {

    companion object {
        private val ROOT_EXTENSION_NAME = "weblate"
    }

    override fun apply(project: Project) {
        val settings = createExtensions(project)
        createAndConfigureDefaultTasks(project, settings)
    }

    /**
     * Создание и конфигурация расширений gradle dsl относящихся к плагину
     */
    private fun createExtensions(target: Project): PluginSettings {
        return target.extensions.create(ROOT_EXTENSION_NAME, PluginSettings::class.java)
    }

    /**
     * Создание и настройка задач по умолчанию
     */
    private fun createAndConfigureDefaultTasks(project: Project, pluginSettings: PluginSettings) {
        registerDownloadTranslationsTask(project, pluginSettings)
    }

    /**
     * Регистрирует задачу, загружающую переводы.
     */
    private fun registerDownloadTranslationsTask(project: Project, pluginSettings: PluginSettings) {
        val connectionSettings = pluginSettings.connectionSettings
        val translationsSettings = pluginSettings.translationsSettings

        project.tasks.register(DownloadTranslationsTask.DEFAULT_TASK_NAME, DownloadTranslationsTask::class.java) {
            it.description = DownloadTranslationsTask.DEFAULT_TASK_DESCRIPTION

            it.weblateBaseUrl = URL(connectionSettings.url)
            it.weblateApiToken = connectionSettings.token
            it.project = translationsSettings.weblateProject
            it.languages = translationsSettings.languages
            it.components = translationsSettings.components
            it.destDir = File(translationsSettings.destDir)
        }
    }
}