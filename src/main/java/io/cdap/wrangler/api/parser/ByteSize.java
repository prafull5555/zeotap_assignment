/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

 package io.cdap.wrangler.api.parser;

 import com.google.gson.JsonElement;
 import com.google.gson.JsonObject;
 import com.google.gson.JsonPrimitive;
 import io.cdap.wrangler.api.annotations.PublicEvolving;
 
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 
 /**
  * This class represents a byte size token with a unit (e.g., "10KB", "1.5MB").
  * It parses the token string and provides methods to retrieve the value in a canonical unit (bytes).
  */
 @PublicEvolving
 public class ByteSize implements Token {
   private static final Pattern PATTERN = Pattern.compile("^(\\d+(?:\\.\\d+)?)([bBkKmMgGtTpP][bB]?)$");
   private final String value;
   private final double numericValue;
   private final String unit;
   private final long bytes;
 
   /**
    * Constructor that takes a string representation of a byte size.
    *
    * @param value String representation of a byte size (e.g., "10KB", "1.5MB")
    * @throws IllegalArgumentException if the value is not a valid byte size format
    */
   public ByteSize(String value) {
     this.value = value;
     Matcher matcher = PATTERN.matcher(value);
     if (!matcher.matches()) {
       throw new IllegalArgumentException(
         String.format("Invalid byte size format: %s. Expected format: <number><unit> (e.g., 10KB, 1.5MB)", value));
     }
     
     this.numericValue = Double.parseDouble(matcher.group(1));
     this.unit = matcher.group(2).toUpperCase();
     this.bytes = calculateBytes();
   }
 
   /**
    * Calculates the number of bytes based on the numeric value and unit.
    *
    * @return the number of bytes
    */
   private long calculateBytes() {
     switch (unit.toUpperCase()) {
       case "B":
         return (long) numericValue;
       case "KB":
       case "K":
         return (long) (numericValue * 1024);
       case "MB":
       case "M":
         return (long) (numericValue * 1024 * 1024);
       case "GB":
       case "G":
         return (long) (numericValue * 1024 * 1024 * 1024);
       case "TB":
       case "T":
         return (long) (numericValue * 1024 * 1024 * 1024 * 1024);
       case "PB":
       case "P":
         return (long) (numericValue * 1024 * 1024 * 1024 * 1024 * 1024);
       default:
         throw new IllegalArgumentException("Unsupported byte unit: " + unit);
     }
   }
 
   /**
    * Returns the original string value of the byte size.
    *
    * @return the original string value
    */
   @Override
   public Object value() {
     return value;
   }
 
   /**
    * Returns the token type.
    *
    * @return TokenType.BYTE_SIZE
    */
   @Override
   public TokenType type() {
     return TokenType.BYTE_SIZE;
   }
 
   /**
    * Returns a JSON representation of the byte size.
    *
    * @return a JsonElement containing the byte size information
    */
   @Override
   public JsonElement toJson() {
     JsonObject object = new JsonObject();
     object.add("value", new JsonPrimitive(value));
     object.add("bytes", new JsonPrimitive(bytes));
     return object;
   }
 
   /**
    * Returns the numeric value of the byte size.
    *
    * @return the numeric value
    */
   public double getNumericValue() {
     return numericValue;
   }
 
   /**
    * Returns the unit of the byte size.
    *
    * @return the unit (e.g., "KB", "MB")
    */
   public String getUnit() {
     return unit;
   }
 
   /**
    * Returns the byte size in bytes.
    *
    * @return the number of bytes
    */
   public long getBytes() {
     return bytes;
   }
 
   /**
    * Returns the byte size in kilobytes.
    *
    * @return the number of kilobytes
    */
   public double getKilobytes() {
     return bytes / 1024.0;
   }
 
   /**
    * Returns the byte size in megabytes.
    *
    * @return the number of megabytes
    */
   public double getMegabytes() {
     return bytes / (1024.0 * 1024.0);
   }
 
   /**
    * Returns the byte size in gigabytes.
    *
    * @return the number of gigabytes
    */
   public double getGigabytes() {
     return bytes / (1024.0 * 1024.0 * 1024.0);
   }
 
   /**
    * Returns the byte size in terabytes.
    *
    * @return the number of terabytes
    */
   public double getTerabytes() {
     return bytes / (1024.0 * 1024.0 * 1024.0 * 1024.0);
   }
 
   /**
    * Returns a string representation of the byte size.
    *
    * @return the string representation
    */
   @Override
   public String toString() {
     return value;
   }
 }
 