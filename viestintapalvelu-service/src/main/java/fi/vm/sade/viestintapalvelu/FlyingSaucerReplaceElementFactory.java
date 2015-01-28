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
 **/package fi.vm.sade.viestintapalvelu;

import com.lowagie.text.Image;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

/**
 * Replaced element in order to replace elements like
 * <tt>&lt;div class="media" data-src="image.png" /></tt> with the real media
 * content.
 */
public class FlyingSaucerReplaceElementFactory implements
        ReplacedElementFactory {
    private final ReplacedElementFactory superFactory;

    public FlyingSaucerReplaceElementFactory(ReplacedElementFactory superFactory) {
        this.superFactory = superFactory;
    }

    public ReplacedElement createReplacedElement(LayoutContext layoutContext,
                                                 BlockBox blockBox, UserAgentCallback userAgentCallback,
                                                 int cssWidth, int cssHeight) {
        Element element = blockBox.getElement();
        if (element == null) {
            return null;
        }
        String nodeName = element.getNodeName();
        String className = element.getAttribute("class");
        // Replace any <div class="media" data-src="image.png" /> with the
        // binary data of `image.png` into the PDF.
        if ("div".equals(nodeName) && "media".equals(className)) {
            if (!element.hasAttribute("data-src")) {
                throw new RuntimeException(
                        "An element with class `media` is missing a `data-src` attribute indicating the media file.");
            }
            try {
                final byte[] bytes = userAgentCallback
                        .getBinaryResource(element.getAttribute("data-src"));
                final Image image = Image.getInstance(bytes);
                final FSImage fsImage = new ITextFSImage(image);
                if ((cssWidth != -1) || (cssHeight != -1)) {
                    fsImage.scale(cssWidth, cssHeight);
                }
                return new ITextImageElement(fsImage);
            } catch (Exception e) {
                throw new RuntimeException(
                        "There was a problem trying to read a template embedded graphic.",
                        e);
            }
        }
        return this.superFactory.createReplacedElement(layoutContext, blockBox,
                userAgentCallback, cssWidth, cssHeight);
    }

    public void reset() {
        this.superFactory.reset();
    }

    public void remove(Element e) {
        this.superFactory.remove(e);
    }

    public void setFormSubmissionListener(FormSubmissionListener listener) {
        this.superFactory.setFormSubmissionListener(listener);
    }
}
