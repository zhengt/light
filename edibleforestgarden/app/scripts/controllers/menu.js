'use strict';

angular.module('lightApp').controller('menuCtrl', ['$scope', '$http', 'authService', function($scope, $http, authService) {
    $scope.menuSettings = {isCollapsed : true};
    $scope.tree = [];

    var getMenuPost = {
        category : 'menu',
        name : 'getMenu',
        readOnly: true,
        data : {
            host : 'www.edibleforestgarden.ca'
        }
    };

    $http.post('api/rs', getMenuPost)
        .success(function(result, status, headers, config) {
            $scope.tree = result.out_Own;

        });

    $scope.toggleCollapsed = function () {
        $scope.menuSettings.isCollapsed =  !$scope.menuSettings.isCollapsed;
    };

    $scope.hasAccess = function(item) {
        for (var i = 0; i < authService.authentication.currentUser.roles.length; i++) {
            for (var j = 0; j < item.roles.length; j++) {
                if (authService.authentication.currentUser.roles[i] == item.roles[j]) {
                    return true;
                }
            }
        }
        return false;
    };

    $scope.logOut = function () {
        authService.logOut();

    };

}]);
