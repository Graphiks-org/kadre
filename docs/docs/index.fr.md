# Documentation kadre

`kadre` est une bibliothèque Kotlin Multiplatform pour l’écosystème Graphiks.
Ce dépôt est en cours de préparation comme fondation réutilisable, avec une
API publiée, des guides pour les contributrices et contributeurs, ainsi qu’une
référence générée.

## Statut actuel

Le projet est en phase de mise en place de sa fondation. L’objectif est de
stabiliser la structure du module, la documentation, les métadonnées de
publication et le workflow de contribution avant d’élargir la surface runtime.

## Cibles prises en charge

- JVM (Java 25)
- Android (minSdk 24)
- iOS arm64
- iOS Simulator arm64

## Commandes de développement local

À exécuter depuis la racine du dépôt :

```bash
./gradlew :kadre:jvmTest
./gradlew build
./gradlew :docs:embedDokkaIntoMkDocs
```

## Référence API

La référence API est générée avec Dokka puis intégrée dans ce site MkDocs.
Après `./gradlew :docs:embedDokkaIntoMkDocs`, vous pouvez consulter les pages
API générées en local ou via le site publié :
<https://graphiks-org.github.io/kadre/>.

## Liens du projet

- Dépôt : <https://github.com/Graphiks-org/kadre>
- Site de documentation : <https://graphiks-org.github.io/kadre/>
- Guide de contribution : [CONTRIBUTING.md](../../CONTRIBUTING.md)
- Support : [SUPPORT.fr.md](../../SUPPORT.fr.md)
