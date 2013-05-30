package fi.vm.sade.viestintapalvelu.infrastructure;

import java.util.ArrayList;
import java.util.List;

public abstract class Batch<T> {
	private List<T> contents;

	public Batch() {
	}

	public Batch(List<T> contents) {
		this.contents = contents;
	}

	public List<T> getContents() {
		return contents;
	}

	public List<Batch<T>> split(int limit) {
		List<Batch<T>> batches = new ArrayList<Batch<T>>();
		split(contents, batches, limit);
		return batches;
	}

	protected abstract Batch<T> createSubBatch(List<T> contentsOfSubBatch);

	private void split(List<T> remaining, List<Batch<T>> batches, int limit) {
		if (limit >= remaining.size()) {
			batches.add(createSubBatch(remaining));
		} else {
			batches.add(createSubBatch(remaining.subList(0, limit)));
			split(remaining.subList(limit, remaining.size()), batches, limit);
		}
	}
}
