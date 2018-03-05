/* FOR STYLING <input type="file"> */
//modify <input> type file name when user chooses a file
const fileAdd = document.getElementById('file-add')

fileAdd.onchange = () => {
	const fileSelected = document.getElementById('file-selected')
	const splitString = fileAdd.value.split(/(\\|\/)/g).pop().split('.')
	console.log(splitString)
	const name = splitString[0].substr(0, 8)
	const ext = splitString[splitString.length-1]
	splitString[0].length <= 8 ? fileSelected.innerHTML = name + '.' + ext  : fileSelected.innerHTML = name + '....' + ext
	fileSelected.style.color = "black"
}

//Get username through cookie
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

//fetch notes for employee

let url = '/management/r/notes'
let username = getCookie('user')
console.log(username)
fetch(url + '/getNotes', {
	headers: {
		'user': username
	}
})
	.then(res => res.json())
	.then(function(json) {
		console.log(json)
	})
	.catch(error => console.log(error))

// fetch(url + '/test')
// 	.then(res => res.text())
// 	.then(function(txt) {
// 		console.log(txt)
// 	})
// 	.catch(error => console.log(error))