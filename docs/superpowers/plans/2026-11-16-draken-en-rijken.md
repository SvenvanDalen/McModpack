# Draken en Rijken Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Een eigen Fabric-mod die Minecraft Java omvormt tot een solo fantasy-RPG met een industrielaag, speelbaar op een matige laptop.

**Architecture:** Alle spelregels (levels, skill tree, quests, rijk, boss-fasen) zitten in pure Java-klassen zonder Minecraft-imports, zodat ze met JUnit te testen zijn. Een dunne laag koppelt die aan Minecraft (events, blokken, entiteiten, opslag). Inhoud (quests, vaardigheden, structuren) staat als JSON in `data/drakenrijken/`.

**Tech Stack:** Java (versie die de gekozen Minecraft-versie vereist, volgens bronnen Java 25 voor 26.x), Gradle met Fabric Loom, Fabric API, JUnit 5, Fabric GameTest, Blockbench. Prestatiemods (Sodium, Lithium) alleen als losse extra's.

**Spec:** `docs/superpowers/specs/2026-11-16-draken-en-rijken-design.md`

## Global Constraints

- Minecraft Java Edition, solo; geen multiplayer-features (spec sectie 2 en gebruikersantwoord).
- "Nieuwste versie": de nieuwste stabiele Minecraft-release waarvoor Fabric Loader, Fabric API en Loom bestaan. Bronnen noemen 26.3 (sept. 2026), maar dit is **niet geverifieerd**; Taak 1 beslist.
- Mod-loader Fabric; geen bestaande RPG- of industriemods als basis (spec sectie 5).
- Doel op de doellaptop: 40+ FPS op lage instelling en onder ca. 3 GB RAM voor het spel (spec sectie 2).
- Eigen teksturen 16x16, modellen laag in polygonen (spec sectie 5).
- Geen zware berekeningen per tick; harde grenzen aan actieve machines en entiteiten; wereldgeneratie bouwt geen grote stukken tegelijk (spec sectie 5).
- Data (quests, klassen, recepten, structuren) zoveel mogelijk als JSON (spec sectie 7).
- Drie industrietijdperken: watermolen en smederij, stoom, rune-machines. Drie klassen: ridder, magiër, schurk.

## Review Focus

1. Nieuwe speler zonder opgeslagen klasse: moet een klassekeuze krijgen, geen crash.
2. Kapot of ontbrekend quest-JSON-bestand: overslaan met een logregel, spel start gewoon.
3. Opslaan en laden van spelerdata (klasse, level, quests): na herstart exact hetzelfde.
4. Meer machines dan de grens: extra machines pauzeren in plaats van de tick-tijd op te rekken.
5. Speler sterft of verlaat de wereld tijdens de drakenstrijd: boss reset zonder dubbele beloning.

Elk punt heeft hieronder een test in de taak die de code bezit.

## File Structure

```
build.gradle, gradle.properties, settings.gradle
src/main/java/com/drakenrijken/
  DrakenRijken.java              ModInitializer, registraties
  core/                          pure Java, geen Minecraft-imports
    PlayerClass.java             enum RIDDER, MAGIER, SCHURK
    Progression.java             xp- en levelregels
    SkillTree.java               vaardigheden en punten
    QuestEngine.java             voortgang van quests
    RealmState.java              rijk-niveau uit industrie
    BossPhases.java              fasen van de draak
    MachineBudget.java           grens op actieve machines
  data/PlayerData.java           opslag per speler (codec)
  data/QuestLoader.java          leest quest-JSON
  world/                         structuren en koninkrijkplaatsing
  industry/                      blokken per tijdperk
  entity/DragonEntity.java       eindbaas
src/main/resources/data/drakenrijken/{quests,skills,structure}/
src/test/java/com/drakenrijken/core/   JUnit-tests
src/gametest/                    Fabric GameTests
tools/perf/measure.md            meetprocedure FPS en RAM
```

---

### Task 1: Projectbasis en versiecontrole

**Files:**
- Create: `build.gradle`, `gradle.properties`, `settings.gradle`, `src/main/java/com/drakenrijken/DrakenRijken.java`, `src/main/resources/fabric.mod.json`, `tools/perf/measure.md`
- Test: `src/test/java/com/drakenrijken/SmokeTest.java`

**Interfaces:**
- Produces: `DrakenRijken implements ModInitializer` met `public static final String MOD_ID = "drakenrijken"`.

