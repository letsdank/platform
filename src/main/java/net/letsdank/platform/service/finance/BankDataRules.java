package net.letsdank.platform.service.finance;

import net.letsdank.platform.utils.string.StringUtils;

// Alias:
// БанковскиеПравила
// БанковскиеПравилаКлиентСервер
// TODO: Пересмотреть функции и переименовать их, если необходимо
public class BankDataRules {
    // "Положение о правилах ведения бухгалтерского учета в кредитных организациях, расположенных на территории Российской Федерации"
    // (утв. Банком России 16.07.2012 N 385-П)
    // "Положение о плане счетов бухгалтерского учета для кредитных организаций и порядке его применения"
    // (утв. Банком России 27.02.2017 N 579-П)

    // ПланСчетовКредитныхОрганизаций
    // - План счетов бухгалтерского учета в кредитных организациях

    // Alias: БалансовыйСчет
    public static String getBalanceAccount(String account) {
        return account.substring(0, 5);
    }

    // Alias: СчетПозволяетИдентифицироватьКонтрагента
    public static boolean isCounterpartyIdentifiable(String accountNumber) {
        // В функцию на вход может прийти как полный номер счета, так и уже выделенный Балансовый счет.
        // В случае полного номера счета отдельно выделяем признак лицевого счета,
        // во втором случае это не требуется.

        String accountIndicator = "";
        String accountNum;

        if (isBankAccountFormat(accountNumber)) {
            accountNum = getBalanceAccount(accountNumber);
            accountIndicator = accountNumber.substring(13, 14);
        } else {
            accountNum = accountNumber;
        }

        // Счета, перечисленные
        // - в разделе 4 "Операции с клиентами" Плана счетов,
        //   за исключением счетов, порядок использования которых не позволяет обеспечить
        //   раздельный учет операций с разными хозяйствующими субъектами на разных лицевых счетах
        // - в разделах 6 и 7 Плана счетов (позволяют идентифицировать банк как участника хозяйственных операций, а не
        // финансового агента)

        if (isIntraBankSettlementAccount(accountNum)) {
            return true;
        }

        String section = accountNumber.substring(0, 1);
        if (!section.equals("4") && !section.equals("6") && !section.equals("7")) {
            return false;
        }

        // На некоторых счетах, предназначенных для учета средств государственного бюджета,
        // в аналитическом учете ведутся лицевые счета органов Федерального казначейства
        switch (accountNumber) {
            case "40101":
            case "40105":
            case "40116":
            case "40201":
            case "40204":
            case "40302":
            case "40312":
                return false;
            // Указание ЦБ РФ от 23 января 2018 г N 4700-У
            case "40501":
                if (accountIndicator.equals("1") || accountIndicator == "2" || accountIndicator == "5") {
                    return false;
                }
            case "40601":
            case "40701":
                if (accountIndicator == "1" || accountIndicator == "2" ||
                        accountIndicator == "3" || accountIndicator == "4" ||
                        accountIndicator == "5") {
                    return false;
                }
            case "40503":
            case "40603":
            case "40703":
                if (accountIndicator == "4") return false;
            default:
                // На счетах прочих операций аналитический учет может вестись по виду операций

                return !isOtherOperationsAccount(accountNumber) &&  // 47422
                        !isBankSettlementAccount(accountNumber);    // 47423
                // На остальных счетах раздела 4 ведется аналитический учет по организациям, физическим лицам или более детальный
        }
    }

    // Alias: ЭтоСчетЮридическогоЛица
    public static boolean isLegalEntityAccount(String balanceAcocunt) {
        // 401 - Средства федерального бюджета
        // 402 - Средства бюджетов субъектов Российской Федерации и местных бюджетов
        // 403 - Прочие средства бюджетов
        // 404 - Средства государственных и других внебюджетных фондов
        // 405 - Счета организаций, находящихся в федеральной собственности
        // 406 - Счета организаций, находящихся в государственной (кроме федеральной) собственности
        // 407 - Счета негосударственных организаций

        String firstOrder = balanceAcocunt.substring(0, 3);
        return  firstOrder.equals("401") ||
                firstOrder.equals("402") ||
                firstOrder.equals("403") ||
                firstOrder.equals("404") ||
                firstOrder.equals("405") ||
                firstOrder.equals("406") ||
                isNonGovernmentalOrganizationAccount(balanceAcocunt);
    }

    // Alias: ЭтоСчетНегосударственныхОрганизаций
    public static boolean isNonGovernmentalOrganizationAccount(String balanceAccount) {
        String firstOrder = balanceAccount.substring(0, 3);
        return  firstOrder.equals("407");
    }

    // Alias: ЭтоПрочийСчет
    public static boolean isOtherAccount(String balanceAccount) {
        String firstOrder = balanceAccount.substring(0, 3);
        return  firstOrder.equals("408");
    }

    // Alias: ЭтоСчетИндивидуальногоПредпринимателя
    public static boolean isIndividualEntrepreneurAccount(String balanceAccount) {
        // Счет - "Физические лица - индивидуальные предприниматели"
        return balanceAccount.equals("40802");
    }

