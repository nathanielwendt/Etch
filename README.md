# Etch
Android Energy Profiling Automation on Unix based systems using Trepn Profiler

##Installation##

1. Obtain the Etch source files.

   Go to the root of your project (module-level).

   If your Android project is represented within a git repository, use <code> git submodule add [url-to-this-repo]

   If your Android project does not have a git repository, simply use <code> git clone [url-to-this-repo]

2. In Android Studio, right click on the Etch directory and select Make module ...

3. In the project settings.gradle file, add: <code> ':etch'</code>

4. In your project's module (typically named 'app'), edit build.gradle to include the following lines: <code> dependencies {
    compile project(':etch')
} </code>

5. Sync with Gradle and Etch should be successfully installed!

##Setup##

####Setting up Trepn####

Install the Trepn app on your target device.  Note that Trepn only works for devices using the Snapdragon processor.  Open Trepn and under "Settings" (wrench icon), select the "Data Points" tab.  Select the items you want to profile, and <b>make sure that Application State is checked. </b>

####Setting up Etch####

Under the Etch module, open com.ut.mpc.etch.Setup.  This is where you can tweak settings for how Etch operates.  <b>Make sure to change EVAL_DIR to the package that will be containing your eval files </b> (typically com.yourpackage.evals).  Note the other settings that are available.

####Writing your First Eval####

In the eval directory stipulated in the previous section, create a new Class that implements the Eval interface. In the 'example' folder of this repo there is a sample test that can help get you started.

##Using Etch##

####Evaluation Plan####
Etch uses an evaluation plan declared in the Etch/scripts/eval_config.json file.  The file provided by this repo serves as a sample that interfaces with the example/EtchTest eval file.

Note that the first key name is 'eval'.  This is the only key that is required and should match the class name of your Eval file located within your EVAL_DIR.  The rest of the keys are completely optional as you can define your own schema as it suits your testing needs.  The example includes a 'dbName' key that is used by the profiler to name the database files as well as the resulting csv files.  The 'iterations' key demonstrates the ability to pass a loop iteration bound value.

####Evals####
Each Eval file represents a distinct test. The following are descriptions of the various components of an Eval file:

<li> Stabilizer: Function passed to the profiler marker to ensure the jvm is warmped up before computing values </li>


<li> Profiler: start/stop profiling and pass a name to the db file and resulting csv file name </li>

<li> Markers: Markers can be started and stopped at any time to mark significant portions of your test.  For example, you may want to profile different portions of your test such as the compression portion and transmission portion.  The markers facilitate ordering this information for analysis in the csv file. The third argument to startMark is the name of the marker that will be referred to in the output csv file.  Note that output data will only be included under a marker, so even if you don't need distinct marking within a test, <b>you must use at least one marker</b>.

####Running Your First Evaluation Plan####
*Note: make sure your code is deployed to your device*

In the Etch/scripts directory:

1. <code> python run_eval.py </code>
2. <code> bash extract.sh </code>

CSV files will be contained within the scripts directory matching the depicted db names.  A backup directory, 'backup' is also created containing the raw Trepn sqlite databases for extensive querying/analysis. 

CSV Files will be in the form:
sensor_name,timestamp,value,delta,marker_duration,marker_name

## Notes to the Scaloid Users 

In the case the test classes are not referenced in the Scaloid project, Scaloid elimimates the test classes during the execution of proguard. 

To prevent this, Scaloid's proguard configuration should include 

    -keep class <package name>.<class name> {
        <init>(...);
    }
    
or     

    -keep class <package name>.<class name> {
        public protected *;
    }

where <package name>.<class name> is the name of the class that is under test. 
