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
package fi.vm.sade.ryhmasahkoposti.dao;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * User: ratamaa
 * Date: 15.9.2014
 * Time: 11:25
 */
public final class DaoHelper {
    private DaoHelper() {
    }

    public static<T> T firstOrNull(TypedQuery<T> q) {
        List<T> list = q.setMaxResults(1).getResultList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public static<T extends Number> int count(TypedQuery<T> q) {
        List<T> list = q.setMaxResults(1).getResultList();
        if (list.isEmpty()) {
            return 0;
        }
        Number val = list.get(0);
        if (val == null) {
            return 0;
        }
        return val.intValue();
    }
}
