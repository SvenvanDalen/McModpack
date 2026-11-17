# Changelog

Versienummering volgt [Semver](https://semver.org/lang/nl/): `MAJOR.MINOR.PATCH`. De versie staat in `gradle.properties`.

## [Niet uitgebracht]

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
