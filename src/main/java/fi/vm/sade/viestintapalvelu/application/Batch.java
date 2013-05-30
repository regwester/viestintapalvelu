package fi.vm.sade.viestintapalvelu.application;

import java.util.List;

public interface Batch<T> {
	List<T> getContents();
}
