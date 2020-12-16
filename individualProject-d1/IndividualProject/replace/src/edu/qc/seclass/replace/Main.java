package edu.qc.seclass.replace;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String replaceFrom = "";
        String replaceTo = "";
        int point = 0;
        List<String> fileList = new ArrayList<>();
        List<String> replaceFromList = new ArrayList<>();
        List<String> replaceToList = new ArrayList<>();
        boolean bFlag = false;
        boolean fFlag = false;
        boolean lFlag = false;
        boolean iFlag = false;
        boolean doubleDash=false;

        /*
        * if the argument is less than 3, then error in the argument and return usage
        * argument should look like
        * "StringFrom" "StringTo" "--" "file1" "file2" without OP option or,
        * "-i" "StringFrom" "StringTo" "--" "file1" "file2" with OP option
        *
        */
        if (args.length < 3) {
            usage();
            return;
        }
        /*
        *
        * iterate through all the argument and flag the OPT option
        *
        * "-i", "-f" "Howdy", "Hello", "--", inputFile1.getPath(), inputFile2.getPath(), inputFile3.getPath()
        * ---------   -----    -----    --    --------------------------------------------------------------
        * point->0      1        2       3                    4
        *
        */
        for (int i=0;i< args.length;i++) {
            /*if(args[i].equalsIgnoreCase("replace")){
                usage();
                break;
            }*/
            /*if (!argument.startsWith("-")) {
                replaceFromList.add(argument);
                point = 2;
                continue;
            }*/

            if(args[i].equals("--"))
                doubleDash=true;

            if (point == 0) {
                if (!args[i].startsWith("-")) {
                    replaceFromList.add(args[i]);
                    point+=2;
                    continue;
                }

                //OPT option
                if (args[i].equals("-b")) {
                    bFlag = true;
                    continue;
                }
                if (args[i].equals("-f")) {
                    fFlag = true;
                    continue;
                }
                if (args[i].equals("-l")) {
                    lFlag = true;
                    continue;
                }
                if (args[i].equals("-i")) {
                    iFlag = true;
                    continue;
                }
                if (doubleDash) {
                    point++;
                    continue;
                }
                /*
                * wrong syntax
                */
                if (args[i].startsWith("-")) {
                    if (!args[i].equals("-f")) {
                        usage();
                        break;
                    }
                    if (!args[i].equals("-l")) {
                        usage();
                        break;
                    }
                    if (!args[i].equals("-b")) {
                        usage();
                        break;
                    }
                    if (!args[i].equals("-i")) {
                        usage();
                        break;
                    }

                }


            }

            if (point == 1) {
                replaceFromList.add(args[i]);
                point ++;
                continue;
            }

            if (point == 2) {
                replaceToList.add(args[i]);
                point ++;
                continue;
            }
            if (point == 3 && doubleDash) {
                point ++;
                continue;
            }
            if (point == 3 && !doubleDash) {
                replaceFromList.add(args[i]);
                point = 2;
                //usage();
                continue;
            }
            if (point == 4) {
                fileList.add(args[i]);
                continue;
            }
        }
        //If no files provided, return usage()
        if (fileList.isEmpty()) {
            // System.out.print(fileList.size());
            usage();
            return;
        }

        if (replaceFromList.size() == 0 || replaceFromList.size() > 1) {
            //System.out.print(replaceFromList.size());
            usage();
            return;
        }
        if (replaceToList.size() == 0 || replaceToList.size() > 1) {
            //System.err.print(replaceToList.size());
            usage();
            return;
        }
        /*
        * replace one string with another.
        * If attempt to replace one string with more string, it will return usage() and vice versa
        */
        if (replaceFromList.size() != replaceToList.size()) {
            usage();
            return;
        }
        //
        replaceFrom = replaceFromList.get(0);
        replaceTo = replaceToList.get(0);

        /*
        * empty string cannot be replaced with the replaceTo String
        */
        if (replaceFrom.length() == 0) {
            usage();
            return;
        }

        for (int i=0;i<fileList.size();i++) {
            try {
                //if bFlag is true, then create a backup file before replacement
                if (bFlag) {
                    backupfile(fileList.get(i));
                }
                //read the data from the file
                String data =Files.readString(Path.of(fileList.get(i)));

                // if none of first or last flag is on replace every string from replaceFrom to replaceTo string
                if (!fFlag && !lFlag && !iFlag) {
                    data = data.replaceAll( replaceFrom, replaceTo);
                }
                //replace all case insensitive string
                if (!fFlag && !lFlag && iFlag) {
                    data = data.replaceAll("(?i)" + replaceFrom, replaceTo);
                }
                // if first or last flag is on
                else {
                    if (fFlag) {
                        data = firstOccurance(data, replaceFrom, replaceTo, iFlag);
                    }
                    if (lFlag) {
                        data = lastOccurance(data, replaceFrom, replaceTo, iFlag);
                    }
                }
                Files.writeString(Path.of(fileList.get(i)), data);


            } catch (IOException e) {
                File f = new File(fileList.get(i));
                System.err.println("File " +f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("\\")+1)  + " not found");
            }
        }
    }
    //
    private static void backupfile(String fileName) {
        String info= null;
        try {
            info = Files.readString(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Files.writeString(Path.of(fileName + ".bck"), info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String lastOccurance(String info, String replaceFrom, String replaceTo, boolean iflag) {

        //if the iflag is true, then replace case insensitive replaceFrom string to replaceTo String
        if(iflag) {
            String str = reverse(info);
            replaceFrom = reverse(replaceFrom);
            replaceTo = reverse(replaceTo);
            str = str.replaceFirst("(?i)" + replaceFrom, replaceTo);
            info = reverse(str);
        }
        else{
            String str = reverse(info);
            replaceFrom = reverse(replaceFrom);
            replaceTo = reverse(replaceTo);
            str = str.replaceFirst( replaceFrom, replaceTo);
            info = reverse(str);
        }
        return info;
    }

    private static String reverse(String input){
        char[] in = input.toCharArray();
        int begin=0;
        int end=in.length-1;
        char temp;
        while(end>begin){
            temp = in[begin];
            in[begin]=in[end];
            in[end] = temp;
            end--;
            begin++;
        }
        return new String(in);
    }

    private static String firstOccurance(String info, String replaceFrom, String replaceTo,boolean iflag) {
        if(iflag)
            return info.replaceFirst("(?i)" + replaceFrom, replaceTo);
        else
            return info.replaceFirst( replaceFrom, replaceTo);
    }

    private static void usage() {
        System.err.println("Usage: Replace [-b] [-f] [-l] [-i] <from> <to> -- " + "<filename> [<filename>]*");
    }

}