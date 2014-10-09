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

package fi.vm.sade.viestintapalvelu.letter.processing;

import com.google.common.base.Optional;

/**
 * MUST implement {@link Object#equals(Object)} and {@link Object#hashCode()}
 *
 * @param <T> the type of Processable this Job is cabable of handling
 */
public interface Job<T extends Processable> {
    /**
     * Called when processing starts
     *
     * @param  description of the job
     */
    void start(JobDescription<T> description);

    /**
     * @param e exception occurred
     * @param description of the job at hand
     */
    void handleJobStartedFailure(Exception e, JobDescription<T> description);

    /**
     * @param processable to process
     * @throws Exception
     */
    void process(T processable) throws Exception;

    /**
     * @param e exception occurred
     * @param processable the processable with which the exception occured
     */
    void handleFailure(Exception e, T processable);

    /**
     * Called when processing has successfully ended
     *
     * @param  description of the job
     * @return the possible next job to do
     */
    Optional<? extends JobDescription<?>> jobFinished(JobDescription<T> description) throws Exception;

    /**
     * @param e exception occurred
     * @param description of the job at hand
     */
    void handleJobFinnishedFailure(Exception e, JobDescription<T> description);
}
