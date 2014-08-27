describe('Directive: limitedParagraph', function() {

  var compileDirective, elem,
      template = '<limited-paragraph content="content" limit="limit" show-button-text="{{showButtonText}}" hide-button-text="{{hideButtonText}}" />',

      testContent = 'Lorem ipsum dolor sit amet';

  beforeEach(function() {
    module('report');

    inject(function($injector) {
      var $compile = $injector.get('$compile'),
          $rootScope = $injector.get('$rootScope');

      compileDirective = function(content, charLimit, showButtonText, hideButtonText) {
        var scope = $rootScope.$new(), compiled;

        scope.content = content;
        scope.limit = charLimit;
        scope.showButtonText = showButtonText;
        scope.hideButtonText = hideButtonText;
        
        compiled = $compile(template);
        elem = compiled(scope);

        scope.$digest();
      };
    });
  });

	it('shows only the content if the content is shorter than the limit', function() {
    compileDirective(testContent, testContent.length + 10, 'show', 'hide');

    expect(elem.text()).toEqual(testContent);
    expect(elem.find('span').length).toEqual(0); // nothing else within than content
  });

  it('shows the ellipsis and the "Show More" -button initially when the content is longer than the limit', function() {
    compileDirective(testContent, testContent.length - 10, 'show', 'hide');

    var controls = elem.find('span');
    expect(controls.length).toEqual(2);
    expect(controls.find('span').last().text()).toEqual('...'); // ellipsis
    expect(controls.find('a').text()).toEqual('show'); // show more button

    var content = elem.text().replace(controls.first().text(), ''); // remove the controls
    expect(content).not.toEqual(testContent);
    expect(content).toEqual(testContent.substr(0, testContent.length -10));
  });

  it('shows the whole content and the "Hide" -button after the "Show More" -button has been clicked', function() {
    compileDirective(testContent, testContent.length - 10, 'show', 'hide');

    elem.find('a').click();
    var controls = elem.find('span');

    expect(controls.length).toEqual(1);
    expect(controls.find('a').text()).toEqual('hide');

    expect(elem.text()).not.toEqual(testContent);
    expect(elem.text().replace(controls.text(), '')).toEqual(testContent);
  });

});
