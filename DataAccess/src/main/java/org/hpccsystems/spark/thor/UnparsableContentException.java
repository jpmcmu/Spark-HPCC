/*******************************************************************************
 *     HPCC SYSTEMS software Copyright (C) 2018 HPCC Systems®.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *******************************************************************************/
package org.hpccsystems.spark.thor;

/**
 * The data block from the HPCC Cluster held unexpected values
 * and could not be parsed successfully.
 */
public class UnparsableContentException extends Exception {
  private static final long serialVersionUID = 1l;
  /**
   * Empty constructor
   */
  public UnparsableContentException() {
  }

  /**
   * @param message
   */
  public UnparsableContentException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public UnparsableContentException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public UnparsableContentException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public UnparsableContentException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
