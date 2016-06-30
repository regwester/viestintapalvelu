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
package fi.vm.sade.viestintapalvelu.attachment.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 24.9.2014
 * Time: 15:47
 */
public class AttachmentUri {
    private static final String SCHEME_PREFIX = "viestinta://";

    public enum TargetType {
        LetterReceiverLetterAttachment("letterReceiverLetterAttachment", Long.class);

        private final String prefix;
        private final Class<?>[] segments;

        TargetType(String prefix) {
            this(prefix, new Class<?>[0]);
        }
        TargetType(String prefix, Class<?>... segments) {
            this.prefix = prefix;
            this.segments = segments;
        }

        public String getPrefix() {
            return prefix;
        }

        public int getNumberSegments() {
            return segments.length;
        }

        public Class<?>[] getSegments() {
            return segments;
        }
    }

    public static AttachmentUri getLetterReceiverLetterAttachment(Long attachmentId) {
        return new AttachmentUri(SCHEME_PREFIX + TargetType.LetterReceiverLetterAttachment.getPrefix() + "/" + attachmentId);
    }

    public static List<String> uriStringsOfList(List<AttachmentUri> attachmentUris) {
        List<String> uris = new ArrayList<>();
        for (AttachmentUri uri : attachmentUris) {
            uris.add(uri.getUri());
        }
        return uris;
    }

    private String uri;
    private String content;
    private String payload;
    private TargetType type;
    private String[] segments;
    private Object[] parameters;

    public AttachmentUri(String uri) {
        this.uri = uri;
        parse();
    }

    private void parse() {
        if (!this.uri.startsWith(SCHEME_PREFIX)) {
            throw new IllegalArgumentException("Illegal viestinta-URI: " + this.uri);
        }
        this.content = this.uri.substring(SCHEME_PREFIX.length());
        if (this.content.length() < 1) {
            throw new IllegalArgumentException("Empty URI: " + this.uri);
        }
        for (TargetType type : TargetType.values()) {
            if (this.content.startsWith(type.getPrefix())) {
                String payload = this.content.substring(type.getPrefix().length());
                int segments = 0;
                String[] prts = new String[0];
                if (payload.length() > 1) {
                    if (payload.charAt(0) != '/') {
                        continue;
                    }
                    prts = payload.substring(1).split("/");
                    segments = prts.length;
                }
                if (type.getNumberSegments() == segments) {
                    this.type = type;
                    this.payload = payload;
                    this.segments = prts;
                    parseSegments();
                }
            }
        }
        if (this.type == null) {
            throw new IllegalArgumentException("Invalid viestinta-URI " + this.uri);
        }
    }

    private void parseSegments() {
        int i = 0;
        this.parameters = new Object[this.type.getNumberSegments()];
        for (Class<?> type : this.type.getSegments()) {
            if (Long.class.equals(type)) {
                try {
                    this.parameters[i] = Long.parseLong(this.segments[i]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Error parsing "+this.type
                            +" URI's "+this.uri+" Long-segment " + this.segments[i], e);
                }
            } else if (String.class.equals(type)) {
                this.parameters[i] = this.segments[i];
            } else {
                throw new IllegalStateException(type + " parameter parsing not implemented!");
            }
            ++i;
        }
    }

    public String getUri() {
        return uri;
    }

    public String getContent() {
        return content;
    }

    public String getPayload() {
        return payload;
    }

    public TargetType getType() {
        return type;
    }

    public String[] getSegments() {
        return segments;
    }

    public Long getLongParameter(int i) {
        int segments = this.type.getNumberSegments();
        if (i < 0 || i >= segments) {
            throw new IllegalArgumentException("Illegal URI segment " + i + " for " + this.type + " viesinta-RUI.");
        }
        if (!Long.class.equals(this.type.getSegments()[i])) {
            throw new IllegalArgumentException("Segment " + i + " in " + this.type + " viesinta-URI is not Long ("
                    +this.type.getSegments()[i]+")");
        }
        return (Long)this.parameters[i];
    }

    public Object[] getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return this.uri;
    }
}
