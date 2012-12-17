## Selenium Screen Flow ##
Designed to take screenshot of given part of the page 
during test execution and creating flow.

<b>Algorithm:</b>

1. Outline web element
2. Find  web element parent of given type (table, div, ...)
3. Crop image to parent or web element (if no parent was found)
4. Crop image to tile that will be placed in flow (best effort to preserve web element)
5. Create flow of titled images using template

<b>Code example:</b>

	// Initialize flow
	ScreenshotFlow flow = new ScreenshotFlow(driver);
	// Using By
	flow.takeScreenshot(By.id("id_of_element"), "Comment");
	// Using Web Element
	flow.takeScreenshot(webElement, "Comment");
	// Save flow to file using Grid template
	flow.toFile(new File(OUTPUT_PATH), new Grid(), WIDTH, HEIGHT);

<b>Image example:</b>
 ![Demo](https://raw.github.com/sun4android/SeleniumScreenFlow/master/view/full.png)