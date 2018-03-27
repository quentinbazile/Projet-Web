<%-- 
    Document   : PieChart
    Created on : 22 mars 2018, 09:30:02
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
        <script src="js/jquery-ui-datepicker.min.js"></script>
	<!-- On charge l'API Google -->
	<script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <link rel="stylesheet" type="text/css" href="views/css/stat.css">
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
        <script type="text/javascript">
            
            $(function(){
                //Datapicker
                $('#datepickerDebut').datepicker({
                    showOn:"button",
                    buttonImage: ,
                    
                    
                            
                    
                });
                $('#datepickerFin').datepicker({
                    showOn: "button",
                    buttonImage: ,
                    
                    
                });
            });
    
            
                
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {

        var data = google.visualization.arrayToDataTable([
          ['Task', 'Hours per Day'],
          ['Work',     11],
          ['Eat',      2],
          ['Commute',  2],
          ['Watch TV', 2],
          ['Sleep',    7]
        ]);

        var options = {
          title: 'My Daily Activities'
        };

        var chart = new google.visualization.PieChart(document.getElementById('piechart'));

        chart.draw(data, options);
      }
    </script>
            
           
            
    </head>
    <body>
        <h1>Chiffre d'affaire par catégorie d'articles</h1>
	
	<!-- Le graphique apparaît ici -->
	<div id="piechart" style="width: 900px; height: 500px;">Diagramme</div>
        
        <p>Date de début: <input type="date" id="datepickerDebut"></p>
        <p>Date de fin: <input type="date" id="datepickerFin"></p>
        
        
        
    </body>
</html>
