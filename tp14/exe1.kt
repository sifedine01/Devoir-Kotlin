package tp14

import kotlinx.coroutines.*

suspend fun verifierDisponibilite() {
    delay(2000)
}

suspend fun preparerCommande() {
    delay(5000)
}

suspend fun livrerRepas() = withContext(Dispatchers.IO) {
    delay(3000)
}

fun main() = runBlocking {
    val job1 = launch { verifierDisponibilite() }
    job1.join()

    val job2 = launch { preparerCommande() }
    job2.join()

    val job3 = launch { livrerRepas() }
    job3.join()
}
