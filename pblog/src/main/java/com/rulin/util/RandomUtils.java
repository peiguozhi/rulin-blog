package com.rulin.util;

import java.util.Random;

/**
 * 随机数工具
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
public class RandomUtils {

    private static final Random RANDOM = new Random();

    private static final String LETTER = "abcdefghijklmnopqrstuvwxyz";

    /**
     * 生成指定位数的随机小写字母字符串
     *
     * @param number
     * @return
     */
    public static String generationCapital(Integer number) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] c = LETTER.toCharArray();
        for (int i = 0; i < number; i++) {
            stringBuilder.append(c[RANDOM.nextInt(c.length)]);
        }
        return stringBuilder.toString();
    }

    public static Integer generationOneNumber(Integer number) {
        return RANDOM.nextInt(number) + 1;
    }

    /**
     * 生成指定位数的随机数字字符串
     *
     * @param number
     * @return
     */
    public static String generationNumber(Integer number) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < number; i++) {
            stringBuilder.append(RANDOM.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
