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
let mailServerAuthSettings = null;
let authSettings = null;

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
    showErrors([]);
    goToNextPage();
}

const back = () => {
    showErrors([]);
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
    let isError = false;

    switch (wizard.getCurrentPage().dataset.wizardId) {
        case "accountSetup":
        case "errorDetails":
            isError = !validateFieldsOnAccountSetup();
            if (isError) break;

            if (!settingsFilled) {
                fillEmailAccountSettings();
            }

            if (authMethod === "oauth") {
                checkFailedWithError = false;
                nextPage = "appAuthSetup";

                getMailServerAuthSettings().then((settings) => {
                    mailServerAuthSettings = settings;
                    authSettings = mailServerAuthSettings;

                    if (authSettings !== null) {
                        if (authSettings.authorizationUri !== null) {
                            setRegistrationAppDescription();
                        }

                        modal.querySelector("#authClientId").value = authSettings.appIdentifier;
                        modal.querySelector("#authClientSecret").value = authSettings.clientSecret;
                    }

                    if (!modal.querySelector("#authRedirectUri").value ||
                        authSettings && !authSettings.authorizationUri ||
                        !codeAuthorizationAvailable()) {
                        showErrors([{message: "Не найдены настройки авторизации почтового сервиса. Используйте авторизацию по паролю."}])
                        checkFailedWithError = true;
                        authMethod = "password";
                        modal.querySelector("#passwordRadio").checked = true;
                        modal.querySelector("#password").disabled = false;
                        nextPage = "accountSetup";
                    } else if (modal.querySelector("#authClientId").value) {
                        nextPage = "authorization";
                        setTimeout(() => authorizeToMailServer(), 100);
                    }

                    wizard.switchPageById(nextPage);
                });
            } else if (checkFailedWithError) {
                nextPage = "accountSetupCheck";
                wizard.switchPageById(nextPage);
            } else {
                if (setupMethod === "auto") {
                    nextPage = "accountSetupCheck";
                } else {
                    if (modal.querySelector("#useForSend").checked || modal.querySelector("#useForReceive").checked) {
                        nextPage = "mailServerSetup";
                    } else {
                        nextPage = "accountSetupCheck";
                    }
                }
                wizard.switchPageById(nextPage);
            }
            break;
        case "appAuthSetup":
            isError = validateFieldsOnAppAuthSetup();
            if (!isError) {
                if (checkFailedWithError) {
                    nextPage = "accountSetup";
                } else {
                    nextPage = "authorization";
                    setTimeout(() => authorizeToMailServer(), 100);
                }
            }

            wizard.switchPageById(nextPage);
            break;
        case "authorization":
            if (!authSettings.registerDeviceUri) {

            }
    }
}

const setRegistrationAppDescription = () => {
    authSettings = mailServerAuthSettings;
    const authRedirectUri = modal.querySelector("#authRedirectUri");

    if (authSettings === null) return;
    authRedirectUri.value = authSettings.redirectUriWebClient; // По умолчанию это веб-клиент

    if (authRedirectUri.value === null) {
        authRedirectUri.value = authSettings.defaultRedirectUri;
    }

    if (authSettings.redirectUriDescription === null &&
        authSettings.clientIdDescription === null &&
        authSettings.clientSecretDescription === null) {
        return;
    }

    modal.querySelector("#redirectUriDescription").innerHTML = authSettings.redirectUriDescription;
    modal.querySelector("#clientIdDescription").innerHTML = authSettings.clientIdDescription;
    modal.querySelector("#clientSecretDescription").innerHTML = authSettings.clientSecretDescription;
    modal.querySelector("#additionalDescription").innerHTML = authSettings.additionalDescription;

    modal.querySelector("label[for='authClientSecret']").parentElement
        .style.display = authSettings.useClientSecret ? 'flex' : 'none';

    modal.querySelector("label[for='authRedirectUri']").textContent = authSettings.redirectUriCaption;
    modal.querySelector("label[for='authClientId']").textContent = authSettings.clientIdCaption;
    modal.querySelector("label[for='authClientSecret']").textContent = authSettings.clientSecretCaption;

    modal.querySelector("label[for='authRedirectUri']").parentElement
        .style.display = authSettings.clientSecretCaption !== null ? 'flex' : 'none';
}

const validateFieldsOnAccountSetup = () => {
    const address = modal.querySelector("#address");
    if (address.value === "") {
        showErrors([
            {
                "fieldName": "address",
                "message": "Введите адрес электронной почты",
            }
        ]);
        return false;
    }
    if (!isValidEmail(address.value, true)) {
        showErrors([
            {
                "fieldName": "address",
                "message": "Адрес электронной почты введен неверно",
            }
        ]);
        return false;
    }

    return true;
}

const validateFieldsOnAppAuthSetup = () => {
    return checkInput(modal.querySelector("#authRedirectUri")) || checkInput(modal.querySelector("#authClientId"));
}

