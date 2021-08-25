[![Build Status](https://travis-ci.com/yoomoney/weblate-plugin.svg?branch=master)](https://travis-ci.com/yoomoney/weblate-plugin)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

# weblate-plugin

- Официальная документация Weblate: https://docs.weblate.org/en/latest/

## Функционал
* Выгрузка из Weblate переводов для запрошенного проекта.

## Подключение

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'ru.yoomoney.gradle.plugins.weblate-plugin:1.+'
    }
}
apply plugin: 'ru.yoomoney.gradle.plugins.weblate-plugin'
```

## Конфигурация

```groovy
weblate {
    connection {
        url = "https://example.net/" // your Weblate prod.
        token = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    }

    translations {
        weblateProject = "shiro"
        destDir = "ansible/templates/config/i18n"
        components = ['payment_name', 'payment', 'payment_history', 'card']
        languages = ["ru", "en"]

        skipOnWeblateError = true //пропускать загрузку в случае недоступности weblate
    }
}
```

## Задачи

* `downloadWeblateTranslations` - сохраняет файлы переводов в директории соответствующие запрошенным языкам `{destDir}/{lang}/`

