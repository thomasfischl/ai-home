pscp -pw xyz ./target/aihome.controller.pi-0.0.1-jar-with-dependencies.jar pi@192.168.1.11:/home/pi/aihome/aihome.test.jar

plink -pw xyz pi@192.168.1.11 sudo java -jar /home/pi/aihome/aihome.test.jar
pause
