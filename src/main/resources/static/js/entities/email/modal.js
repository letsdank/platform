let modal = null;
let wizard = null;

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

    wizard = new Wizard(modal, () => {back();}, () => {next()}, true);
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

    switch (wizard.getCurrentPage().dataset.wizardId) {
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

    wizard.switchPageById(prevPage !== null ? prevPage : "accountSetup");
    setupCurrentPage();
}

const goToNextPage = () => {
    let nextPage = null;

    switch (wizard.getCurrentPage().dataset.wizardId) {
        case "accountSetup":
        case "errorDetails":
            validateFieldsOnAccountSetup();
            if (!settingsFilled) {
                fillEmailAccountSettings();
            }
    }
}

const validateFieldsOnAccountSetup = () => {

}

const fillEmailAccountSettings = () => {
    fillForm(EmailAccountModule.getDefaultSettings(modal.querySelector("#address").value, modal.querySelector("#password").value));
}

const setupCurrentPage = () => {
    const currentPage = wizard.getCurrentPage();
    const backButton = wizard.getBackButton();
    const nextButton = wizard.getNextButton();
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
        wizard.switchPageById("errorDetails");
        setupCurrentPage();
    });

    modal.querySelector("#errorDetailsErrorText").addEventListener("click", () => {
        wizard.switchPageById("checkErrorsFound");
        setupCurrentPage();
    });

    modal.querySelector("#passwordRadio").addEventListener("change", () => {
        modal.querySelector("#password").disabled = false;
    });

    modal.querySelector("#authServiceRadio").addEventListener("change", () => {
        modal.querySelector("#password").disabled = true;
    });
}

// Прочие утилы (залить в общий класс) TODO
const fillForm = (data) => {
    // Получаем все инпуты и селекты на странице
    const inputs = modal.querySelectorAll('input, select');

    // Проходимся по каждому элементу формы
    inputs.forEach((input) => {
        // Получаем ключ элемента (например, name или id)
        const key = input.name || input.id;

        // Если ключ существует в данных, заполняем элемент
        if (data[key]) {
            if (input.type === 'checkbox') {
                input.checked = data[key];
            } else if (input.type === 'radio') {
                if (input.value === data[key]) {
                    input.checked = true;
                }
            } else if (input.tagName === 'SELECT') {
                input.value = data[key];
            } else {
                input.value = data[key];
            }
        }
    });
}

//
// Wizard (TODO: Перенести)
//

class Wizard {
    // TODO: Boolean, по нему будем определять, хочет ли пользователь сам менять текст кнопок
    constructor(modal, prevPageHandler, nextPageHandler, manualEditButtons = false) {
        this.modal = modal;
        this.manuallyEditWizardButtons = manualEditButtons;
        this.prevPageHandler = prevPageHandler;
        this.nextPageHandler = nextPageHandler;
        this.wizardPageIndex = 0;
        this.wizardPages = [];

        this.init();
    }

    init() {
        this.wizardPages = this.modal.querySelectorAll(".wizard-page");
        const wizardBackButton = this.modal.querySelector("#wizardBack");
        const wizardNextButton = this.modal.querySelector("#wizardNext");
        this.modal.querySelector(".wizard-page").classList.add("is-active");
        wizardBackButton.addEventListener("click", () => this.prevPage());
        wizardNextButton.addEventListener("click", () => this.nextPage());
        wizardBackButton.disabled = true;
    }

    switchPage(pageIndex) {
        this.wizardPages.forEach((item) => {
            item.classList.remove("is-active");
        });
        this.wizardPages[pageIndex].classList.add("is-active");
        this.wizardPageIndex = pageIndex;
    }

    switchPageById(pageId) {
        const pageIndex = this.wizardPages.findIndex((item) => item.id === pageId);
        this.switchPage(pageIndex);
    }

    prevPage() {
        this.switchPage(this.wizardPageIndex - 1);
        if (this.prevPageHandler) {
            this.prevPageHandler();
        } else {
            this.updateWizardButtons();
        }
    }

    nextPage() {
        if (this.nextPageHandler) {
            this.nextPageHandler();
            this.switchPage(this.wizardPageIndex + 1);
        } else {
            this.switchPage(this.wizardPageIndex + 1);
            this.updateWizardButtons();
        }
    }

    getCurrentPage() {
        return this.wizardPages[this.wizardPageIndex];
    }

    updateWizardButtons() {
        if (this.manuallyEditWizardButtons) return;

        const wizardBackButton = this.modal.querySelector("#wizardBack");
        const wizardNextButton = this.modal.querySelector("#wizardNext");

        if (this.wizardPageIndex === this.wizardPages.length - 1) {
            wizardNextButton.textContent = "Завершить";
            wizardNextButton.classList.remove("btn-primary");
            wizardNextButton.classList.add("btn-success");
        } else {
            wizardNextButton.textContent = "Далее >";
            wizardNextButton.classList.remove("btn-success");
            wizardNextButton.classList.add("btn-primary");
        }

        wizardBackButton.disabled = this.wizardPageIndex === 0;
    }

    getBackButton() {
        return modal.querySelector("#wizardBack");
    }

    getNextButton() {
        return modal.querySelector("#wizardNext");
    }
}

class EmailAccountModule {
    static getDefaultSettings(emailAddress, password) {
        const position = emailAddress.indexOf("@");
        const serverName = emailAddress.substring(position + 1);

        return {
            usernameIncoming: emailAddress,
            usernameOutgoing: emailAddress,
            passwordIncoming: password,
            passwordOutgoing: password,
            protocol: "IMAP",
            serverIncoming: `imap.${serverName}`,
            portIncoming: 993,
            useSslForIncoming: true,

            serverOutgoing: `smtp.${serverName}`,
            portOutgoing: 465,
            useSslForSend: true,
            needAuthenticationBeforeSend: false,

            timeout: 30,
            saveMessageCopies: true,
            message_ttl: 0,
        };
    }
}