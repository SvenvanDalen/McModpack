# Japans koninkrijk Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Een Japans koninkrijk met eigen gebouwen en gebied, de klassen samoerai en onmyoji, eigen wapens en eigen eten (Epic 12, DR-1201 t/m DR-1208).

**Architecture:** Spelregels (koninkrijkstype, klassen, wapen- en etenwaarden) komen in pure Java in `core/`; inhoud (vaardigheden, startsets, buit, spawns, quests) als JSON in `src/main/resources/data/drakenrijken/`; de Minecraft-laag (`data/`, `client/`) blijft dun. Elke task is één story met een eigen issue en PR, zoals Epic 11.

**Tech Stack:** Java 25, Fabric (Minecraft 26.3, Loader 0.20.1, API 0.166.2+26.3), Loom, Gradle 9.8.2, JUnit 5.

**Spec:** `docs/superpowers/specs/2026-11-18-japans-koninkrijk-design.md`

## Global Constraints

- Spelregels in `core/` zonder `net.minecraft`/`net.fabricmc`/`com.mojang`-imports (bewaakt door `CoreIsolationTest`).
- Inhoud als JSON in `data/drakenrijken/`; teksturen 16x16; modellen laag in polygonen.
- Prestaties: 40+ FPS op lage instelling, onder ca. 3 GB RAM; structuren geleidelijk genereren via `StructureQueue`, zichtbare NPC's binnen `EntityBudget.MAX_NPCS`.
- Antwoorden, commentaar en teksten in het Nederlands; teksten in `lang/nl_nl.json` én `lang/en_us.json` (bewaakt door `LangFilesTest`).
- Bouwen met `JAVA_HOME` op JDK 25; bij certificaatfout `JAVA_TOOL_OPTIONS=-Djavax.net.ssl.trustStoreType=Windows-ROOT`. Tests: `./gradlew test`.
- Na elke story: `docs/backlog.md`, `CHANGELOG.md` en de wiki (`McModpack.wiki.git`: Uitleg, Ideeen, Backlog, Wijzigingen) bijwerken.
- Bouw niets buiten het ontwerp; Minecraft-API-details controleren in de officiële documentatie.

## Review Focus

- Bestaande werelden: een opgeslagen speler met klasse RIDDER/MAGIER/SCHURK laadt ongewijzigd (`PlayerDataCodec` met `valueOf`).
- Bestaande koninkrijken verschuiven niet: `hasKingdom` en `companion` geven voor dezelfde seed en chunk hetzelfde antwoord als voor Epic 12.
- Een kapot of ontbrekend JSON-bestand van een nieuwe klasse crasht niet (waarschuwing, zoals `StartKitLoader`).
- Nieuwe klasse zonder startset of zonder vaardigheden: geen crash in keuzescherm, HUD of skill tree.
- Dichtheid van Japanse koninkrijken blijft binnen 0,5% en 2% bij 10.000 chunks, en ze overlappen niet met de grote stad (`CityPlans.isCity`).

---

## Gevonden in de code (voor de bouwer)

- `PlayerClass` is een enum `RIDDER, MAGIER, SCHURK` (`core/PlayerClass.java`). Gebruikt in `ResourceBar.forClass`, `HudLines`, `SkillLoader`, `StartKitLoader`, `PlayerDataCodec`, `DebugCommand`, `SkillScreen` (loopt over `values()`).
- `BundledSkillsTest` eist **6 vaardigheden per klasse** voor elke waarde van `PlayerClass`; de nieuwe klassen krijgen er dus elk 6 (niet 3 zoals in de backlog van Epic 1).
- Vaardigheid-JSON: `{"id":"schildstoot","class":"RIDDER","requires":["taaiheid"]}` in `data/drakenrijken/skills/`; naam in `lang` als `skill.drakenrijken.<id>`; klassenaam als `class.drakenrijken.<naam>`.
- Startset-JSON: `{"class":"schurk","items":{"stone_sword":1,...}}` in `data/drakenrijken/kits/`; één per klasse.
- Koninkrijken: `KingdomPlacement.hasKingdom(seed, chunkX, chunkZ)` (dichtheid 1%) en `companion(...)` (burcht/dungeon/drakengebied/""). Bouwen in `data/WorldGen.build(...)` met plannen uit `core/Blueprints` en `core/CityPlans` (`Blueprint`: `set`, `fill`, `room`, `spot`, `count`).
- Voorwerpen worden geregistreerd in `data/ModTools.java`; blokken in `data/ModBlocks.java`.

---

