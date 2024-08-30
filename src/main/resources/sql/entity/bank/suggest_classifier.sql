CREATE TEMP TABLE vt_bank_classifier AS
SELECT bc.code         AS bic,
       bc.corr_account AS corr_account,
       CASE
           WHEN coalesce(bc_rkc.name, '') = '' THEN bc.name
           ELSE CONCAT(coalesce(bc_rkc.name, ''), '/', bc.name)
           END         AS name,
       bc.city         AS city,
       bc.address      AS address,
       bc.phones       AS phones,
       bc.is_group     AS is_group,
       bc.inactive     AS inactive,
       bc_parent.code  AS parent_code,
       bc.swift_bic    AS swift_bic
FROM bank_classifier AS bc
         LEFT JOIN bank_classifier AS bc_parent ON bc.parent_id = bc_parent.id
         LEFT JOIN bank_classifier AS bc_rkc ON bc.bic_rkc_id = bc_rkc.id
WHERE bc.id IN (?);

CREATE INDEX idx_vt_bank_classifier ON vt_bank_classifier (bic, corr_account);

CREATE TEMP TABLE vt_banks_without_parents AS
SELECT bank.id                         AS bank,
       vt_bank_classifier.bic          AS code,
       vt_bank_classifier.corr_account AS corr_account,
       vt_bank_classifier.is_group     AS is_region,
       vt_bank_classifier.name         AS name,
       vt_bank_classifier.city         AS city,
       vt_bank_classifier.address      AS address,
       vt_bank_classifier.phones       AS phones,
       vt_bank_classifier.swift_bic    AS swift_bic,
       CASE
           WHEN vt_bank_classifier.inactive = true THEN 3
           ELSE 0 END                  AS manual_modification,
       vt_bank_classifier.parent_code  AS parent_code
FROM vt_bank_classifier
         LEFT JOIN bank ON vt_bank_classifier.corr_account = bank.corr_account
    AND vt_bank_classifier.bic = bank.code
    AND vt_bank_classifier.is_group = bank.is_group
WHERE NOT vt_bank_classifier.is_group
UNION ALL
SELECT bank.id,
       vt_bank_classifier.bic,
       NULL,
       vt_bank_classifier.is_group,
       vt_bank_classifier.name,
       NULL,
       NULL,
       NULL,
       NULL,
       0,
       vt_bank_classifier.parent_code
FROM vt_bank_classifier
         LEFT JOIN bank ON vt_bank_classifier.bic = bank.code
    AND vt_bank_classifier.is_group = bank.is_group
WHERE vt_bank_classifier.is_group;

CREATE INDEX idx_vt_banks_without_parents ON vt_banks_without_parents (parent_code);

SELECT vt_banks_without_parents.bank                AS bank,
       vt_banks_without_parents.code                AS code,
       vt_banks_without_parents.corr_account        AS corr_account,
       vt_banks_without_parents.is_region           AS is_region,
       vt_banks_without_parents.name                AS name,
       vt_banks_without_parents.city                AS city,
       vt_banks_without_parents.address             AS address,
       vt_banks_without_parents.phones              AS phones,
       vt_banks_without_parents.manual_modification AS manual_modification,
       vt_banks_without_parents.parent_code         AS parent_code,
       bank.id                                      AS parent_id,
       vt_banks_without_parents.swift_bic           AS swift_bic
FROM vt_banks_without_parents
         LEFT JOIN bank ON parent_id = bank.parent_id
    AND is_region = bank.is_group
ORDER BY is_region DESC,
         code;

DROP TABLE IF EXISTS vt_banks_without_parents;
DROP TABLE IF EXISTS vt_bank_classifier;
