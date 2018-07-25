(function() {
  var copyObjectToScope,
    __indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; };

  copyObjectToScope = function(object, scope) {

    /*
       Copy object (ng-repeat="object in objects") to scope without `hashKey`.
     */
    var key, value;
    for (key in object) {
      value = object[key];
      if (key !== '$$hashKey') {
        scope[key] = value;
      }
    }
  };

  angular.module('builder.controller', ['builder.provider']).controller('fbFormObjectEditableController', [
    '$scope', '$injector', '$http', '$rootScope', '$filter', function($scope, $injector, $http, $rootScope, $filter) {
      var $builder;
      $builder = $injector.get('$builder');
      $scope.setupScope = function(formObject) {

        /*
              1. Copy origin formObject (ng-repeat="object in formObjects") to scope.
              2. Setup optionsText with formObject.options.
              3. Watch scope.label, .description, .placeholder, .required, .options then copy to origin formObject.
              4. Watch scope.optionsText then convert to scope.options.
              5. setup validationOptions
         */
        var colElm, component, elm, index, key, val, _i, _j, _k, _len, _len1, _len2, _ref, _ref1;
        copyObjectToScope(formObject, $scope);
        $scope.optionsText = formObject.options.join('\n');
        $scope.optionsText2 = formObject.options2.join('\n');
        $scope.subrowsText = formObject.subrows.join('\n');
        $scope.subcolumnsText = formObject.subcolumns.join('\n');
        $scope.numbersubrowsText = formObject.numbersubrows.join('\n');
        $scope.numbersubcolumnsText = formObject.numbersubcolumns.join('\n');
        $scope.widthsubcolumnsText = formObject.widthsubcolumns.join('\n');
        $scope.process = (function() {
          var _results;
          _results = [];
          for (key in $scope.tachesbyprocess) {
            _results.push(key);
          }
          return _results;
        })();
        $scope.taches = [];
        _ref = $scope.tachesbyprocess;
        for (key in _ref) {
          val = _ref[key];
          for (_i = 0, _len = val.length; _i < _len; _i++) {
            elm = val[_i];
            $scope.taches.push(key + "-" + elm);
          }
        }
        $scope.colProcess = [];
        $scope.colTaches = [];
        _ref1 = $scope.colTachesbyprocess;
        for (index = _j = 0, _len1 = _ref1.length; _j < _len1; index = ++_j) {
          colElm = _ref1[index];
          $scope.colProcess[index] = (function() {
            var _results;
            _results = [];
            for (key in colElm) {
              _results.push(key);
            }
            return _results;
          })();
          $scope.colTaches[index] = [];
          for (key in colElm) {
            val = colElm[key];
            for (_k = 0, _len2 = val.length; _k < _len2; _k++) {
              elm = val[_k];
              $scope.colTaches[index].push(key + "-" + elm);
            }
          }
        }
        $scope.entity = $rootScope.entity;
        $scope.list = $rootScope.list;
        $scope.dateformatOptions = ['Date', 'Time', 'DateTime'];
        $scope.datePatterns = {
          'Date': ['dd/MM/yyyy', 'MM-dd-yyyy'],
          'Time': ['HH:mm', 'HH:mm:ss'],
          'DateTime': ['dd/MM/yyyy HH:mm', 'dd/MM/yyyy HH:mm:ss', 'MM-dd-yyyy HH:mm', 'MM-dd-yyyy HH:mm:ss']
        };
        $scope.inputtypes = ['text', 'radio', 'checkbox', 'color'];
        $scope.identitytypes = {
          "user": "Utilisateur",
          "group": "Groupe",
          "userGroup": "Utilisateurs du groupe"
        };
        $scope.doctypes = {
          "docx": "docx",
          "pdf": "pdf"
        };
        $scope.listUsers = $rootScope.listUsers;
        $scope.listGroups = $rootScope.listGroups;
        $scope.listForms = {
          'Module 1': {
            'process 1.1': {
              '1.1.1': 'form 1.1.1',
              '1.1.2': 'form 1.1.2',
              '1.1.3': 'form 1.1.3',
              '1.1.4': 'form 1.1.4'
            },
            'process 1.2': {
              '1.2.1': 'form 1.2.1',
              '1.2.2': 'form 1.2.2',
              '1.2.3': 'form 1.2.3'
            },
            'process 1.3': {
              '1.3.1': 'form 1.3.1',
              '1.3.2': 'form 1.3.2',
              '1.3.3': 'form 1.3.3'
            }
          },
          'Module 2': {
            'process 2.1': {
              '2.1.1': 'form 2.1.1',
              '2.1.2': 'form 2.1.2'
            },
            'process 2.2': {
              '2.2.1': 'form 2.2.1',
              '2.2.2': 'form 2.2.2'
            },
            'process 2.3': {
              '2.3.1': 'form 2.3.1',
              '2.3.2': 'form 2.3.2'
            }
          },
          'Module 3': {
            'process 3.1': {
              '3.1.1': 'form 3.1.1'
            },
            'process 3.2': {
              '3.2.1': 'form 3.2.1'
            }
          }
        };
        $scope.templates = $rootScope.templates;
        $scope.listTaches = $rootScope.taches;
        $scope.getAllTasks();
        $scope.registres = $rootScope.registres;
        $scope.$watch('[id,label, description, onglet1, onglet2, placeholder, required, options, options2, validation, value, dateformat, treechecklistoptions, options3, subrows, subcolumns, numbersubrows, numbersubcolumns, widthsubcolumns, readgroups, readusers, writegroups, writeusers, disabledfortaches, lastcolumn, explastcolumn, multple, multilinerows, multilinecols, tachesbyprocess, allowImport, allowExport, allowSearch, allowFilter, entityUserRights, entityGroupRights, entityUserWriteRights, entityGroupWriteRights, entityFilter, regex, colTachesbyprocess, errorMessage,inputSize, datePattern, allowCreate, allowUpdate, allowDelete, databaseRequest, useRequest, maxSelect]', function() {
          formObject.id = $scope.id;
          formObject.label = $scope.label;
          formObject.description = $scope.description;
          formObject.onglet1 = $scope.onglet1;
          formObject.onglet2 = $scope.onglet2;
          formObject.placeholder = $scope.placeholder;
          formObject.required = $scope.required;
          formObject.options = $scope.options;
          formObject.options2 = $scope.options2;
          formObject.validation = $scope.validation;
          formObject.value = $scope.value;
          formObject.dateformat = $scope.dateformat;
          formObject.treechecklistoptions = $scope.treechecklistoptions;
          formObject.options3 = $scope.options3;
          formObject.subrows = $scope.subrows;
          formObject.subcolumns = $scope.subcolumns;
          formObject.numbersubrows = $scope.numbersubrows;
          formObject.numbersubcolumns = $scope.numbersubcolumns;
          formObject.widthsubcolumns = $scope.widthsubcolumns;
          formObject.readgroups = $scope.readgroups;
          formObject.readusers = $scope.readusers;
          formObject.writegroups = $scope.writegroups;
          formObject.writeusers = $scope.writeusers;
          formObject.disabledfortaches = $scope.disabledfortaches;
          formObject.lastcolumn = $scope.lastcolumn;
          formObject.explastcolumn = $scope.explastcolumn;
          formObject.multple = $scope.multple;
          formObject.multilinerows = $scope.multilinerows;
          formObject.multilinecols = $scope.multilinecols;
          formObject.tachesbyprocess = $scope.tachesbyprocess;
          formObject.allowImport = $scope.allowImport;
          formObject.allowExport = $scope.allowExport;
          formObject.allowSearch = $scope.allowSearch;
          formObject.allowFilter = $scope.allowFilter;
          formObject.entityUserRights = $scope.entityUserRights;
          formObject.entityGroupRights = $scope.entityGroupRights;
          formObject.entityUserWriteRights = $scope.entityUserWriteRights;
          formObject.entityGroupWriteRights = $scope.entityGroupWriteRights;
          formObject.entityFilter = $scope.entityFilter;
          formObject.regex = $scope.regex;
          formObject.colTachesbyprocess = $scope.colTachesbyprocess;
          formObject.allowCreate = $scope.allowCreate;
          formObject.allowUpdate = $scope.allowUpdate;
          formObject.allowDelete = $scope.allowDelete;
          formObject.errorMessage = $scope.errorMessage;
          formObject.inputSize = $scope.inputSize;
          formObject.datePattern = $scope.datePattern;
          formObject.databaseRequest = $scope.databaseRequest;
          formObject.useRequest = $scope.useRequest;
          return formObject.maxSelect = $scope.maxSelect;
        }, true);
        $scope.$watch('optionsText', function(text) {
          var x;
          $scope.options = (function() {
            var _l, _len3, _ref2, _results;
            _ref2 = text.split('\n');
            _results = [];
            for (_l = 0, _len3 = _ref2.length; _l < _len3; _l++) {
              x = _ref2[_l];
              if (x.length > 0) {
                _results.push(x);
              }
            }
            return _results;
          })();
          return $scope.inputText = $scope.options[0];
        });
        $scope.$watch('optionsText2', function(text) {
          var x;
          $scope.options2 = (function() {
            var _l, _len3, _ref2, _results;
            _ref2 = text.split('\n');
            _results = [];
            for (_l = 0, _len3 = _ref2.length; _l < _len3; _l++) {
              x = _ref2[_l];
              if (x.length > 0) {
                _results.push(x);
              }
            }
            return _results;
          })();
          return $scope.inputText2 = $scope.options2[0];
        });
        $scope.$watch('subrowsText', function(text) {
          var x;
          return $scope.subrows = (function() {
            var _l, _len3, _ref2, _results;
            _ref2 = text.split('\n');
            _results = [];
            for (_l = 0, _len3 = _ref2.length; _l < _len3; _l++) {
              x = _ref2[_l];
              if (x.length > 0) {
                _results.push(x);
              }
            }
            return _results;
          })();
        });
        $scope.$watch('subcolumnsText', function(text) {
          var x;
          return $scope.subcolumns = (function() {
            var _l, _len3, _ref2, _results;
            _ref2 = text.split('\n');
            _results = [];
            for (_l = 0, _len3 = _ref2.length; _l < _len3; _l++) {
              x = _ref2[_l];
              if (x.length > 0) {
                _results.push(x);
              }
            }
            return _results;
          })();
        });
        $scope.$watch('numbersubrowsText', function(text) {
          var x;
          return $scope.numbersubrows = (function() {
            var _l, _len3, _ref2, _results;
            _ref2 = text.split('\n');
            _results = [];
            for (_l = 0, _len3 = _ref2.length; _l < _len3; _l++) {
              x = _ref2[_l];
              if (x.length > 0) {
                _results.push(x);
              }
            }
            return _results;
          })();
        });
        $scope.$watch('numbersubcolumnsText', function(text) {
          var x;
          return $scope.numbersubcolumns = (function() {
            var _l, _len3, _ref2, _results;
            _ref2 = text.split('\n');
            _results = [];
            for (_l = 0, _len3 = _ref2.length; _l < _len3; _l++) {
              x = _ref2[_l];
              if (x.length > 0) {
                _results.push(x);
              }
            }
            return _results;
          })();
        });
        $scope.$watch('widthsubcolumnsText', function(text) {
          var x;
          return $scope.widthsubcolumns = (function() {
            var _l, _len3, _ref2, _results;
            _ref2 = text.split('\n');
            _results = [];
            for (_l = 0, _len3 = _ref2.length; _l < _len3; _l++) {
              x = _ref2[_l];
              if (x.length > 0) {
                _results.push(x);
              }
            }
            return _results;
          })();
        });
        $rootScope.$watch('listUsers', function() {
          return $scope.listUsers = $rootScope.listUsers;
        });
        $rootScope.$watch('listGroups', function() {
          return $scope.listGroups = $rootScope.listGroups;
        });
        $rootScope.$watch('entity', function() {
          return $scope.entity = $rootScope.entity;
        });
        $rootScope.$watch('list', function() {
          return $scope.list = $rootScope.list;
        });
        $rootScope.$watch('templates', function() {
          return $scope.templates = $rootScope.templates;
        });
        $rootScope.$watch('forms', function() {
          return $scope.forms = $rootScope.forms;
        });
        $rootScope.$watch('taches', function() {
          $scope.listTaches = $rootScope.taches;
          return $scope.getAllTasks();
        });
        $rootScope.$watch('registres', function() {
          return $scope.registres = $rootScope.registres;
        });
        component = $builder.components[formObject.component];
        return $scope.validationOptions = component.validationOptions;
      };
      $scope.data = {
        model: null,
        backup: function() {

          /*
                   Backup input value.
           */
          return this.model = {
            id: $scope.id,
            label: $scope.label,
            description: $scope.description,
            onglet1: $scope.onglet1,
            onglet2: $scope.onglet2,
            placeholder: $scope.placeholder,
            required: $scope.required,
            optionsText: $scope.optionsText,
            optionsText2: $scope.optionsText2,
            validation: $scope.validation,
            value: $scope.value,
            dateformat: $scope.dateformat,
            treechecklistoptions: JSON.parse(JSON.stringify($scope.treechecklistoptions)),
            subrowsText: $scope.subrowsText,
            subcolumnsText: $scope.subcolumnsText,
            numbersubrowsText: $scope.numbersubrowsText,
            widthsubcolumnsText: $scope.widthsubcolumnsText,
            numbersubcolumnsText: $scope.numbersubcolumnsText,
            readgroups: $scope.readgroups,
            readusers: $scope.readusers,
            writegroups: $scope.writegroups,
            writeusers: $scope.writeusers,
            disabledfortaches: $scope.disabledfortaches,
            lastcolumn: $scope.lastcolumn,
            explastcolumn: $scope.explastcolumn,
            multple: $scope.multple,
            multilinerows: JSON.parse(JSON.stringify($scope.multilinerows)),
            multilinecols: JSON.parse(JSON.stringify($scope.multilinecols)),
            process: $scope.process,
            taches: $scope.taches,
            allowImport: $scope.allowImport,
            allowExport: $scope.allowExport,
            allowSearch: $scope.allowSearch,
            allowFilter: $scope.allowFilter,
            regex: $scope.regex,
            errorMessage: $scope.errorMessage,
            inputSize: $scope.inputSize,
            datePattern: $scope.datePattern,
            colProcess: $scope.process,
            colTaches: $scope.taches,
            allowCreate: $scope.allowCreate,
            allowUpdate: $scope.allowUpdate,
            allowDelete: $scope.allowDelete,
            databaseRequest: $scope.databaseRequest,
            useRequest: $scope.useRequest,
            maxSelect: $scope.maxSelect
          };
        },
        rollback: function() {

          /*
                   Rollback input value.
           */
          if (!this.model) {
            return;
          }
          $scope.id = this.model.id;
          $scope.label = this.model.label;
          $scope.description = this.model.description;
          $scope.onglet1 = this.model.onglet1;
          $scope.onglet2 = this.model.onglet2;
          $scope.placeholder = this.model.placeholder;
          $scope.required = this.model.required;
          $scope.optionsText = this.model.optionsText;
          $scope.optionsText2 = this.model.optionsText2;
          $scope.validation = this.model.validation;
          $scope.value = this.model.value;
          $scope.dateformat = this.model.dateformat;
          $scope.treechecklistoptions = this.model.treechecklistoptions;
          $scope.subrowsText = this.model.subrowsText;
          $scope.subcolumnsText = this.model.subcolumnsText;
          $scope.numbersubrowsText = this.model.numbersubrowsText;
          $scope.widthsubcolumnsText = this.model.widthsubcolumnsText;
          $scope.numbersubcolumnsText = this.model.numbersubcolumnsText;
          $scope.readgroups = this.model.readgroups;
          $scope.readusers = this.model.readusers;
          $scope.writegroups = this.model.writegroups;
          $scope.writeusers = this.model.writeusers;
          $scope.disabledfortaches = this.model.disabledfortaches;
          $scope.lastcolumn = this.model.lastcolumn;
          $scope.explastcolumn = this.model.explastcolumn;
          $scope.multple = this.model.multple;
          $scope.multilinerows = this.model.multilinerows;
          $scope.multilinecols = this.model.multilinecols;
          $scope.process = this.model.process;
          $scope.taches = this.model.taches;
          $scope.allowImport = this.model.allowImport;
          $scope.allowExport = this.model.allowExport;
          $scope.allowSearch = this.model.allowSearch;
          $scope.allowFilter = this.model.allowFilter;
          $scope.regex = this.model.regex;
          $scope.errorMessage = this.model.errorMessage;
          $scope.inputSize = this.model.inputSize;
          $scope.datePattern = this.model.datePattern;
          $scope.colProcess = this.model.colProcess;
          $scope.colTaches = this.model.colTaches;
          $scope.allowCreate = this.model.allowCreate;
          $scope.allowUpdate = this.model.allowUpdate;
          $scope.allowDelete = this.model.allowDelete;
          $scope.databaseRequest = this.model.databaseRequest;
          $scope.useRequest = this.model.useRequest;
          return $scope.maxSelect = this.model.maxSelect;
        }
      };
      $scope.newSubItem = function(elm, index) {
        if (index != null) {
          return $scope[elm][index].nodes.push({
            level: 2,
            title: "",
            nodes: []
          });
        } else {
          return $scope[elm].push({
            level: 1,
            title: "",
            nodes: []
          });
        }
      };
      $scope.removeElm = function(elm, lv1, lv2) {
        if (lv2 != null) {
          return $scope[elm][lv1].nodes.splice(lv2, 1);
        } else {
          return $scope[elm].splice(lv1, 1);
        }
      };
      $scope.getRowPosition = function(index, numbersubrows) {
        var position, tot, value, _i, _len;
        position = 0;
        tot = 0;
        for (_i = 0, _len = numbersubrows.length; _i < _len; _i++) {
          value = numbersubrows[_i];
          tot = tot + parseInt(value);
          if (index >= tot) {
            position = position + 1;
          } else {
            return position;
          }
        }
      };
      $scope.tableBoby = [];
      $scope.getTableBoby = function(options, subrows, numbersubrows, subcolumns, options3, widthsubcolumns) {
        var curentIndex, emptysubrows, inputType, option, optionIndex, rslt, sc, subcolumn, subcolumnIndex, subrow, subrowIndex, _i, _j, _k, _l, _len, _len1, _len2, _len3, _len4, _m, _ref, _ref1;
        rslt = [];
        curentIndex = 0;
        if (options.length) {
          for (optionIndex = _i = 0, _len = options.length; _i < _len; optionIndex = ++_i) {
            option = options[optionIndex];
            emptysubrows = 1;
            for (subrowIndex = _j = 0, _len1 = subrows.length; _j < _len1; subrowIndex = ++_j) {
              subrow = subrows[subrowIndex];
              if (!((curentIndex <= subrowIndex && subrowIndex < curentIndex + parseInt(numbersubrows[optionIndex])))) {
                continue;
              }
              sc = [
                {
                  'content': subrow,
                  'width': parseInt(widthsubcolumns[1])
                }
              ];
              if (emptysubrows) {
                sc.unshift({
                  'content': option,
                  'rowspan': parseInt(numbersubrows[optionIndex]),
                  'width': parseInt(widthsubcolumns[0])
                });
              }
              for (subcolumnIndex = _k = 0, _len2 = subcolumns.length; _k < _len2; subcolumnIndex = ++_k) {
                subcolumn = subcolumns[subcolumnIndex];
                inputType = 'hidden';
                if (_ref = options3[subcolumnIndex], __indexOf.call($scope.inputtypes, _ref) >= 0) {
                  inputType = options3[subcolumnIndex];
                }
                sc.push({
                  'input': inputType
                });
              }
              rslt.push(sc);
              emptysubrows = 0;
            }
            if (emptysubrows) {
              rslt.push([
                {
                  'content': option
                }
              ]);
            }
            curentIndex = curentIndex + parseInt(numbersubrows[optionIndex]);
          }
        } else {
          for (subrowIndex = _l = 0, _len3 = subrows.length; _l < _len3; subrowIndex = ++_l) {
            subrow = subrows[subrowIndex];
            sc = [
              {
                'content': subrow,
                'colspan': 2,
                'width': parseInt(widthsubcolumns[1])
              }
            ];
            for (subcolumnIndex = _m = 0, _len4 = subcolumns.length; _m < _len4; subcolumnIndex = ++_m) {
              subcolumn = subcolumns[subcolumnIndex];
              inputType = 'hidden';
              if (_ref1 = options3[subcolumnIndex], __indexOf.call($scope.inputtypes, _ref1) >= 0) {
                inputType = options3[subcolumnIndex];
              }
              sc.push({
                'input': inputType
              });
            }
            rslt.push(sc);
          }
        }
        if (JSON.stringify($scope.tableBoby) !== JSON.stringify(rslt)) {
          $scope.tableBoby = rslt;
        }
        return $scope.tableBoby;
      };
      $scope.tableMultiline = [];
      $scope.getTableMultiline = function(multilinecols, multilinerows, options) {
        var c, col, inputs, row, rslt, sc, scol, srow, srowIndex, _i, _j, _k, _l, _len, _len1, _len2, _len3, _ref, _ref1, _ref2;
        rslt = [];
        c = [
          {
            'content': ' ',
            'rowspan': 2,
            'colspan': 2
          }
        ];
        sc = [];
        inputs = [];
        for (_i = 0, _len = multilinecols.length; _i < _len; _i++) {
          col = multilinecols[_i];
          c.push({
            'content': col.title,
            'colspan': col.nodes.length,
            'rowspan': col.nodes.length === 0 ? 2 : void 0,
            'width': col.size && col.nodes.length === 0 ? col.size : void 0
          });
          _ref = col.nodes;
          for (_j = 0, _len1 = _ref.length; _j < _len1; _j++) {
            scol = _ref[_j];
            sc.push({
              'content': scol.title,
              'width': scol.size ? scol.size : void 0
            });
            inputs.push({
              'input': (_ref1 = scol.input) != null ? _ref1 : col.input
            });
          }
          if (col.nodes.length === 0) {
            inputs.push({
              'input': col.input
            });
          }
        }
        rslt.push(c);
        rslt.push(sc);
        for (_k = 0, _len2 = multilinerows.length; _k < _len2; _k++) {
          row = multilinerows[_k];
          if (row.nodes.length === 0) {
            rslt.push([
              {
                'content': row.title,
                'colspan': 2,
                'width': options[0]
              }
            ].concat(inputs));
          } else {
            _ref2 = row.nodes;
            for (srowIndex = _l = 0, _len3 = _ref2.length; _l < _len3; srowIndex = ++_l) {
              srow = _ref2[srowIndex];
              if (srowIndex === 0) {
                rslt.push([
                  {
                    'content': row.title,
                    'rowspan': row.nodes.length,
                    'width': options[0]
                  }, {
                    'content': srow.title
                  }
                ].concat(inputs));
              } else {
                rslt.push([
                  {
                    'content': srow.title,
                    'width': options[1]
                  }
                ].concat(inputs));
              }
            }
          }
        }
        if (JSON.stringify($scope.tableMultiline) !== JSON.stringify(rslt)) {
          $scope.tableMultiline = rslt;
        }
        return $scope.tableMultiline;
      };
      $scope.inArray = function(value, arrayvalue) {
        value = String(value);
        if (__indexOf.call(arrayvalue, value) >= 0) {
          return true;
        }
        return false;
      };
      $scope.listProcessModules = {};
      $scope.listFormsProcess = {};
      $scope.listTachesProcess = {};
      $scope.arrayListTachesProcess = [];
      $scope.colListTachesProcess = {};
      $scope.colArrayListTachesProcess = [];
      $scope.mergeObjects = function(data, type, index) {
        var arrayListTachesProcess, key, obj, process, rslt, val, _i, _j, _len, _len1, _ref;
        rslt = {};
        if (data) {
          if (type !== 'taches' && type !== 'colProcess') {
            for (_i = 0, _len = data.length; _i < _len; _i++) {
              obj = data[_i];
              for (key in obj) {
                val = obj[key];
                rslt[key] = val;
              }
            }
          } else {
            arrayListTachesProcess = [];
            for (_j = 0, _len1 = data.length; _j < _len1; _j++) {
              process = data[_j];
              if ($scope.listTaches) {
                _ref = $scope.listTaches[process].fields;
                for (key in _ref) {
                  val = _ref[key];
                  rslt[process + "-" + key] = val;
                  arrayListTachesProcess.push({
                    "id": process + "-" + key,
                    "name": val
                  });
                }
              }
            }
          }
          if (type === 'process') {
            if (JSON.stringify($scope.listProcessModules) !== JSON.stringify(rslt)) {
              $scope.listProcessModules = rslt;
            }
            return $scope.listProcessModules;
          } else if (type === 'forms') {
            if (JSON.stringify($scope.listProcessModules) !== JSON.stringify(rslt)) {
              $scope.listFormsProcess = rslt;
            }
            return $scope.listFormsProcess;
          } else if (type === 'taches') {
            if (JSON.stringify($scope.listTachesProcess) !== JSON.stringify(rslt)) {
              $scope.listTachesProcess = rslt;
              $scope.arrayListTachesProcess = $filter('orderBy')(arrayListTachesProcess, 'name');
            }
            return $scope.arrayListTachesProcess;
          } else if (type === 'colProcess') {
            if (JSON.stringify($scope.colListTachesProcess[index]) !== JSON.stringify(rslt)) {
              $scope.colListTachesProcess[index] = rslt;
              $scope.colArrayListTachesProcess[index] = $filter('orderBy')(arrayListTachesProcess, 'name');
            }
            return $scope.colArrayListTachesProcess[index];
          }
        }
        return rslt;
      };
      $scope.getSrcMap = function(zoom, lat, lng) {
        return GMaps.staticMapURL({
          size: [400, 300],
          lat: lat,
          lng: lng,
          zoom: zoom
        });
      };
      $scope.orderOptions = function(old, options) {
        var elm, option, rslt, _i, _j, _len, _len1;
        for (_i = 0, _len = options.length; _i < _len; _i++) {
          option = options[_i];
          if (__indexOf.call(old, option) < 0) {
            old.push(option);
          }
        }
        rslt = [];
        for (_j = 0, _len1 = old.length; _j < _len1; _j++) {
          elm = old[_j];
          if (__indexOf.call(options, elm) >= 0) {
            rslt.push(elm);
          }
        }
        return rslt;
      };
      $scope.getArrayByOPtions = function(old, options, arrayvalue) {
        var elm, index, rslt, _i, _len;
        if (arrayvalue.length > options.length) {
          rslt = [];
          for (index = _i = 0, _len = old.length; _i < _len; index = ++_i) {
            elm = old[index];
            if (__indexOf.call(options, elm) >= 0) {
              rslt.push(arrayvalue[index]);
            }
          }
          return rslt;
        }
        return arrayvalue;
      };
      $scope.getTachesByProcess = function() {
        var key, proc, rslt, taches, _i, _len, _ref;
        rslt = {};
        _ref = $scope.process;
        for (_i = 0, _len = _ref.length; _i < _len; _i++) {
          proc = _ref[_i];
          if ($scope.listTaches[proc]) {
            taches = [];
            for (key in $scope.listTaches[proc]["fields"]) {
              if ($scope.inArray(proc + "-" + key, $scope.taches)) {
                taches.push(key);
              }
            }
            if (taches.length) {
              rslt[proc] = taches;
            }
          }
        }
        return $scope.tachesbyprocess = rslt;
      };
      $scope.getColTachesByProcess = function() {
        var arr, index, key, proc, process, rslt, taches, _i, _j, _len, _len1, _ref;
        arr = [];
        _ref = $scope.colProcess;
        for (index = _i = 0, _len = _ref.length; _i < _len; index = ++_i) {
          process = _ref[index];
          rslt = {};
          for (_j = 0, _len1 = process.length; _j < _len1; _j++) {
            proc = process[_j];
            taches = [];
            for (key in $scope.listTaches[proc]["fields"]) {
              if ($scope.inArray(proc + "-" + key, $scope.colTaches[index])) {
                taches.push(key);
              }
            }
            if (taches.length) {
              rslt[proc] = taches;
            }
          }
          arr.push(rslt);
        }
        return $scope.colTachesbyprocess = arr;
      };
      $scope.getAllTasks = function() {
        var colTachesbyprocess, pid, pval, tid, _i, _j, _len, _len1, _ref, _ref1, _results;
        _ref = $scope.tachesbyprocess;
        for (pid in _ref) {
          pval = _ref[pid];
          if (typeof $scope.listTaches[pid] === 'undefined') {
            $scope.listTaches[pid] = {
              "name": pid + " (supprimé)",
              "fields": {}
            };
          }
          for (_i = 0, _len = pval.length; _i < _len; _i++) {
            tid = pval[_i];
            if (typeof $scope.listTaches[pid]["fields"][tid] === 'undefined') {
              $scope.listTaches[pid]["fields"][tid] = tid + " (supprimé)";
            }
          }
        }
        _ref1 = $scope.colTachesbyprocess;
        _results = [];
        for (_j = 0, _len1 = _ref1.length; _j < _len1; _j++) {
          colTachesbyprocess = _ref1[_j];
          _results.push((function() {
            var _results1;
            _results1 = [];
            for (pid in colTachesbyprocess) {
              pval = colTachesbyprocess[pid];
              if (typeof $scope.listTaches[pid] === 'undefined') {
                $scope.listTaches[pid] = {
                  "name": pid + " (supprimé)",
                  "fields": {}
                };
              }
              _results1.push((function() {
                var _k, _len2, _results2;
                _results2 = [];
                for (_k = 0, _len2 = pval.length; _k < _len2; _k++) {
                  tid = pval[_k];
                  if (typeof $scope.listTaches[pid]["fields"][tid] === 'undefined') {
                    _results2.push($scope.listTaches[pid]["fields"][tid] = tid + " (supprimé)");
                  } else {
                    _results2.push(void 0);
                  }
                }
                return _results2;
              })());
            }
            return _results1;
          })());
        }
        return _results;
      };
      $scope.toggle = function(item, list) {
        var idx;
        idx = list.indexOf(item);
        if (idx > -1) {
          return list.splice(idx, 1);
        } else {
          return list.push(item);
        }
      };
      return $scope.exists = function(item, list) {
        return list.indexOf(item) > -1;
      };
    }
  ]).controller('fbComponentsController', [
    '$scope', '$injector', function($scope, $injector) {
      var $builder;
      $builder = $injector.get('$builder');
      $scope.selectGroup = function($event, group) {
        var component, name, _ref, _results;
        if ($event != null) {
          $event.preventDefault();
        }
        $scope.activeGroup = group;
        $scope.components = [];
        _ref = $builder.components;
        _results = [];
        for (name in _ref) {
          component = _ref[name];
          if (component.group === group) {
            _results.push($scope.components.push(component));
          }
        }
        return _results;
      };
      $scope.groups = $builder.groups;
      $scope.activeGroup = $scope.groups[0];
      $scope.allComponents = $builder.components;
      $scope.$watch('allComponents', function() {
        return $scope.selectGroup(null, $scope.activeGroup);
      });
      return $scope.getComponents = function($event, group) {
        var component, components, name, _ref;
        if ($event != null) {
          $event.preventDefault();
        }
        $scope.activeGroup = group;
        components = [];
        _ref = $builder.components;
        for (name in _ref) {
          component = _ref[name];
          if (component.group === group) {
            components.push(component);
          }
        }
        return components;
      };
    }
  ]).controller('fbComponentController', [
    '$scope', function($scope) {
      return $scope.copyObjectToScope = function(object) {
        return copyObjectToScope(object, $scope);
      };
    }
  ]).controller('fbFormController', [
    '$scope', '$injector', '$window', '$rootScope', function($scope, $injector, $window, $rootScope) {
      var $builder, $timeout;
      $builder = $injector.get('$builder');
      $timeout = $injector.get('$timeout');
      if ($scope.input == null) {
        $scope.input = [];
      }
      $scope.$watch('form', function() {
        if ($scope.input.length > $scope.form.length) {
          $scope.input.splice($scope.form.length);
        }
        return $timeout(function() {
          return $scope.$broadcast($builder.broadcastChannel.updateInput);
        });
      }, true);
      return $window.onbeforeunload = function() {
        if ($rootScope.exit) {
          return void 0;
        }
        return '';
      };
    }
  ]).controller('fbFormObjectController', [
    '$scope', '$injector', function($scope, $injector) {
      var $builder;
      $builder = $injector.get('$builder');
      $scope.copyObjectToScope = function(object) {
        return copyObjectToScope(object, $scope);
      };
      return $scope.updateInput = function(value) {

        /*
              Copy current scope.input[X] to $parent.input.
              @param value: The input value.
         */
        var input;
        input = {
          id: $scope.formObject.id,
          label: $scope.formObject.label,
          value: value != null ? value : ''
        };
        return $scope.$parent.input.splice($scope.$index, 1, input);
      };
    }
  ]);

}).call(this);

