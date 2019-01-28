app.controller("loginCtrl",function ($rootScope,$scope,$http,$state) {
    $rootScope.isLandingPage = false;
    $scope.isLoggedin = false;
    $scope.isOneblank = true;

        
    $scope.userLogin = function(){
        var param = { username: $scope.username, password: $scope.password};
        if("" !== $scope.username && "" !== $scope.password) {
            $scope.isOneblank = true;
            $http.post("/camel/api/login", param).then(function (response) {
                $state.go("calendarTime",{id:response.data[0].id,branchId:response.data[0].branchId});
                console.log(response.data[0].branchId);
                console.log(response.data[0].id);
            }).catch(function(){
                $scope.isLoggedin = true;
            });

        }else {
            $scope.isOneblank = false;
        }
    } 
       
})