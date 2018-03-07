var allContent = document.querySelectorAll(".notes-content");

for (let e of allContent) {
    e.style.display = "none";
}

var allHeader = document.querySelectorAll(".notes-header");

for (let e of allHeader) {
    e.onclick = function() {
        var nextSibling = e.nextSibling;
        while(nextSibling && nextSibling.nodeType != 1) {
            nextSibling = nextSibling.nextSibling
        }
        
        if (nextSibling.style.display == "block") {
            nextSibling.style.display = "none";
        } else {
            nextSibling.style.display = "block";
        }


    };
}