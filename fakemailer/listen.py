#!/usr/bin/env python
# Original script written by Stuart Colville: http://muffinresearch.co.uk/archives/2010/10/15/fake-smtp-server-with-python/
"""A noddy fake smtp server."""

import os
import sys

pid = str(os.getpid())
pidfile = "./listen.pid"

#if os.path.isfile(pidfile):
#    print "%s already exists, exiting" % pidfile
#    sys.exit()
#else:
#    file(pidfile, 'w').write(pid)

import smtpd
import asyncore
import time
from datetime import datetime
import random

outputFolder = "./incoming/"
bounceFolder = "./bounces/"
bounceDomain = "example.com"
port = 1025
host = "localhost"

class FakeSMTPServer(smtpd.SMTPServer):
    """A Fake smtp server"""
    def __init__(*args, **kwargs):
        print "Running fake smtp server on port "+host+":"+str(port)
        smtpd.SMTPServer.__init__(*args, **kwargs)

    def write_mail(self, content):
        mail = open(outputFolder+str(time.strftime("%Y-%m-%d-%H.%M.%S", time.localtime()))+"_"+str(random.randint(10000,99000))+".eml", "w")
        mail.write(content)
        mail.close

    def check_and_mark_bounced(self, headers, receivers):
        messageId = filter(lambda x: x.startswith('X-Message-ID:'), headers)
        if len(messageId) == 0:
            print "No X-Message-ID found!"
            return
        parsedMessageId = messageId[0].replace("X-Message-ID: ", "").split(".")[0]
        is_bounce = len(filter(lambda x: x.find(bounceDomain) > -1, receivers)) > 0
        if is_bounce:
            print "Writing bounce for " + parsedMessageId
            bounce = open(bounceFolder+parsedMessageId + ".csv", "w")
            bounce.write("".join(receivers) + ";" + str(datetime.utcnow()) + ";" + parsedMessageId)
            bounce.close

    def process_message(*args, **kwargs):
        self = args[0]
        mailFrom = args[2]
        receivers = args[3]
        content = args[4]
        headers = content.split("\n")
        print "New mail from " + mailFrom
        self.write_mail(content)
        self.check_and_mark_bounced(headers, receivers)
        pass

if __name__ == "__main__":
    smtp_server = FakeSMTPServer((host, port), None)
    try:
        asyncore.loop()
    except KeyboardInterrupt:
        smtp_server.close()
