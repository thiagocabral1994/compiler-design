#! /bin/sh
javac -d bin $(find src -name "*.java")
if [ -z "$1"]; then
  echo "É necessário informar o nome do arquivo de teste"
else
  java -cp bin src.main.java.Teste $1
fi