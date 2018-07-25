package com.imaginepartners.imagineworkflow.context;

import org.activiti.engine.delegate.JavaDelegate;

public interface IwEl {
	/**
	 * Trying to convert Tableau to List object with given variable names
	 * @param tab old variable name
	 * @param list new variable name
	 * @return instance of JavaDelegate object
	 */
	public JavaDelegate convertToArray(final String tab, final String list);

	/**
	 * Save the current user in Act_RU_USER, RU for RUNTIME
	 * @param varName variable name that holds the current user
	 * @return instance of JavaDelegate Object
	 */
	public JavaDelegate setCurrentUserInVar(final String varName);

	/**
	 *
	 * @param isMailServerOk name of the variable
	 * @return new instance of JavaDelegate.
	 */
	public JavaDelegate checkMailServer(final String isMailServerOk);

	public JavaDelegate getUsersMails(final String userIdTabName, final String usersEmailsListName);

	public JavaDelegate getUserMail(final String userId, final String usersEmailName);

	/**
	 * Launch a new process instance for the given definition
	 * @param processDefKey using ProcessDefinition.ProcessDefKey attribute
	 * for starting new ProcessInstance of given definition process Object
	 * @return new instance of JavaDelegate.
	 */
	public JavaDelegate launchProcess(final String processDefKey);

	public JavaDelegate launchEvaluationProcess(String process);

	/**
	 *
	 * @return id Responsable for current user
	 */
	public String getResponsable();

	/**
	 *
	 * @return the name of current user
	 */
	public String getNomUtilisateur();

	/**
	 *
	 * @return the id of current user
	 */
	public String getIdUtilisateur();

	/**
	 *
	 * @return the email address of current user
	 */
	public String getEmailUtilisateur();

	public String get(String methodName, Class[] paramTypes) ;
}
