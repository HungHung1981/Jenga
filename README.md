# Jenga

A simple console Jenga game written in Java with code by Thomas Meijers, Jonas van Oenen, Nina läuger, Alessandra van Ree. Play with two or more people, or add computer players. The tower is drawn in the console using ASCII art. The additional code was written by Visual Studio Code and its Copilot. They are very cool.
Thanks to JC and them.

## Build on Windows

Install a Windows JDK that includes `javac`, `jar`, and `jpackage` (JDK 17 or newer recommended). From Command Prompt, run:

```bat
build.bat
```

The build creates `dist\Jenga\Jenga.exe`, a console application image with its own Java runtime, and `dist\Jenga-Windows.zip`, a portable archive of the complete application folder. Share the ZIP (or the complete `dist\Jenga` folder) and have the recipient extract it before launching `Jenga.exe`. The recipient does not need Java installed.

Do not copy or share `Jenga.exe` by itself: it relies on the adjacent `app` and `runtime` folders. The JVM is bundled with the application, but the launcher is not a standalone single-file executable.

## Run from source

Compile the Java files and run the game with a JDK:

```bat
javac -encoding UTF-8 *.java
java Jenga
```

Enter the total number of players and then how many should be computer players. At least one human player is required. On a turn, enter a layer and column to remove a block. The top two layers are protected. After each move, the game checks whether the center of mass above each layer is still within that layer's remaining support; if not, the tower falls and the next player wins. If no legal move remains, the player whose turn it is loses.

The other Java classes retain the project's original tower and robot-movement code; the console game does not require robot hardware.

### How to play ###

Unix/Linux:

Just run it with ./Jenga.exe

Windows:

You can use the Jenga.exe. Or you can put a link of "Jenga" e. g. on your desktop and open it with a double click.
