package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;

import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentContainer;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;

@Service
public class EmailAVChecker {

    private static final String CMD_PING = "PING\0";
    private static final String CMD_INSTREAM = "zINSTREAM\0";
    private static final String RESULT_CLEAN = "stream: OK";
    private static final String RESULT_PONG = "PONG\n";

    @Value("${ryhmasahkoposti.antivirus.host}")
    private String hostname;

    @Value("${ryhmasahkoposti.antivirus.port}")
    private String port;

    @Value("${ryhmasahkoposti.antivirus.timeout}")
    private String timeout;

    private static final int BUFFER_SIZE = 1024;

    private static final Logger log = Logger.getLogger(fi.vm.sade.ryhmasahkoposti.service.impl.EmailAVChecker.class.getName());

    /**
     * Check if clamAV service is alive
     * 
     * @return true if ping gets pong
     * @throws IOException
     */
    public boolean checkAlive() throws IOException {
        return RESULT_PONG.equalsIgnoreCase(getAVResponse(CMD_PING, null));
    }

    /**
     * Checks emailMessageDTO for Viruses
     * 
     * @param sender
     * @param message
     * @return
     * @throws Exception
     * @throws IOException
     * @throws MessagingException
     */
    public void checkMessage(EmailSender sender, EmailMessageDTO message, Optional<? extends AttachmentContainer> additionalAttachments) throws Exception {
        if (!checkAlive()) {
            log.warning("Antivirus service not alive at: " + hostname + " port " + port);
            throw new Exception("Cannot check viruses ClamAV service not available");
        }
        boolean isInfected = isInfected(sender, message, additionalAttachments);
        message.setInfected(isInfected);
        message.setVirusChecked(true);
    }

    public void checkRecipientMessage(EmailSender sender, EmailMessageDTO message, Optional<? extends AttachmentContainer> additionalAttachments) throws Exception {
        if (!checkAlive()) {
            log.warning("Antivirus service not alive at: " + hostname + " port " + port);
            throw new Exception("Cannot check viruses ClamAV service not available");
        }
        boolean isInfected = isInfected(sender, message, additionalAttachments);
        message.setInfected(isInfected);
        message.setVirusChecked(true);
    }

    /*
     * Checks emailMessageDTO for Viruses
     * 
     * @param sender
     * 
     * @param message
     * 
     * @return
     * 
     * @throws IOException
     * 
     * @throws MessagingException
     */
    private boolean isInfected(EmailSender sender, EmailMessage message, Optional<? extends AttachmentContainer> additionalAttachments) throws IOException,
            MessagingException {

        MimeMessage msg = sender.createMail(message, "noone@localhost.local", additionalAttachments);

        // check attachments separately..
        for (EmailAttachment attachment : message.getAttachments()) {
            ByteArrayInputStream attachmentStream = new ByteArrayInputStream(attachment.getData());
            try {
                if (isInfected(new ByteArrayInputStream(attachment.getData()))) {
                    return true;
                }
            } finally {
                if (attachmentStream != null) {
                    attachmentStream.close();
                }
            }
        }
        return isInfected(msg);
    }

    /*
     * Checks MimeMessage for Viruses Does not find viruses in attachments.
     * Check attachments separately.
     * 
     * @param sender
     * 
     * @param message
     * 
     * @return
     * 
     * @throws IOException
     * 
     * @throws MessagingException
     */

    private boolean isInfected(MimeMessage message) throws IOException, MessagingException {

        DataHandler dh = message.getDataHandler();
        InputStream din = dh.getInputStream();
        try {
            return isInfected(din);
        } finally {
            if (din != null) {
                din.close();
            }
        }
    }

    /*
     * Checks InputStream content for viruses
     * 
     * @param data
     * 
     * @return
     * 
     * @throws IOException
     * 
     * @throws MessagingException
     */
    private boolean isInfected(InputStream data) throws IOException, MessagingException {

        String response = getAVResponse(CMD_INSTREAM, data);
        boolean isInfected = !(response != null && response.trim().equals(RESULT_CLEAN));
        if (isInfected) {
            log.info("Virus Checker: Got infected response: " + response);
        }

        return isInfected;
    }

    private String getAVResponse(String command, InputStream data) throws IOException {
        Socket socket = new Socket();
        InputStream responseStream = null;
        DataOutputStream dos = null;
        StringBuilder response = new StringBuilder();

        int timeoutInt = 60;
        try {
            timeoutInt = Integer.parseInt(timeout);
        } catch (NumberFormatException nfe) {
            // never mind use default
        }

        try {
            socket.connect(getAddress(), timeoutInt);
            socket.setSoTimeout(timeoutInt);

            dos = new DataOutputStream(socket.getOutputStream());
            dos.write(command.getBytes());
            dos.flush();

            // do we have something to send ??
            if (data != null) {
                int read = BUFFER_SIZE;
                byte[] buf = new byte[BUFFER_SIZE];
                while (read == BUFFER_SIZE) {
                    read = data.read(buf);
                    if (read == -1) {
                        break;
                    }
                    dos.writeInt(read);
                    dos.write(buf, 0, read);
                }

                dos.writeInt(0);
                dos.flush();
            }

            int read = -1;
            responseStream = socket.getInputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            while (true) {
                read = responseStream.read(buffer);
                if (read == -1) {
                    break;
                }
                response.append(new String(buffer, 0, read));
            }

        } finally {
            socket.close();
            if (dos != null) {
                dos.close();
            }
            if (responseStream != null) {
                responseStream.close();
            }
        }
        return response.toString();
    }

    private InetSocketAddress getAddress() {

        int portInt = 3310;
        try {
            portInt = Integer.parseInt(port);
        } catch (NumberFormatException nfe) {
            // never mind use default
        }
        return new InetSocketAddress(hostname, portInt);
    }
}
