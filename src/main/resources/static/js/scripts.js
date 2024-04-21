let myModal = null;

function openPopup(url) {
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                document.getElementById('popupContainer').innerHTML = xhr.responseText;
                myModal = new bootstrap.Modal(document.querySelector(".modal"))
                myModal.show();
            } else {
                console.error("Ошибка при загрузке содержимого попапа");
            }
        }
    }

    xhr.open('GET', url, true);
    xhr.send();
}

const popupLinks = document.querySelectorAll('.open-popup');
popupLinks.forEach((link) => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        const popupUrl = link.getAttribute('href');
        openPopup(popupUrl);
    })
})

window.addEventListener('click', (e) => {
    if (e.target === null) return;

    if (e.target.getAttribute("type") === "button" &&
        e.target.getAttribute("data-dismiss") === "modal") {
        myModal.hide();
    }
})
