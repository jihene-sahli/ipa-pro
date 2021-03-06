CREATE TABLE IF NOT EXISTS `BIZ_PRIX_VENTE` (
	`ID` bigint(20) NOT NULL AUTO_INCREMENT,
	`CODE` bigint(20) DEFAULT NULL,
	`PRIX_HT` bigint(20) DEFAULT NULL,
	`PRIX_TTC` bigint(20) DEFAULT NULL,
	`TAUX_DE_MARGE` bigint(20) DEFAULT NULL,
	`TYPOLOGIE` varchar(255) DEFAULT NULL,
	`VALEUR_DE_MARGE` bigint(20) DEFAULT NULL,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `BIZ_POLITIQUE_PRIX` (
	`ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`TYPE_MAGASINS` VARCHAR(45) NULL,
	`TAUX_PRIX` VARCHAR(45) NULL,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
