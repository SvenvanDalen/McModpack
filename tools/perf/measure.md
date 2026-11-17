# Meetprocedure en nulmeting (DR-005)

Doel: elke meting op dezelfde manier doen, zodat getallen vergelijkbaar zijn. De procedure is klaar; de **uitgangswaarden moeten nog op de doellaptop gemeten worden** (nu leeg, niets verzonnen).

## Vooraf

1. Sluit andere programma's. Laptop aan de stroom, energiemodus "Beste prestaties".
2. Bouw de jar met `./gradlew build` en zet hem met Fabric API in de map `mods`. Optioneel Sodium en Lithium (zie `docs/aanbevolen-instellingen.md`); noteer wat je gebruikt.
3. Instellingen in Minecraft: weergavebereik 8, grafisch "Snel", deeltjes "Minimaal", VSync uit, FPS-limiet onbeperkt.
4. Nieuwe wereld (vast zaad, noteer het), creatief uit, overleving.

## Meten

Per scenario 2 minuten, noteer gemiddelde en laagste FPS (F3-scherm) en geheugen (F3, rechtsboven "Mem").

| Scenario | Wat je doet |
|---|---|
| A. Leeg | Sta stil op de spawn, kijk rond |
| B. Dorp | Loop door een dorp met dorpelingen |
| C. Industrie | Ga naast 10 draaiende machines staan (`/dr debug`-kit of eigen bouw) |
| D. Rijk | Rijk niveau 3, laat een aanval komen (`/dr aanval`) |
| E. Draak | Gevecht met `/dr draak`, alle drie de fasen |

Daarnaast (DR-805): 30 minuten spelen en elke 5 minuten het geheugen noteren.

## Doelen (uit CLAUDE.md)

- 40+ FPS gemiddeld op lage instelling.
- Onder ca. 3 GB RAM.

## Resultaten

Noteer datum, laptop, jar-versie en of Sodium/Lithium aan stond.

| Datum | Epic/versie | A FPS | B FPS | C FPS | D FPS | E FPS | RAM max | Opmerking |
|---|---|---|---|---|---|---|---|---|
| (nog niet gemeten) | | | | | | | | |

## Na elke epic (DR-801)

Herhaal de scenario's die de epic raakt en voeg een rij toe. Zakt een waarde onder het doel, meld dat dan als probleem voordat je verder bouwt.
