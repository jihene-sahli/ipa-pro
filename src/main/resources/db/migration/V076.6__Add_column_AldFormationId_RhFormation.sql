LOCK TABLES `RH_FORMATION` WRITE;
ALTER TABLE `RH_FORMATION`
    ADD COLUMN `ALD_FORMATION_ID` INTEGER DEFAULT NULL;
UNLOCK TABLES;