const checkInput = (input) => {
    if (input.value === "") {
        const label = modal.querySelector("label[for='" + input.id + "'");
        showErrors([
            {
                "fieldName": input.id,
                "message": `Введите ${label.textContent}`,
            }
        ]);
        return false;
    }
    return true;
}

const fillEmailAccountSettings = () => {
    const data = EmailAccountModule.getDefaultSettings(modal.querySelector("#address").value, modal.querySelector("#password").value);
    fillForm(data);
    const accountName = modal.querySelector("#accountName");
    if (accountName.value === "") {
        accountName.value = modal.querySelector("#address").value;
    }

    settingsFilled = true;

    if (data.useSslForIncoming) modal.querySelector("#encryptionIncomingSsl").checked = true;
    else modal.querySelector("#encryptionIncomingStartTls").checked = true;

    if (data.useSslForSend) modal.querySelector("#encryptionOutgoingSsl").checked = true;
    else modal.querySelector("#encryptionOutgoingStartTls").checked = true;
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

const codeAuthorizationAvailable = () => {
    authSettings = mailServerAuthSettings;
    return authSettings && (authSettings.registerDeviceUri || authSettings.redirectUriWebClient)
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

    modal.querySelector("#authServiceRadio").addEventListener("change", () => {authMethod = "oauth"})
    modal.querySelector("#passwordRadio").addEventListener("change", () => {authMethod = "password"})
}

//
// SERVER
//

const getMailServerSettings = async () => {
    const response = await fetch("/entity/email-account/helper/mail-server-settings?" + new URLSearchParams({
        emailAddress: modal.querySelector("#address").value,
        passwordOutgoing: modal.querySelector("#passwordOutgoing").value,
    }), {
        headers: {
            'Accept': 'application/json',
    }});
    return response.json();
}

const getMailServerAuthSettings = async () => {
    const data = await getMailServerSettings();
    return data.authorizationSettings;
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

// ОбщегоНазначения
const showErrors = (errors) => {
    if (!errors) return;

    const modalErrors = document.getElementById("modalErrors");
    modalErrors.innerHTML = "";
    if (errors.length === 0) return;

    errors.forEach((error) => {
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
}

// ОбщегоНазначения
const isValidEmail = (email, allowLocalAddresses = false) => {
    const letters = "abcdefghijklmnopqrstuvwxyzабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    const digits = "0123456789";
    const specialChars = ".@_:-+";

    // Check for exactly one '@' symbol
    if (email.split('@').length !== 2) return false;
    // Check for no more than one ':' symbol
    if (email.split(':').length > 2) return false;
    // Check for no consecutive dots
    if (email.indexOf('..') > -1) return false;

    // Convert email to lowercase
    email = email.toLowerCase();

    // Check for only allowed characters
    if (!containsOnlyAllowedChars(email, letters + digits + specialChars)) return false;

    // Split email into local part and domain
    const atIndex = email.indexOf('@');
    const localPart = email.substring(0, atIndex);
    const domain = email.substring(atIndex + 1);

    // Check for non-empty local part and domain, and length limits
    if (localPart === '' || domain === '' || localPart.length > 64 || domain.length > 255) return false;
    // Check for no special characters at start or end of domain
    if (hasSpecialCharsAtStartOrEnd(domain, specialChars)) return false;
    // Check for at least one dot in domain, unless allowing local addresses
    if (!allowLocalAddresses && domain.indexOf('.') === -1) return false;
    // Check for no underscore, colon, or plus in domain
    if (domain.indexOf('_') > -1 || domain.indexOf(':') > -1 || domain.indexOf('+') > -1) return false;

    // Extract top-level domain (TLD) from domain
    let tld = domain
    let dotIndex = tld.indexOf('.');
    while (dotIndex > 0) {
        tld = tld.substring(dotIndex + 1);
        dotIndex = tld.indexOf('.');
    }

    // Check TLD length and characters
    return allowLocalAddresses || (tld.length >= 2 && containsOnlyAllowedChars(tld, letters));
}

// ОбщегоНазначения
const hasSpecialCharsAtStartOrEnd = (str, specialChars) => {
    for (let i = 0; i < specialChars.length; i++) {
        const char = specialChars[i];
        if (str.startsWith(char) || str.endsWith(char)) return true;
    }
    return false;
}

// ОбщегоНазначения
const containsOnlyAllowedChars = (str, allowedChars) => {
    const allowedCharsArray = Array.from(allowedChars);
    for (let i = 0; i < str.length; i++) {
        const char = str[i];
        if (!allowedCharsArray.includes(char)) return false;
    }
    return true;
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
        const pageIndex = Object.keys(this.wizardPages).findIndex((key) => this.wizardPages[key].dataset.wizardId === pageId);
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