
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
let noteId;

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
  
// Polling server to every minute to check for new notes
// and to refresh page if there are
setInterval(function()
{
    fetch(apiUrl + '/getNotes', {
    headers: headers
    })
    .then(res => res.json())
    .then(function(json) {
        if (json[0].id > noteId)
            location.reload();
    });
}, 60*1000);
  
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
                .innerHTML = '<a href="/management/secure/manager/notemanager.html" id="note-button">Manager</a>';
            }
          })
          .catch(error => console.log(error));
  


//Modify DOM element based on fetch notes
const notesFetch = notes => {
    let mainBoard = document.querySelector(".main-board");
    let htmlString = "";
    let count = 0;
    noteId = notes[0].id;
    
    for (let note of notes) 
    {
        if (note.status !== 2)
        {
            count += 1;
            htmlString += '<div id="note-' + note.id + '" class="notes">' +
                          '<div class="notes-header">' +
                          '<button class="note-title" onclick="showContent(this.parentNode.nextSibling)">'+ note.title +'</button>' +
                          '</div>' +
                          '<div class="notes-content">' +
                          "<p class='note-text'>" + note.contents + "</p>" +
                          noteImage(note.imgUrl) +
                          "</div>" +
                          '<div class="notes-footer">' +
                          noteStatus(note.status, note.id) +
                          "<span>" + note.noteDate + "</span>" +
                          noteType(note.departmentId) +
                          "</div>" +
                          "</div>";
        }
    }
    mainBoard.innerHTML += htmlString;
    if (count === 1)
        document.querySelector("#note-count").innerHTML = "" + count + " task";
    else document.querySelector("#note-count").innerHTML = "" + count + " tasks";
};

const showContent = content => {
    if (content.style.display === "flex")
        content.style.display = "none";
    else content.style.display = "flex";
};

const noteImage = imgUrl => {
    if (imgUrl !== null)
    {
      let url = imgUrl;
      return '<a class="noteImgLink" href="'+ url +'"><img src="'+ url +'" alt="" style="max-height:5em; width:auto;"/></a>';
    }
    return "";
};

const noteType = departmentId => {
    if (departmentId == null)
        return '<span>Personal task</span>';      
    else switch(departmentId)
    {
        case 1:
            return '<span>Task for Restaurant</span>'; 
            break;
        case 2:
            return '<span>Task for Management</span>'; 
            break;
        case 3:
            return '<span>Task for Reception</span>'; 
            break;
        case 4:
            return '<span>Task for Maintenance</span>'; 
            break;
    }
};

const noteStatus = (status, id) => {
    return '<form action="/management/r/notes/updateNoteStatus" method="POST">' +
           updateStatus(status, id) +
           '</form>';
};

const updateStatus = (status, id) => {
    switch (status)
  {
    case 0:
        return '<input type="hidden" name="status" value=1 />' +
                '<label>New task!</label>' +
                '<input onclick="setTimeout(function(){window.location.reload();},10)" class="accept-task" type="submit" value="Accept task">' +
                '<input type="hidden" name="id" value=' + id + ' />';
        break;
    case 1:
        return '<input type="hidden" name="status" value=2 />' +
                '<label>Task in progress</label>' +
                '<input onclick="setTimeout(function(){window.location.reload();},10)" class="progress-task" type="submit" value="Complete task">' +
                '<input type="hidden" name="id" value=' + id + ' />';
        break;
    case 2:
        return'<input class="complete-task" type="submit" name="status" value="Done" disabled>';
        break;
  }  
};