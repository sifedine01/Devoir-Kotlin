package devoir2
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

//Q1
data class Produit(val id: Int, val nom: String, var quantite: Int) {
    fun afficherDetails() = println("Produit #$id : $nom - Quantité : $quantite")
}

class Stock {
    val produits = mutableMapOf<Int, Produit>()

    fun ajouterProduit(produit: Produit) {
        produits[produit.id] = produit
        println("${produit.nom} ajouté au stock.")
    }

    suspend fun ajouterQuantite(idProduit: Int, quantite: Int) {
        delay(300L)
        produits[idProduit]?.let {
            it.quantite += quantite
            println("Ajout de $quantite unités à ${it.nom}. Total : ${it.quantite}")
        }
    }

    suspend fun retirerQuantite(idProduit: Int, quantite: Int) {
        delay(300L)
        produits[idProduit]?.let {
            if (it.quantite >= quantite) {
                it.quantite -= quantite
                println("Retrait de $quantite unités de ${it.nom}. Reste : ${it.quantite}")
            } else {
                println("Erreur : stock insuffisant pour ${it.nom}")
            }
        }
    }

    fun afficherStock() = produits.values.forEach { it.afficherDetails() }
}
//Q2
data class CommandeClient(val idCommande: Int, val produits: List<Pair<Int, Int>>) {
    fun afficherCommande() {
        println("Commande Client #$idCommande :")
        produits.forEach { println("Produit ${it.first}, Quantité : ${it.second}") }
    }
}

class GestionnaireCommandes(val stock: Stock) {
    suspend fun traiterCommande(commande: CommandeClient) = coroutineScope {
        commande.produits.map { (id, quantite) ->
            async { stock.retirerQuantite(id, quantite) }
        }.awaitAll()
    }
}
//Q3
class Entrepot(val stock: Stock, val gestionnaire: GestionnaireCommandes) {
    suspend fun ajouterProduitAuStock(produit: Produit) = coroutineScope {
        launch { stock.ajouterProduit(produit) }
    }

    suspend fun retirerProduitDuStock(id: Int, quantite: Int) = coroutineScope {
        launch { stock.retirerQuantite(id, quantite) }
    }

    suspend fun gererInventaire() = coroutineScope {
        launch {
            println("Inventaire en cours...")
            stock.afficherStock()
        }
    }
}
//Q4

class StockFlow {
    private val _events = MutableSharedFlow<String>()
    val events: SharedFlow<String> = _events

    suspend fun notifier(message: String) {
        _events.emit(message)
    }
}

suspend fun main() {
    val stockFlow = StockFlow()
    val job = CoroutineScope(Dispatchers.Default).launch {
        stockFlow.events.collect { println("Événement reçu : $it") }
    }

    stockFlow.notifier("Produit ajouté au stock ✅")
    stockFlow.notifier("Quantité mise à jour 🔄")

    delay(1000L)
    job.cancel()
}