(function() {
  angular.module('builder.directive', ['builder.provider', 'builder.controller', 'builder.drag', 'validator']).directive('fbBuilder', [
    '$injector', function($injector) {
      var $builder, $drag;
      $builder = $injector.get('$builder');
      $drag = $injector.get('$drag');
      return {
        restrict: 'A',
        scope: {
          fbBuilder: '='
        },
        template: "<div class='form-horizontal'>\n    <div class='fb-form-object-editable' ng-repeat=\"object in formObjects\"\n        fb-form-object-editable=\"object\" ng-class=\"{'multiline':object.component == 'multiline' || object.component == 'multilineentity' || object.component == 'bardocument' || object.component == 'imagezone'}\"></div>\n</div>",
        link: function(scope, element, attrs) {
          var beginMove, _base, _name;
          scope.formName = attrs.fbBuilder;
          if ((_base = $builder.forms)[_name = scope.formName] == null) {
            _base[_name] = [];
          }
          scope.formObjects = $builder.forms[scope.formName];
          beginMove = true;
          $(element).addClass('fb-builder');
          return $drag.droppable($(element), {
            move: function(e) {
              var $empty, $formObject, $formObjects, height, index, offset, positions, _i, _j, _ref, _ref1;
              if (beginMove) {
                $("div.fb-form-object-editable").popover('hide');
                beginMove = false;
              }
              $formObjects = $(element).find('.fb-form-object-editable:not(.empty,.dragging)');
              if ($formObjects.length === 0) {
                if ($(element).find('.fb-form-object-editable.empty').length === 0) {
                  $(element).find('>div:first').append($("<div class='fb-form-object-editable empty'></div>"));
                }
                return;
              }
              positions = [];
              positions.push(-1000);
              for (index = _i = 0, _ref = $formObjects.length; _i < _ref; index = _i += 1) {
                $formObject = $($formObjects[index]);
                offset = $formObject.offset();
                height = $formObject.height();
                positions.push(offset.top + height / 2);
              }
              positions.push(positions[positions.length - 1] + 1000);
              for (index = _j = 1, _ref1 = positions.length; _j < _ref1; index = _j += 1) {
                if (e.pageY > positions[index - 1] && e.pageY <= positions[index]) {
                  $(element).find('.empty').remove();
                  $empty = $("<div class='fb-form-object-editable empty'></div>");
                  if (index - 1 < $formObjects.length) {
                    $empty.insertBefore($($formObjects[index - 1]));
                  } else {
                    $empty.insertAfter($($formObjects[index - 2]));
                  }
                  break;
                }
              }
            },
            out: function() {
              if (beginMove) {
                $("div.fb-form-object-editable").popover('hide');
                beginMove = false;
              }
              return $(element).find('.empty').remove();
            },
            up: function(e, isHover, draggable) {
              var formObject, newIndex, oldIndex;
              beginMove = true;
              if (!$drag.isMouseMoved()) {
                $(element).find('.empty').remove();
                return;
              }
              if (!isHover && draggable.mode === 'drag') {
                formObject = draggable.object.formObject;
                if (formObject.editable) {
                  $builder.removeFormObject(attrs.fbBuilder, formObject.index);
                }
              } else if (isHover) {
                if (draggable.mode === 'mirror') {
                  $builder.insertFormObject(scope.formName, $(element).find('.empty').index('.fb-form-object-editable'), {
                    component: draggable.object.componentName
                  });
                }
                if (draggable.mode === 'drag') {
                  oldIndex = draggable.object.formObject.index;
                  newIndex = $(element).find('.empty').index('.fb-form-object-editable');
                  if (oldIndex < newIndex) {
                    newIndex--;
                  }
                  $builder.updateFormObjectIndex(scope.formName, oldIndex, newIndex);
                }
              }
              return $(element).find('.empty').remove();
            }
          });
        }
      };
    }
  ]).directive('fbFormObjectEditable', [
    '$injector', function($injector) {
      var $builder, $compile, $drag, $validator;
      $builder = $injector.get('$builder');
      $drag = $injector.get('$drag');
      $compile = $injector.get('$compile');
      $validator = $injector.get('$validator');
      return {
        restrict: 'A',
        controller: 'fbFormObjectEditableController',
        scope: {
          formObject: '=fbFormObjectEditable'
        },
        link: function(scope, element) {
          var popover;
          scope.inputArray = [];
          scope.$component = $builder.components[scope.formObject.component];
          scope.setupScope(scope.formObject);
          scope.$watch('$component.template', function(template) {
            var view;
            if (!template) {
              return;
            }
            view = $compile(template)(scope);
            return $(element).html(view);
          });
          $(element).on('click', function() {
            return false;
          });
          $drag.draggable($(element), {
            object: {
              formObject: scope.formObject
            }
          });
          if (!scope.formObject.editable) {
            return;
          }
          popover = {};
          scope.$watch('$component.popoverTemplate', function(template) {
            if (!template) {
              return;
            }
            $(element).removeClass(popover.id);
            popover = {
              id: "fb-" + (Math.random().toString().substr(2)),
              isClickedSave: false,
              view: null,
              html: template
            };
            popover.html = $(popover.html).addClass(popover.id);
            popover.view = $compile(popover.html)(scope);
            $(element).addClass(popover.id);
            return $(element).popover({
              html: true,
              title: scope.$component.label,
              content: popover.view,
              container: '#popover-contner',
              placement: $builder.config.popoverPlacement
            });
          });
          scope.popover = {
            save: function($event) {

              /*
              The save event of the popover.
               */
              $event.preventDefault();
              $validator.validate(scope).success(function() {
                popover.isClickedSave = true;
                return $(element).popover('hide');
              });
            },
            remove: function($event) {

              /*
              The delete event of the popover.
               */
              $event.preventDefault();
              $builder.removeFormObject(scope.$parent.formName, scope.$parent.$index);
              $(element).popover('hide');
            },
            shown: function() {

              /*
              The shown event of the popover.
               */
              scope.data.backup();
              return popover.isClickedSave = false;
            },
            cancel: function($event) {

              /*
              The cancel event of the popover.
               */

              /*
              scope.data.rollback()
               */
              if ($event) {
                $event.preventDefault();
                $(element).popover('hide');
              }
            },
            refreshmultiselect: function(id) {
              var $popover;
              $popover = $("form." + popover.id).closest('.popover');
              setTimeout(function() {
                return $popover.find('#' + popover.id + '-' + id).multiSelect('refresh');
              }, 0);
            },
            setmultiselect: function(id, refresh) {
              var $popover;
              $popover = $("form." + popover.id).closest('.popover');
              setTimeout(function() {
                if (refresh) {
                  return $popover.find('.' + id).multiSelect('refresh');
                } else {
                  return $popover.find('.' + id).multiSelect();
                }
              }, 0);
            }
          };
          $(element).on('show.bs.popover', function() {
            var $popover, elementOrigin, popoverTop;
            if ($drag.isMouseMoved()) {
              return false;
            }
            $("div.fb-form-object-editable:not(." + popover.id + ")").popover('hide');
            $popover = $("form." + popover.id).closest('.popover');
            if ($popover.length > 0) {
              elementOrigin = $(element).offset().top + $(element).height() / 2;
              popoverTop = elementOrigin - $popover.height() / 2;
              $popover.show();
              $(element).addClass('componant-checked');
              setTimeout(function() {
                return $popover.addClass('in');
              }, 0);
              return false;
            }
          });
          $(element).on('shown.bs.popover', function() {
            var $popover;
            $popover = $("form." + popover.id).closest('.popover');
            $popover.find('#readgroups-select').multiSelect({
              keepOrder: true
            });
            $popover.find('#readusers-select').multiSelect({
              keepOrder: true
            });
            $popover.find('#writegroups-select').multiSelect({
              keepOrder: true
            });
            $popover.find('#writeusers-select').multiSelect({
              keepOrder: true
            });
            $popover.find('#listmodules-select').multiSelect({
              keepOrder: true
            });
            $popover.find('#listprocess-select').multiSelect({
              keepOrder: true
            });
            $popover.find('#listforms-select').multiSelect({
              keepOrder: true
            });
            $popover.find('.multilinecol-select').attr({
              id: popover.id + '-multilinecol-select'
            });
            $popover.find('.multilinecol-select').multiSelect({
              keepOrder: true
            });
            $popover.find('.process-select').attr({
              id: popover.id + '-process-select'
            });
            $popover.find('.process-select').multiSelect();
            $popover.find('.disabledfortaches-select').multiSelect({
              keepOrder: true
            });
            $popover.find('.taches-select').attr({
              id: popover.id + '-taches-select'
            });
            $popover.find('.disabledfortaches-select').attr({
              id: popover.id + '-taches-select'
            });
            $popover.find('.taches-select').multiSelect();
            $popover.find('.picklistidentity-select').attr({
              id: popover.id + '-picklistidentity-select'
            });
            $popover.find('.picklistidentity-select').multiSelect({
              keepOrder: true
            });
            $popover.find('.col-right-group').multiSelect();
            $popover.find('.col-right-user').multiSelect();
            $popover.find('.col-right-process').multiSelect();
            $popover.find('.col-right-taches').multiSelect();
            $popover.css({
              left: '0px',
              top: '0px'
            });
            $(element).addClass('componant-checked');
            $(".popover ." + popover.id + " input:first").select();
            scope.$apply(function() {
              return scope.popover.shown();
            });
          });
          return $(element).on('hide.bs.popover', function() {
            var $popover;
            $popover = $("form." + popover.id).closest('.popover');

            /*
            if not popover.isClickedSave
                 * eval the cancel event
                if scope.$$phase or scope.$root.$$phase
                    scope.popover.cancel()
                else
                    scope.$apply -> scope.popover.cancel()
             */
            $popover.removeClass('in');
            $(element).removeClass('componant-checked');
            setTimeout(function() {
              return $popover.hide();
            }, 300);
            return false;
          });
        }
      };
    }
  ]).directive('fbComponents', function() {
    return {
      restrict: 'A',
      template: "\n\n<div class=\"accordion\" id=\"accordion\">\n    <div ng-repeat=\"group in groups\" class=\"accordion-group\">\n      <div class=\"accordion-heading\" ng-init=\"clicked=false\">\n        <a class=\"accordion-toggle\" data-toggle=\"collapse\" ng-click=\"clicked=!clicked\" data-parent=\"#accordion\" href=\"#\{{group}}\">\n            <i class=\"glyphicon glyphicon-chevron-right\" ng-show=\"clicked==false\"></i>\n            <i class=\"glyphicon glyphicon-chevron-down\" ng-show=\"clicked==true\"></i> {{group}}\n        </a>\n      </div>\n      <hr>\n      <div id=\"{{group}}\" class=\"form-horizontal accordion-body collapse\">\n        <div class=\"accordion-inner\" ng-init=\"components = getComponents($event, group)\">\n         <div class='fb-component' ng-repeat=\"component in components\"\n                fb-component=\"component\"></div><hr>\n      </div>\n    </div>\n</div>",
      controller: 'fbComponentsController'
    };
  }).directive('fbComponent', [
    '$injector', function($injector) {
      var $builder, $compile, $drag;
      $builder = $injector.get('$builder');
      $drag = $injector.get('$drag');
      $compile = $injector.get('$compile');
      return {
        restrict: 'A',
        scope: {
          component: '=fbComponent'
        },
        controller: 'fbComponentController',
        link: function(scope, element) {
          scope.copyObjectToScope(scope.component);
          $drag.draggable($(element), {
            mode: 'mirror',
            defer: false,
            object: {
              componentName: scope.component.name
            }
          });
          return scope.$watch('component.icon', function(icon) {
            var view;
            if (!icon) {
              return;
            }
            view = $compile(icon)(scope);
            return $(element).html(view);
          });
        }
      };
    }
  ]).directive('fbForm', [
    '$injector', function($injector) {
      return {
        restrict: 'A',
        require: 'ngModel',
        scope: {
          formName: '@fbForm',
          input: '=ngModel',
          "default": '=fbDefault'
        },
        template: "<div class='fb-form-object' ng-repeat=\"object in form\" fb-form-object=\"object\"></div>",
        controller: 'fbFormController',
        link: function(scope, element, attrs) {
          var $builder, _base, _name;
          $builder = $injector.get('$builder');
          if ((_base = $builder.forms)[_name = scope.formName] == null) {
            _base[_name] = [];
          }
          return scope.form = $builder.forms[scope.formName];
        }
      };
    }
  ]).directive('fbFormObject', [
    '$injector', function($injector) {
      var $builder, $compile, $parse;
      $builder = $injector.get('$builder');
      $compile = $injector.get('$compile');
      $parse = $injector.get('$parse');
      return {
        restrict: 'A',
        controller: 'fbFormObjectController',
        link: function(scope, element, attrs) {
          scope.formObject = $parse(attrs.fbFormObject)(scope);
          scope.$component = $builder.components[scope.formObject.component];
          scope.$on($builder.broadcastChannel.updateInput, function() {
            return scope.updateInput(scope.inputText);
          });
          if (scope.$component.arrayToText) {
            scope.inputArray = [];
            scope.$watch('inputArray', function(newValue, oldValue) {
              var checked, index, _ref;
              if (newValue === oldValue) {
                return;
              }
              checked = [];
              for (index in scope.inputArray) {
                if (scope.inputArray[index]) {
                  checked.push((_ref = scope.options[index]) != null ? _ref : scope.inputArray[index]);
                }
              }
              return scope.inputText = checked.join(', ');
            }, true);
          }
          scope.$watch('inputText', function() {
            return scope.updateInput(scope.inputText);
          });
          scope.$watch(attrs.fbFormObject, function() {
            return scope.copyObjectToScope(scope.formObject);
          }, true);
          scope.$watch('$component.template', function(template) {
            var $input, $template, view;
            if (!template) {
              return;
            }
            $template = $(template);
            $input = $template.find("[ng-model='inputText']");
            $input.attr({
              validator: '{{validation}}'
            });
            view = $compile($template)(scope);
            return $(element).html(view);
          });
          if (!scope.$component.arrayToText && scope.formObject.options.length > 0) {
            scope.inputText = scope.formObject.options[0];
          }
          return scope.$watch("default['" + scope.formObject.id + "']", function(value) {
            if (!value) {
              return;
            }
            if (scope.$component.arrayToText) {
              return scope.inputArray = value;
            } else {
              return scope.inputText = value;
            }
          });
        }
      };
    }
  ]).directive('resizable', [
    '$window', function($window) {
      return {
        restrict: 'A',
        controller: 'fbFormController',
        link: function(scope, element, attrs) {
          scope.initializeWindowSize = function() {
            scope.windowHeight = $window.innerHeight;
            return scope.style = function() {
              return {
                'height': (scope.windowHeight - 93) + 'px'
              };
            };
          };
          scope.initializeWindowSize();
          return angular.element($window).bind('resize', function() {
            scope.initializeWindowSize();
            return scope.$apply();
          });
        }
      };
    }
  ]).directive('rights', function() {
    return {
      restrict: 'A',
      controller: 'fbComponentsController',
      template: "        <div class=\"form-group\">\n            <input type='button' ng-init=\"composant = false\" ng-click=\"composant = !composant\" class='btn btn-primary btn-block' value='Composant'/>\n        </div>\n        <div ng-show=\"composant\" class=\"modall\">\n            <i class=\"glyphicon glyphicon-remove-circle\" ng-click=\"composant = !composant\"></i>\n            <hr/>\n            <div class=\"btn-group\" data-toggle=\"buttons\">\n                <label class=\"btn btn-success\" ng-click=\"typeRights = 'taskComposant'\">\n                    <input type=\"radio\">Tâches\n</label>\n<label class=\"btn btn-success\" ng-click=\"typeRights = 'groupComposant'\">\n                    <input type=\"radio\">Groupes\n</label>\n<label class=\"btn btn-success\" ng-click=\"typeRights = 'userComposant'\">\n                    <input type=\"radio\">Utilisateurs\n</label>\n            </div>\n            <hr/>\n            <div ng-show=\"typeRights == 'taskComposant'\" class=\"form-group col-sm-6\">\n                <table class=\"table table-bordered\">\n                    <tr><th class=\"col-md-10\">Process</th><th class=\"text-center col-md-1\">Selectionner</th></tr>\n                    <tr ng-repeat=\"(key,element) in listTaches\">\n                        <td>{{element.name}}</td>\n                        <td class=\"text-center\"><input type='checkbox' ng-checked=\"exists(key, process)\" ng-click=\"toggle(key, process); getTachesByProcess();\"/></td>\n                    </tr>\n                </table>\n            </div>\n            <div ng-show=\"typeRights == 'taskComposant'\" class=\"form-group col-sm-6\">\n                <table class=\"table table-bordered\">\n                    <tr><th class=\"col-md-10\">Tâches</th><th class=\"text-center col-md-1\">Selectionner</th><th class=\"text-center col-md-1\">grisé</th></tr>\n                    <tr ng-repeat=\"element in mergeObjects(process, 'taches','disabledfortaches')\">\n                        <td>{{element.name}}</td>\n                        <td class=\"text-center\"><input type='checkbox' ng-checked=\"exists(element.id, taches)\" ng-click=\"toggle(element.id, taches); getTachesByProcess();\"/></td>\n				<td class=\"text-center\"><input type='checkbox' ng-checked=\"exists(element.id, disabledfortaches)\" ng-click=\"toggle(element.id, disabledfortaches); getTachesByProcess();\"/></td>\n                    </tr>\n                </table>\n            </div>\n            <div ng-show=\"typeRights == 'groupComposant'\" class=\"form-group\">\n                <table class=\"table table-bordered\">\n                    <tr><th class=\"col-md-10\">Groupes</th><th class=\"text-center col-md-1\">Lecture</th><th class=\"text-center col-md-1\">Ecriture</th></tr>\n                    <tr ng-repeat=\"group in listGroups\">\n                        <td>{{group.name}}</td>\n                        <td class=\"text-center\"><input type='checkbox' ng-checked=\"exists(group.id, readgroups)\" ng-click=\"toggle(group.id, readgroups)\"/></td>\n                        <td class=\"text-center\"><input type='checkbox' ng-checked=\"exists(group.id, writegroups)\" ng-click=\"toggle(group.id, writegroups)\"/></td>\n                    </tr>\n                </table>\n            </div>\n            <div ng-show=\"typeRights == 'userComposant'\" class=\"form-group\">\n                <table class=\"table table-bordered\">\n                    <tr><th class=\"col-md-10\">Utilisateurs</th><th class=\"text-center col-md-1\">Lecture</th><th class=\"text-center col-md-1\">Ecriture</th></tr>\n                    <tr ng-repeat=\"user in listUsers\">\n                        <td>{{user.firstName}} {{user.lastName}}</td>\n                        <td class=\"text-center\"><input type='checkbox' ng-checked=\"exists(user.id, readusers)\" ng-click=\"toggle(user.id, readusers)\"/></td>\n                        <td class=\"text-center\"><input type='checkbox' ng-checked=\"exists(user.id, writeusers)\" ng-click=\"toggle(user.id, writeusers)\"/></td>\n                    </tr>\n                </table>\n            </div>\n        </div>\n        <hr/>"
    };
  }).directive('deletecomponent', function() {
    return {
      restrict: 'A',
      controller: 'fbComponentsController',
      template: "<input type='button' ng-click=\"popover.remove($event)\" class='btn btn-danger btn-block' value='Delete'/>"
    };
  }).directive('propagation', function() {
    return {
      restrict: 'A',
      controller: 'fbComponentsController',
      template: "<div class='form-group'>\n    <input type='button' ng-init=\"propagation = false\" ng-click=\"propagation = !propagation\" class='btn btn-primary btn-block' value='Propagation'/>\n</div>\n<div ng-show=\"propagation\">\n    <div class=\"form-group\">\n        <label class='control-label'>Modules</label>\n        <select id=\"listmodules-select\" multiple class='form-control' ng-model=\"m\" ng-options=\"module for (module, process) in listForms\" ng-change=\"popover.refreshmultiselect('listprocess-select'); p=[]; f=[]\"/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Process</label>\n        <select id=\"listprocess-select\" multiple class='form-control' ng-model=\"p\" ng-options=\"proc for (proc,forms) in mergeObjects(m, 'process')\" ng-change=\"popover.refreshmultiselect('listforms-select'); f=[]\"/>\n    </div>\n    <div class=\"form-group\">\n        <label class='control-label'>Forms</label>\n\n        <select id=\"listforms-select\" multiple class='form-control' ng-model=\"f\" ng-options=\"value for (id,value) in mergeObjects(p, 'forms')\"/>\n    </div>\n</div>\n<hr/>"
    };
  });

}).call(this);

