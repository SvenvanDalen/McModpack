# Ruïne-indelingen (tekstontwerp)

Vier kleine bouwplannen, bij de verhalen in `src/main/resources/data/drakenrijken/ruins/*.json`. Dit is een ontwerp op papier; er is nog niets gebouwd.

## Gemeenschappelijke regels
- Grondoppervlak maximaal 12x12, hoogte maximaal 8 (kelder en schuilplaats tellen niet mee in die 8, maximaal 3 diep).
- Het raster is van boven gezien. Kolom 1 is links (west), rij 1 is boven (noord). Elke rij heeft precies 12 tekens.
- Alleen gewone blokken, geen entiteiten, geen gloeiende lichtbronnen buiten een uitgedoofd kampvuur. Weinig blokken, dus weinig polygonen.
- Elke ruïne heeft precies één ruïnesteen (S) en één beloningskist (K). De kist is bewust niet vanaf de ingang te zien.
- Gaten in muren en losse stukken puin horen erbij: het is een ruïne, geen gebouw. Mag per plaatsing licht variëren (muren 1 of 2 blokken lager), maar S en K blijven op hun plek.
- Ingang is steeds een opening van 1 of 2 blokken, met aan de buitenkant een pad van een andersoortig blok zodat het pad leesbaar is.
- Beloningen volgens de json: wachttoren 8, drakenkamp 12, smederij 8, kapel 10.

Gebruikte tekens (per ruïne aangevuld):
`.` = open grond (het gras of zand van de omgeving, niet vervangen)

---

## 1. Verlaten wachttoren

Verhaal: de wachters hielden stand tot het vuur kwam. Een verbrand schild met drakenklauwmerk. De schat ligt onder de wachtpost.

Afmeting: toren 8 breed x 7 diep, plan 12x12, hoogte 8, kelder 2 diep.

```
     123456789012
 1   ............
 2   ......r....r
 3   ..CCMCCMCC..
 4   ..CS.....C..
 5   ..M..BB..M..
 6   ..C..PP..C..
 7   ..C......C..
 8   ..C......C..
 9   ..MCCC.CCC..
10   ...r.....r..
11   ............
12   ............
```

Legenda:
- `C` cobblestone, `M` mossy_cobblestone (vooral onderaan en in de hoeken)
- `B` blackstone (verbrande vlek op de vloer, onder het gat in het dak)
- `P` oak_planks (de wachtpost: een verhoogd platform van 2x1, 1 blok hoog)
- `r` los puin (één cobblestone op de grond)
- `S` ruinesteen, `K` kist (ondergronds)

Hoogte per laag (y=0 is de grond buiten):
| Laag | Inhoud |
|---|---|
| -2 | Schuilkelder van 2x1 onder de wachtpost: K op kolom 7 rij 6, verder niets |
| -1 | Gat van 1 blok onder kolom 6 rij 6, bereikbaar via een valluik in de wachtplank (zichtbaar als plank met een rand van mossy_cobblestone) |
| 0 | Vloer van gras/stone (bruin pad bij de ingang), de blackstone-vlek en de wachtpost-planken |
| 1-3 | Alle muren heel, op de ingang na (kolom 7 rij 9, 1 blok breed, 2 hoog) |
| 4-5 | Noord- en westmuur nog heel; oost- en zuidmuur voor de helft afgebrokkeld |
| 6 | Alleen de hoeken en de westmuur (kolom 3, rij 3 t/m 5) |
| 7 | Eén brokstuk bovenop de noordwesthoek (de 'laatste tand' van de toren) |

Positie ruinesteen: binnen tegen de noordmuur, kolom 4 rij 4 (naast de afgebrokkelde inscriptie van mossy_cobblestone). Eerste wat je ziet als je binnenkomt: je kijkt recht naar S.

Kist: op laag -2 onder de wachtpost (kolom 7 rij 6). Je vindt het luik via de brief ('de schat ligt onder de wachtpost'); het valluik zit in de planken, de kist 2 blokken lager.

