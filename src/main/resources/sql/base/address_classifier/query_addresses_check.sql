CREATE TEMP TABLE addresses_fields
    (key,
     is_district,
     name_0,
     object_type_0,
     name_1,
     object_type_1,
     name_2,
     object_type_2,
     name_3,
     object_type_3,
     name_4,
     object_type_4,
     name_5,
     object_type_5)
AS
SELECT addresses_fields.key           AS key,
       addresses_fields.is_district   AS is_district,
       addresses_fields.name_0        AS name_0,
       addresses_fields.object_type_0 AS object_type_0,
       addresses_fields.name_1        AS name_1,
       addresses_fields.object_type_1 AS object_type_1,
       addresses_fields.name_2        AS name_2,
       addresses_fields.object_type_2 AS object_type_2,
       addresses_fields.name_3        AS name_3,
       addresses_fields.object_type_3 AS object_type_3,
       addresses_fields.name_4        AS name_4,
       addresses_fields.object_type_4 AS object_type_4,
       addresses_fields.name_5        AS name_5,
       addresses_fields.object_type_5 AS object_type_5
FROM &addresses_fields AS addresses_fields;

CREATE INDEX addresses_fields_idx ON addresses_fields (key, name_0, object_type_0, name_1, object_type_1, name_2,
                                                       object_type_2, name_3, object_type_3, name_4, object_type_4,
                                                       name_5, object_type_5);

CREATE TEMP TABLE regions (key, is_district, name_0, object_type_0, level, additional_info, id, parent_id)
AS
SELECT addresses_fields.key                    AS key,
       addresses_fields.is_district            AS is_district,
       address_objects.name                    AS name_0,
       address_objects.object_type             AS object_type_0,
       address_objects.level                   AS level,
       address_objects.address_additional_info AS additional_info,
       address_objects.id                      AS id,
       address_objects.id                      AS parent_id
FROM addresses_fields AS addresses_fields
         INNER JOIN reg_address_objects AS address_objects
                    ON (address_objects.name = addresses_fields.name_0)
                        AND (address_objects.object_type = addresses_fields.object_type_0)
                        AND (address_objects.level = 1)
                        AND (address_objects.ru_subject_code IN (&region_codes));

CREATE INDEX regions_idx ON regions (key);

CREATE TEMP TABLE district_address_objects_1 (key, name_1, object_type_1, level, additional_info, id, parent_id)
AS
SELECT addresses_fields.key                    AS key,
       address_objects.name                    AS name_1,
       address_objects.object_type             AS object_type_1,
       address_objects.level                   AS level,
       address_objects.address_additional_info AS additional_info,
       address_objects.id                      AS id,
       district_hierarchy.parent_id            AS parent_id
FROM addresses_fields AS addresses_fields
         INNER JOIN reg_address_objects AS address_objects
                    ON (address_objects.name = addresses_fields.name_1)
                        AND (address_objects.object_type = addresses_fields.object_type_1)
                        AND (address_objects.ru_subject_code IN (&region_codes))
         INNER JOIN regions AS address_objects_ids
                    ON (address_objects_ids.key = addresses_fields.key) AND (address_objects_ids.is_district = TRUE)
         INNER JOIN reg_district_hierarchy AS district_hierarchy
                    ON (district_hierarchy.parent_id = address_objects_ids.id)
                        AND (district_hierarchy.id = address_objects.id)
                        AND (NOT district_hierarchy.id IS NULL);

CREATE TEMP TABLE district_address_objects_2 (key, name_2, object_type_2, level, additional_info, parent_id, id)
AS
SELECT addresses_fields.key                    AS key,
       address_objects.name                    AS name_2,
       address_objects.object_type             AS object_type_2,
       address_objects.level                   AS level,
       address_objects.address_additional_info AS additional_info,
       district_hierarchy.parent_id            AS parent_id,
       address_objects.id                      AS id