### Task 1 (DR-1201): Koninkrijkstype en thema in data

**Files:**
- Create: `src/main/java/com/drakenrijken/core/KingdomTheme.java`
- Modify: `src/main/java/com/drakenrijken/data/WorldGen.java:46-70`
- Test: `src/test/java/com/drakenrijken/core/KingdomThemeTest.java`

**Interfaces:**
- Consumes: `KingdomPlacement.hasKingdom(long,int,int)`, `KingdomPlacement.companion(long,int,int)`, `CityPlans.isCity(long,int,int)`.
- Produces: `enum KingdomTheme { WESTERS, JAPANS }` en `static KingdomTheme KingdomTheme.of(long seed, int chunkX, int chunkZ)`. Een koninkrijk is Japans bij ongeveer 1 op 4; deterministisch; ongeacht companion of stad.

- [ ] **Step 1: Test schrijven** `KingdomThemeTest`: `zelfdeSeedGeeftZelfdeThema` (twee aanroepen gelijk); `ongeveerEenOpVierIsJapans` (op 4000 koninkrijk-chunks tussen 15% en 35% JAPANS); `japansKoninkrijkNooitGroteStad` (voor elke chunk met `of==JAPANS` en `hasKingdom`: gedrag van `WorldGen` is dat de Japanse bouw voorrang heeft — pin dit met een `core`-functie `KingdomTheme.usesCity(long,int,int)` die `false` geeft voor JAPANS); `bestaandeKoninkrijkenVerschuivenNiet` (voor 10.000 chunks met seed 12345: `hasKingdom` telt tussen 50 en 200, gelijk aan de waarde vóór deze wijziging, vastgelegd als constante in de test).
- [ ] **Step 2: Test draaien, moet falen** — `./gradlew test --tests "*KingdomThemeTest"`; verwacht: compilatiefout, `KingdomTheme` bestaat niet.
- [ ] **Step 3: Implementeren** `KingdomTheme.of` met dezelfde `mix`-stijl als `KingdomPlacement` maar met een eigen zout, zodat `hasKingdom` en `companion` niet veranderen; `usesCity` geeft `false` bij JAPANS, anders `CityPlans.isCity`.
- [ ] **Step 4: Test draaien, moet slagen** — zelfde commando; verwacht: PASS.
- [ ] **Step 5: `WorldGen.build` aanpassen** zodat het thema wordt opgevraagd en doorgegeven; tot Task 3 bouwt een Japans koninkrijk nog het bestaande `Blueprints.village()`. Controleer met `./gradlew test` dat alles groen blijft.
- [ ] **Step 6: Commit** — `git commit -m "feat: koninkrijkstype Japans (DR-1201)"`

---

### Task 2 (DR-1202): Klassen samoerai en onmyoji

**Files:**
- Modify: `src/main/java/com/drakenrijken/core/PlayerClass.java`, `core/ResourceBar.java:33`, `core/HudLines.java:14`
- Create: `data/drakenrijken/kits/samoerai.json`, `kits/onmyoji.json`; 12 bestanden in `data/drakenrijken/skills/` (zie lijst)
- Modify: `assets/drakenrijken/lang/nl_nl.json`, `lang/en_us.json`, `guide.drakenrijken.klassen`
- Test: `src/test/java/com/drakenrijken/core/PlayerClassTest.java` (nieuw of bestaand uitbreiden), `data/BundledSkillsTest` (bestaat al, moet blijven slagen), `data/StartKitLoaderTest`

**Interfaces:**
- Produces: `PlayerClass` heeft extra waarden `SAMOERAI, ONMYOJI`; `ResourceBar.forClass(ONMYOJI)` geeft max 100 (mana), `SAMOERAI` max 80 (uithouding); `HudLines` toont "Mana" ook voor ONMYOJI.
- Vaardigheden (id, vereist) — samoerai: `katana_meester`, `eerbetoon` (requires katana_meester), `kendo_houding`, `naginata_zwaai` (requires kendo_houding), `bushido` (requires eerbetoon), `laatste_stand` (requires bushido). Onmyoji: `talisman`, `vuurtalisman` (requires talisman), `schutgeest`, `ijsgeest` (requires schutgeest), `zegel` (requires talisman), `shikigami` (requires vuurtalisman). Definitieve namen en effecten bepaalt de bouwer met de Game Designer.

