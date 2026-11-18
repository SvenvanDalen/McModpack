# Draken en Rijken: Backlog

Bron: `docs/superpowers/specs/2026-11-16-draken-en-rijken-design.md` en `docs/superpowers/plans/2026-11-16-draken-en-rijken.md`.
Status per story: ☐ open. Zet een ✔ als het klaar is.

**Legenda**
- Prioriteit: **M** = Must (zonder dit geen speelbaar spel), **S** = Should, **C** = Could.
- Grootte: **S** ≈ een uur of twee, **M** ≈ een halve dag, **L** ≈ een dag of meer, **XL** = splitsen voor je begint.
- Afh. = afhankelijk van die story.
- Alle waarden zoals getallen en grenzen zijn voorstellen uit het plan en mogen worden bijgesteld.

**Definition of Done (geldt voor elke story)**
1. Code heeft tests (JUnit voor pure logica, GameTest voor gedrag in het spel) en die slagen.
2. Geen nieuwe crash of foutmelding in het logboek bij normaal spelen.
3. Prestatiemeting niet slechter dan de doelen: 40+ FPS op lage instelling, onder ca. 3 GB RAM.
4. Eigen teksturen zijn 16x16, modellen laag in polygonen.
5. Commit met duidelijke boodschap.

---

## Epic 0: Fundament

| ID | Story | Acceptatie | Gr. | Afh. | Pr. |
|---|---|---|---|---|---|
| DR-001 | Versies vaststellen | Nieuwste stabiele Minecraft-versie, Fabric Loader, Fabric API, Loom en Java-versie genoteerd met datum, uit officiële Fabric-bron | S | – | M |
| DR-002 | Git en projectskelet | Repo met `.gitignore`; `./gradlew build` slaagt; mod verschijnt in modlijst | M | DR-001 | M |
| DR-003 | Testopzet | JUnit 5 en Fabric GameTest draaien met één groene rooktest elk | M | DR-002 | M |
| DR-004 | Pure-Java-kern scheiden | Map `core/` heeft geen Minecraft-imports; een test bewaakt dat | S | DR-002 | M |
| DR-005 | Meetprocedure en nulmeting | `tools/perf/measure.md` + uitgangswaarden (FPS en RAM) op de doellaptop | M | DR-002 | M |
| DR-006 | Foutlog en debugcommando's | `/dr debug` toont klasse, level, quests en rijk-niveau | S | DR-002 | S |
| DR-007 | Configbestand | `drakenrijken.json` met schakelaars (deeltjes aan/uit, machinegrens) | S | DR-002 | S |
| DR-008 | CI-script lokaal | Eén commando draait build, tests en GameTests | S | DR-003 | C |

## Epic 1: Klassen, levels en vaardigheden

| ID | Story | Acceptatie | Gr. | Afh. | Pr. |
|---|---|---|---|---|---|
| DR-101 | `PlayerClass` en `Progression` | `xpForLevel(n)=100*n`; max level 30; test op 250 xp op level 2 geeft level 3 | M | DR-004 | M |
| DR-102 | Opslag van spelerdata | `PlayerData.CODEC` roundtrip gelijk; `empty()` heeft geen klasse | M | DR-101 | M |
| DR-103 | Klassekeuzescherm | Nieuwe speler ziet keuze ridder, magiër of schurk; geen crash zonder keuze | M | DR-102 | M |
| DR-104 | XP verdienen | Mobs doden, quests voltooien en bouwen geven xp; xp loopt niet over bij level 30 | M | DR-101 | M |
| DR-105 | Skill tree-logica | `unlock` geeft `NO_POINTS` of `LOCKED` correct; 1 punt per level | M | DR-101 | M |
| DR-106 | Skill tree-scherm | Boom per klasse; punten besteden werkt; tooltip per vaardigheid | L | DR-105 | M |
| DR-107 | Vaardigheden ridder (3 start) | Bijvoorbeeld schildstoot, zwaardbeheersing, taaiheid, uit JSON | M | DR-105 | M |
| DR-108 | Vaardigheden magiër (3 start) | Bijvoorbeeld vuurbal, mana-regen, barrière | M | DR-105 | M |
| DR-109 | Vaardigheden schurk (3 start) | Bijvoorbeeld sluipaanval, snelheid, dubbele buit | M | DR-105 | M |
| DR-110 | Mana en uithouding | Magiër heeft een mana-balk; ridder en schurk een uithoudingsbalk; HUD toont ze | L | DR-108 | S |
| DR-111 | Klasse-uitrusting | Elke klasse krijgt een eigen startset | S | DR-103 | S |
| DR-112 | Respec | Vaardigheden herverdelen tegen een prijs | M | DR-106 | C |
| DR-113 | Uitbreiding tot 6 vaardigheden per klasse | Extra takken in JSON | L | DR-107, DR-108, DR-109 | C |

