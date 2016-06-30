brew install sendemail 

sendemail -f noreply@oph.fi -u "Testiviesti otsikko" -m "Tässä on testiviesti" -o message-header="X-Message-ID: d8fb9ca57912ad3b97902528e9ea5f89.posti@hard.ware.fi"  -s localhost:1025 -t "jussi.vesala@example.com"