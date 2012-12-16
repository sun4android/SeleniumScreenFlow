## Selenium Screen Flow ##
Designed to take screenshot of given part of the page 
during test execution and creating flow ouf of them. 
Support for
1. outlining web elements
2. finding  web elements parents of given type (table, div, ...) 
3. cropping screenshots to parent or web element
4. cropping screenshots to flow with best effort to preserve web element
5. creating flow of titled screenshots

Code example
	ScreenshotFlow flow = new ScreenshotFlow(driver);
	// Using By
	flow.takeScreenshot(By.id("id_of_element"), "Comment");
	// Using Web Element
	flow.takeScreenshot(webElement, "Comment");

Image
 ![Demo](https://raw.github.com/sun4android/SeleniumScreenFlow/master/view/full.png)