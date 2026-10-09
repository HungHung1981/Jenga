# Jenga
A simple game of Jenga. You can play against other human player or computer players

## Build a Windows executable

Install Python 3, then run `build.bat` from Command Prompt or by double-clicking it. The script creates an isolated `.venv-build` environment, installs the game's dependencies and PyInstaller, and builds a console executable.

The packaged game is created at `dist\Jenga\Jenga.exe`. Keep the included music and `scores.txt` files alongside the executable. The first build requires an internet connection to install the Python packages.
