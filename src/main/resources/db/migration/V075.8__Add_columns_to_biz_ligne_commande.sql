LOCK TABLES `BIZ_LIGNE_COMMANDE` WRITE;
	ALTER TABLE `BIZ_LIGNE_COMMANDE`
		ADD COLUMN  `DATE_UTILISATION` date DEFAULT NULL,
		ADD COLUMN  `DELAI` varchar(250) DEFAULT NULL,
		ADD COLUMN  `OBS` varchar(250) DEFAULT NULL;
UNLOCK TABLES;
