# Kadre Library Foundation Design

**Date:** 2026-08-20  
**Status:** Draft for review

## Goal

Transformer le dépôt `Graphiks-org/kadre`, actuellement issu d’un template
Kotlin Multiplatform (KMP), en un socle propre de bibliothèque multiplateforme
sur le modèle structurel de [`kffi`](https://github.com/Graphiks-org/kffi), sans
copier son implémentation FFI ni inventer l’API métier de `kadre`.

Cette première tranche doit laisser un dépôt immédiatement exploitable pour
accueillir les futures sources de la bibliothèque, avec une identité Gradle,
des coordonnées Maven, une documentation, une CI et une arborescence cohérentes.

## Context and constraints

Le template actuel contient :

- un module `:shared` nommé `io.ygdrasil.shared` ;
- des exemples Clean Architecture / DDD, Compose Multiplatform, Koin et Ktor ;
- une publication et une documentation encore rattachées à
  `ygdrasil-io/project-template` ;
- une CI qui référence `:shared` et le comportement d’un starter pack.

Le projet voisin `kffi` adopte au contraire un dépôt KMP autonome : identité
racine explicite, module de bibliothèque principal, conventions de publication
réutilisables, documentation Dokka/MkDocs, tests par plateforme et CI centrée
sur les tâches de bibliothèque.

Contraintes retenues pour `kadre` :

1. conserver le nom de produit `kadre` et le dépôt `Graphiks-org/kadre` ;
2. utiliser `org.graphiks` comme group Maven et `kadre` comme artifact principal ;
3. conserver pour l’instant les cibles déjà supportées par le template : JVM,
   Android, iOS arm64 et iOS Simulator arm64 ;
4. ne pas ajouter de backend ou de module spécifique à `kffi` sans besoin
   fonctionnel explicite ;
5. garder la licence MIT et les workflows de contribution existants, en les
   mettant à jour pour le nouveau nom de module.

## Chosen architecture

### Project identity and module layout

Le dépôt deviendra un projet racine nommé `kadre-root` avec les modules :

```text
:kadre   bibliothèque KMP principale
:docs    documentation Dokka/MkDocs
```

Le module `:shared` sera remplacé par `:kadre`. Le code public sera placé sous
`org.graphiks.kadre` dans les source sets KMP correspondants. Le namespace
Android sera `org.graphiks.kadre`.

La version par défaut restera `1.0.0-SNAPSHOT`, sélectionnable par la propriété
Gradle `releaseVersion`, comme dans `kffi`. Le build racine et la convention de
publication utiliseront `org.graphiks`; la convention Maven utilisera le nom
réel du projet au lieu d’un artifact codé en dur (`shared`).

### Library build

`kadre/build.gradle.kts` utilisera uniquement les conventions nécessaires à une
bibliothèque :

- `ygdrasil.conventions.kmp-library` ;
- `ygdrasil.conventions.kmp-publish` ;
- `ygdrasil.conventions.kmp-dokka`.

Les plugins et dépendances de démonstration liés à Compose, Koin, Ktor,
Kotlin Serialization et `androidx.activity:activity-compose` seront retirés du
module et du version catalog. Le module conservera le support de test Kotlin
nécessaire aux futurs tests de la bibliothèque, mais ne gardera aucun exemple
de ViewModel, repository, use case ou injection de dépendances.

Les conventions Gradle conserveront leur package interne
`ygdrasil.conventions`, car il s’agit de l’identifiant d’outillage du
`buildSrc`, et non de l’identité publiée de `kadre`.

### Source tree and API boundary

Les sources d’exemple sous `io/ygdrasil/shared` seront supprimées et remplacées
par une arborescence `org/graphiks/kadre`. Cette tranche ne définira pas de
contrat métier, de modèle de données ni d’API inspirée de `kffi`. Les futurs
types de `kadre` seront ajoutés dans `commonMain`, avec des implémentations
spécifiques dans `androidMain`, `iosMain` ou `jvmMain` uniquement lorsque le
contrat commun l’exigera.

L’absence d’une API métier initiale est volontaire : le dépôt doit fournir un
cadre de publication et de test sans rendre publique une abstraction provisoire.

### Publication and documentation

Les métadonnées Maven et Dokka seront alignées sur `kadre` :

- nom et description de `kadre` ;
- URL du dépôt `https://github.com/Graphiks-org/kadre` ;
- SCM Git correspondant ;
- module Dokka `kadre` ;
- source link vers `kadre/src/commonMain/kotlin` ;
- site MkDocs `https://graphiks-org.github.io/kadre/` ;
- API générée sous `docs/api/kadre`.

Le README et les pages EN/FR de documentation décriront un runtime/library KMP
en préparation, les cibles actuellement supportées et les commandes de
vérification. Le guide "Getting Started" cessera d’être une checklist de
renommage de template et deviendra un guide de contribution au dépôt.

### CI and repository tooling

Les workflows et scripts seront adaptés aux nouvelles tâches :

- les tests rapides cibleront `:kadre:jvmTest` ;
- les checks complets conserveront la vérification KMP du projet ;
- le script de version vérifiera `:kadre:properties` ;
- les scopes Conventional Commits et les exemples de commandes utiliseront
  `kadre`, `buildSrc`, `docs` et `release`.

Les badges, liens de support, changelog et fichiers de contribution ne devront
plus présenter le dépôt comme un starter pack générique.

## Error handling and compatibility

Cette adaptation ne modifie pas encore un comportement runtime. Les erreurs à
prévenir sont donc des erreurs d’intégration : coordonnées Maven incohérentes,
tâches Gradle pointant vers `:shared`, liens de documentation obsolètes et
source links Dokka invalides.

La compatibilité est traitée par des vérifications explicites : recherche des
anciennes identités, lecture de la version Gradle effective, compilation et
tests JVM, génération de la documentation API et build racine.

Les cibles Native supplémentaires de `kffi` (macOS, Linux, MinGW, Android
Native) sont hors périmètre de cette tranche. Elles feront l’objet d’une
spécification séparée si l’API de `kadre` en a besoin.

## Verification and acceptance criteria

La tranche sera considérée comme valide lorsque :

1. `settings.gradle.kts` expose `kadre-root`, `:kadre` et `:docs` ;
2. le module principal est publié sous `org.graphiks:kadre` avec la version
   par défaut `1.0.0-SNAPSHOT` ;
3. aucune source ou tâche ne référence encore `:shared`, `io.ygdrasil.shared`,
   `project-template` ou le contenu applicatif du starter pack ;
4. `./gradlew :kadre:jvmTest --no-daemon` termine avec succès ;
5. `./gradlew build --no-daemon` termine avec succès ;
6. `./gradlew :docs:embedDokkaIntoMkDocs --no-daemon` termine avec succès ;
7. `scripts/test-publish-version.sh` vérifie la nouvelle tâche `:kadre` ;
8. la documentation et la CI utilisent les noms et URLs de `kadre`.

## Out of scope

- implémenter le cœur fonctionnel de `kadre` ;
- copier les sources FFI de `kffi` ;
- ajouter JNI, cinterop, `libffi`, benchmarks ou modules POSIX/Wayland ;
- modifier le contrat Maven au-delà du premier artifact KMP ;
- introduire une dépendance runtime avant que l’API ne le justifie ;
- réécrire les politiques GitHub ou la licence du projet.

