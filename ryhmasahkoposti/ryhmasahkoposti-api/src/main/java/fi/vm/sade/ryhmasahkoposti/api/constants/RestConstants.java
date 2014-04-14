package fi.vm.sade.ryhmasahkoposti.api.constants;

public interface RestConstants {
	public static final String PATH_REPORT_MESSAGES = "reportMessages";
    public static final String PATH_REPORT_MESSAGES_LIST = "list";
    public static final String PATH_REPORT_MESSAGES_ORGANIZATION_SEARCH = "orgSearch";
	public static final String PATH_REPORT_MESSAGES_SEARCH = "search";
	public static final String PATH_REPORT_MESSAGE_VIEW = "view/{messageID}";
	public static final String PATH_REPORT_MESSAGE_VIEW_WITH_PAGING = "vwp/{messageID}";
    public static final String PATH_REPORT_MESSAGE_FAILED_VIEW_WITH_PAGING = "failed/{messageID}";

    public static final String PARAM_ORGANIZATION_OID = "orgOid";
	public static final String PARAM_SEARCH_ARGUMENT = "searchArgument";
	public static final String PARAM_MESSAGE_ID = "messageID";
	public static final String PARAM_NUMBER_OF_ROWS = "nbrofrows";
	public static final String PARAM_PAGE = "page";
	public static final String PARAM_SORTED_BY = "sortedby";
	public static final String PARAM_ORDER = "order";
}
