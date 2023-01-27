package org.wolflink.paper.wolflinkrpc.utils;

import java.util.List;

public class StringUtil {
    public static String joinToString(List<String> str, String splitStr)
    {
        StringBuilder result = new StringBuilder();
        for (int i = 0;i < str.size();i++)
        {
            result.append(str.get(i));
            if(i != str.size()-1)result.append(splitStr);
        }
        return result.toString();
    }
}
