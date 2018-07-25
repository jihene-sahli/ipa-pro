LOCK TABLES `IW_CONFIG` WRITE;
INSERT INTO `IW_CONFIG` (`CONFIG_NAME`, `CONFIG_DESCRIPTION`, `CONFIG_EDITABLE`, `CONFIG_VALUE`, `DEFAULT_VALUE`) VALUES ('enable_stitcky_title', 'Fixer le titre de la page en haut de page ', b'1', 'false', 'false');
UNLOCK TABLES;
