LOCK TABLES `ACT_RU_TASK` WRITE;
	ALTER TABLE `ACT_RU_TASK`
		CHANGE `CREATE_TIME_` `CREATE_TIME_` TIMESTAMP NULL DEFAULT NULL,
		CHANGE `DUE_DATE_` `DUE_DATE_` DATETIME NULL DEFAULT NULL;
UNLOCK TABLES;
LOCK TABLES `ACT_RU_JOB` WRITE;
	ALTER TABLE `ACT_RU_JOB`
		CHANGE `LOCK_EXP_TIME_` `LOCK_EXP_TIME_` TIMESTAMP NULL DEFAULT NULL,
		CHANGE `DUEDATE_` `DUEDATE_` TIMESTAMP NULL DEFAULT NULL;
UNLOCK TABLES;
LOCK TABLES `ACT_RU_EXECUTION` WRITE;
	ALTER TABLE `ACT_RU_EXECUTION` CHANGE `LOCK_TIME_` `LOCK_TIME_` TIMESTAMP NULL DEFAULT NULL;
UNLOCK TABLES;
LOCK TABLES `ACT_RU_EVENT_SUBSCR` WRITE;
	ALTER TABLE `ACT_RU_EVENT_SUBSCR` CHANGE `CREATED_` `CREATED_` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
UNLOCK TABLES;
LOCK TABLES `ACT_RE_MODEL` WRITE;
	ALTER TABLE `ACT_RE_MODEL`
		CHANGE `CREATE_TIME_` `CREATE_TIME_` TIMESTAMP NULL DEFAULT NULL,
		CHANGE `LAST_UPDATE_TIME_` `LAST_UPDATE_TIME_` TIMESTAMP NULL DEFAULT NULL;
UNLOCK TABLES;
LOCK TABLES `ACT_RE_DEPLOYMENT` WRITE;
	ALTER TABLE `ACT_RE_DEPLOYMENT` CHANGE `DEPLOY_TIME_` `DEPLOY_TIME_` TIMESTAMP NULL DEFAULT NULL;
UNLOCK TABLES;
LOCK TABLES `ACT_HI_VARINST` WRITE;
	ALTER TABLE `ACT_HI_VARINST`
		CHANGE `CREATE_TIME_` `CREATE_TIME_` DATETIME NULL DEFAULT NULL,
		CHANGE `LAST_UPDATED_TIME_` `LAST_UPDATED_TIME_` DATETIME NULL DEFAULT NULL;
UNLOCK TABLES;
LOCK TABLES `ACT_HI_TASKINST` WRITE;
	ALTER TABLE `ACT_HI_TASKINST`
		CHANGE `START_TIME_` `START_TIME_` DATETIME NOT NULL,
		CHANGE `CLAIM_TIME_` `CLAIM_TIME_` DATETIME NULL DEFAULT NULL,
		CHANGE `END_TIME_` `END_TIME_` DATETIME NULL DEFAULT NULL,
		CHANGE `DUE_DATE_` `DUE_DATE_` DATETIME NULL DEFAULT NULL;
UNLOCK TABLES;
LOCK TABLES `ACT_HI_PROCINST` WRITE;
	ALTER TABLE `ACT_HI_PROCINST` CHANGE `START_TIME_` `START_TIME_` DATETIME NOT NULL, CHANGE `END_TIME_` `END_TIME_` DATETIME NULL DEFAULT NULL;
UNLOCK TABLES;
LOCK TABLES `ACT_HI_DETAIL` WRITE;
	ALTER TABLE `ACT_HI_DETAIL` CHANGE `TIME_` `TIME_` DATETIME NOT NULL;
UNLOCK TABLES;
LOCK TABLES `ACT_HI_COMMENT` WRITE;
	ALTER TABLE `ACT_HI_COMMENT` CHANGE `TIME_` `TIME_` DATETIME NOT NULL;
UNLOCK TABLES;
LOCK TABLES `ACT_HI_ATTACHMENT` WRITE;
	ALTER TABLE `ACT_HI_ATTACHMENT` CHANGE `TIME_` `TIME_` DATETIME NULL DEFAULT NULL;
UNLOCK TABLES;
LOCK TABLES `ACT_HI_ACTINST` WRITE;
	ALTER TABLE `ACT_HI_ACTINST`
		CHANGE `START_TIME_` `START_TIME_` DATETIME NOT NULL,
		CHANGE `END_TIME_` `END_TIME_` DATETIME NULL DEFAULT NULL;
UNLOCK TABLES;
LOCK TABLES `ACT_EVT_LOG` WRITE;
	ALTER TABLE `ACT_EVT_LOG` CHANGE `TIME_STAMP_` `TIME_STAMP_` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CHANGE `LOCK_TIME_` `LOCK_TIME_` TIMESTAMP NULL DEFAULT NULL;
UNLOCK TABLES;