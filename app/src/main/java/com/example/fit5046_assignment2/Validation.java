package com.example.fit5046_assignment2;

public class Validation
{
    public static boolean isEmpty(String input)
    {
        if (input.trim().length() == 0)
            return true;
        else
            return false;
    }

    public static boolean isDigit(String input)
    {
        for (int i = 0; i < input.length(); i++)
        {
            if (!Character.isDigit(input.charAt(i)))
                return false;
        }
        return true;
    }
}
