# yamoney-weblate-plugin

- Официальная документация Weblate: https://docs.weblate.org/en/latest/

## Функционал
* Выгрузка из Weblate переводов для запрошенного проекта.

Weblate prod: https://weblate.yamoney.ru/

## Подключение

```groovy
buildscript {
    repositories {
        maven { url 'http://nexus.yamoney.ru/repository/thirdparty/' }
        maven { url 'http://nexus.yamoney.ru/repository/central/' }
        maven { url 'http://nexus.yamoney.ru/repository/releases/' }
        maven { url 'http://nexus.yamoney.ru/repository/jcenter.bintray.com/' }
    }
    dependencies {
        classpath 'ru.yandex.money.gradle.plugins:yamoney-weblate-plugin:1.+'
    }
}
apply plugin: 'yamoney-weblate-plugin'
```

## Конфигурация

```groovy
weblate {
    connection {
        url = "https://weblate.yamoney.ru/"
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

