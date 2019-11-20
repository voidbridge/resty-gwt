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

package org.fusesource.restygwt.rebind;

import static org.fusesource.restygwt.rebind.BaseSourceCreator.DEBUG;
import static org.fusesource.restygwt.rebind.BaseSourceCreator.ERROR;
import static org.fusesource.restygwt.rebind.BaseSourceCreator.INFO;
import static org.fusesource.restygwt.rebind.BaseSourceCreator.TRACE;
import static org.fusesource.restygwt.rebind.BaseSourceCreator.WARN;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.xml.client.Document;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fusesource.restygwt.client.AbstractJsonEncoderDecoder;
import org.fusesource.restygwt.client.AbstractNestedJsonEncoderDecoder;
import org.fusesource.restygwt.client.Json;
import org.fusesource.restygwt.client.Json.Style;
import org.fusesource.restygwt.client.ObjectEncoderDecoder;

/**
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class JsonEncoderDecoderInstanceLocator implements EncoderDecoderLocator {

    public static final String JSON_ENCODER_DECODER_CLASS = AbstractJsonEncoderDecoder.class.getName();
    public static final String JSON_NESTED_ENCODER_DECODER_CLASS = AbstractNestedJsonEncoderDecoder.class.getName();
    public static final String JSON_CLASS = Json.class.getName();
    public static final String CUSTOM_SERIALIZER_GENERATORS = "org.fusesource.restygwt.restyjsonserializergenerator";

    public final JClassType STRING_TYPE;
    public final JClassType JSON_VALUE_TYPE;
    public final JClassType DOCUMENT_TYPE;
    public final JClassType MAP_TYPE;
    public final JClassType SET_TYPE;
    public final JClassType LIST_TYPE;
    public final JClassType COLLECTION_TYPE;

    public final HashMap<JType, String> builtInEncoderDecoders = new HashMap<JType, String>();
    public final JsonSerializerGenerators customGenerators = new JsonSerializerGenerators();

    public final GeneratorContext context;
    public final TreeLogger logger;

    public JsonEncoderDecoderInstanceLocator(GeneratorContext context, TreeLogger logger)
        throws UnableToCompleteException {
        this.context = context;
        this.logger = logger;

        STRING_TYPE = find(String.class);
        JSON_VALUE_TYPE = find(JSONValue.class);
        DOCUMENT_TYPE = find(Document.class);
        MAP_TYPE = find(Map.class);
        SET_TYPE = find(Set.class);
        LIST_TYPE = find(List.class);
        COLLECTION_TYPE = find(Collection.class);

        builtInEncoderDecoders.put(JPrimitiveType.BOOLEAN, "BOOLEAN");
        builtInEncoderDecoders.put(JPrimitiveType.BYTE, "BYTE");
        builtInEncoderDecoders.put(JPrimitiveType.CHAR, "CHAR");
        builtInEncoderDecoders.put(JPrimitiveType.SHORT, "SHORT");
        builtInEncoderDecoders.put(JPrimitiveType.INT, "INT");
        builtInEncoderDecoders.put(JPrimitiveType.LONG, "LONG");
        builtInEncoderDecoders.put(JPrimitiveType.FLOAT, "FLOAT");
        builtInEncoderDecoders.put(JPrimitiveType.DOUBLE, "DOUBLE");
        builtInEncoderDecoders.put(find(Boolean.class), "BOOLEAN");
        builtInEncoderDecoders.put(find(Byte.class), "BYTE");
        builtInEncoderDecoders.put(find(Character.class), "CHAR");
        builtInEncoderDecoders.put(find(Short.class), "SHORT");
        builtInEncoderDecoders.put(find(Integer.class), "INT");
        builtInEncoderDecoders.put(find(Long.class), "LONG");
        builtInEncoderDecoders.put(find(Float.class), "FLOAT");
        builtInEncoderDecoders.put(find(Double.class), "DOUBLE");
        builtInEncoderDecoders.put(find(BigDecimal.class), "BIG_DECIMAL");
        builtInEncoderDecoders.put(find(BigInteger.class), "BIG_INTEGER");

        builtInEncoderDecoders.put(STRING_TYPE, "STRING");
        builtInEncoderDecoders.put(DOCUMENT_TYPE, "DOCUMENT");
        builtInEncoderDecoders.put(JSON_VALUE_TYPE, "JSON_VALUE");

        builtInEncoderDecoders.put(find(Date.class), "DATE");

        builtInEncoderDecoders.put(find(Object.class), ObjectEncoderDecoder.class.getName() + ".INSTANCE");

        fillInCustomGenerators(context, logger);

    }

    @SuppressWarnings("unchecked")
    private void fillInCustomGenerators(GeneratorContext context, TreeLogger logger) {
        try {
            List<String> classNames =
                context.getPropertyOracle().getConfigurationProperty(CUSTOM_SERIALIZER_GENERATORS).getValues();
            for (String name : classNames) {
                try {
                    Class<? extends RestyJsonSerializerGenerator> clazz =
                        (Class<? extends RestyJsonSerializerGenerator>) Class.forName(name);
                    Constructor<? extends RestyJsonSerializerGenerator> constructor = clazz.getDeclaredConstructor();
                    RestyJsonSerializerGenerator generator = constructor.newInstance();
                    customGenerators.addGenerator(generator, context.getTypeOracle());
                } catch (Exception e) {
                    logger.log(WARN, "Could not access class: " + name, e);
                }
            }
        } catch (BadPropertyValueException ignore) {
        }
    }

    private JClassType find(Class<?> type) throws UnableToCompleteException {
        return find(type.getName());
    }

    private JClassType find(String type) throws UnableToCompleteException {
        return RestServiceGenerator.find(logger, context, type);
    }

    private String getEncoderDecoder(JType type, TreeLogger logger) throws UnableToCompleteException {
        String rc = builtInEncoderDecoders.get(type);
        if (rc == null) {
            JClassType ct = type.isClass() == null ? type.isInterface() : type.isClass();
            if (ct != null && !isCollectionType(ct)) {
                JsonEncoderDecoderClassCreator generator = new JsonEncoderDecoderClassCreator(logger, context, ct);
                return generator.create() + ".INSTANCE";
            }
        }
        return rc;
    }

    private String getCustomEncoderDecoder(JType type) {
        RestyJsonSerializerGenerator restyGenerator = customGenerators.findGenerator(type);
        if (restyGenerator == null) {
            return null;
        }
        Class<? extends JsonEncoderDecoderClassCreator> clazz = restyGenerator.getGeneratorClass();
        try {
            Constructor<? extends JsonEncoderDecoderClassCreator> constructor =
                clazz.getDeclaredConstructor(TreeLogger.class, GeneratorContext.class, JClassType.class);
            JsonEncoderDecoderClassCreator generator = constructor.newInstance(logger, context, type);
            return generator.create() + ".INSTANCE";
        } catch (Exception e) {
            logger.log(WARN, "Could not access class: " + clazz, e);
            return null;
        }
    }

    /* (non-Javadoc)
    * @see org.fusesource.restygwt.rebind.EncoderDecoderLocator#hasCustomEncoderDecoder(com.google.gwt.core.ext
    * .typeinfo.JType)
    */
    @Override
    public boolean hasCustomEncoderDecoder(JType type) {
        return getCustomEncoderDecoder(type) != null;
    }

    /* (non-Javadoc)
    * @see org.fusesource.restygwt.rebind.EncoderDecoderLocator#encodeExpression(com.google.gwt.core.ext.typeinfo
    * .JType, java.lang.String, org.fusesource.restygwt.client.Json.Style)
    */
    @Override
    public String encodeExpression(JType type, String expression, Style style) throws UnableToCompleteException {
        return encodeDecodeExpression(type, expression, style, "encode", "toJSON","toJSON", "toJSON",
            "toJSON");
    }

    /* (non-Javadoc)
    * @see org.fusesource.restygwt.rebind.EncoderDecoderLocator#decodeExpression(com.google.gwt.core.ext.typeinfo
    * .JType, java.lang.String, org.fusesource.restygwt.client.Json.Style)
    */
    @Override
    public String decodeExpression(JType type, String expression, Style style) throws UnableToCompleteException {
        return encodeDecodeExpression(type, expression, style, "decode", "toMap", "toSet", "toList", "toArray");
    }

    private String encodeDecodeExpression(JType type, String expression, Style style, String encoderMethod,
                                          String mapMethod, String setMethod, String listMethod, String arrayMethod)
        throws UnableToCompleteException {

        String customEncoderDecoder = getCustomEncoderDecoder(type);
        if (customEncoderDecoder != null) {
            return customEncoderDecoder + "." + encoderMethod + "(" + expression + ")";
        }

        String encoderDecoder = getEncoderDecoder(type, logger);
        if (encoderDecoder != null) {
            return encoderDecoder + "." + encoderMethod + "(" + expression + ")";
        }
        // TODO enum have an encodeDecoder now - should be obsolete code below
        if (null != type.isEnum()) {
            if (encoderMethod.equals("encode")) {
                return encodeDecodeExpression(STRING_TYPE, expression + ".name()", style, encoderMethod, mapMethod,
                    setMethod, listMethod, arrayMethod);
            }
            return type.getQualifiedSourceName() + ".valueOf(" +
                encodeDecodeExpression(STRING_TYPE, expression, style, encoderMethod, mapMethod, setMethod, listMethod,
                    arrayMethod) + ")";
        }

        JClassType clazz = type.isClassOrInterface();

        if (isCollectionType(clazz)) {
            JClassType[] types = getTypes(type);

            String[] coders = isMapEncoderDecoder(clazz, types, style);
            if (coders != null) {
                String keyEncoderDecoder = coders[1];
                encoderDecoder = coders[0];
                if (encoderDecoder != null && keyEncoderDecoder != null) {
                    return mapMethod + "(" + expression + ", " + keyEncoderDecoder + ", " + encoderDecoder + ", " +
                        JSON_CLASS + ".Style." + style.name() + ")";
                } else if (encoderDecoder != null) {
                    return mapMethod + "(" + expression + ", " + encoderDecoder + ", " + JSON_CLASS + ".Style." +
                        style.name() + ")";
                }
            }
            encoderDecoder = isSetEncoderDecoder(clazz, types, style);
            if (encoderDecoder != null) {
                return setMethod + "(" + expression + ", " + encoderDecoder + ")";
            }

            encoderDecoder = isListEncoderDecoder(clazz, types, style);
            if (encoderDecoder != null) {
                return listMethod + "(" + expression + ", " + encoderDecoder + ")";
            }

            encoderDecoder = isCollectionEncoderDecoder(clazz, types, style);
            if (encoderDecoder != null) {
                return listMethod + "(" + expression + ", " + encoderDecoder + ")";
            }
        }

        encoderDecoder = isArrayEncoderDecoder(type, style);
        if (encoderDecoder != null) {
            if (encoderMethod.equals("encode")) {
                return arrayMethod + "(" + expression + ", " + encoderDecoder + ")";
            } else if (type.isArray().getComponentType().isPrimitive() == JPrimitiveType.BYTE) {
                return arrayMethod + "(" + expression + ", " + encoderDecoder + ")";
            }
            return arrayMethod + "(" + expression + ", " + encoderDecoder + ", new " +
                type.isArray().getComponentType().getQualifiedSourceName() + "[" + JSON_ENCODER_DECODER_CLASS +
                ".getSize(" + expression + ")])";
        }

        error("Do not know how to encode/decode " + type);
        return null;
    }

    protected String[] isMapEncoderDecoder(JClassType clazz, JClassType[] types, Style style)
        throws UnableToCompleteException {
        String encoderDecoder;
        if (clazz.isAssignableTo(MAP_TYPE)) {
            if (types.length != 2) {
                error("Map must define two and only two type parameters");
            }

            String keyEncoderDecoder = getNestedEncoderDecoder(types[0], style);
            encoderDecoder = getNestedEncoderDecoder(types[1], style);
            return new String[] { encoderDecoder, keyEncoderDecoder };
        }
        return null;
    }

    String getNestedEncoderDecoder(JType type, Style style) throws UnableToCompleteException {
        String result = getEncoderDecoder(type, logger);
        if (result != null) {
            return result;
        }

        JClassType clazz = type.isClassOrInterface();
        if (isCollectionType(clazz)) {
            JClassType[] types = getTypes(type);

            String[] coders = isMapEncoderDecoder(clazz, types, style);
            if (coders != null) {
                String keyEncoderDecoder = coders[1];
                result = coders[0];
                if (result != null && keyEncoderDecoder != null) {
                    return JSON_NESTED_ENCODER_DECODER_CLASS + ".mapEncoderDecoder( " + keyEncoderDecoder + ", " +
                        result + ", " + JSON_CLASS + ".Style." + style.name() + " )";
                } else if (result != null) {
                    return JSON_NESTED_ENCODER_DECODER_CLASS + ".mapEncoderDecoder( " + result + ", " + JSON_CLASS +
                        ".Style." + style.name() + " )";
                }
            }
            result = isListEncoderDecoder(clazz, types, style);
            if (result != null) {
                return JSON_NESTED_ENCODER_DECODER_CLASS + ".listEncoderDecoder( " + result + " )";
            }
            result = isSetEncoderDecoder(clazz, types, style);
            if (result != null) {
                return JSON_NESTED_ENCODER_DECODER_CLASS + ".setEncoderDecoder( " + result + " )";
            }
            result = isCollectionEncoderDecoder(clazz, types, style);
            if (result != null) {
                return JSON_NESTED_ENCODER_DECODER_CLASS + ".collectionEncoderDecoder( " + result + " )";
            }
        }
        result = isArrayEncoderDecoder(type, style);
        if (result != null) {
            return JSON_NESTED_ENCODER_DECODER_CLASS + ".arrayEncoderDecoder( " + result + " )";
        }
        return null;
    }

    protected String isArrayEncoderDecoder(JType type, Style style) throws UnableToCompleteException {
        if (type.isArray() != null) {
            JType componentType = type.isArray().getComponentType();

            if (componentType.isArray() != null) {
                error("Multi-dimensional arrays are not yet supported");
            }

            String encoderDecoder = getNestedEncoderDecoder(componentType, style);
            debug("type encoder for: " + componentType + " is " + encoderDecoder);
            return encoderDecoder;
        }
        return null;
    }

    protected String isSetEncoderDecoder(JClassType clazz, JClassType[] types, Style style)
        throws UnableToCompleteException {
        if (clazz.isAssignableTo(SET_TYPE)) {
            if (types.length != 1) {
                error("Set must define one and only one type parameter");
            }
            String encoderDecoder = getNestedEncoderDecoder(types[0], style);
            debug("type encoder for: " + types[0] + " is " + encoderDecoder);
            return encoderDecoder;
        }
        return null;
    }

    protected String isListEncoderDecoder(JClassType clazz, JClassType[] types, Style style)
        throws UnableToCompleteException {
        if (clazz.isAssignableTo(LIST_TYPE)) {
            if (types.length != 1) {
                error("List must define one and only one type parameter");
            }
            String encoderDecoder = getNestedEncoderDecoder(types[0], style);
            debug("type encoder for: " + types[0] + " is " + encoderDecoder);
            return encoderDecoder;
        }
        return null;
    }

    protected String isCollectionEncoderDecoder(JClassType clazz, JClassType[] types, Style style)
        throws UnableToCompleteException {
        if (clazz.isAssignableTo(COLLECTION_TYPE)) {
            if (types.length != 1) {
                error("Collection must define one and only one type parameter");
            }
            String encoderDecoder = getNestedEncoderDecoder(types[0], style);
            debug("type encoder for: " + types[0] + " is " + encoderDecoder);
            return encoderDecoder;
        }
        return null;
    }

    protected JClassType[] getTypes(JType type) throws UnableToCompleteException {
        JClassType[] types = getTypesHelper(type);
        if (types == null) {
            JClassType superType = type.isClassOrInterface();
            while (types == null) {
                superType = superType.getSuperclass();
                if (superType == null) {
                    break;
                }
                types = getTypesHelper(superType);
            }
            if (types == null) {
                error("Collection types must be parameterized: " + type);
            }
        }
        return types;
    }

    protected JClassType[] getTypesHelper(JType type) {
        JParameterizedType parameterizedType = type.isParameterized();
        if (parameterizedType == null || parameterizedType.getTypeArgs() == null) {
            return null;
        }
        return parameterizedType.getTypeArgs();
    }

    @Override
    public boolean isCollectionType(JClassType clazz) {
        return clazz != null &&
            (clazz.isAssignableTo(SET_TYPE) || clazz.isAssignableTo(LIST_TYPE) || clazz.isAssignableTo(MAP_TYPE) ||
                clazz.isAssignableTo(COLLECTION_TYPE));
    }

    protected void error(String msg) throws UnableToCompleteException {
        logger.log(ERROR, msg);
        throw new UnableToCompleteException();
    }

    protected void warn(String msg) throws UnableToCompleteException {
        logger.log(WARN, msg);
        throw new UnableToCompleteException();
    }

    protected void info(String msg) {
        logger.log(INFO, msg);
    }

    protected void debug(String msg) {
        logger.log(DEBUG, msg);
    }

    protected void trace(String msg) {
        logger.log(TRACE, msg);
    }

    @Override
    public JClassType getListType() {
        return LIST_TYPE;
    }

}
