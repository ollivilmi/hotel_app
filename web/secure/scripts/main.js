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