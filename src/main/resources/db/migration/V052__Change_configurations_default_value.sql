LOCK TABLES `IW_CONFIG` WRITE;
UPDATE `IW_CONFIG`
SET `CONFIG_DESCRIPTION` = 'Afficher la barre de progression du processus'
WHERE `CONFIG_NAME` = 'show_process_progresssion';
UPDATE `IW_CONFIG`
SET `CONFIG_VALUE` = 'false'
WHERE `CONFIG_NAME` = 'display_traduction_list';
UPDATE `IW_CONFIG`
SET `CONFIG_VALUE` = 'false'
WHERE `CONFIG_NAME` = 'display_completed_tasks';
UPDATE `IW_CONFIG`
SET `CONFIG_VALUE` = 'false'
WHERE `CONFIG_NAME` = 'enable_stitcky_title';
UPDATE `IW_CONFIG`
SET `CONFIG_VALUE` = 'false'
WHERE `CONFIG_NAME` = 'display_folder_number';
UPDATE `IW_CONFIG`
SET `CONFIG_VALUE` = 'false'
WHERE `CONFIG_NAME` = 'display_instance_creation_dialog';
UPDATE `IW_CONFIG`
SET `CONFIG_VALUE` = 'false'
WHERE `CONFIG_NAME` = 'display_involved_task';
UPDATE `IW_CONFIG`
SET `CONFIG_VALUE` = 'false'
WHERE `CONFIG_NAME` = 'display_task_number';
UPDATE `IW_CONFIG`
SET `CONFIG_VALUE` = 'false'
WHERE `CONFIG_NAME` = 'display_traduction_list';
UPDATE `IW_CONFIG`
SET `CONFIG_VALUE` = 'false'
WHERE `CONFIG_NAME` = 'enable_stitcky_title';
UPDATE `IW_CONFIG`
SET `CONFIG_VALUE` = 'false'
WHERE `CONFIG_NAME` = 'ldap_sync_ad_groups';
UPDATE `IW_CONFIG`
SET `CONFIG_VALUE` = 'false'
WHERE `CONFIG_NAME` = 'ldap_sync_ad_membership';
UPDATE `IW_CONFIG`
SET `CONFIG_VALUE` = 'false'
WHERE `CONFIG_NAME` = 'smtp_mail_server_use_ssl';
UPDATE `IW_CONFIG`
SET `CONFIG_VALUE` = 'false'
WHERE `CONFIG_NAME` = 'smtp_mail_server_use_tls';
UPDATE `IW_CONFIG`
SET `CONFIG_VALUE` = 'false'
WHERE `CONFIG_NAME` = 'ldap_sync_ad_users';
UPDATE `IW_CONFIG`
SET `CONFIG_VALUE` = 'false'
WHERE `CONFIG_NAME` = 'show_process_progresssion';
UNLOCK TABLES;
