# Changelog

Versienummering volgt [Semver](https://semver.org/lang/nl/): `MAJOR.MINOR.PATCH`. De versie staat in `gradle.properties`.

## [Niet uitgebracht]

- Gevangenen werken in de mijn, op het land of op schepen; per 5 gevangenen is een bewaker nodig (DR-1109) (DR-1109, #13)
- Koning omkopen (prijs per niveau) of afzetten met een sterker leger dan zijn wacht; zijn plek innemen kan met een even groot eigen rijk (DR-1108) (DR-1108, #12)
- Scheepsbemanning: zonder minimale bemanning vaart het schip niet, kanonnen hebben schutters nodig en delen de plekken (DR-1107) (DR-1107, #11)
- Schip zelf bouwen: kiel, breedte, romp en zeilen bepalen drijfkracht, snelheid, wendbaarheid en plekken; een te zwaar schip zinkt (DR-1106) (DR-1106, #10)
- Belegeringswapens: ontwerp (arm, contragewicht, frame) bepaalt bereik, schade en gewicht; te licht frame breekt, te zwaar wapen is niet te verplaatsen (DR-1105) (DR-1105, #9)
- Leger: soldaten aanwerven (max 200), uitrusten en voeden; zichtbare soldaten binnen het budget, hongerig leger vecht minder (DR-1104) (DR-1104, #8)
- Bouwwerk telt mee voor macht: 40 blokken is 1 punt, bouwen alleen geeft hooguit niveau 3; groter rijk trekt meer mensen en vijanden aan (DR-1103) (DR-1103, #7)
- Een koning erkent je pas na eigen bouwwerken (troonzaal, haven, kazerne); grotere rijken eisen er meer; spelregels en tests, nog niet in de wereld (DR-1102, #6)
- Koninkrijken groeien, blijven gelijk of vervallen in de tijd (niveau 0 tot 5, 0 is verwoest); spelregels en tests, nog niet zichtbaar in de wereld (DR-1101, #5)
- Grote stad van 64x64 met 25 woningen en 200 inwoners (een op de drie koninkrijken zonder burcht, dungeon of drakengebied); inwoners zijn een getal, zichtbaar blijven er hooguit 16 figuren.
- Vier ruïnes (wachttoren, smederij, kapel, drakenkamp) worden nu echt gebouwd bij het drakengebied, met één ruïnesteen en één kist (#158).
- Treinstation: reis voor de helft van de prijs naar het volgende ontdekte koninkrijk, met een brandende stoomketel in de buurt (#144).
- Karstation: een mijnkar levert kolen op uit de speltijd (#146).
- Ruïnesteen: vier verhalen (wachttoren, smederij, kapel, drakenkamp), per plek vast gekozen.
- Glazen buis: rechtsklikken meet de lengte en doorvoer van de leiding (#150).
- Machinebord: rechtsklikken toont de status van alle machines binnen 8 blokken (#148).
- Een ruïnesteen met aanwijzingen staat nu bij elk drakengebied in de wereld.
- Ruïnesteen: elke rechtsklik onthult een volgende aanwijzing; na de laatste volgt een beloning (#158).
- Geruchtentafel: rechtsklik vertelt een nieuw gerucht uit een JSON-lijst; je hoort elk gerucht één keer (#157).
- Bliksemafleider: laadt op bij onweer en geeft een machine in de buurt extra kracht voor één taak (#151).
- Moersleutel: herstelt een machine met slijtage of storing; machines kunnen na ongeveer 60 producten stukgaan (#149).
- Belastingkist betaalt ook de opbrengst van je dorpelingen (boer, mijnwerker, smid) uit (#152).
- Bouwplantafel: rechtsklik zet een werkplaats (waterrad, as, smederij) naast de tafel neer als je de blokken hebt (#147).
- Belastingkist: rechtsklik haalt de belasting van je rijk op (#153); `/dr belasting` doet hetzelfde.
- Machines draaien nu doorlopend: een klik start een werk dat over meerdere ticks loopt, met kracht- en tijdsgrens, en bij afsluiten wordt alles afgemaakt (DR-802).
- Economiesimulatie van 90 speldagen als test (belasting, beroepen en muuronderhoud, prijsgrenzen).
- Kern voor bliksemvanger (`LightningRod`), herbergsgeruchten (`RumorTable`) en ruïneverhalen (`RuinStory`); nog zonder blokken of inhoud.
- Belastingplafond 5 dagen, `Profession.payout` (uitbetaling beroepen) en `MachineWear` (slijtage en storing, uit te zetten).
- Economie bijgesteld: NPC-grens en rijkshuisvesting gelijk (max 16), belasting 0/2/4/7/11/16, boer levert 2, seizoenen winter 60/herfst 115/zomer 130, nieuwe `GoldSinks` (bouwplan 1 goud per 10 blokken, muuronderhoud 1 goud per 20 blokken per dag).
- Trein en mijnkar (#144, #146, eerste stap): in de kern bepalen prijs (helft van snelreis), reistijd, kracht van de ketel en de opbrengst per minuut van een kar (hoogstens 4 karren). Nog geen wagon of rails in het spel.
- Seizoen en beroepen (#155, #152): de opbrengst per dag van een rijk kan nu per seizoen berekend worden; alleen de boeren merken het (winter de helft, zomer een kwart extra). Nog niet aangesloten op de dagelijkse uitbetaling.
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
