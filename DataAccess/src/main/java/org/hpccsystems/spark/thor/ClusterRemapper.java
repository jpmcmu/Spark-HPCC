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

import org.hpccsystems.spark.HpccFileException;
import org.hpccsystems.ws.client.platform.DFUFilePartInfo;

/**
 * Re-map address information for Clusters that can
 * only be reached through alias addresses.
 * Addresses are re-mapped to a range of IP addresses or to an
 * IP address and a range of ports.
 */
public abstract class ClusterRemapper {
  protected int nodes;
  protected final static int DEFAULT_CLEAR = 7100;
  protected final static int DEFAULT_SSL = 7700;

  /**
   * Constructor for common information.
   */
  protected ClusterRemapper(RemapInfo ri) throws HpccFileException{
    this.nodes = ri.getNodes();
  }
  /**
   * The optionally revised primary IP for this part.
   * @param fpi file part information
   * @return an IP address as a string
   */
  public abstract String revisePrimaryIP(DFUFilePartInfo fpi)
                        throws HpccFileException;
  /**
   * Thie optionally revised secondary IP or blank if no copy
   * @param fpi file part information
   * @return an IP address or blank string if no copy is available
   */
  public abstract String reviseSecondaryIP(DFUFilePartInfo fpi)
                        throws HpccFileException;
  /**
   * The clear communications port number or zero if clear communication
   * is not accepted
   * @param fpi file part information
   * @return the port number
   */
  public abstract int reviseClearPort(DFUFilePartInfo fpi);
  /**
   * The SSL communications port number of zero if SSL is not supported
   * @param fpi the file part information
   * @return the port number
   */
  public abstract int reviseSslPort(DFUFilePartInfo fpi);
  /**
   * Factory for making a cluster re-map.
   * @param ri the re-mapping information
   * @param fpiList a list of the file parts
   * @return a re-mapping object consistent with the provided information
   * @throws HpccFileException
   */
  public static ClusterRemapper makeMapper(RemapInfo ri,
      DFUFilePartInfo[] fpiList) throws HpccFileException {
    ClusterRemapper rslt = (ri.isNullMapper()) ? new NullRemapper(ri)
        : (ri.isPortAliasing()) ? new PortRemapper(ri)
            : new AddrRemapper(ri, fpiList);
    return rslt;
  }
}
