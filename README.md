# Draken en Rijken

Een solo fantasy-RPG als Fabric-mod voor Minecraft Java: kies een klasse (Ridder, Magiër of Schurk), doe quests, bouw een rijk, ontwikkel industrie in drie tijdperken en versla een draak.

> Status: in ontwikkeling (versie 0.1.0). De spelregels zijn getest, maar het meeste is nog niet in het draaiende spel geprobeerd.

## Wat heb je nodig

| Onderdeel | Versie |
|---|---|
| Minecraft Java | 26.3 |
| Java | 25 |
| Fabric Loader | 0.20.1 of nieuwer |
| Fabric API | 0.166.2+26.3 |

Optioneel voor betere prestaties: Sodium en Lithium (zie `docs/aanbevolen-instellingen.md`).

## Installeren

1. Installeer Java 25.
2. Installeer Fabric Loader 0.20.1 voor Minecraft 26.3 via https://fabricmc.net/use/.
3. Zet de Fabric API (0.166.2+26.3) in de map `mods`.
4. Zet de jar van Draken en Rijken in dezelfde map `mods`.
5. Start Minecraft met het Fabric-profiel.

## Zelf bouwen

Zet `JAVA_HOME` op JDK 25 en draai:

```
./gradlew build
```

Bij een certificaatfout in Gradle: `JAVA_TOOL_OPTIONS=-Djavax.net.ssl.trustStoreType=Windows-ROOT`.
Tests draaien met `./gradlew test`.

## Meer lezen

- Ontwerp: `docs/superpowers/specs/2026-11-16-draken-en-rijken-design.md`
- Plan: `docs/superpowers/plans/2026-11-16-draken-en-rijken.md`
- Backlog: `docs/backlog.md`
- Wijzigingen: `CHANGELOG.md`