- [ ] **Step 1: Tests schrijven**: `PlayerClassTest.parseKentNieuweKlassen` (`parse("samoerai")` en `parse("ONMYOJI")` geven de enum); `ResourceBarTest.onmyojiHeeftMana` (`forClass(ONMYOJI).max()==100`, `forClass(SAMOERAI).max()==80`); `HudLinesTest` (onmyoji toont "Mana"); `BundledSkillsTest` blijft eisen 6 per klasse (geen wijziging nodig, moet nu eerst falen); `StartKitLoaderTest.bundeldeKitsDekkenAlleKlassen` (elke `PlayerClass` heeft een kit in de gebundelde map; kapotte JSON geeft waarschuwing, geen crash).
- [ ] **Step 2: Draaien, moet falen** — `./gradlew test --tests "*PlayerClass*" --tests "*Bundled*" --tests "*StartKit*"`; verwacht: FAIL (enum en bestanden ontbreken).
- [ ] **Step 3: Implementeren**: enum uitbreiden; `ResourceBar.forClass` en `HudLines` laten ook ONMYOJI als mana-klasse gelden (`c == MAGIER || c == ONMYOJI`); 12 skill-JSONs en 2 kit-JSONs (samoerai: `iron_sword` als tijdelijke katana, `bow`, `arrow`, `bread`; onmyoji: `paper` x16, `bread`, tijdelijke staf); `class.drakenrijken.samoerai`, `class.drakenrijken.onmyoji` en 12 `skill.drakenrijken.*` in beide lang-bestanden; handleidingtekst uitbreiden.
- [ ] **Step 4: Draaien, moet slagen** — `./gradlew test`; verwacht: alle tests PASS, ook `LangFilesTest`, `CoreIsolationTest`, `PlayerDataCodecTest` (oude klassen laden ongewijzigd).
- [ ] **Step 5: Klassekeuzescherm en skill tree controleren** in het spel (`./gradlew runClient`): kies elke nieuwe klasse, open het skill tree-scherm, geen crash zonder keuze.
- [ ] **Step 6: Commit** — `git commit -m "feat: klassen samoerai en onmyoji (DR-1202)"`

---

### Task 3 (DR-1203): Japans dorp en kasteel

**Files:**
- Create: `src/main/java/com/drakenrijken/core/JapanesePlans.java`
- Modify: `core/Blueprints.java` (alleen de doorverwijzing), `data/WorldGen.java:46-70`
- Test: `src/test/java/com/drakenrijken/core/JapanesePlansTest.java`

**Interfaces:**
- Consumes: `Blueprint` (`set`, `fill`, `room`, `spot`, `count`), `KingdomTheme` uit Task 1.
- Produces: `static Blueprint JapanesePlans.village()` (huizen, markt, dojo, torii-poort, pagode; rollen `smid`, `marktkoopman`, `dorpeling`, `sensei`) en `static Blueprint JapanesePlans.castle()` (kasteel op stenen voet met troonzaal en kerker); `static final int VILLAGE_SIZE`. Alleen vanilla-blokken (bijv. `spruce_planks`, `dark_oak_planks`, `stone_brick_stairs`, `cherry_planks`, `red_concrete`, `paper`-achtig via `white_stained_glass_pane`); geen nieuwe teksturen nodig.

- [ ] **Step 1: Tests schrijven** `JapanesePlansTest`: `dorpHeeftNPCPlekkenMetAlleRollen` (rollen `smid`, `marktkoopman`, `dorpeling`, `sensei` aanwezig); `dorpHeeftBedden` (`count("white_bed") >= 6`); `toriiAanwezig` (bevat `red_concrete` of gekozen toriiblok minstens 6 keer); `kasteelHeeftTroonzaalEnKerker` (zelfde soort controles als `CastleBlueprintTest`); `planBlijftBinnenAfmeting` (alle `placements` binnen `0..VILLAGE_SIZE-1`).
- [ ] **Step 2: Draaien, moet falen** — `./gradlew test --tests "*JapanesePlansTest"`; verwacht: compilatiefout.
- [ ] **Step 3: Implementeren** `JapanesePlans.village()` en `castle()` met `Blueprint`-hulpmethoden; houd het aantal placements in de buurt van `Blueprints.village()` (prestaties).
- [ ] **Step 4: `WorldGen.build` aansluiten**: bij `KingdomTheme.JAPANS` het Japanse dorp (en bij companion `burcht` het Japanse kasteel) via `StructureBuilder.place` en de bestaande `StructureQueue`; companion `dungeon`/`drakengebied` blijft westers. Draaien: `./gradlew test`; verwacht PASS.
- [ ] **Step 5: In het spel bekijken** (`./gradlew runClient`, `/dr` debugcommando om te verplaatsen naar een Japans koninkrijk); noteer in de backlog "niet in het spel bekeken" alleen als dit niet gelukt is.
- [ ] **Step 6: Commit** — `git commit -m "feat: Japans dorp en kasteel (DR-1203)"`

