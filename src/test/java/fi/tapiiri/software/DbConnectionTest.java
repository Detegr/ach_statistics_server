package fi.tapiiri.software;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DbConnectionTest extends TestCase
{
	public DbConnectionTest(String test)
	{
		super(test);
	}

	public static Test suite()
	{
		return new TestSuite(DbConnectionTest.class);
	}

	public void testApp()
	{
		assertTrue(true);
	}
}
