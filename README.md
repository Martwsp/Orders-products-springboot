# Spring Boot zadanie starter pack
## Street of Code - Java kurz
### Popis systému
Aplikační server umožňuje spravovat produkty a objednávky. Webové rozhraní (API), stejně jako objekty používané pro komunikaci, jsou definovány v dané specifikaci.

Specifikace API je dostupná na adrese: https://app.swaggerhub.com/apis-docs/JAHICJAKUB/Street_of_Code_Spring_Boot_zadanie/1.0.0

Systém umožňuje vytváření a odebírání produktů. Dále umožňuje úpravu existujících produktů, stejně jako zvýšení stavu produktů na skladě.

Systém umožňuje vytváření a mazání objednávek. Do objednávek je možné primárně přidávat produkty, které jsou dostupné na skladě. V případě nedostatku zboží na skladě bude odpovědí chybový kód 400.

Systém umožňuje zaplacení objednávky. Po zaplacení objednávky se změní její stav na `paid`. 
Do zaplacené objednávky již není možné přidávat produkty.

### Automatizované testy
Systém obsahuje automatizované integrační testy, které se nacházejí ve složce `src/test/java`.
Testy testují funkčnost API. Není potřeba přidávat další testy ani je jinak upravovat.

Pokud všechny testy projdou, znamená to, že aplikace byla správně implementována.
# Orders-products-springboot
