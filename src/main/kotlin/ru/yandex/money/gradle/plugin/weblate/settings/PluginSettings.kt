package ru.yandex.money.gradle.plugin.weblate.settings

import groovy.lang.Closure
import org.gradle.util.ConfigureUtil

/**
 * Настройки плагина.
 *
 * @author Ilya Doroshenko (ivdoroshenko@yamoney.ru)
 * @since 23.06.2020
 */
open class PluginSettings {
    val connectionSettings = ConnectionSettings()
    val translationsSettings = TranslationsSettings()

    /**
     * Конфигурация подключения к Weblate
     */
    fun connection(closure: Closure<*>): ConnectionSettings = ConfigureUtil.configure(closure, connectionSettings)
    fun connection(configure: ConnectionSettings.() -> Unit) = connectionSettings.configure()

    /**
     * Конфигурация требуемых переводов
     */
    fun translations(closure: Closure<*>): TranslationsSettings = ConfigureUtil.configure(closure, translationsSettings)
    fun translations(configure: TranslationsSettings.() -> Unit) = translationsSettings.configure()

    override fun toString(): String {
        return "TankerPluginSettings(connectionSettings=$connectionSettings, translationsSettings=$translationsSettings)"
    }
}