- [ ] **Step 1:** Zoek de nieuwste stabiele Minecraft-versie en de bijbehorende Fabric Loader, Fabric API en Loom op fabricmc.net (de template-generator onder "develop"). Noteer versies en vereiste Java-versie bovenaan `gradle.properties` als commentaar met datum.
- [ ] **Step 2:** `git init` in de projectmap en voeg een `.gitignore` toe (`build/`, `.gradle/`, `run/`).
- [ ] **Step 3:** Genereer het project met de Fabric-template voor die versie en pas `MOD_ID`, pakket `com.drakenrijken` en `fabric.mod.json` aan.
- [ ] **Step 4:** Schrijf `SmokeTest.modIdIsStable()` met `assertEquals("drakenrijken", DrakenRijken.MOD_ID)`. Run `./gradlew test`. Verwacht: PASS.
- [ ] **Step 5:** Run `./gradlew runClient`. Verwacht: Minecraft start met de mod in de modlijst, geen crash.
- [ ] **Step 6:** Schrijf `tools/perf/measure.md`: nieuwe wereld, vaste route van 2 minuten, F3 voor FPS, taakbeheer voor RAM, resultaat in een tabel. Voer die uit op de doellaptop en noteer de uitgangswaarden.
- [ ] **Step 7:** Commit `chore: fabric-projectbasis en meetprocedure`.

### Task 2: Klassen, levels en skill tree

**Files:**
- Create: `core/PlayerClass.java`, `core/Progression.java`, `core/SkillTree.java`, `data/PlayerData.java`, `src/main/resources/data/drakenrijken/skills/*.json`
- Test: `core/ProgressionTest.java`, `core/SkillTreeTest.java`, `PlayerDataTest.java`

**Interfaces:**
- Produces: `Progression.xpForLevel(int level): int`, `Progression.addXp(PlayerProgress p, int xp): PlayerProgress`, `SkillTree.unlock(PlayerProgress p, String skillId): Result`, `PlayerData.CODEC`, `PlayerData.empty()` (geen klasse gekozen).

Mijn keuzes: `xpForLevel(n) = 100 * n`, maximaal level 30, 1 vaardigheidspunt per level.

- [ ] **Step 1:** Schrijf tests: `xpForLevel(3) == 300`; `addXp` bij 250 xp op level 2 geeft level 3; level blijft 30 en xp loopt niet over bij extra xp; `unlock` zonder punten geeft `Result.NO_POINTS`; `unlock` van een vaardigheid waarvan de ouder ontbreekt geeft `Result.LOCKED`.
- [ ] **Step 2:** Schrijf `PlayerDataTest.roundTripKeepsEverything()`: encode en decode via `PlayerData.CODEC` geeft gelijke data. Schrijf `emptyHasNoClass()` (Review Focus 1, 3).
- [ ] **Step 3:** Run, verwacht FAIL. Implementeer de klassen met de signaturen hierboven en een JSON-lader voor vaardigheden (drie per klasse als start).
- [ ] **Step 4:** Koppel in `DrakenRijken`: bij het inloggen zonder klasse opent een keuzescherm. Voeg een GameTest toe die een nieuwe speler laat inloggen zonder crash.
- [ ] **Step 5:** Run `./gradlew test` en `./gradlew runGametest`. Verwacht: PASS.
- [ ] **Step 6:** Commit `feat: klassen, levels en skill tree`.

### Task 3: Questmotor en akte 1

**Files:**
- Create: `core/QuestEngine.java`, `data/QuestLoader.java`, `src/main/resources/data/drakenrijken/quests/akte1_*.json`
- Test: `core/QuestEngineTest.java`, `data/QuestLoaderTest.java`

**Interfaces:**
- Consumes: `PlayerData` uit Taak 2.
- Produces: `QuestEngine.onEvent(QuestEvent e, PlayerData d): PlayerData`, `QuestLoader.load(Stream<String> jsonFiles): List<QuestDef>` (slaat ongeldige bestanden over).

- [ ] **Step 1:** Tests: een quest "verzamel 5 hout" wordt voltooid na 5 events; volgorde van een quest-keten wordt afgedwongen; `load` met één kapot JSON-bestand geeft de overige quests terug en logt een waarschuwing (Review Focus 2).
- [ ] **Step 2:** Run, verwacht FAIL. Implementeer de motor en lader; schrijf vijf quests voor akte 1 (dorp, eerste smederij, eerste dungeon).
- [ ] **Step 3:** Koppel aan Minecraft-events (blok breken, entiteit doden, item oprapen) in een dunne `QuestEvents`-klasse.
- [ ] **Step 4:** Run tests. Verwacht: PASS. Commit `feat: questmotor en akte 1`.

### Task 4: Wereldgeneratie

**Files:**
- Create: `world/KingdomPlacement.java`, `src/main/resources/data/drakenrijken/structure/{dorp,burcht,dungeon}.*`
- Test: `world/KingdomPlacementTest.java`, GameTest `StructureSpawnTest`

**Interfaces:**
- Produces: `KingdomPlacement.chunkHasKingdom(long seed, int chunkX, int chunkZ): boolean` (deterministisch).

