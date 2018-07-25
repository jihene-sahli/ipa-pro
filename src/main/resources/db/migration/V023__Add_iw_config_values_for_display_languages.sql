LOCK TABLES `IW_CONFIG` WRITE;
INSERT INTO `IW_CONFIG` (`CONFIG_NAME`, `CONFIG_DESCRIPTION`, `CONFIG_EDITABLE`, `CONFIG_VALUE`, `DEFAULT_VALUE`) VALUES ('display_traduction_list','Afficher la liste des langue dans la page d''accueil',0x01,'true','1');
UNLOCK TABLES;
