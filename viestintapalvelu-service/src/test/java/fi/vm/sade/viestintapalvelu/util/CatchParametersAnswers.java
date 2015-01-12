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
import java.util.Arrays;
import java.util.List;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * User: ratamaa
 * Date: 9.10.2014
 * Time: 11:24
 */
public class CatchParametersAnswers<AnswerType>  implements Answer<AnswerType> {
    private List<List<Object>> arguments = new ArrayList<List<Object>>();
    private List<AnswerType> returnValues = new ArrayList<AnswerType>();
    private Answer<AnswerType> target;

    private CatchParametersAnswers() {
    }

    private CatchParametersAnswers(Answer<AnswerType> target) {
        this.target = target;
    }

    public static CatchParametersAnswers<Void> catchAllParameters() {
        return new CatchParametersAnswers<Void>(AnswerChain.atFirstDoNothing());
    }

    public static<AnswerType> CatchParametersAnswers<AnswerType> catchAllParameters(Answer<AnswerType> delegateTarget) {
        return new CatchParametersAnswers<AnswerType>(delegateTarget);
    }

    public static<AnswerType> CatchParametersAnswers<AnswerType> catchAllInvokingRealMethod() {
        return new CatchParametersAnswers<AnswerType>();
    }

    @Override
    public AnswerType answer(InvocationOnMock invocation) throws Throwable {
        this.arguments.add(Arrays.asList(invocation.getArguments()));
        AnswerType value;
        if (this.target != null) {
            value = this.target.answer(invocation);
        } else {
            value = (AnswerType) invocation.callRealMethod();
        }
        this.returnValues.add(value);
        return value;
    }

    public int getInvocationCount() {
        return this.arguments.size();
    }

    public List<List<Object>> getArguments() {
        return arguments;
    }

    public List<AnswerType> getReturnValues() {
        return returnValues;
    }
}