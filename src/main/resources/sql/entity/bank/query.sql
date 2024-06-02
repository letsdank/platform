CREATE TEMP TABLE vt_changed_banks AS
SELECT bank_classifier.code                                         AS code,
       bank_classifier.corr_account                                 AS corr_account,
       CASE
           WHEN coalesce(bc_rkc.name, '') = '' THEN bank_classifier.name
           ELSE concat(coalesce(bc_rkc.name, ''), '/', bank_classifier.name)
           END                                                      AS name,
       bank_classifier.city                                         AS city,
       bank_classifier.address                                      AS address,
       bank_classifier.phones                                       AS phones,
       bank_classifier.is_group                                     AS is_group,
       bc_parent.code                                               AS parent_code,
       bc_parent.name                                               AS parent_name,
       bank_classifier.inactive                                     AS inactive,
       bank_classifier.swift_bic                                    AS swift_bic,
       bank_classifier.name                                         AS bank_name,
       coalesce(bc_rkc.name, '')                                    AS bic_rkfc_name,
       concat(coalesce(bc_rkc.name, ''), '/', bank_classifier.name) AS bank_ufk_name,
       (SELECT id FROM world_country WHERE code = 'RU' LIMIT 1)     AS country
FROM bank_classifier
         LEFT JOIN bank_classifier as bc_rkc ON bank_classifier.bic_rkc_id = bc_rkc.id
         LEFT JOIN bank_classifier as bc_parent ON bc_parent.id = bank_classifier.parent_id
WHERE NOT bank_classifier.deleted
  AND NOT bank_classifier.inactive
  AND (?);

CREATE INDEX idx_vt_changed_banks ON vt_changed_banks (code, corr_account, is_group);

CREATE TEMP TABLE vt_changed_elements AS
SELECT inset_banks.bank         as bank,
       inset_banks.code         as code,
       inset_banks.corr_account as corr_account,
       inset_banks.name         as name,
       inset_banks.city         as city,
       inset_banks.address      as address,
       inset_banks.phones       as phones,
       inset_banks.is_group     as is_group,
       inset_banks.parent_code  as parent_code,
       inset_banks.parent_name  as parent_name,
       inset_banks.inactive     as inactive,
       inset_banks.swift_bic    as swift_bic,
       inset_banks.country      as country
