/*
 * The column doesn't have a default value to avoid updating the column on migration. The pipeline server
 * will populate the column as new beneficaries are added or existing beneficaries are updated. 
 */

alter table "Beneficiaries" add column "derivedMailingAddress1" varchar(40);

alter table "Beneficiaries" add column "derivedMailingAddress2" varchar(40);

alter table "Beneficiaries" add column "derivedMailingAddress3" varchar(40);

alter table "Beneficiaries" add column "derivedMailingAddress4" varchar(40);

alter table "Beneficiaries" add column "derivedMailingAddress5" varchar(40);

alter table "Beneficiaries" add column "derivedMailingAddress6" varchar(40);

alter table "Beneficiaries" add column "derivedCityName" varchar(100);

alter table "Beneficiaries" add column "mbiEffectiveDate" date;

alter table "Beneficiaries" add column "mbiObsoleteDate" date;

alter table "InpatientClaimLines" add column "clmUncompensatedCareAmount" numeric(38, 2)