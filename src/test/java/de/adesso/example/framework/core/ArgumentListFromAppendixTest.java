package de.adesso.example.framework.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.Other;
import de.adesso.example.framework.OtherTestAppendix;
import de.adesso.example.framework.StringTestAppendix;

@RunWith(SpringRunner.class)
public class ArgumentListFromAppendixTest {

	@Test
	public void testConstructor() {
		final ArgumentListFromAppendix result = new ArgumentListFromAppendix(String.class,
				StringTestAppendix.class);

		assertThat(result).isNotNull();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorTypeIsNull() {
		new ArgumentListFromAppendix(null, StringTestAppendix.class);

		fail("should detect null value for type");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorAttachmentIsNull() {
		new ArgumentListFromAppendix(String.class, null);

		fail("should detect null value for attachmentId");
	}

	@Test
	public void testPrepareArgumentToList() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(new StringTestAppendix("some string"))
				.addAppendix(new StringTestAppendix("other string"));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentListFromAppendix argumentProcessor = new ArgumentListFromAppendix(String.class,
				StringTestAppendix.class);

		@SuppressWarnings("unchecked")
		final List<String> result = (List<String>) (Object) argumentProcessor.prepareArgument(state, args);

		assertThat(result)
				.isNotNull()
				.hasOnlyElementsOfType(String.class)
				.hasSize(2);
	}

	@Test
	public void testPrepareArgumentToListEmpty() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(new StringTestAppendix("some string"))
				.addAppendix(new StringTestAppendix("other string"));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentListFromAppendix argumentProcessor = new ArgumentListFromAppendix(String.class,
				OtherTestAppendix.class);

		@SuppressWarnings("unchecked")
		final List<String> result = (List<String>) (Object) argumentProcessor.prepareArgument(state, args);

		assertThat(result)
				.isNotNull()
				.hasSize(0);
	}

	@Test
	public void testPrepareArgumentToListDifferentAppendixes() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(new OtherTestAppendix(new Other("some string", 5)))
				.addAppendix(new StringTestAppendix("another string"))
				.addAppendix(new StringTestAppendix("thrid string"))
				.addAppendix(new OtherTestAppendix(new Other("the last string", 7)));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentListFromAppendix argumentProcessor = new ArgumentListFromAppendix(String.class,
				StringTestAppendix.class);

		@SuppressWarnings("unchecked")
		final List<String> result = (List<String>) (Object) argumentProcessor.prepareArgument(state, args);

		assertThat(result)
				.isNotNull()
				.hasOnlyElementsOfType(String.class)
				.hasSize(2);
	}
}