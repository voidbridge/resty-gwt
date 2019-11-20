/**
 * Copyright (C) 2009-2011 the original author or authors.
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

package org.fusesource.restygwt.client;

import org.fusesource.restygwt.client.callback.CallbackAware;

/**
 * @author <a href="mailto:bogdan.mustiata@gmail.com">Bogdan Mustiata</a>
 */
public class REST<R> {
    private MethodCallback<R> callback;

    public REST(MethodCallback<R> callback) {
        this.callback = callback;
    }

    public static <R> REST<R> withCallback(MethodCallback<R> callback) {
        return new REST<R>(callback);
    }

    public final <T extends DirectRestService> T call(T service) {
        ((CallbackAware) service).setCallback(callback);
        return service;
    }
}
