/**
* Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
*
* This program is free software:  Licensed under the EUPL, Version 1.1 or - as
* soon as they will be approved by the European Commission - subsequent versions
* of the EUPL (the "Licence");
*
* You may not use this work except in compliance with the Licence.
* You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* European Union Public Licence for more details.
**/
package fi.vm.sade.ryhmasahkoposti.api.constants;

public interface RestConstants {
    public static final String PATH_REPORT_MESSAGES = "reportMessages";
    public static final String PATH_REPORT_MESSAGES_LIST = "list";
    public static final String PATH_REPORT_MESSAGES_ORGANIZATION_SEARCH = "orgSearch";
    public static final String PATH_REPORT_MESSAGES_SEARCH = "search";
    public static final String PATH_REPORT_MESSAGES_CURRENT_USER = "currentUserHistory";
    public static final String PATH_REPORT_MESSAGE_VIEW = "view/{messageID}";
    public static final String PATH_REPORT_MESSAGE_VIEW_WITH_PAGING = "vwp/{messageID}";
    public static final String PATH_REPORT_MESSAGE_FAILED_VIEW_WITH_PAGING = "failed/{messageID}";
    public static final String PATH_REPORT_MESSAGE_BOUNCED_VIEW_WITH_PAGING = "bounced/{messageID}";
    public static final String PATH_REPORT_MESSAGE_DOWNLOAD_ATTACHMENT = "attachment/{attachmentID}";

    public static final String PARAM_ORGANIZATION_OID = "orgOid";
    public static final String PARAM_SEARCH_ARGUMENT = "searchArgument";
    public static final String PARAM_MESSAGE_ID = "messageID";
    public static final String PARAM_NUMBER_OF_ROWS = "nbrofrows";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_SORTED_BY = "sortedby";
    public static final String PARAM_ORDER = "order";
    public static final String PARAM_PROCESS = "process";
    public static final String PARAM_ATTACHMENT_ID = "attachmentID";
}
