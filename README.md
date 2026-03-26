# GameEDR

<p align="center">
  <img src="app/src/main/res/drawable-nodpi/logo_gameedr_android_icon.png" alt="GameEDR Logo" width="180" />
</p>

**GameEDR** est une application Android educative et hors ligne, concue en **Kotlin + Jetpack Compose**, pour s'entrainer aux exercices de reseaux IPv4.

L'application propose un theme militaire sobre et une experience pedagogique progressive avec 4 niveaux de difficulte.

---

## Fonctionnalites

### Modes de jeu

| Mode | Description |
|------|-------------|
| **Calcul d'adresse reseau et broadcast** | Trouver l'adresse reseau et l'adresse de broadcast a partir d'un hote IPv4 |
| **Decoupage en sous-reseaux** | Diviser un reseau en sous-reseaux egaux et alignes |
| **VLSM** | Allouer des blocs principaux en respectant le tri des besoins, le choix du masque et l'alignement |

### Niveaux de difficulte

| Niveau | Description |
|--------|-------------|
| Niveau 1 | Bases et blocs simples |
| Niveau 2 | Masques varies et calculs intermediaires |
| Niveau 3 | Raisonnement elargi sur plusieurs sous-reseaux |
| Niveau 4 | Situation operationnelle avec alignements fins |

### Caracteristiques

- 100 % hors ligne, aucune connexion reseau requise
- Interface en francais, orientation portrait
- Sans compte, sans score, sans backend, sans publicite
- Validation pedagogique avec explication en cas d'erreur
- Exercices generes aleatoirement a partir de reseaux prives RFC1918

---

## Installation

### Depuis la release GitHub

1. Telecharger le fichier `GameEDR-v1.0.0.apk` depuis la page [Releases](../../releases).
2. Transferer l'APK sur un appareil Android (API 26+ / Android 8.0+).
3. Autoriser l'installation depuis des sources inconnues si necessaire.
4. Installer et lancer l'application.

### Compilation depuis les sources

**Prerequis** : Java JDK 17, Android SDK (API 34)

```bash
# Clone du depot
git clone https://github.com/<owner>/GameEDR.git
cd GameEDR

# Build debug
./gradlew assembleDebug

# Build release signee
./gradlew assembleRelease
```

Les APK sont generes dans :
- Debug : `app/build/outputs/apk/debug/app-debug.apk`
- Release : `app/build/outputs/apk/release/app-release.apk`

---

## Architecture

```
app/src/main/java/fr/gir/gameedr/
├── navigation/          Routes et graphe de navigation
├── ui/
│   ├── components/      Composants Compose reutilisables
│   ├── screen/          Ecrans (Accueil, Selection, Credits)
│   ├── screen/game/     Ecrans de jeu par mode
│   └── theme/           Theme, couleurs, typographie
├── domain/
│   ├── model/           Modeles metier (IPv4, exercices, modes)
│   ├── service/         Parsing IPv4 et calcul reseau
│   ├── generator/       Generation aleatoire des exercices
│   └── validator/       Validation pedagogique des reponses
└── data/                Facade repository pour l'UI
```

### Flux de navigation

```
Accueil → Selection du mode → Selection du niveau → Ecran de jeu
                                                       ↓
                                                   Correction avec
                                                   explication detaillee
```

---

## Choix techniques

- **Kotlin + Jetpack Compose** : UI moderne, declarative et maintenable
- **Navigation Compose** : flux simple `Accueil → Mode → Niveau → Jeu`
- **Architecture separee** : UI, logique metier, generateurs, validateurs et modeles distincts
- **Logique 100 % locale** : aucune dependance reseau ni stockage serveur
- **Validation pedagogique** : chaque mode retourne une explication courte en cas d'erreur
- **Theme militaire sobre** : palette sombre, kaki, sable et accents laiton

---

## Configuration requise

- Android 8.0 (API 26) minimum
- Android 14 (API 34) cible
- Aucune permission speciale requise

---

## Licence

Projet educatif - Tous droits reserves.
