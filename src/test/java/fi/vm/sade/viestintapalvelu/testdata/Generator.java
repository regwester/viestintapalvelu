package fi.vm.sade.viestintapalvelu.testdata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public abstract class Generator<T> {

	private Map<String, List<String>> datasets = new HashMap<String, List<String>>();
	private Random random = new Random();

	public Generator() throws JsonParseException, JsonMappingException,
			IOException {
		addDataset("firstname", "/generator/firstnames.json");
		addDataset("lastname", "/generator/lastnames.json");
		addDataset("street", "/generator/streets.json");
		addDataset("houseNumber", Range.asList(1, 200));
		addDataset("postOffice", "/generator/postoffices.json");
		addDataset("country", "/generator/countries.json");
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
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<String> values = mapper.readValue(
				getClass().getResourceAsStream(datasetURL), List.class);
		datasets.put(datasetKey, values);
	}

	public void addDataset(String datasetKey, List<String> values) {
		datasets.put(datasetKey, values);
	}

	protected class TestData {
		public String random(String datasetKey) {
			List<String> dataset = datasets.get(datasetKey);
			return dataset != null ? dataset
					.get(random.nextInt(dataset.size())) : "";
		}
	}

	public static class Range {
		public static List<String> asList(int from, int to) {
			List<String> values = new ArrayList<String>();
			for (int i = from; i <= to; i++) {
				values.add("" + i);
			}
			return values;
		}
	}
}
