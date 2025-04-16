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
  * This class represents a time duration token with a unit (e.g., "10ms", "1.5s").
  * It parses the token string and provides methods to retrieve the value in a canonical unit (nanoseconds).
  */
 @PublicEvolving
 public class TimeDuration implements Token {
   private static final Pattern PATTERN = Pattern.compile("^(\\d+(?:\\.\\d+)?)([nNuUmMsShHdD][sS]?)$");
   private final String value;
   private final double numericValue;
   private final String unit;
   private final long nanoseconds;
 
   /**
    * Constructor that takes a string representation of a time duration.
    *
    * @param value String representation of a time duration (e.g., "10ms", "1.5s")
    * @throws IllegalArgumentException if the value is not a valid time duration format
    */
   public TimeDuration(String value) {
     this.value = value;
     Matcher matcher = PATTERN.matcher(value);
     if (!matcher.matches()) {
       throw new IllegalArgumentException(
         String.format("Invalid time duration format: %s. Expected format: <number><unit> (e.g., 10ms, 1.5s)", value));
     }
     
     this.numericValue = Double.parseDouble(matcher.group(1));
     this.unit = matcher.group(2).toLowerCase();
     this.nanoseconds = calculateNanoseconds();
   }
 
   /**
    * Calculates the number of nanoseconds based on the numeric value and unit.
    *
    * @return the number of nanoseconds
    */
   private long calculateNanoseconds() {
     switch (unit.toLowerCase()) {
       case "ns":
         return (long) numericValue;
       case "us":
       case "µs":
         return (long) (numericValue * 1000);
       case "ms":
         return (long) (numericValue * 1000 * 1000);
       case "s":
         return (long) (numericValue * 1000 * 1000 * 1000);
       case "m":
         return (long) (numericValue * 60 * 1000 * 1000 * 1000);
       case "h":
         return (long) (numericValue * 60 * 60 * 1000 * 1000 * 1000);
       case "d":
         return (long) (numericValue * 24 * 60 * 60 * 1000 * 1000 * 1000);
       default:
         throw new IllegalArgumentException("Unsupported time unit: " + unit);
     }
   }
 
   /**
    * Returns the original string value of the time duration.
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
    * @return TokenType.TIME_DURATION
    */
   @Override
   public TokenType type() {
     return TokenType.TIME_DURATION;
   }
 
   /**
    * Returns a JSON representation of the time duration.
    *
    * @return a JsonElement containing the time duration information
    */
   @Override
   public JsonElement toJson() {
     JsonObject object = new JsonObject();
     object.add("value", new JsonPrimitive(value));
     object.add("nanoseconds", new JsonPrimitive(nanoseconds));
     return object;
   }
 
   /**
    * Returns the numeric value of the time duration.
    *
    * @return the numeric value
    */
   public double getNumericValue() {
     return numericValue;
   }
 
   /**
    * Returns the unit of the time duration.
    *
    * @return the unit (e.g., "ms", "s")
    */
   public String getUnit() {
     return unit;
   }
 
   /**
    * Returns the time duration in nanoseconds.
    *
    * @return the number of nanoseconds
    */
   public long getNanoseconds() {
     return nanoseconds;
   }
 
   /**
    * Returns the time duration in microseconds.
    *
    * @return the number of microseconds
    */
   public double getMicroseconds() {
     return nanoseconds / 1000.0;
   }
 
   /**
    * Returns the time duration in milliseconds.
    *
    * @return the number of milliseconds
    */
   public double getMilliseconds() {
     return nanoseconds / (1000.0 * 1000.0);
   }
 
   /**
    * Returns the time duration in seconds.
    *
    * @return the number of seconds
    */
   public double getSeconds() {
     return nanoseconds / (1000.0 * 1000.0 * 1000.0);
   }
 
   /**
    * Returns the time duration in minutes.
    *
    * @return the number of minutes
    */
   public double getMinutes() {
     return nanoseconds / (60.0 * 1000.0 * 1000.0 * 1000.0);
   }
 
   /**
    * Returns the time duration in hours.
    *
    * @return the number of hours
    */
   public double getHours() {
     return nanoseconds / (60.0 * 60.0 * 1000.0 * 1000.0 * 1000.0);
   }
 
   /**
    * Returns the time duration in days.
    *
    * @return the number of days
    */
   public double getDays() {
     return nanoseconds / (24.0 * 60.0 * 60.0 * 1000.0 * 1000.0 * 1000.0);
   }
 
   /**
    * Returns a string representation of the time duration.
    *
    * @return the string representation
    */
   @Override
   public String toString() {
     return value;
   }
 }
 