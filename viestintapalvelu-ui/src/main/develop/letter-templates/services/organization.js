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
            getOrganizationHierarchy: function (applicationPeriod) {
                return $http.get(organizationResUrl + 'applicationPeriod/' + applicationPeriod);
            },
            getParsedTreeGrid : function(response) {


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
                    oidList.push(item.oid);
                    if(item.children && item.children.length > 0) {
                        var childrenRows = map(parseData, item.children);
                        newRow["children"] = childrenRows;
                    }
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
                    var parent = oidMap[orgOid];

                    var newRow = control.add_branch(parent, letterRow, true);
                    control.expand_all_parents(parent);
                });

                return tree;
            }
        }
    }]);