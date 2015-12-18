package com.jeprolab.assets.tools.os;




import com.jeprolab.JeproLab;
import com.jeprolab.views.installer.JeproLabInstaller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JeproUnix extends JeproOperatingSystem {
    @Override
    public String getInstallDirectory(String name){
        String dir = "/usr/local/share/";
        if(!new File(dir).canWrite()){
            dir = System.getProperty("user.home");
        }
        return new File(dir, name).getPath();
    }

    public JeproOsTask[] getOsTasks(JeproLabInstaller installer){
        return new JeproOsTask[]{ new JeproScriptOsTask(installer), new JeproManPageOsTask(installer) };
    }

    public String getExtraClassPath(){
        return "";
    }


    @Override
    public void makeDirectories(String directory) throws IOException {
        File file = new File(directory);
        if(!file.exists()){
            String[] mkDirArgs = {"mkdir", "-m", "755", "-p", directory };
            execute(mkDirArgs);
        }
    }

    public void execute(String[] args) throws IOException {
        Process proc = Runtime.getRuntime().exec(args);
        proc.getInputStream().close();
        proc.getOutputStream().close();
        proc.getErrorStream().close();

        try{
            proc.waitFor();
        }catch(InterruptedException ignored){}
    }


    public class JeproScriptOsTask extends JeproOsTask {
        public JeproScriptOsTask(JeproLabInstaller installer){
            super(installer, "unix_script");
        }

        @Override
        public String getDefaultDirectory(JeproLabInstaller installer){
            String dir = "/usr/local";
            if(!new File(dir).canWrite()){
                dir = System.getProperty("user.home");
            }
            return new File(dir, "bin").getPath();
        }

        @Override
        public void perform(String installDirectory, ArrayList fileSets) throws IOException {
            if(enabled){
               makeDirectories(directory);

                //create app start script
                String appName = JeproLab.getApplicationProperties().getString("JEPROLAB_APP_NAME");
                String script = directory + File.separatorChar + appName.toLowerCase();

                //Delete existing copy
                new File(script).delete();

                //Write simple script
                FileWriter out = new FileWriter(script);
                out.write("#!/bin/sh\n");
                out.write("#\n");
                out.write("# Runs JeproLab - Web Manager Application.\n");
                out.write("#\n");
                out.write("\n");
                out.write("# Find a java installation.\n");
                out.write("if [ -z \"${JAVA_HOME}\" ]; then\n");
                out.write("	echo 'Warning: $JAVA_HOME environment variable not set! Consider setting it.'\n");
                out.write("	echo '         Attempting to locate java...'\n");
                out.write("	j=`which java 2>/dev/null`\n");
                out.write("	if [ -z \"$j\" ]; then\n");
                out.write("		echo \"Failed to locate the java virtual machine! Bailing...\"\n");
                out.write("		exit 1\n");
                out.write("	else\n");
                out.write("		echo \"Found a virtual machine at: $j...\"\n");
                out.write("		JAVA=\"$j\"\n");
                out.write("	fi\n");
                out.write("else\n");
                out.write("	JAVA=\"${JAVA_HOME}/bin/java\"\n");
                out.write("fi\n");
                out.write("\n");
                out.write("# Launch application.\n");
                out.write("\n");
                out.write("exec \"${JAVA}\" -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -jar \""
                        + installDirectory + File.separator
                        + "jeprolab.jar\" -reuseview \"$@\"\n");
                out.close();

                // Make it executable
                String[] chmodArgs = { "chmod", "755", script };
                execute(chmodArgs);
            }
        }
    }

    public class JeproManPageOsTask extends JeproOsTask{
        public JeproManPageOsTask(JeproLabInstaller installer){
            super(installer, "unix_man");
        }

        @Override
        public String getDefaultDirectory(JeproLabInstaller installer){
            String dir = "/usr/local/";
            if(!new File(dir).canWrite()){
                dir = System.getProperty("user.home");
            }
            return new File(dir, "man/man1").getPath();
        }

        @Override
        public void perform(String installerDirectory, ArrayList fileSets) throws IOException{
            if(enabled){
                makeDirectories(directory);

                String manPage = JeproLab.getApplicationProperties().getString("JEPROLAB_OS_TASK_UNIX_MAN_PAGE");

                InputStream in = getClass().getResourceAsStream("/" + manPage);
                installer.copy(in, new File(directory, manPage).getPath(), null);
            }
        }
    }
}