FROM (SELECT bank.id                       AS bank,
             vt_changed_banks.code         as code,
             vt_changed_banks.corr_account as corr_account,
             vt_changed_banks.name         as name,
             vt_changed_banks.city         as city,
             vt_changed_banks.address      as address,
             vt_changed_banks.phones       as phones,
             vt_changed_banks.is_group     as is_group,
             vt_changed_banks.parent_code  as parent_code,
             vt_changed_banks.parent_name  as parent_name,
             vt_changed_banks.inactive     as inactive,
             vt_changed_banks.swift_bic    as swift_bic,
             vt_changed_banks.country      as country
      FROM bank
               INNER JOIN vt_changed_banks ON bank.code = vt_changed_banks.code
          AND bank.corr_account = vt_changed_banks.corr_account
          AND bank.is_group = vt_changed_banks.is_group
          AND (vt_changed_banks.bic_rkfc_name = '')
          AND bank.name <> vt_changed_banks.bank_name
          AND (bank.manual_modification = 0)
      WHERE NOT bank.is_group
      UNION ALL
      SELECT bank.id,
             vt_changed_banks.code,
             vt_changed_banks.corr_account,
             vt_changed_banks.bank_ufk_name,
             vt_changed_banks.city,
             vt_changed_banks.address,
             vt_changed_banks.phones,
             vt_changed_banks.is_group,
             vt_changed_banks.parent_code,
             vt_changed_banks.parent_name,
             vt_changed_banks.inactive,
             vt_changed_banks.swift_bic,
             vt_changed_banks.country
      FROM bank
               INNER JOIN vt_changed_banks ON bank.code = vt_changed_banks.code
          AND bank.corr_account = vt_changed_banks.corr_account
          AND bank.is_group = vt_changed_banks.is_group
          AND (vt_changed_banks.bic_rkfc_name <> '')
          AND bank.name = vt_changed_banks.bank_ufk_name
          AND (bank.manual_modification = 0)
      UNION ALL
      SELECT bank.id,
             vt_changed_banks.code,
             vt_changed_banks.corr_account,
             vt_changed_banks.name,
             vt_changed_banks.city,
             vt_changed_banks.address,
             vt_changed_banks.phones,
             vt_changed_banks.is_group,
             vt_changed_banks.parent_code,
             vt_changed_banks.parent_name,
             vt_changed_banks.inactive,
             vt_changed_banks.swift_bic,
             vt_changed_banks.country
      FROM bank
               INNER JOIN vt_changed_banks ON bank.code = vt_changed_banks.code
          AND bank.corr_account = vt_changed_banks.corr_account
          AND bank.is_group = vt_changed_banks.is_group
          AND bank.city <> vt_changed_banks.city
          AND (bank.manual_modification = 0)
      WHERE NOT bank.is_group
      UNION ALL
      SELECT bank.id,
             vt_changed_banks.code,
             vt_changed_banks.corr_account,
             vt_changed_banks.name,
             vt_changed_banks.city,
             vt_changed_banks.address,
             vt_changed_banks.phones,
             vt_changed_banks.is_group,
             vt_changed_banks.parent_code,
             vt_changed_banks.parent_name,
             vt_changed_banks.inactive,
             vt_changed_banks.swift_bic,
             vt_changed_banks.country
      FROM bank
               INNER JOIN vt_changed_banks ON bank.code = vt_changed_banks.code
          AND bank.corr_account = vt_changed_banks.corr_account
          AND bank.is_group = vt_changed_banks.is_group
          AND bank.address <> vt_changed_banks.address
          AND (bank.manual_modification = 0)
      WHERE NOT bank.is_group
      UNION ALL
      SELECT bank.id,
             vt_changed_banks.code,
             vt_changed_banks.corr_account,
             vt_changed_banks.name,
             vt_changed_banks.city,
             vt_changed_banks.address,
             vt_changed_banks.phones,
             vt_changed_banks.is_group,
             vt_changed_banks.parent_code,
             vt_changed_banks.parent_name,
             vt_changed_banks.inactive,
             vt_changed_banks.swift_bic,
             vt_changed_banks.country
      FROM bank
               INNER JOIN vt_changed_banks ON bank.code = vt_changed_banks.code
          AND bank.corr_account = vt_changed_banks.corr_account
          AND bank.is_group = vt_changed_banks.is_group
          AND bank.phones <> vt_changed_banks.phones
          AND (bank.manual_modification = 0)
      WHERE NOT bank.is_group
      UNION ALL
      SELECT bank.id,
             vt_changed_banks.code,
             vt_changed_banks.corr_account,
             vt_changed_banks.name,
             vt_changed_banks.city,
             vt_changed_banks.address,
             vt_changed_banks.phones,
             vt_changed_banks.is_group,
             vt_changed_banks.parent_code,
             vt_changed_banks.parent_name,
             vt_changed_banks.inactive,
             vt_changed_banks.swift_bic,
             vt_changed_banks.country
      FROM bank
               LEFT JOIN bank bank_parent ON bank.parent_id = bank_parent.id
               INNER JOIN vt_changed_banks ON bank.code = vt_changed_banks.code
          AND bank.code = vt_changed_banks.code
          AND bank.corr_account = vt_changed_banks.corr_account
          AND bank.is_group = vt_changed_banks.is_group
          AND bank_parent.code <> vt_changed_banks.parent_code
          AND (bank.manual_modification = 0)
      WHERE NOT bank.is_group
      UNION ALL
      SELECT bank.id,
             vt_changed_banks.code,
             vt_changed_banks.corr_account,
             vt_changed_banks.name,
             vt_changed_banks.city,
             vt_changed_banks.address,
             vt_changed_banks.phones,
             vt_changed_banks.is_group,
             vt_changed_banks.parent_code,
             vt_changed_banks.parent_name,
             vt_changed_banks.inactive,
             vt_changed_banks.swift_bic,
             vt_changed_banks.country
      FROM bank
               INNER JOIN vt_changed_banks ON bank.code = vt_changed_banks.code
          AND bank.code = vt_changed_banks.code
          AND bank.corr_account = vt_changed_banks.corr_account
          AND bank.is_group = vt_changed_banks.is_group
          AND bank.swift_bic <> vt_changed_banks.swift_bic
          AND (bank.manual_modification = 0)
      WHERE NOT bank.is_group
      UNION ALL
      SELECT bank.id,
             vt_changed_banks.code,
             vt_changed_banks.corr_account,
             vt_changed_banks.name,
             vt_changed_banks.city,
             vt_changed_banks.address,
             vt_changed_banks.phones,
             vt_changed_banks.is_group,
             vt_changed_banks.parent_code,
             vt_changed_banks.parent_name,
             vt_changed_banks.inactive,
             vt_changed_banks.swift_bic,
             vt_changed_banks.country
      FROM bank
               INNER JOIN vt_changed_banks ON (bank.manual_modification = 0)
          AND bank.code = vt_changed_banks.code
          AND bank.corr_account = vt_changed_banks.corr_account
          AND bank.is_group = vt_changed_banks.is_group
          AND bank.country_id <> vt_changed_banks.country
      WHERE NOT bank.is_group
      UNION ALL
      SELECT bank.id,
             vt_changed_banks.code,
             vt_changed_banks.corr_account,
             vt_changed_banks.name,
             vt_changed_banks.city,
             vt_changed_banks.address,
             vt_changed_banks.phones,
             vt_changed_banks.is_group,
             vt_changed_banks.parent_code,
             vt_changed_banks.parent_name,
             vt_changed_banks.inactive,
             vt_changed_banks.swift_bic,
             vt_changed_banks.country
      FROM bank
               INNER JOIN vt_changed_banks ON bank.code = vt_changed_banks.code
          AND bank.corr_account = vt_changed_banks.corr_account
          AND bank.is_group = vt_changed_banks.is_group
          AND (bank.manual_modification = 2)
      WHERE NOT bank.is_group
      UNION ALL
      SELECT bank.id,
             vt_changed_banks.code,
             vt_changed_banks.corr_account,
             vt_changed_banks.name,
             vt_changed_banks.city,
             vt_changed_banks.address,
             vt_changed_banks.phones,
             vt_changed_banks.is_group,
             vt_changed_banks.parent_code,
             vt_changed_banks.parent_name,
             vt_changed_banks.inactive,
             vt_changed_banks.swift_bic,
             vt_changed_banks.country
      FROM bank
               INNER JOIN vt_changed_banks ON bank.code = vt_changed_banks.code
          AND bank.corr_account = vt_changed_banks.corr_account
          AND bank.is_group = vt_changed_banks.is_group
          AND (bank.manual_modification = 3)
      WHERE NOT bank.is_group) AS inset_banks;

