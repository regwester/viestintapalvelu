Viestintäpalvelu
================

Kehitysympäristön pystytys
--------------------------

    git clone git@github.com:Opetushallitus/viestintapalvelu.git
    mvn clean install eclipse:eclipse

Ajaminen paikallisesti
----------------------

    mvn tomcat7:run-war
    
Mene selaimella osoitteeseen <http://localhost:9090/index.html>

Dokumentaatio
-------------

Löytyy osoitteesta:

<http://liitu.hard.ware.fi/confluence/pages/viewpage.action?pageId=9994831>

Bamboo
------
http://pulpetti.hard.ware.fi:8085/bamboo/browse/VIESTINTAPALVELU-OPHVIESTINTAPALVELU10

Osoitetarrojen tulostus
-----------------------

Aseta skaalaus: 100%.

Tekijät
-------

* Iina Sipilä <iina.sipila@reaktor.fi>
* Ville Peurala <ville.peurala@reaktor.fi>
