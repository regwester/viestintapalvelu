package fi.vm.sade.viestintapalvelu.application;

import java.util.List;

public interface SplittableBatch<T> extends Batch<T> {
	public List<Batch<T>> split(int limit, String dummy);
}
