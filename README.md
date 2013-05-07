viestintapalvelu
================

Ville Peurala
Iina Sipilä

1. Yleistä

OPH Viestintäpalvelun avulla käyttäjä voi muodostaa asiakirjoja (osoitetarroja) PDF ja CSV formaateissa. Palvelu muodostaa asiakirjan tiedosto-cacheen ja palauttaa kutsujalle tunnuksen jolla se voidaan hakea erillisellä download-kutsulla.

Osoitetarrat PDF-muodossa muodostetaan käyttäen seuraavia kirjastoja:
* Apache Velocity template engine: http://velocity.apache.org/
* Flying Saucer: http://code.google.com/p/flying-saucer/

PDF-tiedostomuoto valitaan antamalla palvelulle template-tiedosto parametri "osoitetarrat.html". Muodostettu asiakirja sisältää osoitteen asemoituna tarratulostusta silmälläpitäen (Avery L7159, A4, 3x8 tarraa per lomake). Tiedoston enkoodaus on UTF-8.

Osoitetarrat CSV-muodossa muodostetaan käyttäen seuraavia kirjastoja:
* Apache Velocity template engine: http://velocity.apache.org/

CSV-tiedostomuoto valitaan antamalla palvelulle template-tiedosto parametri "osoitetarrat.csv". Muodostettu asiakirja sisältää osoitteen kentät pilkulla eroteltuna ja osoitteet rivivälillä eroteltuna. Tiedoston enkoodaus on UTF-8.

Palvelu on julkistettu REST/JSON API:n välityksellä, tarkempi kuvaus API:sta luvussa 3.

2. Ajoympäristöt

Palvelua voidaan ajaa paikallisesti ja sovelluspalvelimella. Alla keskitytään paikalliseen ajoympäristöön.

Sovellus voidaan käynnistää ajamalla seuraava komento projektin juuressa:

mvn clean compile exec:java -Dexec.mainClass="fi.vm.sade.viestintapalvelu.Launcher"

Komento käynnistää paikallisen tomcatin porttiin 8080. Sovelluksen testi-käyttöliittymä aukeaa selaimella osoitteesta:

http://localhost:8080/index.html

3. API-dokumentaatio




