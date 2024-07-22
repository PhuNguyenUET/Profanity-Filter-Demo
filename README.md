# Profanity-Filter-Demo
## User Manual
1, Import the .jar file into the project
```dockerfile
        <dependency>
            <groupId>com.filter</groupId>
            <artifactId>profanity-filter</artifactId>
            <version>0.0.1</version>
            <scope>system</scope>
            <systemPath>${project.basedir}/src/main/resources/profanity-filter-0.0.1.jar</systemPath>
        </dependency>
```
2, To use the library, first create a folder that contains all the dictionaries required, the file extensions should be the language code (.vi, .en). The two sample dictionaries can be found here in the project, under "dictionaries" directory

3, The reloadAllDictionaries function must be called before the searchAndFilter function, otherwise the filter will not work