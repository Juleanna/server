DROP TABLE teleport;
DELETE FROM clanhall_functions;
DELETE FROM fort_functions;
DELETE FROM castle_functions;

ALTER TABLE `castle`
ADD COLUMN `next_tax` INT NOT NULL DEFAULT 0 AFTER `taxPercent`,
ADD COLUMN `next_tax_time` bigint(13) unsigned NOT NULL DEFAULT '0' AFTER `next_tax`;