## Generate the Java:
Avro generates Java source files from the Avro schema. The Avro Maven Plugin can be used to achieve this:

``` 
mvn generate-sources
```

Locations of the Avro schema and generated Java source are defined in pom.xml.

--- 

> Alternatively, without Maven:

>```
java -jar /path/to/avro-tools-1.8.1.jar compile schema <schema file> <destination>
```
 
 