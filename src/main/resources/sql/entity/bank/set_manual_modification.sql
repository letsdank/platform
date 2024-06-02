SELECT bank.id AS bank,
       2       AS manual_modification
FROM bank
         LEFT JOIN bank_classifier as bank_classifier_ru
                   ON bank.code = bank_classifier_ru.code
                       AND (bank.is_group OR bank.corr_account = bank_classifier_ru.corr_account)
WHERE bank_classifier_ru.id IS NULL
  AND bank.manual_modification <> 2
UNION
SELECT bank.id,
       3
FROM bank
         LEFT JOIN bank_classifier AS bank_classifier_ru
                   ON bank.code = bank_classifier_ru.code
                       AND (bank.is_group OR bank.corr_account = bank_classifier_ru.corr_account)
WHERE bank_classifier_ru.id IS NULL
  AND bank.manual_modification < 2;