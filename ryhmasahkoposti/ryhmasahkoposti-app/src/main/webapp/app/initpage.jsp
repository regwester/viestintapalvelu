<!DOCTYPE form PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<body>
	<form action="index.jsp" method="post">
		<textarea rows="30" cols="200" name="emailData">
			{
					recipient: [<%for (int i = 0; i < 2; i++) {%>
						{oid: '1234567890ABCD<%=i%>',
						oidType: 'henkilo',
						email: 'ville.vastaanottaja@gmail.com',
						languageCode: 'FI'},										
					<%}%>
						{oid: '1234567890ABCD',
						oidType: 'henkilo',
						email: 'ville.vastaanottaja@gmail.com',
						languageCode: 'FI'},
										
						{oid: 'ABCD0987654321',
						oidType: 'henkilo',
						email: 'torspo.uolevi@gmail.com',
						languageCode: 'FI'}	
					],		
					email: {callingProcess: 'Osoitetietojarjestelma',
						from: 'oph_tiedotus@oph.fi',
						replyTo: 'Mikko.Mallikas@oph.fi',
						subject: 'Testi viesti',
						body: 'Testi bodya ja sporttia.'
					}
			}
		</textarea>
		<input type="submit" />
	</form>
	</body>
</html>
