/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



var editButton;
var saveButton;
var cancelButton;
var formInputFields;
var requestPicture;
var profilePopup;

var green = '#4CAF50';
var red = '#f44336';
var blue = '#008CBA';

window.onload = function () {

    editButton = document.getElementById('edit-btn');
    // editButton.addEventListener('click', editInfo);

    requestPicture = document.getElementById('request-profile-picture');



    requestPicture.onmouseover = function () {
        console.log('mouse enter called');
        ShowInfoCard();
    };
    
    requestPicture.onclick = function (){
        if (profilePopup.style.display === "none") {
            profilePopup.style.display = "block";
        } else {
            profilePopup.style.display = "none";
        }
    };


    saveButton = document.getElementById('save-btn');

    cancelButton = document.getElementById('cancel-btn');

    formInputFields = document.getElementsByClassName('profile-input');

    console.log(profilePopup);
    
    let currentUser;
    fetch("/management/currentUser")
            .then(response => response.json())
            .then(function(json){
                console.log(json);
            })
            .catch(error => console.log(error));
            
            
};

// Edit user information
EditInfo = function () {
    console.log('edit called');
    //enableButtons();
    //enableInputs();
};

//Enable button

EnableButtons = function () {
    saveButton.disabled = false;
    saveButton.style.backgroundColor = green;
    cancelButton.disabled = false;
    cancelButton.style.backgroundColor = red;
};

EnableInputs = function () {
    for (var i = 0; i < formInputFields.length; i++) {
        formInputFields[i].disabled = false;
    }
};

//show info card on hover

ShowInfoCard = function () {
    console.log('show infocard called');
    profilePopup = document.getElementById("info-card-popup");
    profilePopup.classList.toggle('show');
};