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

package org.fusesource.restygwt.server.basic;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet that just implements the services required for the direct service test.
 *
 * @author <a href="mailto:bogdan.mustiata@gmail.com">Bogdan Mustiata</a>
 */
public class DirectServiceTestGwtServlet extends HttpServlet {

    String EMPTY_RESPONSE = "";
    String THREE_ELEMENT_LIST = "[{name:'1'},{name:'2'},{name:'3'}]";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (request.getRequestURI().endsWith("/date")) {
            response.getWriter().print(Long.parseLong(request.getParameter("date")));
        } else if (request.getRequestURI().endsWith("/dateIso8601")) {
            try {
                String date = request.getParameter("date");
                if (!"null".equals(date)) {
                    response.getWriter().print(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(date)
                            .getTime());
                } else {
                    response.getWriter().print((Long) null);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else if (request.getRequestURI().endsWith("/dateCustomPattern")) {
            try {
                String date = request.getParameter("date");
                if (!"null".equals(date)) {
                    response.getWriter().print(new SimpleDateFormat("\"''yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(date)
                            .getTime());
                } else {
                    response.getWriter().print((Long) null);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else if (request.getRequestURI().endsWith("/api/list")) {
            response.getWriter().print(THREE_ELEMENT_LIST);
        } else if (request.getRequestURI().matches(".+/\\d+")) {
            String url = request.getRequestURI();
            String val = url.substring(url.lastIndexOf('/') + 1);
            response.getWriter().print(val);
        } else {
            throw new IllegalArgumentException("Invalid servlet path called by service: " + request.getRequestURI());
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (request.getRequestURI().endsWith("/api/store")) {
            response.getWriter().print(EMPTY_RESPONSE);
        } else {
            throw new IllegalArgumentException("Invalid servlet path called by service: " + request.getRequestURI());
        }
    }
}
