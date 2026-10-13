# Jenga

A console Jenga game written in Java. Play with two or more people, or add computer players. The tower is drawn in the console using ASCII art.

## Build on Windows

Install a Windows JDK that includes `javac`, `jar`, and `jpackage` (JDK 17 or newer recommended). From Command Prompt, run:

```bat
build.bat
```

The build creates `dist\Jenga\Jenga.exe`, a console application image with its own Java runtime. Share the complete `dist\Jenga` folder; the executable needs the adjacent runtime files and is not a standalone single-file executable.

## Run from source

Compile the Java files and run the game with a JDK:

```bat
javac -encoding UTF-8 *.java
java Jenga
```

Enter the total number of players and then how many should be computer players. At least one human player is required. On a turn, enter a layer and column to remove a block. The top two layers are protected. After each move, the game checks whether the center of mass above each layer is still within that layer's remaining support; if not, the tower falls and the next player wins. If no legal move remains, the player whose turn it is loses.

The other Java classes retain the project's original tower and robot-movement code; the console game does not require robot hardware.
