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
package fi.vm.sade.viestintapalvelu.testdata;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Generator<T> {

    private Map<String, List<Object>> datasets = new HashMap<String, List<Object>>();
    private Random random = new Random();

    public Generator() throws IOException {
        addDataset("firstname", "src/main/webapp/generator/firstnames.json");
        addDataset("lastname", "src/main/webapp/generator/lastnames.json");
        addDataset("street", "src/main/webapp/generator/streets.json");
        addDataset("houseNumber", Range.asList(1, 200));
        addDataset("postOffice", "src/main/webapp/generator/postoffices.json");
        addDataset("country", "src/main/webapp/generator/countries.json");
    }

    public List<T> generateObjects(int count) {
        List<T> labels = new ArrayList<T>();
        for (int i = 0; i < count; i++) {
            labels.add(createObject(new TestData()));
        }
        return labels;
    }

    protected abstract T createObject(TestData data);

    @SuppressWarnings("unchecked")
    public void addDataset(String datasetKey, String datasetURL)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Object> values = mapper
                .readValue(new File(datasetURL), List.class);
        datasets.put(datasetKey, values);
    }

    public void addDataset(String datasetKey, List<Object> values) {
        datasets.put(datasetKey, values);
    }

    private String toString(Object randomItem) {
        if (randomItem instanceof String) {
            return (String) randomItem;
        }
        if (randomItem instanceof String[]) {
            return ((String[]) randomItem)[0];
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    private String[] toStringArray(Object randomItem) {
        if (randomItem instanceof String) {
            return new String[]{(String) randomItem};
        }
        if (randomItem instanceof String[]) {
            return (String[]) randomItem;
        }
        if (randomItem instanceof List) {
            return ((List<String>) randomItem).toArray(new String[1]);
        }
        return new String[]{""};
    }

    protected class TestData {
        public String random(String datasetKey) {
            List<Object> dataset = datasets.get(datasetKey);
            if (dataset == null) {
                return "";
            }
            return Generator.this.toString(dataset.get(random.nextInt(dataset
                    .size())));
        }

        public String[] randomArray(String datasetKey) {
            List<Object> dataset = datasets.get(datasetKey);
            if (dataset == null) {
                return new String[]{""};
            }
            return Generator.this.toStringArray(dataset.get(random
                    .nextInt(dataset.size())));
        }
    }

    public static class Range {
        public static List<Object> asList(int from, int to) {
            List<Object> values = new ArrayList<Object>();
            for (int i = from; i <= to; i++) {
                values.add("" + i);
            }
            return values;
        }
    }
}
