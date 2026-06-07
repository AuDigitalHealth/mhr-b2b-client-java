@echo off
setlocal
set "P="
set "WS="
if /i "%~1"=="shaded" set "P=-Pfat-jar "
if /i "%~1"=="-shaded" set "P=-Pfat-jar "
if /i "%~1"=="--shaded" set "P=-Pfat-jar "
if /i "%~1"=="wsimport" set "WS=-Pwsimport "
if /i "%~1"=="-wsimport" set "WS=-Pwsimport "
if /i "%~1"=="--wsimport" set "WS=-Pwsimport "
if not "%~1"=="" if not defined P if not defined WS (
    echo Unknown argument: %~1 >&2
    echo Optional: shaded ^| wsimport >&2
    exit /b 2
)
if defined P if defined WS (
    echo Use either shaded or wsimport, not both. >&2
    exit /b 2
)
pushd "%~dp0" || exit /b 1
set "SARGS="
if defined MVN_SETTINGS set "SARGS=-s %MVN_SETTINGS% "
if defined WS (
    echo Building mhr-b2b-client JAR (with in-repo wsimport validation)
) else if defined P (
    echo Building mhr-b2b-client FAT/UBER JAR
) else (
    echo Building mhr-b2b-client JAR
)
mvn -B %SARGS%%WS%%P%-Dgpg.skip=true clean verify
set "EXIT=%ERRORLEVEL%"
popd
exit /b %EXIT%
