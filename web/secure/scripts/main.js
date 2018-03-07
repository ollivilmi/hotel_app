
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

fetch(apiUrl + '/getNotes', {
  headers: headers
})
  .then(res => res.json())
  .then(function(json) {
    notesFetch(json)
  })
  .catch(error => console.log)
  
fetch("/management/r/users/byUsername?username="+username, {
      headers: {
          'user': username
      }
  })
          .then(response => response.json())
          .then(function(json) {
            if (json[0].permissionsId !== 1 && json[0].permissionsId !== null)
            {
                document.querySelector("#note-button-container")
                .innerHTML = '<a href="/management/secure/manager/notemanager.html" id="note-button">Note</a>';
            }
          })
          .catch(error => console.log(error));
  


//Modify DOM element based on fetch notes
const notesFetch = notes => {
    let htmlString = "";
    if (notes.length === 0)
    {
        document.querySelector(".main-board").innerHTML += '<h1>No notes found</h1>';
    }
    else
    {
        for (let note of notes) {
          htmlString += '<div id="note-' + note.id + '" class="notes">' +
              '<div class="notes-header">' +
                "<h1>"+ note.title + "</h1>" +
              "</div>" +
              '<div class="notes-content">' +
                "<p>" + note.contents + "</p>" +
            // +		'<img src="'+ note.imgUrl + '" alt="">'
                //"<p>" + note.imgUrl + "</p>" +
                // Add if condition here - if note.imUrl is not null then add it.
              "</div>" +
              '<div class="notes-footer">' +
                  "<span>" + note.noteDate + "</span>" +
              "</div>" +
            "</div>";
        }
        document.querySelector(".main-board").innerHTML += htmlString;
    }

};

