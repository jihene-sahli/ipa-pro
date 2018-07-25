(function() {
  angular.module('builder.components', ['builder', 'validator.rules', 'ui.tree']).config([
    '$builderProvider', function($builderProvider) {
      $builderProvider.registerComponent('textInput', {
        group: 'Text',
        tab: 'Text',
        label: 'Text Input',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        validationOptions: [
          {
            label: 'string',
            rule: 'string'
          }, {
            label: 'long',
            rule: 'long'
          }, {
            label: 'money',
            rule: 'money'
          }, {
            label: 'date',
            rule: 'date'
          }
        ],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <input type=\"text\" ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\" id=\"{{formName+index}}\" class=\"form-control\" placeholder=\"{{placeholder}}\"/>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-minus\"></i>\n<span>Text Input</span>",
        popoverTemplate: "            <form>\n                <div class=\"form-group\">\n                    <label class='control-label'>ID</label>\n                    <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Label</label>\n                    <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Description</label>\n                    <input type='text' ng-model=\"description\" class='form-control'/>\n                </div>\n<div class=\"form-group\">\n                    <label class='control-label'>Taille</label>\n                    <input type='text' ng-model=\"inputSize\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Placeholder</label>\n                    <input type='text' ng-model=\"placeholder\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Expression régulière</label>\n                    <input type='text' ng-model=\"regex\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Message d'erreur</label>\n                    <input type='text' ng-model=\"errorMessage\" class='form-control'/>\n                </div>\n                <div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox' ng-model=\"required\" />\n                        Required</label>\n                </div>\n                <div class=\"form-group\" ng-if=\"validationOptions.length > 0\">\n                    <label class='control-label'>Type</label>\n                    <select ng-model=\"$parent.validation\" class='form-control' ng-options=\"option.rule as option.label for option in validationOptions\"></select>\n                </div>\n                <hr/>\n                <div rights></div>\n                <div class='form-group' deletecomponent></div>\n                </div>\n            </form>"
      });
      $builderProvider.registerComponent('textArea', {
        group: 'Text',
        tab: 'Text',
        label: 'Text Area',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <textarea type=\"text\" ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\" id=\"{{formName+index}}\" class=\"form-control\" rows='6' placeholder=\"{{placeholder}}\"/>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-align-justify\"></i>\n<span>Text Area</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Placeholder</label>\n        <input type='text' ng-model=\"placeholder\" class='form-control'/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('staticText', {
        group: 'Text',
        tab: 'Text',
        label: 'Static text',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        validationOptions: [],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <div type=\"text\" ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\" id=\"{{formName+index}}\" class=\"form-control\" placeholder=\"{{placeholder}}\">{{placeholder}}</div>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-arrow-right\"></i>\n<span>Static text</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Placeholder</label>\n        <textarea type='text' ng-model=\"placeholder\" class='form-control'/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n    <div class=\"form-group\" ng-if=\"validationOptions.length > 0\">\n        <label class='control-label'>Type</label>\n        <select ng-model=\"$parent.validation\" class='form-control' ng-options=\"option.rule as option.label for option in validationOptions\"></select>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('dynamicText', {
        group: 'Text',
        tab: 'Text',
        label: 'Dynamic text',
        description: 'description',
        required: false,
        value: 'value',
        validationOptions: [],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <div type=\"text\" ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\" id=\"{{formName+index}}\" class=\"form-control\" placeholder=\"{{value}}\">{{value}}</div>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-arrow-right\"></i>\n<span>Dynamic text</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Value</label>\n        <input type='text' ng-model=\"value\" class='form-control'/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n    <div class=\"form-group\" ng-if=\"validationOptions.length > 0\">\n        <label class='control-label'>Type</label>\n        <select ng-model=\"$parent.validation\" class='form-control' ng-options=\"option.rule as option.label for option in validationOptions\"></select>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('percentage', {
        group: 'Text',
        tab: 'Text',
        label: 'Percentage',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <div class='input-group'>\n            <input type='text' class=\"form-control\"  placeholder=\"{{placeholder}}\"/>\n            <span class=\"input-group-addon\">%</span>\n         </div>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<span class=\"glyphicon\" style=\"font-weight: bold;\">%</span>\n<span>Percentage</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Placeholder</label>\n        <input type='text' ng-model=\"placeholder\" class='form-control'/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n    <div class=\"form-group\" ng-if=\"validationOptions.length > 0\">\n        <label class='control-label'>Type</label>\n        <select ng-model=\"$parent.validation\" class='form-control' ng-options=\"option.rule as option.label for option in validationOptions\"></select>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('message', {
        group: 'Text',
        tab: 'Text',
        label: 'Message',
        description: 'description',
        value: 'info',
        template: "<div class=\"form-group\">\n    <span class=\"col-sm-11 {{value}}\" style=\"padding-top: 7px;padding-bottom: 7px;margin-left: 40px;\"><i class=\"glyphicon glyphicon-info-sign\" ng-if=\"value == 'info'\"></i><i class=\"glyphicon glyphicon-warning-sign\" ng-if=\"value == 'warn'\"></i><i class=\"glyphicon glyphicon-minus-sign\" ng-if=\"value == 'error'\"></i> {{description}}</span>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-info-sign\"></i>\n<span>Message</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Type</label>\n        <select class='form-control' ng-model=\"value\" ng-options=\"option for option in ['info','warn','error']\"></select>\n    </div>\n\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('checkbox', {
        group: 'Radio-Checkbox',
        tab: 'Radio-Checkbox',
        label: 'Checkbox',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        options: ['value'],
        arrayToText: true,
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <input type='hidden' ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\"/>\n        <div class='checkbox' ng-repeat=\"item in options track by $index\">\n            <label><input type='checkbox' ng-model=\"$parent.inputArray[$index]\" value='item'/>\n                {{item}}\n            </label>\n        </div>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-check\"></i>\n<span>Checkbox</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Options</label>\n        <input class=\"form-control\" ng-model=\"optionsText\"/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required\n        </label>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('checklist', {
        group: 'Radio-Checkbox',
        tab: 'Radio-Checkbox',
        label: 'Checklist',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        options: ['value one', 'value two'],
        arrayToText: true,
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <input type='hidden' ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\"/>\n        <div class='checkbox' ng-repeat=\"item in options track by $index\">\n            <label><input type='checkbox' ng-model=\"$parent.inputArray[$index]\" value='item'/>\n                {{item}}\n            </label>\n        </div>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-list\"></i>\n<span>Checklist</span>",
        popoverTemplate: "<form>\n     <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Options</label>\n        <textarea class=\"form-control\" rows=\"3\" ng-model=\"optionsText\"/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required\n        </label>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('radio', {
        group: 'Radio-Checkbox',
        tab: 'Radio-Checkbox',
        label: 'Radio',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        options: ['value one', 'value two'],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <div class='radio' ng-repeat=\"item in options track by $index\">\n            <label><input name='{{formName+index}}' ng-model=\"$parent.inputText\" validator-group=\"{{formName}}\" value='{{item}}' type='radio'/>\n                {{item}}\n            </label>\n        </div>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-record\"></i>\n<span>Radio</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Options</label>\n        <textarea class=\"form-control\" rows=\"3\" ng-model=\"optionsText\"/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('select', {
        group: 'Select',
        tab: 'Select',
        label: 'Select',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        options: ['value one', 'value two'],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <select ng-show=\"required\" ng-options=\"value for value in options\" id=\"{{formName+index}}\" class=\"form-control\"\n            ng-model=\"inputText\" ng-init=\"inputText = options[0]\"/>\n        <select ng-show=\"!required\" id=\"{{formName+index}}\" class=\"form-control\">\n            <option value=\"0\">{{value}}</option>\n        </select>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-collapse-down\"></i>\n<span>Select</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Options</label>\n        <textarea class=\"form-control\" rows=\"3\" ng-model=\"optionsText\"/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n    <div class=\"form-group\" ng-show=\"!required\">\n        <label class='control-label'>Empty value</label>\n        <input type='text' ng-model=\"value\" class='form-control'/>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('picklist', {
        group: 'Select',
        tab: 'picklist',
        label: 'PickList',
        description: 'description',
        placeholder: 'placeholder',
        options: ['value one', 'value two'],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <div class=\"ms-container\">\n            <div class=\"ms-selectable\">\n                <ul class=\"ms-list\">\n                    <li class=\"ms-elem-selectable\" ng-repeat=\"val in options track by $index\">{{val}}</li>\n                </ul></div><div class=\"ms-selection\">\n                <ul class=\"ms-list\" tabindex=\"-1\"></ul>\n            </div>\n        </div>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-pause\"></i>\n<span>PickList</span>",
        popoverTemplate: "            <form>\n                <div class=\"form-group\">\n                    <label class='control-label'>ID</label>\n                    <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Label</label>\n                    <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Description</label>\n                    <input type='text' ng-model=\"description\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Options</label>\n                    <textarea class=\"form-control\" rows=\"3\" ng-model=\"optionsText\"/>\n                </div>\n<div class=\"form-group\">\n                    <label class='control-label'>Nombre maximum à selectionner</label>\n                    <input type='text' ng-model=\"maxSelect\" class='form-control'/>\n                </div>\n <div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox' ng-model=\"required\" />\n                        Required</label>\n                </div>\n                <hr/>\n                <div rights></div>\n                <div class='form-group' deletecomponent></div>\n            </form>"
      });
      $builderProvider.registerComponent('date', {
        group: 'Select',
        tab: 'Select',
        label: 'Date',
        description: 'description',
        required: false,
        dateformat: 'Date',
        options3: [false],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <div class=\"row\">\n            <div class='col-sm-6'>\n                <div class=\"form-group\">\n                    <div class='input-group date' id='datetimepicker1'>\n                        <input type='text' class=\"form-control\" />\n                        <span class=\"input-group-addon\">\n                            <span ng-show=\"dateformat != 'Time'\" class=\"glyphicon glyphicon-calendar\"></span>\n                            <span ng-show=\"dateformat != 'Date'\" class=\"glyphicon glyphicon-time\"></span>\n                        </span>\n                    </div>\n                </div>\n            </div>\n        </div>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-calendar\"></i>\n<span>Date</span>",
        popoverTemplate: "            <form>\n                <div class=\"form-group\">\n                    <label class='control-label'>ID</label>\n                    <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Label</label>\n                    <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Description</label>\n                    <input type='text' ng-model=\"description\" class='form-control'/>\n                </div>\n                <div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox' ng-model=\"required\" />\n                        Required</label>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Format</label>\n                    <select class='form-control' ng-model=\"dateformat\" ng-options=\"option for option in dateformatOptions\" ng-change=\"datePattern = datePatterns[dateformat][0]\" ng-init=\"datePattern = datePattern || datePatterns['Date'][0]\"></select>\n                </div>\n<div class=\"form-group\">\n			<label class='control-label'>Pattern</label>\n                    <select class='form-control' ng-model=\"datePattern\">\n                        <option ng-repeat=\"option in datePatterns['Date']\" ng-show=\"dateformat == 'Date'\" ng-value=\"option\" ng-selected=\"option == datePattern\">{{option}}</option>\n				<option ng-repeat=\"option in datePatterns['Time']\" ng-show=\"dateformat == 'Time'\" ng-value=\"option\" ng-selected=\"option == datePattern\">{{option}}</option>\n				<option ng-repeat=\"option in datePatterns['DateTime']\" ng-show=\"dateformat == 'DateTime'\" ng-value=\"option\" ng-selected=\"option == datePattern\">{{option}}</option>\n                    </select>\n                </div>\n                <div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox' ng-model=\"options3[0]\" />\n                        Date du jour</label>\n                </div>\n                <hr/>\n                <div rights></div>\n                <div class='form-group' deletecomponent></div>\n            </form>"
      });
      $builderProvider.registerComponent('document', {
        group: 'Bouton',
        tab: 'Bouton',
        label: 'Générer rapport',
        description: '',
        value: 'value',
        placeholder: 'placeholder',
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <button type=\"button\" class=\"btn btn-primary\">{{value}}</button>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-cloud-download\"></i>\n<span>Générer rapport</span>",
        popoverTemplate: "            <form>\n                <div class=\"form-group\">\n                    <label class='control-label'>ID</label>\n                    <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Label</label>\n                    <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Value</label>\n                    <input type='text' ng-model=\"value\" validator=\"[required]\" class='form-control'/>\n                </div>\n<div class=\"form-group\">\n                    <label class='control-label'>Type</label>\n                    <select class='form-control' ng-model=\"options[0]\" ng-options=\"option for option in ['docx','pdf']\"></select>\n                </div>\n                <hr/>\n                <div rights></div>\n                <div class='form-group' deletecomponent></div>\n            </form>"
      });
      $builderProvider.registerComponent('fileInput', {
        group: 'Bouton',
        tab: 'Bouton',
        label: 'File Input',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        validationOptions: [],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <input type=\"file\" ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\" id=\"{{formName+index}}\" class=\"form-control\" placeholder=\"{{placeholder}}\"/>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-cloud-upload\"></i>\n<span>File Input</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Placeholder</label>\n        <input type='text' ng-model=\"placeholder\" class='form-control'/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('fileimport', {
        group: 'Bouton',
        tab: 'Bouton',
        label: 'File Import',
        description: '',
        value: 'value',
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <button type=\"button\" class=\"btn btn-primary\">{{value}}</button>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-cloud-upload\"></i>\n<span>File Import</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Value</label>\n        <input type='text' ng-model=\"value\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('bardocument', {
        group: 'Bouton',
        tab: 'Bouton',
        label: 'Bar Document',
        options: ['document 1', 'document 2'],
        description: '',
        template: "<div class=\"form-group\"  style=\"width: 100%\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <table><tr><td ng-repeat=\"item in options track by $index\">\n            <button type=\"button\" class=\"btn btn-primary\">{{item}}</button>\n        </td></tr></table>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-folder-open\"></i>\n<span>Bar Document</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Options</label>\n        <textarea class=\"form-control\" rows=\"6\" ng-model=\"optionsText\"/>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('doublechecklist', {
        group: 'Composé',
        tab: 'Composé',
        label: 'Double Checklist',
        description: '',
        placeholder: 'placeholder',
        required: false,
        options: ['value one', 'value two'],
        options2: ['value three', 'value four'],
        onglet1: 'Onglet 1',
        onglet2: 'Onglet 2',
        arrayToText: true,
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <ul class=\"nav nav-tabs\">\n            <li class=\"active col-md-6\"><a class=\"row\" data-toggle=\"tab\" href=\"#onglet1\">{{onglet1}}</a></li>\n            <li class=\"col-md-6\"><a class=\"row\" data-toggle=\"tab\" href=\"#onglet2\">{{onglet2}}</a></li>\n        </ul>\n\n        <div class=\"tab-content\">\n            <div id=\"onglet1\" class=\"tab-pane fade in active col-md-6\">\n                <div class='checkbox' ng-repeat=\"item in options track by $index\">\n                    <label><input type='checkbox' value='item'/>\n                        {{item}}\n                    </label>\n                </div>\n            </div>\n            <div id=\"onglet2\" class=\"tab-pane fade in active col-md-6\">\n                <div class='checkbox' ng-repeat=\"item2 in options2 track by $index\">\n                    <label><input type='checkbox' value='item2'/>\n                        {{item2}}\n                    </label>\n                </div>\n            </div>\n            <div id=\"menu1\" class=\"tab-pane fade\">\n            </div>\n        </div>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-arrow-right\"></i>\n<span>Double Checklist</span>",
        popoverTemplate: "<form>\n     <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Onglet 1</label>\n        <input type='text' ng-model=\"onglet1\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Onglet 2</label>\n        <input type='text' ng-model=\"onglet2\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Options</label>\n        <textarea class=\"form-control\" rows=\"3\" ng-model=\"optionsText\"/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Options 2</label>\n        <textarea class=\"form-control\" rows=\"3\" ng-model=\"optionsText2\"/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required\n        </label>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('treechecklist', {
        group: 'Composé',
        tab: 'Composé',
        label: 'Tree Checklist',
        description: 'description',
        required: false,
        multple: false,
        treechecklistoptions: [
          {
            "level": 1,
            "title": "item1",
            "multple": false,
            "nodes": [
              {
                "level": 2,
                "title": "item1.1",
                "nodes": []
              }, {
                "level": 2,
                "title": "item1.2",
                "nodes": []
              }
            ]
          }, {
            "level": 1,
            "title": "item2",
            "multple": false,
            "nodes": [
              {
                "level": 2,
                "title": "item2.1",
                "nodes": []
              }, {
                "level": 2,
                "title": "item2.2",
                "nodes": []
              }
            ]
          }
        ],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <input type='hidden' ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\"/>\n        <div ng-repeat=\"item in treechecklistoptions track by $index\">\n            <div class='checkbox'>\n                <label>\n                    <input ng-if=\"multple\" type='checkbox' ng-model=\"$parent.inputArray[$index]\" value='item.title'/>\n                    <input ng-if=\"!multple\" type='radio' ng-model=\"$parent.inputArray[$index]\" value='item.title'/>\n                    {{item.title}}\n                </label>\n            </div>\n            <ul ng-if=\"item.nodes.length > 0\">\n                <li class=\"list-group-item\" ng-repeat=\"sub in item.nodes\">\n                    <div class='checkbox'>\n                        <label>\n                            <input ng-if=\"item.multple\" type='checkbox' ng-model=\"$parent.inputArray[$index]\" value='sub'/>\n                            <input ng-if=\"!item.multple\" type='radio' ng-model=\"$parent.inputArray[$index]\" value='sub'/>\n                            {{sub.title}}\n                        </label>\n                    </div>\n                </li>\n            </ul>\n        </div>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-tree-conifer\"></i>\n<span>Tree Checklist</span>",
        popoverTemplate: "<form>\n     <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Options</label>\n        <a class=\"pull-right btn btn-primary btn-xs\" data-nodrag ng-click=\"newSubItem('treechecklistoptions')\" style=\"margin-right: 8px;\"><span class=\"glyphicon glyphicon-plus\"></span></a>\n        <!-- Nested node template -->\n        <script type=\"text/ng-template\" id=\"nodes_renderer.html\">\n            <div ui-tree-handle class=\"tree-node tree-node-content\"  data-nodrag>\n              <div class=\"tree-node-content\">\n                <table class=\"tree-col-multiline\">\n                    <tr>\n                        <td>\n                            <a ng-if=\"node.level == 1\" class=\"btn btn-success btn-xs\" data-nodrag ng-click=\"toggle(this)\"><span class=\"glyphicon\" ng-class=\"{'glyphicon-chevron-right': collapsed, 'glyphicon-chevron-down': !collapsed}\"></span></a>\n                        </td>\n                        <td>\n                            <input type=\"text\" ng-model=\"node.title\" class=\"tree-input\">\n                             <div ng-if=\"node.level == 1\" class=\"checkbox\">\n                                <label>\n                                    <input type='checkbox' ng-model=\"node.multple\" />\n                                    Multiple\n                                </label>\n                            </div>\n                        </td>\n                        <td class=\"operations\">\n                            <a class=\"pull-right btn btn-danger btn-xs\" data-nodrag ng-click=\"removeElm('treechecklistoptions',lv1,lv2)\"><span class=\"glyphicon glyphicon-remove\"></span></a>\n                            <a ng-if=\"node.level == 1\" class=\"pull-right btn btn-primary btn-xs\" data-nodrag ng-click=\"newSubItem('treechecklistoptions',$index)\" style=\"margin-right: 8px;\"><span class=\"glyphicon glyphicon-plus\"></span></a>\n                        </td>\n                    </tr>\n                </table>\n              </div>\n            </div>\n            <ol ui-tree-nodes=\"\" ng-model=\"node.nodes\" ng-class=\"{hidden: collapsed}\">\n              <li ng-repeat=\"(lv2,node) in node.nodes\" ui-tree-node ng-include=\"'nodes_renderer.html'\">\n              </li>\n            </ol>\n        </script>\n        <div class=\"row\">\n            <div ui-tree>\n                <ol ui-tree-nodes=\"\" ng-model=\"treechecklistoptions\" id=\"tree-root\">\n                    <li ng-repeat=\"(lv1,node) in treechecklistoptions\" ui-tree-node ng-include=\"'nodes_renderer.html'\"></li>\n                </ol>\n            </div>\n        </div>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required\n        </label>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"multple\" />\n            Multiple\n        </label>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('user', {
        group: 'Entities',
        label: 'User',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        validationOptions: [],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <input type=\"text\" ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\" id=\"{{formName+index}}\" class=\"form-control\" placeholder=\"{{placeholder}}\"/>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-arrow-right\"></i>\n<span>User</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Placeholder</label>\n        <input type='text' ng-model=\"placeholder\" class='form-control'/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('group', {
        group: 'Entities',
        label: 'Group',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        validationOptions: [],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <input type=\"text\" ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\" id=\"{{formName+index}}\" class=\"form-control\" placeholder=\"{{placeholder}}\"/>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-arrow-right\"></i>\n<span>Group</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Placeholder</label>\n        <input type='text' ng-model=\"placeholder\" class='form-control'/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('process', {
        group: 'Workflow',
        label: 'Process',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        validationOptions: [],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <input type=\"text\" ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\" id=\"{{formName+index}}\" class=\"form-control\" placeholder=\"{{placeholder}}\"/>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-arrow-right\"></i>\n<span>Process</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Placeholder</label>\n        <input type='text' ng-model=\"placeholder\" class='form-control'/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('instance', {
        group: 'Workflow',
        label: 'Instance',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        validationOptions: [],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <input type=\"text\" ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\" id=\"{{formName+index}}\" class=\"form-control\" placeholder=\"{{placeholder}}\"/>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-arrow-right\"></i>\n<span>Instance</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Placeholder</label>\n        <input type='text' ng-model=\"placeholder\" class='form-control'/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('task', {
        group: 'Workflow',
        label: 'Task',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        validationOptions: [],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <input type=\"text\" ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\" id=\"{{formName+index}}\" class=\"form-control\" placeholder=\"{{placeholder}}\"/>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-arrow-right\"></i>\n<span>Task</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Placeholder</label>\n        <input type='text' ng-model=\"placeholder\" class='form-control'/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('modal', {
        group: 'Workflow',
        label: 'Modal',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        validationOptions: [],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <input type=\"text\" ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\" id=\"{{formName+index}}\" class=\"form-control\" placeholder=\"{{placeholder}}\"/>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-arrow-right\"></i>\n<span>Modal</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Placeholder</label>\n        <input type='text' ng-model=\"placeholder\" class='form-control'/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('form', {
        group: 'Workflow',
        label: 'Form',
        description: 'description',
        placeholder: 'placeholder',
        required: false,
        validationOptions: [],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <input type=\"text\" ng-model=\"inputText\" validator-required=\"{{required}}\" validator-group=\"{{formName}}\" id=\"{{formName+index}}\" class=\"form-control\" placeholder=\"{{placeholder}}\"/>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-arrow-right\"></i>\n<span>Form</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Placeholder</label>\n        <input type='text' ng-model=\"placeholder\" class='form-control'/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"required\" />\n            Required</label>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('multiline', {
        group: 'Composé',
        tab: 'Composé',
        label: 'MultiLine',
        description: '',
        options: [60, 60],
        arrayToText: true,
        multilinerows: [
          {
            "level": 1,
            "title": "row1",
            "nodes": [
              {
                "level": 2,
                "title": "row1.1",
                "nodes": []
              }, {
                "level": 2,
                "title": "row1.2",
                "nodes": []
              }
            ]
          }, {
            "level": 1,
            "title": "row2",
            "nodes": [
              {
                "level": 2,
                "title": "row2.1",
                "nodes": []
              }, {
                "level": 2,
                "title": "row2.2",
                "nodes": []
              }
            ]
          }
        ],
        multilinecols: [
          {
            "level": 1,
            "title": "col1",
            "input": "",
            "size": "",
            "nodes": [
              {
                "level": 2,
                "title": "col1.1",
                "input": "text",
                "size": "60",
                "nodes": []
              }, {
                "level": 2,
                "title": "col1.2",
                "input": "color",
                "size": "60",
                "nodes": []
              }
            ]
          }, {
            "level": 1,
            "title": "col2",
            "input": "",
            "size": "",
            "nodes": [
              {
                "level": 2,
                "title": "col2.1",
                "input": "checkbox",
                "size": "60",
                "nodes": []
              }, {
                "level": 2,
                "title": "col2.2",
                "input": "radio",
                "size": "60",
                "nodes": []
              }
            ]
          }
        ],
        template: "<div class=\"form-group\" style=\"width: 100%\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <table class=\"table table-bordered\">\n            <tr ng-repeat=\"elm in getTableMultiline(multilinecols, multilinerows, options) track by $index\">\n                <td ng-repeat=\"val in elm track by $index\" rowspan=\"{{val.rowspan}}\" colspan=\"{{val.colspan}}\" ng-class=\"{'cell-center' : !val.content}\"><p ng-if=\"val.content\" style=\"width:{{val.width}}px\">{{val.content}}</p><input ng-if=\"val.input\" type='{{val.input}}' ng-class=\"{'col-xs-12' : val.input == 'text'}\"/></td>\n            </tr>\n        </table>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-th\"></i>\n<span>MultiLine</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n            <label class='control-label'>Colonnes</label>\n        <a class=\"pull-right btn btn-primary btn-xs\" data-nodrag ng-click=\"newSubItem('multilinecols')\" style=\"margin-right: 8px;\"><span class=\"glyphicon glyphicon-plus\"></span></a>\n        <!-- Nested node template -->\n        <script type=\"text/ng-template\" id=\"nodes_renderer_col.html\">\n            <div ui-tree-handle class=\"tree-node tree-node-content\"  data-nodrag>\n              <div class=\"tree-node-content\">\n                <table class=\"tree-col-multiline\">\n                    <tr>\n                        <td>\n                            <a  ng-if=\"node.level == 1\" class=\"btn btn-success btn-xs\" data-nodrag ng-click=\"toggle(this)\"><span class=\"glyphicon\" ng-class=\"{'glyphicon-chevron-right': collapsed, 'glyphicon-chevron-down': !collapsed}\"></span></a>\n                        </td>\n                        <td>\n                            <input type=\"text\" ng-model=\"node.title\" class=\"tree-input\" placeholder=\"Nom\">\n                            <input type=\"text\" ng-model=\"node.size\" class=\"tree-input\" placeholder=\"Size\">\n                            <select class='tree-input' ng-model=\"node.input\" ng-options=\"type as type for type in inputtypes\"><option value=\"\">-- type input --</option></select>\n                        </td>\n                        <td class=\"operations\">\n                            <a class=\"pull-right btn btn-danger btn-xs\" data-nodrag ng-click=\"removeElm('multilinecols',lv1,lv2)\"><span class=\"glyphicon glyphicon-remove\"></span></a>\n                            <a ng-if=\"node.level == 1\" class=\"pull-right btn btn-primary btn-xs\" data-nodrag ng-click=\"newSubItem('multilinecols', $index)\" style=\"margin-right: 8px;\"><span class=\"glyphicon glyphicon-plus\"></span></a>\n                        </td>\n                    </tr>\n                </table>\n              </div>\n            </div>\n            <ol ui-tree-nodes=\"\" ng-model=\"node.nodes\" ng-class=\"{hidden: collapsed}\">\n              <li ng-repeat=\"(lv2,node) in node.nodes\" ui-tree-node ng-include=\"'nodes_renderer_col.html'\">\n              </li>\n            </ol>\n        </script>\n        <div class=\"row\">\n            <div ui-tree>\n                <ol ui-tree-nodes=\"\" ng-model=\"multilinecols\" id=\"tree-root\">\n                    <li ng-repeat=\"(lv1,node) in multilinecols\" ui-tree-node ng-include=\"'nodes_renderer_col.html'\"></li>\n                </ol>\n            </div>\n        </div>\n    </div>\n    <div class=\"form-group\">\n            <label class='control-label'>Lignes</label>\n        <a class=\"pull-right btn btn-primary btn-xs\" data-nodrag ng-click=\"newSubItem('multilinerows')\" style=\"margin-right: 8px;\"><span class=\"glyphicon glyphicon-plus\"></span></a>\n        <!-- Nested node template -->\n        <script type=\"text/ng-template\" id=\"nodes_renderer_row.html\">\n            <div ui-tree-handle class=\"tree-node tree-node-content\"  data-nodrag>\n              <div class=\"tree-node-content\">\n                <a  ng-if=\"node.level == 1\" class=\"btn btn-success btn-xs\" data-nodrag ng-click=\"toggle(this)\"><span class=\"glyphicon\" ng-class=\"{'glyphicon-chevron-right': collapsed, 'glyphicon-chevron-down': !collapsed}\"></span></a>\n                <input type=\"text\" ng-model=\"node.title\" class=\"tree-input\" placeholder=\"Nom\">\n                <a class=\"pull-right btn btn-danger btn-xs\" data-nodrag ng-click=\"removeElm('multilinerows',lv1,lv2)\"><span class=\"glyphicon glyphicon-remove\"></span></a>\n                <a ng-if=\"node.level == 1\" class=\"pull-right btn btn-primary btn-xs\" data-nodrag ng-click=\"newSubItem('multilinerows', $index)\" style=\"margin-right: 8px;\"><span class=\"glyphicon glyphicon-plus\"></span></a>\n              </div>\n            </div>\n            <ol ui-tree-nodes=\"\" ng-model=\"node.nodes\" ng-class=\"{hidden: collapsed}\">\n              <li ng-repeat=\"(lv2,node) in node.nodes\" ui-tree-node ng-include=\"'nodes_renderer_row.html'\">\n              </li>\n            </ol>\n        </script>\n        <div class=\"row\">\n            <div ui-tree>\n                <ol ui-tree-nodes=\"\" ng-model=\"multilinerows\" id=\"tree-root\">\n                    <li ng-repeat=\"(lv1,node) in multilinerows\" ui-tree-node ng-include=\"'nodes_renderer_row.html'\"></li>\n                </ol>\n            </div>\n        </div>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Taille Des Lignes</label>\n        <input type='text' ng-model=\"options[0]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Taille Des Sous Lignes</label>\n        <input type='text' ng-model=\"options[1]\" class='form-control'/>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('multilineentity', {
        group: 'Composé',
        tab: 'Composé',
        label: 'MultiLine Entity',
        description: '',
        placeholder: 'placeholder',
        value: null,
        allowImport: false,
        allowExport: false,
        allowSearch: false,
        template: "<div class=\"form-group\" style=\"width: 100%\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <table class=\"table table-bordered\">\n            <tr>\n                <th></th>\n                <th ng-repeat=\"element in options2 track by $index\"><p style=\"width:{{options3[$index]}}px\">{{entity[value].fields[element]}}</p></th>\n                <th ng-if=\"lastcolumn[0].length\" class=\"col-sm-2\">{{lastcolumn[0]}}</th>\n            </tr>\n        </table>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-th\"></i>\n<span>MultiLine Entity</span>",
        popoverTemplate: "            <form>\n                <div class=\"form-group\">\n                    <label class='control-label'>ID</label>\n                    <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Label</label>\n                    <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Entité</label>\n                    <select class='form-control' ng-model=\"value\" ng-change=\"options2 = []; options3 = [];entityGroupRights = [];entityUserRights = [];entityGroupWriteRights = [];entityUserWriteRights = [];entityFilter = [];colProcess = [];colTaches = [];getColTachesByProcess();popover.refreshmultiselect('multilinecol-select')\">\n                        <option ng-repeat=\"(key, option) in entity track by $index\" ng-value=\"key\" ng-selected=\"value == key\">{{option.name}}</option>\n                    </select>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Colonnes</label>\n                    <select multiple class='form-control multilinecol-select' ng-model=\"options2\"  ng-init=\"old = options2\" ng-change=\"options3 = getArrayByOPtions(old,options2,options3);entityUserRights = getArrayByOPtions(old,options2,entityUserRights);entityGroupRights = getArrayByOPtions(old,options2,entityGroupRights);entityUserWriteRights = getArrayByOPtions(old,options2,entityUserWriteRights);entityGroupWriteRights = getArrayByOPtions(old,options2,entityGroupWriteRights);entityFilter = getArrayByOPtions(old,options2,entityFilter);colProcess = getArrayByOPtions(old,options2,colProcess);colTaches = getArrayByOPtions(old,options2,colTaches);getColTachesByProcess();popover.setmultiselect('col-right-user','refresh');popover.setmultiselect('col-right-group','refresh');options2 = orderOptions(old,options2); old = options2\"\n			ng-options=\"key as value for (key , value) in entity[value].fields\">\n                    </select>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Dernière Colonne</label>\n                    <input type='text' ng-model=\"lastcolumn[0]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Formule Dernière Colonne</label>\n                    <input type='text' ng-model=\"explastcolumn[0]\" class='form-control'/>\n                </div>\n	<div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox' ng-model=\"useRequest\" ng-checked=\"useRequest\"/>\n                        Utiliser la requête\n                    </label>\n                </div>\n	<div class=\"form-group\">\n                    <label class='control-label'>Requête</label>\n                    <textarea class=\"form-control\" rows=\"5\" ng-model=\"databaseRequest\"/>\n                </div>\n                <div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox' ng-model=\"allowImport\" ng-checked=\"allowImport\"/>\n                        Activer l'import\n                    </label>\n                </div>\n                <div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox' ng-model=\"allowExport\" ng-checked=\"allowExport\"/>\n                        Activer l'export\n                    </label>\n                </div>\n                <div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox' ng-model=\"allowSearch\" ng-checked=\"allowSearch\"/>\n                        Activer la recherche\n                    </label>\n                </div>\n                <div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox' ng-model=\"allowFilter\" ng-checked=\"allowFilter\"/>\n                        Activer le filtre\n                    </label>\n                </div>\n                <hr/>\n                <div rights></div>\n                <div class=\"form-group\">\n                    <input type='button' ng-init=\"attributs = false\" ng-click=\"attributs = !attributs\" class='btn btn-primary btn-block' value='Attributes'/>\n                </div>\n                <div ng-show=\"attributs\" class=\"modall\">\n                    <i class=\"glyphicon glyphicon-remove-circle\" ng-click=\"attributs = !attributs\"></i>\n                    <hr/>\n                    <div class=\"form-group\">\n                        <table class=\"table table-bordered\">\n                            <tr><th class=\"col-md-8\">Colonne</th><th class=\"text-center col-md-2\">Taille</th><th class=\"text-center col-md-2\">Filtre</th></tr>\n                            <tr ng-repeat=\"element in options2 track by $index\">\n                                <td>{{entity[value].fields[element]}}</td>\n                                <td class=\"text-center\"><span>&nbsp;{{options3[$index]}}</span><input type=\"range\" min=\"0\" max=\"500\"  ng-model=\"options3[$index]\" ng-init=\"options3[$index] = options3[$index] || ''\" /></td>\n                                <td class=\"text-center\"><input type='text' ng-model=\"entityFilter[$index]\" class='form-control' ng-init=\"entityFilter[$index] = entityFilter[$index] || ''\"/></td>\n                            </tr>\n                        </table>\n                    </div>\n                </div>\n                <hr/>\n                <div class=\"form-group\">\n                    <input type='button' ng-init=\"columnsrights = false\" ng-click=\"columnsrights = !columnsrights\" class='btn btn-primary btn-block' value='Droits par Colonnes'/>\n                </div>\n                <div ng-show=\"columnsrights\" class=\"modall\">\n                    <i class=\"glyphicon glyphicon-remove-circle\" ng-click=\"columnsrights = !columnsrights\"></i>\n                    <hr/>\n                    <select ng-options=\"element as entity[value].fields[element] for element in options2\" class=\"form-control\" ng-model=\"selectedColumn\" />\n                    <hr/>\n                    <div class=\"btn-group\" data-toggle=\"buttons\">\n<label class=\"btn btn-success\" ng-click=\"typeRights = 'task'\">\n	<input type=\"radio\">Tâches\n</label>\n<label class=\"btn btn-success\" ng-click=\"typeRights = 'group'\">\n	<input type=\"radio\">Groupes\n</label>\n<label class=\"btn btn-success\" ng-click=\"typeRights = 'user'\">\n	<input type=\"radio\">Utilisateurs\n</label>\n                    </div>\n                    <hr/>\n                    <div ng-repeat=\"element in options2 track by $index\" ng-init=\"index = $index\">\n                        <div ng-show=\"element == selectedColumn\">\n                            <div ng-show=\"typeRights == 'task'\" class=\"form-group col-sm-6\">\n                                <table class=\"table table-bordered\" ng-init=\"colProcess[index] = colProcess[index] || []\">\n                                    <tr><th class=\"col-md-10\">Process</th><th class=\"text-center col-md-1\">Selectionner</th></tr>\n                                    <tr ng-repeat=\"(key,element) in listTaches\">\n                                        <td>{{element.name}}</td>\n                                        <td class=\"text-center\"><input type='checkbox' ng-checked=\"exists(key, colProcess[index])\" ng-click=\"toggle(key, colProcess[index]); getColTachesByProcess();\"/></td>\n                                    </tr>\n                                </table>\n                            </div>\n                            <div ng-show=\"typeRights == 'task'\" class=\"form-group col-sm-6\">\n                                <table class=\"table table-bordered\" ng-init=\"colTaches[index] = colTaches[index] || []\">\n                                    <tr><th class=\"col-md-10\">Tâches</th><th class=\"text-center col-md-1\">Selectionner</th></tr>\n                                    <tr ng-repeat=\"element in mergeObjects(colProcess[index], 'colProcess', index)\">\n                                        <td>{{element.name}}</td>\n                                        <td class=\"text-center\"><input type='checkbox' ng-checked=\"exists(element.id, colTaches[index])\" ng-click=\"toggle(element.id, colTaches[index]); getColTachesByProcess();\"/></td>\n                                    </tr>\n                                </table>\n                            </div>\n                            <div ng-show=\"typeRights == 'group'\" class=\"form-group\">\n                                <table class=\"table table-bordered\" ng-init=\"entityGroupRights[index] = entityGroupRights[index] || []; entityGroupWriteRights[index] = entityGroupWriteRights[index] || [];\">\n                                    <tr><th class=\"col-md-10\">Groupes</th><th class=\"text-center col-md-1\">Lecture</th><th class=\"text-center col-md-1\">Ecriture</th></tr>\n                                    <tr ng-repeat=\"group in listGroups\">\n                                        <td>{{group.name}}</td>\n                                        <td class=\"text-center\"><input type='checkbox' ng-checked=\"exists(group.id, entityGroupRights[index])\" ng-click=\"toggle(group.id, entityGroupRights[index])\"/></td>\n                                        <td class=\"text-center\"><input type='checkbox' ng-checked=\"exists(group.id, entityGroupWriteRights[index])\" ng-click=\"toggle(group.id, entityGroupWriteRights[index])\"/></td>\n                                    </tr>\n                                </table>\n                            </div>\n                            <div ng-show=\"typeRights == 'user'\" class=\"form-group\">\n                                <table class=\"table table-bordered\" ng-init=\"entityUserRights[index] = entityUserRights[index] || []; entityUserWriteRights[index] = entityUserWriteRights[index] || [];\">\n                                    <tr><th class=\"col-md-10\">Utilisateurs</th><th class=\"text-center col-md-1\">Lecture</th><th class=\"text-center col-md-1\">Ecriture</th></tr>\n                                    <tr ng-repeat=\"user in listUsers\">\n                                        <td>{{user.firstName}} {{user.lastName}}</td>\n                                        <td class=\"text-center\"><input type='checkbox' ng-checked=\"exists(user.id, entityUserRights[index])\" ng-click=\"toggle(user.id, entityUserRights[index])\"/></td>\n                                        <td class=\"text-center\"><input type='checkbox' ng-checked=\"exists(user.id, entityUserWriteRights[index])\" ng-click=\"toggle(user.id, entityUserWriteRights[index])\"/></td>\n                                    </tr>\n                                </table>\n                            </div>\n                        </div>\n                    </div>\n                </div>\n                <hr/>\n                <div class='form-group' deletecomponent></div>\n            </form>"
      });
      $builderProvider.registerComponent('multilineentityconfig', {
        group: 'Composé',
        tab: 'Composé',
        label: 'MultiLine Entity Config',
        description: '',
        placeholder: 'placeholder',
        value: null,
        allowImport: false,
        allowExport: false,
        allowSearch: false,
        template: "<div class=\"form-group\" style=\"width: 100%\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <table class=\"table table-bordered\">\n            <tr>\n                <th></th>\n                <th ng-repeat=\"element in options2 track by $index\"><p style=\"width:{{options3[$index]}}px\">{{entity[value].fields[element]}}</p></th>\n                <th ng-if=\"lastcolumn[0].length\" class=\"col-sm-2\">{{lastcolumn[0]}}</th>\n            </tr>\n        </table>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-th\"></i>\n<span>MultiLine Entity Config</span>",
        popoverTemplate: "            <form>\n                <div class=\"form-group\">\n                    <label class='control-label'>ID</label>\n                    <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Label</label>\n                    <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Entité</label>\n                    <select class='form-control' ng-model=\"value\" ng-change=\"options2 = []; options3 = [];entityGroupRights = [];entityUserRights = [];entityGroupWriteRights = [];entityUserWriteRights = [];entityFilter = [];popover.refreshmultiselect('multilinecol-select')\">\n                        <option ng-repeat=\"(key, option) in entity track by $index\" ng-value=\"key\" ng-selected=\"value == key\">{{option.name}}</option>\n                    </select>\n                </div>\n                 <div class=\"form-group\">\n                    <label class='control-label'>Colonnes</label>\n                    <select multiple class='form-control multilinecol-select' ng-model=\"options2\"  ng-init=\"old = options2\" ng-change=\"options3 = getArrayByOPtions(old,options2,options3);entityUserRights = getArrayByOPtions(old,options2,entityUserRights);entityGroupRights = getArrayByOPtions(old,options2,entityGroupRights);entityUserWriteRights = getArrayByOPtions(old,options2,entityUserWriteRights);entityGroupWriteRights = getArrayByOPtions(old,options2,entityGroupWriteRights);entityFilter = getArrayByOPtions(old,options2,entityFilter);popover.setmultiselect('col-right-user','refresh');popover.setmultiselect('col-right-group','refresh');options2 = orderOptions(old,options2); old = options2\"\n                        ng-options=\"key as value for (key , value) in entity[value].fields\">\n                    </select>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Dernière Colonne</label>\n                    <input type='text' ng-model=\"lastcolumn[0]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Formule Dernière Colonne</label>\n                    <input type='text' ng-model=\"explastcolumn[0]\" class='form-control'/>\n                </div>\n<div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox' ng-model=\"useRequest\" ng-checked=\"useRequest\"/>\n                        Utiliser la requête\n                    </label>\n                </div>\n<div class=\"form-group\">\n                    <label class='control-label'>Requête</label>\n                    <textarea class=\"form-control\" rows=\"5\" ng-model=\"databaseRequest\"/>\n                </div>\n<div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox' ng-model=\"allowImport\" ng-checked=\"allowImport\"/>\n                        Activer l'import\n                    </label>\n                </div>\n                <div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox' ng-model=\"allowExport\" ng-checked=\"allowExport\"/>\n                        Activer l'export\n                    </label>\n                </div>\n                <div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox' ng-model=\"allowSearch\" ng-checked=\"allowSearch\"/>\n                        Activer la recherche\n                    </label>\n                </div>\n                <div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox' ng-model=\"allowCreate\" ng-checked=\"allowCreate\"/>\n                        Activer la création\n                    </label>\n                </div>\n                <div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox' ng-model=\"allowUpdate\" ng-checked=\"allowUpdate\"/>\n                        Activer la modification\n                    </label>\n                </div>\n                <div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox' ng-model=\"allowDelete\" ng-checked=\"allowDelete\"/>\n                        Activer la suppression\n                    </label>\n                </div>\n                <hr/>\n                <div class=\"form-group\">\n                    <input type='button' ng-init=\"columnssizes = false\" ng-click=\"columnssizes = !columnssizes\" class='btn btn-primary btn-block' value='Taille Des Colonnes'/>\n                </div>\n                <div class=\"form-group\"  ng-show=\"columnssizes\">\n                    <div ng-repeat=\"element in options2 track by $index\" >\n                        <span>{{entity[value].fields[element]}}</span>\n                        <input type='text' ng-model=\"options3[$index]\" class='form-control' ng-init=\"options3[$index] = options3[$index] || ''\"/>\n                    </div>\n                </div>\n                <hr/>\n                <div class=\"form-group\">\n                    <input type='button' ng-init=\"columnsfilter = false\" ng-click=\"columnsfilter = !columnsfilter\" class='btn btn-primary btn-block' value='Filtre par Colonnes'/>\n                </div>\n                <div class=\"form-group\"  ng-show=\"columnsfilter\">\n                    <div ng-repeat=\"element in options2 track by $index\" >\n                        <span>{{entity[value].fields[element]}}</span>\n                        <input type='text' ng-model=\"entityFilter[$index]\" class='form-control' ng-init=\"entityFilter[$index] = entityFilter[$index] || ''\"/>\n                    </div>\n                </div>\n                <hr/>\n                <div class='form-group' deletecomponent></div>\n            </form>"
      });
      $builderProvider.registerComponent('conditionalselect', {
        group: 'Composé',
        tab: 'Composé',
        label: 'Conditional Select',
        options: ['option one', 'option two'],
        options2: ['default option'],
        value: false,
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <div class='checkbox'>\n            <input type='checkbox' ng-checked=\"value\"/>\n        </div>\n    </div>\n</div>\n<div class=\"form-group\">\n    <label class=\"col-sm-4 control-label\"></label>\n    <div class=\"col-sm-8\">\n        <select ng-show=\"!value\" ng-options=\"value for value in options\" class=\"form-control\"\n            ng-model=\"inputText\" ng-init=\"inputText = options[0]\"/>\n        <span ng-show=\"value\">{{options2[0]}}</span>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-arrow-right\"></i>\n<span>Conditional Select</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"value\" ng-checked=\"value\"/>\n            Default value\n        </label>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Options No Checked</label>\n        <textarea class=\"form-control\" rows=\"3\" ng-model=\"optionsText\"/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Option Checked</label>\n        <input type='text' ng-model=\"options2[0]\"  class='form-control'/>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('template', {
        group: 'Composé',
        tab: 'Composé',
        label: 'Template',
        template: "<div ng-if=\"value\" class=\"no-template\"><i class=\"glyphicon glyphicon-ok\"></i> {{templates[value].name}}</div>\n<div ng-if=\"!value\" class=\"no-template\"><i class=\"glyphicon glyphicon-remove\"></i> No Template</div>",
        icon: "<i class=\"glyphicon glyphicon-arrow-right\"></i>\n<span>Template</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Template</label>\n        <select class='form-control' ng-model=\"value\">\n            <option ng-repeat=\"(key, option) in templates track by $index\" ng-value=\"key\" ng-selected=\"value == key\">{{option.name}}</option>\n        </select>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('fform', {
        group: 'Composé',
        tab: 'Composé',
        label: 'Form',
        options3: [false],
        template: "<div ng-if=\"forms[value]\" class=\"no-template\"><i class=\"glyphicon glyphicon-ok\"></i> {{forms[value]}}</div>\n<div ng-if=\"!forms[value]\" class=\"no-template\"><i class=\"glyphicon glyphicon-remove\"></i> No Form</div>",
        icon: "<i class=\"glyphicon glyphicon-arrow-right\"></i>\n<span>Form</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Form</label>\n        <select class='form-control' ng-model=\"value\">\n            <option ng-repeat=\"(key, option) in forms track by $index\" ng-value=\"key\" ng-selected=\"value == key\">{{option}}</option>\n        </select>\n    </div>\n    <div class=\"checkbox\">\n        <label>\n            <input type='checkbox' ng-model=\"options3[0]\" />\n            Reprendre les droits du formulaire</label>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('entity', {
        group: 'Composé',
        tab: 'Composé',
        label: 'Entity',
        description: 'description',
        options: [false, false, 'Select'],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <div ng-if=\"options[2] == 'Checkbox' && entity[options[0]].fields[options[1]]\" class='checkbox' ng-repeat=\"item in [1,2,3] track by $index\">\n            <label><input type='checkbox' value='item'/>\n                {{entity[options[0]].name}}.{{entity[options[0]].fields[options[1]]}}.{{item}}\n            </label>\n        </div>\n        <select ng-if=\"options[2] == 'Select'\" class='form-control'>\n            <option ng-if=\"entity[options[0]].fields[options[1]]\">{{entity[options[0]].name}}.{{entity[options[0]].fields[options[1]]}}.1</option>\n        </select>\n        <input ng-if=\"options[2] == 'Auto Suggest'\" type=\"text\" class=\"form-control\"\"/>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-arrow-right\"></i>\n<span>Entity</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Entité</label>\n        <select class='form-control' ng-model=\"options[0]\" ng-change=\"options[1] = no;\">\n            <option ng-repeat=\"(key, option) in entity track by $index\" ng-value=\"key\" ng-selected=\"options[0] == key\">{{option.name}}</option>\n        </select>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Colonne</label>\n        <select class='form-control' ng-model=\"options[1]\">\n            <option ng-repeat=\"(key,element) in entity[options[0]].fields\" ng-value=\"key\" ng-selected=\"options[1] == key\">{{element}}</option>\n        </select>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Type Input</label>\n        <select class='form-control' ng-model=\"options[2]\" ng-options=\"option for option in ['Select','Checkbox','Auto Suggest']\"></select>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('list', {
        group: 'Composé',
        tab: 'Composé',
        label: 'Liste',
        description: 'description',
        options: [false, 'Select'],
        template: "<div class=\"form-group\" style=\"width: 100%\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <div ng-if=\"options[1] == 'Checkbox'\" class='checkbox' ng-repeat=\"item in list[options[0]].fields track by $index\">\n            <label><input type='checkbox' value='item'/>\n                {{item}}\n            </label>\n        </div>\n        <select ng-if=\"options[1] == 'Select'\" class='form-control'>\n            <option ng-repeat=\"option in list[options[0]].fields\">{{option}}</option>\n        </select>\n        <input ng-if=\"options[1] == 'Auto Suggest'\" type=\"text\" class=\"form-control\"\"/>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-arrow-right\"></i>\n<span>Liste</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Liste</label>\n        <select class='form-control' ng-model=\"options[0]\">\n            <option ng-repeat=\"(key, option) in list track by $index\" ng-value=\"key\" ng-selected=\"value == key\">{{option.name}}</option>\n        </select>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Type Input</label>\n        <select class='form-control' ng-model=\"options[1]\" ng-options=\"option for option in ['Select','Checkbox','Auto Suggest']\"></select>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Description</label>\n        <input type='text' ng-model=\"description\" class='form-control'/>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('map', {
        group: 'Composé',
        tab: 'Composé',
        label: 'Map',
        options: ['11', '40.4', '-3.7'],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <img ng-src=\"{{getSrcMap(options[0],options[1],options[2])}}\"></div>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-map-marker\"></i>\n<span>Map</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Zoom</label>\n        <select class='form-control' ng-model=\"options[0]\" ng-options=\"option for option in ['1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20']\"></select>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Latitude</label>\n        <input type='text' ng-model=\"options[1]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Longitude</label>\n        <input type='text' ng-model=\"options[2]\" class='form-control'/>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('addentity', {
        group: 'Composé',
        tab: 'Composé',
        label: 'Add Entity',
        description: '',
        value: null,
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <input ng-repeat=\"element in options\" type='text' class='form-control' placeholder='{{entity[value].fields[element]}}' style=\"margin-top: 5px;\"/>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-plus\"></i>\n<span>Add Entity</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Entité</label>\n        <select class='form-control' ng-model=\"value\" ng-change=\"options = [];popover.refreshmultiselect('multilinecol-select')\">\n            <option ng-repeat=\"(key, option) in entity track by $index\" ng-value=\"key\" ng-selected=\"value == key\">{{option.name}}</option>\n        </select>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Colonnes</label>\n        <select multiple class='form-control multilinecol-select' ng-model=\"options\" ng-init=\"old = options\" ng-change=\"options = orderOptions(old,options); old = options\">\n            <option ng-repeat=\"(key,element) in entity[value].fields\" ng-value=\"key\" ng-selected=\"inArray(key,options)\">{{element}}</option>\n        </select>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('imagezone', {
        group: 'Composé',
        tab: 'Composé',
        label: 'Image Zone',
        options: ['80', '400'],
        template: "<div class=\"form-group\"  style=\"width: 100%\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <div ng-if=\"!value\" class=\"no-template\" style=\"width:{{options[1]}}px;height:{{options[0]}}px;background-image: url('dist/img/imageb.png');background-repeat: repeat;\"></div>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-picture\"></i>\n<span>Image Zone</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Height</label>\n        <input type='text' ng-model=\"options[0]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Width</label>\n        <input type='text' ng-model=\"options[1]\" class='form-control'/>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('registre', {
        group: 'Composé',
        tab: 'Composé',
        label: 'Registre',
        value: 'prefix',
        options3: ['suffix'],
        options: ['Num_Auto'],
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <span class=\"col-sm-8\" style=\"padding-top: 7px;\">{{value}}_({{options[0]}})_{{options3[0]}}</span>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-arrow-right\"></i>\n<span>Registre</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Label</label>\n        <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Prefix</label>\n        <input type='text' ng-model=\"value\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Suffix</label>\n        <input type='text' ng-model=\"options3[0]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Numero</label>\n        <select class='form-control' ng-model=\"options[0]\" ng-options=\"option for option in ['Num_Auto','Num_Auto_Increment','Chaine_Auto','TimeStimp','Num_Instance','Num_Tache']\"></select>\n    </div>\n    <div class=\"form-group\" ng-show=\"options[0] == 'Num_Auto_Increment'\">\n        <label class='control-label'>Registre</label>\n        <select class='form-control' ng-model=\"options2[0]\">\n            <option ng-repeat=\"(key, option) in registres track by $index\" ng-value=\"key\" ng-selected=\"options2[0] == key\">{{option}}</option>\n        </select>\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      $builderProvider.registerComponent('header', {
        group: 'Composé',
        tab: 'Composé',
        label: 'Header',
        multple: 'true',
        template: "<div>\n    <div class=\"col\"><h5 ng-show=\"multple == 'false'\">{{value}}</h5><h4 ng-show=\"multple == 'true'\">{{value}}</h4></div>\n    <div ng-show=\"multple == 'false'\" class=\"col\" style=\"margin-bottom: 20px;border-top: 1px solid;\"></div>\n    <div ng-show=\"multple == 'true'\" class=\"col\" style=\"margin-bottom: 20px;border-top: 2px solid;\"></div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-arrow-right\"></i>\n<span>Header</span>",
        popoverTemplate: "<form>\n    <div class=\"form-group\">\n        <label class='control-label'>ID</label>\n        <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Text</label>\n        <input type='text' ng-model=\"value\" class='form-control'/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Niveau</label>\n        <br><input type=\"radio\" ng-model=\"multple\" name=\"multiple\" value=true /> Niveau 1\n        <br><input type=\"radio\" ng-model=\"multple\" name=\"multiple\" value=false /> Niveau 2\n    </div>\n    <hr/>\n    <div rights></div>\n    <div class='form-group' deletecomponent></div>\n</form>"
      });
      return $builderProvider.registerComponent('picklistidentity', {
        group: 'Select',
        tab: 'picklistidentity',
        label: 'PickList Identity',
        description: 'description',
        value: null,
        template: "<div class=\"form-group\">\n    <label for=\"{{formName+index}}\" class=\"col-sm-4 control-label\" ng-class=\"{'fb-required':required}\">{{label}}</label>\n    <div class=\"col-sm-8\">\n        <div class=\"ms-container\">\n            <div class=\"ms-selectable\">\n                <ul class=\"ms-list\">\n                    <li ng-if=\"value == 'group'\" class=\"ms-elem-selectable\" ng-repeat=\"val in options track by $index\"><span ng-repeat=\"element in listGroups\" ng-if=\"element.id == val\">{{element.name}}</span></li>\n                    <li ng-if=\"value == 'user'\" class=\"ms-elem-selectable\" ng-repeat=\"val in options track by $index\"><span ng-repeat=\"element in listUsers\" ng-if=\"element.id == val\">{{element.firstName}} {{element.lastName}}</span></li>\n                </ul></div><div class=\"ms-selection\">\n                <ul class=\"ms-list\" tabindex=\"-1\"></ul>\n            </div>\n        </div>\n        <p class='help-block'>{{description}}</p>\n    </div>\n</div>",
        icon: "<i class=\"glyphicon glyphicon-pause\"></i>\n<span>PickList Identity</span>",
        popoverTemplate: "            <form>\n                <div class=\"form-group\">\n                    <label class='control-label'>ID</label>\n                    <input type='text' ng-model=\"id\" validator=\"[required]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Label</label>\n                    <input type='text' ng-model=\"label\" validator=\"[required]\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Description</label>\n                    <input type='text' ng-model=\"description\" class='form-control'/>\n                </div>\n                <div class=\"form-group\">\n                    <label class='control-label'>Type</label>\n                    <select class='form-control' ng-model=\"value\" ng-change=\"options = [];popover.refreshmultiselect('picklistidentity-select')\">\n                        <option ng-repeat=\"(key, option) in identitytypes track by $index\" ng-value=\"key\" ng-selected=\"value == key\">{{option}}</option>\n                    </select>\n                </div>\n                <div ng-show=\"value == 'group' || value == 'user'\" class=\"form-group\">\n                    <select multiple class='form-control picklistidentity-select' ng-model=\"options\"  ng-init=\"old = options\" ng-change=\"options = orderOptions(old,options); old = options\">\n                        <option ng-show=\"value == 'group'\" ng-repeat=\"element in listGroups\" ng-value=\"element.id\" ng-selected=\"inArray(element.id,options)\">{{element.name}}</option>\n                        <option ng-show=\"value == 'user'\" ng-repeat=\"element in listUsers\" ng-value=\"element.id\" ng-selected=\"inArray(element.id,options)\">{{element.firstName}} {{element.lastName}}</option>\n                    </select>\n                </div>\n<div ng-show=\"value == 'userGroup'\" class=\"form-group\">\n	<select class='form-control' ng-model=\"options[0]\"\n		ng-options=\"element.id as element.name for element in listGroups\">\n	</select>\n</div>\n<div class=\"form-group\">\n                    <label class='control-label'>Nombre maximum à selectionner</label>\n                    <input type='text' ng-model=\"maxSelect\" class='form-control'/>\n                </div>\n <div class=\"checkbox\">\n                    <label>\n                        <input type='checkbox' ng-model=\"required\" />\n                        Required</label>\n                </div>\n                <hr/>\n                <div rights></div>\n                <div class='form-group' deletecomponent></div>\n            </form>"
      });
    }
  ]);

}).call(this);