## Epic 2: Quests en verhaal

| ID | Story | Acceptatie | Gr. | Afh. | Pr. |
|---|---|---|---|---|---|
| DR-201 | Questmotor | Voltooiing na N events; ketens afgedwongen in volgorde | L | DR-102 | M |
| DR-202 | Questlader | Ongeldig JSON-bestand wordt overgeslagen met een logregel; de rest laadt | M | DR-201 | M |
| DR-203 | Quest-events koppelen | Blok breken, mob doden, item oprapen en locatie bereiken sturen events | M | DR-201 | M |
| DR-204 | Questlog-scherm | Lijst met actieve en voltooide quests plus doel | M | DR-201 | M |
| DR-205 | HUD-tracker | Actieve quest bovenin beeld, aan en uit te zetten | S | DR-204 | S |
| DR-206 | Dialoogsysteem | NPC-gesprekken met keuzes, data-gestuurd | L | DR-201 | M |
| DR-207 | Verhaalschets en namen | Document met rijk, personages, plekken en de eindbaas | M | – | M |
| DR-208 | Akte 1: 5 quests | Dorp, eerste smederij, eerste dungeon | L | DR-206, DR-207 | M |
| DR-209 | Akte 2: 8 quests | Naar het draken-gebied en de eindstrijd | L | DR-208 | M |
| DR-210 | Akte 3: epiloog | Rijk-keuze en afsluiting | M | DR-209 | S |
| DR-211 | Zijquests | 6 kleine quests per tijdperk | L | DR-208 | C |
| DR-212 | Quest-beloningen | xp, items en vaardigheidspunten uit JSON | S | DR-201 | M |

## Epic 3: Wereld

| ID | Story | Acceptatie | Gr. | Afh. | Pr. |
|---|---|---|---|---|---|
| DR-301 | `KingdomPlacement` | Deterministisch per seed en chunk; dichtheid tussen 0,5% en 2% bij 10.000 chunks | M | DR-004 | M |
| DR-302 | Dorpsstructuur | Huizen, markt en smid met NPC-plekken | L | DR-301 | M | (deels: bouwplan met huizen, markt en smederij; niet in het spel bekeken)
| DR-303 | Burchtstructuur | Zetel van het koninkrijk met kerker | L | DR-301 | S | (deels: bouwplan met troonzaal, torens en kerker; niet in het spel bekeken)
| DR-304 | Dungeonstructuur | 3 kamers, vallen, schatkist | L | DR-301 | M | (deels: bouwplan met 3 kamers, val en schatkist; niet in het spel bekeken)
| DR-305 | Draken-gebied | Eigen biome of gebied met rotsen en een grot | L | DR-301 | M | (deels: bouwplan met rotsgrot en nest; geen eigen biome)
| DR-306 | Geleidelijke wereldgeneratie | Eén structuur per chunk-activatie; geen tick-piek boven de grens | M | DR-302 | M |
| DR-307 | Kaart en kompas | Kaart toont ontdekte koninkrijken | M | DR-301 | S |
| DR-308 | Vijanden per gebied | Spawnlijsten per structuur en gebied | M | DR-304 | M |
| DR-309 | Buittabellen | Schatkisten volgen de tijdperken | M | DR-304 | S |
| DR-310 | Snelreis | Betaalde reis tussen ontdekte dorpen | M | DR-307 | C | (deels: /dr reis, smaragden als betaling, geen scherm)

## Epic 4: Industrie

| ID | Story | Acceptatie | Gr. | Afh. | Pr. |
|---|---|---|---|---|---|
| DR-401 | `MachineBudget` | Grens van 64 actieve machines; de 65e activatie geeft `false`; na `release` lukt een nieuwe | M | DR-004 | M |
| DR-402 | Tikken alleen in geladen chunks | Machines buiten geladen chunks tikken niet; GameTest | M | DR-401 | M |
| DR-403 | Waterrad | Levert kracht naast stromend water | L | DR-401 | M |
| DR-404 | Smederij | Verwerkt erts naar staven met recepten uit JSON | L | DR-403 | M |
| DR-405 | Krachtoverdracht tijdperk 1 | Assen en tandwielen met eenvoudige regels | L | DR-403 | M |
| DR-406 | Stoomketel | Tijdperk 2: brandstof naar stoom naar kracht | L | DR-405 | M |
| DR-407 | Stoommachines | Pers, zaag en pomp | L | DR-406 | S |
| DR-408 | Runesteen en rune-machines | Tijdperk 3: energie uit runen, eigen machines | XL | DR-406 | M |
| DR-409 | Transportbanden of leidingen | Eén eenvoudig transportsysteem | L | DR-405 | S |
| DR-410 | Machine-UI | Eén gedeeld scherm met voortgang en status | M | DR-404 | S |
| DR-411 | Receptendata | Alle machinerecepten als JSON | M | DR-404 | M |
| DR-412 | Tijdperk-poorten | Tijdperk 2 en 3 ontgrendelen via quests | S | DR-209 | M |

