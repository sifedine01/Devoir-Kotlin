package devoir2
import kotlinx.coroutines.*
//Q1
data class Commande(val id: Int, val plats: List<String>, val total: Double) {
    fun afficherDetails() {
        println("Commande #$id : $plats - Total : $total €")
    }
}

class Serveur(val nom: String) {
    fun prendreCommande(plats: List<String>, prix: List<Double>): Commande {
        val total = prix.sum()
        val commande = Commande((0..999).random(), plats, total)
        println("$nom a pris la commande : $plats (Total : $total €)")
        return commande
    }
}
//Q2
class Cuisinier(val nom: String) {
    suspend fun preparerPlat(plat: String): String {
        delay(1000L)
        println("$nom prépare le plat : $plat")
        return "$plat prêt"
    }

    suspend fun preparerCommande(commande: Commande): List<String> = coroutineScope {
        commande.plats.map { plat ->
            async { preparerPlat(plat) }
        }.awaitAll()
    }
}
//Q3
class Caisse {
    suspend fun traiterPaiement(commande: Commande): Boolean {
        delay(500L)
        return if ((0..1).random() == 1) {
            println("Paiement de ${commande.total}€ réussi ")
            true
        } else {
            println("Paiement échoué ")
            false
        }
    }

    fun annulerPaiement(commande: Commande) {
        println("Paiement pour la commande #${commande.id} annulé.")
    }
}
//Q4
class Restaurant(
    val serveurs: List<Serveur>,
    val cuisine: List<Cuisinier>,
    val caisse: Caisse
) {
    private val commandes = mutableListOf<Commande>()

    suspend fun prendreCommandeEtTraiter(serveur: Serveur, plats: List<String>, prix: List<Double>) {
        val commande = serveur.prendreCommande(plats, prix)
        commandes.add(commande)

        val platsPrepares = coroutineScope {
            cuisine.map { cuisinier ->
                async { cuisinier.preparerCommande(commande) }
            }.awaitAll()
        }

        println("Commande #${commande.id} prête : $platsPrepares")
        val paiementReussi = caisse.traiterPaiement(commande)

        if (!paiementReussi) caisse.annulerPaiement(commande)
    }

    fun afficherCommandesEnCours() {
        println("Commandes en cours : ${commandes.map { it.id }}")
    }
}