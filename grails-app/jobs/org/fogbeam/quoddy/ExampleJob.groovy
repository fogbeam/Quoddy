package org.fogbeam.quoddy

class ExampleJob 
{
    static triggers = 
    {
      simple repeatInterval: 65000L // execute job once in 5 seconds
    }

    def execute() 
    {
        // execute job
        println "ExampleJob firing...";
    }
}
