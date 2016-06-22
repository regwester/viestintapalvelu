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
package fi.vm.sade.viestintapalvelu.iposti;

import java.io.ByteArrayInputStream;

import com.jcraft.jsch.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IPostiUpload {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IPostiUpload.class);

    @Value("${viestintapalvelu.iposti.username}")
    private String username;
    @Value("${viestintapalvelu.iposti.password}")
    private String password;
    @Value("${viestintapalvelu.iposti.host}")
    private String hostname;
    @Value("${viestintapalvelu.iposti.port}")
    private String port;
    @Value("${viestintapalvelu.iposti.directory}")
    private String directory;

    public boolean upload(byte[] zipBytes, String filename) throws Exception {
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            LOGGER.info("Initializing session for SFTP connection for {} file upload...", filename);
            session = jsch.getSession(username, hostname, Integer.parseInt(port));
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            LOGGER.info("Connecting to SFTP server for {} file upload...", filename);
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;

            LOGGER.info("Starting to upload file {} to SFTP server...", filename);
            channelSftp.cd(directory);
            channelSftp.put(new ByteArrayInputStream(zipBytes), filename + ".temppi");
            channelSftp.rename(filename + ".temppi", filename + ".ok");
            LOGGER.info("File upload {} is ready.", filename);

        } catch(Exception e) {
            LOGGER.error("Got error when uploading file " + filename + " to SFTP server!", e);
            throw e;
        } finally {
            if(null != session) {
                session.disconnect();
            }
            if(null != channelSftp) {
                channelSftp.exit();
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "IPostiUpload [username=" + username + ", hostname=" + hostname + ", port=" + port + ", directory=" + directory + "]";
    }

}
