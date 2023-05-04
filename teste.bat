javac Main.java

java Main || goto :error

del Main.class
cd regras
del Casa.class
del Dado.class
del Gerente.class
del Piao.class
del Tabuleiro.class
del Tester.class
del Tipo.class
cd ..
cd cores
del Cor.class
cd ..
goto :EOF

:error
exit /b %errorlevel%