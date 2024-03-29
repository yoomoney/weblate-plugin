### NEXT_VERSION_TYPE=MAJOR|MINOR|PATCH
### NEXT_VERSION_DESCRIPTION_BEGIN
### NEXT_VERSION_DESCRIPTION_END
## [1.6.0](https://github.com/yoomoney/weblate-plugin/pull/6) (27-10-2021)

* Поднята версия зависимости jackson-module-kotlin: 2.11.0 -> 2.12.1
* Поднята версия зависимости jackson-dataformat-xml: 2.11.0 -> 2.12.1

## [1.5.1](https://github.com/yoomoney/weblate-plugin/pull/5) (27-10-2021)

* Теперь флаг skipOnWeblateError работает и для Throwable исключений.

## [1.5.0](https://github.com/yoomoney/weblate-plugin/pull/4) (26-08-2021)

* Переезд организации yoomoney-gradle-plugins -> yoomoney

## [1.4.2](https://github.com/yoomoney/weblate-plugin/pull/3) (25-06-2021)

* Исправлено падение DownloadTranslationsTask при содержании нескольких тегов <note>

## [1.4.1](https://github.com/yoomoney/weblate-plugin/pull/2) (19-05-2021)

* Добавлена информация о сборке, покрытии, лицензии в README.md.

## [1.4.0](https://github.com/yoomoney/weblate-plugin/pull/1) (08-04-2021)

* Внесены изменения в связи с переходом в GitHub:
* Переименованы пакеты
* Сборка переведена на travis (ранее использовался jenkins)
* Исключены "внутренние" зависимости

## [1.3.0]() (12-02-2021)

* Переименование kotlin-module-plugin в ru.yoomoney.gradle.plugins.kotlin-plugin

## [1.2.0]() (13-07-2020)

* В конфигурацию плагина добавлен флаг пропуска загрузки перевода в случае ошибок weblate
* Добавлен повтор вызова weblate в случае ошибки

## [1.1.0]() (03-07-2020)

* Поднята версия gradle: 6.0.1 -> 6.4.1.

## [1.0.0]() (29-06-2020)

* Создан плагин для загрузки переводов из weblate