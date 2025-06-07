@echo off
echo Creating .env file from env.example...

if exist .env (
    echo .env file already exists. Do you want to overwrite it? [Y/N]
    set /p confirm=
    if /i not "%confirm%"=="Y" (
        echo Operation cancelled.
        exit /b
    )
)

copy env.example .env
echo .env file created successfully.
echo Please edit the .env file with your production values.
echo Especially change these security-sensitive values:
echo - DB_PASSWORD
echo - JWT_SECRET
echo - REMEMBER_ME_KEY
echo - SSL_KEYSTORE_PASSWORD
echo - CORS_ALLOWED_ORIGINS 