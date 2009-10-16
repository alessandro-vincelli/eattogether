package it.av.eatt.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(Suite.class)
@SuiteClasses({RistoranteServiceTest.class, ActivityRistoranteServiceTest.class, UserProfileServiceTest.class, UserRelationServiceTest.class, UserServiceTest.class, UserServiceTransactionManagerTest.class})
public class TestSuite {

}
