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
        <link rel="stylesheet" href="<c:url value="css/statistiques_admin.css"/>">
        <!-- On charge les polices Google -->
        <link rel="stylesheet" href="<c:url value="https://fonts.googleapis.com/css?family=Oswald:400,500|Roboto:300,400,500,700"/>">
        <script type="text/javascript">
            google.load("visualization", "1", {packages: ["corechart"]});

            // Après le chargement de la page, on fait l'appel AJAX
            google.setOnLoadCallback(doAjax);

            // La fonction permettant de dessiner le diagramme en secteurs
            function drawChart(dataArray) {
                var data = google.visualization.arrayToDataTable(dataArray);
                var options = {
                    is3D: true
                };
                var chart = new google.visualization.PieChart(document.getElementById('piechart'));
                chart.draw(data, options);
            }

            // La fonction permettant de dessiner l'histogramme
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
                    url: "../salesByProduct",
                    data: {"datePickerDebut": $("#datePickerDebut").val(), "datePickerFin": $("#datePickerFin").val()},
                    dataType: "json",
                    success: // La fonction qui traite les résultats
                            function (result) {
                                // On reformate le résultat comme un tableau
                                var chartData = [];
                                // On met le descriptif des données
                                chartData.push(["Produit", "Ventes"]);
                                for (var produit in result.records) {
                                    chartData.push([produit, result.records[produit]]);
                                }
                                // On dessine les graphiques
                                drawChart(chartData);
                                drawHistogram(chartData);
                            },
                    error: showError
                });
            }

            // Fonction qui traite les erreurs de la requête
            function showError(xhr, status, message) {
                alert(JSON.parse(xhr.responseText).message);
            }

        </script>
    </head>
    <body>
        <!-- Le bouton Retour -->
        <input type="button" value="<< RETOUR" onclick="location.href = document.referrer; return false;">
        <!-- Le bouton Déconnexion -->
        <form action="../" method="POST"> <input type='submit' name='action' value='DECONNEXION'></form>
        <h1>Chiffre d'affaires par catégorie d'article</h1><br/>

        <!-- La période sur laquelle porte la statistique -->
        <p>Date de début : <input type="date" id="datePickerDebut" onchange="doAjax()" value="2011-01-01">
            Date de fin : <input type="date" id="datePickerFin" onchange="doAjax()" value="2018-04-20"></p>

        <!-- Le diagramme en secteurs apparaît ici -->
        <div id="piechart" style="width: 725px; height: 500px;"></div>
        <!-- L'histogramme apparaît ici -->
        <div id="histogram" style="width: 725px; height: 450px;"></div>
    </body>
</html>