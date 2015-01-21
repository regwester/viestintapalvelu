/*
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 */

'use strict';

angular.module('letter-templates')
    .factory('TemplateTreeService', ['$resource', '$http','$q', function ($resource, $http, $q) {

        var serviceUrl = '/viestintapalvelu/api/v1/',
            organizationResUrl = serviceUrl + 'organizationhierarchy/',
            selectedApplicationTarget,
            deferred = $q.defer();

        var map = function(fn, arr) {
            var result = [];

            arr.forEach(function(element) {
                result.push(fn(element)) ;
            });
            return result;
        };

        return {

            getHakukohteetByApplicationPeriod: function (applicationperiod) {
                return $http.get(organizationResUrl+"/hakukohteet/applicationPeriod/" + applicationperiod);
            },

            getOrganizationName: function(orgoid, languageCode) {
                return $http.get(organizationResUrl + 'name/' + orgoid + '/' + languageCode);   
            },
            getOrganizationHierarchy: function (applicationPeriod) {
                return $http.get(organizationResUrl + 'applicationPeriod/' + applicationPeriod);
            },
            getParsedTreeGrid : function(response, onlyLeafOids) {


                var oidList = [];
                var oidMap = {};

                var parseData = function(item) {
                    var firstColum18nStr = "Organisaatio ja kirjetyyppi";
                    var newRow = {};

                    //TODO handle localization
                    if(item.nimi && item.nimi.fi) {
                        newRow[firstColum18nStr] = item.nimi.fi;
                    } else if (item.nimi && item.nimi.sv) {
                        newRow[firstColum18nStr] = item.nimi.sv;
                    } else if (item.nimi && item.nimi.en) {
                        newRow[firstColum18nStr] = item.nimi.en;
                    }

                    newRow["language"] = item.language;
                    newRow["state"] = item.state;
                    newRow["oid"] = item.oid;
                    var isLeaf = false;
                    if(item.children && item.children.length > 0) {
                        isLeaf = true;
                        var childrenRows = map(parseData, item.children);
                        newRow["children"] = childrenRows;
                    }
                    oidList.push({"oid": item.oid, "isLeaf": isLeaf});
                    oidMap[item.oid] = newRow;
                    return newRow;
                };

                var newData = [];
                var dataArr = [];
                dataArr.push(response.data[0]);
                newData = map(parseData, dataArr);
                return {"tree": newData, "oids":oidList, "oidToRowMap": oidMap};
            },

            addTemplatesToTree : function (tree, templates, oidMap, control) {

                templates.forEach(function(item) {
                    var firstColum18nStr = "Organisaatio ja kirjetyyppi";
                    var orgOid = item.organizationOid;
                    var letterRow = item;
                    letterRow[firstColum18nStr] = item.name;
                    letterRow["isLetter"] = true;
                    var parent;
                    if(orgOid) {
                        parent = oidMap[orgOid];
                    } else {
                        parent = control.get_first_branch();
                    }
                    var newRow = control.add_branch(parent, letterRow, true);
                    control.expand_all_parents(parent);
                });

                return tree;
            },

            addHakukohteetToTree : function (tree, drafts, oidMap, control) {
                var hakukohdeOids = [];
                var hakukohdeOidMap = {};
                drafts.forEach(function(draft) {
                    var firstColum18nStr = "Organisaatio ja kirjetyyppi";

                    var orgOid = draft.oid;
                    var parent = oidMap[orgOid];

                    if(draft.tulokset) {
                        draft.tulokset.forEach(function(tulos) {
                            var hakukohdeRow = tulos;

                            if(tulos.nimi && tulos.nimi.fi) {
                                hakukohdeRow[firstColum18nStr] = tulos.nimi.fi;
                            } else if (tulos.nimi && tulos.nimi.sv) {
                                hakukohdeRow[firstColum18nStr] = tulos.nimi.sv;
                            } else if (tulos.nimi && tulos.nimi.en) {
                                hakukohdeRow[firstColum18nStr] = tulos.nimi.en;
                            }
                            hakukohdeOids.push(tulos.oid);
                            var newRow = control.add_branch(parent, hakukohdeRow, true);
                            hakukohdeOidMap[tulos.oid] = newRow; //used to quickly add drafts under this row
                        });
                    }
                });
                return {"tree" : tree, "hakukohdeoids" : hakukohdeOids, hakukohdeOidMap : hakukohdeOidMap};
            },

            addDraftsToTree : function (tree, templates, oidMap, control) {

                templates.forEach(function(item) {
                    var firstColum18nStr = "Organisaatio ja kirjetyyppi";
                    var orgOid = item.organizationOid;
                    var letterRow = item;
                    letterRow["language"] = item.languageCode;
                    letterRow["state"] = item.tila;
                    letterRow[firstColum18nStr] = item.fetchTarget;
                    letterRow["isLetter"] = true;
                    letterRow["isDraft"] = true;
                    var parent = oidMap[orgOid];
                    var newRow = control.add_branch(parent, letterRow, true);
                    control.expand_all_parents(parent);
                });

                return tree;
            }
        }
    }]);