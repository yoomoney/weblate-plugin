package ru.yoomoney.gradle.plugin.weblate.helper

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.FileSource
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.extension.Parameters
import com.github.tomakehurst.wiremock.extension.ResponseTransformer
import com.github.tomakehurst.wiremock.http.Request
import com.github.tomakehurst.wiremock.http.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.net.ServerSocket
import java.net.URL
import java.util.function.BiFunction

class WireMockHelper {

    private val log: Logger = LoggerFactory.getLogger(WireMockHelper::class.java)

    /**
     * Создает конфигурацию со значениями по умолчанию
     *
     * @return [WireMockConfiguration]
     */
    fun buildConfig(): WireMockConfiguration {
        return WireMockConfiguration.options()
                .extensions(ToStringResponseTransformer(), ImperativeResponseTransformer())
                .containerThreads(40)
                .maxRequestJournalEntries(1000)
    }

    /**
     * Создает инстанс сервиса [WireMockServer] и прогревает его
     *
     * @param options опции полученные из [.buildConfig]
     * @return [WireMockServer]
     */
    fun wireMockServer(options: WireMockConfiguration): WireMockServer {
        val wireMockServer = WireMockServer(options)
        wireMockServer.start()
        WireMock.configureFor("localhost", options.portNumber())
        log.info("warmUp started")
        try {
            val stubMapping = wireMockServer.stubFor(WireMock.get("/").willReturn(WireMock.aResponse().withStatus(200)))
            val url = URL("http://localhost:" + options.portNumber() + "/")
            val httpClient = url.openConnection() as HttpURLConnection
            httpClient.requestMethod = "GET"
            httpClient.responseCode
            httpClient.disconnect()
            wireMockServer.removeStub(stubMapping)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        log.info("warmUp end")
        return wireMockServer
    }

    fun freeRandomLocalPort(): Int {
        try {
            ServerSocket(0).use { socket -> return socket.localPort }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    /**
     * Позволяет описать ответ стаба в императивном стиле.
     */
    private class ImperativeResponseTransformer : ResponseTransformer() {

        /**
         * Имя трансформера
         */
        val NAME = "ImperativeResponseTransformer"

        /**
         * Имя параметра в котором лежит процессов ответа
         */
        val PROCESSOR_PARAM = "processor"

        override fun transform(request: Request, response: Response, files: FileSource?, parameters: Parameters): Response? {
            val processor = parameters[PROCESSOR_PARAM] as BiFunction<Request, Response, Response>?
            return processor!!.apply(request, response)
        }

        override fun applyGlobally(): Boolean {
            return false
        }

        override fun getName(): String? {
            return NAME
        }
    }

    /**
     * Позволяет описать ответ стаба в императивном стиле, формат ответа всегда строка.
     * <p>
     * Вместо данного класа лучше использовать {@link ImperativeResponseTransformer}
     */
    private class ToStringResponseTransformer : ResponseTransformer() {

        /**
         * Имя трансформера
         */
        val NAME = "ToStringResponseTransformer"

        /**
         * Имя параметра в котором лежит процессов ответа
         */
        val PROCESSOR_PARAM = "processor"

        override fun transform(request: Request, response: Response, files: FileSource?, parameters: Parameters): Response? {
            val processor = parameters["processor"] as BiFunction<Request, Response, Any>?
            val result = processor!!.apply(request, response)
            return Response.Builder.like(response).body(result.toString()).build()
        }

        override fun applyGlobally(): Boolean {
            return false
        }

        override fun getName(): String? {
            return NAME
        }
    }
}