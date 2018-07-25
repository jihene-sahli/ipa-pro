LOCK TABLES `IW_CONFIG` WRITE;
INSERT INTO `IW_CONFIG` (`ID`, `CONFIG_NAME`, `CONFIG_DESCRIPTION`, `CONFIG_EDITABLE`, `CONFIG_VALUE`, `DEFAULT_VALUE`, `CREATED_AT`, `UPDATED_AT`) VALUES (NULL, 'show_agenda_reservation', 'Afficher la réservation de ressources dans l\'agenda', b'1', 'true', 'true', CURRENT_TIMESTAMP, NULL);
UNLOCK TABLES;
