SELECT DISTINCT income_information.person_id           AS person_id,
                income_information.month_of_tax_period AS period
INTO tt_persons_periods
FROM accum_income_tax_information AS income_information
WHERE income_information.month_of_tax_period BETWEEN &start_date AND &end_date;

SELECT DISTINCT persons_periods.person_id AS person_id,
                persons_periods.period    AS period,
                CASE
                    WHEN persons_periods.period < &law_date_285fz AND taxpayer_status.status = 'refugees' THEN 'nonResident'
                    ELSE COALESCE(taxpayer_status.status, 'resident')
END
AS status
INTO tt_persons_taxpayer_statuses
        FROM (
                    SELECT DISTINCT physical_persons_list.person_id             AS person_id,
                                    MAX(taxpayer_status.physical_person_period) AS status_period,
                                    physical_persons_list.period                AS `period`
                    FROM tt_persons_periods AS physical_persons_list
                             LEFT JOIN info_physical_persons_taxpayer_status AS taxpayer_status
                                       ON physical_persons_list.person_id = taxpayer_status.person_id
                                           AND physical_persons_list.period >= taxpayer_status.period
                    GROUP BY physical_persons_list.person_id, physical_persons_list.period) AS persons_periods
        LEFT JOIN info_physical_persons_taxpayer_status AS taxpayer_status
            ON persons_periods.person_id = taxpayer_status.person_id
            AND persons_periods.status_period = taxpayer_status.period;

SELECT DISTINCT CASE
                    WHEN withheld_income.personal_income_movement_type = 'income' THEN 2
                    WHEN withheld_income.withholding_variant = 'withheld' THEN 3
                    WHEN withheld_income.withholding_variant = 'offsetAdvancePayments' THEN 6
                    WHEN withheld_income.withholding_variant = 'returnedByTaxAgent' THEN 7
                    WHEN withheld_income.withholding_variant = 'transferredForCollectionToTaxAuthority' THEN 8
                    ELSE 3
                    END                                                                    AS record_type,
                DATE_TRUNC('MONTH', withheld_income.period)                                AS `month`,
                DATE_TRUNC('MONTH', withheld_income.tax_period_month)                      AS tax_period_month,
                withheld_income.main_organization_id                                       AS main_organization_id,
                withheld_income.organization_id                                            AS organization_id,
                withheld_income.individual_id                                              as individual_id,
                withheld_income.registration_in_tax_authority                              AS registration_in_tax_authority,
                CASE
                    WHEN withheld_income.income_category = 'dividendsAtRate05' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate05'
                    WHEN withheld_income.income_category = 'dividendsAtRate10' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate10'
                    WHEN withheld_income.income_category = 'dividendsAtRate12' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate12'
                    WHEN withheld_income.income_category = 'dividends' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate15'
                    WHEN withheld_income.income_category = 'royaltiesAtRate03' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate03'
                    WHEN withheld_income.income_category = 'royaltiesAtRate05' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate05'
                    WHEN withheld_income.income_category = 'royaltiesAtRate06' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate06'
                    WHEN withheld_income.income_category = 'royaltiesAtRate07' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate07'
                    WHEN withheld_income.income_category = 'royaltiesAtRate10' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate10'
                    WHEN withheld_income.income_category = 'royaltiesAtRate15' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate15'
                    WHEN withheld_income.income_category = 'rate13' THEN CASE
                                                                             WHEN COALESCE(taxpayer_status.status, 'resident') = 'nonResident'
                                                                                 THEN 'rate30'
                                                                             WHEN COALESCE(taxpayer_status.status, 'resident') = 'resident'
                                                                                 THEN 'rate13'
                                                                             WHEN withheld_income.tax_period_month >= DATETIME(2018, 1, 1)
                                                                                 THEN CASE
                                                                                          WHEN withheld_income.income_category IN
                                                                                               ('otherIncomeInCashFromEmploymentActivity',
                                                                                                'incomeInKindFromEmploymentActivity',
                                                                                                'remuneration',
                                                                                                'incomeFromPreviousEditions')
                                                                                              THEN 'rate13'
                                                                                          ELSE 'rate30'
                                                                                 END
                                                                             ELSE 'rate13'
                        END
                    WHEN withheld_income.income_category = 'dividends' OR withheld_income.income_code = 'code1010'
                        THEN CASE
                                 WHEN COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate15'
                                 WHEN DATE_TRUNC('MONTH', withheld_income.tax_period_month) >= DATETIME(2015, 1, 1)
                                     THEN 'rate13'
                                 ELSE 'rate09' END
                    WHEN COALESCE(taxpayer_status.status, 'resident') = 'resident' THEN CASE
                                                                                            WHEN withheld_income.resident_taxation_rate = 'rate09'
                                                                                                THEN 'rate09'
                                                                                            WHEN withheld_income.resident_taxation_rate = 'rate35'
                                                                                                THEN 'rate35'
                                                                                            ELSE '' END
                    ELSE 'rate30' END                                                      as rate,
                SUM(CASE
                        WHEN withheld_income.movement_type = 'expenditure' AND
                             withheld_income.withholding_variant = 'returnedByTaxAgent'
                            THEN -(withheld_income.amount + withheld_income.amount_exceeded)
                        ELSE withheld_income.amount + withheld_income.amount_exceeded END) AS tax_amount
