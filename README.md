# PharmaTrack 🏥

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green)
![Angular](https://img.shields.io/badge/Angular-17%2B-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED)

> **Une solution ERP complète et moderne pour la gestion intelligente de pharmacie.**

---

## 📖 À propos

**PharmaTrack** est une application web full-stack conçue pour moderniser et simplifier le quotidien des pharmaciens. Elle offre une plateforme intuitive pour la gestion des stocks, la facturation au comptoir, et le suivi financier.

Développé avec une architecture **Clean Code**, ce projet allie la puissance de **Spring Boot** pour le backend à la réactivité d'**Angular** pour le frontend.

---

## ✨ Fonctionnalités Clés

### 📦 Gestion des Stocks & Catalogue
- **Inventaire Temps Réel** : Suivi précis des quantités (Médicaments).
- **Catégorisation** : Organisation par familles thérapeutiques.
- **Mouvements de Stock** : Traçabilité totale des entrées (achats) et sorties (ventes/pertes).
- **Alertes** : Détection automatique des stocks critiques et dates de péremption.

### 💰 Ventes & Facturation (POS)
- **Caisse Enregistreuse** : Interface de vente rapide pour le comptoir.
- **Panier Dynamique** : Ajout/suppression d'articles, calcul automatique des totaux.
- **Historique des Ventes** : Consultation détaillée des transactions passées.

### 🚚 Achats & Fournisseurs
- **Commandes Fournisseurs** : Gestion du réapprovisionnement.
- **Annuaire Partenaires** : Base de données des fournisseurs et laboratoires.

### 📊 Tableau de Bord (Dashboard)
- **Visualisation de Données** : Graphiques interactifs (via **Chart.js**) pour l'analyse des ventes.
- **KPIs** : Chiffre d'affaires journalier/mensuel, Top produits, Alertes actives.

### 🔐 Sécurité & Accès
- **Authentification Forte** : Sécurisation via **JWT (JSON Web Token)**.
- **Gestion des Rôles** : Permissions différenciées (Admin vs Pharmacien).

---

## 🛠️ Stack Technique

Une stack technologique moderne sélectionnée pour sa performance et sa scalabilité.

### Backend (API REST)
| Composant | Technologie | Description |
| :--- | :--- | :--- |
| **Langage** | **Java 17** | Version LTS robuste et performante. |
| **Framework** | **Spring Boot 3** | Développement rapide d'applications Java. |
| **Sécurité** | **Spring Security** | Authentification et autorisation (Stateless). |
| **Data** | **Spring Data JPA** | Abstraction pour l'accès aux données. |
| **SGBD** | **PostgreSQL 15** | Base de données relationnelle fiable. |
| **Outils** | **MapStruct & Lombok** | Simplification du code et mapping DTO. |
| **Doc** | **OpenAPI (Swagger)** | Documentation automatique de l'API. |

### Frontend (SPA)
| Composant | Technologie | Description |
| :--- | :--- | :--- |
| **Framework** | **Angular 17+** | Plateforme web moderne et modulaire. |
| **Design** | **Tailwind CSS** | Framework CSS utilitaire pour un UI rapide. |
| **Composants** | **Angular Material** | Composants UI riches et accessibles. |
| **Charts** | **Chart.js** | Visualisation de données graphiques. |
| **Tests** | **Vitest** | Framework de test ultra-rapide. |

### DevOps & Infrastructure
- **Docker** : Conteneurisation de tous les services (Back, Front, DB).
- **Docker Compose** : Orchestration simple pour le développement local.
- **GitHub Actions** : Pipeline CI/CD pour l'intégration continue.

---

## 🚀 Installation et Démarrage

### Prérequis
- [Docker Desktop](https://www.docker.com/products/docker-desktop) (Recommandé)
- *Ou pour le mode manuel :* JDK 17, Node.js v20+, PostgreSQL.

### 🐳 Option 1 : Démarrage Rapide (Docker)
L'application complète se lance en une seule commande.

1. **Cloner le projet**
   ```bash
   git clone https://github.com/votre-username/PharmaTrack.git
   cd PharmaTrack
   ```

2. **Lancer les conteneurs**
   ```bash
   docker-compose up -d --build
   ```

3. **Accéder à l'application**
   - 🖥️ **Application (Frontend)** : [http://localhost:4200](http://localhost:4200)
   - ⚙️ **API (Backend)** : [http://localhost:8080](http://localhost:8080)
   - 📑 **Swagger API Docs** : [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
   - 🗄️ **Base de données** : `localhost:5432` (User: `postgres`, Pass: `root`)

### 🔧 Option 2 : Installation Manuelle (Dev)

<details>
<summary><b>Voir les étapes manuelles</b></summary>

#### 1. Base de données
Assurez-vous qu'un serveur PostgreSQL tourne en local.
- Créez une DB nommée : `PharmaTrack`
- Configurez `BackEnd/src/main/resources/application.yaml` si vos accès diffèrent (défaut: `postgres`/`root`).

#### 2. Backend (Spring Boot)
```bash
cd BackEnd
# Compilation et tests (Skip tests pour aller plus vite si besoin: -DskipTests)
./mvnw clean install
# Démarrage
./mvnw spring-boot:run
```

#### 3. Frontend (Angular)
```bash
cd FrontEnd
# Installation des dépendances
npm install
# Serveur de développement
ng serve
```
</details>

---

## 🔐 Identifiants par défaut

Pour tester l'application dès le premier lancement :

| Rôle | Email | Mot de passe |
| :--- | :--- | :--- |
| **Administrateur** | `admin@pharmatrack.com` | `admin123` |

> *Note : Cet utilisateur est créé automatiquement par le script `init-admin.sql` au démarrage.*

---

## 🤝 Contribution & CI/CD

Ce projet intègre un pipeline **GitHub Actions** qui :
1. Compile le code Backend (Maven).
2. Lance les tests unitaires Java.
3. Compile le code Frontend (Angular/NPM).

Toute Pull Request sur la branche `main` déclenche automatiquement ces vérifications.

---

## 👤 Auteur

Projet réalisé par **Houssam**.

---
*Ce projet a été développé dans un but éducatif et démonstratif.*

