package it.av.eatt.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({RistoranteServiceTest.class, ActivityRistoranteServiceTest.class, UserProfileServiceTest.class, UserRelationServiceTest.class, UserServiceTest.class, UserServiceTransactionManagerTest.class})
public class TestSuite {

}
