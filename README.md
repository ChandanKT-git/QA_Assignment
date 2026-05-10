# BookCart Automation Test  

This project contains automated tests for the BookCart e-commerce website using Selenium WebDriver with Java and TestNG.

## Prerequisites

- Java JDK 11 or higher
- Maven 3.6.0 or higher
- Chrome browser installed
- Internet connection (to download dependencies and access the test website)

## Project Structure

```
src/
├── main/java/com/bookcart/
│   ├── base/BaseTest.java         # Base test class with setup/teardown
│   └── pages/                     # Page Object Model classes
│       ├── HomePage.java
│       ├── CartPage.java
│       └── CheckoutPage.java
└── test/java/com/bookcart/tests/
    └── BookCartTest.java          # Test class with test scenarios
```

## Setup Instructions

1. Clone this repository or download the source code
2. Open a terminal/command prompt and navigate to the project root directory
3. Run the following command to download all dependencies:
   ```
   mvn clean install
   ```

## Running Tests

### Run all tests
```
mvn test
```

### Run specific test class
```
mvn test -Dtest=BookCartTest
```

### Run with different browser (default is Chrome)
```
mvn test -Dbrowser=firefox
```

## Test Scenario

The automated test performs the following steps:
1. Opens the BookCart homepage
2. Searches for a product ("The Secret")
3. Adds the product to the cart
4. Verifies the product is in the cart
5. Proceeds to checkout
6. Verifies the login page is displayed (as checkout requires authentication)

## Reports

TestNG generates an HTML report in the `test-output` directory after test execution. Open `emailable-report.html` or `index.html` to view the test results.

## Best Practices Followed

- Page Object Model (POM) design pattern
- Explicit waits for better synchronization
- Meaningful locators (XPath and CSS selectors)
- Proper test assertions
- Maven for dependency management
- Separate test data from test logic
- Clear and descriptive method and variable names
