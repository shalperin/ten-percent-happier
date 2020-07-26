
# Sam Shimi Halperin: Demos and UI Clones
## Project "Redwood"

Intro:
======
There is a tradition in Fine Arts education of copying master works in order to get the muscle memory for good technique.  Android isn't painting, but it has been useful to more or less clone UI elements that
I like into these little demo apps.  It has been helpful to implement other people's designs, which bounds the project to a successful clone, rather than having open-ended original designs.  Fastidiousness about pixel-perfect interpretations quickly becomes gratuitous compulsivity when not bounded by a "finished" state.

To Build:
=========
It should be straightforward to just clone the project and open it in Android Studio.  

You will get "Code 10, Message 10" in Firebase until the debug SHA1 key
of the current instance of Android Studio is added to the Firebase project.

Project Structure:
==================
There are 2 modules in this project
+ _Library_: I moved a lot of code into a reuseable UI Library.
+ _MeditationChallenge_: The feature you see above.  This is a self-contained fragment that you could drop into a larger app.

Architecture:
=============
The organizing principle here are _component_ Fragments built with  Android MVVMs. Components talk to each other via ViewModels, which are reactively bound to their frontend code.






