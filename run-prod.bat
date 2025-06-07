@echo off
echo Loading environment variables from .env file...

if not exist .env (
    echo .env file not found. Creating from env.example...
    call create-env.bat
    if not exist .env (
        echo Failed to create .env file. Please create it manually.
        exit /b 1
    )
)

:: Load environment variables from .env
for /f "tokens=*" %%a in ('type .env ^| findstr /v "#"') do (
    set "%%a"
)

echo Starting application in production mode...
mvn clean spring-boot:run -Dspring-boot.run.profiles=prod 