SELECT vt_changed_elements.bank         as bank,
       vt_changed_elements.code         as code,
       vt_changed_elements.corr_account as corr_account,
       vt_changed_elements.name         as name,
       vt_changed_elements.city         as city,
       vt_changed_elements.address      as address,
       vt_changed_elements.phones       as phones,
       vt_changed_elements.is_group     as is_group,
       0                                AS manual_modification,
       bank.id                          AS parent,
       vt_changed_elements.parent_code  as parent_code,
       vt_changed_elements.parent_name  as parent_name,
       vt_changed_elements.inactive     as inactive,
       vt_changed_elements.swift_bic    as swift_bic,
       vt_changed_elements.country      as country
FROM vt_changed_elements
         LEFT JOIN bank ON vt_changed_elements.parent_code = bank.code
    AND (bank.is_group)
UNION ALL
SELECT bank.id,
       vt_changed_elements.code,
       NULL,
       vt_changed_elements.name,
       NULL,
       NULL,
       NULL,
       vt_changed_elements.is_group,
       0,
       NULL,
       NULL,
       NULL,
       vt_changed_elements.inactive,
       NULL,
       NULL
FROM bank
         INNER JOIN vt_changed_elements ON bank.code = vt_changed_elements.code
    AND bank.name <> vt_changed_elements.name
    AND bank.is_group = vt_changed_elements.is_group
    AND (bank.manual_modification = 0)
WHERE vt_changed_elements.is_group
UNION ALL
SELECT bank.id,
       vt_changed_elements.code,
       NULL,
       vt_changed_elements.name,
       NULL,
       NULL,
       NULL,
       vt_changed_elements.is_group,
       0,
       NULL,
       NULL,
       NULL,
       vt_changed_elements.inactive,
       NULL,
       NULL
FROM bank as bank
         INNER JOIN vt_changed_elements ON bank.code = vt_changed_elements.code
    AND bank.is_group = vt_changed_elements.is_group
    AND (bank.manual_modification = 2)
WHERE vt_changed_elements.is_group
ORDER BY is_group DESC;

DROP TABLE vt_changed_elements;
DROP TABLE vt_changed_banks;