<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html> 
    <head>
        <title>Statistiques</title>
	<!-- On charge JQuery -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
	<!-- On charge l'API Google -->
	<script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <!-- On charge le fichier CSS associé -->
        <link rel="stylesheet" href="<c:url value="css/statAdmin3.css"/>">
        <link rel="stylesheet" href="<c:url value="https://fonts.googleapis.com/css?family=Oswald:400,500|Roboto:300,400,500,700"/>">
	<script type="text/javascript">
		google.load("visualization", "1", {packages: ["corechart"]});

		// Après le chargement de la page, on fait l'appel AJAX
		google.setOnLoadCallback(doAjax);
		
		function drawChart(dataArray) {
			var data = google.visualization.arrayToDataTable(dataArray);
			var options = {
				is3D: true
			};
			var chart = new google.visualization.PieChart(document.getElementById('piechart'));
			chart.draw(data, options);
		}
                
                function drawHistogram(dataArray) {
			var data = google.visualization.arrayToDataTable(dataArray);
			var options = {
                                colors: ['#EC644B']
			};
			var chart = new google.visualization.Histogram(document.getElementById('histogram'));
			chart.draw(data, options);
		}

		// Afficher les ventes par client
		function doAjax() {
			$.ajax({
				url: "../salesByZone",
                                data: {"datePickerDebut": $("#datePickerDebut").val(), "datePickerFin": $("#datePickerFin").val()},
				dataType: "json",
				success: // La fonction qui traite les résultats
					function (result) {
						// On reformate le résultat comme un tableau
						var chartData = [];
						// On met le descriptif des données
						chartData.push(["Zone", "Ventes"]);
						for(var zone in result.records) {
							chartData.push([zone, result.records[zone]]);
						}
						// On dessine le graphique
						drawChart(chartData);
                                                drawHistogram(chartData);
					},
				error: showError
			});
		}
		
		// Fonction qui traite les erreurs de la requête
		function showError(xhr, status, message) {
			//alert("Erreur: " + status + " : " + message);
		}

	</script>
</head>
    <body>
        <h1>Chiffre d'affaire par zone géographique</h1><br/>
        
        <p>Date de début : <input type="date" id="datePickerDebut" onchange="doAjax()">
           Date de fin : <input type="date" id="datePickerFin" onchange="doAjax()"></p>
        
        <!-- Le graphique apparaît ici -->
	<div id="piechart" style="width: 900px; height: 500px;"></div>
        <div id="histogram" style="width: 725px; height: 450px;"></div>
    </body>
</html>
