package edu.qc.seclass.replace;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main
{

public static void main(String[] args)
   {
// TODO: Empty skeleton method

String from_String;
String to_String;

int point = 0;

List<String> file_List = new ArrayList<>();
  
List<String> from_StringList = new ArrayList<>();
  
List<String> to_StringList = new ArrayList<>();
  
boolean var_b = false;
boolean var_f = false;
boolean var_l = false;
boolean var_i = false;
  
if (args.length < 3)
       {
usage();
return;
}
for (String arg : args)
       {
if (point == 0)
{
   if (arg.equals("-b"))
{
var_b = true;
continue;
}
   if (arg.equals("-f"))
{
var_f = true;
continue;
}
if (arg.equals("-l"))
{
var_l = true;
continue;
}
if (arg.equals("-i"))
{
var_i = true;
continue;
}
if (arg.equals("--"))
{
point += 1;
continue;
}
if (arg.startsWith("-") && !arg.equals("-f") && !arg.equals("-l") && !arg.equals("-b") && !arg.equals("-i"))
               {
usage();
return;
}
if (!arg.startsWith("-"))
               {
from_StringList.add(arg);
point = 2;
continue;
}
}
if (point == 1)
{
from_StringList.add(arg);
point = 2;
continue;
}
  
if (point == 2)
{
to_StringList.add(arg);
point = 3;
continue;
}
if (point == 3 && arg.equals("--"))
{
point = 4;
continue;
}
if (point == 3 && !arg.equals("--"))
{
from_StringList.add(arg);
point = 2;
continue;
}
if (point == 4)
{
file_List.add(arg);
continue;
}
}

if (file_List.size() == 0)
{
usage();
return;
}

if (from_StringList.size() == 0)
{
usage();
return;
}
if (to_StringList.size() == 0)
{
usage();
return;
}
if (from_StringList.size() != to_StringList.size())
{
usage();
return;
}

String regex = "";
if (var_i)
       {
regex = "(?i)";
}

for (int l = 0; l < from_StringList.size(); l++)
       {

   from_String = from_StringList.get(l);
if (from_String.equals(""))
           {
               usage();
               return;
           }
to_String = to_StringList.get(l);
  
for (String fileName : file_List)
           {
try
               {
if (var_b)
                   {// backup files first
Path p_path = Paths.get(fileName);
Path p_path_b = Paths.get(fileName + ".bck");
int index = fileName.lastIndexOf(File.separator);
String fileShortName = fileName.substring(index + 1);
if (Files.notExists(p_path_b))
                       {
String info;
info = new String(Files.readAllBytes(p_path), StandardCharsets.UTF_8);
FileWriter fw = new FileWriter(fileName + ".bck");
fw.write(info);
fw.close();
}
                       else
                       {
System.err.println("Not performing replace for " + fileShortName + ": Backup file already exists");
continue;
}
}

String info = new String(Files.readAllBytes(Paths.get(fileName)),
StandardCharsets.UTF_8);
FileWriter fw = new FileWriter(fileName);
// if none of first or last flag is on
if (!var_f && !var_l)
                   {
                       info = info.replaceAll(regex + from_String, to_String);
                   }
// if first or last flag is on
else
                   {
if (var_f)
                       {
                           info = info.replaceFirst(regex + from_String, to_String);
                       }
if (var_l)
                       {
String r_info = new StringBuffer(info).reverse().toString();
from_String = new StringBuffer(from_String).reverse().toString();
to_String = new StringBuffer(to_String).reverse().toString();
r_info = r_info.replaceFirst(regex + from_String, to_String);
info = new StringBuffer(r_info).reverse().toString();
}
}
fw.write(info);
fw.close();

}

catch (Exception e)
           {
int index = fileName.lastIndexOf(File.separator);
String file_SN = fileName.substring(index + 1);
System.err.println("File " + file_SN + " not found");
}
}
}
  
}

private static void usage()
   {
System.err.println("Usage: Replace [-b] [-f] [-l] [-i] <from> <to> -- " + "<filename> [<filename>]*" );
}

}