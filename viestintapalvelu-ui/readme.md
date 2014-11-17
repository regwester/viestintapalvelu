Viestintäpalvelun käyttöliittymä
================================

Viestintäpalvelun käyttöliittymä on tehty [AngularJS](https://angularjs.org/) sovelluksena.

### Ajaminen

```mvn clean install```


Kehitystyössä kannattaa ajaa ```gulp watch```, niin gulp vahtii javascript ja sass tiedostoja ja kääntää ne automaattisesti.

### Teknologiat

| Nimi                                                                                      | Kuvaus                                                                                                      | Versio   |
|-------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|:--------:|
| [AngularJS](https://angularjs.org)                                                        | AngularJS on rakenteellinen sovelluskehys dynaamisten web-sovellusten rakentamiseen                         | 1.2.7    |
| [JQuery](http://jquery.com/)                                                              | JQuery on yleiskirjasto, jota käytetään erityisesti DOMin muokkaamiseen                                     | 1.10.2   |
| [JQuery File Upload](https://blueimp.github.io/jQuery-File-Upload/)                       | JQueryn lisäosa, joka mahdollistaa tiedostojen lataamisen palvelimelle kaikilla tuetuilla selaimilla        | 9.5.2    |
| [JQuery i18 Properties](https://github.com/jquery-i18n-properties/jquery-i18n-properties) | JQueryn lisäosa, joka helpottaa tekstien kieliversioiden hallintaa                                          | 1.0.9    |
| [Bootstrap](http://getbootstrap.com/2.3.2/)                                               | Kokoelma valmiita tyyliluokkia ja pieniä javascript toimintoja                                              | 2.3.2    |
| [AngularUI](http://angular-ui.github.io/)                                                 | Kokoelma AngularJS sovelluskehystä tukevia Bootstrap-tyylisiä komponentteja                                 | 0.11.0   |
| [UI-Router](https://github.com/angular-ui/ui-router)                                      | AngularJS:n sisäisen reitityksen hoitava kirjasto                                                           | 0.2.10   |
| [TinyMCE](http://www.tinymce.com/)                                                        | Javascript pohjainen HTML WYSIWYG editori                                                                   | 4.0.11   |
| [AngularUI TinyMCE](https://github.com/angular-ui/ui-tinymce)                             | AngularJS yhteensopiva sovelluskääre TinyMCE:lle                                                            | 0.0.5    |
| [Lodash](https://lodash.com/)                                                             | Kokoelma pieniä apufunktioita tiedon käsittelemiseen                                                        | 2.4.1    |
| [File Saver](https://github.com/eligrey/FileSaver.js)                                     | HTML5 saveAs() funktion toteuttava kirjasto                                                                 |   *      |
| [Blob](https://github.com/eligrey/Blob.js/)                                               | HTML5 Blob rajapinnan toteuttava kirjasto                                                                   |   *      |

### Projektin rakenne

Kehitystiedostot ovat suoraan src/main kansion alla. Julkisesti näkyvät tuotantotiedostot generoidaan webapps kansion alle.
