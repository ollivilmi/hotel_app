//get ref to modal on load
var modal;
var registerContainer;
var closeButton;

window.onload = function() {
    modal = document.getElementById('modal-signup-id');
    registerContainer = document.getElementById('register-container');
    closeButton = document.getElementById('closeButton');
    let loginUser = document.querySelector("#login-username");
    let loginAlert = document.querySelector("#login-alert");
    let registerUser = document.querySelector("#register-username");
    let registerAlert = document.querySelector("#register-alert");
    
    
    // Script for checking username during login
    loginUser.addEventListener("focusout", function() {checkUsernameLogin(this, loginAlert);} );
    loginUser.addEventListener("focus", function() {clearAlert(loginUser, loginAlert);} );
    
    // Script for checking username when registering
    registerUser.addEventListener("focusout", function() {checkUsernameRegister(this, registerAlert);} );
    registerUser.addEventListener("focus", function() {clearAlert(registerUser, registerAlert);} );
};

const checkUsernameLogin = (username, alert) => {
    fetch("/management/r/users/userExists?user="+username.value)
        .then(response => response.json())
        .then(function(check) {
            if (check[0].status === "false"){
                alert.innerHTML = "<i>Username not found</i>";
            }
            else clearAlert(username, alert);
        })
         .catch(error => console.log(error));
};

const checkUsernameRegister = (username, alert) => {
    fetch("/management/r/users/userExists?user="+username.value)
        .then(response => response.json())
        .then(function(check) {
            if (check[0].status === "true"){
                username.style.border = "1px solid red";
                alert.innerHTML = "<i>Username already exists</i>";
            }
            else {
                clearAlert(username, alert);
            }
        })
         .catch(error => console.log(error));
};

const clearAlert = (username, alert) => {
    alert.innerHTML = "";
    username.style.border = "none";
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
