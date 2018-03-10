
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
        for (let note of notes) 
        {
            htmlString += '<div id="note-' + note.id + '" class="notes">' +
                          '<div class="notes-header">' +
                          "<h1>"+ note.title + "</h1>" +
                          "</div>" +
                          '<div class="notes-content">' +
                          "<p>" + note.contents + "</p>";
            if (note.imgUrl !== null)
            {
                let url = '/management/images/'+ note.imgUrl + '';
                htmlString += '<a class="noteImgLink" href="'+ url +'"><img src="'+ url +'" alt="" style="max-height:5em; width:auto;"/></a>';
            }
            htmlString += "</div>" +
                          '<div class="notes-footer">' +
                          noteStatus(note.status, note.id) +
                          "<span>" + note.noteDate + "</span>" +
                          "</div>" +
                          "</div>";
        }
        document.querySelector(".main-board").innerHTML += htmlString;
    }

};

const noteStatus = (status, id) => {
    let returnForm = '<form onsubmit="setTimeout(function(){window.location.reload();},10)" action="/management/r/notes/manager/updateNoteStatus" method="POST">' +
                     '<input type="hidden" name="id" value=' + id + ' />';
  switch (status)
  {
    case 0:
        returnForm += '<input type="hidden" name="status" value=1 />' +
                      '<label>New task!</label>' +
                      '<input class="accept-task" type="submit" value="Accept task">';
        break;
    case 1:
        returnForm += '<input type="hidden" name="status" value=2 />' +
                      '<label>Task in progress</label>' +
                      '<input class="progress-task" type="submit" value="Complete task">';
        break;
    case 2:
        returnForm += '<input class="complete-task" type="submit" name="status" value="Done" disabled>';
        break;
  }
  returnForm += '</form>';
  return returnForm;
};