YADrone - Yet Another Drone Framework 
=====================================

YADrone is yet another framework for controlling the AR.Drone 2. 

Changes
--------
A.o. we have made the following changes to the original distribution:
* Implemented full navdata parsing, adding listeners for every documented type of navdata.
* Implemented support for allmost all documented commands.
* Completely revamped the command queue implementation, adding support for
	- continuous movement commands (also called sticky commands)
	- proper queue synchronisation	
	- command prioritization; sticky commands are now properly mixed with other commands
* Added a Configuration manager
* Removed most of the impractical command propagation through the ARDrone class
* Added a flightplan application
* Added support for video decoding on Android based on the VLC application

Original distribution
---------------------
YADrone was originally based on the ARDroneForP5 project adding some bug fixes and features. Such as:

* Video support for JRE applications based on the Xuggler library
* Integrating the Navdata listener from the Javadrone project
* A control center that visualizes flight behaviour and sensor readings
* Fix for the command manager so that hovering works
* Android support (but no video)

YADrone is still beta. There are some known bugs.

For further information on the original distribution see http://vsis-www.informatik.uni-hamburg.de/projects/yadrone/index.html


