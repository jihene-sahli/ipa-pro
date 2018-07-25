LOCK TABLES `IW_CONFIG` WRITE;
INSERT INTO `IW_CONFIG` (`CONFIG_NAME`, `CONFIG_DESCRIPTION`, `CONFIG_EDITABLE`, `CONFIG_VALUE`, `DEFAULT_VALUE`) VALUES ('show_process_progresssion', 'Afficher/Masquer la barre de progression du processus ', b'1', 'true', 'true');
UNLOCK TABLES;
