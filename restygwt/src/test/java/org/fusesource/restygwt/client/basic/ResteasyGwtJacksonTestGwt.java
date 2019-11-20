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

import static org.fusesource.restygwt.client.complex.ResteasyService.Bean;
import static org.fusesource.restygwt.client.complex.ResteasyService.CustomException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.junit.client.GWTTestCase;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.REST;
import org.fusesource.restygwt.client.TextCallback;
import org.fusesource.restygwt.client.complex.ResteasyService;

public class ResteasyGwtJacksonTestGwt extends GWTTestCase {
    private static final String THROWABLE_JSON = "{\n" +
            "  \"cause\" : {\n" +
            "    \"cause\" : null,\n" +
            "    \"stackTrace\" : [ {\n" +
            "      \"methodName\" : \"method1\",\n" +
            "      \"fileName\" : \"Class1.java\",\n" +
            "      \"lineNumber\" : 1,\n" +
            "      \"className\" : \"com.fusesource.Class1\",\n" +
            "      \"nativeMethod\" : false\n" +
            "    }, {\n" +
            "      \"methodName\" : \"method2\",\n" +
            "      \"fileName\" : \"Class2.java\",\n" +
            "      \"lineNumber\" : 2,\n" +
            "      \"className\" : \"com.fusesource.Class2\",\n" +
            "      \"nativeMethod\" : false\n" +
            "    } ],\n" +
            "    \"message\" : \"Var cannot be null.\",\n" +
            "    \"localizedMessage\" : \"Var cannot be null.\",\n" +
            "    \"suppressed\" : [ ]\n" +
            "  },\n" +
            "  \"stackTrace\" : [ {\n" +
            "    \"methodName\" : \"method3\",\n" +
            "    \"fileName\" : \"Class3.java\",\n" +
            "    \"lineNumber\" : 3,\n" +
            "    \"className\" : \"com.fusesource.Class3\",\n" +
            "    \"nativeMethod\" : false\n" +
            "  } ],\n" +
            "  \"message\" : \"Shouldn't happen.\",\n" +
            "  \"localizedMessage\" : \"Shouldn't happen.\",\n" +
            "  \"suppressed\" : [ ]\n" +
            "}";


    @Override
    public String getModuleName() {
        return "org.fusesource.restygwt.ResteasyGwtJacksonTestGwt";
    }

