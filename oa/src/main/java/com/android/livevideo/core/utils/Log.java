/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.livevideo.core.utils;

/**
 * 带开关的日志类
 * @author flan
 * @date   2015年11月13日
 */
public class Log {

    public static final int VERBOSE = android.util.Log.VERBOSE;
    public static final int DEBUG 	= android.util.Log.DEBUG;
    public static final int INFO 	= android.util.Log.INFO;
    public static final int WARN 	= android.util.Log.WARN;
    public static final int ERROR 	= android.util.Log.ERROR;

    //默认日志基本为error
    private static int LEVEL = Log.ERROR;

    /**
     * 设置全局日志输出级别
     * @param level
     */
    public static void setLevel(int level){
        Log.LEVEL = level;
    }

    /**
     * 指定基本的日志是否会被输出
     * @param level
     * @return
     */
    public static boolean isLogable(int level){
        return level >= Log.LEVEL;
    }

    /**
     * Verbose log message
     * @param tag
     * @param msg
     */
    public static void v(String tag,String msg){
        if(Log.VERBOSE >= LEVEL){
            android.util.Log.v(tag, msg);
        }
    }

    /**
     * Debug log message
     * @param tag
     * @param msg
     */
    public static void d(String tag,String msg){
        if(Log.DEBUG >= LEVEL){
            android.util.Log.d(tag, msg);
        }
    }

    /**
     * Info log message
     * @param tag
     * @param msg
     */
    public static void i(String tag,String msg){
        if(Log.INFO >= LEVEL){
            android.util.Log.i(tag, msg);
        }
    }

    /**
     * WARN log message
     * @param tag
     * @param msg
     */
    public static void w(String tag,String msg){
        if(Log.WARN >= LEVEL){
            android.util.Log.w(tag, msg);
        }
    }

    /**
     * WARN log message
     * @param tag
     * @param msg
     */
    public static void e(String tag,String msg){
        if(Log.ERROR >= LEVEL){
            android.util.Log.e(tag, msg);
        }
    }

    public static void sysout() {
        android.util.Log.e("System.out", "*********************** 我被调用了 ********************");
    }
}
