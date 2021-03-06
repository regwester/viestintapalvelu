/**
 * Enhanced Select2 Dropmenus
 *
 * AJAX Mode - When in this mode, your value will be an object (or array of objects) of the data used by Select2
 *     This change is so that you do not have to do an additional query yourself on top of Select2's own query
 * params [options] {object} The configuration options passed to $.fn.select2(). Refer to the documentation
 *
 *
 * Hacked support for select all and select none based on
 * - https://github.com/ivaynberg/select2/issues/195#issuecomment-22770367
 * - http://ivaynberg.github.io/select2/#events and
 * - similar "implementation" for jQuery chosen plugin: http://stackoverflow.com/questions/11172269/select-all-and-remove-all-with-chosen-js#18785302
 * Tag with: data-disable-selectall attribute to disable this feature or specify a tresshold with data-selectall-tresshold
 */
angular.module('ui.select2', []).value('uiSelect2Config', {})
    .directive('uiSelect2', ['uiSelect2Config', '$timeout', '$filter',
function (uiSelect2Config, $timeout, $filter) {
  var format = function (option) {
      var $originalOption = $(option.element);
      if ($originalOption.attr("class")) {
          return '<span class="'+$originalOption.attr("class")+'">'+option.text+'</span>';
      }
      return option.text;
  };
  var options = {
    formatSelection: format,
    formatResult: format
  };
  if (uiSelect2Config) {
    angular.extend(options, uiSelect2Config);
  }
  return {
    require: 'ngModel',
    priority: 1,
    compile: function (tElm, tAttrs) {
      var watch,
        repeatOption,
        repeatAttr,
        isSelect = tElm.is('select'),
        isMultiple = angular.isDefined(tAttrs.multiple);

      // Enable watching of the options dataset if in use
      if (tElm.is('select')) {
        $(tElm).on("select2-open", function(evnt) {
          var $field = $(evnt.currentTarget),
          $dropdown = $(".select2-drop-multi");
          var contained = function( el, clz ) {
              var container = document.createElement("div");
              container.appendChild(el);
              return container;
          }
          var width = $dropdown.width();
          var showBtnsTresshold = $field.attr("data-selectall-tresshold") || 0;
          optionsCount = $field.children().length,
              selectAllText = $filter("i18n")('select_all'),
              selectNoneText = $filter("i18n")('select_none');
          if( $field.attr("multiple") !== undefined
                    && !( $field.attr("data-disable-selectall") !== undefined && $field.attr("data-disable-selectnone") !== undefined )
                    && optionsCount >= showBtnsTresshold ) {
              var selectAllEl = document.createElement("a"),
                  selectAllElContainer = contained(selectAllEl),
                  selectNoneEl = document.createElement("a"),
                  selectNoneElContainer = contained(selectNoneEl);
              selectAllEl.appendChild( document.createTextNode( selectAllText ) );
              selectNoneEl.appendChild( document.createTextNode( selectNoneText ) );
              $dropdown.prepend("<div class='ui-select2-spcialbuttons-foot' style='clear:both;border-bottom: 1px solid black;'></div>");
              var $selectAllEl = $(selectAllEl),
                  $selectAllElContainer = $(selectAllElContainer),
                  $selectNoneEl = $(selectNoneEl),
                  $selectNoneElContainer = $(selectNoneElContainer);
              $selectNoneElContainer.addClass("ui-select2-selectNoneBtnContainer");
              $selectAllElContainer.addClass("ui-select2-selectAllBtnContainer");
              if( $field.attr("data-disable-selectnone") === undefined ) {
                  $dropdown.prepend(selectNoneElContainer);
              }
              if( $field.attr("data-disable-selectall") === undefined ) {
                  $dropdown.prepend(selectAllElContainer);
              }
              var reservedSpacePerComp = (width - 35) / 2;
              $selectNoneElContainer.css("float", "right").css("padding", "5px 8px 5px 0px")
                  .css("max-width", reservedSpacePerComp+"px")
                  .css("max-height", "15px").css("overflow", "hidden");
              $selectAllElContainer.css("float", "left").css("padding", "5px 5px 5px 7px")
                  .css("max-width", reservedSpacePerComp+"px")
                  .css("max-height", "15px").css("overflow", "hidden");
              $selectAllEl.on("click", function(e) {
                  e.preventDefault();
                  $field.select2('destroy').find('option').prop('selected', 'selected').end().select2().change();
                  return false;
              });
              $selectNoneEl.on("click", function(e) {
                  e.preventDefault();
                  $field.select2('destroy').find('option').prop('selected', false).end().select2().change();
                  return false;
              });
          }
        }).on("select2-close", function(evnt) {
            $(".ui-select2-selectAllBtnContainer").remove();
            $(".ui-select2-selectNoneBtnContainer").remove();
            $(".ui-select2-spcialbuttons-foot").remove();
        });

        repeatOption = tElm.find('option[ng-repeat], option[data-ng-repeat]');

        if (repeatOption.length) {
          repeatAttr = repeatOption.attr('ng-repeat') || repeatOption.attr('data-ng-repeat');
          watch = jQuery.trim(repeatAttr.split('|')[0]).split(' ').pop();
        }
      }

      return function (scope, elm, attrs, controller) {
        // instance-specific options
        var opts = angular.extend({}, options, scope.$eval(attrs.uiSelect2));

        /*
        Convert from Select2 view-model to Angular view-model.
        */
        var convertToAngularModel = function(select2_data) {
          var model;
          if (opts.simple_tags) {
            model = [];
            angular.forEach(select2_data, function(value, index) {
              model.push(value.id);
            });
          } else {
            model = select2_data;
          }
          return model;
        };

        /*
        Convert from Angular view-model to Select2 view-model.
        */
        var convertToSelect2Model = function(angular_data) {
          var model = [];
          if (!angular_data) {
            return model;
          }

          if (opts.simple_tags) {
            model = [];
            angular.forEach(
              angular_data,
              function(value, index) {
                model.push({'id': value, 'text': value});
              });
          } else {
            model = angular_data;
          }
          return model;
        };

        if (isSelect) {
          // Use <select multiple> instead
          delete opts.multiple;
          delete opts.initSelection;
        } else if (isMultiple) {
          opts.multiple = true;
        }

        if (controller) {
          // Watch the model for programmatic changes
           scope.$watch(tAttrs.ngModel, function(current, old) {
            if (!current) {
              return;
            }
            if (current === old) {
              return;
            }
            controller.$render();
          }, true);
          controller.$render = function () {
            if (isSelect) {
              elm.select2('val', controller.$viewValue);
            } else {
              if (opts.multiple) {
                elm.select2(
                  'data', convertToSelect2Model(controller.$viewValue));
              } else {
                if (angular.isObject(controller.$viewValue)) {
                  elm.select2('data', controller.$viewValue);
                } else if (!controller.$viewValue) {
                  elm.select2('data', null);
                } else {
                  elm.select2('val', controller.$viewValue);
                }
              }
            }
          };

          // Watch the options dataset for changes
          if (watch) {
            scope.$watch(watch, function (newVal, oldVal, scope) {
              if (angular.equals(newVal, oldVal)) {
                return;
              }
              // Delayed so that the options have time to be rendered
              $timeout(function () {
                elm.select2('val', controller.$viewValue);
                // Refresh angular to remove the superfluous option
                elm.trigger('change');
                if(newVal && !oldVal && controller.$setPristine) {
                  controller.$setPristine(true);
                }
              });
            });
          }

          // Update valid and dirty statuses
          controller.$parsers.push(function (value) {
            var div = elm.prev();
            div
              .toggleClass('ng-invalid', !controller.$valid)
              .toggleClass('ng-valid', controller.$valid)
              .toggleClass('ng-invalid-required', !controller.$valid)
              .toggleClass('ng-valid-required', controller.$valid)
              .toggleClass('ng-dirty', controller.$dirty)
              .toggleClass('ng-pristine', controller.$pristine);
            return value;
          });

          if (!isSelect) {
            // Set the view and model value and update the angular template manually for the ajax/multiple select2.
            elm.bind("change", function () {
              if (scope.$$phase || scope.$root.$$phase) {
                return;
              }
              scope.$apply(function () {
                controller.$setViewValue(
                  convertToAngularModel(elm.select2('data')));
              });
            });

            if (opts.initSelection) {
              var initSelection = opts.initSelection;
              opts.initSelection = function (element, callback) {
                initSelection(element, function (value) {
                  controller.$setViewValue(convertToAngularModel(value));
                  callback(value);
                });
              };
            }
          }
        }

        elm.bind("$destroy", function() {
          elm.select2("destroy");
        });

        attrs.$observe('disabled', function (value) {
          elm.select2('enable', !value);
        });

        attrs.$observe('readonly', function (value) {
          elm.select2('readonly', !!value);
        });

        if (attrs.ngMultiple) {
          scope.$watch(attrs.ngMultiple, function(newVal) {
            elm.select2(opts);
          });
        }

        // Initialize the plugin late so that the injected DOM does not disrupt the template compiler
        $timeout(function () {
          elm.select2(opts);

          // Set initial value - I'm not sure about this but it seems to need to be there
          elm.val(controller.$viewValue);
          // important!
          controller.$render();

          // Not sure if I should just check for !isSelect OR if I should check for 'tags' key
          if (!opts.initSelection && !isSelect) {
            controller.$setViewValue(
              convertToAngularModel(elm.select2('data'))
            );
          }
        });
      };
    }
  };
}]);