---

### Task 4 (DR-1204): Japans gebied

**Files:**
- Create: `core/JapaneseLand.java`
- Modify: `data/WorldGen.java`
- Test: `src/test/java/com/drakenrijken/core/JapaneseLandTest.java`

**Interfaces:**
- Produces: `static Blueprint JapaneseLand.garden()` met kersenbomen, bamboe, een vijver en een kleine berg (`SIZE` constante, zelfde stijl als `Blueprints.lair()`).

- [ ] **Step 1: Tests schrijven**: `tuinHeeftKersenhoutEnBamboe` (`count("cherry_log")>=4`, `count("bamboo")>=10`); `tuinBlijftBinnenAfmeting`; `aantalBlokkenBinnenBudget` (placements ≤ 20.000, zodat `StructureQueue` het gespreid kan bouwen).
- [ ] **Step 2: Draaien, moet falen** — `./gradlew test --tests "*JapaneseLandTest"`.
- [ ] **Step 3: Implementeren** `JapaneseLand.garden()`.
- [ ] **Step 4: `WorldGen`** plaatst het gebied naast het Japanse dorp via `StructureQueue` (één structuur per activatie, DR-306); `./gradlew test` slaagt.
- [ ] **Step 5: Commit** — `git commit -m "feat: Japans gebied (DR-1204)"`

---

### Task 5 (DR-1205): Wapens

**Files:**
- Create: `core/JapaneseWeapons.java` (waarden), `data/ModJapaneseItems.java` (registratie, of uitbreiding van `ModTools`), 4 tekstuur-PNG's 16x16, 4 item-models/definities en lang-regels
- Test: `src/test/java/com/drakenrijken/core/JapaneseWeaponsTest.java`

**Interfaces:**
- Produces: `record JapaneseWeapons.Stats(String id, double damage, double reach, int durability)` en `static List<Stats> JapaneseWeapons.all()` met `katana`, `naginata`, `yumi` (boog) en `shuriken`; getallen zet de bouwer met de Game Designer, voorlopig: katana schade 7, naginata schade 8 en bereik 3, shuriken schade 3 (stapelt tot 16). Wapens zijn herkenbaar als `PlayerClass.SAMOERAI`-wapens in een `static boolean usableBy(String id, PlayerClass c)`: katana en naginata voor SAMOERAI, shuriken en yumi voor iedereen.

- [ ] **Step 1: Tests schrijven**: `alleVierWapensBestaan`; `waardenZijnPositief` (schade, bereik, duurzaamheid > 0); `katanaIsSterkerDanIjzerzwaard` (schade > 6); `shurikenIsVoorIedereen` (`usableBy("shuriken", c)` voor elke klasse).
- [ ] **Step 2: Draaien, moet falen** — `./gradlew test --tests "*JapaneseWeaponsTest"`.
- [ ] **Step 3: Implementeren** `JapaneseWeapons`; registreer de vier items zoals `ModTools.init()` doet (`Registry.register(BuiltInRegistries.ITEM, key, ...)`), met `src/main/resources/assets/drakenrijken/{items,models,textures}`-bestanden; controleer de item-API voor Minecraft 26.3 in de officiële documentatie. Zet de wapens in de startsets van samoerai (`katana`) en vervang de tijdelijke items uit Task 2.
- [ ] **Step 4: Draaien, moet slagen** — `./gradlew test` en `./gradlew runClient` (item verschijnt, tekstuur laadt); `LangFilesTest` blijft groen.
- [ ] **Step 5: Commit** — `git commit -m "feat: Japanse wapens (DR-1205)"`

---

### Task 6 (DR-1206): Eten

**Files:**
- Create: `core/JapaneseFood.java`, registratie in `data/ModJapaneseItems.java`, 4 teksturen, models, lang-regels, receptbestanden in `data/drakenrijken/recipe/`
- Test: `src/test/java/com/drakenrijken/core/JapaneseFoodTest.java`

**Interfaces:**
- Produces: `record JapaneseFood.Stats(String id, int hunger, double saturation, String bonus)`; `static List<Stats> JapaneseFood.all()` met `rijst`, `onigiri`, `sushi`, `thee`; bonus bijv. `onigiri`: `uithouding_herstel`, `thee`: `mana_herstel`, `sushi`: `snelheid`; getallen bepaalt de bouwer met de Economy Designer.