## Epic 5: Rijksysteem

| ID | Story | Acceptatie | Gr. | Afh. | Pr. |
|---|---|---|---|---|---|
| DR-501 | `RealmState` | `levelFor(macht)` van 0 tot 5; macht 0 geeft 0; monotoon; maximum bij zeer hoge macht | M | DR-401 | M |
| DR-502 | Machtsberekening | Totale machine-macht optellen binnen de machinegrens | M | DR-501 | M |
| DR-503 | Effect: dorpelingen | Meer dorpelingen bij hoger niveau | M | DR-501 | M |
| DR-504 | Effect: handel | Betere handelaren en prijzen | M | DR-501 | S |
| DR-505 | Effect: aanvallen | Vijandelijke golven op hogere niveaus | L | DR-501 | S |
| DR-506 | Rijk-scherm | Toont niveau, macht en volgende drempel | M | DR-501 | S |
| DR-507 | Rijk bij herstart | Niveau blijft behouden na opslaan en laden | S | DR-501 | M |

## Epic 6: Draak en eindstrijd

| ID | Story | Acceptatie | Gr. | Afh. | Pr. |
|---|---|---|---|---|---|
| DR-601 | `BossPhases` | `phaseFor(0.7)=1`, `phaseFor(0.5)=2`, `phaseFor(0.2)=3` | S | DR-004 | M |
| DR-602 | Draakmodel | Laag-polygoon model in Blockbench met animaties voor lopen, vliegen en aanval | L | – | M |
| DR-603 | `DragonEntity` | Beweegt, valt aan, heeft gezondheidsbalk | L | DR-601, DR-602 | M |
| DR-604 | Fase 1: grondaanval | Klauw en staart | M | DR-603 | M | (deels: staartzwiep op een Ravager als stand-in; klauw volgt met DR-602)
| DR-605 | Fase 2: vuur | Vuuradem met maximaal 8 eigen deeltjes per aanval | M | DR-603 | M | (deels: vuuradem met hoogstens 8 deeltjes)
| DR-606 | Fase 3: lucht en woede | Duikaanval en snellere cyclus | M | DR-603 | M | (deels: snellere cyclus; duikaanval wacht op vlucht en model, DR-602)
| DR-607 | Beloning eenmalig | `rewardOnce` geeft maar één keer beloning per speler | S | DR-601 | M |
| DR-608 | Reset bij dood of vertrek | Speler sterft of verlaat de wereld: draak herstelt naar fase 1, geen beloning; GameTest | M | DR-603 | M |
| DR-609 | Strijdmuziek | Eigen muziekloop tijdens de strijd | M | DR-603 | C |
| DR-610 | Tweede eindbaas | Optionele baas voor tijdperk 3 | XL | DR-408 | C |

## Epic 7: Interface en gevoel

| ID | Story | Acceptatie | Gr. | Afh. | Pr. |
|---|---|---|---|---|---|
| DR-701 | HUD-ontwerp | Eén eenvoudige opzet voor balken, quest en rijk-niveau | M | DR-110 | S |
| DR-702 | Hoofdmenu-aanpassing | Eigen titelscherm en logo | M | – | C |
| DR-703 | Geluiden | Eigen geluiden voor machines, vaardigheden en de draak | L | – | S | (deels: vanilla geluiden als tijdelijke vervanger bij vaardigheden en drakenvuur; eigen geluiden ontbreken) |
| DR-704 | Muziek | Drie sfeerloops voor dorp, dungeon en natuur | L | – | C |
| DR-705 | Teksturen | Eigen 16x16 voor blokken, items en NPC's | XL | – | M |
| DR-706 | Handleiding in het spel | Boekje met uitleg over klassen, industrie en rijk | M | DR-209 | S |
| DR-707 | Toegankelijkheid | Tekstgrootte en contrastopties in de eigen schermen | S | DR-701 | C | (deels: highContrast en textScale (alleen de kop) klaar; knoppen blijven vanilla)
| DR-708 | Nederlandse en Engelse teksten | `nl_nl.json` en `en_us.json` | M | – | S |