FROM addresses_fields AS addresses_fields
         INNER JOIN reg_address_objects AS address_objects
                    ON (address_objects.name = addresses_fields.name_2)
                        AND (address_objects.object_type = addresses_fields.object_type_2)
                        AND (address_objects.ru_subject_code IN (&region_codes))
         INNER JOIN district_address_objects_1 AS address_objects_ids
                    ON (address_objects_ids.key = addresses_fields.key)
         INNER JOIN reg_district_hierarchy AS district_hierarchy
                    ON (district_hierarchy.parent_id = address_objects_ids.id)
                        AND (district_hierarchy.id = address_objects.id)
                        AND (NOT district_hierarchy.id IS NULL);

CREATE TEMP TABLE district_address_objects_3 (key, name_3, object_type_3, level, additional_info, parent_id, id)
AS
SELECT addresses_fields.key                    AS key,
       address_objects.name                    AS name_3,
       address_objects.object_type             AS object_type_3,
       address_objects.level                   AS level,
       address_objects.address_additional_info AS additional_info,
       district_hierarchy.parent_id            AS parent_id,
       address_objects.id                      AS id
FROM addresses_fields AS addresses_fields
         INNER JOIN reg_address_objects AS address_objects
                    ON (address_objects.name = addresses_fields.name_3)
                        AND (address_objects.object_type = addresses_fields.object_type_3)
                        AND (address_objects.ru_subject_code IN (&region_codes))
         INNER JOIN district_address_objects_2 AS address_objects_ids
                    ON (address_objects_ids.key = addresses_fields.key)
         INNER JOIN reg_district_hierarchy AS district_hierarchy
                    ON (district_hierarchy.parent_id = address_objects_ids.id)
                        AND (district_hierarchy.id = address_objects.id)
                        AND (NOT district_hierarchy.id IS NULL);

CREATE TEMP TABLE district_address_objects_4 (key, name_4, object_type_4, level, additional_info, parent_id, id)
AS
SELECT addresses_fields.key                    AS key,
       address_objects.name                    AS name_4,
       address_objects.object_type             AS object_type_4,
       address_objects.level                   AS level,
       address_objects.address_additional_info AS additional_info,
       district_hierarchy.parent_id            AS parent_id,
       address_objects.id                      AS id
FROM addresses_fields AS addresses_fields
         INNER JOIN reg_address_objects AS address_objects
                    ON (address_objects.name = addresses_fields.name_4)
                        AND (address_objects.object_type = addresses_fields.object_type_4)
                        AND (address_objects.ru_subject_code IN (&region_codes))
         INNER JOIN district_address_objects_3 AS address_objects_ids
                    ON (address_objects_ids.key = addresses_fields.key)
         INNER JOIN reg_district_hierarchy AS district_hierarchy
                    ON (district_hierarchy.parent_id = address_objects_ids.id)
                        AND (district_hierarchy.id = address_objects.id)
                        AND (NOT district_hierarchy.id IS NULL);

CREATE TEMP TABLE district_address_objects_5 (key, name_5, object_type_5, level, additional_info, parent_id, id)
AS
SELECT addresses_fields.key                    AS key,
       address_objects.name                    AS name_5,
       address_objects.object_type             AS object_type_5,
       address_objects.level                   AS level,
       address_objects.address_additional_info AS additional_info,
       district_hierarchy.parent_id            AS parent_id,
       address_objects.id                      AS id
FROM addresses_fields AS addresses_fields
         INNER JOIN reg_address_objects AS address_objects
                    ON (address_objects.name = addresses_fields.name_5)
                        AND (address_objects.object_type = addresses_fields.object_type_5)
                        AND (address_objects.ru_subject_code IN (&region_codes))
         INNER JOIN district_address_objects_4 AS address_objects_ids
                    ON (address_objects_ids.key = addresses_fields.key)
         INNER JOIN reg_district_hierarchy AS district_hierarchy
                    ON (district_hierarchy.parent_id = address_objects_ids.id)
                        AND (district_hierarchy.id = address_objects.id)
                        AND (NOT district_hierarchy.id IS NULL);

CREATE TEMP TABLE administrative_address_objects_1 (key, name_1, object_type_1, level, additional_info, id, parent_id)
AS
SELECT addresses_fields.key                    AS key,
       address_objects.name                    AS name_1,
       address_objects.object_type             AS object_type_1,
       address_objects.level                   AS level,
       address_objects.address_additional_info AS additional_info,
       address_objects.id                      AS id,
       address_objects.parent_id               AS parent_id
