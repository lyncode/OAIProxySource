/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 
 */
package com.lyncode.oai.proxy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 
 * @author Lyncode Development Team <development@lyncode.com>
 */
public class DateUtils
{

    private static Logger log = LogManager.getLogger(DateUtils.class);

    public static String formatToSolr(Date date)
    {
        // 2008-01-01T00:00:00Z
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String ret = format.format(date);
        return ret;
    }

    public static Date parseDate(String date)
    {
        // 2008-01-01T00:00:00Z
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        // format.setTimeZone(TimeZone.getTimeZone("ZULU"));
        Date ret;
        try
        {
            ret = format.parse(date);
            return ret;
        }
        catch (ParseException e)
        {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault());
            try
            {
                return format.parse(date);
            }
            catch (ParseException e1)
            {
                format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try
                {
                    return format.parse(date);
                }
                catch (ParseException e2)
                {
                    format = new SimpleDateFormat("yyyy-MM",
                            Locale.getDefault());
                    try
                    {
                        return format.parse(date);
                    }
                    catch (ParseException e3)
                    {
                        format = new SimpleDateFormat("yyyy",
                                Locale.getDefault());
                        try
                        {
                            return format.parse(date);
                        }
                        catch (ParseException e4)
                        {
                            log.error(e4.getMessage(), e);
                        }
                    }
                }
            }
        }
        return new Date();
    }

    public static Date parseFromSolrDate(String date)
    {
        // 2008-01-01T00:00:00Z
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        // format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date ret;
        try
        {
            ret = format.parse(date);
            return ret;
        }
        catch (ParseException e)
        {
            log.error(e.getMessage(), e);
        }
        return new Date();
    }
}