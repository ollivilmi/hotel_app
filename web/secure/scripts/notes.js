/* FOR STYLING <input type="file"> */
//modify <input> type file name when user chooses a file
const fileAdd = document.getElementById("file-add");

fileAdd.onchange = () => {
  const fileSelected = document.getElementById("file-selected");
  const splitString = fileAdd.value
    .split(/(\\|\/)/g)
    .pop()
    .split(".");
  console.log(splitString);
  const name = splitString[0].substr(0, 8);
  const ext = splitString[splitString.length - 1];
  splitString[0].length <= 8
    ? (fileSelected.innerHTML = name + "." + ext)
    : (fileSelected.innerHTML = name + "...." + ext);
  fileSelected.style.color = "black";
};

//Get username through cookie
const getCookie = cname => {
  const name = cname + "=";
  const decodedCookie = decodeURIComponent(document.cookie);
  const ca = decodedCookie.split(";");
  for (let i = 0; i < ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) == " ") {
      c = c.substring(1);
    }
    if (c.indexOf(name) === 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
};

/*---- FETCH NOTES ----*/

let apiUrl = "/management/r/notes";
let username = getCookie("user");

let headers = {
  user: username
};
// console.log(username);
// fetch(apiUrl + "/getNotes", {
//   headers: headers
// })
//   .then(res => res.json())
//   .then(function(json) {
//     console.log(json);
//     notesFetch(json);
//   })
//   .catch(error => console.log(error));

//Filter notes
let filterButton = document.querySelector("#filter-apply-button");
filterButton.addEventListener('click', function() {
  let filterType = document.querySelector("#filter-type").value
  let filterValue = document.querySelector('#filter-value').value
	let filterUrl = ""

  switch (filterType) {
    case 'department':
      filterUrl = apiUrl + '/byDepartment?departmentId=' + filterValue
      break
    case 'time':
      filterUrl = apiUrl + '/byTime?time=' + filterValue
      break
    case 'userId':
      filterUrl = apiUrl + '/byUserId?id=' + filterValue
      break
    default:
      filterUrl = apiUrl + '/getNotes'
      break
  }

	console.log(filterUrl)
	fetch(filterUrl, {
		headers: headers
	})
		.then(res => res.json())
		.then(function(json) {
			notesFetch(json)
		})
		.catch(error => console.log)
});



//Modify DOM element based on fetch notes
const notesFetch = notes => {
  
  console.log(notes)
  let htmlString = "";
  for (let note of notes) {
    htmlString +=
      '<div id="note-' + note.id + '" class="notes">' +
        '<input type="hidden" name="noteId" value="'+note.id+'"/>'+
        '<div class="notes-header">' +
          '<div class="notes-sender">' +
            '<div class="sender-avatar">' +
              '<img src="images/main/person1.png" alt="">' +
            "</div>" +
            '<div class="sender-name">' +
              "<h5>An Nguyen</h5>" +
              "<span>" + note.noteDate + "</span>" +
            "</div>" +
          "</div>" +
          '<div class="note-manager-buttons">' +
            '<button id="note-edit">Edit</button>' +
            '<button id="note-delete">Delete</button>' +
          "</div>" +
        "</div>" + 
        '<div class="notes-content">' +
          "<h1>"+ note.title + "</h1>" +
          "<p>" + note.contents + "</p>" +
      // +		'<img src="'+ note.imgUrl + '" alt="">'
          "<p>" + note.imgUrl + "</p>" +
        "</div>" +
        '<div class="notes-footer">' +
          '<p>Find employees to add note:</p>'+
          '<select id="search-type-' + note.id + '" class="dropdown-menu" onchange="changeOptions(this.value, ' + note.id + ')">'+
              '<option value="firstname">First name</option>'+
              '<option value="lastname">Last name</option>'+
              '<option value="username">Username</option>'+
              '<option value="job">Job</option>'+
              '<option value="department">Department</option>'+
          '</select>'+
          '<div id="search-option-' + note.id + '" class="sidemargins">'+
          '</div>'+
          '<button id="search-btn-' + note.id + '" class="sidemargins search-btn" onclick="searchButton('+ note.id + ')">Search</button>'+
          '<select id="user-search-container-' + note.id + '">'+
          '</select>'+
          '<button id="add-note-btn-' + note.id + '" class="sidemargins add-note-btn">Add note</button>'+
        "</div>" +
      "</div>";
  }
  document.querySelector("#notes-container").innerHTML = htmlString;
};

