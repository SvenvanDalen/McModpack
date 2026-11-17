# Voorstel tweede eindbaas (DR-610)

**Status: voorstel, wacht op jouw goedkeuring.** Er is nog niets gebouwd. DR-610 is XL en optioneel (tijdperk 3), dus eerst opknippen en bespreken.

## Opknippen in kleinere stories

| Nieuw | Wat | Maat | Hangt af van |
|---|---|---|---|
| DR-610a | Ontwerp: wie of wat is de baas, waar komt hij voor, wat wil hij (past bij akte 3 in `docs/verhaalschets.md`) | S | jouw keuze |
| DR-610b | Spelregels in `core/`: gezondheid, fasen en aanvallen als getallen, met tests (hergebruik `BossPhases`, `BossRewards`) | M | 610a |
| DR-610c | Baas in het spel als stand-in (vanilla mob met balk, zoals de draak nu), `/dr`-commando om hem te roepen | M | 610b |
| DR-610d | Beloning die bij tijdperk 3 past (bijvoorbeeld een runenmachine-onderdeel), eenmalig per speler | S | 610c |
| DR-610e | Eigen model, geluid en muziek | L | DR-602, DR-703 |

610a t/m 610d kan ik zonder het draaiende spel bouwen en testen (balans niet). 610e kan pas met Blockbench en geluiden.

## Vraag aan jou

Wat voor baas wil je? Voorbeelden om uit te kiezen of te negeren:
1. **Runenwachter**: een golem die uit de runesteen is gemaakt, vecht met schokgolven.
2. **Stoomkoning**: een mechanische baas uit tijdperk 2, past bij de industrielaag.
3. **Iets anders**, jouw idee.

Mijn voorkeur: de Runenwachter, omdat hij het tijdperk-3-thema (runen) afsluit en als stand-in een bestaande vanilla mob kan gebruiken.
