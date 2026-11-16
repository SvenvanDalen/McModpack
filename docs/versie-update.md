# Naar een nieuwere Minecraft-versie (DR-1005)

Doe dit in een eigen branch en werk één ding tegelijk bij. Elke stap heeft een controle.

## 1. Zoek de nieuwe versies
- Minecraft, Fabric Loader en Fabric API: https://meta.fabricmc.net en https://fabricmc.net/develop
- Loom, Gradle en Java: kijk in het officiële `fabric-example-mod` voor dezelfde Minecraft-versie.
- Noteer de nieuwe waarden en de datum in `CLAUDE.md` (regel "Versies").

## 2. Pas de versies aan
- `gradle.properties`: `minecraft_version`, `loader_version`, `loom_version`, `fabric_api_version`.
- `gradle/wrapper/gradle-wrapper.properties`: Gradle-versie, als het voorbeeld-mod dat vraagt.
- JDK: zet `JAVA_HOME` op de Java-versie die het voorbeeld vraagt.

## 3. Bouw en los fouten op
1. `./gradlew build` (bij een certificaatfout: `JAVA_TOOL_OPTIONS=-Djavax.net.ssl.trustStoreType=Windows-ROOT`).
2. Fouten zijn bijna altijd hernoemde of verplaatste Minecraft-klassen. Zoek de nieuwe naam met `javap` op de Minecraft-jar in `~/.gradle/caches/fabric-loom/minecraftMaven/`.
3. Alleen de dunne Minecraft-laag (`data/`, `src/client`) hoort te breken. Breekt `core/`, dan zit er een Minecraft-import in die er niet hoort (de test `CoreIsolationTest` vangt dat).
4. Check dat de JSON-formaten (blockstates, modellen, items, recepten, taalbestanden) nog kloppen; die veranderen soms tussen versies.

## 4. Controleer
- [ ] `./gradlew build` is groen, inclusief alle GameTests ("All N required tests passed").
- [ ] Het spel start, een nieuwe wereld laadt, `/dr debug` werkt.
- [ ] Eén machine gebouwd en één quest afgerond.
- [ ] Prestaties opnieuw gemeten (zie `docs/aanbevolen-instellingen.md` en DR-801).
- [ ] Eventuele optionele mods (Sodium, Lithium) hebben een versie voor de nieuwe Minecraft; zo niet, noteer dat in de README.

## 5. Rond af
- Verhoog `version` in `gradle.properties` en schrijf een regel in `CHANGELOG.md`.
- Merge via een pull request met in de beschrijving wat je wel en niet in het echte spel hebt gecontroleerd.
