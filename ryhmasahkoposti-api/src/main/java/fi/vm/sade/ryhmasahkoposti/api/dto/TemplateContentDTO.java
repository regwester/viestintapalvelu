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
package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.Date;

import fi.vm.sade.generic.common.BaseDTO;

/**
 * Template content transfer object class
 *
 * @author ovmol1
 */
public class TemplateContentDTO extends BaseDTO implements Comparable<TemplateContentDTO> {

    private static final long serialVersionUID = 1664909682191763117L;

    /**
     * Template content name
     */
    private String name;

    /**
     * Order
     */
    private int order;

    /**
     * Content
     */
    private String content;

    /**
     * Content type
     */
    private String contentType;

    /**
     * Timestamp
     */
    private Date timestamp;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the order
     */
    public int getOrder() {
        return order;
    }

    /**
     * @param order
     *            the order to set
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType
     *            the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "TemplateContentDTO [name=" + name + ", order=" + order + ", content=" + content + ", contentType=" + contentType + ", timestamp=" + timestamp
                + ", getId()=" + getId() + "]";
    }

    /**
     * Compare
     */
    @Override
    public int compareTo(TemplateContentDTO o) {
        Integer ord = new Integer(order);
        return ord.compareTo(o.order);
    }
}
