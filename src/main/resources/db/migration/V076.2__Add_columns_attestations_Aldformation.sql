LOCK TABLES `ALD_FORMATION` WRITE;
	ALTER TABLE `ALD_FORMATION`

	ADD COLUMN `ATTESTATION_CH` bigint(20) DEFAULT NULL ,
	ADD COLUMN `ATTESTATION_FR` bigint(20) DEFAULT NULL;

UNLOCK TABLES;

