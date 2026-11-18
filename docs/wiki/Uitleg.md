# Uitleg: wat is alles nu

## Het spel in het kort
- **Klassen:** Ridder, Magiër, Schurk, met levels (max 30) en vaardigheden.
- **Quests en verhaal:** opdrachten en gesprekken met figuren in dorpen.
- **Wereld:** koninkrijken met dorpen; soms een burcht, dungeon of drakengebied ernaast. Zonder zo'n extra plek is er ongeveer een op de drie keer een **grote stad** (64x64, 25 woningen, 200 inwoners). Inwoners zijn een getal; zichtbaar zijn er hooguit 16 figuren, zodat het spel snel blijft.
- **Ruïnes:** vier soorten (wachttoren, smederij, kapel, drakenkamp) met een ruïnesteen met aanwijzingen en een kist met beloning.
- **Industrie in drie tijdperken:** waterrad, windmolen, smederij, as, stoomketel, zaag, pers, runesmederij, transportband en glazen buizen.
- **Rijk:** belastingkist, bouwplantafel, geruchtentafel, machinebord, bliksemafleider, karstation (kolen uit een mijnkar) en treinstation (reis voor de helft van de prijs).
- **Draak:** eindbaas in het drakengebied.

## Hoe het is opgebouwd
- `core/`: de spelregels in gewone Java, zonder Minecraft. Daardoor goed te testen.
- `data/`: de dunne laag die het in Minecraft zet (blokken, wereldgeneratie).
- `data/drakenrijken/`: inhoud als JSON (quests, vaardigheden, recepten, ruïnes, geruchten, loot, dialogen).
- Tests: JUnit voor de regels en GameTests voor gedrag in het spel (40 GameTests groen).

## Wat nog niet af is
Metingen en speelrondes in het echte spel, een eigen draakmodel, eigen geluiden en titelscherm, en een besluit over het verhaal en de tweede eindbaas. Ook is de ruïne en de stad nog niet bekeken op een draaiende server.

## Zelf bouwen
JDK 25 en `./gradlew build`. Bij een certificaatfout: `JAVA_TOOL_OPTIONS=-Djavax.net.ssl.trustStoreType=Windows-ROOT`.
