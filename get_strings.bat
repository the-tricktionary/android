@ECHO off
SETLOCAL

SET url=https://us-central1-project-5641153190345267944.cloudfunctions.net/i18nApi/

:flags
FOR %%A IN (%*) DO (
    IF "%%A"=="/nocurl" (
        ECHO Falling to BITS
        GOTO :bitsadmin
    )
)

:curl
ECHO Checking if curl is installed
WHERE curl
IF %ERRORLEVEL% NEQ 0 (
    ECHO Curl not detected, falling to BITS
    GOTO :bitsadmin
)

ECHO downloading list of enabled langs
FOR /F "tokens=* USEBACKQ" %%F IN (`curl "%url%"`) DO (
    SET langs=%%F
)

ECHO downloading langs
curl --create-dirs -v -H 'Cache-Control: no-cache' -o "app\src\main\res\values-#1\strings.xml" "%url%{%langs%}"

ECHO Moving en to base
MOVE /y "app\src\main\res\values-en\strings.xml" "app\src\main\res\values\strings.xml"
RD "app\src\main\res\values-en"

ECHO all langs downloaded
GOTO :EOF

:bitsadmin
ECHO downloading list of enabled langs
bitsadmin /TRANSFER "Langs" %url% %cd%\temp_langs
SET /p langs=<temp_langs
ECHO %langs%

FOR %%F in ("%langs:,=" "%") do (
    ECHO Downloading %%F
    IF %%F=="en" (
        ECHO %url%%%~F
        bitsadmin /TRANSFER %%F %url%%%F %cd%\app\src\main\res\values\strings.xml
    ) ELSE (
        IF NOT EXIST %cd%\app\src\main\res\values-%%F mkdir %cd%\app\src\main\res\values-%%F
        bitsadmin /TRANSFER %%F %url%%%F %cd%\app\src\main\res\values-%%F\strings.xml
    )
)

ECHO all langs downloaded
DEL %cd%\temp_langs
GOTO :EOF
