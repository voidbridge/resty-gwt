package org.fusesource.restygwt.rebind;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.SelectionProperty;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

import java.lang.reflect.Constructor;

public class EncoderDecoderLocatorFactory {


	public static final String LOCATOR_CLASS_PROPERTY_NAME = "restygwt.encodeDecode.locatorClass";

	public static final String USE_GWT_JACKSON_ENCODE_DECODER_PROPERTY_NAME = "restygwt.encodeDecode.useGwtJackson";

	public static EncoderDecoderLocator getEncoderDecoderInstanceLocator(GeneratorContext context,
			TreeLogger logger) throws UnableToCompleteException {
		try {
			ConfigurationProperty prop =
					context.getPropertyOracle().getConfigurationProperty(LOCATOR_CLASS_PROPERTY_NAME);
			String locatorClassName = prop.getValues().get(0);
			if (locatorClassName != null) {
				logger.log(TreeLogger.Type.DEBUG, "Using locator class: " + locatorClassName);
				try {
					Class<?> locatorClass = Class.forName(locatorClassName);
					Constructor<?> locatorConstructor =
							locatorClass.getConstructor(GeneratorContext.class, TreeLogger.class);
					return (EncoderDecoderLocator) locatorConstructor.newInstance(context, logger);
				}
				catch (ClassNotFoundException e) {
					logger.log(TreeLogger.Type.ERROR, "Locator class not found: " + e.getMessage(), e);
				}
				catch (ReflectiveOperationException e) {
					logger.log(TreeLogger.Type.ERROR, "Error creating instance of locator class [" +
							locatorClassName + "]", e);
				}
			}
		}
		catch (BadPropertyValueException e) {
		}
		
		boolean useGwtJacksonDecoder = false;
		try {
			SelectionProperty  prop = context.getPropertyOracle().getSelectionProperty(logger,
					USE_GWT_JACKSON_ENCODE_DECODER_PROPERTY_NAME);
			if (prop != null) {
				String propVal = prop.getCurrentValue();
				if (propVal != null) {
					useGwtJacksonDecoder = Boolean.parseBoolean(propVal);
				}
			}
		} catch (BadPropertyValueException e) {
		}

		if (useGwtJacksonDecoder) {
			return getGwtJacksonInstance(context, logger);
		}
		return restyGwtInstance(context, logger);
	}

	private static EncoderDecoderLocator restyGwtInstance(GeneratorContext context, TreeLogger logger)
			throws UnableToCompleteException {
		//JsonEncoderDecoderInstance needs to be created every time. Why???????
		return new JsonEncoderDecoderInstanceLocator(context, logger);

//		if (restyGwtInstanceLocator == null) {
//			restyGwtInstanceLocator = new JsonEncoderDecoderInstanceLocator(context, logger);
//		}
//		return restyGwtInstanceLocator;
	}

	private static EncoderDecoderLocator getGwtJacksonInstance(GeneratorContext context, TreeLogger logger)
			throws UnableToCompleteException {
		return new GwtJacksonEncoderDecoderInstanceLocator(context, logger);

//		if (gwtJacksonInstanceLocator == null) {
//			gwtJacksonInstanceLocator = new GwtJacksonEncoderDecoderInstanceLocator(context, logger);
//		}
//		return gwtJacksonInstanceLocator;
	}
}
