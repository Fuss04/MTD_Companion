package edu.illinois.mtdcompanion.activities;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import edu.illinois.mtdcompanion.R.string;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Hello World test to check Robolectric setup
 * @author Tom Olson
 */
@RunWith(RobolectricTestRunner.class)
public class HelloWorldTest {

	@Test
	public void checkHelloWorld() throws Exception {
		String helloWorld = new TestActivity().getResources().getString(string.hello_world);
		assertThat(helloWorld, equalTo("Hello World!"));
	}

}
