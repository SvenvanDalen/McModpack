# Ontwerp: Japans koninkrijk (Epic 12)

Datum: 2026-11-18. Status: wacht op goedkeuring.

## Doel en waarom
De speler wil naast het bestaande koninkrijk een tweede, Japans geïnspireerd koninkrijk met eigen gebouwen, een eigen gebied, extra klassen, eigen wapens en eigen eten. Het geeft de wereld variatie en is het eerste koninkrijk met eigen inhoud.

## Doelen
- Een Japans koninkrijk met pagoden, torii-poorten, dojo's, een kasteel op een stenen voet en een dorp met houten huizen.
- Een eigen gebied eromheen: kersenbomen, bamboe, een berg.
- Twee extra klassen: **samoerai** (zwaard en eer) en **onmyoji** (talisman-magie), met eigen vaardigheden en startset.
- Eigen wapens: katana, naginata, boog, shuriken.
- Eigen eten: rijst, onigiri, sushi, thee, met kleine bonussen.

## Niet-doelen
- Geen ninja-klasse (valt grotendeels samen met de schurk). Eventueel later.
- Geen eigen biome; het gebied is een bouwplan, net als het drakengebied (DR-305).
- Geen nieuwe industrietijdperken of eindbaas.
- Geen aanpassing van bestaande klassen, koninkrijken of balans.

## Anti-doelen (mislukt, ook als het werkt)
- Prestaties onder de grens: 40+ FPS op lage instelling, onder ca. 3 GB RAM.
- Spelregels met Minecraft-imports in `core/`.
- Een koninkrijk dat leeg aanvoelt: zonder bewoners, quests of buit telt het niet als klaar.

## Beperkingen
Uit CLAUDE.md: spelregels in pure Java in `core/`; inhoud als JSON in `data/drakenrijken/`; teksturen 16x16; modellen laag in polygonen; Definition of Done uit de backlog; prestaties meten na de epic; wiki en changelog bijwerken na elke story; elke story een eigen issue en PR (zoals Epic 11).

## Aanpak (gekozen: optie 1)
Overwogen: (1) in plakjes via Epic 12, (2) eerst alleen koninkrijk en gebied, (3) alles in één PR. Gekozen is 1: elk plakje laat het spel heel, is klein te controleren en terug te draaien. Optie 2 laat het koninkrijk leeg ogen; optie 3 is te zwaar voor de Definition of Done.

## Voorgestelde stories (volgorde)
| ID | Story | Acceptatie (kort) |
|---|---|---|
| DR-1201 | Koninkrijkstype en thema in data | `KingdomPlacement` kent een Japans type; deterministisch per seed; dichtheid blijft tussen 0,5% en 2% |
| DR-1202 | Klassen samoerai en onmyoji | Keuzescherm toont ze; startset, 3 start-vaardigheden per klasse uit JSON; geen crash zonder keuze |
| DR-1203 | Japans dorp en kasteel | Bouwplan met huizen, markt, dojo, torii, pagode en kasteel; NPC-plekken |
| DR-1204 | Japans gebied | Bouwplan met kersenbomen, bamboe en berg rond het koninkrijk; geleidelijke generatie zonder tick-piek |
| DR-1205 | Wapens | Katana, naginata, boog, shuriken met schade uit JSON en 16x16-tekstuur |
| DR-1206 | Eten | Rijst, onigiri, sushi, thee met bonussen uit JSON |
| DR-1207 | Bewoners, quests en buit | Minstens één quest, spawnlijst en buittabel voor het koninkrijk |
| DR-1208 | Meting en afronding | FPS- en RAM-meting; handleiding, wiki, changelog bijgewerkt |

Elke story: spelregels en tests in `core/`, data in JSON, dunne Minecraft-laag. Afhankelijkheden: DR-1201 eerst; 1203 en 1204 hangen van 1201 af; 1207 van 1203.

## Door jou beslist
- Optie 1, in plakjes, eerst het koninkrijk als zichtbaar plakje.
- Japans koninkrijk met eigen gebied, klassen, wapens en eten.
- Klassen samoerai en onmyoji; wapens en eten zoals hierboven.

## Mijn keuzes (graag controleren)
- Het gebied is een bouwplan, geen biome.
- Geen ninja-klasse.
- Nummering DR-1201 t/m DR-1208 en prioriteit M; volgorde van de stories.
- Namen en getallen (schade, bonussen, xp) bepaalt de bouwer met Game Designer en Economy Designer; ze komen in JSON.

## Aan de bouwer
- Vaardigheden en balans van beide klassen; getallen van wapens en eten.
- Exacte bouwplannen en kleuren; Minecraft-API-details controleren in de officiële docs.