Hoe de omgeving het verhaal vertelt:
- De blackstone-vlek en een kale plek in het dak (gat van 2x2 in laag 5-6 boven de vlek) laten zien waar het vuur binnenkwam.
- Het ene wachtplatform van planken is het enige blok dat niet verbrand is: de wachters bewaarden daar hun spullen.
- Binnen ligt een schild: ´een iron_bars-blok of een banner in het puin naast de vlek (decoratie, vervangt een `.` in kolom 8 rij 7).
- Buiten ligt een paar blokken puin (`r`) richting de ingang: de toren is van binnenuit naar buiten gebarsten.
- Mossy_cobblestone onderaan en in de hoeken: de toren staat al lang leeg.

Blokkenaantal: ongeveer 140 (muren) plus 8 (vloer-details), makkelijk binnen het budget.

---

## 2. Gevallen smederij

Verhaal: de draak kwam in de nacht, het staal smolt. Aambeeld door hitte gespleten, half afgemaakt zwaard in de koude oven, goud in de kelder achter een luik.

Afmeting: gebouw 10 breed x 7 diep, plan 12x12, hoogte 5 (de schoorsteen 8), kelder 3 diep.

```
     123456789012
 1   ............
 2   .CCCCCCCCCC.
 3   .C........C.
 4   .C.AA..FF.C.
 5   .C......S.C.
 6   .C.ZT.....C.
 7   .C........C.
 8   .CCCC..CCCC.
 9   ............
10   ...BBBB.....
11   ....BB......
12   ............
```

Legenda:
- `C` cobblestone (muren), vloer van stone
- `A` twee losse aambeeld-helften (beschadigd aambeeld `chipped_anvil` en `damaged_anvil` naast elkaar, een van beide een kwartslag gedraaid; zo lijkt hij gespleten)
- `F` furnace (de koude oven, 2 breed, niet aan: er ligt een half zwaard als iron_sword in een rand van het dichte item-frame, of simpeler één `iron_bars` erop)
- `Z` blaasbalg (smoker of dropper tegen de muur, zijkant bruin)
- `T` valluik (trapdoor) in de vloer, vlak achter de blaasbalg
- `B` blackstone (verbrande grond, buiten en op de vloer rond de oven)
- `S` ruinesteen
- `K` kist (kelder, zie onder)

Hoogte per laag:
| Laag | Inhoud |
|---|---|
| -3 | Kelder 3x3 (kolom 4 t/m 6, rij 6 t/m 8); K op kolom 6 rij 8, tegen de achterwand |
| -2 | Trap/ladder van het valluik naar beneden, kelderwand van stone |
| -1 | Valluik T in de vloer |
| 0 | Vloer van stone, blackstone rondom de oven en voor de ingang (kolom 4 t/m 7, rij 10 t/m 11) |
| 1-3 | Muren; noordmuur (rij 2) volledig, oost- en westmuur met een gat van 2 blokken, zuidmuur met de ingang (kolom 6 t/m 7, rij 8, 2 breed en 3 hoog) |
| 4 | Alleen de noord- en westmuur, het dak ontbreekt |
| 5 | Stukjes van de noordmuur (hoekstenen) |
| 6-8 | Schoorsteen op kolom 8 rij 4 boven de oven (cobblestone, 1x1, 3 hoog; de kruin is afgebroken, dus 2 blokken) |

Positie ruinesteen: tegen de oostkant van de oven (kolom 9 rij 5, naast `F`), op de plek waar de plattegrond van de kelder in de steen gekrast is (dat staat in de clue; tekst, geen bouwwerk).

Kist: in de kelder, op laag -3, in het achterste hoekje (kolom 6 rij 8). Toegang: valluik T in de vloer achter de blaasbalg (kolom 5 rij 6). Het luik 'hield stand': het is de enige houten plek die niet is verbrand.

Hoe de omgeving het verhaal vertelt:
- Het gespleten aambeeld (twee helften, verschillend gedraaid) midden in de ruimte, omringd door blackstone: het staal is gesmolten.
- Rond de oven blackstone op vloer en muur (het vuur was daar het heetst).
- Een gat in het dak boven het aambeeld (4x3 boven rij 4 t/m 6), de rest van het dak is weg.
- Buiten een spoor van blackstone (kolom 4 t/m 7, rij 10 t/m 11): de draak kwam van buiten, het vuur kwam via de deur.
- Een brandplek binnen laat zien dat de smid hier tot het laatste moment werkte.

