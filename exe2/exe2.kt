package devoir1

// Partie 1 : Classes de base

abstract class Vehicule(
    val immatriculation: String,
    val marque: String,
    val modele: String,
    var kilometrage: Int,
    var disponible: Boolean = true
) {
    open fun afficherDetails() {
        println("Immatriculation: $immatriculation, Marque: $marque, Modèle: $modele, Km: $kilometrage, Disponible: $disponible")
    }

    fun estDisponible(): Boolean = disponible
    fun marquerIndisponible() { disponible = false }
    fun marquerDisponible() { disponible = true }
    fun mettreAJourKilometrage(km: Int) { kilometrage = km }
}

class Voiture(
    immatriculation: String,
    marque: String,
    modele: String,
    kilometrage: Int,
    val nombrePortes: Int,
    val typeCarburant: String
) : Vehicule(immatriculation, marque, modele, kilometrage) {
    override fun afficherDetails() {
        super.afficherDetails()
        println("Type: Voiture, Portes: $nombrePortes, Carburant: $typeCarburant")
    }
}

class Moto(
    immatriculation: String,
    marque: String,
    modele: String,
    kilometrage: Int,
    val cylindree: Int
) : Vehicule(immatriculation, marque, modele, kilometrage) {
    override fun afficherDetails() {
        super.afficherDetails()
        println("Type: Moto, Cylindrée: ${cylindree}cm³")
    }
}

// Partie 2 : Gestion des réservations

class Conducteur(val nom: String, val prenom: String, val numeroPermis: String) {
    fun afficherDetails() {
        println("Conducteur: $nom $prenom, Permis: $numeroPermis")
    }
}

class Reservation(
    val vehicule: Vehicule,
    val conducteur: Conducteur,
    val dateDebut: String,
    val dateFin: String,
    val kilometrageDebut: Int
) {
    var kilometrageFin: Int? = null

    fun cloturerReservation(kilometrageRetour: Int) {
        kilometrageFin = kilometrageRetour
        vehicule.mettreAJourKilometrage(kilometrageRetour)
        vehicule.marquerDisponible()
    }

    fun afficherDetails() {
        println("Réservation - Véhicule: ${vehicule.immatriculation}, Conducteur: ${conducteur.nom}, Du: $dateDebut au $dateFin, Km début: $kilometrageDebut, Km fin: ${kilometrageFin ?: "Non clôturée"}")
    }
}

// Partie 4 : Exceptions

class VehiculeIndisponibleException(message: String) : Exception(message)
class VehiculeNonTrouveException(message: String) : Exception(message)

// Partie 3 : Gestion du parc

class ParcAutomobile {
    val vehicules = mutableListOf<Vehicule>()
    val reservations = mutableListOf<Reservation>()

    fun ajouterVehicule(vehicule: Vehicule) {
        vehicules.add(vehicule)
    }

    fun supprimerVehicule(immatriculation: String) {
        vehicules.removeIf { it.immatriculation == immatriculation }
    }

    fun reserverVehicule(
        immatriculation: String,
        conducteur: Conducteur,
        dateDebut: String,
        dateFin: String
    ) {
        val vehicule = vehicules.find { it.immatriculation == immatriculation }
            ?: throw VehiculeNonTrouveException("Véhicule $immatriculation introuvable.")

        if (!vehicule.estDisponible()) {
            throw VehiculeIndisponibleException("Véhicule $immatriculation non disponible.")
        }

        vehicule.marquerIndisponible()
        val reservation = Reservation(vehicule, conducteur, dateDebut, dateFin, vehicule.kilometrage)
        reservations.add(reservation)
        println("Réservation effectuée avec succès.")
    }

    fun afficherVehiculesDisponibles() {
        println("Véhicules disponibles :")
        vehicules.filter { it.estDisponible() }.forEach { it.afficherDetails() }
    }

    fun afficherReservations() {
        println("Réservations en cours :")
        reservations.forEach { it.afficherDetails() }
    }
}

// ----------- Simulation -----------

fun main() {
    val parc = ParcAutomobile()

    // Question 1 : Création de véhicules
    val voiture1 = Voiture("123ABC", "Peugeot", "208", 15000, 5, "Essence")
    val voiture2 = Voiture("456DEF", "Tesla", "Model 3", 8000, 4, "Électrique")
    val moto1 = Moto("789GHI", "Yamaha", "MT-07", 5000, 689)

    parc.ajouterVehicule(voiture1)
    parc.ajouterVehicule(voiture2)
    parc.ajouterVehicule(moto1)

    // Question 2 : Ajout de conducteurs
    val conducteur1 = Conducteur("Ali", "Kacem", "PERM123")
    val conducteur2 = Conducteur("Nora", "Bennani", "PERM456")

    // Question 3 : Réservations
    try {
        parc.reserverVehicule("123ABC", conducteur1, "2025-10-05", "2025-10-10")
        parc.reserverVehicule("789GHI", conducteur2, "2025-10-06", "2025-10-09")
    } catch (e: Exception) {
        println("Erreur : ${e.message}")
    }

    // Question 4 : Gestion des exceptions
    try {
        parc.reserverVehicule("123ABC", conducteur2, "2025-10-07", "2025-10-12") // déjà réservé
    } catch (e: Exception) {
        println("Exception capturée : ${e.message}")
    }

    try {
        parc.reserverVehicule("000XYZ", conducteur1, "2025-10-08", "2025-10-11") // véhicule inexistant
    } catch (e: Exception) {
        println("Exception capturée : ${e.message}")
    }

    // Question 5 : Affichage
    parc.afficherVehiculesDisponibles()
    parc.afficherReservations()

    // Clôture d'une réservation
    val reservation = parc.reservations.first()
    reservation.cloturerReservation(15200)
    reservation.afficherDetails()
}
