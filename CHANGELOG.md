# Changelog

Versienummering volgt [Semver](https://semver.org/lang/nl/): `MAJOR.MINOR.PATCH`. De versie staat in `gradle.properties`.

## [Niet uitgebracht]

- Machine-overzicht (#148, eerste stap): in de kern maakt het overzicht per machine een regel, zet problemen bovenaan, kapt af op 64 regels en geeft een korte samenvatting. Nog geen scherm.
- Leidingen (#150, eerste stap): in de kern bepaalt een leiding de doorvoer (hoogstens 10 per tick) en een maximale lengte van 32 blokken, gelijk aan de assen. Nog geen buisblok en geen koppeling aan de stoomketel.
- Blauwdrukkosten (#147, eerste stap): in de kern kunnen de kosten, de maximale grootte (16x16x16) en het tekort van een bouwplan berekend worden. Nog geen boek-item en geen plaatsing in het spel.
- Beroepen (#152, eerste stap): dorpelingen worden gelijk verdeeld over boer, mijnwerker, smid en wachter; `/dr beroepen` toont de verdeling en de dagopbrengst. Nog niet uitbetaald, zelf een beroep kiezen en JSON-inhoud volgen; de balans is nog niet door de Economy Designer nagekeken.
- Seizoenen (#155): cyclus van 4 x 7 speldagen (lente, zomer, herfst, winter). Spelers krijgen een melding bij een nieuw seizoen en `/dr seizoen` toont het seizoen. De oogstfactor (winter 50%, zomer 125%) staat in de kern maar is nog niet aan de oogst gekoppeld; weer, kleuren en aanvallen veranderen nog niet. Niet in het spel getest.
- Windmolen (#145): nieuw blok dat aan een as kracht levert: 0 onder hoogte 64, oplopend tot 6 op hoogte 200, alleen met lucht om zich heen en zicht op de hemel. Zwakker dan een waterrad met vier zijden (8). De 16x16-textuur is een eerste ontwerp en niet in het spel bekeken.
- Marktprijzen (#156): handelsprijzen schommelen per speldag tussen 85 en 115 procent, begrensd op 50 tot 150 samen met de rijkskorting. De werking via de welwillendheid van dorpelingen is een benadering en niet in het spel getest; vraag en aanbod door verkopen zit er nog niet in.
- Stadsmuur (#153): muurblokken op een ring rond je rijk verkleinen een aanval met hoogstens 30% (nooit tot nul). Gemeten bij het begin van een aanval; torens tellen nog niet mee.
- Belasting (#154): `/dr belasting` geeft goudstaven per speldag (2 per rijksniveau, hoogstens 3 dagen opgespaard). Een eigen belastingkist als blok volgt later.
- Tijdelijke vanilla geluiden bij vaardigheden en drakenvuur (niet in het spel getest; eigen geluiden volgen).
- Vaardighedenscherm (toets K): alle vaardigheden per klasse, met punten over en geleerde vaardigheden als [x]. Klikken leert een vaardigheid via de server.
- Alle 18 vaardigheden werken via `/dr gebruik <id>` (13 actief, 5 passief); waarden zijn een eerste schatting.
- Quest-tracker in de HUD en een draak (tijdelijk een Ravager) met drie fasen, vuur en beloning.
- Nog niet getest in het echte spel: schermen en HUD; lijnen in de vaardighedenboom zijn niet gezien in het spel.

## [0.1.0]

Eerste ontwikkelstand. Nog geen speelbare release.

- Spelregels in pure Java (`core/`): klassen, vaardigheden en respec, quests, dialogen, rijk, eindbaasfasen, tijdperkpoorten, snelreizen, wereldkaart en kompas.
- Industrie: waterrad, stoomketel, stroomnet, receptmachines, transportband, machinebudget.
- Inhoud als JSON: vaardigheden (6 per klasse), startuitrusting, buittabellen, spawnlijsten.
- Prestatiegrenzen: machines, entiteiten, deeltjes en tijd per tick.
- Nederlandse en Engelse teksten voor klassen en vaardigheden.
- Nog niet in het spel: schermen, HUD, modellen, structuren, geluiden, teksturen en de draak als echte entiteit.
