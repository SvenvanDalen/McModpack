# Draken en Rijken: ontwerp

Status: concept, wacht op goedkeuring.
Keuze van de gebruiker: variant C (bijna alles zelf programmeren), met een middeleeuws fantasy-RPG en een industrielaag, en het moet vlot spelen op een matige laptop.

## 1. Intentie en het waarom
Een technisch indrukwekkend Minecraft-project: een eigen mod (geen verzameling van andermans mods) die Minecraft verandert in een fantasy-RPG waarin industrie een vast onderdeel van het verhaal is. Het moet te spelen zijn op een matige laptop (geïntegreerde GPU, 8 GB RAM), zodat vrienden het ook kunnen draaien. De gebruiker wil vooral dat de techniek zelf indrukwekkend is.

## 2. Doelen
- Eigen klassensysteem: ridder, magiër, schurk, met levels en een skill tree.
- Eigen questsysteem met een doorlopend verhaal, van dorp naar rijk naar eindbaas.
- Eigen wereldgeneratie: koninkrijken, dorpen, dungeons en een draken-gebied.
- Eigen industrielaag in drie tijdperken: watermolen en smederij, stoom, en een late "rune-machine"-fase.
- Eigen bossen, waaronder een eindbaas (draak) met meerdere fasen.
- Rijksysteem (wereld verandert als je industrie groeit, bijvoorbeeld meer dorpelingen, handel, aanvallen).
- Speelbaar op een matige laptop: stabiel 40+ FPS op een lage instelling en onder ca. 3 GB RAM voor het spel.

## 3. Non-goals
- Geen eigen engine, geen eigen graphics-pijplijn.
- Geen multiplayer-economie of serverplatform (dat was idee 5, niet dit).
- Geen Bedrock-versie.
- Geen honderden items: liever een kleine, afgewerkte set dan een grote, halve set.

## 4. Anti-goals (mislukt, ook als het technisch werkt)
- Het draait alleen op een krachtige pc.
- Het is een losse stapel features zonder doorlopend verhaal.
- Het project is nooit speelbaar tot de allerlaatste stap.

## 5. Randvoorwaarden
- Minecraft Java Edition; geen bestaande RPG- of industriemods als basis.
- Prestatiemods (zoals Sodium en Lithium) zijn toegestaan als losse extra's, omdat ze niets aan de inhoud veranderen.
- Eigen code moet licht zijn: geen zware berekeningen per tick, grenzen aan het aantal actieve machines en entiteiten, en wereldgeneratie die geen grote stukken tegelijk bouwt.
- Eigen teksturen en modellen blijven laag in resolutie (bijvoorbeeld 16x16) en laag in polygonen.

## 6. Aanpakken die zijn overwogen
- **A. Bestaande mods samenvoegen:** snelst, stabielst, maar niet technisch bijzonder. Afgewezen door de gebruiker.
- **B. Pack met scripts en één eigen mod:** goede middenweg. Niet gekozen.
- **C. Bijna alles zelf programmeren:** gekozen door de gebruiker. Grootste omvang en risico, maar het indrukwekkendst.

Gekozen: C. Het risico (nooit af) wordt beperkt met de fasering in sectie 8, zodat er na elke fase iets speelbaars is.

## 7. Technische keuzes (mijn keuzes, niet door de gebruiker bevestigd)
- Mod-loader: Fabric, omdat die lichter is en de gangbare prestatiemods ondersteunt.
- Taal: Java, met de Java-versie die bij de gekozen Minecraft-versie hoort.
- Minecraft-versie: de bouwer controleert eerst welke recente versie goed ondersteund wordt door Fabric en de prestatiemods, en kiest die. Ik heb dit niet vooraf geverifieerd.
- Data (quests, klassen, recepten, structuren): zoveel mogelijk als datapack-bestanden (JSON), zodat het makkelijk aan te passen is en de code klein blijft.
- Hulpmiddelen: Gradle, Fabric Loom, en Blockbench voor modellen.

## 8. Fasering (elke fase is speelbaar)
1. **Fundament:** een leeg Fabric-project dat start, plus een profiel- en meetinstelling (FPS en RAM op de doellaptop).
2. **Klassen en levels:** kies een klasse, krijg ervaring, skill tree met een paar vaardigheden per klasse.
3. **Quests en verhaal:** een questmotor (data-gestuurd) en de eerste akte van het verhaal.
4. **Wereld:** een koninkrijk met dorpen en dungeons via wereldgeneratie.
5. **Industrie tijdperk 1:** watermolen, smederij en een eenvoudig transportsysteem.
6. **Eindbaas en akte 2:** de draak, met fasen en beloningen.
7. **Industrie tijdperk 2 en 3 en het rijksysteem.**
8. **Prestatie-ronde en afwerking:** meten, profileren, snoeien en een releasepakket.

## 9. Succescriteria
- De doellaptop haalt de FPS- en RAM-doelen uit sectie 2.
- Een speler kan van begin tot einde van het verhaal komen zonder crashes.
- Alle fasen staan los te testen.

## 10. Overgelaten aan de bouwer
- Exacte Minecraft- en loader-versie, en welke prestatiemods.
- Verhaal, namen, balans en getallen van klassen, quests en machines.
- Teksturen en modellen (eigen stijl, laag in detail).
- Indeling van de code en tests.

## 11. Open punten voor de gebruiker
- Zijn de doelen voor de laptop (40+ FPS, ca. 3 GB RAM) goed, of heeft de doellaptop andere specs?
- Moet het solo speelbaar zijn, of ook samen met vrienden (multiplayer)?
