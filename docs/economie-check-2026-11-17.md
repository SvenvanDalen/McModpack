# Economiecheck rijk, belasting, markt en beroepen (2026-11-17)

Gedaan door de Economy Designer-agent op de huidige getallen. Het zijn voorstellen; niets is aangepast. Eerst bespreken, want ze veranderen het ontwerp.

## Uitkomsten
- Belasting per dag (niveau 0 t/m 5): 0, 2, 4, 6, 8, 10 goudstaven.
- Beroepsopbrengst per dag (niveau 0 t/m 5): 5, 10, 15, 20, 20, 20.
- Seizoenen geven gemiddeld 96% oogst; winter raakt alleen de boeren (niveau 3: 18 in plaats van 20).
- Marktprijs: 85 tot 115% per dag plus 5% korting per niveau, blijft binnen 50 tot 150%.

## Bevindingen
1. Niveau 4 en 5 leveren geen extra opbrengst, want de NPC-grens (16, DR-803) ligt onder de rijkshuisvesting (20 en 24). Dit is nagekeken in de code: `RealmVillagers.target` kapt af op `EntityBudget.MAX_NPCS`.
2. Er zijn bijna geen putten voor goud, dus het kan zich ophopen.
3. Boer (1) is het zwakste beroep.

## Voorstellen (nog niet doorgevoerd)
1. NPC-grens en rijkshuisvesting gelijktrekken (24 of 16).
2. Belasting op niveau 4 en 5 laten doorgroeien: 0, 2, 4, 7, 11, 16.
3. Boer van 1 naar 2.
4. Seizoenen: winter 60, herfst 115, zomer 130.
5. Plafond van de belasting van 3 naar 5 dagen.
6. Gouddump: bouwplannen in goud (1 per 10 blokken) en muuronderhoud (1 per 20 blokken per dag).
7. Marktprijzen vastleggen op speeltijd (basisitem ongeveer 20 goud) en 90 speldagen simuleren.