    public void testGetStringAnyString() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        final String name = "Any string";
        REST.withCallback(new MethodCallback<String>() {
            @Override
            public void onSuccess(Method method, String response) {
                assertEquals(name, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).getString(name);
    }

    public void testGetStringEmptyString() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        final String name = "";
        REST.withCallback(new MethodCallback<String>() {
            @Override
            public void onSuccess(Method method, String response) {
                assertEquals(name, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).getString(name);
    }

    public void testGetStringNullString() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        final String name = null;
        REST.withCallback(new MethodCallback<String>() {
            @Override
            public void onSuccess(Method method, String response) {
                assertEquals(name, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).getString(name);
    }

    public void testPostStringAnyString() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        final String name = "Any string";
        REST.withCallback(new MethodCallback<String>() {
            @Override
            public void onSuccess(Method method, String response) {
                assertEquals(name, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).postString(name);
    }

    public void testPostStringEmptyString() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        final String name = "";
        REST.withCallback(new MethodCallback<String>() {
            @Override
            public void onSuccess(Method method, String response) {
                assertEquals(name, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).postString(name);
    }

    public void testPostStringNullString() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        final String name = null;
        REST.withCallback(new MethodCallback<String>() {
            @Override
            public void onSuccess(Method method, String response) {
                assertEquals(name, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).postString(name);
    }

    public void testGetStringAsPlainTextAnyString() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        final String name = "Any string";
        REST.withCallback(new TextCallback() {
            @Override
            public void onSuccess(Method method, String response) {
                assertEquals(name, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).getStringAsPlainText(name);
    }

    @Override
    protected void reportUncaughtException(Throwable ex) {
        if (ex instanceof ExpectedTestException) {
            finishTest();
            return;
        }
        super.reportUncaughtException(ex);
    }

    class ExpectedTestException extends RuntimeException {
    }

    public void testGetStringAsPlainTextNullStringException() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        final String name = null;
        REST.withCallback(new TextCallback() {
            @Override
            public void onSuccess(Method method, String response) {
                assertEquals(name, response);
                throw new ExpectedTestException();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).getStringAsPlainText(name);
    }

    public void testGetStringAsPlainTextEmptyString() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        final String name = "";
        REST.withCallback(new TextCallback() {
            @Override
            public void onSuccess(Method method, String response) {
                assertEquals(name, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).getStringAsPlainText(name);
    }

    public void testGetStringAsPlainTextNullString() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        final String name = null;
        REST.withCallback(new TextCallback() {
            @Override
            public void onSuccess(Method method, String response) {
                assertEquals(name, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).getStringAsPlainText(name);
    }

    public void testPostStringAsPlainTextAnyString() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        final String name = "Any string";
        REST.withCallback(new TextCallback() {
            @Override
            public void onSuccess(Method method, String response) {
                assertEquals(name, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).postStringAsPlainText(name);
    }

    public void testPostStringAsPlainTextEmptyString() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        final String name = "";
        REST.withCallback(new TextCallback() {
            @Override
            public void onSuccess(Method method, String response) {
                assertEquals(name, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).postStringAsPlainText(name);
    }

    public void testPostStringAsPlainTextNullString() {
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        final AtomicBoolean onSuccessConsumed = new AtomicBoolean();
        final String name = null;
        REST.withCallback(new TextCallback() {
            @Override
            public void onSuccess(Method method, String response) {
                assertFalse("onSuccess should be called only once", onSuccessConsumed.isValue());
                onSuccessConsumed.setValue(true);
                assertEquals(name, response);
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).postStringAsPlainText(name);
    }

    @Nonnull
    private static CustomException createCustomException() {
        NullPointerException cause = new NullPointerException("Var cannot be null.");
        StackTraceElement elt0 = new StackTraceElement("com.fusesource.Class1", "method1", "Class1.java", 1);
        StackTraceElement elt1 = new StackTraceElement("com.fusesource.Class2", "method2", "Class2.java", 2);
        cause.setStackTrace(new StackTraceElement[]{elt0, elt1});

        CustomException ex = new CustomException("Shouldn't happen.", cause);
        StackTraceElement elt3 = new StackTraceElement("com.fusesource.Class3", "method3", "Class3.java", 3);
        ex.setStackTrace(new StackTraceElement[]{elt3});
        return ex;
    }

    public void testPostThrowable() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        Throwable throwable = createCustomException();
        REST.withCallback(new MethodCallback<String>() {
            @Override
            public void onSuccess(Method method, String response) {
                convertToJSONValueAndAssertEqual(THROWABLE_JSON, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).postThrowable(throwable);
    }

    public void testPostCustomException() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        CustomException customException = createCustomException();
        REST.withCallback(new MethodCallback<String>() {
            @Override
            public void onSuccess(Method method, String response) {
                convertToJSONValueAndAssertEqual(THROWABLE_JSON, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).postCustomException(customException);
    }

    public void testPostCustomExceptionAsFormParam() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        CustomException customException = createCustomException();
        REST.withCallback(new MethodCallback<String>() {
            @Override
            public void onSuccess(Method method, String response) {
                convertToJSONValueAndAssertEqual(THROWABLE_JSON, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).postCustomExceptionAsFormParam(customException);
    }

    public void testPostThrowableAsFormParam() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        Throwable throwable = createCustomException();
        REST.withCallback(new MethodCallback<String>() {
            @Override
            public void onSuccess(Method method, String response) {
                convertToJSONValueAndAssertEqual(THROWABLE_JSON, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).postThrowableAsFormParam(throwable);
    }

    public void testPostBean() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        final Bean bean = new Bean("string0", new Date(0), 0L, 'a', true);

        REST.withCallback(new MethodCallback<Bean>() {
            @Override
            public void onSuccess(Method method, Bean response) {
                assertEquals(bean, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).postBean(bean);
    }

    public void testPostBeans() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        Bean bean0 = new Bean("string0", new Date(0), 0L, 'a', true);
        Bean bean1 = new Bean("string1", new Date(1), 1L, 'b', false);
        final List<Bean> beans = Arrays.asList(bean0, bean1);

        REST.withCallback(new MethodCallback<List<Bean>>() {
            @Override
            public void onSuccess(Method method, List<Bean> response) {
                assertEquals(beans, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).postBeans(beans);
    }

    public void testPostBeanAsFormParam() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        final Bean bean = new Bean("string0", new Date(0), 0L, 'a', true);

        REST.withCallback(new MethodCallback<Bean>() {
            @Override
            public void onSuccess(Method method, Bean response) {
                assertEquals(bean, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).postBeanAsFormParam(bean);
    }

    public void testPostBeansAsFormParams() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        final Bean bean0 = new Bean("string0", new Date(0), 0L, 'a', true);
        final Bean bean1 = new Bean("string1", new Date(1), 1L, 'b', false);

        REST.withCallback(new MethodCallback<List<Bean>>() {
            @Override
            public void onSuccess(Method method, List<Bean> response) {
                assertEquals(Arrays.asList(bean0, bean1), response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).postBeansAsFormParams(bean0, bean1);
    }

    public void testPostBeansAsFormParam() {
        delayTestFinish(10000);
        ResteasyService resteasyService = GWT.create(ResteasyService.class);

        Bean bean0 = new Bean("string0", new Date(0), 0L, 'a', true);
        Bean bean1 = new Bean("string1", new Date(1), 1L, 'b', false);
        final List<Bean> beans = Arrays.asList(bean0, bean1);

        REST.withCallback(new MethodCallback<List<Bean>>() {
            @Override
            public void onSuccess(Method method, List<Bean> response) {
                assertEquals(beans, response);
                finishTest();
            }

            @Override
            public void onFailure(Method method, Throwable e) {
                fail(e.getMessage());
            }
        }).call(resteasyService).postBeansAsFormParam(beans);
    }
    
    private void convertToJSONValueAndAssertEqual(String first, String second) {
        JSONValue firstValue = JSONParser.parseStrict(first);
        JSONValue secondValue = JSONParser.parseStrict(second);
        compareJSONValue(firstValue, secondValue);
    }
    
    private void compareJSONValue(JSONValue first, JSONValue second) {
        JSONObject firstObject;
        JSONBoolean firstBoolean;
        JSONArray firstArray;
        JSONNumber firstNumber;
        JSONString firstString;
        if ((firstObject = first.isObject()) != null) {
            JSONObject secondObject;
            if ((secondObject = second.isObject()) == null) {
                fail("First " + first + " was a JSONObject while Second " + second + " was not");
            }
            Set<String> firstKeySet = firstObject.keySet();
            Set<String> secondKeySet = secondObject.keySet();
            assertTrue(firstKeySet.containsAll(secondKeySet));
            assertTrue(secondKeySet.containsAll(firstKeySet));
            for (String key : firstKeySet) {
                JSONValue firstValue = firstObject.get(key);
                JSONValue secondValue = secondObject.get(key);
                compareJSONValue(firstValue, secondValue);
            }
        } else if ((firstArray = first.isArray()) != null) {
            JSONArray secondArray;
            if ((secondArray = second.isArray()) == null) {
                fail("First " + first + " was a JSONArray while Second " + second + " was not");
            }
            assertTrue(secondArray.size() == firstArray.size());
            for (int x = 0; x < secondArray.size(); x++) {
                JSONValue firstValue = firstArray.get(x);
                JSONValue secondValue = secondArray.get(x);
                compareJSONValue(firstValue, secondValue);
            }
        } else if (first.isNull() != null) {
            if (second.isNull() == null) {
                fail("First " + first + " was a JSONNull while Second " + second + " was not");
            }
            assertTrue(true);
        } else if ((firstNumber = first.isNumber()) != null) {
            JSONNumber secondNumber;
            if ((secondNumber = second.isNumber()) == null) {
                fail("First " + first + " was a JSONNumber while Second " + second + " was not");
            }
            assertTrue(firstNumber.doubleValue() == secondNumber.doubleValue());
        } else if ((firstString = first.isString()) != null) {
            JSONString secondString;
            if ((secondString = second.isString()) == null) {
                fail("First " + first + " was a JSONString while Second " + second + " was not");
            }
            assertTrue(firstString.stringValue().equals(secondString.stringValue()));
        } else if ((firstBoolean = first.isBoolean()) != null) {
            JSONBoolean secondBoolean;
            if ((secondBoolean = second.isBoolean()) == null) {
                fail("First " + first + " was a JSONBoolean while Second " + second + " was not");
            }
            assertTrue(firstBoolean.booleanValue() == secondBoolean.booleanValue());
        } else {
            fail("Unknown JSONValue");
        }
    }

    public static class AtomicBoolean {
        private boolean value;

        public boolean isValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }
    }
}
