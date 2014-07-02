<%@ page import="org.jsoup.*" %>
<%@ page import="org.jsoup.safety.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>

<%@ page import="org.jsoup.nodes.Entities.EscapeMode" %>
<!DOCTYPE html>
<html id="ng-app" ng-app="viestintapalvelu">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>OPH - Viestint&aumlpalvelu</title>
</head>

<body>
	<div id="application-wrapper">
	    <div id="angularbox" ng-view=""></div>
	</div>

    <!-- libs -->
    <script type="text/javascript" src="../assets/lib/jquery/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="../assets/lib/jquery/jquery.i18n.properties-min-1.0.9.js"></script> 
    <script type="text/javascript" src="../assets/lib/jQuery-File-Upload-9.5.2/vendor/jquery.ui.widget.js"></script>

    <script type="text/javascript" src="../assets/lib/angular/angular.js"></script> 
    <script type="text/javascript" src="../assets/lib/angular/angular-resource.js"></script>
    <script type="text/javascript" src="../assets/lib/angular/angular-route.js"></script>
    <script type="text/javascript" src="../assets/lib/angular/angular-animate.js"></script>
    <script type="text/javascript" src="../assets/lib/ui-bootstrap/ui-bootstrap-tpls-0.8.0.min.js"></script>
     
    <script type="text/javascript" src="../assets/lib/jQuery-File-Upload-9.5.2/jquery.fileupload.js"></script>
    <script type="text/javascript" src="../assets/lib/jQuery-File-Upload-9.5.2/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="../assets/lib/jQuery-File-Upload-9.5.2/jquery.fileupload-ui.js"></script>

    <!--Virkailija layout script -->
    <script type="text/javascript" src="/virkailija-raamit/apply-raamit.js"></script>

	<script type="text/javascript" src="../assets/lib/tinymce/tinymce.min.js"></script>
	<script type="text/javascript" src="../assets/lib/tinymce/ui-angular-tinymce.js"></script>
	
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
	<script type="text/javascript">	window.emailData = <%= emailData %>;</script>
	
	<!-- Initialize modules -->
	<script src="./js/init.js"></script>
	
    <!-- Filters-->
    <script src="./js/filters/emailLocalization.js"></script>

	<!-- Services -->
	<script src="./js/services/emailService.js"></script>
	<script src="./js/services/errorDialog.js"></script>
	
	<!-- Directives -->
    <script src="./js/directives/upload.js"></script>
    
	<!-- Controllers -->
	<script src="./js/controllers/email.js"></script>
    <script src="./js/controllers/emailCancel.js"></script>
    <script src="./js/controllers/emailSendStatus.js"></script>
    <script src="./js/controllers/emailResponse.js"></script>
    <script src="./js/controllers/errorDialog.js"></script>
	
	<!-- Routes -->
	<script src="./js/routes/email.js"></script>
	
	
    <!-- css -->
    <link rel="stylesheet" href="../assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="../assets/css/other.css"/>
    <link rel="stylesheet" href="../assets/css/virkailija.css"/>
    <link rel="stylesheet" href="../assets/css/dialogs.css"/>    
</body>