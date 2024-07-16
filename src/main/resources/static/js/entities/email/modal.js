let modal = null;
// TODO: Тоже Wizard часть
let wizardPages = [];
let wizardPageIndex = 0;

// GET parameters
let reconfig = false;
let contextMode = false;
let onlyAuth = false;

let authMethod = ""; // "password" or "oauth"
let setupMethod = ""; // "auto", also can be "manually", but with safe mode
let checkFailedWithError = false;
let checkSkipped = false;
let settingsFilled = false;
let needConfirmationToClose = false;

document.addEventListener('shown.bs.modal', (e) => {
    modal = e.target;
    modal.querySelector("#gotoSettings").style.display = "none";
    const modalTitle = modal.querySelector('.modal-title');
    modalTitle.textContent = "Настройка почты";

    initializeCommonControls(modal);
    wizardPages = modal.querySelectorAll(".wizard-page");
    wizardPageIndex = 0;

    // TODO: Перенести в Wizard часть
    const wizardBackButton = modal.querySelector("#wizardBack");
    const wizardNextButton = modal.querySelector("#wizardNext");
    modal.querySelector(".wizard-page").classList.add("is-active");
    wizardBackButton.addEventListener("click", () => prevPage());
    wizardNextButton.addEventListener("click", () => nextPage());
    wizardBackButton.disabled = true;

    // TODO: Снести
    prevPageHandler = () => {back();};
    nextPageHandler = () => {next();}

    setupMethod = "auto"; // also can be "manually", but with safe mode TODO
    authMethod = "password";

    modal.querySelector("#accountSetupAccess").classList.add("d-none");
    setTimeout(() => setupCurrentPageOnLoad(), 100);
    registerEvents();
});

const next = () => {
    goToNextPage();
}

const back = () => {
    let prevPage = null;

    switch (getCurrentPage().dataset.wizardId) {
        case "mailServerSetup":
        case "accountSetupCheck":
        case "errorDetails":
            prevPage = "accountSetup";
            break;
        case "checkErrorsFound":
            prevPage = "appAuthSetup";
            checkSkipped = false;
            break;
        default: break;
    }

    switchPageById(prevPage !== null ? prevPage : "accountSetup");
    setupCurrentPage();
}

const goToNextPage = () => {
    let nextPage = null;

    switch (getCurrentPage().dataset.wizardId) {
        case "accountSetup":
        case "errorDetails":
            validateFieldsOnAccountSetup();
            if (!settingsFilled) {

            }
    }
}

const validateFieldsOnAccountSetup = () => {

}

const setupCurrentPage = () => {
    const currentPage = getCurrentPage();
    const backButton = getBackButton();
    const nextButton = getNextButton();
    const cancelButton = modal.querySelector("#cancelButton");
    const gotoSettings = modal.querySelector("#gotoSettings");

    switch (currentPage.dataset.wizardId) {
        case "accountSetup":
            if (checkFailedWithError) {
                nextButton.textContent = "Повторить";
            } else if (setupMethod === "auto") {
                nextButton.textContent = (contextMode || reconfig) ? "Настроить" : "Создать";
            }
            nextButton.disabled = false;
            nextButton.style.display = "block";
            backButton.style.display = "none";
            cancelButton.style.display = "block";
            gotoSettings.textContent = "Настроить вручную";

            if (!checkFailedWithError) {
                modal.querySelector("#accountSetupError").classList.add("d-none");
            }

            gotoSettings.style.display = checkFailedWithError ? "block" : "none";

            modal.querySelector("#password").disabled = authMethod !== "password";

            break;
        case "mailServerSetup":
            nextButton.disabled = false;
            nextButton.style.display = "block";
            backButton.style.display = onlyAuth ? "none" : "block";
            cancelButton.style.display = "block";
            gotoSettings.style.display = "none";
            gotoSettings.textContent = "Настроить вручную";

            updateSettingsDaysToDelete();
            setElementsVisibility();

            break;
        case "appAuthSetup":
            nextButton.disabled = false;
            nextButton.style.display = "block";
            backButton.style.display = onlyAuth ? "none" : "block";
            cancelButton.style.display = "block";
            gotoSettings.style.display = "none";
            gotoSettings.textContent = "Настроить вручную";

            break;
        case "authorization":
            nextButton.disabled = false;
            nextButton.style.display = "block";
            backButton.style.display = onlyAuth ? "none" : "block";
            cancelButton.style.display = "block";
            gotoSettings.style.display = "none";
            gotoSettings.textContent = "Настроить вручную";

            break;
        case "accountSetupCheck":
            nextButton.disabled = checkSkipped;
            nextButton.style.display = setupMethod === "auto" ? "none" : "block";
            backButton.style.display = "none";
            cancelButton.style.display = "block";
            gotoSettings.textContent = "Настроить вручную";

            gotoSettings.style.display = (!contextMode && reconfig) ? "block" : "none";

            break;
        case "errorDetails":
            nextButton.disabled = false;
            nextButton.textContent = "Повторить";
            nextButton.style.display = "block";
            backButton.style.display = onlyAuth ? "none" : "block";
            cancelButton.style.display = "block";
            gotoSettings.style.display = "none";
            gotoSettings.textContent = "Настроить вручную";

            break;

        case "accountSetupSuccess":
            nextButton.disabled = false;
            nextButton.textContent = "Закрыть";
            nextButton.style.display = "block";
            backButton.style.display = "none";
            cancelButton.style.display = "none";
            gotoSettings.style.display = "none";
            gotoSettings.textContent = contextMode ? "Настроить вручную" : "Перейти к настройкам";

            const email = modal.querySelector("#address").value;
            modal.querySelector("#accountSetupSuccessMessage").innerHTML =
                `Настройка почты ${email}<br>успешно завершена.`;

            break;
        case "checkErrorsFound":
            nextButton.disabled = false;
            nextButton.style.display = "none";
            backButton.style.display = onlyAuth ? "none" : "block";
            cancelButton.style.display = "block";
            gotoSettings.style.display = "none";
            gotoSettings.textContent = "Настроить вручную";

            break;
    }
}

