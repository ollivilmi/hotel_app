/* FOR STYLING <input type="file"> */
//modify <input> type file name when user chooses a file
let fileAdd = document.getElementById('file-add')

fileAdd.onchange = function () {
	let fileSelected = document.getElementById('file-selected')
	fileSelected.innerHTML = fileAdd.value.split(/(\\|\/)/g).pop()
	fileSelected.style.color = "black"
}

