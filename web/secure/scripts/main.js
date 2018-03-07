
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

//Modify DOM element based on fetch notes
const notesFetch = notes => {
  let htmlString = "";
  for (let note of notes) {
    htmlString +=
      '<div id="note-' + note.id + '" class="notes">' +
        '<div class="notes-header">' +
          "<h1>"+ note.title + "</h1>" +
        "</div>" +
        '<div class="notes-content">' +
          "<p>" + note.contents + "</p>" +
      // +		'<img src="'+ note.imgUrl + '" alt="">'
          "<p>" + note.imgUrl + "</p>" +
        "</div>" +
        '<div class="notes-footer">' +
            "<span>" + note.noteDate + "</span>" +
        "</div>" +
      "</div>";
  }
  document.querySelector(".main-board").innerHTML = htmlString;
};