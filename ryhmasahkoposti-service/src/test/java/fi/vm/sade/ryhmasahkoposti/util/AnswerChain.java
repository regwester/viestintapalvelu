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
package fi.vm.sade.ryhmasahkoposti.util;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * User: ratamaa Date: 16.9.2014 Time: 10:24
 */
public class AnswerChain<T> implements Answer<T> {
    private int callCount = 0;
    private int totalCallCount = 0;
    private Answer<T> delegate;
    private int times = -1;
    private AnswerChain<T> next;
    private AnswerChain<T> parent;

    protected static class ThrowAnswer<Type> implements Answer<Type> {
        private Throwable exception;

        public ThrowAnswer(Throwable exception) {
            this.exception = exception;
        }

        @Override
        public Type answer(InvocationOnMock invocation) throws Throwable {
            throw this.exception;
        }
    }

    protected static class ReturnAnswer<Type> implements Answer<Type> {
        private Type value;

        public ReturnAnswer(Type value) {
            this.value = value;
        }

        @Override
        public Type answer(InvocationOnMock invocation) throws Throwable {
            return this.value;
        }
    }

    protected static class DoNothingAnswer<Type> implements Answer<Type> {
        @Override
        public Type answer(InvocationOnMock invocation) throws Throwable {
            return null;
        }
    }

    public AnswerChain(Answer<T> answer) {
        this.delegate = answer;
    }

    public static <Type> AnswerChain<Type> atFirst(Answer<Type> answer) {
        return new AnswerChain<Type>(answer);
    }

    public static <Type> AnswerChain<Type> atFirstReturn(Type answer) {
        return atFirst(new ReturnAnswer<Type>(answer));
    }

    public static <Void> AnswerChain<Void> atFirstDoNothing() {
        return atFirst(new DoNothingAnswer<Void>());
    }

    public static <Type> AnswerChain<Type> atFirstThrow(Throwable exception) {
        return atFirst(new ThrowAnswer<Type>(exception));
    }

    public AnswerChain<T> times(int times) {
        this.times = times;
        return this;
    }

    public AnswerChain<T> then(Answer<T> answer) {
        AnswerChain<T> nextElement = new AnswerChain<T>(answer);
        nextElement.parent = this;
        this.next = nextElement;
        if (this.times < 0) {
            this.times = 1;
        }
        return nextElement;
    }

    public AnswerChain<T> thenReturn(T value) {
        return then(new ReturnAnswer<T>(value));
    }

    public AnswerChain<T> thenThrow(Throwable exception) {
        return then(new ThrowAnswer<T>(exception));
    }

    public AnswerChain<T> thenDoNothing() {
        return then(new DoNothingAnswer<T>());
    }

    public T doAnswer(InvocationOnMock invocation) throws Throwable {
        ++this.totalCallCount;
        if (this.times < 0) {
            ++this.callCount;
            return this.delegate.answer(invocation);
        } else if (this.times > 0) {
            ++this.callCount;
            --this.times;
            T ret = this.delegate.answer(invocation);
            return ret;
        } else if (this.next != null) {
            return this.next.doAnswer(invocation);
        }
        ++this.callCount;
        return null;
    }

    public int getCallCount() {
        return callCount;
    }

    public int getTotalCallCount() {
        if (this.parent != null) {
            return this.parent.getTotalCallCount();
        }
        return totalCallCount;
    }

    public AnswerChain<T> getParent() {
        return parent;
    }

    @Override
    public T answer(InvocationOnMock invocation) throws Throwable {
        if (this.parent != null) {
            return this.parent.answer(invocation);
        }
        return this.doAnswer(invocation);
    }
}
