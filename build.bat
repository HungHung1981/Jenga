@echo off
setlocal
cd /d "%~dp0"

set "PYTHON="
set "PYTHON_ARGS="
where py >nul 2>nul
if not errorlevel 1 (
    py -3 --version >nul 2>nul
    if not errorlevel 1 (
        set "PYTHON=py"
        set "PYTHON_ARGS=-3"
    )
)

if not defined PYTHON (
    where python >nul 2>nul
    if errorlevel 1 (
        echo Error: Python 3 is required. Install Python 3 and add it to PATH.
        exit /b 1
    )
    python --version >nul 2>nul
    if errorlevel 1 (
        echo Error: Python 3 is required. Install Python 3 and add it to PATH.
        exit /b 1
    )
    set "PYTHON=python"
)

if not exist ".venv-build\Scripts\python.exe" (
    %PYTHON% %PYTHON_ARGS% -m venv ".venv-build"
    if errorlevel 1 (
        echo Error: Failed to create the build environment.
        exit /b 1
    )
)

".venv-build\Scripts\python.exe" -m pip install pygame pyinstaller
if errorlevel 1 (
    echo Error: Failed to install the build dependencies.
    exit /b 1
)

".venv-build\Scripts\python.exe" -m PyInstaller --noconfirm --clean --onedir --console --name Jenga --distpath "dist" --workpath "build" --specpath "build" "newJenga.py"
if errorlevel 1 (
    echo Error: PyInstaller failed to build the executable.
    exit /b 1
)

copy /y "Undertale - Bonetrousle.mp3" "dist\Jenga\" >nul
if errorlevel 1 (
    echo Error: Failed to copy the game music into the package.
    exit /b 1
)
copy /y "scores.txt" "dist\Jenga\" >nul
if errorlevel 1 (
    echo Error: Failed to copy the leaderboard into the package.
    exit /b 1
)

echo Build complete: dist\Jenga\Jenga.exe
exit /b 0