FROM addresses_fields AS addresses_fields
         INNER JOIN reg_address_objects AS address_objects
                    ON (address_objects.name = addresses_fields.name_1)
                        AND (address_objects.object_type = addresses_fields.object_type_1)
                        AND (address_objects.ru_subject_code IN (&region_codes))
         INNER JOIN regions AS address_objects_ids
                    ON (address_objects_ids.id = addresses_fields.id)
                        AND (address_objects_ids.is_district = FALSE)
         INNER JOIN reg_administrative_hierarchy AS administrative_hierarchy
                    ON (administrative_hierarchy.parent_id = address_objects_ids.id)
                        AND (administrative_hierarchy.id = address_objects.id)
                        AND (NOT administrative_hierarchy.id IS NULL);

CREATE TEMP TABLE administrative_address_objects_2 (key, name_2, object_type_2, level, additional_info, id, parent_id)
AS
SELECT addresses_fields.key                    AS key,
       address_objects.name                    AS name_2,
       address_objects.object_type             AS object_type_2,
       address_objects.level                   AS level,
       address_objects.address_additional_info AS additional_info,
       address_objects.id                      AS id,
       address_objects.parent_id               AS parent_id
FROM addresses_fields AS addresses_fields
         INNER JOIN reg_address_objects AS address_objects
                    ON (address_objects.name = addresses_fields.name_2)
                        AND (address_objects.object_type = addresses_fields.object_type_2)
                        AND (address_objects.ru_subject_code IN (&region_codes))
         INNER JOIN administrative_address_objects_1 AS address_objects_ids
                    ON (address_objects_ids.key = addresses_fields.key)
         INNER JOIN reg_administrative_hierarchy AS administrative_hierarchy
                    ON (administrative_hierarchy.parent_id = address_objects_ids.id)
                        AND (administrative_hierarchy.id = address_objects.id)
                        AND (NOT administrative_hierarchy.id IS NULL);

CREATE TEMP TABLE administrative_address_objects_3 (key, name_3, object_type_3, level, additional_info, id, parent_id)
AS
SELECT addresses_fields.key                    AS key,
       address_objects.name                    AS name_3,
       address_objects.object_type             AS object_type_3,
       address_objects.level                   AS level,
       address_objects.address_additional_info AS additional_info,
       address_objects.id                      AS id,
       address_objects.parent_id               AS parent_id
FROM addresses_fields AS addresses_fields
         INNER JOIN reg_address_objects AS address_objects
                    ON (address_objects.name = addresses_fields.name_3)
                        AND (address_objects.object_type = addresses_fields.object_type_3)
                        AND (address_objects.ru_subject_code IN (&region_codes))
         INNER JOIN administrative_address_objects_2 AS address_objects_ids
                    ON (address_objects_ids.key = addresses_fields.key)
         INNER JOIN reg_administrative_hierarchy AS administrative_hierarchy
                    ON (administrative_hierarchy.parent_id = address_objects_ids.id)
                        AND (administrative_hierarchy.id = address_objects.id)
                        AND (NOT administrative_hierarchy.id IS NULL);

CREATE TEMP TABLE administrative_address_objects_4 (key, name_4, object_type_4, level, additional_info, id, parent_id)
AS
SELECT addresses_fields.key                    AS key,
       address_objects.name                    AS name_4,
       address_objects.object_type             AS object_type_4,
       address_objects.level                   AS level,
       address_objects.address_additional_info AS additional_info,
       address_objects.id                      AS id,
       address_objects.parent_id               AS parent_id
FROM addresses_fields AS addresses_fields
         INNER JOIN reg_address_objects AS address_objects
                    ON (address_objects.name = addresses_fields.name_4)
                        AND (address_objects.object_type = addresses_fields.object_type_4)
                        AND (address_objects.ru_subject_code IN (&region_codes))
         INNER JOIN administrative_address_objects_3 AS address_objects_ids
                    ON (address_objects_ids.key = addresses_fields.key)
         INNER JOIN reg_administrative_hierarchy AS administrative_hierarchy
                    ON (administrative_hierarchy.parent_id = address_objects.id)
                        AND (administrative_hierarchy.id = address_objects.id)
                        AND (NOT administrative_hierarchy.id IS NULL);

