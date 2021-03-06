/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.dmn.core.ast;

import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNMessage;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.core.api.DMNExpressionEvaluator;
import org.kie.dmn.core.api.EvaluatorResult;
import org.kie.dmn.core.api.EvaluatorResult.ResultType;
import org.kie.dmn.api.core.event.DMNRuntimeEventManager;
import org.kie.dmn.core.impl.DMNContextImpl;
import org.kie.dmn.core.impl.DMNResultImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DMNListEvaluator
        implements DMNExpressionEvaluator {
    private static final Logger logger = LoggerFactory.getLogger( DMNListEvaluator.class );

    private final String                           name;
    private final String                           nodeId;
    private final org.kie.dmn.feel.model.v1_1.List listDef;
    private final List<DMNExpressionEvaluator> elements = new ArrayList<>();

    public DMNListEvaluator(String name, String nodeId, org.kie.dmn.feel.model.v1_1.List listDef) {
        this.name = name;
        this.nodeId = nodeId;
        this.listDef = listDef;
    }

    public void addElement(DMNExpressionEvaluator evaluator) {
        this.elements.add( evaluator );
    }

    public List<DMNExpressionEvaluator> getElements() {
        return this.elements;
    }

    @Override
    public EvaluatorResult evaluate(DMNRuntimeEventManager eventManager, DMNResult dmnr) {
        DMNResultImpl result = (DMNResultImpl) dmnr;
        List<Object> results = new ArrayList<>();
        DMNContext previousContext = result.getContext();
        DMNContextImpl dmnContext = (DMNContextImpl) previousContext.clone();
        result.setContext( dmnContext );

        try {
            int index = 0;
            for ( DMNExpressionEvaluator ee : elements ) {
                try {
                    EvaluatorResult er = ee.evaluate( eventManager, result );
                    if ( er.getResultType() == ResultType.SUCCESS ) {
                        results.add( er.getResult() );
                    } else {
                        String message = "Error evaluating list element on position '" + (index + 1) + "' on list '" + name + "'";
                        logger.error( message );
                        result.addMessage(
                                DMNMessage.Severity.ERROR,
                                message,
                                nodeId );
                        return new EvaluatorResultImpl( results, ResultType.FAILURE );
                    }
                } catch ( Exception e ) {
                    String message = "Error evaluating list element on position '" + (index + 1) + "' on list '" + name + "'";
                    logger.error( message );
                    result.addMessage(
                            DMNMessage.Severity.ERROR,
                            message,
                            nodeId,
                            e );
                    return new EvaluatorResultImpl( results, ResultType.FAILURE );
                } finally {
                    index++;
                }
            }
        } finally {
            result.setContext( previousContext );
        }
        return new EvaluatorResultImpl( results, ResultType.SUCCESS );
    }

}
