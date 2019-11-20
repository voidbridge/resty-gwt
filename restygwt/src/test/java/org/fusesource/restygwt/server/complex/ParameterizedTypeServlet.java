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

package org.fusesource.restygwt.server.complex;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fusesource.restygwt.client.basic.ParameterizedTypeServiceInterfaces;
import org.fusesource.restygwt.client.basic.ParameterizedTypeServiceInterfaces.Thing;
import org.fusesource.restygwt.client.complex.JsonTypeIdResolver.DTOInterface;

@SuppressWarnings("serial")
public class ParameterizedTypeServlet {
    /**
     * Ignores input and outputs a value.
     */
    public abstract static class JacksonOutputServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            Object o = getThing();
            resp.setContentType("application/json");
            new ObjectMapper().writeValue(resp.getOutputStream(), o);
        }

        protected abstract Object getThing();
    }

    public static class IntServlet extends JacksonOutputServlet {
        @Override
        protected Integer getThing() {
            return 123456;
        }

    }

    public static class ThingServlet extends JacksonOutputServlet {
        @Override
        protected ParameterizedTypeServiceInterfaces.Thing getThing() {
            Thing thing = new Thing();
            thing.name = "Fred Flintstone";
            thing.shoeSize = 12;
            return thing;
        }
    }


    public static class EchoNameServlet extends HttpServlet {
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            ObjectMapper mapper = new ObjectMapper();
            ObjectReader reader = mapper.reader(DTOInterface.class);
            DTOInterface dto = reader.readValue(req.getInputStream());
            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(), dto.getName());
        }
    }
}
