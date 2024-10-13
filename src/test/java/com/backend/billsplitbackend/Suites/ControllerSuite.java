package com.backend.billsplitbackend.Suites;

import com.backend.billsplitbackend.ControllerTests.EventControllerTests;
import com.backend.billsplitbackend.ControllerTests.PersonControllerTests;
import org.junit.platform.suite.api.IncludePackages;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Controller Layer Test Suite")
@IncludePackages("com.backend.billsplitbackend.ControllerTests")
@SelectClasses({
        EventControllerTests.class,
        PersonControllerTests.class,
})

public class ControllerSuite {
}
