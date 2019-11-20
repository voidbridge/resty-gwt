/**
 * Copyright (C) 2009-2012 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fusesource.restygwt.mocking;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.junit.GWTMockUtilities;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.callback.CallbackFilter;
import org.fusesource.restygwt.client.callback.DefaultFilterawareRequestCallback;
import org.fusesource.restygwt.client.callback.FilterawareRequestCallback;


public class DefaultFilterawareRequestCallbackTestCase extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        GWTMockUtilities.disarm();

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        GWTMockUtilities.restore();
    }

    public void test() throws Exception {
        // setup a method, a builder and a filter-aware callback
        Method method = EasyMock.createMock(Method.class);
        RequestBuilder builder = EasyMock.createMock(RequestBuilder.class);
        RequestCallback rc = new RequestCallback() {

            @Override
            public void onResponseReceived(Request request, Response response) {
            }

            @Override
            public void onError(Request request, Throwable exception) {
                throw new RuntimeException(exception);
            }
        };
        EasyMock.expect(builder.getCallback()).andReturn(rc);
        EasyMock.replay(builder);
        method.builder = builder;
        EasyMock.expect(method.isExpected(200)).andReturn(true);
        EasyMock.replay(method);

        FilterawareRequestCallback callback = new DefaultFilterawareRequestCallback(method);

        // set the response code
        Response resp = EasyMock.createMock(Response.class);
        EasyMock.expect(resp.getStatusCode()).andReturn(200).anyTimes();
        EasyMock.replay(resp);

        // expect to call each filter with RequestCallback rc
        CallbackFilter[] filters = new CallbackFilter[4];
        for (int i = 0; i < filters.length; i++) {
            filters[i] = EasyMock.createMock(CallbackFilter.class);
            callback.addFilter(filters[i]);
            EasyMock.expect(filters[i].filter(method, resp, rc)).andReturn(rc);
            EasyMock.replay(filters[i]);
        }

        //now call the success case
        callback.onResponseReceived(null, resp);

        // now call the error case
        Exception e = new Exception();
        try {
            callback.onError(null, e);
            fail();
        } catch (RuntimeException ee) {
            assertSame(e, ee.getCause());
        }

        // verify the calls on the mocks
        for (CallbackFilter filter : filters) {
            EasyMock.verify(filter);
        }
        EasyMock.verify(method, builder, resp);
    }
}
