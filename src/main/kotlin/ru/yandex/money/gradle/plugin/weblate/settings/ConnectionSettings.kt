package ru.yandex.money.gradle.plugin.weblate.settings

/**
 * Настройки подключения к weblate.
 *
 * @author Ilya Doroshenko (ivdoroshenko@yamoney.ru)
 * @since 23.06.2020
 */
class ConnectionSettings {

    /**
     * Базовый url weblate
     */
    lateinit var url: String

    /**
     * [API токен](https://docs.weblate.org/en/latest/api.html#any--) авторизации в weblate
     */
    lateinit var token: String
}