## Epic 8: Prestaties

| ID | Story | Acceptatie | Gr. | Afh. | Pr. |
|---|---|---|---|---|---|
| DR-801 | Meetronde per epic | Na elke epic een meting op de doellaptop en de tabel bijwerken | S | DR-005 | M |
| DR-802 | Tick-budgetten | Maximumtijd per tick voor machines en rijk, afgedwongen | M | DR-401 | M | (klaar: TickBudget en MachineBudget bestaan en zijn getest, maar zijn nog niet aangesloten: machines werken nu alleen bij een klik, er is geen tick-werk om te begrenzen. Aansluiten kan pas als machines doorlopend draaien) |
| DR-803 | Entiteitsgrenzen | Maximum aantal eigen NPC's en vijanden in beeld | M | DR-503 | M |
| DR-804 | Deeltjesgrens | Eigen deeltjes begrensd en uit te zetten via config | S | DR-007 | M |
| DR-805 | Geheugenprofiel | Heap-meting tijdens een speelsessie van 30 minuten onder het doel | M | DR-801 | M |
| DR-806 | Aanbevolen instellingen | Document met aanbevolen videoinstellingen en optionele prestatiemods | S | DR-801 | S |
| DR-807 | Laadtijd | Spelstart en wereldlaad binnen een vastgesteld doel | M | DR-801 | C |

## Epic 9: Kwaliteit

| ID | Story | Acceptatie | Gr. | Afh. | Pr. |
|---|---|---|---|---|---|
| DR-901 | Opslaan en laden volledig | Klasse, level, quests en rijk komen na herstart exact terug | M | DR-102, DR-201, DR-507 | M |
| DR-902 | Kapotte data | Ontbrekend of beschadigd JSON laat het spel starten en logt een waarschuwing | M | DR-202 | M |
| DR-903 | Speelronde 1 (akte 1) | Volledig uitgespeeld; fouten genoteerd en opgelost | M | DR-208 | M |
| DR-904 | Speelronde 2 (tot de draak) | Volledig uitgespeeld; fouten genoteerd en opgelost | L | DR-209 | M |
| DR-905 | Speelronde 3 (alles) | Van begin tot einde zonder crash | L | DR-210 | M |
| DR-906 | Compatibiliteit | Spel start met en zonder Sodium en Lithium | S | DR-806 | S |
| DR-907 | Regressietestset | Alle eerder gevonden fouten hebben een test | M | DR-903 | S |

## Epic 10: Release

| ID | Story | Acceptatie | Gr. | Afh. | Pr. |
|---|---|---|---|---|---|
| DR-1001 | README | Installatie (Java-versie, Fabric, Fabric API, optioneel Sodium en Lithium) | S | DR-905 | M |
| DR-1002 | Release-jar | `./gradlew build` levert een werkende jar | S | DR-905 | M |
| DR-1003 | Versienummering en changelog | Semver en een changelog | S | DR-1002 | S |
| DR-1004 | Modrinth- of CurseForge-pagina | Beschrijving, screenshots, tags | M | DR-1002 | C |
| DR-1005 | Update naar een nieuwere Minecraft-versie | Procedure en checklist voor een versie-update | M | DR-1002 | C |

## Epic 11: Crowns & Corruption, zelfbouw-editie (issue #1)

Bron: GitHub-issue #1. Elke story heeft een eigen issue en een eigen PR. Bouwt voort op RealmPower, RealmState, RealmRaids en de bouwplantafel.

