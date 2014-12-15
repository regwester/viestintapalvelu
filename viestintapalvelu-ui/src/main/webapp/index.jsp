<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <link rel="stylesheet" type="text/css" href="lib/bootstrap-css/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="lib/select2/select2.css"/>
    <link rel="stylesheet" href="lib/select2/select2-bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="css/all.css"/>
</head>

<body>
    <div id="application-wrapper">
        <div id="angularbox" ui-view></div>
    </div>

    <!-- libs -->
    <script type="text/javascript" src="lib/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="lib/jquery-i18n-properties/jquery.i18n.properties-min.js"></script>
    <script type="text/javascript" src="lib/jquery-file-upload/js/vendor/jquery.ui.widget.js"></script>

    <script type="text/javascript" src="lib/angular/angular.js"></script>
    <script type="text/javascript" src="lib/angular-resource/angular-resource.js"></script>
    <script type="text/javascript" src="lib/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>
    <script type="text/javascript" src="lib/ui-router/release/angular-ui-router.js"></script>

    <script src="lib/select2/select2.min.js"></script>
    <script src="lib/select2/select2_locale_fi.js"></script>


    <script type="text/javascript" src="lib/jquery-file-upload/js/jquery.fileupload.js"></script>
    <script type="text/javascript" src="lib/jquery-file-upload/js/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="lib/jquery-file-upload/js/jquery.fileupload-ui.js"></script>

    <!-- HTML5 saveAs() polyfill, IE 10+, Firefox, Chrome, Opera, Safari supported -->
    <script type="text/javascript" src="lib/file-saver/FileSaver.min.js"></script>
    <!-- HTML5 Blob polyfill -->
    <script type="text/javascript" src="lib/blob/Blob.js"></script>

    <!-- Virkailija layout script -->
    <script type="text/javascript" src="/virkailija-raamit/apply-raamit.js"></script>

    <script type="text/javascript" src="lib/tinymce/tinymce.min.js"></script>
    <script type="text/javascript" src="lib/angular-ui-tinymce/src/tinymce.js"></script>
    
    <%
        request.setCharacterEncoding("UTF-8");
        String emailData = request.getParameter("emailData");
        if (emailData != null) {
            // Sanitize data:
            emailData = Jsoup.clean(emailData, Whitelist.relaxed());
            emailData = StringEscapeUtils.unescapeHtml(emailData);
        } else {
            // no emaildata found.
            emailData = "";
        }
    %>
    <!-- Bind the email data to window. -->
    <script type="text/javascript"> window.emailData = <%= emailData %>;</script>
    <script type="text/javascript" src="js/all.min.js"></script>

</body>