- [ ] **Step 1: Tests schrijven**: `viergerechtenBestaan`; `voedingswaardenZijnPositief`; `theeGeeftManaHerstel`; `receptenBestaanVoorElkGerecht` (voor elk gerecht een recept-JSON in de gebundelde map; zie `BundledRecipesTest`).
- [ ] **Step 2: Draaien, moet falen** — `./gradlew test --tests "*JapaneseFoodTest" --tests "*BundledRecipesTest"`.
- [ ] **Step 3: Implementeren** de waarden, items (met voedingseigenschappen volgens de officiële API van 26.3), recepten en teksturen.
- [ ] **Step 4: Draaien, moet slagen** — `./gradlew test`.
- [ ] **Step 5: Commit** — `git commit -m "feat: Japans eten (DR-1206)"`

---

### Task 7 (DR-1207): Bewoners, quests en buit

**Files:**
- Create: `data/drakenrijken/loot/japans_dorp.json`, `loot/japans_kasteel.json`, `spawns/japans.json`, `quests/japan_*.json` (minstens 1), `dialogues/sensei.json`
- Test: bestaande `BundledLootTest`, `QuestLoaderTest`, `BundledDialoguesTest` blijven slagen; nieuw `data/JapaneseContentTest.java`

- [ ] **Step 1: Test schrijven** `JapaneseContentTest`: `japansDorpHeeftBuittabelMetEten` (`japans_dorp.json` bevat `drakenrijken:onigiri`); `japansKasteelHeeftKatana`; `erIsMinstensEenJapanseQuest` (id begint met `japan_`, `requires` verwijst naar een bestaande quest); `spawnlijstJapansBestaat`; `senseiHeeftDialoog`.
- [ ] **Step 2: Draaien, moet falen** — `./gradlew test --tests "*JapaneseContentTest"`.
- [ ] **Step 3: JSON schrijven** in dezelfde vorm als `loot/dorp.json`, `spawns/bos.json` en `quests/akte1_dorp_stenen.json`; één quest bijvoorbeeld: 10 bamboe verzamelen voor de sensei, beloning xp en een onigiri.
- [ ] **Step 4: Draaien, moet slagen** — `./gradlew test`.
- [ ] **Step 5: Commit** — `git commit -m "feat: bewoners, quests en buit Japans koninkrijk (DR-1207)"`

---

### Task 8 (DR-1208): Meting en afronding

**Files:**
- Modify: `docs/backlog.md` (Epic 12 toevoegen), `CHANGELOG.md`, `README.md` indien nodig, handleiding (`handbook/`), wiki

- [ ] **Step 1: Prestatiemeting** met de Performance Benchmarker-aanpak uit eerdere epics: FPS en RAM in een Japans koninkrijk op lage instelling; eis 40+ FPS en onder ca. 3 GB; noteer de uitkomst in `docs/backlog.md`.
- [ ] **Step 2: Volledige testronde** — `./gradlew test`; verwacht alles PASS.
- [ ] **Step 3: Handleiding, backlog en changelog bijwerken** (Epic 12 met DR-1201 t/m DR-1208 en hun status).
- [ ] **Step 4: Wiki bijwerken** (Uitleg, Ideeen, Backlog, Wijzigingen) via `McModpack.wiki.git`.
- [ ] **Step 5: Commit** — `git commit -m "docs: Epic 12 afgerond, meting en wiki (DR-1208)"`

---

## Self-review

- **Spec-dekking:** koninkrijk en gebouwen (Task 1, 3), gebied (4), klassen (2), wapens (5), eten (6), bewoners/quests/buit tegen "leeg koninkrijk" (7), meting en documentatie (8). Niet-doelen (geen ninja, geen biome) zijn nergens opgenomen.
- **Afwijking van het ontwerp:** klassen krijgen 6 vaardigheden in plaats van 3, omdat `BundledSkillsTest` dat eist; backlog noemde 3 als start.
- **Typeconsistentie:** `KingdomTheme.of/usesCity` (Task 1) wordt in Task 3 en 4 gebruikt; `JapanesePlans.village()/castle()` en `JapaneseLand.garden()` hebben dezelfde namen overal; `PlayerClass.SAMOERAI/ONMYOJI` overal hoofdletters (JSON `"class":"SAMOERAI"`, kits via `parse`, hoofdletterongevoelig).
- **Open voor de bouwer:** getallen van wapens en eten, effecten van de 12 vaardigheden, exacte bouwplannen. Zijn nog niet vastgelegd en komen in JSON of constanten.