Blokkenaantal: ongeveer 110 plus 25 voor de kelder.

---

## 3. Verzonken kapel

Verhaal: het water staat tot aan de preekstoel. De klokken zwijgen sinds de draak de toren insloeg. Offerkist verstopt door monniken achter het altaar.

Afmeting: kapel 8 breed x 11 diep, plan 12x12, hoogte 8 (de afgebroken toren), water 1 tot 2 diep.

```
     123456789012
 1   ..CCMMMMCC..
 2   ..C..OO..C..
 3   ..C.....rC..
 4   ..CU..WWWC..
 5   ..CWWWWWWC..
 6   ..MSQWWQWM..
 7   ..CWWWWWWC..
 8   ..CWQWWQWC..
 9   ..MWWWWWWM..
10   ..C.WWWW.C..
11   ..CCC.CCCC..
12   ............
```

Legenda:
- `C` cobblestone, `M` mossy_cobblestone (veel: het is al lang nat)
- `W` water, op de kapelvloer van stone_bricks (ondergelopen, 1 blok diep in het koor, 2 blokken in het schip)
- `Q` pilaar van mossy_stone_bricks, 4 blokken hoog, zonder dak erop
- `O` altaar: 2x1 stone_brick_slab onderin het koor (droog, de enige droge plek behalve de nis)
- `U` preekstoel (een trap omhoog met een plankblok erop, half onder water)
- `r` puin van de ingestorte toren (cobblestone, 2 hoog)
- `S` ruinesteen, `K` kist (nis)

Hoogte per laag:
| Laag | Inhoud |
|---|---|
| -1 | Vloer van stone_bricks onder het water |
| 0 | Water, wateroppervlak op laag 1 (in het schip) en laag 0 (op het koor, droog) |
| 1 | Pilaren (Q), preekstoel, altaar; alle muren |
| 2-3 | Muren en pilaren; muren in het schip met 2 boogvormige gaten (ramen) aan de oostkant |
| 4 | Pilaren afgebroken (hoogte 3 tot 4 wisselt); noordmuur heel, schip-muren gaten |
| 5-6 | Alleen noordmuur (kolom 3 t/m 10, rij 1) en een stuk westmuur; puin `r` op de plek van de ingestorte toren (kolom 9 rij 3) |
| 7-8 | Los stuk toren (kolom 9 t/m 10 rij 2 t/m 3): de plek waar de draak insloeg |

Positie ruinesteen: op de voet van de eerste pilaar in het water (kolom 4 rij 6, bij de preekstoel), net boven water. Dit is de 'natte pilaar' met het gebed.

Kist: in een nis van 1 blok in de achterwand, kolom 6 rij 1 (laag 1), afgedekt door een losse stone_bricks-blok achter het altaar (kolom 6 rij 2 laag 1: een blok dat iets naar voren steekt, een ander kleur dan de rest). Het blok achter het altaar is de 'losse steen' uit de clue; de kist zit er direct achter.

Hoe de omgeving het verhaal vertelt:
- Water in het schip tot de preekstoel: de kapel is verzonken of ondergelopen sinds het dak instortte.
- Puin en een stuk toren (`r`) naast het koor laten zien waar de draak insloeg.
- Het koor is droog, zodat je naar het altaar kunt lopen (beloning voor het kijken).
- Een rij pilaren zonder dak; er is een tweede rij maar de oostelijke is korter (verschil in hoogte maakt het oneven en half ingestort).
- Mossy_cobblestone onder in de muren: het water staat er al lang.

Blokkenaantal: ongeveer 180 plus water (water is gratis in polygonen, wel een lichte lichtberekening: niet meer dan 40 waterblokken).

---

## 4. Drakenkamp

Verhaal: een kring verbrande tenten, de jagers vluchtten. Buit onder de grote rots, ver van de grot van de draak.

Afmeting: open kamp 12x12, hoogte 6 (de rots), geen kelder.

```
     123456789012
 1   ........RRR.
 2   .TTT....RRRR
 3   .TTT....RRR.
 4   .TT......K..
 5   ...B....BB..
 6   ..BB.FF.B...
 7   ...BSFF.....
 8   .TTT...s.TTT
 9   .TTT.....TTT
10   .TT....B.TT.
11   ......s.....
12   ............
```

