<%-- 
    Document   : ChiffreClient
    Created on : 22 mars 2018, 10:02:40
    Author     : csalagna
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Statistiques</title>
	<!-- On charge JQuery -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
	<!-- On charge l'API Google -->
	<script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript">
            
            google.load("visualization", "1", {packages: ["corechart"]});
            // Après le chargement de la page, on fait l'appel AJAX
            google.setOnLoadCallback(doAjax);
            
            function drawChart(dataArray) {
			var data = google.visualization.arrayToDataTable(dataArray);
			var options = {
				title: 'Ventes par catégories',
				is3D: true
			};
			var chart = new google.visualization.PieChart(document.getElementById('piechart'));
			chart.draw(data, options);
            }
            // Afficher les ventes par client
		function doAjax() {
			$.ajax({
				url: "salesByCustomer",
				dataType: "json",
				success: // La fonction qui traite les résultats
					function (result) {
						// On reformate le résultat comme un tableau
						var chartData = [];
						// On met le descriptif des données
						chartData.push(["Product", "Description"]);
						for(var client in result.records) {
							chartData.push([client, result.records[client]]);
						}
						// On dessine le graphique
						drawChart(chartData);
					},
				error: showError
			});
		}
		
		// Fonction qui traite les erreurs de la requête
		function showError(xhr, status, message) {
			alert("Erreur: " + status + " : " + message);
		}
            
            
            
            
            
            
            
            
         </script>   
    </head>
    <body>
        <h1>Chiffre d'affaire par client</h1>
	
	<!-- Le graphique apparaît ici -->
	<div id="piechart" style="width: 900px; height: 500px;"></div>
        
        
        <p>Date de début: <input type="date" id="datepickerDebut"></p>
        <p>Date de fin: <input type="date" id="datepickerFin"></p>
        
    </body>
</html>
