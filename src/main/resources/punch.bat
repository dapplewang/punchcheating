@ECHO OFF

@ECHO Punch Cheating V1.0
FOR %%F IN (lib\*.jar) DO call :addcp %%F
goto extlibe
:addcp
SET CLASSPATH=%CLASSPATH%;%1
goto :eof
:extlibe
SET CLASSPATH=%CLASSPATH%;
SET CLASSPATH

SET ReqVer=1.7
FOR /F "tokens=1,2 delims=." %%A IN ("%ReqVer%") DO (
    SET ReqMajorVer=%%A
    SET ReqMinorVer=%%B
)

FOR /F "tokens=3" %%A IN ('JAVA -version 2^>^&1 ^| FIND /I "java version"') DO (
    FOR /F "tokens=1,2 delims=." %%B IN ("%%~A") DO (
        SET JavaMajorVer=%%B
        SET JavaMinorVer=%%C
    )
)
IF %ReqMajorVer% GTR %JavaMajorVer% (
    ECHO Please install Java %ReqVer% or later
) ELSE (
    IF %ReqMinorVer% GTR %JavaMinorVer% (
        ECHO Please install Java %ReqVer% or later
    ) ELSE (
        ECHO Java %ReqVer% or later is already installed
        java -jar lib/punch.jar
    )
)