CREATE TEMP TABLE administrative_address_objects_5 (key, name_5, object_type_5, level, additional_info, id, parent_id)
AS
SELECT addresses_fields.key                    AS key,
       address_objects.name                    AS name_5,
       address_objects.object_type             AS object_type_5,
       address_objects.level                   AS level,
       address_objects.address_additional_info AS additional_info,
       address_objects.id                      AS id,
       address_objects.parent_id               AS parent_id
FROM addresses_fields AS addresses_fields
         INNER JOIN reg_address_objects AS address_objects
                    ON (address_objects.name = addresses_fields.name_5)
                        AND (address_objects.object_type = addresses_fields.object_type_5)
                        AND (address_objects.ru_subject_code IN (&region_codes))
         INNER JOIN administrative_address_objects_4 AS address_objects_ids
                    ON (address_objects_ids.key = addresses_fields.key)
         INNER JOIN reg_administrative_hierarchy AS administrative_hierarchy
                    ON (administrative_hierarchy.parent_id = address_objects.id)
                        AND (administrative_hierarchy.id = address_objects.id)
                        AND (NOT administrative_hierarchy.id IS NULL);

CREATE TEMP TABLE address_objects
    (key,
     id,
     id_level_0,
     id_level_1,
     id_level_2,
     id_level_3,
     id_level_4,
     id_level_5,
     level_0,
     level_1,
     level_2,
     level_3,
     level_4,
     level_5,
     name_0,
     object_type_0,
     name_1,
     object_type_1,
     name_2,
     object_type_2,
     name_3,
     object_type_3,
     name_4,
     object_type_4,
     name_5,
     object_type_5,
     additional_info,
     level)
AS
SELECT addresses_fields.key                                                                     AS key,
       COALESCE(address_objects_level_5.id,
                COALESCE(address_objects_level_4.id,
                         COALESCE(address_objects_level_3.id,
                                  COALESCE(
                                          address_objects_level_2.id,
                                          COALESCE(address_objects_level_1.id,
                                                   address_objects_level_0.id)))))              AS id,
       address_objects_level_0.id                                                               AS id_level_0,
       address_objects_level_1.id                                                               AS id_level_1,
       address_objects_level_2.id                                                               AS id_level_2,
       address_objects_level_3.id                                                               AS id_level_3,
       address_objects_level_4.id                                                               AS id_level_4,
       address_objects_level_5.id                                                               AS id_level_5,
       address_objects_level_0.level                                                            AS level_0,
       address_objects_level_1.level                                                            AS level_1,
       address_objects_level_2.level                                                            AS level_2,
       address_objects_level_3.level                                                            AS level_3,
       address_objects_level_4.level                                                            AS level_4,
       address_objects_level_5.level                                                            AS level_5,
       address_objects_level_0.name_0                                                           AS name_0,
       address_objects_level_0.object_type_0                                                    AS object_type_0,
       address_objects_level_1.name_1                                                           AS name_1,
       address_objects_level_1.object_type_1                                                    AS object_type_1,
       address_objects_level_2.name_2                                                           AS name_2,
       address_objects_level_2.object_type_2                                                    AS object_type_2,
       address_objects_level_3.name_3                                                           AS name_3,
       address_objects_level_3.object_type_3                                                    AS object_type_3,
       address_objects_level_4.name_4                                                           AS name_4,
       address_objects_level_4.object_type_4                                                    AS object_type_4,
       address_objects_level_5.name_5                                                           AS name_5,
       address_objects_level_5.object_type_5                                                    AS object_type_5,
       COALESCE(address_objects_level_5.additional_info,
                COALESCE(address_objects_level_4.additional_info,
                         COALESCE(address_objects_level_3.additional_info,
                                  COALESCE(
                                          address_objects_level_2.additional_info,
                                          COALESCE(address_objects_level_1.additional_info,
                                                   address_objects_level_0.additional_info))))) AS
                                                                                                   additional_info,
       COALESCE(address_objects_level_5.level,
                COALESCE(address_objects_level_4.level,
                         COALESCE(address_objects_level_3.level,
                                  COALESCE(address_objects_level_2.level,
                                           COALESCE(address_objects_level_1.level,
                                                    address_objects_level_0.level)))))          AS
                                                                                                   level
