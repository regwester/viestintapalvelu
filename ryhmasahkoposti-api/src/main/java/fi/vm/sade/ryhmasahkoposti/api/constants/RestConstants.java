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
    String PATH_REPORT_MESSAGES = "reportMessages";
    String PATH_REPORT_MESSAGES_LIST = "list";
    String PATH_REPORT_MESSAGES_ORGANIZATION_SEARCH = "orgSearch";
    String PATH_REPORT_MESSAGES_SEARCH = "search";
    String PATH_REPORT_MESSAGES_CURRENT_USER = "currentUserHistory";
    String PATH_REPORT_MESSAGE_VIEW = "view/{messageID}";
    String PATH_REPORT_MESSAGE_BY_LETTER_VIEW = "view/letter/{letterID}";
    String PATH_REPORT_MESSAGE_VIEW_WITH_PAGING = "vwp/{messageID}";
    String PATH_REPORT_MESSAGE_FAILED_VIEW_WITH_PAGING = "failed/{messageID}";
    String PATH_REPORT_MESSAGE_BOUNCED_VIEW_WITH_PAGING = "bounced/{messageID}";
    String PATH_REPORT_MESSAGE_DOWNLOAD_ATTACHMENT = "attachment/{attachmentID}";

    String PARAM_ORGANIZATION_OID = "orgOid";
    String PARAM_SEARCH_ARGUMENT = "searchArgument";
    String PARAM_MESSAGE_ID = "messageID";
    String PARAM_LETTER_ID = "letterID";
    String PARAM_NUMBER_OF_ROWS = "nbrofrows";
    String PARAM_PAGE = "page";
    String PARAM_SORTED_BY = "sortedby";
    String PARAM_ORDER = "order";
    String PARAM_PROCESS = "process";
    String PARAM_ATTACHMENT_ID = "attachmentID";
}
