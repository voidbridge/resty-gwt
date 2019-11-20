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

package org.fusesource.restygwt.examples.server;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;

/**
 * This servlet is a horible hack to integrate jersey /w gwt hosted mode junit
 * tests.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class CxfServlet extends CXFNonSpringJaxrsServlet {

    private static final long serialVersionUID = -273961734543645503L;

    private static Properties initParams = new Properties();

    static {
        initParams.put("jaxrs.address", "/org.fusesource.restygwt.examples.CXF_JAXSON.JUnit/rest/");
        initParams.put("jaxrs.serviceClasses", MapService.class.getName() + " ");
        initParams.put("jaxrs.providers", "com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider");
    }

    @Override
    public void init(final ServletConfig servletConfig) throws ServletException {
        super.init(new ServletConfig() {
            @Override
            public String getServletName() {
                return servletConfig.getServletName();
            }

            @Override
            public ServletContext getServletContext() {
                return servletConfig.getServletContext();
            }

            @Override
            @SuppressWarnings("unchecked")
            public Enumeration getInitParameterNames() {
                return initParams.keys();
            }

            @Override
            public String getInitParameter(String name) {
                return initParams.getProperty(name);
            }
        });
    }
}
