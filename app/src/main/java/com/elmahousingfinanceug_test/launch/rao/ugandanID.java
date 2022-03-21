package com.elmahousingfinanceug_test.launch.rao;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Brian.Tuitoek on 04/08/2019.
 */

public class ugandanID {
    private JSONObject results;
    private String jibu;
    private ArrayList<String> allTags;
    private ArrayList<String> fTags;
    private ArrayList<String> fTags30;

    public ugandanID(JSONObject res) {
        allTags = new ArrayList<>();
        fTags = new ArrayList<>();
        fTags30 = new ArrayList<>();
        this.results = res;

        Iterator<String> iter = results.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            allTags.add(key);
            if (key.contains("F-"))
                fTags.add(key);
        }
    }

    public static int LevenshteinTrick(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    public boolean isUgandanID() {
        try {
            for (int i = 0; i < fTags.size(); i++) {
                String s = results.getString(fTags.get(i));
                if (LevenshteinTrick(s, "REPUBLIC OF UGANDA") > 3)
                    if (LevenshteinTrick(s, "UGANDA") > 3)
                        return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean isUgandanIDBack() {

        try {
            for (int i = fTags.size() - 3; i < fTags.size(); i++) {
                String s = results.getString(fTags.get(i)).replaceAll("\\s+", "");
                if (s.length() == 30) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public ugID getUgandanIDBack() {
        String ALLLINES = "";
        String LINE1, LINE2, LINE3;
        String FNAME = "", SNAME = "", LNAME = "", DOB = "", NIN = "", SEX = "";
        try {
            for (int i = fTags.size() - 3; i < fTags.size(); i++) {
                ALLLINES = results.getString(fTags.get(i)).replaceAll("\\s+", "") + ":" + ALLLINES;
            }
            LINE1 = ALLLINES.split(":")[2];
            LINE2 = ALLLINES.split(":")[1];
            LINE3 = ALLLINES.split(":")[0];

            //Kazi Ianze NAMES

            try {
                FNAME = LINE3.split("<<")[0];
                SNAME = LINE3.split("<<")[1].split("<")[0];
                LNAME = LINE3.split("<<")[1].split("<")[1];
            } catch (Exception e) {
               try
               {
                   FNAME = LINE3.split("<")[0];
                   SNAME = LINE3.split("<")[1];
                   LNAME = LINE3.split("<")[2];
               }catch (Exception ee){
                   FNAME = LINE3.split("<<")[0];
                   SNAME = LINE3.split("<<")[1];
                   LNAME = "";
               }
            }
            //Kazi iendelee DOB
            DOB = LINE2.substring(0, Math.min(LINE2.length(), 6));
            //Kazi iishe NIN
            NIN = LINE1.replace("<", "");
            NIN = NIN.substring(LINE1.length() - 15);
            SEX = LINE2.substring(0, Math.min(LINE2.length(), 8));
            SEX = SEX.substring(SEX.length() - 1);

            return new ugID(FNAME, SNAME, LNAME, resetDob(DOB), NIN, SEX);

        } catch (Exception e) {
            return null;
        }
    }

    private String resetDob(String dob) {
        String dayOfMonth = dob.substring(4,6);
        String year = dob.substring(0,2);
        String month = dob.substring(2,4);

        int yearInt = Integer.parseInt(year);
        if(yearInt<20)
            year = "20"+year;
        else
            year = "19"+year;
        //Date . Month . Year
        return dayOfMonth+"."+month+"."+ year;
    }

    public String getBirthDate() {
        String s;
        String AllDates = "";
        for (int i = 0; i < fTags.size(); i++) {
            try {
                s = results.getString(fTags.get(i));
                if (containsDateSeparators(s) && containsNumbers(s))
                    AllDates = AllDates + s + ":";

            } catch (Exception e) {
                return jibu;
            }
        }

        if (AllDates.split(":").length > 1) {
            String datea = AllDates.split(":")[0];
            String dateb = AllDates.split(":")[1];

            int a = Integer.parseInt(datea.substring(datea.length() - 4).trim());
            int b = Integer.parseInt(datea.substring(dateb.length() - 4).trim());

            if (a > b)
                return dateb;
            else
                return datea;
        }
        return jibu;
    }

    public String GetSurName() {
        try {
            for (int i = 0; i < fTags.size(); i++) {
                String s = results.getString(fTags.get(i));
                if (!containsNumbers(s)) {
                    if (LevenshteinTrick(s, "SURNAME") > 3 && LevenshteinTrick(s, "REPUBLIC OF UGANDA") > 3 && LevenshteinTrick(s, "NATIONAL ID CARD") > 3 && LevenshteinTrick(s, "NATIONAL") > 3 && LevenshteinTrick(s, "SEX") > 2 && LevenshteinTrick(s, "UGA") > 2 && !checkForSpace(s)) {
                        return s;
                    }
                }
            }
        } catch (Exception e) {
            return jibu;
        }
        return jibu;
    }

    public String getGivenName() {
        try {
            for (int i = 0; i < fTags.size(); i++) {
                String s = results.getString(fTags.get(i));
                if (!containsNumbers(s)) {
                    if (LevenshteinTrick(s, "SURNAME") > 3 && LevenshteinTrick(s, "GIVEN NAME") > 3 && LevenshteinTrick(s, "REPUBLIC OF UGANDA") > 3 && LevenshteinTrick(s, "NATIONAL ID CARD") > 3 && LevenshteinTrick(s, "NATIONAL") > 3 && LevenshteinTrick(s, "SEX") > 2 && LevenshteinTrick(s, "DATE OF BIRTH") > 3) {
                        return s;
                    }
                }
            }
        } catch (Exception e) {
            return jibu;
        }
        return jibu;
    }

    public String getGivenName(String fname) {
        try {
            for (int i = 0; i < fTags.size(); i++) {
                String s = results.getString(fTags.get(i));
                if (!containsNumbers(s)) {
                    if (LevenshteinTrick(s, fname.toUpperCase()) > 3 && LevenshteinTrick(s, "SURNAME") > 3 && LevenshteinTrick(s, "GIVEN NAME") > 3 && LevenshteinTrick(s, "REPUBLIC OF UGANDA") > 3 && LevenshteinTrick(s, "NATIONAL ID CARD") > 3 && LevenshteinTrick(s, "NATIONAL") > 3 && LevenshteinTrick(s, "SEX") > 2 && LevenshteinTrick(s, "DATE OF BIRTH") > 3) {
                        return s;
                    }
                }
            }
        } catch (Exception e) {
            return jibu;
        }
        return jibu;
    }

    public String getSurNameNew() {
        String nM="";
        try {
            nM = results.getString("F-3");
            if (nM.isEmpty()){
                nM = results.getString("B-3");
            }
            return nM;
        } catch (Exception e) {
            return nM;
        }
    }

    public String getGivenNameNew() {
        String nM="";
        try {
            nM = results.getString("F-5");
            if (nM.isEmpty()){
                nM = results.getString("B-5");
            }
            return nM;
        } catch (Exception e) {
            return nM;
        }
    }

    public String getDOBNew() {
        String nM="";
        try {
            nM = getBirthDate();
            String [] parts = nM.split("\\."); //dd.mm.yyyy
            return parts[2]+"-"+parts[1]+"-"+parts[0];
        } catch (Exception e) {
            return nM;
        }
    }

    private boolean checkForSpace(String s) {
        return s.contains(" ");
    }

    public String getSex() {
        String s;
        for (int i = 0; i < fTags.size(); i++) {
            try {
                s = results.getString(fTags.get(i));
                if (s.matches("[a-zA-Z]{1}") && s.length() < 2 && (s.contains("M") || s.contains("F")))
                    return s;
            } catch (Exception e) {
                return "M";
            }
        }
        return "M";
    }

    public String getNIN() {
        String s;
        for (int i = 0; i < fTags.size(); i++) {
            try {
                s = results.getString(fTags.get(i));
                if (isNoNumberAtBeginning(s) && containsNumbers(s))
                    return s.replaceAll("\\s+", "");
            } catch (Exception e) {
                return jibu;
            }
        }
        return jibu;
    }

    private boolean containsNumbers(String s) {
        return s.matches(".*\\d.*");
    }

    public boolean isNoNumberAtBeginning(String s) {
        return s.matches("^[^\\d].*");
    }

    public boolean containsDateSeparators(String s) {
        return s.matches(".*[;:'\",<.>/].*");
    }


}