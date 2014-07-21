<%@ page import="org.jsoup.*" %>
<%@ page import="org.jsoup.safety.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>

<%@ page import="org.jsoup.nodes.Entities.EscapeMode" %>
<!DOCTYPE html>
<html id="ng-app" ng-app="viestintapalvelu">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>OPH - Viestint&aumlpalvelu</title>

    <!-- css -->
    <link rel="stylesheet" type="text/css" href="./assets/lib/bootstrap/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="./assets/css/virkailija.css"/>
    <link rel="stylesheet" type="text/css" href="./assets/css/dialogs.css"/>
    <link rel="stylesheet" type="text/css" href="./assets/css/other.css"/>

</head>

<body>
    <div id="application-wrapper">
        <div id="angularbox" ui-view></div>
    </div>

    <!-- libs -->
    <script type="text/javascript" src="./assets/lib/jquery/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="./assets/lib/jquery/jquery.i18n.properties-min-1.0.9.js"></script> 
    <script type="text/javascript" src="./assets/lib/jQuery-File-Upload-9.5.2/vendor/jquery.ui.widget.js"></script>

    <script type="text/javascript" src="./assets/lib/angular/angular.js"></script> 
    <script type="text/javascript" src="./assets/lib/angular/angular-resource.js"></script>
    <script type="text/javascript" src="./assets/lib/angular/angular-animate.js"></script>
    <script type="text/javascript" src="./assets/lib/ui-bootstrap/ui-bootstrap-tpls-0.11.0.min.js"></script>
    <script type="text/javascript" src="./assets/lib/ui-router/angular-ui-router.min.js"></script>
     
    <script type="text/javascript" src="./assets/lib/jQuery-File-Upload-9.5.2/jquery.fileupload.js"></script>
    <script type="text/javascript" src="./assets/lib/jQuery-File-Upload-9.5.2/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="./assets/lib/jQuery-File-Upload-9.5.2/jquery.fileupload-ui.js"></script>

    <!--Virkailija layout script -->
    <script type="text/javascript" src="/virkailija-raamit/apply-raamit.js"></script>

    <script type="text/javascript" src="./assets/lib/tinymce/tinymce.min.js"></script>
    <script type="text/javascript" src="./assets/lib/tinymce/ui-angular-tinymce.js"></script>
    
    <%
        String emailData = request.getParameter("emailData");
        if (emailData != null) {
            // Sanitize data:
            emailData = Jsoup.clean(emailData, Whitelist.relaxed());
            emailData = StringEscapeUtils.unescapeHtml(emailData);
            //System.out.println(emailData);
        } else {
            // no emaildata found.
            emailData = "";
        }
    %>
    <!-- Bind the email data to window. -->
    <script type="text/javascript"> window.emailData = <%= emailData %>;</script>

    <!-- Initialize modules -->
    <script type="text/javascript" src="./email/init.js"></script>
    <script type="text/javascript" src="./report/init.js"></script>
    <script type="text/javascript" src="./system/init.js"></script>

    <!-- Filters-->
    <script type="text/javascript" src="./report/filters/listpaging.js"></script>
    <script type="text/javascript" src="./system/filters/localization.js"></script>
    <script type="text/javascript" src="./system/filters/html2text.js"></script>
    <script type="text/javascript" src="./system/filters/bytes2size.js"></script>

    <!-- Services -->
    <script type="text/javascript" src="./email/services/email.js"></script>
    <script type="text/javascript" src="./email/services/upload.js"></script>
    <script type="text/javascript" src="./email/services/draft.js"></script>
    <script type="text/javascript" src="./email/services/errorDialog.js"></script>
    <script type="text/javascript" src="./report/services/errorDialog.js"></script>
    <script type="text/javascript" src="./report/services/loading.js"></script>
    <script type="text/javascript" src="./report/services/reportedMessage.js"></script>
    <script type="text/javascript" src="./report/services/sharedVariables.js"></script>
    
    <!-- Directives -->
    <script type="text/javascript" src="./email/directives/upload.js"></script>
    <script type="text/javascript" src="./report/directives/reportedMessageStatus.js"></script>
    <script type="text/javascript" src="./report/directives/limitedParagraph.js"></script>

    <!-- Controllers -->
    <script type="text/javascript" src="./email/controllers/tab.js"></script>
    <script type="text/javascript" src="./email/controllers/draft.js"></script>
    <script type="text/javascript" src="./email/controllers/email.js"></script>
    <script type="text/javascript" src="./email/controllers/emailCancel.js"></script>
    <script type="text/javascript" src="./email/controllers/errorDialog.js"></script>

    <script type="text/javascript" src="./report/controllers/reportedMessageList.js"></script>
    <script type="text/javascript" src="./report/controllers/reportedMessageView.js"></script>
    <script type="text/javascript" src="./report/controllers/errorDialog.js"></script>

    <!-- Routes -->
    <script type="text/javascript" src="./system/routes.js"></script>

</body>