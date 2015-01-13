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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@Service
public class IPostiUpload {

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
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, hostname, Integer.parseInt(port));
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;
            channelSftp.cd(directory);

            channelSftp.put(new ByteArrayInputStream(zipBytes), filename + ".temppi");
            channelSftp.rename(filename + ".temppi", filename + ".ok");
            channelSftp.exit();
            session.disconnect();
        } catch (Exception ex) {
            throw (ex);
        }
        return true;
    }

    @Override
    public String toString() {
        return "IPostiUpload [username=" + username + ", hostname=" + hostname + ", port=" + port + ", directory=" + directory + "]";
    }

}
