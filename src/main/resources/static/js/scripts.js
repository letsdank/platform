let myModal = null;

function openPopup(url) {
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                const container = document.getElementById("popupContainer");
                container.innerHTML = xhr.responseText;
                myModal = new bootstrap.Modal(document.querySelector(".modal"))
                myModal.show();

                const popupForms = container.querySelectorAll(".modal-form");
                popupForms.forEach((form) => registerPopupForms(form));
            } else {
                console.error("Ошибка при загрузке содержимого попапа");
            }
        }
    }

    xhr.open('GET', url, true);
    xhr.send();
}

function popupFormHandler(form, e) {
    e.preventDefault();
    const modalErrors = document.getElementById("modalErrors");
    modalErrors.innerHTML = "";

    const formData = new FormData(form);
    fetch(form.getAttribute("action"), {
        body: formData,
        method: 'POST',
        headers: {
            'Accept': 'application/json',
        }
    }).then((result) => {
        if (result.redirected) {
            location.href = result.url;
            return {};
        } else {
            return result.json();
        }
    }).then((data) => {
        if (data.length === 0) return;
        data.errors.forEach((error) => {
            modalErrors.innerHTML += `
                    <div class="alert alert-warning alert-dismissible fade show m-2" role="alert">
                        ${error.message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>`;

            if (error.fieldName !== null) {
                const formInput = document.querySelector(`input[name="${error.fieldName}"]`);
                if (formInput !== null) formInput.classList.toggle("is-invalid");
            }
        });
    });
}

function removeInvalidationInInput(input) {
    input.classList.remove("is-invalid");
}

function registerPopupForms(form) {
    form.addEventListener("submit", (e) => popupFormHandler(form, e))
    form.querySelectorAll("input").forEach((input) => {
        input.addEventListener("input", (e) => removeInvalidationInInput(input))
    })
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
