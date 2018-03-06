var editButton;
var saveButton;
var cancelButton;
var formInputFields;
var requestPicture;
var profilePopup;
var user;

var green = '#4CAF50';
var red = '#f44336';
var blue = '#008CBA';
//variables for modal container
var modal;
var registerContainer;
var closeButton;

window.onload = function () {
    //Get elements for modal container
    modal = document.getElementById('modal-edit-id');
    registerContainer = document.getElementById('register-container');
    closeButton = document.getElementById('closeButton');

    saveButton = document.getElementById('save-btn');

    formInputFields = document.getElementsByClassName('profile-input');
    
    //Current session cookie to identify we are logged in for resources
    user = getCookie("user");
    
    //Fetch profile information for the logged in user
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
    
    document.querySelector("#management-info").style.visibility = "hidden";
    
    fetch("/management/r/users/byUsername?username="+user, {
        headers: {
            'user': user
        }
    })
            .then(response => response.json())
            .then(function(json) {
                if (json[0].permissionsId !== 1 && json[0].permissionsId !== null)
            {
                buildManagementWindow();
            }
            })
            .catch(error => console.log(error));
    
};
    
const buildManagementWindow = () => 
{
    document.querySelector("#management-info").style.visibility = "visible";
        
    //Fetch users without permissions to log in for managers to accept
    fetch("/management/r/users/getNewUsers", {
        headers: {
            'user': user
        }
    })
            .then(response => response.json())
            .then(function(json) {
                newUsers(json);
    })
            .catch(error => console.log(error));
    /*
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
    }; */
    
    // Search users to edit
    
    let searchType = document.querySelector("#searchtype");
    let searchButton = document.querySelector("#search-btn");
 
    searchButton.addEventListener("click", function() 
    {
        let apiString = "";
        let search = document.querySelector("#search-user").value;
        let searchType = document.querySelector("#search-type").value;    
        
        switch (searchType)
        {
            case "unassigned":
                apiString = "/management/r/users/unassigned";
                break;
            case "username":
                apiString = "/management/r/users/byUsername?username="+search;
                break;
            case "firstname":
                apiString = "/management/r/users/byFirstName?firstName="+search;
                break;
            case "lastname":
                apiString = "/management/r/users/byLastName?lastName="+search;
                break;
            case "job":
                apiString = "/management/r/users/byJobId?jobId="+search;
                break;
            case "department":
                apiString = "/management/r/users/byDepartment?department="+search;
                break;
            default:
                apiString = "/management/r/users";
                break;
        }
        
        
        fetch(apiString, {
        headers: {
            'user': user
        }
    })
            .then(response => response.json())
            .then(function(json) {
                searchResults(json);
            })
            .catch(error => console.log(error)); 
    });
    
    changeOptions("unassigned");
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
  let firstname = document.querySelector("#firstname");
  let lastname = document.querySelector("#lastname");
  let username = document.querySelector("#username");
  let telnumber = document.querySelector("#telnumber");
  let email = document.querySelector("#email");
  
  firstname.value = info[0].first_name;
  lastname.value = info[0].last_name;
  username.value = info[0].username;
  telnumber.value = info[0].phone;
  email.value = info[0].email;  
};

const newUsers = (users) => {
    let requestString = "";
    for (let user of users)
    {
        requestString += "<form action='/management/r/users/manager/acceptUser' method='POST' onsubmit=\"this.style.display = 'none'\">"
        +'<input type="hidden" name="username" value="'+user.username+'"/>'
        +"<div class='request-person'>"
        +"<div class='request-person-static'>"
        +"<p class='request-container-element' id='request-name'>" + user.firstName + " " + user.lastName + "</p>"
        +"<img class='request-profile-picture' id='request-profile-picture' src='images/person-icon.png'>"
        +"<button type='submit' class='request-container-element request-button' name='accept' value='accept'>Accept</button>"
        +'<button type="submit" class="request-container-element request-button">Decline</button>'
        +"</div>"
        +'<span class="popup-text" id="info-card-popup">This will be a popup for more info on the user</span>'
        +"</div>"
        +"</form>";
    }
    document.querySelector(".request-container").innerHTML = requestString;
};

const searchResults = (users) => {
    let resultString = "";
    for (let user of users)
    {
        resultString += '<div class="search-person">'
        + '<div class="search-person-static">'
        + '<p class="search-container-element" id="search-name">'+user.firstName + " " + user.lastName + '</p>'      
        + '<img class="search-profile-picture" id="search-profile-picture" src="images/person-icon.png">'
        + '<button type="submit" id="edit-user-btn" class="search-container-element" name="request-accept" onclick="openModal()>Edit</button>'
        + '</div>'
        + '<span class="popup-text" id="info-card-popup">This will be a popup for more info on the user</span>'
        + '</div>';
    }
    document.querySelector(".search-result-container").innerHTML = resultString;
};

const changeOptions = (searchType) => {
    let search = document.querySelector("#search-option");
    document.querySelector("#user-search-container").innerHTML = "";
    switch (searchType)
    {
        case "department":
            search.innerHTML =  'Choose department '
                                +'<select id="search-user" class="search-department-dropdown">'
                                + '<option value="1">Restaurant</option>'
                                + '<option value="2">Management</option>'
                                + '<option value="3">Reception</option>'
                                + '<option value="4">Maintenance</option>'
                                + '</select>';
            break;
        case "job":
        search.innerHTML =  'Choose department '
                            +'<select id="search-user" class="search-department-dropdown" onchange="jobOptions(this.value)">'
                            + '<option value="1">Restaurant</option>'
                            + '<option value="2">Management</option>'
                            + '<option value="3">Reception</option>'
                            + '<option value="4">Maintenance</option>'
                            + '</select>';
            break;
        case "unassigned":
            search.innerHTML = '<input type="hidden" id="search-user"/>';
            break;
        default:
            search.innerHTML = '<input class="search-name-element" type="text" placeholder="Enter name" id="search-user">';
            break;
    }
};

const jobOptions = (department) => {
    let search = document.querySelector("#search-option");
    fetch("/management/r/jobs/byDepartment?dptId="+department)
          .then(response => response.json())
          .then(function(json) 
    {
        let jobSelect = 'Choose job <select id="search-user" class="search-department-dropdown">';
        for(let job of json)
        {
            jobSelect += '<option value="' + job.id + '">' + job.title + '</option>';
        }
        jobSelect += '</select>';
        search.innerHTML = jobSelect;
    })
            .catch(error => console.log(error));  
};


// method for openin edit modal for admin

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
    }
};
