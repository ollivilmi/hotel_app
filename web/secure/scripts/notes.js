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
          "Add more receivers:" +
          '<input type="text" name="employeeIds" id="add-employee-ids">' +
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
      filter.innerHTML = '<select id="filter-value">'
      + '<option value="1">Restaurant</option>'
      + '<option value="2">Management</option>'
      + '<option value="3">Reception</option>'
      + '<option value="4">Maintenance</option>'
      + '</select>';
      break
    case 'time':
      filter.innerHTML = '<select id="filter-value" >'
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