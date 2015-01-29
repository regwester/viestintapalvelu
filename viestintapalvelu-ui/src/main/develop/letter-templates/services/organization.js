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
    .factory('TemplateTreeService', ['$resource', '$http','$q', '$filter', 'TemplateService', function ($resource, $http, $q, $filter, TemplateService) {

        var serviceUrl = '/viestintapalvelu/api/v1/',
            organizationResUrl = serviceUrl + 'organizationhierarchy/',
            selectedApplicationTarget,
            deferred = $q.defer();

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
            getParsedTreeGrid : function(response, firstColumnDisplayName) {


                var oidList = [];
                var oidMap = {};


                var firstColum18nStr = firstColumnDisplayName;
                console.log(firstColum18nStr);
                var parseData = function(item) {

                    var newRow = {};

                    newRow[firstColum18nStr] = TemplateService.getNameFromHaku(item, "");
                    newRow["language"] = item.language;
                    newRow["state"] = item.state;
                    newRow["oid"] = item.oid;
                    newRow["type"] = $filter('i18n')('reportedmessagelist.otsikko.organisaatio');
                    var isLeaf = false;
                    if(item.children && item.children.length > 0) {
                        isLeaf = true;
                        var childrenRows = item.children.map(parseData);
                        newRow["children"] = childrenRows;
                    }
                    oidList.push({"oid": item.oid, "isLeaf": isLeaf});
                    oidMap[item.oid] = newRow;
                    return newRow;
                };

                var newData = [];
                var dataArr = [];
                dataArr.push(response.data[0]);
                newData = dataArr.map(parseData);
                newData["firstColumnName"] = firstColumnDisplayName; //hack to get correct display name for the header
                return {"tree": newData, "oids":oidList, "oidToRowMap": oidMap};
            },

            addTemplatesToTree : function (tree, templates, oidMap, control) {

                var firstColum18nStr = tree.firstColumnName;
                templates.forEach(function(item) {
                    var orgOid = item.organizationOid;
                    var letterRow = item;
                    letterRow[firstColum18nStr] = item.name;
                    letterRow["type"] = $filter('i18n')('common.header.lettertemplate');
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

            addHakukohteetToTree : function (tree, hakukohteet, oidMap, control) {
                var hakukohdeOids = [];
                var hakukohdeOidMap = {};
                var firstColum18nStr = tree.firstColumnName;
                hakukohteet.forEach(function(hakukohde) {

                    var orgOid = hakukohde.oid;
                    var parent = oidMap[orgOid];

                    if(hakukohde.tulokset) {
                        hakukohde.tulokset.forEach(function(tulos) {
                            var hakukohdeRow = tulos;
                            
                            hakukohdeRow[firstColum18nStr] = TemplateService.getNameFromHaku(tulos, "");
                            hakukohdeRow["orgOid"] = orgOid;
                            hakukohdeRow["isHakukohde"] = true;
                            hakukohdeRow["type"] = $filter('i18n')('reportedletterlist.otsikko.hakukohde');
                            hakukohdeOids.push(tulos.oid);
                            var newRow = control.add_branch(parent, hakukohdeRow, false);
                            hakukohdeOidMap[tulos.oid] = newRow; //used to quickly add drafts under this row
                        });
                    }
                });
                return {"tree" : tree, "hakukohdeoids" : hakukohdeOids, hakukohdeOidMap : hakukohdeOidMap};
            },

            addDraftsToTree : function (tree, templates, oidMap, control) {
                var firstColum18nStr = tree.firstColumnName;
                templates.forEach(function(item) {
                    var orgOid = item.organizationOid;
                    var letterRow = item;
                    letterRow["language"] = item.languageCode;
                    letterRow["state"] = "Kirjeluonnos";
                    letterRow[firstColum18nStr] = item.fetchTarget;
                    letterRow["isLetter"] = true;
                    letterRow["isDraft"] = true;
                    letterRow["type"] = $filter('i18n')('letter.draft');
                    var parent = oidMap[orgOid];
                    var newRow = control.add_branch(parent, letterRow, true);
                    control.expand_all_parents(parent);
                });

                return tree;
            }
        }
    }]);