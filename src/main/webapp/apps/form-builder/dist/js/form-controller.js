(function() {
  angular.module('app', ['builder', 'builder.components', 'validator.rules']).controller('FormController', [
    '$scope', '$builder', '$validator', '$http', '$rootScope', function($scope, $builder, $validator, $http, $rootScope) {
      $scope.form = $builder.forms['default'];
      $scope.input = [];
      $scope.defaultValue = {};
      $scope.ajaxResponse = 0;
      $scope.formId = '';
      $scope.saveError = '';
      $scope.formError = '';
      $scope.modalForm = {
        name: '',
        description: ''
      };
      $scope.getParameterByName = function(name) {
        var regex, results;
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        regex = new RegExp("[\\?&]" + name + "=([^&#]*)");
        results = regex.exec(location.search);
        if (results === null) {
          return "";
        } else {
          return decodeURIComponent(results[1].replace(/\+/g, " "));
        }
      };
      $scope.transformRequest = function(obj) {
        var key, str, value;
        str = [];
        for (key in obj) {
          value = obj[key];
          str.push(encodeURIComponent(key) + "=" + encodeURIComponent(value));
        }
        console.log(str.join("&"));
        return str.join("&");
      };
      $scope.submit = function(redirect) {
        var url;
        url = "../../service/form/new";
        if ($scope.formId !== '' && typeof $scope.formId === 'number') {
          url = "../../service/form/" + $scope.formId + "/save";
        }
        return $validator.validate($scope, 'modalForm').success(function() {
          return $http({
            method: 'PUT',
            data: {
              name: $scope.modalForm.name,
              description: $scope.modalForm.description,
              json: JSON.stringify($scope.form)
            },
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            },
            transformRequest: $scope.transformRequest,
            url: url
          }).success(function(data, status, headers, config) {
            if (status !== 200 || data.status !== 'success') {
              $scope.saveError = 'An error occured whiel saving form, you may not connected to server';
            }
            if (status === 200 && data.status === 'success') {
              if ($scope.formId === '' && typeof data.formId === 'number') {
                $scope.formId = data.formId;
              }
              $scope.saveError = '';
              if (redirect) {
                $rootScope.exit = true;
                document.location.href = "../../pages/formulaire/liste.xhtml";
              }
              return $('#saveModal').modal('hide');
            }
          }).error(function(data, status, headers, config) {
            $scope.saveError = 'An error occured whiel saving form';
            if ($scope.formId === '' && typeof data.formId === 'number') {
              return $scope.formId = data.formId;
            }
          });
        });
      };
      $scope.getForm = function(formId) {
        var url;
        if (typeof formId === 'undefined' || formId === "") {
          return;
        }
        url = "../../service/form/" + formId + "/json";
        return $http({
          method: 'GET',
          'url': url
        }).success(function(data, status, headers, config) {
          if (status !== 200 || data.status !== 'success') {
            $scope.formError = 'An error occured whiel getting the form';
            if (data.json === 'doesntExists') {
              $scope.formError += " , form doesn't exist";
            } else {
              $scope.formError += " , " + data.json;
            }
            if ($scope.formId === '' && typeof data.formId === 'number') {
              $scope.formId = data.formId;
            }
            $('#formModal').modal('show');
          }
          if (status === 200 && data.status === 'success') {
            if ($scope.formId === '' && typeof data.formId === 'number') {
              $scope.formId = data.formId;
              $scope.modalForm.name = data.name;
              $scope.modalForm.description = data.description;
              jQuery.each(data.json, function(index, value) {
                return $builder.addFormObject('default', value);
              });
            }
            return $scope.formError = '';
          }
        }).error(function(data, status, headers, config) {
          $scope.formError = 'An error occured whiel saving form, you may not connected to server';
          return $('#formModal').modal('show');
        });
      };
      $scope.getForm($scope.getParameterByName('formId'));
      $scope.getListGroups = function() {
        return $http({
          method: 'GET',
          url: "../../service/group/json"
        }).success(function(data, status, headers, config) {
          $rootScope.listGroups = data;
          return $scope.ajaxResponse = $scope.ajaxResponse + 1;
        }).error(function(data, status, headers, config) {
          $rootScope.listGroups = [];
          return $scope.ajaxResponse = $scope.ajaxResponse + 1;
        });
      };
      $scope.getListUsers = function() {
        return $http({
          method: 'GET',
          url: "../../service/user/json"
        }).success(function(data, status, headers, config) {
          $rootScope.listUsers = data;
          return $scope.ajaxResponse = $scope.ajaxResponse + 1;
        }).error(function(data, status, headers, config) {
          $rootScope.listUsers = [];
          return $scope.ajaxResponse = $scope.ajaxResponse + 1;
        });
      };
      $scope.getEntity = function() {
        return $http({
          method: 'GET',
          url: "../../service/entity/json"
        }).success(function(data, status, headers, config) {
          $rootScope.entity = data;
          return $scope.ajaxResponse = $scope.ajaxResponse + 1;
        }).error(function(data, status, headers, config) {
          $rootScope.entity = [];
          return $scope.ajaxResponse = $scope.ajaxResponse + 1;
        });
      };
      $scope.getList = function() {
        return $http({
          method: 'GET',
          url: "../../service/list/json"
        }).success(function(data, status, headers, config) {
          $rootScope.list = data;
          return $scope.ajaxResponse = $scope.ajaxResponse + 1;
        }).error(function(data, status, headers, config) {
          $rootScope.list = [];
          return $scope.ajaxResponse = $scope.ajaxResponse + 1;
        });
      };
      $scope.getTemplate = function() {
        return $http({
          method: 'GET',
          url: "../../service/template/json"
        }).success(function(data, status, headers, config) {
          $rootScope.templates = data;
          return $scope.ajaxResponse = $scope.ajaxResponse + 1;
        }).error(function(data, status, headers, config) {
          $rootScope.templates = [];
          return $scope.ajaxResponse = $scope.ajaxResponse + 1;
        });
      };
      $scope.getForm = function() {
        return $http({
          method: 'GET',
          url: "../../service/form/json"
        }).success(function(data, status, headers, config) {
          $rootScope.forms = data;
          return $scope.ajaxResponse = $scope.ajaxResponse + 1;
        }).error(function(data, status, headers, config) {
          $rootScope.forms = [];
          return $scope.ajaxResponse = $scope.ajaxResponse + 1;
        });
      };
      $scope.getTache = function() {
        return $http({
          method: 'GET',
          url: "../../service/tache/json"
        }).success(function(data, status, headers, config) {
          $rootScope.taches = data;
          return $scope.ajaxResponse = $scope.ajaxResponse + 1;
        }).error(function(data, status, headers, config) {
          $rootScope.taches = [];
          return $scope.ajaxResponse = $scope.ajaxResponse + 1;
        });
      };
      $scope.getRegistres = function() {
        return $http({
          method: 'GET',
          url: "../../service/registre/json"
        }).success(function(data, status, headers, config) {
          $rootScope.registres = data;
          return $scope.ajaxResponse = $scope.ajaxResponse + 1;
        }).error(function(data, status, headers, config) {
          $rootScope.registres = [];
          return $scope.ajaxResponse = $scope.ajaxResponse + 1;
        });
      };
      $scope.getListGroups();
      $scope.getListUsers();
      $scope.getEntity();
      $scope.getList();
      $scope.getTemplate();
      $scope.getForm();
      $scope.getTache();
      return $scope.getRegistres();
    }
  ]);

}).call(this);
