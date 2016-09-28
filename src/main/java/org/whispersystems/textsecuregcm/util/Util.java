/**
 * Copyright (C) 2013 Open WhisperSystems
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.whispersystems.textsecuregcm.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class Util {

  private static final Pattern EMAIL_PATTERN = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
  private static final Pattern NUMBER_PATTERN = Pattern.compile("^\\+[0-9]{10,}");

  public static byte[] getContactToken(String number) {
    try {
      MessageDigest digest    = MessageDigest.getInstance("SHA1");
      byte[]        result    = digest.digest(number.getBytes());
      byte[]        truncated = Util.truncate(result, 10);

      return truncated;
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }

  public static String getEncodedContactToken(String number) {
    return Base64.encodeBytesWithoutPadding(getContactToken(number));
  }

  public static boolean isValidNumber(String number) {
    return NUMBER_PATTERN.matcher(number).matches();
  }

  public static boolean validateEmailAddressParts(String email) {
    boolean isValid = EMAIL_PATTERN.matcher(email).matches();

    String[] emailParts = StringUtils.split(email, "@");
    if (isValid && emailParts.length != 2) {
      isValid = false;
    }

    return isValid;
  }

  public static String encodeFormParams(Map<String, String> params) {
    try {
      StringBuffer buffer = new StringBuffer();

      for (String key : params.keySet()) {
        buffer.append(String.format("%s=%s",
                                    URLEncoder.encode(key, "UTF-8"),
                                    URLEncoder.encode(params.get(key), "UTF-8")));
        buffer.append("&");
      }

      buffer.deleteCharAt(buffer.length()-1);
      return buffer.toString();
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError(e);
    }
  }

  public static boolean isEmpty(String param) {
    return param == null || param.length() == 0;
  }

  public static byte[] combine(byte[] one, byte[] two, byte[] three, byte[] four) {
    byte[] combined = new byte[one.length + two.length + three.length + four.length];
    System.arraycopy(one, 0, combined, 0, one.length);
    System.arraycopy(two, 0, combined, one.length, two.length);
    System.arraycopy(three, 0, combined, one.length + two.length, three.length);
    System.arraycopy(four, 0, combined, one.length + two.length + three.length, four.length);

    return combined;
  }

  public static byte[] truncate(byte[] element, int length) {
    byte[] result = new byte[length];
    System.arraycopy(element, 0, result, 0, result.length);

    return result;
  }


  public static byte[][] split(byte[] input, int firstLength, int secondLength) {
    byte[][] parts = new byte[2][];

    parts[0] = new byte[firstLength];
    System.arraycopy(input, 0, parts[0], 0, firstLength);

    parts[1] = new byte[secondLength];
    System.arraycopy(input, firstLength, parts[1], 0, secondLength);

    return parts;
  }

  public static byte[][] split(byte[] input, int firstLength, int secondLength, int thirdLength, int fourthLength) {
    byte[][] parts = new byte[4][];

    parts[0] = new byte[firstLength];
    System.arraycopy(input, 0, parts[0], 0, firstLength);

    parts[1] = new byte[secondLength];
    System.arraycopy(input, firstLength, parts[1], 0, secondLength);

    parts[2] = new byte[thirdLength];
    System.arraycopy(input, firstLength + secondLength, parts[2], 0, thirdLength);

    parts[3] = new byte[fourthLength];
    System.arraycopy(input, firstLength + secondLength + thirdLength, parts[3], 0, fourthLength);

    return parts;
  }

  public static void sleep(long i) {
    try {
      Thread.sleep(i);
    } catch (InterruptedException ie) {}
  }

  public static void wait(Object object) {
    try {
      object.wait();
    } catch (InterruptedException e) {
      throw new AssertionError(e);
    }
  }

  public static int hashCode(Object... objects) {
    return Arrays.hashCode(objects);
  }

  public static long todayInMillis() {
    return TimeUnit.DAYS.toMillis(TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()));
  }
}
