LOCK TABLES `IW_CONFIG` WRITE;
INSERT INTO `IW_CONFIG` (`CONFIG_NAME`, `CONFIG_DESCRIPTION`, `CONFIG_EDITABLE`, `CONFIG_VALUE`, `DEFAULT_VALUE`, `CREATED_AT`, `UPDATED_AT`) VALUES
	('file_export_path_csv', 'Chemin d''export de fichiers CSV', b'1', '', '', '2016-11-10 00:00:00', NULL),
	('file_export_path_excel', 'Chemin d''export de fichiers EXCEL', b'1', '', '', '2016-11-10 00:00:00', NULL),
	('file_export_path_pdf', 'Chemin d''export de fichiers PDF', b'1', '', '', '2016-11-10 00:00:00', NULL),
	('file_export_path_log', 'Chemin d''export de fichiers LOG', b'1', '', '', '2016-11-10 00:00:00', NULL);
UNLOCK TABLES;
