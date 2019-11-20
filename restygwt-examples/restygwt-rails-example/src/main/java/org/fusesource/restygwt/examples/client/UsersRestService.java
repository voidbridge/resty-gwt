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

package org.fusesource.restygwt.examples.client;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.Attribute;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

/**
 *
 * @author <a href="http://blog.mkristian.tk">Kristian Meier</a>
 */
public interface UsersRestService extends RestService {

    @GET
    @Path("/users")
    void index(MethodCallback<List<User>> callback);

    @GET
    @Path("/users/{id}")
    void show(@PathParam("id") int id, MethodCallback<User> callback);

    @POST
    @Path("/users")
    void create(User value, MethodCallback<User> callback);

    @PUT
    @Path("/users/{id}")
    void update(@PathParam("id") @Attribute("id") User value, MethodCallback<User> callback);

    @DELETE
    @Path("/users/{id}")
    void destroy(@PathParam("id") @Attribute("id") User value, MethodCallback<Void> callback);

}
