'use strict'

ajastusApp.factory('HakuNameByLocale', ['$locale', function ($locale) {
    var localeMapping = new Map([
        ["fi-fi", "kieli_fi"],
        ["sv-se", "kieli_sv"],
        ["en-us", "kieli_en"]
    ]);

    var lang = (!$locale.id) ? $locale.id : "fi_fi";

    return function (haku) {
        function getAnyName(haku) {
            var anyName = null;
            localeMapping.forEach(function (value) {
                if (!anyName || anyName == '') {
                    anyName = haku.nimi[value]
                }
            });
            return anyName
        }

        var name = haku.nimi[localeMapping.get(lang)];
        if (!name || name == '') name = getAnyName(haku);
        return name;
    }
}]);