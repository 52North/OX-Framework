@echo off
mvn install:install-file -DgroupId=jfreechart -DartifactId=jfreechart -Dversion=1.0.0 -Dpackaging=jar -Dfile=jfreechart.jar
REM mvn install:install-file -DgroupId=units -DartifactId=units -Dversion=0.0.1 -Dpackaging=jar -Dfile=units-0.0.1.jar