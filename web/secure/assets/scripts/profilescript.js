/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



var editButton;
var saveButton;
var cancelButton;
var formInputFields;

var green = '#4CAF50';
var red = '#f44336';
var blue = '#008CBA';

window.onload = function () {
    editButton = document.getElementById('edit-btn');
    editButton.addEventListener('click', editInfo);

    saveButton = document.getElementById('save-btn');

    cancelButton = document.getElementById('cancel-btn');

    formInputFields = document.getElementById('info-form').elements;

};

// Edit user information
editInfo = function () {
    console.log('edit called');
    enableButtons();
    enableInputs();
};

//Enable button

enableButtons = function () {
    saveButton.disabled = false;
    saveButton.style.backgroundColor = green;
    cancelButton.disabled = false;
    cancelButton.style.backgroundColor = red;
};

enableInputs = function () {
    for (var i = 0; i < formInputFields.length; i++) {
        formInputFields[i].disable = false;
    }
};