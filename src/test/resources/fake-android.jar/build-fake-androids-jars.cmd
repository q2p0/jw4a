@echo off
SETLOCAL parent=%~dp0
CLS

CD platforms

FOR /D %%I IN (%parent%platforms\*) DO (

	CD %%I

		echo Generating fake %%~nI jar file
		echo ==================================

		mkdir build
		dir /s /B *.java > sources.txt
		javac @sources.txt -d build
		jar cvf android.jar build/*
		del sources.txt
		rd /Q /S build

		echo.

	CD ..
)

CD ..