FROM addresses_fields AS addresses_fields
         LEFT JOIN regions AS address_objects_level_0
                   ON addresses_fields.key = address_objects_level_0.key
         LEFT JOIN district_address_objects_1 AS address_objects_level_1
                   ON (address_objects_level_0.id = address_objects_level_1.parent_id)
                       AND addresses_fields.key = address_objects_level_1.key
         LEFT JOIN district_address_objects_2 AS address_objects_level_2
                   ON (address_objects_level_1.id = address_objects_level_2.parent_id)
                       AND addresses_fields.key = address_objects_level_2.key
         LEFT JOIN district_address_objects_3 AS address_objects_level_3
                   ON (address_objects_level_2.id = address_objects_level_3.parent_id)
                       AND addresses_fields.key = address_objects_level_3.key
         LEFT JOIN district_address_objects_4 AS address_objects_level_4
                   ON (address_objects_level_3.id = address_objects_level_4.parent_id)
                       AND addresses_fields.key = address_objects_level_4.key
         LEFT JOIN district_address_objects_5 AS address_objects_level_5
                   ON (address_objects_level_4.id = address_objects_level_5.parent_id)
                       AND addresses_fields.key = address_objects_level_5.key
WHERE address_objects_level_0.is_district = TRUE
UNION ALL
SELECT addresses_fields.key,
       COALESCE(administrative_address_objects_5.id,
                COALESCE(administrative_address_objects_4.id,
                         COALESCE(administrative_address_objects_3.id,
                                  COALESCE(administrative_address_objects_2.id,
                                           COALESCE(administrative_address_objects_1.id,
                                                    administrative_address_objects_0.id))))),
       administrative_address_objects_0.id,
       administrative_address_objects_1.id,
       administrative_address_objects_2.id,
       administrative_address_objects_3.id,
       administrative_address_objects_4.id,
       administrative_address_objects_5.id,
       administrative_address_objects_0.level,
       administrative_address_objects_1.level,
       administrative_address_objects_2.level,
       administrative_address_objects_3.level,
       administrative_address_objects_4.level,
       administrative_address_objects_5.level,
       administrative_address_objects_0.name_0,
       administrative_address_objects_0.object_type_0,
       administrative_address_objects_1.name_1,
       administrative_address_objects_1.object_type_1,
       administrative_address_objects_2.name_2,
       administrative_address_objects_2.object_type_2,
       administrative_address_objects_3.name_3,
       administrative_address_objects_3.object_type_3,
       administrative_address_objects_4.name_4,
       administrative_address_objects_4.object_type_4,
       administrative_address_objects_5.name_5,
       administrative_address_objects_5.object_type_5,
       COALESCE(administrative_address_objects_5.additional_info,
                COALESCE(administrative_address_objects_4.additional_info,
                         COALESCE(administrative_address_objects_3.additional_info,
                                  COALESCE(administrative_address_objects_2.additional_info,
                                           COALESCE(administrative_address_objects_1.additional_info,
                                                    administrative_address_objects_0.additional_info))))),
       COALESCE(administrative_address_objects_5.level,
                COALESCE(administrative_address_objects_4.level,
                         COALESCE(administrative_address_objects_3.level,
                                  COALESCE(
                                          administrative_address_objects_2.level,
                                          COALESCE(administrative_address_objects_1.level,
                                                   administrative_address_objects_0.level)))))
