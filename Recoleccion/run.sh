#!/bin/bash
clear

echo $1
echo $2

for i in $(seq $1 $2)
do
   java -cp analisis.jar ec.Evolve -file recoleccion.params $i
   cp Resultado.xls Resultado$i.xls
done


