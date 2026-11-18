# Ontwerp: mijnkarren (#146) en stoomtrein (#144)

Status: voorstel, nog niet gebouwd. Wacht op akkoord (CLAUDE.md: niet bouwen buiten het goedgekeurde ontwerp).

## Uitgangspunt
De spelregels bestaan al in `core/`: `CartRoute` (laadgrootte per rondetijd, max. 4 actieve routes) en `TrainLine` (prijs met 50% korting op snelreizen, 8 blokken per seconde, ketel nodig). Wat ontbreekt is de Minecraft-laag. Laag houden voor de laptop: **geen rijdende entiteiten en geen echte rails**.

## Aanpak (aanbevolen): stationblokken
- **Karstation** (blok): rechtsklik met een route-instelling (laadgrootte/rondetijd uit het blok zelf, drie niveaus). Opbrengst wordt niet per tick berekend maar bij een klik uit de speltijd: `CartRoute.deliveredIn(seconden sinds laatste ophalen)`. Resultaat valt als items in je inventaris. Kosten: 0 ticks.
- **Treinstation** (blok): rechtsklik kiest een bestemming (volgende ontdekte dorp), toont prijs en reistijd; met ketel in de buurt (`SteamBoilerBlock`) en genoeg munten wordt de speler na `TrainLine.travelSeconds` verplaatst, via de bestaande `FastTravelService`. Tijdens de rit een bericht en geen laden van extra chunks.
- Een wagon-model/animatie kan later als versiering (los item, geen logica).

## Alternatieven
1. Echte rails en wagons als entiteiten: mooier, maar kost FPS/RAM, chunkproblemen bij weglopen, veel werk. Past niet bij het laptopdoel.
2. Alleen commando's: geen speelbare inhoud. Valt af.

## Kleine stappen
1. Karstation + GameTest (opbrengst na X seconden).
2. Treinstation + GameTest (geen kracht / te arm / OK).
3. Teksturen 16x16, recepten, lang-bestanden, CHANGELOG.

## Open vragen voor de eigenaar
- Akkoord met stationblokken in plaats van echte rails?
- Moeten karstations ook zonder spelerpresentie doorlopen (ja in dit voorstel, via speltijd)?
