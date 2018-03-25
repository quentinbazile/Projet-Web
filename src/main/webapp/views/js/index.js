$(document).ready(// Exécuté à la fin du chargement de la page
        function () {
            // On montre la liste des produits et des commandes
            showProducts();
            showOrders();
        }
);

function showProducts() {
    // On fait un appel AJAX pour chercher les produits
    $.ajax({
        url: "listeProduits",
        dataType: "json",
        error: showError,
        success: // La fonction qui traite les résultats
                function (result) {
                    // Le code source du template est dans la page
                    var template = $('#productsTemplate').html();
                    // On combine le template avec le résultat de la requête
                    var processedTemplate = Mustache.to_html(template, result);
                    // On affiche la liste des options dans le select
                    $('#products').html(processedTemplate);
                }
    });
}

function showOrders() {
    // On fait un appel AJAX pour chercher les produits
    $.ajax({
        url: "listeCommandes",
        dataType: "json",
        error: showError,
        success: // La fonction qui traite les résultats
                function (result) {
                    // Le code source du template est dans la page
                    var template = $('#ordersTemplate').html();
                    // On combine le template avec le résultat de la requête
                    var processedTemplate = Mustache.to_html(template, result);
                    // On affiche la liste des options dans le select
                    $('#orders').html(processedTemplate);
                }
    });
}

// Ajouter une commande
function addCommande(product_id) {
    $.ajax({
        url: "addCommande",
        data: {"product_id": product_id, "qte": $("#qte" + product_id).val(), "fc": $("#fc" + product_id).val()},
        dataType: "json",
        success: // La fonction qui traite les résultats
                function (result) {
                    showOrders();
                    console.log(result);
                },
        error: showError
    });
    showProducts();
    return false;
}

// Modifier une commande
function updateCommande(order_num, qte, fc) {
    $.ajax({
        url: "updateCommande",
        data: {"order_num": order_num, "qte": qte, "fc": fc},
        dataType: "json",
        success:
                function (result) {
                    showOrders();
                    console.log(result);
                },
        error: showError
    });
    return false;
}

function paramUpdateCommande(order_num) {
    if(document.getElementById("btnUpdate"+order_num).innerHTML === "Modifier"){
        document.getElementById("btnUpdate"+order_num).innerHTML = "Valider";
        let qte = document.getElementById("qte"+order_num).innerHTML;
        let product_id = document.getElementById("product_id"+order_num).innerHTML;
        let qteMax = document.getElementById("qteMax"+product_id).innerHTML;
        document.getElementById("qte"+order_num).innerHTML = '<input id="newQte' + order_num + '" class="qte" name="qte" value="' + qte + '" type="number" step="1" min="1" max="' + qteMax + '">';
        let fc = document.getElementById("fc"+order_num).innerHTML;
        document.getElementById("fc"+order_num).innerHTML = 
                '<select class="fc" id="newFc' + order_num + '">\n\
                    <option value="Coastal Freight">Coastal Freight</option>\n\
                    <option value="FR Express">FR Express</option>\n\
                    <option value="Poney Express">Poney Express</option>\n\
                    <option value="Slow Snail">Slow Snail</option>\n\
                    <option value="Southern Delivery Service">Southern Delivery Service</option>\n\
                    <option value="We deliver">We deliver</option>\n\
                    <option value="Western Fast">Western Fast</option>\n\
                </select>';
        document.getElementById("newFc"+order_num).value = fc;
    }
    else{
        document.getElementById("btnUpdate"+order_num).innerHTML = "Modifier";
        let newQte = document.getElementById("newQte"+order_num).value;
        let newFc = document.getElementById("newFc"+order_num).value;
        updateCommande(order_num, newQte, newFc);
    }
}

// Supprimer une commande
function deleteCommande(order_num) {
    $.ajax({
        url: "deleteCommande",
        data: {"order_num": order_num},
        dataType: "json",
        success:
                function (result) {
                    showOrders();
                    console.log(result);
                },
        error: showError
    });
    return false;
}

// Fonction qui traite les erreurs de la requête
function showError(xhr, status, message) {
    alert(JSON.parse(xhr.responseText).message);
}