(function() {
  angular.module('builder.drag', []).provider('$drag', function() {
    var $injector, $rootScope, delay;
    $injector = null;
    $rootScope = null;
    this.data = {
      draggables: {},
      droppables: {}
    };
    this.mouseMoved = false;
    this.isMouseMoved = (function(_this) {
      return function() {
        return _this.mouseMoved;
      };
    })(this);
    this.hooks = {
      down: {},
      move: {},
      up: {}
    };
    this.eventMouseMove = function() {};
    this.eventMouseUp = function() {};
    $((function(_this) {
      return function() {
        $(document).on('mousedown', function(e) {
          var func, key, _ref;
          _this.mouseMoved = false;
          _ref = _this.hooks.down;
          for (key in _ref) {
            func = _ref[key];
            func(e);
          }
        });
        $(document).on('mousemove', function(e) {
          var func, key, _ref;
          _this.mouseMoved = true;
          _ref = _this.hooks.move;
          for (key in _ref) {
            func = _ref[key];
            func(e);
          }
        });
        return $(document).on('mouseup', function(e) {
          var func, key, _ref;
          _ref = _this.hooks.up;
          for (key in _ref) {
            func = _ref[key];
            func(e);
          }
        });
      };
    })(this));
    this.currentId = 0;
    this.getNewId = (function(_this) {
      return function() {
        return "" + (_this.currentId++);
      };
    })(this);
    this.setupEasing = function() {
      return jQuery.extend(jQuery.easing, {
        easeOutQuad: function(x, t, b, c, d) {
          return -c * (t /= d) * (t - 2) + b;
        }
      });
    };
    this.setupProviders = function(injector) {

      /*
      Setup providers.
       */
      $injector = injector;
      return $rootScope = $injector.get('$rootScope');
    };
    this.isHover = (function(_this) {
      return function($elementA, $elementB) {

        /*
        Is element A hover on element B?
        @param $elementA: jQuery object
        @param $elementB: jQuery object
         */
        var isHover, offsetA, offsetB, sizeA, sizeB;
        offsetA = $elementA.offset();
        offsetB = $elementB.offset();
        sizeA = {
          width: $elementA.width(),
          height: $elementA.height()
        };
        sizeB = {
          width: $elementB.width(),
          height: $elementB.height()
        };
        isHover = {
          x: false,
          y: false
        };
        isHover.x = offsetA.left > offsetB.left && offsetA.left < offsetB.left + sizeB.width;
        isHover.x = isHover.x || offsetA.left + sizeA.width > offsetB.left && offsetA.left + sizeA.width < offsetB.left + sizeB.width;
        if (!isHover) {
          return false;
        }
        isHover.y = offsetA.top > offsetB.top && offsetA.top < offsetB.top + sizeB.height;
        isHover.y = isHover.y || offsetA.top + sizeA.height > offsetB.top && offsetA.top + sizeA.height < offsetB.top + sizeB.height;
        return isHover.x && isHover.y;
      };
    })(this);
    delay = function(ms, func) {
      return setTimeout(function() {
        return func();
      }, ms);
    };
    this.autoScroll = {
      up: false,
      down: false,
      scrolling: false,
      scroll: (function(_this) {
        return function() {
          _this.autoScroll.scrolling = true;
          if (_this.autoScroll.up) {
            $('html, body').dequeue().animate({
              scrollTop: $(window).scrollTop() - 50
            }, 100, 'easeOutQuad');
            return delay(100, function() {
              return _this.autoScroll.scroll();
            });
          } else if (_this.autoScroll.down) {
            $('html, body').dequeue().animate({
              scrollTop: $(window).scrollTop() + 50
            }, 100, 'easeOutQuad');
            return delay(100, function() {
              return _this.autoScroll.scroll();
            });
          } else {
            return _this.autoScroll.scrolling = false;
          }
        };
      })(this),
      start: (function(_this) {
        return function(e) {
          if (e.clientY < 50) {
            _this.autoScroll.up = true;
            _this.autoScroll.down = false;
            if (!_this.autoScroll.scrolling) {
              return _this.autoScroll.scroll();
            }
          } else if (e.clientY > $(window).innerHeight() - 50) {
            _this.autoScroll.up = false;
            _this.autoScroll.down = true;
            if (!_this.autoScroll.scrolling) {
              return _this.autoScroll.scroll();
            }
          } else {
            _this.autoScroll.up = false;
            return _this.autoScroll.down = false;
          }
        };
      })(this),
      stop: (function(_this) {
        return function() {
          _this.autoScroll.up = false;
          return _this.autoScroll.down = false;
        };
      })(this)
    };
    this.dragMirrorMode = (function(_this) {
      return function($element, defer, object) {
        var result;
        if (defer == null) {
          defer = true;
        }
        result = {
          id: _this.getNewId(),
          mode: 'mirror',
          maternal: $element[0],
          element: null,
          object: object
        };
        $element.on('mousedown', function(e) {
          var $clone;
          e.preventDefault();
          $clone = $element.clone();
          result.element = $clone[0];
          $clone.addClass("fb-draggable form-horizontal prepare-dragging");
          _this.hooks.move.drag = function(e, defer) {
            var droppable, id, _ref, _results;
            if ($clone.hasClass('prepare-dragging')) {

              /*
              $clone.css
                  width: $element.width()
                  height: $element.height()
               */
              $clone.removeClass('prepare-dragging');
              $clone.addClass('dragging');
              if (defer) {
                return;
              }
            }
            $clone.offset({
              left: e.pageX - $clone.width() / 2,
              top: e.pageY - $clone.height() / 2
            });
            _this.autoScroll.start(e);
            _ref = _this.data.droppables;
            _results = [];
            for (id in _ref) {
              droppable = _ref[id];
              if (_this.isHover($clone, $(droppable.element))) {
                _results.push(droppable.move(e, result));
              } else {
                _results.push(droppable.out(e, result));
              }
            }
            return _results;
          };
          _this.hooks.up.drag = function(e) {
            var droppable, id, isHover, _ref;
            _ref = _this.data.droppables;
            for (id in _ref) {
              droppable = _ref[id];
              isHover = _this.isHover($clone, $(droppable.element));
              droppable.up(e, isHover, result);
            }
            delete _this.hooks.move.drag;
            delete _this.hooks.up.drag;
            result.element = null;
            $clone.remove();
            return _this.autoScroll.stop();
          };
          $('#div-contner').append($clone);
          if (!defer) {
            return _this.hooks.move.drag(e, defer);
          }
        });
        return result;
      };
    })(this);
    this.dragDragMode = (function(_this) {
      return function($element, defer, object) {
        var result;
        if (defer == null) {
          defer = true;
        }
        result = {
          id: _this.getNewId(),
          mode: 'drag',
          maternal: null,
          element: $element[0],
          object: object
        };
        $element.addClass('fb-draggable');
        $element.on('mousedown', function(e) {
          e.preventDefault();
          if ($element.hasClass('dragging')) {
            return;
          }
          $element.addClass('prepare-dragging');
          _this.hooks.move.drag = function(e, defer) {
            var droppable, id, _ref;
            if ($element.hasClass('prepare-dragging')) {
              $element.css({
                width: $element.width(),
                height: $element.height()
              });
              $element.removeClass('prepare-dragging');
              $element.addClass('dragging');
              if (defer) {
                return;
              }
            }
            $element.offset({
              left: e.pageX - $element.width() / 2,
              top: e.pageY - $element.height() / 2
            });
            _this.autoScroll.start(e);
            _ref = _this.data.droppables;
            for (id in _ref) {
              droppable = _ref[id];
              if (_this.isHover($element, $(droppable.element))) {
                droppable.move(e, result);
              } else {
                droppable.out(e, result);
              }
            }
          };
          _this.hooks.up.drag = function(e) {
            var droppable, id, isHover, _ref;
            _ref = _this.data.droppables;
            for (id in _ref) {
              droppable = _ref[id];
              isHover = _this.isHover($element, $(droppable.element));
              droppable.up(e, isHover, result);
            }
            delete _this.hooks.move.drag;
            delete _this.hooks.up.drag;
            $element.css({
              width: '',
              height: '',
              left: '',
              top: ''
            });
            $element.removeClass('dragging defer-dragging');
            return _this.autoScroll.stop();
          };
          if (!defer) {
            return _this.hooks.move.drag(e, defer);
          }
        });
        return result;
      };
    })(this);
    this.dropMode = (function(_this) {
      return function($element, options) {
        var result;
        result = {
          id: _this.getNewId(),
          element: $element[0],
          move: function(e, draggable) {
            return $rootScope.$apply(function() {
              return typeof options.move === "function" ? options.move(e, draggable) : void 0;
            });
          },
          up: function(e, isHover, draggable) {
            return $rootScope.$apply(function() {
              return typeof options.up === "function" ? options.up(e, isHover, draggable) : void 0;
            });
          },
          out: function(e, draggable) {
            return $rootScope.$apply(function() {
              return typeof options.out === "function" ? options.out(e, draggable) : void 0;
            });
          }
        };
        return result;
      };
    })(this);
    this.draggable = (function(_this) {
      return function($element, options) {
        var draggable, element, result, _i, _j, _len, _len1;
        if (options == null) {
          options = {};
        }

        /*
        Make the element could be drag.
        @param element: The jQuery element.
        @param options: Options
            mode: 'drag' [default], 'mirror'
            defer: yes/no. defer dragging
            object: custom information
         */
        result = [];
        if (options.mode === 'mirror') {
          for (_i = 0, _len = $element.length; _i < _len; _i++) {
            element = $element[_i];
            draggable = _this.dragMirrorMode($(element), options.defer, options.object);
            result.push(draggable.id);
            _this.data.draggables[draggable.id] = draggable;
          }
        } else {
          for (_j = 0, _len1 = $element.length; _j < _len1; _j++) {
            element = $element[_j];
            draggable = _this.dragDragMode($(element), options.defer, options.object);
            result.push(draggable.id);
            _this.data.draggables[draggable.id] = draggable;
          }
        }
        return result;
      };
    })(this);
    this.droppable = (function(_this) {
      return function($element, options) {
        var droppable, element, result, _i, _len;
        if (options == null) {
          options = {};
        }

        /*
        Make the element coulde be drop.
        @param $element: The jQuery element.
        @param options: The droppable options.
            move: The custom mouse move callback. (e, draggable)->
            up: The custom mouse up callback. (e, isHover, draggable)->
            out: The custom mouse out callback. (e, draggable)->
         */
        result = [];
        for (_i = 0, _len = $element.length; _i < _len; _i++) {
          element = $element[_i];
          droppable = _this.dropMode($(element), options);
          result.push(droppable);
          _this.data.droppables[droppable.id] = droppable;
        }
        return result;
      };
    })(this);
    this.get = function($injector) {
      this.setupEasing();
      this.setupProviders($injector);
      return {
        isMouseMoved: this.isMouseMoved,
        data: this.data,
        draggable: this.draggable,
        droppable: this.droppable
      };
    };
    this.get.$inject = ['$injector'];
    this.$get = this.get;
  });

}).call(this);

