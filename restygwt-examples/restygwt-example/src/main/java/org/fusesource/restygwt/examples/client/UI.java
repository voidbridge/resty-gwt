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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import org.fusesource.restygwt.client.JsonCallback;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestServiceProxy;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class UI implements EntryPoint {

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
        Button button = new Button("Place Pizza Order");
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                placeOrder();
            }
        });
        RootPanel.get().add(button);

        button = new Button("Do Put");
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                doPut();
            }
        });
        RootPanel.get().add(button);

        button = new Button("Do A Search on Yahoo /w Jsonp");
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                doJsonp();
            }
        });

        RootPanel.get().add(button);
    }

    protected void doPut() {
        MethodService service = GWT.create(MethodService.class);
        service.put(new MethodCallback<String>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Window.alert("Error x: " + exception);
            }

            @Override
            public void onSuccess(Method method, String response) {
                RootPanel.get().add(new Label("done."));
            }
        });
    }

    private void placeOrder() {
        PizzaService service = GWT.create(PizzaService.class);
        Resource resource = new Resource(GWT.getModuleBaseURL() + "pizza-service");
        ((RestServiceProxy) service).setResource(resource);

        PizzaOrder order = new PizzaOrder();
        order.delivery = true;
        order.delivery_address.add("3434 Pinerun Ave.");
        order.delivery_address.add("Tampa, FL  33734");

        Pizza pizza = new Pizza();
        pizza.crust = "thin";
        pizza.quantity = 1;
        pizza.size = 16;
        pizza.toppings.add("ham");
        pizza.toppings.add("pineapple");
        order.pizzas.add(pizza);

        pizza = new Pizza();
        pizza.crust = "thin";
        pizza.quantity = 1;
        pizza.size = 16;
        pizza.toppings.add("extra cheese");
        order.pizzas.add(pizza);

        service.order(order, new MethodCallback<OrderConfirmation>() {
            @Override
            public void onSuccess(Method method, OrderConfirmation receipt) {
                RootPanel.get().add(new Label("got receipt: " + receipt));
            }

            @Override
            public void onFailure(Method method, Throwable exception) {
                Window.alert("Error: " + exception);
            }
        });
    }

    protected void doJsonp() {
        Resource resource = new Resource(
            "http://search.yahooapis.com/WebSearchService/V1/webSearch?appid=YahooDemo&query=finances&format=pdf" +
                "&output=json&callback=callback");
        resource.jsonp().send(new JsonCallback() {
            @Override
            public void onSuccess(Method method, JSONValue response) {
                JSONObject obj = (JSONObject) ((JSONObject) response).get("ResultSet");
                RootPanel.get().add(new Label("Search Results Available: " + obj.get("totalResultsAvailable")));
            }

            @Override
            public void onFailure(Method method, Throwable exception) {
                Window.alert("Error x: " + exception);
            }
        });
    }

}
