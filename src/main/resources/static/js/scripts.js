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
                const alertContainer = document.querySelector(".alerts-container");
                if (alertContainer) {
                    const data = JSON.parse(xhr.responseText);
                    data.errors.forEach((error) => {
                        alertContainer.innerHTML += `
                           <div class="alert alert-warning alert-dismissible fade show m-2" role="alert">
                                ${error.message}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Закрыть"></button>
                            </div>`;
                    });
                } else {
                    console.error("Ошибка при загрузке содержимого попапа");
                }
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
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Закрыть"></button>
                    </div>`;

            if (error.fieldName !== null) {
                const formInput = document.querySelector(`input[name="${error.fieldName}"]`);
                if (formInput !== null) formInput.classList.add("is-invalid");
            }
        });
    });
}

function removeInvalidationInInput(e) {
    e.target.classList.remove("is-invalid");
}

function suggestInputHandler(inputText, dropdownMenu, suggestUrl) {
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                const data = JSON.parse(xhr.responseText);
                dropdownMenu.innerHTML = "";
                if (data.total > 0) {
                    data.suggests.forEach((item) => {
                        dropdownMenu.innerHTML += `<a href="#" class="dropdown-item" data-suggest-entity-id="${item.id}">${item.name}</a>`;
                    });
                } else {
                    dropdownMenu.innerHTML = `<a href="#" class="dropdown-item">Ничего не найдено</a>`;
                }
            } else {
                console.error("Ошибка при загрузке подсказок");
            }
        }
    }
    xhr.open('GET', `${suggestUrl}?query=${inputText.value}`, true);
    xhr.send();
}

function suggestInputFocusHandler(e, inputId, dropdownMenu, focused) {
    focused ? dropdownMenu.classList.add("show") : setTimeout(() => {
        if (e.target.value === "") inputId.value = "";
        dropdownMenu.classList.remove("show")
    }, 150);
}

function registerPopupForms(form) {
    form.addEventListener("submit", (e) => popupFormHandler(form, e));
    form.querySelectorAll("input").forEach((input) => {
        input.addEventListener("input", removeInvalidationInInput)
    });
    form.querySelectorAll(".suggester").forEach((suggester) => {
        const inputId = suggester.querySelector("input[type='hidden']");
        const inputText = suggester.querySelector("input[type='text']");
        const suggestUrl = suggester.dataset.suggestUrl;

        // Создаем dropdown-menu
        const dropdownMenu = document.createElement("div");
        dropdownMenu.classList.add("dropdown-menu");
        dropdownMenu.innerHTML = "<span class='dropdown-item'>Введите текст для поиска</span>";
        suggester.appendChild(dropdownMenu);

        inputText.addEventListener("input", () => suggestInputHandler(inputText, dropdownMenu, suggestUrl));
        inputText.addEventListener("focus", (e) => suggestInputFocusHandler(e, inputId, dropdownMenu, true));
        inputText.addEventListener("blur", (e) => suggestInputFocusHandler(e, inputId, dropdownMenu, false));
    });
}

window.addEventListener('click', (e) => {
    if (e.target === null) return;

    if (e.target.classList.contains("open-popup")) {
        e.preventDefault();
        if (myModal !== null) myModal.hide();
        const popupUrl = e.target.getAttribute('href');
        openPopup(popupUrl);
    }

    if (e.target.getAttribute("type") === "button" &&
        e.target.getAttribute("data-dismiss") === "modal") {
        myModal.hide();
    }

    if (e.target.classList.contains("dropdown-item") && e.target.dataset.suggestEntityId !== undefined) {
        const suggester = e.target.closest(".suggester");
        const inputId = suggester.querySelector("input[type='hidden']");
        const inputText = suggester.querySelector("input[type='text']");
        inputId.value = e.target.dataset.suggestEntityId;
        inputText.value = e.target.innerText;
    }
})

// TABLE GROUP
const tableGroupData = {};
let tableGroup = null;

function tableClickHandler(clickedGroup) {
    if (clickedGroup.dataset.groupId === undefined) return;

    let groupItems = tableGroupData[clickedGroup.dataset.groupId];
    let children = clickedGroup.children[0];
    if (children.getAttribute("rowspan") !== "1") {
        clickedGroup.children[0].setAttribute("rowspan", 1);
    } else {
        clickedGroup.children[0].setAttribute("rowspan", groupItems.length + 1);
    }
    groupItems.forEach((item) => {
        let groupItemElem = tableGroup.querySelector(`tr[data-entity-id="${item}"]`);
        groupItemElem.classList.toggle("visually-hidden");
    });
}

function initTableGroupData() {
    tableGroup = document.querySelector(".table-group");
    if (!tableGroup) return;

    const rows = tableGroup.querySelectorAll("tr");
    rows.forEach((row) => {
        if (row.dataset.groupId !== undefined) {
            tableGroupData[row.dataset.groupId] = [];
            row.children[0].setAttribute("rowspan", "1");
            row.addEventListener("click", () => tableClickHandler(row));
        } else if (row.dataset.parentId !== undefined) {
            tableGroupData[row.dataset.parentId].push(row.dataset.entityId);
        }
    });
}

function init() {
    initTableGroupData();
}

init();