    // Alias: ЭтоСчетПереводовФизическимЛицам
    public static boolean isIndividualTransfersAccount(String balanceAccount) {
        // 40817 - "Физические лица" - "Назначение счета - учет денежных средств физических лиц, не связанных с осуществлением
        //                             ими предпринимательской деятельности"
        // Как правило, лицевые счета открываются физическим лицам.
        // Но могут быть открыты и организациям - для учета средств, направленных на выплаты физическим лицам.
        // 40820 - "Счета физических лиц - нерезидентов"

        return balanceAccount.equals("40817") || balanceAccount.equals("40820");
    }

    // Alias: ЭтоСчетДепозитовФизическихЛиц
    public static boolean isIndividualDepositsAccount(String balanceAccount) {
        // 423 - Депозиты и прочие привлеченные средства физических лиц
        return balanceAccount.startsWith("423");
    }

    // Alias: ЭтоКассаКредитнойОрганизации
    public static boolean isBankCashAccount(String balanceAccount) {
        return balanceAccount.equals("20202"); // "Касса кредитных организаций"
    }

    // Alias: ЭтоСчетКассыКредитныхОрганизаций
    public static boolean isBankCashAccountDetail(String balanceAccount) {
        return isBankCashAccount(balanceAccount) ||
                balanceAccount.equals("20208"); // "Денежные средства в банкоматах и платежных терминалах"
    }

    // Alias: ЭтоСчетБанковскогоПлатежногоАгента
    public static boolean isBankPaymentAgentAccount(String balanceAccount) {
        return balanceAccount.equals("40821"); // Специальный банковский счет платежного агента, банковского платежного агента (субагента), поставщика
    }

    // Alias: ЭтоСчетИнкассированныхНаличныхДенег
    public static boolean isCashCollectionAccount(String balanceAccount) {
        return balanceAccount.equals("40906"); // "Инкассированные наличные деньги"
    }

    // Alias: ЭтоСчетДоходовБанка
    public static boolean isBankIncomeAccount(String balanceAccount) {
        return balanceAccount.equals("70601") || // Доходы (банка)
                balanceAccount.equals("61301") || // Доходы (банка) будущих периодов по кредитным операциям
                balanceAccount.equals("61304"); // Доходы (банка) будущих периодов по другим операциям
    }

    // Alias: ЭтоРасчетыПоПереводамДенежныхСредств
    public static boolean isMoneyTransferAccount(String balanceAccount) {
        return balanceAccount.equals("40911"); // Расчеты по переводам денежных средств
    }

    // Alias: ЭтоОбязательстваПоПрочимОперациям
    public static boolean isOtherOperationsAccount(String balanceAccount) {
        return balanceAccount.equals("47422");// Обязательства по прочим операциям
    }

    // Alias: ЭтоСчетРасчетовСБанком
    public static boolean isBankSettlementAccount(String balanceAccount) {
        return balanceAccount.equals("47423");// Требования по прочим операциям - "отражаются требования кредитной организации"
    }

    // Alias: ЭтоРасчетыПоВыданнымБанковскимГарантиям
    public static boolean isIssuedBankGuaranteesSettlementAccount(String balanceAccount) {
        return balanceAccount.equals("47502"); // Расчеты по выданным банковским гарантиям
    }

    // Alias: ЭтоСчетВнутрибанковскихОпераций
    public static boolean isIntraBankSettlementAccount(String balanceAccount) {
        String section = balanceAccount.substring(0, 1);
        return section.equals("6") || section.equals("7");
    }

    // Alias: ЭтоСчетВнутрибанковскихОперацийНДС
    public static boolean isVATReceivedAccount(String balanceAccount) {
        return balanceAccount.equals("60309"); // Налог на добавленную стоимость, полученный
    }

    // Alias: ЭтоВнутрибанковскиеРасчетыСПрочимиКредиторами
    public static boolean isIntraBankSettlementsWithOtherCreditorsAccount(String balanceAccount) {
        return balanceAccount.equals("60309"); // Расчеты с прочими кредиторами
    }

    // Alias: ЭтоСчетНезавершенныхРасчетов3023Актив
    public static boolean isUnfinishedTransactions3023ActiveAccount(String balanceAccount) {
        return balanceAccount.equals("30233"); // незавершенные расчеты с операторами услуг платежной инфраструктуры
                                               // и операторами по переводу денежных средств
    }

    // Alias: ЭтоСчетНезавершенныхРасчетов3023Пассив
    public static boolean isUnfinishedTransactions3023PassiveAccount(String balanceAccount) {
        return balanceAccount.equals("30232"); // незавершенные расчеты с операторами услуг платежной инфраструктуры
                                               // и операторами по переводу денежных средств
    }

    // Alias: ЭтоСчетНезавершенныхРасчетов3022Пассив
    public static boolean isUnfinishedTransactions3022PassiveAccount(String balanceAccount) {
        return balanceAccount.equals("30222"); // незавершенные переводы и расчеты кредитной организации
    }