//Filter DOM

const changeFilters = (filterType) => {
  let filter = document.querySelector('#filter-option')
  document.querySelector('#notes-container').innerHTML = ''
  switch (filterType) {
    case 'department': 
      filter.innerHTML = '<select id="filter-value" class="dropdown-menu">'
      + '<option value="1">Restaurant</option>'
      + '<option value="2">Management</option>'
      + '<option value="3">Reception</option>'
      + '<option value="4">Maintenance</option>'
      + '</select>';
      break
    case 'time':
      filter.innerHTML = '<select id="filter-value" class="dropdown-menu" >'
      + '<option value="99">All</option>'
      + '<option value="0">Today</option>'
      + '<option value="7">This week</option>'
      + '<option value="1">This month</option>'
      + '</select>'
      break
    case 'userId': 
      filter.innerHTML = '<input type="text" id="filter-value" placeholder="Filter user id">'
      break
    default:
      filter.innerHTML = ''
      break;
  }
}

// Search more users to add note
const searchButton = (id) => {
        let apiString = "";
        let search = document.querySelector("#search-user-"+ id).value;
        let searchType = document.querySelector("#search-type-" + id).value;    
        
        switch (searchType)
        {
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
        headers: headers
    })
            .then(response => response.json())
            .then(function(json) {
                searchResults(json, id);
            })
            .catch(error => console.log(error)); 
}

const searchResults = (users, id) => {
  let resultString = ""
  for (let user of users) {
    resultString += "<option>" + user.firstName + " " + user.lastName + "</option>"
  }

  document.querySelector("#user-search-container-" + id).innerHTML = resultString;
}

const changeOptions = (searchType, id) => {
  let search = document.querySelector("#search-option-" + id);
  search.innerHTML = getOptions(searchType, id);
  document.querySelector("#user-search-container-" + id).innerHTML = "";
};

let jobs = []

const getOptions = (searchType, id) => {
  switch (searchType)
  {
      case "department":
        console.log('here')
          return  '<select id="search-user-' + id + '" class="dropdown-menu">'
                  + '<option selected disabled>Choose department</option>'
                  + getManagementOptions()
                  + '</select>';
          break;
      case "job":
          return  '<select class="dropdown-menu sidemargins" onchange="changeJobOptions(this.value, ' + id + ')">'
                  + '<option selected disabled>Choose department</option>'
                  + getManagementOptions()
                  + '</select>'
                  + '<br>'
                  + '<select id="search-user-' + id + '" class="dropdown-menu sidemargins">'
                  + '<option selected disabled>Choose job</option>'
                  + jobs[1]
                  + '</select>';
          break;
      default:
          return '<input class="profile-input name-search" type="text" placeholder="Enter name" id="search-user-' + id + '">';
          break;
  }
};

const getManagementOptions = () => {
  return  '<option value="1">Restaurant</option>'
          + '<option value="2">Management</option>'
          + '<option value="3">Reception</option>'
          + '<option value="4">Maintenance</option>';
};

const changeJobOptions = (department, id) => {
  let search = document.querySelector("#search-user-" + id);
  getJobOptions(department).then(function(result) {
     search.innerHTML = result; 
  });
};

const getJobOptions = (department) => {
  
  return fetch("/management/r/jobs/byDepartment?dptId="+department)
        .then(response => response.json())
        .then(function(json) 
  {
      let jobSelect = "";
      
      for(let job of json)
          jobSelect += '<option value="' + job.id + '">' + job.title + '</option>';
      
      return jobSelect;
  })
          .catch(error => console.log(error));  
};

const jobOptions = (element, value) =>
{
element.innerHTML = jobs[value];  
};
