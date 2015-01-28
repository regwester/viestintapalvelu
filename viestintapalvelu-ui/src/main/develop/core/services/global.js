'use strict';

angular.module('core.services').factory('Global', function () {

    function getUserLanguage() {
        var i, lang, max;

        if (window.myroles) {
            for (i = 0, max = window.myroles.length; i < max; i++) {
                if (window.myroles[i].indexOf('LANG_') == 0) {
                    lang = window.myroles[i].substring(5);
                    if (['fi', 'sv', 'en'].indexOf(lang) != -1) { // returns index or -1 if lang is not in array
                        return 'fi'; //default to finnish in anycase
                    }
                }
            }
        }

        return 'fi'; // Language defaults to finnish
    }

    return {
        getUserLanguage: getUserLanguage,
        getEditorOptions: function () {
            return {
                height: 400,
                width: 650,
                menubar: false,
                language_url: 'i18n/tinymce/' + getUserLanguage() + '.js',
                //paste plugin to avoid ms word tags and similar content
                plugins: "paste textcolor",
                toolbar: "undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | forecolor backcolor",
                paste_auto_cleanup_on_paste: true,
                paste_remove_styles: true,
                paste_remove_styles_if_webkit: true,
                paste_strip_class_attributes: true
            };
        }
    }
});