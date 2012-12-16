## Selenium Screen Flow ##
Designed to take screenshot of given part of the page 
during test execution and creating flow out of them.

1. outlining web elements
2. finding  web elements parents of given type (table, div, ...) 
3. cropping screenshots to parent or web element
4. cropping screenshots to flow with best effort to preserve web element
5. creating flow of titled screenshots using template

<b>Code example</b>

	// Initialize flow
	ScreenshotFlow flow = new ScreenshotFlow(driver);
	// Using By
	flow.takeScreenshot(By.id("id_of_element"), "Comment");
	// Using Web Element
	flow.takeScreenshot(webElement, "Comment");
	// Save flow to file using Grid template
	flow.toFile(new File(OUTPUT_PATH), new Grid(), 1920, 1080);

<b>Image example</b>
 ![Demo](https://raw.github.com/sun4android/SeleniumScreenFlow/master/view/full.png)