package devoir1

// Classe de base Personne
open class Personne(val nom: String, val prenom: String, val email: String) {
    fun afficherInfos() {
        println("Nom: $nom, Prénom: $prenom, Email: $email")
    }
}

// Classe Livre
class Livre(val titre: String, val auteur: String, val isbn: String, var nombreExemplaires: Int) {
    fun afficherDetails() {
        println("Titre: $titre, Auteur: $auteur, ISBN: $isbn, Exemplaires: $nombreExemplaires")
    }

    fun disponiblePourEmprunt(): Boolean = nombreExemplaires > 0

    fun mettreAJourStock(nouveauStock: Int) {
        nombreExemplaires = nouveauStock
    }
}

// Classe Emprunt
class Emprunt(val utilisateur: Utilisateur, val livre: Livre, val dateEmprunt: String) {
    var dateRetour: String? = null

    fun afficherDetails() {
        println("Emprunt - Utilisateur: ${utilisateur.nom}, Livre: ${livre.titre}, Date Emprunt: $dateEmprunt, Date Retour: ${dateRetour ?: "Non retourné"}")
    }

    fun retournerLivre(date: String) {
        dateRetour = date
        livre.mettreAJourStock(livre.nombreExemplaires + 1)
    }
}

// Classe Utilisateur héritée de Personne
class Utilisateur(nom: String, prenom: String, email: String, val idUtilisateur: Int) :
    Personne(nom, prenom, email) {

    val emprunts = mutableListOf<Emprunt>()

    fun emprunterLivre(livre: Livre, dateEmprunt: String) {
        if (livre.disponiblePourEmprunt()) {
            livre.mettreAJourStock(livre.nombreExemplaires - 1)
            val emprunt = Emprunt(this, livre, dateEmprunt)
            emprunts.add(emprunt)
            println("Livre emprunté avec succès.")
        } else {
            println("Le livre n'est pas disponible.")
        }
    }

    fun afficherEmprunts() {
        println("Emprunts de ${nom} ${prenom}:")
        emprunts.forEach { it.afficherDetails() }
    }
}

// Classe abstraite GestionBibliotheque
abstract class GestionBibliotheque {
    val utilisateurs = mutableListOf<Utilisateur>()
    val livres = mutableListOf<Livre>()

    abstract fun ajouterUtilisateur(utilisateur: Utilisateur)
    abstract fun ajouterLivre(livre: Livre)
    abstract fun afficherTousLesLivres()
}

// Classe Bibliotheque héritée de GestionBibliotheque
class Bibliotheque : GestionBibliotheque() {
    override fun ajouterUtilisateur(utilisateur: Utilisateur) {
        utilisateurs.add(utilisateur)
    }

    override fun ajouterLivre(livre: Livre) {
        livres.add(livre)
    }

    override fun afficherTousLesLivres() {
        println("Liste des livres dans la bibliothèque :")
        livres.forEach { it.afficherDetails() }
    }

    fun rechercherLivreParTitre(titre: String): Livre? {
        return livres.find { it.titre.equals(titre, ignoreCase = true) }
    }
}

// ----------- Simulation -----------

fun main() {
    val biblio = Bibliotheque()

    // Question 1 : Création
    val livre1 = Livre("Kotlin pour les débutants", "salem", "123456789", 3)
    val livre2 = Livre("Programmation Orientée Objet", "mestapha", "987654321", 2)

    val utilisateur1 = Utilisateur("Ahmed", "Benali", "ahmed@example.com", 1)
    val utilisateur2 = Utilisateur("Sara", "Toumi", "sara@example.com", 2)

    // Question 2 : Ajout
    biblio.ajouterLivre(livre1)
    biblio.ajouterLivre(livre2)
    biblio.ajouterUtilisateur(utilisateur1)
    biblio.ajouterUtilisateur(utilisateur2)

    // Question 3 : Emprunt
    utilisateur1.emprunterLivre(livre1, "2025-10-04")
    utilisateur2.emprunterLivre(livre2, "2025-10-04")

    // Question 4 : Affichage
    biblio.afficherTousLesLivres()
    utilisateur1.afficherInfos()
    utilisateur1.afficherEmprunts()
    utilisateur2.afficherInfos()
    utilisateur2.afficherEmprunts()

    // Question 5 : Retour
    val empruntARetourner = utilisateur1.emprunts.first()
    empruntARetourner.retournerLivre("2025-10-10")
    empruntARetourner.afficherDetails()
}
