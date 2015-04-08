var pilihan=[];
pilihan[0] = "satu";
pilihan[1] = "dua";

function tambahSelect(namaDiv){
    var newDiv = document.createElement('div');
    var selectHTML = "";
    selectHTML = "<select>";
    for (i = 0; i < pilihan.length; i++){
        selectHTML += "<option value='" + pilihan[i] + "'>" + pilihan[i] + "</option>";
    }
    selectHTML += "</select>";
    newDiv.innerHTML = selectHTML;
    document.getElementById(namaDiv).appendChild(newDiv);
}