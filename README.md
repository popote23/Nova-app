# NOVA TV — Android TV / Box

Projet Android natif Kotlin pour box Android, avec une interface pensée pour la télécommande et un lecteur vidéo Media3.

## Fonctionnalités présentes
- Cible minimale Android 5.0 (API 21).
- Interface paysage, navigation au D-pad/télécommande et tuiles visuelles.
- Catégories par pays et genres, section satellites à but informatif.
- Import de playlist M3U depuis le stockage local.
- Analyse des entrées `#EXTINF`, `group-title`, `tvg-logo`, `tvg-id` et lecture des URL HTTP(S) avec Media3/ExoPlayer (notamment HLS lorsque le flux est compatible).
- Workflow GitHub Actions pour compiler un APK debug et le joindre comme artifact.

## Limites importantes
- Aucune URL de chaîne n'est intégrée par défaut : seuls les flux que tu ajoutes et dont l'accès est gratuit et autorisé seront lus. Les URLs publiques peuvent changer, être géobloquées ou avoir des conditions d'utilisation.
- Le catalogue se remplit à partir des catégories `group-title` de la playlist M3U; les catégories vides indiquent qu'aucun flux correspondant n'a été importé.
- La page Satellites est informative. Une box Android TV ordinaire ne peut pas syntoniser Nilesat ou un autre satellite sans matériel tuner satellite compatible; l'application ne remplace pas un récepteur DVB-S/S2.
- Cette première version est un socle compilable; elle ne prétend pas être testée sur chaque box ou chaque flux. Un build GitHub doit confirmer la compilation.

## Compilation locale
Avec JDK 17 et Gradle 8.7 :
```bash
gradle assembleDebug
```
APK généré : `app/build/outputs/apk/debug/app-debug.apk`.

## Compilation sur GitHub
1. Crée un dépôt GitHub vide.
2. Décompresse ce projet et envoie tous les fichiers à la racine du dépôt (pas le dossier ZIP lui-même).
3. Va dans **Actions** et ouvre **Build NOVA TV APK**. Le workflow démarre après un push, ou lance-le avec **Run workflow**.
4. Quand le workflow réussit, ouvre son exécution et télécharge l'artifact **NOVA-TV-debug-APK**.
5. Décompresse l'artifact téléchargé pour obtenir `app-debug.apk`, puis transfère-le sur la box et installe-le.

## Playlist M3U
Utilise une playlist `.m3u` ou `.m3u8` contenant uniquement des flux gratuits que tu es autorisé à regarder. Exemple de structure (URL illustrative, non fonctionnelle) :
```m3u
#EXTM3U
#EXTINF:-1 group-title="Maroc" tvg-id="exemple.ma",Nom de chaîne
https://exemple.org/flux.m3u8
```
