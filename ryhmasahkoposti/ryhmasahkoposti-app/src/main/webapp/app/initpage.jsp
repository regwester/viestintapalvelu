<!DOCTYPE form PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<body>
	<form action="index.jsp" method="post">
		<textarea rows="30" cols="200" name="emailData">
			{
					recipient: [<%for (int i = 0; i < 2; i++) {%>
						{oid: '1.2.246.562.24.63926748289',
						oidType: 'virkailija',
						email: 'ville.vastaanottaja@example.com',
						languageCode: 'FI'},										
					<%}%>
						{oid: '1.2.246.562.24.21860986107',
						oidType: 'virkailija',
						email: 'ville.vastaanottaja@example.com',
						languageCode: 'FI'},
										
						{oid: '1.2.246.562.24.98095750306',
						oidType: 'virkailija',
						email: 'torspo.uolevi@example.com',
						languageCode: 'FI'}	
					],		
					email: {callingProcess: 'Osoitetietojarjestelma',
						from: 'oph_tiedotus@oph.fi',
						organizationOid: '1.2.246.562.10.00000000001',
						replyTo: 'Mikko.Mallikas@oph.fi',
						subject: 'Testi viesti',
						body: 'Testi bodya ja sporttia. ההההההצצצצצצצוווווו'
					}
			}
		</textarea>
		<input type="submit" />
	</form>
	</body>
</html>