FROM addresses_fields AS addresses_fields
         LEFT JOIN regions AS administrative_address_objects_0
                   ON addresses_fields.key = administrative_address_objects_0.key
         LEFT JOIN district_administrative_address_objects_1 AS administrative_address_objects_1
                   ON (administrative_address_objects_0.id = administrative_address_objects_1.parent_id)
                       AND addresses_fields.key = administrative_address_objects_1.key
         LEFT JOIN district_administrative_address_objects_2 AS administrative_address_objects_2
                   ON (administrative_address_objects_1.id = administrative_address_objects_2.parent_id)
                       AND addresses_fields.key = administrative_address_objects_2.key
         LEFT JOIN district_administrative_address_objects_3 AS administrative_address_objects_3
                   ON (administrative_address_objects_2.id = administrative_address_objects_3.parent_id)
                       AND addresses_fields.key = administrative_address_objects_3.key
         LEFT JOIN district_administrative_address_objects_4 AS administrative_address_objects_4
                   ON (administrative_address_objects_3.id = administrative_address_objects_4.parent_id)
                       AND addresses_fields.key = administrative_address_objects_4.key
         LEFT JOIN district_administrative_address_objects_5 AS administrative_address_objects_5
                   ON (administrative_address_objects_4.id = administrative_address_objects_5.parent_id)
                       AND addresses_fields.key = administrative_address_objects_5.key
WHERE administrative_address_objects_0.is_district = FALSE;

SELECT address_objects.key           AS address_key,
       address_objects.id            AS id,
       address_objects.id_level_0    AS id_level_0,
       address_objects.id_level_1    AS id_level_1,
       address_objects.id_level_2    AS id_level_2,
       address_objects.id_level_3    AS id_level_3,
       address_objects.id_level_4    AS id_level_4,
       address_objects.id_level_5    AS id_level_5,
       address_objects.name_0        AS name_0,
       address_objects.object_type_0 AS object_type_0,
       address_objects.name_1        AS name_1,
       address_objects.object_type_1 AS object_type_1,
       address_objects.name_2        AS name_2,
       address_objects.object_type_2 AS object_type_2,
       address_objects.name_3        AS name_3,
       address_objects.object_type_3 AS object_type_3,
       address_objects.name_4        AS name_4,
       address_objects.object_type_4 AS object_type_4,
       address_objects.name_5        AS name_5,
       address_objects.object_type_5 AS object_type_5,
       address_objects.level         AS level,
       address_objects.level_0       AS level_0,
       address_objects.level_1       AS level_1,
       address_objects.level_2       AS level_2,
       address_objects.level_3       AS level_3,
       address_objects.level_4       AS level_4,
       address_objects.level_5       AS level_5
FROM address_objects AS address_objects;

SELECT address_objects.key                      AS house_key,
       houses_buildings.address_object          AS address_object,
       houses_buildings.additional_address_info AS additional_info,
       houses_buildings.buildings               AS buildings,
       info.id                                  AS id,
       info.ru_subject_code                     AS ru_subject_code,
       info.postal_code                         AS postal_code,
       info.oktmo                               AS oktmo,
       info.okato                               AS okato,
       info.plot_code_ifnsul                    AS plot_code_ifnsul,
       info.plot_code_ifnsfl                    AS plot_code_ifnsfl,
       info.code_ifnsul                         AS code_ifnsul,
       info.code_ifnsfl                         AS code_ifnsfl,
FROM address_objects AS address_objects
         INNER JOIN reg_houses_buildings AS houses_buildings
                    ON (houses_buildings.address_object = address_objects.id)
                        AND (NOT houses_buildings.buildings IS NULL)
         LEFT JOIN reg_additional_address_info AS info
                   ON (info.id = houses_buildings.additional_address_info);

SELECT address_objects.key       AS plot_key,
       land_plots.address_object AS address_object,
       land_plots.plots          AS plots,
       info.id                   AS id,
       info.ru_subject_code      AS ru_subject_code,
       info.postal_code          AS postal_code,
       info.oktmo                AS oktmo,
       info.okato                AS okato,
       info.plot_code_ifnsul     AS plot_code_ifnsul,
       info.plot_code_ifnsfl     AS plot_code_ifnsfl,
       info.code_ifnsul          AS code_ifnsul,
       info.code_ifnsfl          AS code_ifnsfl
FROM address_objects AS address_objects
         INNER JOIN reg_land_plots AS land_plots
                    ON (land_plots.address_object = address_objects.id)
                        AND (NOT land_plots.plots IS NULL)
         LEFT JOIN reg_additional_address_info AS info
                   ON (info.id = land_plots.additional_address_info);