Legenda:
- `T` tent: zwarte wol (verbrand), 1 tot 2 blokken hoog, de bovenste laag deels ingestort (hier en daar een gat)
- `R` de grote rots: stone/andesite, 3 tot 4 breed, 6 hoog, met een uitstekende rand aan de zuidkant zodat de kist eronder verstopt zit
- `K` kist met gebroken slot (ruiger: onder de rotsrand, half onder cobblestone-puin)
- `B` blackstone (as en verbrande grond)
- `F` uitgedoofd kampvuur (campfire, niet aan) in het midden
- `s` speer (fence, 2 hoog, schuin gebroken)
- `S` ruinesteen
- Helmen en speren: één `iron_bars`-blok of een los pot/skull-blok (decoratie) in de as, geen aparte voorwerpen

Hoogte per laag:
| Laag | Inhoud |
|---|---|
| 0 | Gras met blackstone-vlekken en het uitgedoofde kampvuur |
| 1 | Tenten (zwarte wol), speren (fence), de onderkant van de rots |
| 2 | Tenten: alleen de zuid- en oostrand, de noordkant is ingestort; speren |
| 3-6 | Alleen de rots (R): trapsgewijs smaller, de top 2x2 op laag 6 |

Positie ruinesteen: naast het kampvuur aan de westkant (kolom 5 rij 7), met rondom een kring van blackstone zodat hij opvalt in het midden van het kamp.

Kist: onder de grote rots, kolom 10 rij 4 (aan de zuidkant van de rots, onder de uitstekende rand). Het is het verst van de plek waar de draak slaapt (de draak ligt in het noordoosten, de rots ligt noordoost; zie de ontwerpnoot hieronder). De kist is open gebroken: het slot is verdwenen (een open kist, met een leeg item-frame ernaast als decoratie).

Opmerking over de ligging: de clue zegt 'ver van zijn grot'. In het spel hoort de grot van de draak dus aan de zuid- of westkant van het kamp te liggen, niet bij de rots. Dit hangt af van hoe de plaatsing van het drakengebied werkt; dit is een vraag voor de plaatsingscode, niet voor het bouwplan.

Hoe de omgeving het verhaal vertelt:
- Een ring van verbrande tenten rond een koud kampvuur: ze zaten hier samen, het vuur kwam plotseling.
- As en blackstone tussen de tenten, speren die schuin in de grond staan en een paar gebroken: ze vluchtten zonder te vechten.
- Eén tent (kolom 2 t/m 4, rij 2 t/m 4) is half ingestort: daar kwam de draak neer.
- Het pad van de ruinesteen naar de rots is open, de rest van het kamp is bezaaid met as: ze namen alleen het hoognodige mee.
- De kist ligt onder de rots, uit het zicht van de lucht, zodat een draak hem niet ziet.

Blokkenaantal: ongeveer 120 (tenten 60, rots 40, rest 20), de laagste van de vier.

---

## Overzicht

| Ruïne | Grond | Hoogte | Kelder | Ruinesteen | Kist | Ongeveer blokken |
|---|---|---|---|---|---|---|
| Wachttoren | 8x7 in 12x12 | 8 | 2 | binnen, kolom 4 rij 4 | onder de wachtpost | 150 |
| Smederij | 10x7 in 12x12 | 5 (schoorsteen 8) | 3 | naast de oven, kolom 9 rij 5 | kelder via valluik | 135 |
| Kapel | 8x11 in 12x12 | 8 | 0 (water 2) | op pilaarvoet, kolom 4 rij 6 | nis achter altaar | 180 + water |
| Drakenkamp | 12x12 open | 6 | 0 | naast kampvuur, kolom 5 rij 7 | onder de rots | 120 |

## Nog te controleren bij het bouwen
- Of de ruinesteen en de kist elkaar niet storen met de omgeving (bomen, water) die erbij gegenereerd wordt: eerst een vlakke grond met 1 blok marge nodig.
- Of de kapel in water of op een droge plek begint; de waterlaag moet dan als blokken in het plan zitten, niet als natuurlijk water, anders is het afhankelijk van de wereld.
- Controleer of de valluiken en het kampvuur (campfire) als gewone blokken in het bouwplan-formaat passen voordat deze vastliggen.
