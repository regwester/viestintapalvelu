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
    <link rel="stylesheet" type="text/css" href="lib/bootstrap/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="lib/tree-grid/src/treeGrid.css">
    <link rel="stylesheet" type="text/css" href="css/all.css"/>
</head>

<body>
    <div id="application-wrapper">
        <div id="angularbox" ui-view></div>
    </div>

    <!-- libs -->
    <script type="text/javascript" src="lib/jquery/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="lib/jquery/jquery.i18n.properties-min-1.0.9.js"></script>
    <script type="text/javascript" src="lib/jQuery-File-Upload-9.5.2/vendor/jquery.ui.widget.js"></script>

    <script type="text/javascript" src="lib/angular/angular.js"></script>
    <script type="text/javascript" src="lib/angular/angular-resource.js"></script>
    <script type="text/javascript" src="lib/angular/angular-animate.js"></script>
    <script type="text/javascript" src="lib/ui-bootstrap/ui-bootstrap-tpls-0.11.0.min.js"></script>
    <script type="text/javascript" src="lib/ui-router/angular-ui-router.min.js"></script>
     
    <script type="text/javascript" src="lib/jQuery-File-Upload-9.5.2/jquery.fileupload.js"></script>
    <script type="text/javascript" src="lib/jQuery-File-Upload-9.5.2/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="lib/jQuery-File-Upload-9.5.2/jquery.fileupload-ui.js"></script>
    <script type="text/javascript" src="lib/tree-grid/src/tree-grid-directive.js"></script>

    <!-- HTML5 saveAs() polyfill, IE 10+, Firefox, Chrome, Opera, Safari supported -->
    <script type="text/javascript" src="lib/file/FileSaver.js"></script>
    <!-- HTML5 Blob polyfill -->
    <script type="text/javascript" src="lib/file/Blob.js"></script>

    <!-- Virkailija layout script -->
    <script type="text/javascript" src="/virkailija-raamit/apply-raamit.js"></script>

    <script type="text/javascript" src="lib/tinymce/tinymce.min.js"></script>
    <script type="text/javascript" src="lib/tinymce/ui-angular-tinymce.js"></script>
    
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