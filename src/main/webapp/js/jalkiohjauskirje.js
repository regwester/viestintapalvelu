angular.module('app').controller('JalkiohjauskirjeController', ['$scope', 'Generator', 'Printer' ,function($scope, Generator, Printer) {	
	$scope.jalkiohjauskirjeet = [];
	
	function generateJalkiohjauskirje(count) {
		$scope.jalkiohjauskirjeet = $scope.jalkiohjauskirjeet.concat(Generator.generateObjects(count, function(data) {
			var postoffice = data.any('postoffice')
			return {
				"addressLabel": {
					"firstName": data.any('firstname'),
					"lastName": data.any('lastname'),
					"addressline": data.any('street') + ' ' + data.any('housenumber'),
					"addressline2": "",
					"addressline3": "",
					"postalCode": postoffice.substring(0, postoffice.indexOf(' ')),
					"city": postoffice.substring(postoffice.indexOf(' ') + 1),
					"region": "",
					"country": data.prioritize('Finland', 0.95).otherwise(data.any('country'))
				},
				"tulokset": generateTulokset(data.any('hakutoive-lukumaara'))
			}
		}))
	}
	
	function generateTulokset(count) {
		return Generator.generateObjects(count, function(data) {
			return {
		    	"koulu": data.any('koulu'),
		    	"hakutoive": data.any('hakutoive'),
		    	"ensisijaisetHakijat": data.any('ensisijaiset'),
		    	"kaikkiHakijat": data.any('hakijat'),
		    	"aloituspaikat": data.any('paikat'),
		    	"varasija": data.prioritize('', 0.9).otherwise(data.any('varasija')),
		    	"alinHyvaksytty": data.any('raja'),
		    	"omatPisteesi": data.any('pisteetvajaa'),
		    	"paasyJaSoveltuvuusKoe": data.any('koe'),
		    	"hylkayksenSyy": data.any('syy')
			}
		})
	}
	
	$scope.updateGenerated = function() {
		if ($scope.count > $scope.jalkiohjauskirjeet.length) {
			generateJalkiohjauskirje($scope.count - $scope.jalkiohjauskirjeet.length)
		} else if ($scope.count < $scope.jalkiohjauskirjeet.length) {
			$scope.jalkiohjauskirjeet.splice($scope.count, $scope.jalkiohjauskirjeet.length)
		}
	}
	$scope.removeJalkiohjauskirje = function(index) {
		$scope.jalkiohjauskirjeet.splice(index, 1)
		$scope.count--;
	}
	
	$scope.addHakukohde = function(index) {
		$scope.jalkiohjauskirjeet[index].tulokset.unshift({
	    	"koulu": "",
	    	"hakutoive": "",
	    	"ensisijaisetHakijat": "",
	    	"kaikkiHakijat": "",
	    	"aloituspaikat": "",
	    	"varasija": "",
	    	"alinHyvaksytty": "",
	    	"omatPisteesi": "",
	    	"paasyJaSoveltuvuusKoe": "",
	    	"hylkayksenSyy": ""
		});
	}
	
	$scope.removeHakukohde = function(kirjeIndex, hakukohdeIndex) {
		$scope.jalkiohjauskirjeet[kirjeIndex].tulokset.splice(hakukohdeIndex, 1);
	}
	
	$scope.addJalkiohjauskirje = function() {
		$scope.jalkiohjauskirjeet.unshift({
			"addressLabel": {
				"firstName": $scope.firstName,
				"lastName": $scope.lastName,
				"addressline": $scope.addressline,
				"addressline2": $scope.addressline2,
				"addressline3": $scope.addressline3,
				"postalCode": $scope.postalCode,
				"region": $scope.region,
				"city": $scope.city,
				"country": $scope.country
			},
			"tulokset": []
		})
		$scope.count++;
		$scope.firstName = '';
		$scope.lastName = '';
		$scope.addressline = '';
		$scope.addressline2 = '';
		$scope.addressline3 = '';
		$scope.postalCode = '';
		$scope.region = '';
		$scope.city = '';
		$scope.country = '';
	}
	$scope.generatePDF = function() {
		Printer.jalkiohjauskirjePDF($scope.jalkiohjauskirjeet)
	}
}]);
