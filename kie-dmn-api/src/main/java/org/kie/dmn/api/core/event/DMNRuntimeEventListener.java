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

package org.kie.dmn.api.core.event;

import org.kie.dmn.api.core.event.AfterEvaluateBKMEvent;
import org.kie.dmn.api.core.event.AfterEvaluateDecisionEvent;
import org.kie.dmn.api.core.event.AfterEvaluateDecisionTableEvent;
import org.kie.dmn.api.core.event.BeforeEvaluateBKMEvent;
import org.kie.dmn.api.core.event.BeforeEvaluateDecisionEvent;
import org.kie.dmn.api.core.event.BeforeEvaluateDecisionTableEvent;

public interface DMNRuntimeEventListener {

    void beforeEvaluateDecision(BeforeEvaluateDecisionEvent event);

    void afterEvaluateDecision(AfterEvaluateDecisionEvent event);

    void beforeEvaluateBKM(BeforeEvaluateBKMEvent event);

    void afterEvaluateBKM(AfterEvaluateBKMEvent event);

    void beforeEvaluateDecisionTable(BeforeEvaluateDecisionTableEvent event);

    void afterEvaluateDecisionTable(AfterEvaluateDecisionTableEvent event);
}
