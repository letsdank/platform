
let modal = null;
let wizardPages = [];
let wizardPageIndex = 0;

document.addEventListener('shown.bs.modal', (e) => {
    modal = e.target;
    const modalTitle = modal.querySelector('.modal-title');
    modalTitle.textContent = "Настройка почты";

    initializeCommonControls(modal);
    wizardPages = modal.querySelectorAll(".wizard-page");
    wizardPageIndex = 0;

    const wizardBackButton = document.getElementById("wizardBack");
    const wizardNextButton = document.getElementById("wizardNext");
    modal.querySelector(".wizard-page").classList.add("is-active");
    wizardBackButton.addEventListener("click", () => prevPage());
    wizardNextButton.addEventListener("click", () => nextPage());
});

const initializeCommonControls = (modal) => {
    const showPassword = modal.querySelectorAll(".show-password");
    showPassword.forEach((item) => {
        item.addEventListener("click", (e) => {
            const input = e.target.parentElement.previousElementSibling;
            if (input.type === "password") {
                input.type = "text";
            } else {
                input.type = "password";
            }
        });
    });
}

// Wizard (TODO: Перенести)

const switchPage = (pageIndex) => {
    wizardPages.forEach((item) => {
        item.classList.remove("is-active");
    });
    wizardPages[pageIndex].classList.add("is-active");
    wizardPageIndex = pageIndex;
}

const nextPage = () => {
    switchPage(wizardPageIndex + 1);
}

const prevPage = () => {
    switchPage(wizardPageIndex - 1);
}
