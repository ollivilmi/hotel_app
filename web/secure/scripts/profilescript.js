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
var registerContainer;
var closeButton;
var jobs = [];

window.onload = function () {
    //Get elements for modal container
    modal = document.getElementById('modal-edit-id');
    registerContainer = document.getElementById('register-container');
    closeButton = document.getElementById('closeButton');

    saveButton = document.getElementById('save-btn');

    formInputFields = document.getElementsByClassName('profile-input');
    
    for(let i = 1; i<5; i++)
        getJobOptions(i).then(function(result) {
           jobs[i] = result;
        });
    
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
    // Make manager options visible
    document.querySelector("#management-info").style.visibility = "visible";
    document.querySelector("#note-button-container")
                .innerHTML = '<a href="/management/secure/manager/notemanager.html" id="note-button">Manager</a>';
    document.querySelector("#note-button-container").classList.add("nav-menu-items-style");
    
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
            .then(function(users) {
                searchResults(users);
            })
            .catch(error => console.log(error)); 
    });
    
    changeOptions("unassigned");
};

const fillProfile = (info) => {
  document.querySelector("#firstname").value = info[0].first_name;
  document.querySelector("#lastname").value = info[0].last_name;
  document.querySelector("#username").value = info[0].username;
  document.querySelector("#telnumber").value = info[0].phone;
  document.querySelector("#email").value = info[0].email;
  document.querySelector("#profile-picture").src = info[0].picture;
};


const newUsers = (users) => {
    let requestString = "";
    for (let user of users)
    {
        requestString += "<form action='/management/r/users/manager/acceptUser' method='POST' onsubmit=\"this.style.display = 'none'\">"
        +'<input type="hidden" name="username" value="'+user.username+'"/>'
        +"<div class='request-person'>"
        +"<div class='request-person-static'>"
        +"<p class='request-container-element'>" + user.firstName + " " + user.lastName + "</p>"
        +"<img class='request-profile-picture' src='"+ user.picture +"'>"
        +"<button type='submit' class='request-container-element request-button accept-request-btn' name='accept' value='accept'>Accept</button>"
        +'<button type="submit" class="request-container-element request-button decline-request-btn">Decline</button>'
        +"</div>"
        +"</div>"
        +"</form>";
    }
    document.querySelector(".request-container").innerHTML = requestString;
};

const searchResults = (users) => {
    let resultString = "";
    for (let user of users)
    {
        resultString += "<form action='/management/r/users/manager/setUserJob' method='POST'>"
        + '<input type="hidden" name="username" value="'+user.username+'"/>'
        + "<div class='request-person'>"
        + "<div class='request-person-static'>"
        + '<p class="w20">'+user.firstName + " " + user.lastName + " (" + user.username + ")" + '</p>'      
        + '<img class="request-profile-picture" src="' + user.picture + '"/>'
        + '<div class="select-wrapper sidemargins">'
        + '<select class="dropdown-menu sidemargins" onchange="jobOptions(this.nextSibling, this.value)">'
        + getManagementOptions()
        + '</select>'
        + '<select name="job" class="dropdown-menu sidemargins">'
        + jobs[1]
        + '</select>'
        + '<button type="submit" class="request-container-element request-button edit-user-btn">Change job</button>'
        + '</div>'
        + '</form>'
        + '<form class="sidemargins" action="/management/r/users/manager/setUserPermissions" method="POST">'
        +' <input type="hidden" name="username" value="'+user.username+'"/>'
        + '<select name="perm" class="dropdown-menu sidemargins">'
        + '<option value="0">None</option>'
        + '<option value="1">Employee</option>'
        + '<option value="2">Manager</option>'
        + '</select>'
        + '<button type="submit" class="request-container-element request-button edit-user-btn">Change permissions</button>'
        + '</form>'
        + '</div>'
        + '</div>';
        
    }
    document.querySelector("#user-search-container").innerHTML = resultString;
};

const changeOptions = (searchType) => {
    let search = document.querySelector("#search-option");
    search.innerHTML = getOptions(searchType);
    document.querySelector("#user-search-container").innerHTML = "";
};
    
const getOptions = (searchType) => {
    switch (searchType)
    {
        case "department":
            return 'Choose department '
                    +'<select id="search-user" class="dropdown-menu">'
                    + getManagementOptions()
                    + '</select>';
            break;
        case "job":
            return  'Choose department '
                    +'<select class="dropdown-menu sidemargins" onchange="changeJobOptions(this.value)">'
                    + getManagementOptions()
                    + '</select>'
                    + '<br>'
                    + 'Choose job '
                    + '<select id="search-user" class="dropdown-menu sidemargins">'
                    + jobs[1]
                    + '</select>';
            break;
        case "unassigned":
            return '<input type="hidden" id="search-user"/>';
            break;
        default:
            return '<input class="profile-input name-search" type="text" placeholder="Enter name" id="search-user">';
            break;
    }
};

const changeJobOptions = (department) => {
    let search = document.querySelector("#search-user");
    getJobOptions(department).then(function(result) {
       search.innerHTML = result; 
    });
};