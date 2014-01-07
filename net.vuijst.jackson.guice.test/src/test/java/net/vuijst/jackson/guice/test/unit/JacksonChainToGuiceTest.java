package net.vuijst.jackson.guice.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import net.vuijst.jackson.guice.test.model.Address;
import net.vuijst.jackson.guice.test.model.IAddress;
import net.vuijst.jackson.guice.test.model.PersonWithGoogleNamedAddress;
import net.vuijst.jackson.guice.test.model.PersonWithJavaxNamedAddress;
import net.vuijst.jackson.guice.test.util.JSONUtil;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.guice.ObjectMapperModule;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;

/**
 * The Class JacksonGuiceTest.
 * 
 * @author Charles Vuijst
 */
public class JacksonChainToGuiceTest {

	/** The injector. */
	private static Injector injector;

	/** The json string. */
	private static String jsonString;

	/** The javax named address annotation. */
	private static String JAVAX_NAMED_ADDRESS_ANNOTATION = "JavaxNamedHomeAddress";

	/** The javax named address type. */
	private static String JAVAX_NAMED_ADDRESS_TYPE = "Javax Named Home Address Type";

	/** The google named address annotation. */
	private static String GOOGLE_NAMED_ADDRESS_ANNOTATION = "GoogleNamedHomeAddress";

	/** The google named address type. */
	private static String GOOGLE_NAMED_ADDRESS_TYPE = "Google Named Home Address Type";

	/** The test person name. */
	private static String TEST_PERSON_NAME = "Test Person Name";

	/** The test address street. */
	private static String TEST_ADDRESS_STREET = "Test Street 11";

	/**
	 * Setup injector.
	 */
	@BeforeClass
	public static void setupInjector() {
		
		injector = Guice.createInjector(new ObjectMapperModule(), new Module() {

			@Override
			public void configure(final Binder binder) {

				// Works only with the Google inject annotation
				binder.bind(IAddress.class)
						.annotatedWith(Names.named(GOOGLE_NAMED_ADDRESS_ANNOTATION))
						.toInstance(new Address(GOOGLE_NAMED_ADDRESS_TYPE));

				binder.bind(IAddress.class)
						.annotatedWith(Names.named(JAVAX_NAMED_ADDRESS_ANNOTATION))
						.toInstance(new Address(JAVAX_NAMED_ADDRESS_TYPE));

			}
		});
	}

	/**
	 * Setup person model. Creates the object and marshalls it to a static JSON String to be reused.
	 */
	@BeforeClass
	public static void setupPersonModel() {
		
		// Basic person model
		PersonWithGoogleNamedAddress person = new PersonWithGoogleNamedAddress();
		person.setName(TEST_PERSON_NAME);

		// Basic address model
		Address address = new Address();
		address.setStreet(TEST_ADDRESS_STREET);

		person.setAddress(address);

		// Address is assigned to person, therefore it is not null here.
		assertNotNull(person.getAddress());

		jsonString = JSONUtil.marshalToJSON(person);

		// Address is assigned to person, but as it is a @JsonIgnore variable,
		// it is not picked up during marshalling, so won't be there after unmarshalling.
		person = JSONUtil.unmarshalFromJSON(jsonString, PersonWithGoogleNamedAddress.class);
		assertNull(person.getAddress());

		//System.out.println("JSONString: \n" + jsonString);
	}

	/**
	 * Test google named injection chained through the jackson mapper. As a result, the person model
	 * is filled with the JSON data and the attributes are filled (i.e. not null).
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testJacksonChainToGoogleNamedInjection() throws IOException {

		PersonWithGoogleNamedAddress person = injector.getInstance(ObjectMapper.class)
				.readValue(jsonString, PersonWithGoogleNamedAddress.class);

		assertNotNull(person);
		assertNotNull(person.getAddress());
		assertEquals(GOOGLE_NAMED_ADDRESS_TYPE, person.readAddressType());
		// As the address was not part of the marshalling, the street will be empty
		assertNull(person.readStreet());
	}

	/**
	 * Test javax named injection with jackson mapper.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testJacksonChainToJavaxNamedInjection() throws IOException {

		PersonWithJavaxNamedAddress person = injector.getInstance(ObjectMapper.class)
				.readValue(jsonString, PersonWithJavaxNamedAddress.class);

		assertNotNull(person);
		assertNotNull(person.getAddress());
		assertEquals(JAVAX_NAMED_ADDRESS_TYPE, person.readAddressType());
		// As the address was not part of the marshalling, the street will be empty
		assertNull(person.readStreet());
	}

	/**
	 * Test without mapper with google named injection. As a result, the person model is not filled
	 * and the attributes are empty (i.e. null).
	 */
	@Test
	public void testNoJacksonWithGoogleNamedInjection() {
		
		PersonWithGoogleNamedAddress personGoogleNamed = injector.getInstance(PersonWithGoogleNamedAddress.class);

		assertNull(personGoogleNamed.getName());
		assertNull(personGoogleNamed.getAddress());
	}

	/**
	 * Test without mapper with javax named injection. As a result, the person model is not filled
	 * and the attributes are empty (i.e. null).
	 */
	@Test
	public void testNoJacksonWithJavaxNamedInjection() {
		
		PersonWithJavaxNamedAddress personJavaxNamed = injector.getInstance(PersonWithJavaxNamedAddress.class);

		assertNull(personJavaxNamed.getName());
		assertNull(personJavaxNamed.getAddress());
	}

}
