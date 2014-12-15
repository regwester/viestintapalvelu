/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

'use strict';

angular.module('report')
    .factory('Options',['$http', '$window', '$log', function($http, $window, $log) {
        var _uri = "/viestintapalvelu/api/v1/options",
            _hakuCallbacks = null,
            _hakus = null;

        function hakus(success) {
            if (_hakus) {
                success(_hakus);
                return;
            }
            if (_hakuCallbacks) {
                _hakuCallbacks.push(success);
                return;
            }
            _hakuCallbacks = [success];
            $http.get(_uri+"/hakus").success(function(results) {
                _hakus = results;
                angular.forEach(_hakuCallbacks, function(cb) {
                    cb(results);
                });
            }).error(function(response) {
                $log.error("Hakujen haku epäonnistui: " +response);
            });
        }

        return {
            hakus: hakus
        };
    }]);