| ID | Story | Acceptatie | Gr. | Afh. | Pr. | Issue |
|---|---|---|---|---|---|---|
| DR-1101 | Koninkrijken groeien en vervallen | Rijksgrootte per koninkrijk groeit of krimpt deterministisch met de tijd | M | DR-301 | M | #5 | (deels: spelregels en tests in core, nog niet in de wereld)
| DR-1102 | Koning erkent je na een bouwplan | Erkenning pas na troonzaal, haven of kazerne | M | DR-1101 | M | #6 | (deels: spelregels en tests in core, nog niet in de wereld)
| DR-1103 | Macht en aanzien door je bouwwerk | Groter bouwwerk geeft meer macht, mensen en vijanden | M | DR-501 | M | #7 | (deels: spelregels en tests in core, nog niet in de wereld)
| DR-1104 | Leger | Soldaten aanwerven, uitrusten en voeden; zichtbare soldaten binnen EntityBudget | L | DR-1103 | M | #8 | (deels: spelregels en tests in core, nog niet in de wereld)
| DR-1105 | Belegeringswapens zelf bouwen | Ontwerp bepaalt bereik, schade en gewicht | L | DR-1104 | S | #9 | (deels: spelregels en tests in core, nog niet in de wereld)
| DR-1106 | Schip zelf bouwen | Kiel en ontwerp bepalen drijfkracht, snelheid en plekken | L | – | M | #10 | (deels: spelregels en tests in core, nog niet in de wereld)
| DR-1107 | Scheepsbemanning en kanonnen | Zonder bemanning vaart het schip niet | M | DR-1106 | M | #11 | (deels: spelregels en tests in core, nog niet in de wereld)
| DR-1108 | Koning omkopen of afzetten | Daarna zelf zijn plek innemen via een rijk | M | DR-1102 | S | #12 | (deels: spelregels en tests in core, nog niet in de wereld)
| DR-1109 | Gevangenen laten werken | Werk in mijn, op het land en op schepen | M | DR-1104 | S | #13 | (deels: spelregels en tests in core, nog niet in de wereld)
| DR-1110 | Opstanden | Slechte behandeling leidt tot opstand | M | DR-1109 | S | #14 | (deels: spelregels en tests in core, nog niet in de wereld)
| DR-1111 | Optioneel: lege kavel | Start zonder gegenereerde structuren; eerst bespreken | L | – | C | #15 | (deels: alleen de instelling, geen eigen startpakket of quests voor de lege kavel; later uit te breiden)

---

## Voorgestelde volgorde (mijlpalen)

1. **M0: start.** Epic 0 compleet. Mod start en meet.
2. **M1: speler.** Epic 1 (DR-101 tot DR-109) en HUD basis. Je kunt een klasse kiezen en levelen.
3. **M2: verhaal.** Epic 2 tot en met akte 1 (DR-201 tot DR-208). Eerste speelronde.
4. **M3: wereld.** Epic 3 (DR-301 tot DR-306, DR-308).
5. **M4: eerste industrie.** DR-401 tot DR-405, DR-411.
6. **M5: draak.** Epic 6 en akte 2.
7. **M6: tijdperken 2 en 3 en rijk.** Epic 4 rest en Epic 5.
8. **M7: afwerken.** Epic 7 tot en met 10.

Na elke mijlpaal draait meetstory DR-801 en kan het spel gespeeld worden. Dat voorkomt een project dat pas aan het einde werkt.

## Bekende risico's

| Risico | Effect | Aanpak |
|---|---|---|
| Nieuwste Minecraft-versie verandert API's of mappings | Code moet herschreven | DR-001 eerst; kern in pure Java (DR-004); kleine Minecraft-laag |
| Bronnen over versies waren tegenstrijdig | Verkeerde versie gekozen | Alleen officiële Fabric-pagina's gebruiken |
| Omvang van variant C | Project komt niet af | Mijlpalen met speelbare tussenstand; "Could" mag vervallen |
| Prestaties op een matige laptop | Spel niet speelbaar | Budgetten (DR-802 tot DR-804) en meting na elke epic |
| Eigen teksturen en geluiden kosten veel tijd | Planning schuift | DR-705 laag houden (16x16), start met placeholders |
| Machine-systeem wordt te complex | Bugs en lag | Eén transportsysteem (DR-409) en harde machinegrens |

## Samenvatting

- **Epics:** 11 (0 tot 10).
- **Stories:** 99, waarvan 63 Must, 23 Should en 13 Could (geteld op 2026-11-16).
- **XL-stories** (DR-408, DR-610, DR-705): eerst opsplitsen voordat je eraan begint.

## Open en geblokkeerd (stand 2026-11-17)

Wat nog open staat kan niet zonder het echte spel, eigen bestanden of een besluit van de eigenaar.

| Story | Wat ontbreekt |
|---|---|
| DR-005, DR-801, DR-805, DR-807 | Metingen (FPS, RAM, laadtijd) op de doellaptop in het echte spel |
| DR-903 t/m DR-905 | Speelrondes door een mens in het echte spel |
| DR-906 | Starten met en zonder Sodium en Lithium in het echte spel |
| DR-907 | Wacht op de fouten uit de speelrondes (DR-903) |
| DR-602, DR-606 (duikaanval) | Eigen draakmodel en vlucht; er is nu een Ravager als stand-in |
| DR-609, DR-704, DR-703 (eigen geluiden) | Eigen audiobestanden; die kan Claude niet maken |
| DR-702 | Eigen titelscherm en logo (beeldmateriaal) |
| DR-802 (aansluiten) | Machines die doorlopend draaien; nu alleen op klik. Voorstel nodig |
| Verhaal en tweede eindbaas | `docs/verhaalschets.md` en `docs/tweede-eindbaas-voorstel.md` wachten op akkoord |
