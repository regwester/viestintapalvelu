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
	
	public Generator() {
		addDataset("firstname", "/generator/firstnames.json");
		addDataset("lastname", "/generator/lastnames.json");
		addDataset("streets", "/generator/streets.json");
		addDataset("houseNumber", Range.asList(1, 200));
		addDataset("postOffice", "/generator/postoffices.json");
		addDataset("country", "/generator/countries.json");
	}
	
	public List<T> enumerate(int count) {
		List<T> labels = new ArrayList<T>();
		for (int i = 0; i < count; i++) {
			labels.add(create());
		}
		return labels;
	}
	
	protected abstract T create();
	
	protected String random(String datasetKey) {
		List<String> dataset = datasets.get(datasetKey);
		return dataset != null ? dataset.get(random.nextInt(dataset.size())) : "";
	}

	@SuppressWarnings("unchecked")
	public void addDataset(String datasetKey, String datasetURL) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<String> values = mapper.readValue(getClass().getResourceAsStream(datasetURL), List.class);
			datasets.put(datasetKey, values);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addDataset(String datasetKey, List<String> values) {
		datasets.put(datasetKey, values);
	}
	
	public static class Range {
		public static List<String> asList(int from, int to) {
			List<String> values = new ArrayList<String>();
			for (int i = from; i <= to; i++) {
				values.add(""+i);
			}
			return values;
		}
	}
}