    // Alias: ЭтоСчетНезавершенныхРасчетов302
    public static boolean isUnfinishedTransactions302Account(String balanceAccount) {
        return balanceAccount.equals("30221") || // незавершенные переводы и расчеты кредитной организации (Актив)
                isUnfinishedTransactions3022PassiveAccount(balanceAccount) ||
                balanceAccount.equals("30223") || // Незавершенные переводы и расчеты по банковским счетам клиентов
                    // при осуществлении расчетов через подразделения Банка России
                isUnfinishedTransactions3023ActiveAccount(balanceAccount) ||
                isUnfinishedTransactions3023PassiveAccount(balanceAccount) ||
                balanceAccount.equals("30236"); // Незавершенные переводы, поступившие от платежных систем и на корреспондентские счета
    }

    // Alias: ЭтоСчетНезавершенныхРасчетов
    public static boolean isUnfinishedTransactionsAccount(String balanceAccount) {
        return isUnfinishedTransactions302Account(balanceAccount) ||
                isOtherOperationsAccount(balanceAccount) ||
                isBankSettlementAccount(balanceAccount) || // Требования по прочим операциям
                balanceAccount.equals("40907") || // Расчеты клиентов по зачетам
                balanceAccount.equals("40911"); // Расчеты по переводам денежных средств
    }

    // Alias: ЭтоСчетРасчетовПоФакторингу
    public static boolean isFactoringSettlementAccount(String balanceAccount) {
        return balanceAccount.equals("47401") || // Расчеты с клиентами по факторинговым операциям
                balanceAccount.equals("47402"); // Расчеты с клиентами по факторинговым операциям
    }

    // TODO: Доделать область ПланСчетовКредитныхОрганизаций

    public static final int BANK_ACCOUNT_LENGTH = 20;
    // Символы приведены в порядке, определенном письмом ЦБР от 8 сентября 1997 г. N 515
    // См. также ПроверитьКонтрольныйКлючВНомереБанковскогоСчета()
    public static final String BANK_ACCOUNT_VALID_ALPHABETICAL_SYMBOLS = "ABCEHKMPTX";

    public static boolean isBankAccountFormat(String string) {
        if (!checkBankAccountLength(string)) {
            return false;
        }

        // Check the special case: alphabetical character in the account number
        String alphanumeric = string.substring(0, 5) + string.substring(6);
        if (!StringUtils.isDigitsOnly(alphanumeric)) {
            return false;
        }

        char alphabeticalSymbol = string.charAt(5);
        if (StringUtils.isDigitsOnly(String.valueOf(alphabeticalSymbol))) {
            return true;
        }

        int sixthDigitIndex = BANK_ACCOUNT_VALID_ALPHABETICAL_SYMBOLS.indexOf(alphabeticalSymbol);
        return sixthDigitIndex != -1;
    }

    public static boolean checkBankAccountLength(String bankAccount) {
        return bankAccount.length() == BANK_ACCOUNT_LENGTH;
    }

    // ISO 9362
    // SWIFT BIC

    public static final int SWIFT_OFFICE_LENGTH = 8;
    public static final int SWIFT_BRANCH_LENGTH = 11;

    /**
     * Retrieves the country code from the SWIFT code according to
     * ISO 9362.
     *
     * @param swiftBic The string representing the SWIFT BIC code.
     * @return The SWIFT country code.
     */
    // Alias: КодСтраныSWIFT
    public static String getCountryCodeFromSwift(String swiftBic) {
        return swiftBic.substring(4, 6);
    }

    /**
     * Checks if the string conforms to the SWIFT code format.
     *
     * @param value The string to be checked for SWIFT code
     *              format compliance.
     * @return <code>true</code> if the string conforms to the SWIFT
     * code format, otherwise <code>false</code>.
     */
    // Alias: СтрокаСоответствуетФорматуSWIFT
    public static boolean isSwiftFormatValid(String value) {
        return isSwiftLengthValid(value) && isSwiftCharactersValid(value);
    }

    /**
     * Checks the length of the SWIFT code.
     *
     * @param swiftBic The string representing the SWIFT BIC code.
     * @return <code>true</code> if the length of the SWIFT code is valid,
     * otherwise <code>false</code>.
     */
    // Alias: ПроверитьДлинуSWIFT
    public static boolean isSwiftLengthValid(String swiftBic) {
        int codeLength = swiftBic.length();
        return codeLength == SWIFT_OFFICE_LENGTH || codeLength == SWIFT_BRANCH_LENGTH;
    }

    /**
     * Checks if the string contains only valid SWIFT characters.
     *
     * @param swiftBic The string representing the SWIFT BIC code.
     * @return <code>true</code> if all characters in the string are valid
     * SWIFT characters, otherwise <code>false</code>.
     */
    // Alias: ПроверитьРазрешенныеСимволыSWIFT
    public static boolean isSwiftCharactersValid(String swiftBic) {
        return swiftBic.chars()
            .allMatch(symbol -> {
                char upperCaseSymbol = Character.toUpperCase((char) symbol);
                return "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(upperCaseSymbol) != -1;
            });
    }
}
