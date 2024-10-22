package com.backend.billsplitbackend.Suites;

import com.backend.billsplitbackend.ServiceTests.EventServiceTests;
import com.backend.billsplitbackend.ServiceTests.PersonServiceTests;
import com.backend.billsplitbackend.ServiceTests.UserServiceTests;
import org.junit.platform.suite.api.IncludePackages;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Service Layer Test Suite")
@IncludePackages("com.backend.billsplitbackend.ServiceTests")
@SelectClasses({
        EventServiceTests.class,
        PersonServiceTests.class,
		UserServiceTests.class
})
public class ServiceSuite {
}
