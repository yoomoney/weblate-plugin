package ru.yandex.money.gradle.plugin.weblate

import kotlinx.coroutines.delay
import org.gradle.api.logging.Logging
import java.time.Duration

private val log = Logging.getLogger("RetryUtils")

/**
 * При [Exception] повторяет переданный [block] заданное количество раз
 *
 * @param times количество повторов
 * @param initialDelay задержка перед первым повтором
 * @param maxDelay максимальная задержка
 * @param factor мультипликатор увеличения задержки перед следующим повтором
 */
internal suspend fun <T> retryOnException(
    times: Int = Int.MAX_VALUE,
    initialDelay: Duration = Duration.ofMillis(100),
    maxDelay: Duration = Duration.ofSeconds(1),
    factor: Double = 2.0,
    block: suspend () -> T
): T {
    var currentDelay = initialDelay
    repeat(times - 1) {
        try {
            return block()
        } catch (ex: Exception) {
            log.warn("Exception during call", ex)
        }
        delay(currentDelay.toMillis())
        currentDelay = Duration.ofMillis(
            (currentDelay.toMillis() * factor).toLong().coerceAtMost(maxDelay.toMillis()))
    }
    return block() // last attempt
}