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

package org.fusesource.restygwt.client.basic;

import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.fusesource.restygwt.client.DirectRestService;

/**
 * Example of using the DirectRestService call.
 * @author <a href="mailto:bogdan.mustiata@gmail.com">Bogdan Mustiata</a>
 */
public interface DirectExampleService extends DirectRestService {

    @GET
    @Path("/list")
    List<ExampleDto> getExampleDtos(@QueryParam("id") String id);

    @GET
    @Path("/date")
    Long getDate(@QueryParam("date") Date date);

    @GET
    @Path("/dateIso8601")
    Long getDateIso8601(@QueryParam("date") Date date);

    @GET
    @Path("/dateCustomPattern")
    Long getDateCustomPattern(@QueryParam("date") Date date);

    @POST
    @Path("/store")
    void storeDto(ExampleDto exampleDto);

    @GET
    @Path("/get/{id : \\d+}")
    Integer getRegex(@PathParam(value = "id") Integer i);

    @GET
    @Path("/get/{id : \\d+}/things/{thing: \\d+}")
    Integer getRegexMultiParams(@PathParam(value = "id") Integer i, @PathParam(value = "thing") Integer t);

}
