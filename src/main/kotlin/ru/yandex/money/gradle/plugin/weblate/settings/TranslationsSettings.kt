package ru.yandex.money.gradle.plugin.weblate.settings

/**
 * Параметры загружаемых переводов.
 *
 * @author Ilya Doroshenko (ivdoroshenko@yamoney.ru)
 * @since 23.06.2020
 */
class TranslationsSettings {

    /**
     * Проект для которого необходимо загрузить перевод
     * [project](https://docs.weblate.org/en/latest/user/translating.html#translation-projects)
     */
    lateinit var weblateProject: String

    /**
     * Список компонентов weblate, для которых требуются переводы
     */
    var components: Set<String> = emptySet()

    /**
     * Список языков соответствующего проекта [weblateProject].
     * Для каждого языка будет сформирована отдельная директория c его именем в [destDir]
     */
    var languages: Set<String> = emptySet()

    /**
     * Путь до директории, в которую будут сохранены файлы переводов
     */
    lateinit var destDir: String

    /**
     * если [true] игнорировать недоступность weblate и пропускать загрузку переводов.
     * Следует использовать при наличии бэкапа переводов в репозитории компонента.
     */
    var skipOnWeblateError: Boolean = false
}