- [ ] **Step 1:** Tests: dezelfde seed en chunk geven altijd hetzelfde antwoord; over 10.000 chunks ligt de dichtheid tussen 0,5% en 2% (mijn keuze).
- [ ] **Step 2:** Implementeer de plaatsing en drie structuren; één structuur per chunk-activatie om grote bouwpieken te vermijden.
- [ ] **Step 3:** Meet met `tools/perf/measure.md` tijdens het verkennen. Verwacht: binnen de doelen uit Taak 1.
- [ ] **Step 4:** Commit `feat: koninkrijken en dungeons`.

### Task 5: Industrie tijdperk 1

**Files:**
- Create: `industry/WaterWheelBlock.java`, `industry/ForgeBlock.java`, `core/MachineBudget.java`, recepten-JSON
- Test: `core/MachineBudgetTest.java`, GameTest `WaterWheelTest`

**Interfaces:**
- Produces: `MachineBudget.tryActivate(MachineId id): boolean`, `MachineBudget.release(MachineId id)`; harde grens 64 actieve machines per wereld (mijn keuze).

- [ ] **Step 1:** Tests: de 65e activatie geeft `false`; na `release` lukt een nieuwe weer (Review Focus 4). GameTest: een waterrad naast stromend water levert kracht aan een smederij.
- [ ] **Step 2:** Implementeer de blokken met eigen 16x16-teksturen; machines buiten geladen chunks tikken niet.
- [ ] **Step 3:** Run tests en meet. Commit `feat: industrie tijdperk 1`.

### Task 6: Draak en akte 2

**Files:**
- Create: `core/BossPhases.java`, `entity/DragonEntity.java`, quests voor akte 2
- Test: `core/BossPhasesTest.java`, GameTest `DragonResetTest`

**Interfaces:**
- Produces: `BossPhases.phaseFor(float healthFraction): int` (fasen bij 100%, 66% en 33%), `BossPhases.rewardOnce(UUID player, boolean alreadyRewarded): boolean`.

- [ ] **Step 1:** Tests: `phaseFor(0.7f) == 1`, `phaseFor(0.5f) == 2`, `phaseFor(0.2f) == 3`; beloning wordt maar één keer gegeven; GameTest: speler sterft in de strijd, de draak herstelt naar fase 1 zonder beloning (Review Focus 5).
- [ ] **Step 2:** Implementeer de entiteit met een laag-polygoon model uit Blockbench en maximaal 8 eigen deeltjes per aanval.
- [ ] **Step 3:** Run tests en meet de strijd op de doellaptop. Commit `feat: de draak`.

### Task 7: Industrie tijdperk 2 en 3 en rijksysteem

**Files:**
- Create: `core/RealmState.java`, `industry/steam/*`, `industry/rune/*`
- Test: `core/RealmStateTest.java`

**Interfaces:**
- Produces: `RealmState.levelFor(int totalMachinePower): int` (niveaus 0 tot 5), `RealmState.effects(int level): RealmEffects`.

- [ ] **Step 1:** Tests: macht 0 geeft niveau 0; monotone stijging; niveau 5 is het maximum bij zeer hoge macht.
- [ ] **Step 2:** Implementeer stoom en rune-machines binnen `MachineBudget`; het rijk-niveau bepaalt extra dorpelingen, handel en aanvallen.
- [ ] **Step 3:** Run tests en meet. Commit `feat: stoom, runen en rijksysteem`.

### Task 8: Prestatie-ronde en release

**Files:**
- Modify: delen die uit de metingen als zwaarste blijken; `tools/perf/measure.md` (resultaten)
- Create: `README.md`, release-jar

- [ ] **Step 1:** Voer de volledige meetprocedure uit op de doellaptop en vul de tabel. Verwacht: 40+ FPS en onder ca. 3 GB RAM. Zo niet: profileer, snoei de zwaarste onderdelen en meet opnieuw.
- [ ] **Step 2:** Speel het verhaal van begin tot eind uit (spec sectie 9) en noteer crashes; los ze op.
- [ ] **Step 3:** Schrijf een `README.md` met installatie (Java-versie, Fabric, Fabric API, optioneel Sodium en Lithium).
- [ ] **Step 4:** Bouw `./gradlew build`, commit `release: eerste speelbare versie`.

---

## Self-Review

- **Spec-dekking:** klassen en levels (T2), quests en verhaal (T3), wereld (T4), industrie 1 (T5), draak (T6), industrie 2 en 3 plus rijk (T7), prestaties (T1, T8). Alle doelen uit sectie 2 hebben een taak.
- **Typen:** `PlayerData` (T2) wordt gebruikt in T3; `MachineBudget` (T5) in T7. Namen komen overeen.
- **Review Focus:** punten 1 tot 5 hebben een test in T2, T3, T5 en T6.
- **Bekende onzekerheid:** de Minecraft-, Loader- en Java-versies zijn niet geverifieerd; T1 stap 1 beslist. Latere taken kunnen op API-namen van die versie moeten worden aangepast.
