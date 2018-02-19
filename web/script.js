//get ref to modal on load

window.onload = function() {
    modal = document.getElementById('signupModal');
};

//method for opening modal from profile element

openModal = function() {
    modal.style.display = 'block';
};

//methdod for closing the modal if user clicks anywhere else

window.onclick = function(event) {
    if(event.target == modal) {
        modal.style.display = 'none';
    };
};

