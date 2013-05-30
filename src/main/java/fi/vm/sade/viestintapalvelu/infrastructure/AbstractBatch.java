package fi.vm.sade.viestintapalvelu.infrastructure;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBatch<T> {
	private List<T> contents;

	public AbstractBatch() {
	}

	public AbstractBatch(List<T> contents) {
		this.contents = contents;
	}

	public List<T> getContents() {
		return contents;
	}

	public List<AbstractBatch<T>> split(int limit) {
		List<AbstractBatch<T>> batches = new ArrayList<AbstractBatch<T>>();
		split(contents, batches, limit);
		return batches;
	}

	protected abstract AbstractBatch<T> createSubBatch(
			List<T> contentsOfSubBatch);

	private void split(List<T> remaining, List<AbstractBatch<T>> batches,
			int limit) {
		if (limit >= remaining.size()) {
			batches.add(createSubBatch(remaining));
		} else {
			batches.add(createSubBatch(remaining.subList(0, limit)));
			split(remaining.subList(limit, remaining.size()), batches, limit);
		}
	}
}