FROM accum_taxpayer_budget_calculations_personal_income_tax as withheld_income
         LEFT JOIN info_individual_taxpayer_status AS taxpayer_status
                   ON withheld_income.individual_id = taxpayer_status.individual_id
                       AND DATE_TRUNC('MONTH', withheld_income.tax_period_month) = taxpayer_status.period
WHERE withheld_income.tax_period_month BETWEEN &start_date
  and &end_date
GROUP BY
    DATE_TRUNC('MONTH', withheld_income.period),
    withheld_income.tax_period_month,
    withheld_income.main_organization_id,
    withheld_income.organization_id,
    withheld_income.individual_id,
    withheld_income.registration_in_tax_authority,
    taxpayer_status.status,
    withheld_income.income_code,
    withheld_income.income_category,
    withheld_income.resident_taxation_rate,
    CASE
    WHEN withheld_income.personal_income_movement_type = 'income' THEN 2
    WHEN withheld_income.withholding_variant = 'withheld' THEN 3
    WHEN withheld_income.withholding_variant = 'offsetAdvancePayments' THEN 6
    WHEN withheld_income.withholding_variant = 'returnedByTaxAgent' THEN 7
    WHEN withheld_income.withholding_variant = 'transferredForCollectionToTaxAuthority' THEN 8
    ELSE 3
END;

SELECT DISTINCT 2                                                     AS record_type,
                DATE_TRUNC('MONTH', income_data.period)               as `period`,
                DATE_TRUNC('MONTH', income_data.tax_period_month)     as tax_period_month,
                income_data.main_organization_id                      as main_organization_id,
                income_data.organization_id                           as organization_id,
                income_data.individual_id                             as individual_id,
                income_data.registration_in_tax_authority             as registration_in_tax_authority,
                CASE
                    WHEN withheld_income.personal_income_movement_type = 'income' THEN 2
                    WHEN withheld_income.withholding_variant = 'withheld' THEN 3
                    WHEN withheld_income.withholding_variant = 'offsetAdvancePayments' THEN 6
                    WHEN withheld_income.withholding_variant = 'returnedByTaxAgent' THEN 7
                    WHEN withheld_income.withholding_variant = 'transferredForCollectionToTaxAuthority' THEN 8
                    ELSE 3
                    END                                               AS record_type,
                DATE_TRUNC('MONTH', withheld_income.period)           AS `month`,
                DATE_TRUNC('MONTH', withheld_income.tax_period_month) AS tax_period_month,
                withheld_income.main_organization_id                  AS main_organization_id,
                withheld_income.organization_id                       AS organization_id,
                withheld_income.individual_id                         as individual_id,
                withheld_income.registration_in_tax_authority         AS registration_in_tax_authority,
                CASE
                    WHEN withheld_income.income_category = 'dividendsAtRate05' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate05'
                    WHEN withheld_income.income_category = 'dividendsAtRate10' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate10'
                    WHEN withheld_income.income_category = 'dividendsAtRate12' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate12'
                    WHEN withheld_income.income_category = 'dividends' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate15'
                    WHEN withheld_income.income_category = 'royaltiesAtRate03' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate03'
                    WHEN withheld_income.income_category = 'royaltiesAtRate05' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate05'
                    WHEN withheld_income.income_category = 'royaltiesAtRate06' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate06'
                    WHEN withheld_income.income_category = 'royaltiesAtRate07' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate07'
                    WHEN withheld_income.income_category = 'royaltiesAtRate10' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate10'
                    WHEN withheld_income.income_category = 'royaltiesAtRate15' AND
                         COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate15'
                    WHEN withheld_income.income_category = 'rate13' THEN CASE
                                                                             WHEN COALESCE(taxpayer_status.status, 'resident') = 'nonResident'
                                                                                 THEN 'rate30'
                                                                             WHEN COALESCE(taxpayer_status.status, 'resident') = 'resident'
                                                                                 THEN 'rate13'
                                                                             WHEN withheld_income.tax_period_month >= DATETIME(2018, 1, 1)
                                                                                 THEN CASE
                                                                                          WHEN withheld_income.income_category IN
                                                                                               ('otherIncomeInCashFromEmploymentActivity',
                                                                                                'incomeInKindFromEmploymentActivity',
                                                                                                'remuneration',
                                                                                                'incomeFromPreviousEditions')
                                                                                              THEN 'rate13'
                                                                                          ELSE 'rate30'
                                                                                 END
                                                                             ELSE 'rate13'
                        END
                    WHEN withheld_income.income_category = 'dividends' OR withheld_income.income_code = 'code1010'
                        THEN CASE
                                 WHEN COALESCE(taxpayer_status.status, 'resident') <> 'resident' THEN 'rate15'
                                 WHEN DATE_TRUNC('MONTH', withheld_income.tax_period_month) >= DATETIME(2015, 1, 1)
                                     THEN 'rate13'
                                 ELSE 'rate09' END
                    WHEN COALESCE(taxpayer_status.status, 'resident') = 'resident' THEN CASE
                                                                                            WHEN withheld_income.resident_taxation_rate = 'rate09'
                                                                                                THEN 'rate09'
                                                                                            WHEN withheld_income.resident_taxation_rate = 'rate35'
                                                                                                THEN 'rate35'
                                                                                            ELSE '' END
                    ELSE 'rate30' END                                 as rate,
    0 AS tax_amount,
    SUM()
