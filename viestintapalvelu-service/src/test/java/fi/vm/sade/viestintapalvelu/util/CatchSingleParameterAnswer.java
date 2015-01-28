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
package fi.vm.sade.viestintapalvelu.util;

import java.util.ArrayList;
import java.util.List;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * User: ratamaa
 * Date: 1.10.2014
 * Time: 16:54
 */
public class CatchSingleParameterAnswer<AnswerType,T> implements Answer<AnswerType> {
    private List<T> arguments = new ArrayList<T>();
    private List<AnswerType> returnValues = new ArrayList<AnswerType>();
    private Answer<AnswerType> target;

    CatchSingleParameterAnswer() {
    }

    CatchSingleParameterAnswer(Answer<AnswerType> target) {
        this.target = target;
    }

    public static<AnswerType,T> CatchSingleParameterAnswer<AnswerType,T> catchParameters(Answer<AnswerType> delegateTarget) {
        return new CatchSingleParameterAnswer<AnswerType, T>(delegateTarget);
    }

    public static<AnswerType,T> CatchSingleParameterAnswer<AnswerType,T> catchInvokingRealMethod() {
        return new CatchSingleParameterAnswer<AnswerType, T>();
    }

    @Override
    public AnswerType answer(InvocationOnMock invocation) throws Throwable {
        T argument = (T) invocation.getArguments()[0];
        argument = handleArgument(argument);
        this.arguments.add(argument);
        AnswerType value;
        if (this.target != null) {
            value = this.target.answer(invocation);
        } else {
            value = (AnswerType) invocation.callRealMethod();
        }
        this.returnValues.add(value);
        return value;
    }

    protected T handleArgument(T argument) {
        return argument;
    }

    public int getInvocationCount() {
        return this.arguments.size();
    }

    public List<T> getArguments() {
        return arguments;
    }

    public List<AnswerType> getReturnValues() {
        return returnValues;
    }
}
