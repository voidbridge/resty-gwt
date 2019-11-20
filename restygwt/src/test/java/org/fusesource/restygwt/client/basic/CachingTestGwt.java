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

package org.fusesource.restygwt.client.basic;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.Timer;

import java.util.logging.Logger;

import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Dispatcher;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestServiceProxy;
import org.fusesource.restygwt.client.cache.QueueableCacheStorage;
import org.fusesource.restygwt.client.cache.VolatileQueueableCacheStorage;
import org.fusesource.restygwt.client.callback.CachingCallbackFilter;
import org.fusesource.restygwt.client.callback.CallbackFactory;
import org.fusesource.restygwt.client.callback.CallbackFilter;
import org.fusesource.restygwt.client.callback.DefaultCallbackFactory;
import org.fusesource.restygwt.client.callback.ModelChangeCallbackFilter;
import org.fusesource.restygwt.client.dispatcher.CachingDispatcherFilter;
import org.fusesource.restygwt.client.dispatcher.DefaultFilterawareDispatcher;
import org.fusesource.restygwt.client.dispatcher.DispatcherFilter;

/**
 * @author <a href="mailto:mail@raphaelbauer.com">rEyez</a>
 */
public class CachingTestGwt extends GWTTestCase {

    public int timesAskedServer = 0;

    @Override
    public String getModuleName() {
        return "org.fusesource.restygwt.CachingTestGwt";
    }

    /**
     * We are inside HtmlUni - therefore it's a bit more wired...
     *
     * Main simple idea:
     *
     * There is one regular JSON request against the server scheduled each 2000 ms.
     *
     * Another "Check" request is also scheduled at 2000ms. This "Check" request
     * checks if the real server was contacted only one time and fires doFinish() when
     * everything is okay...
     *
     */
    public void testIfCachingWorks() {
        //configure RESTY to use cache:
        EventBus eventBus = new SimpleEventBus();
        QueueableCacheStorage cache = new VolatileQueueableCacheStorage();
        CallbackFilter cachingCallbackFilter = new CachingCallbackFilter(cache);
        CallbackFactory callbackFactory =
            new DefaultCallbackFactory(cachingCallbackFilter, new ModelChangeCallbackFilter(eventBus));
        DispatcherFilter cachingDispatcherFilter = new CachingDispatcherFilter(cache, callbackFactory);

        Dispatcher dispatcher = new DefaultFilterawareDispatcher(cachingDispatcherFilter);

        Defaults.setDispatcher(dispatcher);

        Resource resource = new Resource(GWT.getModuleBaseURL() + "api/getendpoint");
        final ExampleService service = GWT.create(ExampleService.class);
        ((RestServiceProxy) service).setResource(resource);

        Timer timer = new Timer() {
            @Override
            public void run() {
                if (LogConfiguration.loggingIsEnabled()) {
                    Logger.getLogger(CachingTestGwt.class.getName()).severe("run timer method");
                }
                service.getExampleDto(new MethodCallback<ExampleDto>() {
                    @Override
                    public void onSuccess(Method method, ExampleDto response) {
                        assertEquals(response.name, "myName");
                    }

                    @Override
                    public void onFailure(Method method, Throwable exception) {
                        fail("got to failure method - unexpected");
                    }
                });
            }
        };

        timer.scheduleRepeating(2000);

        //the checking task => for convenience in separate method...
        checkIfServerIsRequestsAreCached();

        //wait 10 secs for the test to call doFinish() => otherwise it fails..
        delayTestFinish(10000);
    }


    public void checkIfServerIsRequestsAreCached() {
        Timer timerCheck = new Timer() {
            @Override
            public void run() {
                RequestBuilder ajax =
                    new RequestBuilder(RequestBuilder.GET, GWT.getModuleBaseURL() + "api/getnumberofcontacts");

                try {
                    ajax.sendRequest("", new RequestCallback() {
                        @Override
                        public void onError(Request request, Throwable exception) {

                        }

                        @Override
                        public void onResponseReceived(Request request, Response response) {
                            String text = response.getText();

                            if (!text.equals("1")) {
                                fail("response counter must be \"1\", got \"" + text + "\" instead");
                            }

                            if (timesAskedServer == 3) {
                                finishTest();
                            }

                            ++timesAskedServer;
                        }

                    });
                } catch (Exception e) {
                    GWT.log("Exception on AJAX request: " + e.getMessage());
                }
            }
        };

        timerCheck.scheduleRepeating(2000);
    }
}