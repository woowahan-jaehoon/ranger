/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ranger.services.trino;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ranger.plugin.client.HadoopConfigHolder;
import org.apache.ranger.plugin.client.HadoopException;
import org.apache.ranger.plugin.service.RangerBaseService;
import org.apache.ranger.plugin.service.ResourceLookupContext;
import org.apache.ranger.services.trino.client.TrinoResourceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RangerServiceTrino extends RangerBaseService {
  private static final Log LOG = LogFactory.getLog(RangerServiceTrino.class);

  @Override
  public Map<String, Object> validateConfig() throws Exception {
    Map<String, Object> ret = new HashMap<String, Object>();
    String serviceName = getServiceName();

    if (LOG.isDebugEnabled()) {
      LOG.debug("RangerServiceTrino.validateConfig(): Service: " +
        serviceName);
    }

    if (configs != null) {
      try {
        if (!configs.containsKey(HadoopConfigHolder.RANGER_LOGIN_PASSWORD)) {
          configs.put(HadoopConfigHolder.RANGER_LOGIN_PASSWORD, null);
        }
        ret = TrinoResourceManager.connectionTest(serviceName, configs);
      } catch (HadoopException he) {
        LOG.error("<== RangerServiceTrino.validateConfig Error:" + he);
        throw he;
      }
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("RangerServiceTrino.validateConfig(): Response: " +
        ret);
    }
    return ret;
  }

  @Override
  public List<String> lookupResource(ResourceLookupContext context) throws Exception {

    List<String> ret 		   = new ArrayList<String>();
    String 	serviceName  	   = getServiceName();
    String	serviceType		   = getServiceType();
    Map<String,String> configs = getConfigs();
    if(LOG.isDebugEnabled()) {
      LOG.debug("==> RangerServiceHive.lookupResource Context: (" + context + ")");
    }
    if (context != null) {
      try {
        if (!configs.containsKey(HadoopConfigHolder.RANGER_LOGIN_PASSWORD)) {
          configs.put(HadoopConfigHolder.RANGER_LOGIN_PASSWORD, null);
        }
        ret  = TrinoResourceManager.getTrinoResources(serviceName, serviceType, configs,context);
      } catch (Exception e) {
        LOG.error( "<==RangerServiceTrino.lookupResource Error : " + e);
        throw e;
      }
    }
    if(LOG.isDebugEnabled()) {
      LOG.debug("<== RangerServiceTrino.lookupResource Response: (" + ret + ")");
    }
    return ret;
  }

}
