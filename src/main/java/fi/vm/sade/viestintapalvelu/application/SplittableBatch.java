package fi.vm.sade.viestintapalvelu.application;

import java.util.List;

public interface SplittableBatch<T> extends Batch<T> {
	List<Batch<T>> split(int limit);
}
