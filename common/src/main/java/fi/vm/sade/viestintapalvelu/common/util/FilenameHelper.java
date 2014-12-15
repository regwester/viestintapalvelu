/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.viestintapalvelu.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.internal.util.StringHelper;

/**
 * User: ratamaa
 * Date: 15.12.2014
 * Time: 12:50
 */
public class FilenameHelper {
    private FilenameHelper() {
    }

    public static String withoutExtension(String filename) {
        if (filename == null) {
            return null;
        }
        String prts[] = filename.split("\\.");
        if (prts.length > 1) {
            List<String> prtsList = new ArrayList<>(Arrays.asList(prts));
            prtsList.remove(prtsList.size()-1);
            return StringHelper.join(".", prtsList.iterator());
        }
        return filename;
    }

}
