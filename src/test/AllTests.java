package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ EmblReaderTest.class, GenbankReaderTest.class, UniprotReaderTest.class })
public class AllTests {

}
