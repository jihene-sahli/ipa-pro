LOCK TABLES `RH_CANDIDAT` WRITE;
ALTER TABLE `RH_CANDIDAT`
	ADD COLUMN `HEURE_DEBUT_ENTRETIEN_SOUHAITEE` TIME NULL AFTER `DATE_ENTRETIEN_SOUHAITEE2`,
	ADD COLUMN `HEURE_FIN_ENTRETIEN_SOUHAITEE` TIME NULL AFTER `HEURE_DEBUT_ENTRETIEN_SOUHAITEE`;
UNLOCK TABLES;
