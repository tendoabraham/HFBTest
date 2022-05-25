package com.elmahousingfinanceug.recursiveClasses;

import java.util.Random;

public class CreateRandomKey {

    public String GetKey()
    {
        Random number = new Random();
        float f = number.nextFloat();
        number.setSeed(System.currentTimeMillis());
        String RandomNumber = "" + (f*100.0f) % 100;

        RandomNumber = RandomNumber.replace('.', '5');
        RandomNumber = RandomNumber.replace('0', '2');
        RandomNumber = RandomNumber + RandomNumber.substring(3,5) + RandomNumber + RandomNumber.substring(1,5);

        String A = RandomNumber.substring(1,3);
        String B = RandomNumber.substring(6,8);
        String C = RandomNumber.substring(10,12);
        String D = RandomNumber.substring(12,14);

        while (Integer.parseInt(A) > 26)
            A = Integer.toString(Integer.parseInt(A) - 26);

        while (Integer.parseInt(B) > 26)
            B = Integer.toString(Integer.parseInt(B) - 26);

        while (Integer.parseInt(C) > 26)
            C = Integer.toString(Integer.parseInt(C) - 26);

        while (Integer.parseInt(D) > 26)
            D = Integer.toString(Integer.parseInt(D) - 26);

        int intA = (Integer.parseInt(A) + 65);
        int intB = (Integer.parseInt(B) + 65);
        int intC = (Integer.parseInt(C) + 65);
        int intD = (Integer.parseInt(D) + 65);

        A = Character.toString((char)intA);
        B = Character.toString((char)intB);
        C = Character.toString((char)intC);
        D = Character.toString((char)intD);

        RandomNumber = A + B + RandomNumber.substring(1,2) + RandomNumber.substring(5,6) + C + RandomNumber.substring(7,8) + RandomNumber.substring(9,10) + D;
        return RandomNumber;
    }
}