(function() {
  angular.module('builder', ['builder.directive']);

}).call(this);


/*
    component:
        It is like a class.
        The base components are textInput, textArea, select, check, radio.
        User can custom the form with components.
    formObject:
        It is like an object (an instance of the component).
        User can custom the label, description, required and validation of the input.
    form:
        This is for end-user. There are form groups int the form.
        They can input the value to the form.
 */

(function() {
  var __indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; };

  angular.module('builder.provider', []).provider('$builder', function() {
    var $http, $injector, $templateCache;
    $injector = null;
    $http = null;
    $templateCache = null;
    this.config = {
      popoverPlacement: ''
    };
    this.components = {};
    this.groups = [];
    this.broadcastChannel = {
      updateInput: '$updateInput'
    };
    this.forms = {
      "default": []
    };
    this.genrateId = function(label) {

      /*
            if label and label.trim()
                return label.toLowerCase().replace(/[^a-z0-9_]/g,'')
            else
       */
      return 'form_' + (new Date()).getTime();
    };
    this.convertComponent = function(name, component) {
      var result, _ref, _ref1, _ref10, _ref11, _ref12, _ref13, _ref14, _ref15, _ref16, _ref17, _ref18, _ref19, _ref2, _ref20, _ref21, _ref22, _ref23, _ref24, _ref25, _ref26, _ref27, _ref28, _ref29, _ref3, _ref30, _ref31, _ref32, _ref33, _ref34, _ref35, _ref36, _ref37, _ref38, _ref39, _ref4, _ref40, _ref41, _ref42, _ref43, _ref44, _ref45, _ref46, _ref47, _ref48, _ref49, _ref5, _ref50, _ref51, _ref52, _ref53, _ref54, _ref6, _ref7, _ref8, _ref9;
      result = {
        name: name,
        group: (_ref = component.group) != null ? _ref : 'Default',
        tab: (_ref1 = component.tab) != null ? _ref1 : 'Default',
        id: (_ref2 = component.id) != null ? _ref2 : '',
        label: (_ref3 = component.label) != null ? _ref3 : '',
        description: (_ref4 = component.description) != null ? _ref4 : '',
        onglet1: (_ref5 = component.onglet1) != null ? _ref5 : '',
        onglet2: (_ref6 = component.onglet2) != null ? _ref6 : '',
        placeholder: (_ref7 = component.placeholder) != null ? _ref7 : '',
        editable: (_ref8 = component.editable) != null ? _ref8 : true,
        required: (_ref9 = component.required) != null ? _ref9 : false,
        validation: (_ref10 = component.validation) != null ? _ref10 : 'string',
        validationOptions: (_ref11 = component.validationOptions) != null ? _ref11 : [],
        options: (_ref12 = component.options) != null ? _ref12 : [],
        options2: (_ref13 = component.options2) != null ? _ref13 : [],
        arrayToText: (_ref14 = component.arrayToText) != null ? _ref14 : false,
        template: component.template,
        templateUrl: component.templateUrl,
        icon: component.icon,
        popoverTemplate: component.popoverTemplate,
        popoverTemplateUrl: component.popoverTemplateUrl,
        value: (_ref15 = component.value) != null ? _ref15 : '',
        dateformat: (_ref16 = component.dateformat) != null ? _ref16 : '',
        treechecklistoptions: (_ref17 = component.treechecklistoptions) != null ? _ref17 : '',
        options3: (_ref18 = component.options3) != null ? _ref18 : [],
        subrows: (_ref19 = component.subrows) != null ? _ref19 : [],
        subcolumns: (_ref20 = component.subcolumns) != null ? _ref20 : [],
        numbersubrows: (_ref21 = component.numbersubrows) != null ? _ref21 : [],
        numbersubcolumns: (_ref22 = component.numbersubcolumns) != null ? _ref22 : [],
        widthsubcolumns: (_ref23 = component.widthsubcolumns) != null ? _ref23 : [],
        readgroups: (_ref24 = component.readgroups) != null ? _ref24 : [],
        readusers: (_ref25 = component.readusers) != null ? _ref25 : [],
        writegroups: (_ref26 = component.writegroups) != null ? _ref26 : [],
        writeusers: (_ref27 = component.writeusers) != null ? _ref27 : [],
        disabledfortaches: (_ref28 = component.disabledfortaches) != null ? _ref28 : [],
        lastcolumn: (_ref29 = component.lastcolumn) != null ? _ref29 : [],
        explastcolumn: (_ref30 = component.explastcolumn) != null ? _ref30 : [],
        multple: (_ref31 = component.multple) != null ? _ref31 : false,
        multilinerows: (_ref32 = component.multilinerows) != null ? _ref32 : '',
        multilinecols: (_ref33 = component.multilinecols) != null ? _ref33 : '',
        tachesbyprocess: (_ref34 = component.tachesbyprocess) != null ? _ref34 : {},
        allowImport: (_ref35 = component.allowImport) != null ? _ref35 : false,
        allowExport: (_ref36 = component.allowExport) != null ? _ref36 : false,
        allowSearch: (_ref37 = component.allowSearch) != null ? _ref37 : false,
        allowFilter: (_ref38 = component.allowFilter) != null ? _ref38 : false,
        entityUserRights: (_ref39 = component.entityUserRights) != null ? _ref39 : [],
        entityGroupRights: (_ref40 = component.entityGroupRights) != null ? _ref40 : [],
        entityUserWriteRights: (_ref41 = component.entityUserWriteRights) != null ? _ref41 : [],
        entityGroupWriteRights: (_ref42 = component.entityGroupWriteRights) != null ? _ref42 : [],
        entityFilter: (_ref43 = component.entityFilter) != null ? _ref43 : [],
        regex: (_ref44 = component.regex) != null ? _ref44 : '',
        colTachesbyprocess: (_ref45 = component.colTachesbyprocess) != null ? _ref45 : [],
        allowCreate: (_ref46 = component.allowCreate) != null ? _ref46 : false,
        allowUpdate: (_ref47 = component.allowUpdate) != null ? _ref47 : false,
        allowDelete: (_ref48 = component.allowDelete) != null ? _ref48 : false,
        errorMessage: (_ref49 = component.errorMessage) != null ? _ref49 : '',
        inputSize: (_ref50 = component.inputSize) != null ? _ref50 : '',
        datePattern: (_ref51 = component.datePattern) != null ? _ref51 : '',
        databaseRequest: (_ref52 = component.databaseRequest) != null ? _ref52 : '',
        useRequest: (_ref53 = component.useRequest) != null ? _ref53 : false,
        maxSelect: (_ref54 = component.maxSelect) != null ? _ref54 : ''
      };
      if (!result.template && !result.templateUrl) {
        console.error("The template is empty.");
      }
      if (!result.popoverTemplate && !result.popoverTemplateUrl) {
        console.error("The popoverTemplate is empty.");
      }
      return result;
    };
    this.convertFormObject = function(name, formObject) {
      var component, result, _ref, _ref1, _ref10, _ref11, _ref12, _ref13, _ref14, _ref15, _ref16, _ref17, _ref18, _ref19, _ref2, _ref20, _ref21, _ref22, _ref23, _ref24, _ref25, _ref26, _ref27, _ref28, _ref29, _ref3, _ref30, _ref31, _ref32, _ref33, _ref34, _ref35, _ref36, _ref37, _ref38, _ref39, _ref4, _ref40, _ref41, _ref42, _ref43, _ref44, _ref45, _ref46, _ref47, _ref48, _ref49, _ref5, _ref6, _ref7, _ref8, _ref9;
      if (formObject == null) {
        formObject = {};
      }
      component = this.components[formObject.component];
      if (component == null) {
        throw "The component " + formObject.component + " was not registered.";
      }
      if (typeof formObject.lastcolumn === "string") {
        formObject.lastcolumn = [formObject.lastcolumn];
      }
      if (typeof formObject.explastcolumn === "string") {
        formObject.explastcolumn = [formObject.explastcolumn];
      }
      result = {
        component: formObject.component,
        tab: (_ref = formObject.tab) != null ? _ref : component.tab,
        editable: (_ref1 = formObject.editable) != null ? _ref1 : component.editable,
        index: (_ref2 = formObject.index) != null ? _ref2 : 0,
        id: formObject.id ? formObject.id : component.id ? component.id : this.genrateId((_ref3 = formObject.label) != null ? _ref3 : component.label),
        label: (_ref4 = formObject.label) != null ? _ref4 : component.label,
        description: (_ref5 = formObject.description) != null ? _ref5 : component.description,
        onglet1: (_ref6 = formObject.onglet1) != null ? _ref6 : component.onglet1,
        onglet2: (_ref7 = formObject.onglet2) != null ? _ref7 : component.onglet2,
        placeholder: (_ref8 = formObject.placeholder) != null ? _ref8 : component.placeholder,
        options: (_ref9 = formObject.options) != null ? _ref9 : component.options,
        options2: (_ref10 = formObject.options2) != null ? _ref10 : component.options2,
        required: (_ref11 = formObject.required) != null ? _ref11 : component.required,
        validation: (_ref12 = formObject.validation) != null ? _ref12 : component.validation,
        value: (_ref13 = formObject.value) != null ? _ref13 : component.value,
        dateformat: (_ref14 = formObject.dateformat) != null ? _ref14 : component.dateformat,
        treechecklistoptions: formObject.treechecklistoptions ? JSON.parse(JSON.stringify(formObject.treechecklistoptions)) : JSON.parse(JSON.stringify(component.treechecklistoptions)),
        options3: (_ref15 = formObject.options3) != null ? _ref15 : component.options3,
        subrows: (_ref16 = formObject.subrows) != null ? _ref16 : component.subrows,
        subcolumns: (_ref17 = formObject.subcolumns) != null ? _ref17 : component.subcolumns,
        numbersubrows: (_ref18 = formObject.numbersubrows) != null ? _ref18 : component.numbersubrows,
        numbersubcolumns: (_ref19 = formObject.numbersubcolumns) != null ? _ref19 : component.numbersubcolumns,
        widthsubcolumns: (_ref20 = formObject.widthsubcolumns) != null ? _ref20 : component.widthsubcolumns,
        readgroups: (_ref21 = formObject.readgroups) != null ? _ref21 : component.readgroups,
        readusers: (_ref22 = formObject.readusers) != null ? _ref22 : component.readusers,
        writegroups: (_ref23 = formObject.writegroups) != null ? _ref23 : component.writegroups,
        writeusers: (_ref24 = formObject.writeusers) != null ? _ref24 : component.writeusers,
        disabledfortaches: (_ref25 = formObject.disabledfortaches) != null ? _ref25 : component.disabledfortaches,
        lastcolumn: (_ref26 = formObject.lastcolumn) != null ? _ref26 : component.lastcolumn,
        explastcolumn: (_ref27 = formObject.explastcolumn) != null ? _ref27 : component.explastcolumn,
        multple: (_ref28 = formObject.multple) != null ? _ref28 : component.multple,
        multilinerows: formObject.multilinerows ? JSON.parse(JSON.stringify(formObject.multilinerows)) : JSON.parse(JSON.stringify(component.multilinerows)),
        multilinecols: formObject.multilinecols ? JSON.parse(JSON.stringify(formObject.multilinecols)) : JSON.parse(JSON.stringify(component.multilinecols)),
        tachesbyprocess: (_ref29 = formObject.tachesbyprocess) != null ? _ref29 : component.tachesbyprocess,
        allowImport: (_ref30 = formObject.allowImport) != null ? _ref30 : component.allowImport,
        allowExport: (_ref31 = formObject.allowExport) != null ? _ref31 : component.allowExport,
        allowSearch: (_ref32 = formObject.allowSearch) != null ? _ref32 : component.allowSearch,
        allowFilter: (_ref33 = formObject.allowFilter) != null ? _ref33 : component.allowFilter,
        entityUserRights: (_ref34 = formObject.entityUserRights) != null ? _ref34 : component.entityUserRights,
        entityGroupRights: (_ref35 = formObject.entityGroupRights) != null ? _ref35 : component.entityGroupRights,
        entityUserWriteRights: (_ref36 = formObject.entityUserWriteRights) != null ? _ref36 : component.entityUserWriteRights,
        entityGroupWriteRights: (_ref37 = formObject.entityGroupWriteRights) != null ? _ref37 : component.entityGroupWriteRights,
        entityFilter: (_ref38 = formObject.entityFilter) != null ? _ref38 : component.entityFilter,
        regex: (_ref39 = formObject.regex) != null ? _ref39 : component.regex,
        colTachesbyprocess: (_ref40 = formObject.colTachesbyprocess) != null ? _ref40 : component.colTachesbyprocess,
        allowCreate: (_ref41 = formObject.allowCreate) != null ? _ref41 : component.allowCreate,
        allowUpdate: (_ref42 = formObject.allowUpdate) != null ? _ref42 : component.allowUpdate,
        allowDelete: (_ref43 = formObject.allowDelete) != null ? _ref43 : component.allowDelete,
        errorMessage: (_ref44 = formObject.errorMessage) != null ? _ref44 : component.errorMessage,
        inputSize: (_ref45 = formObject.inputSize) != null ? _ref45 : component.inputSize,
        datePattern: (_ref46 = formObject.datePattern) != null ? _ref46 : component.datePattern,
        databaseRequest: (_ref47 = formObject.databaseRequest) != null ? _ref47 : component.databaseRequest,
        useRequest: (_ref48 = formObject.useRequest) != null ? _ref48 : component.useRequest,
        maxSelect: (_ref49 = formObject.maxSelect) != null ? _ref49 : component.maxSelect
      };
      return result;
    };
    this.reindexFormObject = (function(_this) {
      return function(name) {
        var formObjects, index, _i, _ref;
        formObjects = _this.forms[name];
        for (index = _i = 0, _ref = formObjects.length; _i < _ref; index = _i += 1) {
          formObjects[index].index = index;
        }
      };
    })(this);
    this.setupProviders = (function(_this) {
      return function(injector) {
        $injector = injector;
        $http = $injector.get('$http');
        return $templateCache = $injector.get('$templateCache');
      };
    })(this);
    this.loadTemplate = function(component) {

      /*
            Load template for components.
            @param component: {object} The component of $builder.
       */
      if (component.template == null) {
        $http.get(component.templateUrl, {
          cache: $templateCache
        }).success(function(template) {
          return component.template = template;
        });
      }
      if (component.popoverTemplate == null) {
        return $http.get(component.popoverTemplateUrl, {
          cache: $templateCache
        }).success(function(template) {
          return component.popoverTemplate = template;
        });
      }
    };
    this.registerComponent = (function(_this) {
      return function(name, component) {
        var newComponent, _ref;
        if (component == null) {
          component = {};
        }

        /*
              Register the component for form-builder.
              @param name: The component name.
              @param component: The component object.
                  group: {string} The component group.
                  tab: {string} The component tab.
                  id: {string} The id of the input.
                  label: {string} The label of the input.
                  description: {string} The description of the input.
                  placeholder: {string} The placeholder of the input.
                  editable: {bool} Is the form object editable?
                  required: {bool} Is the form object required?
                  validation: {string} angular-validator. "/regex/" or "[rule1, rule2]". (default is RegExp(.*))
                  validationOptions: {array} [{rule: angular-validator, label: 'option label'}] the options for the validation. (default is [])
                  options: {array} The input options.
                  arrayToText: {bool} checkbox could use this to convert input (default is no)
                  template: {string} html template
                  templateUrl: {string} The url of the template.
                  popoverTemplate: {string} html template
                  popoverTemplateUrl: {string} The url of the popover template.
         */
        if (_this.components[name] == null) {
          newComponent = _this.convertComponent(name, component);
          _this.components[name] = newComponent;
          if ($injector != null) {
            _this.loadTemplate(newComponent);
          }
          if (_ref = newComponent.group, __indexOf.call(_this.groups, _ref) < 0) {
            _this.groups.push(newComponent.group);
          }
        } else {
          console.error("The component " + name + " was registered.");
        }
      };
    })(this);
    this.addFormObject = (function(_this) {
      return function(name, formObject) {
        var _base;
        if (formObject == null) {
          formObject = {};
        }

        /*
              Insert the form object into the form at last.
         */
        if ((_base = _this.forms)[name] == null) {
          _base[name] = [];
        }
        return _this.insertFormObject(name, _this.forms[name].length, formObject);
      };
    })(this);
    this.insertFormObject = (function(_this) {
      return function(name, index, formObject) {
        var _base;
        if (formObject == null) {
          formObject = {};
        }

        /*
              Insert the form object into the form at {index}.
              @param name: The form name.
              @param index: The form object index.
              @param form: The form object.
                  id: The form object id.
                  tab: {string} The tab name
                  component: {string} The component name
                  editable: {bool} Is the form object editable? (default is yes)
                  label: {string} The form object label.
                  description: {string} The form object description.
                  placeholder: {string} The form object placeholder.
                  options: {array} The form object options.
                  required: {bool} Is the form object required? (default is no)
                  validation: {string} angular-validator. "/regex/" or "[rule1, rule2]".
                  [index]: {int} The form object index. It will be updated by $builder.
              @return: The form object.
         */
        if ((_base = _this.forms)[name] == null) {
          _base[name] = [];
        }
        if (index > _this.forms[name].length) {
          index = _this.forms[name].length;
        } else if (index < 0) {
          index = 0;
        }
        _this.forms[name].splice(index, 0, _this.convertFormObject(name, formObject));
        _this.reindexFormObject(name);
        return _this.forms[name][index];
      };
    })(this);
    this.removeFormObject = (function(_this) {
      return function(name, index) {

        /*
              Remove the form object by the index.
              @param name: The form name.
              @param index: The form object index.
         */
        var formObjects;
        formObjects = _this.forms[name];
        formObjects.splice(index, 1);
        return _this.reindexFormObject(name);
      };
    })(this);
    this.updateFormObjectIndex = (function(_this) {
      return function(name, oldIndex, newIndex) {

        /*
              Update the index of the form object.
              @param name: The form name.
              @param oldIndex: The old index.
              @param newIndex: The new index.
         */
        var formObject, formObjects;
        if (oldIndex === newIndex) {
          return;
        }
        formObjects = _this.forms[name];
        formObject = formObjects.splice(oldIndex, 1)[0];
        formObjects.splice(newIndex, 0, formObject);
        return _this.reindexFormObject(name);
      };
    })(this);
    this.$get = [
      '$injector', (function(_this) {
        return function($injector) {
          var component, name, _ref;
          _this.setupProviders($injector);
          _ref = _this.components;
          for (name in _ref) {
            component = _ref[name];
            _this.loadTemplate(component);
          }
          return {
            config: _this.config,
            components: _this.components,
            groups: _this.groups,
            forms: _this.forms,
            broadcastChannel: _this.broadcastChannel,
            registerComponent: _this.registerComponent,
            addFormObject: _this.addFormObject,
            insertFormObject: _this.insertFormObject,
            removeFormObject: _this.removeFormObject,
            updateFormObjectIndex: _this.updateFormObjectIndex
          };
        };
      })(this)
    ];
  });

}).call(this);
