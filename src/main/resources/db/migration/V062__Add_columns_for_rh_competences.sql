LOCK TABLES `RH_COMPETENCE` WRITE;
	ALTER TABLE `RH_COMPETENCE`
	ADD COLUMN `EVAL_COLLABORATEUR` VARCHAR(500) NULL  ,
	ADD COLUMN `EVAL_MANAGER` VARCHAR(500) NULL ,
	ADD COLUMN `EVAL_NEGOCIE` VARCHAR(500) NULL  ,
	ADD COLUMN `EVAL_N2` VARCHAR(500) NULL  ,
	ADD COLUMN `COMMENTAIRE` VARCHAR(500) NULL ;

UNLOCK TABLES;
