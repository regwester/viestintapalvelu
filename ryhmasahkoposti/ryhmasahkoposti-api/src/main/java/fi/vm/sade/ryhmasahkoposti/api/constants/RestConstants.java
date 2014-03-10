package fi.vm.sade.ryhmasahkoposti.api.constants;

public interface RestConstants {
	public static final String PATH_REPORT_MESSAGES = "reportMessages";
	public static final String PATH_REPORT_MESSAGES_LIST = "list";	
	public static final String PATH_REPORT_MESSAGES_SEARCH = "search/{searchArgument}";
	public static final String PATH_REPORT_MESSAGE_VIEW = "view/{messageID}";

	public static final String PARAM_SEARCH_ARGUMENT = "searchArgument";
	public static final String PARAM_MESSAGE_ID = "messageID";
}
