//get ref to modal on load
var modal;
var registerContainer;
var closeButton;

window.onload = function() {
    modal = document.getElementById('modal-signup-id');
    registerContainer = document.getElementById('register-container');
    closeButton = document.getElementById('closeButton');
};

//method for opening modal from profile element

openModal = function() {
    modal.style.display = 'flex';
    registerContainer.style.display = 'none';
};

//methdod for closing the modal if user clicks anywhere else or the cross

closeModal = function() {
    modal.style.display = 'none';
    registerContainer.style.display = 'flex';
};


window.onclick = function(event) {
    if(event.target === modal) {
        modal.style.display = 'none';
        registerContainer.style.display = 'flex';
    };
};