const updateSettingsDaysToDelete = () => {
    modal.querySelector("#deleteMessageFromServer").disabled = !modal.querySelector("#saveMessageCopies").checked;
    modal.querySelector("#messageTtl").disabled = !modal.querySelector("#deleteMessageFromServer").checked;
}

const setElementsVisibility = () => {
    modal.querySelector("#saveMessageCopiesContainer").style.display =
        modal.querySelector("#protocol").value === "POP" ? "block" : "none";
}

const setupCurrentPageOnLoad = () => {
    // TODO: get parameter onlyAuth
    // if (onlyAuth) { ... }

    setupCurrentPage();
}

// TODO: В конфигурации такой функционал есть?
// Если нет - переименовать либо перенести в какой-то скрипт управления стандартными инпутами
// Иначе убрать этот кусок
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


const registerEvents = () => {
    modal.querySelector("#password").addEventListener("keyup", (e) => {
        // TODO: Может это в контроллер вставим?
        modal.querySelector("#passwordOutgoing").value = e.target.value;
        modal.querySelector("#passwordIncoming").value = e.target.value;
    });

    modal.querySelector("#saveMessageCopies").addEventListener("change", () => {
        updateSettingsDaysToDelete();
    });

    modal.querySelector("#deleteMessageFromServer").addEventListener("change", () => {
        updateSettingsDaysToDelete();
    });

    modal.querySelector("#address").addEventListener("keyup", () => {
        settingsFilled = false;
        needConfirmationToClose = true;
    });

    modal.querySelector("#senderName").addEventListener("keyup", () => {
        needConfirmationToClose = true;
    });

    modal.querySelector("#protocol").addEventListener("change", (e) => {
        setElementsVisibility();
        modal.querySelector("#serverIncomingName").textContent = `Сервер ${e.target.value}:`;
    });

    const useSslForIncoming = modal.querySelector("#useSslForReceive");
    const useSslForOutgoing = modal.querySelector("#useSslForSend");
    modal.querySelector("#encryptionIncomingStartTls").addEventListener("change", () => {useSslForIncoming.value = "false"})
    modal.querySelector("#encryptionIncomingSsl").addEventListener("change", () => {useSslForIncoming.value = "true"})
    modal.querySelector("#encryptionOutgoingStartTls").addEventListener("change", () => {useSslForOutgoing.value = "false"})
    modal.querySelector("#encryptionOutgoingSsl").addEventListener("change", () => {useSslForOutgoing.value = "true"})

    modal.querySelector("#accountSetupErrorText").addEventListener("click", () => {
        switchPageById("errorDetails");
        setupCurrentPage();
    });

    modal.querySelector("#errorDetailsErrorText").addEventListener("click", () => {
        switchPageById("checkErrorsFound");
        setupCurrentPage();
    });

    modal.querySelector("#passwordRadio").addEventListener("change", () => {
        modal.querySelector("#password").disabled = false;
    });

    modal.querySelector("#authServiceRadio").addEventListener("change", () => {
        modal.querySelector("#password").disabled = true;
    });
}

//
// Wizard (TODO: Перенести)
//

// TODO: Boolean, по нему будем определять, хочет ли пользователь сам менять текст кнопок
let manuallyEditWizardButtons = true;
let prevPageHandler = null;
let nextPageHandler = null;

const switchPage = (pageIndex) => {
    wizardPages.forEach((item) => {
        item.classList.remove("is-active");
    });
    wizardPages[pageIndex].classList.add("is-active");
    wizardPageIndex = pageIndex;
}

const switchPageById = (pageId) => {
    const pageIndex = wizardPages.findIndex((item) => item.id === pageId);
    switchPage(pageIndex);
}

const nextPage = () => {
    switchPage(wizardPageIndex + 1);
    if (nextPageHandler) {
        nextPageHandler();
    } else {
        updateWizardButtons();
    }
}

const getCurrentPage = () => {
    return wizardPages[wizardPageIndex];
}

const updateWizardButtons = () => {
    if (manuallyEditWizardButtons) return;

    const wizardBackButton = document.getElementById("wizardBack");
    const wizardNextButton = document.getElementById("wizardNext");

    if (wizardPageIndex === wizardPages.length - 1) {
        wizardNextButton.textContent = "Завершить";
        wizardNextButton.classList.remove("btn-primary");
        wizardNextButton.classList.add("btn-success");
    } else {
        wizardNextButton.textContent = "Далее >";
        wizardNextButton.classList.remove("btn-success");
        wizardNextButton.classList.add("btn-primary");
    }

    wizardBackButton.disabled = wizardPageIndex === 0;
}

const prevPage = () => {
    switchPage(wizardPageIndex - 1);
    if (prevPageHandler) {
        prevPageHandler();
    } else {
        updateWizardButtons();
    }
}

const getBackButton = () => {
    return document.getElementById("wizardBack");
}

const getNextButton = () => {
    return document.getElementById("wizardNext");
}