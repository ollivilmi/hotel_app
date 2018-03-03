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


    saveButton = document.getElementById('save-btn');

    cancelButton = document.getElementById('cancel-btn');

    formInputFields = document.getElementsByClassName('profile-input');

    console.log(profilePopup);
    
    let user = getCookie("user");
    console.log(user);
    
    fetch("/management/r/users/u", {
        headers: {
            'user': user
        }
    })
            .then(response => response.json())
            .then(function(json) {
                fillProfile(json);
            })
            .catch(error => console.log(error));
            
    fetch("/management/r/users/manager/getNewUsers", {
        headers: {
            'user': user
        }
    })
            .then(response => response.json())
            .then(function(json) {
                newUsers(json);
    })
            .catch(error => console.log(error));
    
    requestPicture = document.getElementById('request-profile-picture');

    requestPicture.onmouseover = function () {
        console.log('mouse enter called');
        ShowInfoCard();
    };
    
    requestPicture.onclick = function () {
        if (profilePopup.style.display === "none") {
            profilePopup.style.display = "block";
        } else {
            profilePopup.style.display = "none";
        }
    };
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

const getCookie = (cname) => {
    const name = cname + "=";
    const decodedCookie = decodeURIComponent(document.cookie);
    const ca = decodedCookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
};

const fillProfile = (info) => {
  let department = document.querySelector("#department");
  let job = document.querySelector("#job");
  let firstname = document.querySelector("#firstname");
  let lastname = document.querySelector("#lastname");
  let username = document.querySelector("#username");
  let telnumber = document.querySelector("#telnumber");
  let email = document.querySelector("#email");
  
  department.innerHTML = info[0].department;
  job.innerHTML = info[0].job;
  firstname.value = info[0].first_name;
  lastname.value = info[0].last_name;
  username.value = info[0].username;
  telnumber.value = info[0].phone;
  email.value = info[0].email;  
};

const newUsers = (info) => {
    let requestString = "";
    for (let item of info)
    {
        console.log(item);
        requestString += "<div class='request-person'"
        +"div class='request-person-static'>"
        +"<p class='request-container-element' id='request-name'>" + item.firstName + " " + item.lastName + "</p>"
        +"<img class='request-profile-picture' id='request-profile-picture' src='images/person-icon.png'>"
        +"<button type='submit' id='accept-request-btn' class='request-container-element request-button' name='request-accept'>Accept</button>"
        +'<button type="submit" id="decline-request-btn" class="request-container-element request-button" name="request-decline">Decline</button>'
        +"</div>"
        +'<span class="popup-text" id="info-card-popup">This will be a popup for more info on the user</span>'
        +"</div>";
    }
    document.querySelector(".request-container").innerHTML = requestString;
};