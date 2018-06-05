var app = angular.module('eshop', []);
app.controller('esCtrl', function( $scope, $http, $window ) {
	$scope.msgRow = false;
	$scope.login = function() {
		api.post({
			http:$http,
			data:$scope.formData,
			url:'login'
		},function(data){
			console.log(data);
			if (data.code == 1) {
				$window.location.href = "Framework.jsp";
				$scope.msgRow = false ;
			} else {
				$scope.msgRow = true;
				$scope.msg = data.message;
			}
		});
	};
});