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
    return false;
}

// Modifier une commande
function updateCommande(order_num, qte) {
    $.ajax({
        url: "updateCommande",
        data: {"order_num": order_num, "qte": qte},
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

function paramUpdateCommande(order_num) {
    let res;
    if(document.getElementById("btnUpdate"+order_num).innerHTML === "Modifier"){
        document.getElementById("btnUpdate"+order_num).innerHTML = "Valider";
        let qte = document.getElementById("qte"+order_num).innerHTML;
        document.getElementById("qte"+order_num).innerHTML = '<input id="new' + order_num + '" class="qte" name="qte" value="' + qte + '" type="number">';
        res = qte;
    }
    else{
        document.getElementById("btnUpdate"+order_num).innerHTML = "Modifier";
        let newQte = document.getElementById("new"+order_num).value;
        document.getElementById("qte"+order_num).innerHTML = newQte;
        updateCommande(order_num, newQte);
    }
}

// Fonction qui traite les erreurs de la requête
function showError(xhr, status, message) {
    alert(JSON.parse(xhr.responseText).message);
}