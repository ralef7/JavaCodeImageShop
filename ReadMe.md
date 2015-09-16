This was a final project for advanced java. It was an opportunity to build something a bit more complex with javaFX, FXML and
lambdas.  It has a couple small bugs (still can't really get images to resize and hold onto changes properly so I removed that
feature.) For the most part, it functions to spec. Again, I was provided base code in ImageTransform that I use for the
various image transformations.

This project requires java 8 compatibility to run.  Clone and import the project into Intellij or some other IDE of the best
viewing of code.  Run the application through MainApp.

proImageshop Readme:

Virtually all of my code is located in AdvancedImageFilters, BlurImage, ColoringImage, FullImage, ImageshopController, and BlendMode.
Also, in imageshop.fxml and styles.css.  ImageshopController also has some of Adam's code that I used with his permission.
All of the other code is borrowed directly from jaLabJava.

1. Coloring and Filter pen filers stack.  You can do several and they will
all save in addition to one another on the image.

2.  Only a single advanced, blur, sepia, or hue tone saves on the photo at a time.
So, if you blur and then try to set hue it will not stack; your blur will become a hue.

3.  Any unused code in this application is code that someone may want to make the application bigger / more complex.
For example, I left the singleton (Adam's code) even though I don't use most of it.  I also left a few setters that I don't use and a
few methods within ImageTransform that I don't really use.  This is still good code that could be useful if this thing
ever becomes more complex.  Unused variables and esoteric methods that are not used have been removed to make code more readable.

4. BlendModes should be applied carefully since they look at what colors are currently on the image before making their effects.  So,
if you begin adding lots of white, a blendmode may decide to just make your entire image black.  It's working properly, but is contrasting
colors you did not originally intend to contrast. For this reason, I added a blendmode off button.  I think it makes things more convenient.

Project specs:

Created photoshop 'lite' application proImageshop using JavaFX FXML, Java8 Lambdas, FXML and CSS
per project specs.  No streams were included per class discussion about the project.

1. user can open a .jpg or .png.  On my computer, after clicking open, there is a drop menu so you can view
either .jpg or .pngs that are available for open. You can also save file as .jpg or .png by just clicking save as and typing:
image_title.jpg  or image_title.png.

2.  I have more than 10 unique filter transforming operations.
    I have 8 coloring filters, 2 slider filters, 3 blur filters, 3 advanced filters, and 2 blend modes.  Some of these are similar, but
    I had fun messing with each individually.  Linear gradient image never really worked right, but I left it anyways since
    someone out there who someday uses my application may want to use that filter.

4. The menus at the top of my GUI are used to organize various commands per
project specs.

5.  Click Maven Projects, open Lifecycle and double click install. The jar will appear in
your target directory.  It did for me.

6.  I have gone above and beyond filters and features in a couple ways I guess. It could certainly be a
better application, but it could also be worse :-D.

7. User can make a selection and apply multiple filters to that selection. Brushes also exist that can color
the image in a variety of brush shapes.  The Sunlight brush I had trouble naming.  It kind of looks like rays of sunlight
coming down on the image like in the pictures that kids draw of the sun.

8.  User can undo and redo as many times as user wants as long as the computer in
use has the memory to hold that many FullImage objects.  I have undo / redo'ed more
than 50 times on my machine.

Comments in the code are designed to help explain things and enable grader to navigate code more quickly.

Any additional questions / issues feel free to e-mail (ralef7@gmail.com) or call (860) 546-8721

