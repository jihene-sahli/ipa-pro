LOCK TABLES `IW_MULTILINE_ENTITY` WRITE;
DELETE FROM `IW_MULTILINE_ENTITY` WHERE `IW_NAME` = 'BizAgenda';
UPDATE `IW_MULTILINE_ENTITY` SET `IW_CLASS` = 'com.imaginepartners.imagineworkflow.models.rh.RhConge', `IW_NAME` = 'RhConge' WHERE `IW_MULTILINE_ENTITY_ID`='109';
UPDATE `IW_MULTILINE_ENTITY` SET `IW_CLASS` = 'com.imaginepartners.imagineworkflow.models.rh.RhMotifConge', `IW_NAME` = 'RhMotifConge' WHERE `IW_MULTILINE_ENTITY_ID`='121';
UPDATE `IW_MULTILINE_ENTITY` SET `IW_CLASS` = 'com.imaginepartners.imagineworkflow.models.rh.RhTypeConge', `IW_NAME` = 'RhTypeConge' WHERE `IW_MULTILINE_ENTITY_ID`='141';
UNLOCK TABLES;
