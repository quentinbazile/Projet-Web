<%-- 
    Document   : ChiffreZoneGeo
    Created on : 22 mars 2018, 10:02:08
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
        
        
        
        
        
        
        
        
        </script>
    </head>
    
    
    
    
    <body>
        <h1>Chiffre d'affaire par zone géographique</h1>
	
	<!-- Le graphique apparaît ici -->
	<div id="piechart" style="width: 900px; height: 500px;"></div>
        
        
        <p>Date de début: <input type="text" id="datepickerDebut"></p>
        <p>Date de fin: <input type="text" id="datepickerFin"></p>
        
    </body>
</html>
