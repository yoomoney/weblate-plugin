package ru.yandex.money.gradle.plugin.weblate

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import org.amshove.kluent.shouldEqual
import org.apache.commons.io.FilenameUtils
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.rules.TemporaryFolder
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import ru.yandex.money.net.InetUtils
import ru.yandex.money.tools.testing.mock.WireMockServiceProvider
import java.io.File

class DownloadWebleateTranslationsTest {

    private val testProjectDir: TemporaryFolder = TemporaryFolder()

    private lateinit var settingsFile: File
    private lateinit var buildFile: File

    @BeforeMethod
    fun beforeEach() {
        testProjectDir.create()

        settingsFile = testProjectDir.newFile("settings.gradle")
        buildFile = testProjectDir.newFile("build.gradle")
    }

    @AfterMethod
    fun afterEach() {
        testProjectDir.delete()
    }

    @Test
    fun `Should download translations`() {
        val translationDestDir = testProjectDir.root.resolve("target/translate")
        val fileSystemAwareDestDir = FilenameUtils
            .separatorsToSystem(translationDestDir.canonicalPath).replace("\\", "\\\\")

        val wireMockServer = WireMockServiceProvider.wireMockServer(WireMockServiceProvider
            .buildConfig()
            .port(InetUtils.getFreeRandomLocalPort()))

        wireMockServer.stubFor(
            get(urlPathMatching("/api/translations/test-backend-api/kiosk_api/ru/file"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/x-xliff+xml; charset=utf-8")
                    .withBody(getResourceContent("weblate-kiosk-response-ru.xml")))
        )

        wireMockServer.stubFor(
            get(urlPathMatching("/api/translations/test-backend-api/kiosk_api/en/file"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/x-xliff+xml; charset=utf-8")
                    .withBody(getResourceContent("weblate-kiosk-response-en.xml")))
        )

        buildFile.writeText("""
            plugins {
                id 'yamoney-weblate-plugin'
            }

            weblate {
                connection {
                    url = "${wireMockServer.baseUrl()}"
                    token = "Token iDVBWUtvcir04EYIBkLJmzvUQ8Bo1ZAAJaVhS8Xx"
                }
            
                translations {
                    weblateProject = "test-backend-api"
                    destDir = "$fileSystemAwareDestDir"
                    components = ["kiosk_api"]
                    languages = ["ru", "en"]
                }
            }
        """.trimIndent())

        val result = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(testProjectDir.root)
            .withArguments("downloadWeblateTranslations")
            .build()

        // проверяем статус gradle task
        result.task(":downloadWeblateTranslations")?.outcome shouldEqual TaskOutcome.SUCCESS

        // проверяем результирующие файлы
        val actualRu = testProjectDir.root.resolve("target/translate/ru/yamoney.kiosk_api.xml").readText()
        val expectedRu = getResourceContent("expected.ru.yamoney.kiosk_api.xml")

        actualRu shouldEqual expectedRu

        val actualEn = testProjectDir.root.resolve("target/translate/en/yamoney.kiosk_api.xml").readText()
        val expectedEn = getResourceContent("expected.en.yamoney.kiosk_api.xml")

        actualEn shouldEqual expectedEn
    }

    private fun getResourceContent(filename: String) = this::class.java.getResource(filename).readText()
}