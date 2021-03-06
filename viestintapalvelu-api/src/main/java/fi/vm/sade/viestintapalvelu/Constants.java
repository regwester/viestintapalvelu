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
package fi.vm.sade.viestintapalvelu;

import java.nio.charset.Charset;

public class Constants {
    public static final int IPOST_BATCH_LIMIT = 500;
    public static final String LIITE_TEMPLATE = "/templates/liite_{LANG}.html";
    public static final String KOEKUTSUKIRJE_TEMPLATE = "/templates/koekutsukirje_{LANG}.html";
    public static final String LETTER_TEMPLATE = "/templates/letter_{LANG}.html";
    public static final String IPOST_TEMPLATE = "/templates/ipost.xml";
    public static final String LETTER_IPOST_TEMPLATE = "/templates/letter_ipost.xml";
    public static final String ADDRESS_LABEL_PDF_TEMPLATE = "/templates/osoitetarrat.html";
    public static final String ADDRESS_LABEL_XLS_TEMPLATE = "/templates/osoitetarrat.xls";
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Boolean IPOST_TEST = Boolean.FALSE;
    
    // Roolit
    public static final String ROLE_APP_ASIAKIRJAPALVELU_READ = "ROLE_APP_ASIAKIRJAPALVELU_READ";
    public static final String ROLE_APP_ASIAKIRJAPALVELU_CREATE_TEMPLATE = "ROLE_APP_ASIAKIRJAPALVELU_CREATE_TEMPLATE";
    public static final String ROLE_APP_ASIAKIRJAPALVELU_CREATE_LETTER = "ROLE_APP_ASIAKIRJAPALVELU_CREATE_LETTER";
    
    // Käyttöoikeudet
    public static final String ASIAKIRJAPALVELU_READ = "hasRole('"+ROLE_APP_ASIAKIRJAPALVELU_READ+"')";
    public static final String ASIAKIRJAPALVELU_CREATE_TEMPLATE = "hasRole('"+ ROLE_APP_ASIAKIRJAPALVELU_CREATE_TEMPLATE + "')";
    public static final String ASIAKIRJAPALVELU_CREATE_LETTER = "hasRole('"+ROLE_APP_ASIAKIRJAPALVELU_CREATE_LETTER+"')";
    public static final String ASIAKIRJAPALVELU_SEND_LETTER_EMAIL = "hasRole('ROLE_APP_ASIAKIRJAPALVELU_SEND_LETTER_EMAIL')";
    public static final String IPOSTI_READ = "hasRole('ROLE_APP_IPOSTI_READ')";
    public static final String IPOSTI_SEND = "hasRole('ROLE_APP_IPOSTI_SEND')";
    public static final String ASIOINTITILI_CRUD = "hasRole('ROLE_APP_ASIAKIRJAPALVELU_ASIOINTITILICRUD')";
    
    // REST parameters
    public static final String PARAM_ORGANIZATION_OID = "orgOid";
    public static final String PARAM_LETTER_BATCH_SEARCH_ARGUMENT = "searchArgument";
    public static final String PARAM_RECEIVER_SEARCH_ARGUMENT = "receiverSearchArgument";
    public static final String PARAM_APPLICATION_PERIOD = "applicationPeriod";
    public static final String PARAM_INCLUDE_OLDER_RESULTS = "includeOlder";
    public static final String PARAM_SEARCH_TARGET = "searchTarget";
    public static final String PARAM_NUMBER_OF_ROWS = "nbrofrows";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_SORTED_BY = "sortedby";
    public static final String PARAM_ORDER = "order";
    public static final String PARAM_QUERY = "query";
    
    public static final String PARAM_PROCESS = "process";

    public static final String PARAM_ID = "id";

    // Error message
    public static final Object INTERNAL_SERVICE_ERROR = "Internal service error";
    
    // OPH Organization identification
    public static final String OPH_ORGANIZATION_OID = "1.2.246.562.10.00000000001";
}
