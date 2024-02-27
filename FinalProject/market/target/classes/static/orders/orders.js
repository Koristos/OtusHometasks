angular.module('app').controller('ordersController', function ($scope, $http, $localStorage) {
    const contextPath = 'http://localhost:8189/summer';

    $scope.loadOrders = function () {
        $http({
            url: contextPath + '/api/v1/orders',
            method: 'GET'
        }).then(function (response) {
            $scope.orders = response.data;
            console.log($scope.orders);
        });
    }

    $scope.loadOrders();
});